package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class Lognormal
        extends AbstractDistribution {
    private Normal N;

    public Lognormal() {
        this(0.0D, 1.0D);
    }

    public Lognormal(double paramDouble1, double paramDouble2) {
        this.N = new Normal(paramDouble1, paramDouble2);
    }

    public double cdf(double paramDouble) {
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (paramDouble == 0.0D) return 0.0D;
        return this.N.cdf(Math.log(paramDouble));
    }

    public double getLocation() {
        return this.N.mean();
    }

    public double getScale() {
        return this.N.sd();
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble == 0.0D) return 0.0D;
        if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY;
        return Math.exp(this.N.inverseCdf(paramDouble));
    }

    public double mean() {
        return Math.exp(this.N.mean() + 0.5D * this.N.variance());
    }

    public double pdf(double paramDouble) {
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (paramDouble == 0.0D) return 0.0D;
        return this.N.pdf(Math.log(paramDouble)) / paramDouble;
    }

    public double random() {
        return Math.exp(this.N.random());
    }

    public String toString() {
        return new String("Lognormal distribution: location = " + this.N.mean() + ", scale = " + this.N.sd() + ".");
    }

    public double variance() {
        double d = this.N.mean() + this.N.variance();
        return Math.exp(d + d) - Math.exp(this.N.mean() + d);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double d1 = 5.0D;
            double d2 = 0.6D;
            Lognormal lognormal = new Lognormal(d1, d2);

            char c = '✐';

            lognormal = new Lognormal(-10.0D, 99.0D);
            double[] arrayOfDouble = new double[c];
            for (byte b = 0; b < c; ) {
                arrayOfDouble[b] = lognormal.random();
                b++;
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, lognormal, H1.NOT_EQUAL, false);
            System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }
}

