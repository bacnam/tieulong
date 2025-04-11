package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;

public class LogarithmicSeries
        extends AbstractDistribution {
    private double alpha;
    private double C1;
    private double P1;

    public LogarithmicSeries(double paramDouble) {
        setAlpha(paramDouble);
    }

    public double cdf(double paramDouble) {
        if (paramDouble < 1.0D) {
            throw new IllegalArgumentException("Invalid variate-value.");
        }

        if (paramDouble * (1.0D - this.alpha) > 10.0D) {
            return 1.0D;
        }

        double d1 = this.P1;
        double d2 = d1;

        for (byte b = 2; b <= paramDouble; b++) {

            d1 *= (1.0D - 1.0D / b) * this.alpha;
            d2 += d1;
            if (d1 <= 0.0D)
                break;
            if (d2 >= 1.0D) return 1.0D;

        }
        return d2;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public void setAlpha(double paramDouble) {
        if (paramDouble <= 0.0D || paramDouble >= 1.0D)
            throw new IllegalArgumentException("Invalid distribution parameter.");
        this.alpha = paramDouble;
        this.C1 = Math.log(1.0D - paramDouble);
        this.P1 = -paramDouble / this.C1;
    }

    public double inverseCdf(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble >= 1.0D) {
            throw new IllegalArgumentException("Invalid probability.");
        }

        double d3 = 1.0D;

        double d1 = this.P1;
        double d2 = d1;

        while (d2 < paramDouble && d1 > 0.0D) {

            d3++;
            d1 *= (1.0D - 1.0D / d3) * this.alpha;
            d2 += d1;
        }
        return d3;
    }

    public boolean isDiscrete() {
        return true;
    }

    public double mean() {
        return this.P1 / (1.0D - this.alpha);
    }

    public double pdf(double paramDouble) {
        if (paramDouble < 1.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        return -Math.pow(this.alpha, paramDouble) / paramDouble * this.C1;
    }

    public double random() {
        if (this.alpha > 0.986D) {

            double d8 = this.C1;
            double d7 = this.rand.nextDouble();
            if (d7 > this.alpha) {
                return 1.0D;
            }

            double d6 = 1.0D - this.rand.nextDouble();
            double d5 = 1.0D - Math.exp(d6 * d8);
            if (d7 < d5 * d5)
                return Math.floor(1.0D + Math.log(d7) / Math.log(d5));
            if (d7 > d5) {
                return 1.0D;
            }
            return 2.0D;
        }

        double d4 = this.P1;
        double d3 = this.rand.nextDouble();
        double d2 = 1.0D;
        double d1 = d4;
        while (d3 > d1) {
            d3 -= d1;
            d2++;
            d1 *= this.alpha * (d2 - 1.0D) / d2;
        }
        return d2;
    }

    public String toString() {
        return new String("Logarithmic series distribution: alpha = " + this.alpha + ".");
    }

    public double variance() {
        return this.P1 * (1.0D - this.P1) / (1.0D - this.alpha) * (1.0D - this.alpha);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double d = 0.9D;
            LogarithmicSeries logarithmicSeries = new LogarithmicSeries(d);

            char c = '✐';

            logarithmicSeries = new LogarithmicSeries(0.5D);
            int[] arrayOfInt = new int[c];
            for (byte b = 0; b < c; ) {
                arrayOfInt[b] = (int) logarithmicSeries.random();
                b++;
            }

            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), logarithmicSeries, 0);
            System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
            System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

