package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;
import jsc.util.Maths;

public class MannWhitneyU
        extends AbstractDiscreteDistribution {
    public static final int MAX_PRODUCT = 10000;
    private int n;
    private int nA;
    private int nB;
    private double[] p;

    public MannWhitneyU(int paramInt1, int paramInt2) {
        super(0L, (paramInt1 * paramInt2));

        if (this.maxValue > 10000L)
            throw new IllegalArgumentException("Cannot calculate exact distribution: try normal approximation.");
        this.nA = paramInt1;
        this.nB = paramInt2;
        this.n = (int) Math.ceil(0.5D * this.maxValue);
        this.p = new double[1 + this.n];
        harding(false, paramInt1, paramInt2, this.n, this.p);

        double d = Maths.logBinomialCoefficient((paramInt1 + paramInt2), paramInt1);

        for (byte b = 0; b <= this.n; b++) {

            this.p[b] = Math.exp(Math.log(this.p[b]) - d);
            if (Double.isNaN(this.p[b])) {

                throw new RuntimeException("Cannot calculate exact distribution: try normal approximation.");
            }
        }
    }

    public static void harding(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfdouble) {
        if (paramInt1 < 1 || paramInt2 < 1) {
            throw new IllegalArgumentException("Sample size too small.");
        }
        if (!paramBoolean) {

            paramArrayOfdouble[0] = 1.0D;
            for (byte b1 = 1; b1 <= paramInt3; ) {
                paramArrayOfdouble[b1] = 0.0D;
                b1++;
            }

        }
        if (paramInt2 + 1 <= paramInt3) {

            int j = Math.min(paramInt1 + paramInt2, paramInt3);
            for (int k = paramInt2 + 1; k <= j; k++) {
                for (int m = paramInt3; m >= k; m--) {
                    paramArrayOfdouble[m] = paramArrayOfdouble[m] - paramArrayOfdouble[m - k];
                }
            }
        }

        int i = Math.min(paramInt1, paramInt3);
        for (byte b = 1; b <= i; b++) {
            for (byte b1 = b; b1 <= paramInt3; b1++) {
                paramArrayOfdouble[b1] = paramArrayOfdouble[b1] + paramArrayOfdouble[b1 - b];
            }
        }
    }

    public static Normal normalApproximation(int paramInt1, int paramInt2, int paramInt3) {
        double d1 = (paramInt1 + paramInt2);
        double d2 = (paramInt1 * paramInt2);
        double d3 = (d1 * d1 * d1 - d1 - paramInt3) / 12.0D;
        if (paramInt3 < 0 || d3 <= 0.0D) {
            throw new IllegalArgumentException("Invalid samples sizes or correction factor.");
        }
        double d4 = 0.5D * d2;
        double d5 = Math.sqrt(d2 / d1 * (d1 - 1.0D) * d3);
        return new Normal(d4, d5);
    }

    public long criticalValue(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability.");
        long l = this.minValue;

        double d = pdf(l);
        while (l < this.maxValue && d < paramDouble) {
            l++;
            d += pdf(l);
        }
        if (Math.abs(d - paramDouble) < 1.0E-11D) return l;
        return --l;
    }

    public double mean() {
        return 0.5D * this.nA * this.nB;
    }

    public double pdf(double paramDouble) {
        int i = (int) paramDouble;
        if (i < this.minValue || i > this.maxValue)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (i <= this.n) {
            return this.p[i];
        }
        return this.p[(int) (this.maxValue - i)];
    }

    public String toString() {
        return new String("Mann-Whitney U distribution: nA = " + this.nA + ", nB = " + this.nB + ".");
    }

    public double variance() {
        return (this.nA * this.nB) * ((this.nA + this.nB) + 1.0D) / 12.0D;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            MannWhitneyU mannWhitneyU = new MannWhitneyU(50, 40);
            char c = '✐';
            int[] arrayOfInt = new int[c];
            for (byte b = 0; b < c; b++) {
                arrayOfInt[b] = (int) mannWhitneyU.random();
            }

            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), mannWhitneyU, 0);
            System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
            System.out.println("n = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

