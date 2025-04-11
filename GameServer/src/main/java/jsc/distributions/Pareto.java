package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class Pareto
        extends AbstractDistribution {
    private double location;
    private double shape;
    private double RECIPC;
    private double CAC;

    public Pareto(double paramDouble1, double paramDouble2) {
        if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.location = paramDouble1;
        this.shape = paramDouble2;
        this.RECIPC = 1.0D / paramDouble2;
        this.CAC = paramDouble2 * Math.pow(paramDouble1, paramDouble2);
    }

    public double cdf(double paramDouble) {
        if (paramDouble < this.location)
            throw new IllegalArgumentException("Invalid variate-value.");
        return 1.0D - Math.pow(this.location / paramDouble, this.shape);
    }

    public double getLocation() {
        return this.location;
    }

    public double getShape() {
        return this.shape;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D)
            throw new IllegalArgumentException("Invalid probability.");
        if (paramDouble == 0.0D) return this.location;
        if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY;
        return this.location / Math.pow(1.0D - paramDouble, this.RECIPC);
    }

    public double mean() {
        return (this.shape > 1.0D) ? (this.shape * this.location / (this.shape - 1.0D)) : Double.NaN;
    }

    public double pdf(double paramDouble) {
        if (paramDouble < this.location)
            throw new IllegalArgumentException("Invalid variate-value.");
        return this.CAC / Math.pow(paramDouble, this.shape + 1.0D);
    }

    public double random() {
        return this.location / Math.pow(1.0D - this.rand.nextDouble(), this.RECIPC);
    }

    public String toString() {
        return new String("Pareto distribution: location = " + this.location + ", shape = " + this.shape + ".");
    }

    public double variance() {
        return (this.shape > 2.0D) ? (this.shape * this.location * this.location / (this.shape - 1.0D) * (this.shape - 1.0D) * (this.shape - 2.0D)) : Double.NaN;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = '✐';
            Pareto pareto = new Pareto(1.0D, 2.0D);
            double[] arrayOfDouble = new double[c];
            for (byte b = 0; b < c; b++) {
                arrayOfDouble[b] = pareto.random();
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, pareto, H1.NOT_EQUAL, true);
            System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }
}

