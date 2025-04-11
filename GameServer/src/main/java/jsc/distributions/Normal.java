package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class Normal
        extends AbstractDistribution {
    static final double SQRPI2 = Math.sqrt(6.283185307179586D);

    private double mean;

    private double sd;

    public Normal() {
        this(0.0D, 1.0D);
    }

    public Normal(double paramDouble1, double paramDouble2) {
        if (paramDouble2 <= 0.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.mean = paramDouble1;
        this.sd = paramDouble2;
    }

    public static double inverseStandardCdf(double paramDouble) {
        double d1, d2 = paramDouble - 0.5D;
        if (Math.abs(d2) <= 0.425D) {

            double d = 0.180625D - d2 * d2;
            d1 = d2 * (((((((2509.0809287301227D * d + 33430.57558358813D) * d + 67265.7709270087D) * d + 45921.95393154987D) * d + 13731.69376550946D) * d + 1971.5909503065513D) * d + 133.14166789178438D) * d + 3.3871328727963665D) / (((((((5226.495278852854D * d + 28729.085735721943D) * d + 39307.89580009271D) * d + 21213.794301586597D) * d + 5394.196021424751D) * d + 687.1870074920579D) * d + 42.31333070160091D) * d + 1.0D);

            return d1;
        }

        double d3 = (d2 < 0.0D) ? paramDouble : (1.0D - paramDouble);
        if (d3 <= 0.0D) {
            throw new IllegalArgumentException("Invalid probability.");
        }
        d3 = Math.sqrt(-Math.log(d3));

        if (d3 <= 5.0D) {

            d3 -= 1.6D;
            d1 = (((((((7.745450142783414E-4D * d3 + 0.022723844989269184D) * d3 + 0.2417807251774506D) * d3 + 1.2704582524523684D) * d3 + 3.6478483247632045D) * d3 + 5.769497221460691D) * d3 + 4.630337846156546D) * d3 + 1.4234371107496835D) / (((((((1.0507500716444169E-9D * d3 + 5.475938084995345E-4D) * d3 + 0.015198666563616457D) * d3 + 0.14810397642748008D) * d3 + 0.6897673349851D) * d3 + 1.6763848301838038D) * d3 + 2.053191626637759D) * d3 + 1.0D);

        } else {

            d3 -= 5.0D;
            d1 = (((((((2.0103343992922881E-7D * d3 + 2.7115555687434876E-5D) * d3 + 0.0012426609473880784D) * d3 + 0.026532189526576124D) * d3 + 0.29656057182850487D) * d3 + 1.7848265399172913D) * d3 + 5.463784911164114D) * d3 + 6.657904643501103D) / (((((((2.0442631033899397E-15D * d3 + 1.421511758316446E-7D) * d3 + 1.8463183175100548E-5D) * d3 + 7.868691311456133E-4D) * d3 + 0.014875361290850615D) * d3 + 0.1369298809227358D) * d3 + 0.599832206555888D) * d3 + 1.0D);
        }

        if (d2 < 0.0D) d1 = -d1;
        return d1;
    }

    public static double standardTailProb(double paramDouble, boolean paramBoolean) {
        double d1;
        boolean bool = paramBoolean;
        double d2 = paramDouble;
        if (d2 < 0.0D) {
            bool = !bool;
            d2 = -d2;
        }
        if (d2 <= 8.0D || (bool && d2 <= 37.0D)) {

            double d = 0.5D * d2 * d2;
            if (d2 >= 1.28D) {
                d1 = 0.398942280385D * Math.exp(-d) / (d2 - 3.8052E-8D + 1.00000615302D / (d2 + 3.98064794E-4D + 1.98615381364D / (d2 - 0.151679116635D + 5.29330324926D / (d2 + 4.8385912808D - 15.1508972451D / (d2 + 0.742380924027D + 30.789933034D / (d2 + 3.99019417011D))))));
            } else {

                d1 = 0.5D - d2 * (0.398942280444D - 0.399903438504D * d / (d + 5.75885480458D - 29.8213557808D / (d + 2.62433121679D + 48.6959930692D / (d + 5.92885724438D))));
            }
        } else {

            d1 = 0.0D;
        }
        if (!bool) d1 = 1.0D - d1;
        return d1;
    }

    public double cdf(double paramDouble) {
        return standardTailProb((paramDouble - this.mean) / this.sd, false);
    }

    public double getMaximumPdf() {
        return 1.0D / this.sd * SQRPI2;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY;
        if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY;
        return this.mean + this.sd * inverseStandardCdf(paramDouble);
    }

    public double mean() {
        return this.mean;
    }

    public double pdf(double paramDouble) {
        double d = (paramDouble - this.mean) / this.sd;
        return Math.exp(-0.5D * d * d) / SQRPI2 * this.sd;
    }

    public double random() {
        return this.mean + this.sd * this.rand.nextGaussian();
    }

    public double sd() {
        return this.sd;
    }

    public void setMean(double paramDouble) {
        this.mean = paramDouble;
    }

    public void setSd(double paramDouble) {
        if (paramDouble <= 0.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.sd = paramDouble;
    }

    public void setVariance(double paramDouble) {
        if (paramDouble <= 0.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.sd = Math.sqrt(paramDouble);
    }

    public String toString() {
        return new String("Normal distribution: mean = " + this.mean + ", sd = " + this.sd + ".");
    }

    public double upperTailProb(double paramDouble) {
        return standardTailProb((paramDouble - this.mean) / this.sd, true);
    }

    public double variance() {
        return this.sd * this.sd;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = '✐';
            Normal normal = new Normal(-5.0D, 10.0D);
            double[] arrayOfDouble = new double[c];
            for (byte b = 0; b < c; b++) {
                arrayOfDouble[b] = normal.random();
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, normal, H1.NOT_EQUAL, true);
            System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }
}

