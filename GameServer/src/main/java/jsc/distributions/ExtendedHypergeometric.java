package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;
import jsc.util.Maths;

public class ExtendedHypergeometric
        extends AbstractDiscreteDistribution {
    private int n1;
    private int n2;
    private int m;
    private int mode;
    private int ll;
    private int uu;
    private double mean;
    private double variance;
    private double psi;
    private int lStar;
    private int uStar;
    private double[] prob;

    public ExtendedHypergeometric(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2) {
        super(Math.max(0, paramInt3 - paramInt2), Math.min(paramInt1, paramInt3));
        setParameters(paramInt1, paramInt2, paramInt3, paramDouble1, paramDouble2);
    }

    public ExtendedHypergeometric(int paramInt1, int paramInt2, int paramInt3, double paramDouble) {
        this(paramInt1, paramInt2, paramInt3, paramDouble, 0.0D);
    }

    public double cdf(double paramDouble) {
        if (paramDouble < this.ll || paramDouble > this.uu)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (paramDouble < this.lStar) return 0.0D;
        if (paramDouble >= this.uStar) return 1.0D;
        double d = 0.0D;
        long l = this.lStar;
        while (l <= paramDouble) {

            d += this.prob[(int) (l - this.lStar)];
            l++;
        }

        return d;
    }

    public int getEffectiveMaxX() {
        return this.uStar;
    }

    public int getEffectiveMinX() {
        return this.lStar;
    }

    public double getMaximumPdf() {
        double d = 0.0D;
        for (byte b = 0; b < this.prob.length; b++) {

            if (this.prob[b] > d) d = this.prob[b];
        }
        return d;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability.");
        if (paramDouble == 0.0D) return Math.max(this.lStar - 1, this.ll);
        if (paramDouble == 1.0D) return this.uStar;
        long l = this.lStar;

        double d = this.prob[0];
        while (l < this.uStar && d < paramDouble) {
            l++;
            d += this.prob[(int) (l - this.lStar)];
        }
        return l;
    }

    public double mean() {
        return this.mean;
    }

    public int mode() {
        return this.mode;
    }

    public double pdf(double paramDouble) {
        if (paramDouble < this.ll || paramDouble > this.uu)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (paramDouble < this.lStar || paramDouble > this.uStar) return 0.0D;
        return this.prob[(int) paramDouble - this.lStar];
    }

    public double random() {
        double d1 = 0.0D;
        double d2 = this.rand.nextDouble();
        long l = this.lStar;
        for (byte b = 0; b < this.prob.length; b++) {

            d1 += this.prob[b];
            if (d2 < d1) return l;
            l++;
        }
        return this.uStar;
    }

    private double rFunction(int paramInt) {
        double d = paramInt;
        return (this.n1 - d + 1.0D) * (this.m - d + 1.0D) / d / ((this.n2 - this.m) + d) * this.psi;
    }

    public void setParameters(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2) {
        this.ll = Math.max(0, paramInt3 - paramInt2);
        this.uu = Math.min(paramInt1, paramInt3);
        int i = paramInt1 + paramInt2;
        if (paramInt1 < 0 || paramInt2 < 0 || paramInt3 < 0 || paramInt3 > i || paramDouble1 <= 0.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.minValue = this.ll;
        this.maxValue = this.uu;
        this.n1 = paramInt1;
        this.n2 = paramInt2;
        this.m = paramInt3;
        this.psi = paramDouble1;

        double d1 = paramDouble1 - 1.0D;
        double d2 = -((paramInt3 + paramInt1 + 2) * paramDouble1 + paramInt2 - paramInt3);
        double d3 = paramDouble1 * (paramInt1 + 1) * (paramInt3 + 1);
        double d4 = d2 + ((d2 < 0.0D) ? -1 : true) * Math.sqrt(d2 * d2 - 4.0D * d1 * d3);
        d4 = -d4 / 2.0D;
        this.mode = (int) Maths.truncate(d3 / d4);
        if (this.uu < this.mode || this.mode < this.ll) {
            this.mode = (int) Maths.truncate(d4 / d1);
        }

        double[] arrayOfDouble = new double[this.uu + 2];

        arrayOfDouble[this.mode] = 1.0D;
        double d5 = 0.1D * paramDouble2;
        int j;
        for (j = this.mode + 1; j <= this.uu; j++) {

            double d = rFunction(j);
            arrayOfDouble[j] = arrayOfDouble[j - 1] * d;
            if (arrayOfDouble[j] <= d5 && d < 0.8333333333333333D)
                break;
        }
        this.uStar = Math.min(j, this.uu);
        for (j = this.mode - 1; j >= this.ll; j--) {

            double d = rFunction(j + 1);
            arrayOfDouble[j] = arrayOfDouble[j + 1] / d;
            if (arrayOfDouble[j] <= d5 && d > 1.2D)
                break;
        }
        this.lStar = Math.max(this.ll, j);

        int m = this.uStar - this.lStar + 1;
        this.prob = new double[m];
        int k;
        for (j = this.lStar, k = 0; j <= this.uStar; ) {
            this.prob[k] = arrayOfDouble[j];
            j++;
            k++;
        }

        double d6 = 0.0D;

        for (j = 0; j < m; ) {
            d6 += this.prob[j];
            j++;
        }
        if (d6 <= 0.0D) {
            throw new IllegalArgumentException("Zero probabilities.");
        }
        for (j = 0; j < m; ) {
            this.prob[j] = this.prob[j] / d6;
            j++;
        }

        this.mean = 0.0D;
        this.variance = 0.0D;
        for (j = 0, k = this.lStar; k <= this.uStar; ) {
            this.mean += k * this.prob[j];
            j++;
            k++;
        }
        for (j = 0, k = this.lStar; k <= this.uStar; ) {
            this.variance += (k - this.mean) * (k - this.mean) * this.prob[j];
            j++;
            k++;
        }

    }

    public String toString() {
        return new String("Extended hypergeometric distribution: n1 = " + this.n1 + ", n2 = " + this.n2 + ", m = " + this.m + ", psi = " + this.psi + ".");
    }

    public double variance() {
        return this.variance;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c1 = 'Ǵ';
            char c2 = c1;
            char c3 = c1;
            char c4 = c1;

            double d1 = 6.0D;
            double d2 = 1.0E-14D;

            ExtendedHypergeometric extendedHypergeometric = new ExtendedHypergeometric(500, 500, 500, 6.0D, 1.0E-14D);
            System.out.println(extendedHypergeometric.toString());

            int i = 1000000;

            int[] arrayOfInt1 = new int[i];

            int[] arrayOfInt2 = {50, 500, 5000, 50, 500, 5000};
            double[] arrayOfDouble = {1.5D, 1.5D, 1.5D, 6.0D, 6.0D, 6.0D};

            System.out.println("tolerance = " + d2);
            byte b;
            for (b = 0; b < arrayOfInt2.length; b++) {

                extendedHypergeometric.setParameters(arrayOfInt2[b], arrayOfInt2[b], arrayOfInt2[b], arrayOfDouble[b], d2);
                double d5 = 0.0D, d6 = 0.0D;

                long l1 = System.currentTimeMillis();
                byte b1;
                for (b1 = 0; b1 < i; ) {
                    arrayOfInt1[b1] = (int) extendedHypergeometric.random();
                    b1++;
                }
                long l2 = System.currentTimeMillis();
                for (b1 = 0; b1 < i; ) {
                    d5 += arrayOfInt1[b1];
                    b1++;
                }
                double d3 = Maths.round(d5 / i, 4);
                for (b1 = 0; b1 < i; ) {
                    d6 += (arrayOfInt1[b1] - d3) * (arrayOfInt1[b1] - d3);
                    b1++;
                }
                double d4 = Maths.round(d6 / i, 4);
                System.out.println("m=" + arrayOfInt2[b] + " psi=" + arrayOfDouble[b] + " l=" + extendedHypergeometric.getEffectiveMinX() + " mode=" + extendedHypergeometric.mode() + " u=" + extendedHypergeometric.getEffectiveMaxX() + " " + ((l2 - l1) / 1000L) + " secs" + " mean=" + d3 + " var=" + d4);
            }

            i = 10000;

            extendedHypergeometric = new ExtendedHypergeometric(50, 50, 90, 6.0D, d2);

            int[] arrayOfInt3 = new int[i];
            for (b = 0; b < i; ) {
                arrayOfInt3[b] = (int) extendedHypergeometric.random();
                b++;
            }

            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt3), extendedHypergeometric, 0);
            System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
            System.out.println("n = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

