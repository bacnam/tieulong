package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;
import jsc.util.Maths;

public class NegativeBinomial
        extends AbstractDistribution {
    private long n;
    private double p;
    private Poisson poisson;
    private double LOG1MP;
    private double LOGP;

    public NegativeBinomial(long paramLong, double paramDouble) {
        if (paramLong < 1L || paramDouble <= 0.0D || paramDouble >= 1.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.n = paramLong;
        this.p = paramDouble;
        this.poisson = new Poisson(paramLong * (1.0D - paramDouble) / paramDouble);
        this.LOG1MP = Math.log(1.0D - paramDouble);
        this.LOGP = Math.log(paramDouble);
    }

    public double cdf(double paramDouble) {
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        return Beta.incompleteBeta(this.p, this.n, paramDouble + 1.0D, Maths.lnB(this.n, paramDouble + 1.0D));
    }

    public long getN() {
        return this.n;
    }

    public double getP() {
        return this.p;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble <= 0.0D || paramDouble >= 1.0D) {
            throw new IllegalArgumentException("Invalid probability.");
        }

        double d2 = 0.0D;

        d2 = this.poisson.inverseCdf(paramDouble);
        double d1 = cdf(d2);

        while (d1 > paramDouble && d2 > 0.0D) {
            d2--;
            d1 = cdf(d2);
        }
        while (d1 < paramDouble) {
            d2++;
            d1 = cdf(d2);
        }
        return d2;
    }

    public boolean isDiscrete() {
        return true;
    }

    public double mean() {
        return this.n * (1.0D - this.p) / this.p;
    }

    public double pdf(double paramDouble) {
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        return Math.exp(Maths.logBinomialCoefficient((long) (this.n + paramDouble - 1.0D), (long) paramDouble) + this.n * this.LOGP + paramDouble * this.LOG1MP);
    }

    public double random() {
        byte b2 = 0;
        double d1 = 0.0D;
        double d2 = 0.0D;

        if (this.p > 0.6D) {
            while (true) {

                if (this.rand.nextDouble() < this.p) {
                    b2++;
                } else {
                    d1++;
                }
                if (b2 >= this.n) {
                    return d1;
                }
            }
        }
        for (byte b1 = 1; b1 <= this.n; b1++)
            d2 += Math.ceil(Math.log(1.0D - this.rand.nextDouble()) / this.LOG1MP);
        return Math.max(0.0D, Math.floor(d2 - this.n));
    }

    public String toString() {
        return new String("Negative binomial distribution: n = " + this.n + ", p = " + this.p + ".");
    }

    public double variance() {
        return this.n * (1.0D - this.p) / this.p * this.p;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            long l = 40L;
            double d = 0.9D;
            NegativeBinomial negativeBinomial = new NegativeBinomial(l, d);

            negativeBinomial = new NegativeBinomial(10L, 0.4D);
            char c = '✐';
            int[] arrayOfInt = new int[c];
            for (byte b = 0; b < c; b++) {
                arrayOfInt[b] = (int) negativeBinomial.random();
            }

            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), negativeBinomial, 0);
            System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
            System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

