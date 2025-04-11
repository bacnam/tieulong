package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class Logistic
        extends AbstractDistribution {
    private double mean;
    private double scale;

    public Logistic() {
        this(0.0D, 1.0D);
    }

    public Logistic(double paramDouble1, double paramDouble2) {
        if (paramDouble2 <= 0.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.mean = paramDouble1;
        this.scale = paramDouble2;
    }

    public double cdf(double paramDouble) {
        if (paramDouble > this.mean) {
            return 1.0D / (1.0D + Math.exp(-(paramDouble - this.mean) / this.scale));
        }
        return 1.0D - 1.0D / (1.0D + Math.exp((paramDouble - this.mean) / this.scale));
    }

    public double getScale() {
        return this.scale;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D)
            throw new IllegalArgumentException("Invalid probability.");
        if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY;
        if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY;
        return this.mean + this.scale * Math.log(paramDouble / (1.0D - paramDouble));
    }

    public double mean() {
        return this.mean;
    }

    public double pdf(double paramDouble) {
        double d = 1.0D + ((paramDouble > this.mean) ? Math.exp(-(paramDouble - this.mean) / this.scale) : Math.exp((paramDouble - this.mean) / this.scale));
        return (d - 1.0D) / this.scale * d * d;
    }

    public double random() {
        while (true) {
            double d = this.rand.nextDouble();
            if (d != 0.0D)
                return this.mean + this.scale * Math.log(d / (1.0D - d));
        }
    }

    public String toString() {
        return new String("Laplace distribution: mean = " + this.mean + ", scale = " + this.scale + ".");
    }

    public double variance() {
        return 9.869604401089358D * this.scale * this.scale / 3.0D;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double d1 = 2.0D;
            double d2 = 3.0D;
            Logistic logistic = new Logistic(d1, d2);

            char c = '✐';

            logistic = new Logistic(2.0D, 3.0D);
            double[] arrayOfDouble = new double[c];
            for (byte b = 0; b < c; ) {
                arrayOfDouble[b] = logistic.random();
                b++;
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, logistic, H1.NOT_EQUAL, false);
            System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }
}

