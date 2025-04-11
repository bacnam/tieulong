package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;

public class Bernoulli
        extends AbstractDiscreteDistribution {
    private double p;

    public Bernoulli(double paramDouble) {
        super(0L, 1L);
        if (paramDouble <= 0.0D || paramDouble >= 1.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.p = paramDouble;
    }

    public double cdf(double paramDouble) {
        if (paramDouble == 0.0D)
            return 1.0D - this.p;
        if (paramDouble == 1.0D) {
            return 1.0D;
        }
        throw new IllegalArgumentException("Invalid variate-value.");
    }

    public double getP() {
        return this.p;
    }

    public double mean() {
        return this.p;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D)
            throw new IllegalArgumentException("Invalid probability.");
        return ((paramDouble <= 1.0D - this.p) ? false : true);
    }

    public double pdf(double paramDouble) {
        if (paramDouble == 0.0D)
            return 1.0D - this.p;
        if (paramDouble == 1.0D) {
            return this.p;
        }
        throw new IllegalArgumentException("Invalid variate-value.");
    }

    public double random() {
        return ((this.rand.nextDouble() < this.p) ? true : false);
    }

    public String toString() {
        return new String("Bernoulli distribution: p = " + this.p + ".");
    }

    public double variance() {
        return this.p * (1.0D - this.p);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double d2 = 0.5D;
            double d1 = 0.7D;
            boolean bool = false;
            Bernoulli bernoulli = new Bernoulli(d1);
            System.out.println("Bernoulli distribution: p = " + d1);
            System.out.println("p.m.f. at X = " + bool + ") = " + bernoulli.pdf(bool));
            System.out.println("Probability(X <= " + bool + ") = " + bernoulli.cdf(bool));
            System.out.println("x such that Probability(X <= x) = " + d2 + ") = " + bernoulli.inverseCdf(d2));

            int i = 100000;
            int[] arrayOfInt = new int[i];
            for (byte b = 0; b < i; ) {
                arrayOfInt[b] = (int) bernoulli.random();
                b++;
            }

            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), bernoulli, 0);
            System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
            System.out.println("n = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

