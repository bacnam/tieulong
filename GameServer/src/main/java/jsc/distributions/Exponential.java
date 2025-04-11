package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class Exponential
        extends AbstractDistribution {
    private double mean;

    public Exponential(double paramDouble) {
        setMean(paramDouble);
    }

    public Exponential() {
        this(1.0D);
    }

    public double cdf(double paramDouble) {
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        return 1.0D - Math.exp(-paramDouble / this.mean);
    }

    public double getMaximumPdf() {
        return 1.0D / this.mean;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D)
            throw new IllegalArgumentException("Invalid probability.");
        if (paramDouble == 0.0D) return 0.0D;
        if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY;
        return -this.mean * Math.log(1.0D - paramDouble);
    }

    public double mean() {
        return this.mean;
    }

    public double pdf(double paramDouble) {
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        return Math.exp(-paramDouble / this.mean) / this.mean;
    }

    public double random() {
        return -this.mean * Math.log(1.0D - this.rand.nextDouble());
    }

    public double sd() {
        return this.mean;
    }

    public void setMean(double paramDouble) {
        if (paramDouble <= 0.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.mean = paramDouble;
    }

    public String toString() {
        return new String("Exponential distribution: mean = " + this.mean + ".");
    }

    public double variance() {
        return this.mean * this.mean;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double d = 2.0D;
            Exponential exponential = new Exponential(d);

            int i = 100000;
            double[] arrayOfDouble = new double[i];
            for (byte b = 0; b < i; ) {
                arrayOfDouble[b] = exponential.random();
                b++;
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, exponential, H1.NOT_EQUAL, false);
            System.out.println("n = " + i + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }
}

