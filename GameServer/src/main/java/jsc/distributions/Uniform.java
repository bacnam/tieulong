package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class Uniform
        extends AbstractDistribution {
    private double a;
    private double b;

    public Uniform() {
        this(0.0D, 1.0D);
    }

    public Uniform(double paramDouble1, double paramDouble2) {
        setInterval(paramDouble1, paramDouble2);
    }

    public double cdf(double paramDouble) {
        if (paramDouble < this.a || paramDouble > this.b)
            throw new IllegalArgumentException("Invalid variate-value.");
        return (paramDouble - this.a) / (this.b - this.a);
    }

    public double getA() {
        return this.a;
    }

    public double getB() {
        return this.b;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D)
            throw new IllegalArgumentException("Invalid probability.");
        return this.a + (this.b - this.a) * paramDouble;
    }

    public double mean() {
        return 0.5D * (this.a + this.b);
    }

    public double pdf(double paramDouble) {
        if (paramDouble < this.a || paramDouble > this.b)
            throw new IllegalArgumentException("Invalid variate-value.");
        return 1.0D / (this.b - this.a);
    }

    public double random() {
        return this.a + (this.b - this.a) * this.rand.nextDouble();
    }

    public void setInterval(double paramDouble1, double paramDouble2) {
        if (paramDouble2 <= paramDouble1)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.a = paramDouble1;
        this.b = paramDouble2;
    }

    public String toString() {
        return new String("Uniform distribution: a = " + this.a + ", b = " + this.b + ".");
    }

    public double variance() {
        return (this.b - this.a) * (this.b - this.a) / 12.0D;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = '✐';
            Uniform uniform = new Uniform(10.0D, 20.0D);
            double[] arrayOfDouble = new double[c];
            for (byte b = 0; b < c; b++) {
                arrayOfDouble[b] = uniform.random();
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, uniform, H1.NOT_EQUAL, true);
            System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }
}

