package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;
import jsc.util.Maths;

public class Hypergeometric
        extends AbstractDiscreteDistribution {
    static final int MBIG = 3000;
    static final double ELIMIT = Math.log(Double.MIN_VALUE);

    static final double SCALE = 1.0E300D;

    private int sampleSize;

    private int populationSize;

    private int markedItemsCount;

    private double P;

    public Hypergeometric(int paramInt1, int paramInt2, int paramInt3) {
        super(Math.max(0, paramInt1 - paramInt2 + paramInt3), Math.min(paramInt1, paramInt3));
        if (paramInt3 < 0 || paramInt2 < paramInt3 || paramInt1 < 0 || paramInt1 > paramInt2) {
            throw new IllegalArgumentException("Invalid distribution parameter.");
        }
        this.sampleSize = paramInt1;
        this.populationSize = paramInt2;
        this.markedItemsCount = paramInt3;
        this.P = paramInt3 / paramInt2;
    }

    public double cdf(double paramDouble) {
        return chyper(false, this.sampleSize, this.populationSize, this.markedItemsCount, (int) paramDouble);
    }

    private double chyper(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        boolean bool = true;
        double d3 = 0.0D;

        int i = paramInt1 + 1;
        int j = paramInt4 + 1;
        int k = paramInt2 + 1;
        int m = paramInt3 + 1;

        if (j < 1 || i - j > k - m)
            throw new IllegalArgumentException("Invalid variate-value " + j);
        if (!paramBoolean) d3 = 1.0D;
        if (j > m || j > i) {
            throw new IllegalArgumentException("Invalid variate-value " + j);
        }
        d3 = 1.0D;
        if (i == 1 || i == k || m == 1 || m == k) return d3;

        double d1 = paramInt3 / (paramInt2 - paramInt3);

        if (Math.min(i - 1, k - i) > Math.min(m - 1, k - m)) {
            int i3 = i;
            i = m;
            m = i3;
        }
        if (k - i < i - 1) {

            bool = !bool ? true : false;
            j = m - j + 1;
            i = k - i + 1;
        }

        if (paramInt2 > 3000) {

            d1 = Maths.logFactorial(paramInt3) - Maths.logFactorial(paramInt2) + Maths.logFactorial((paramInt2 - paramInt1)) + Maths.logFactorial(paramInt1) + Maths.logFactorial((paramInt2 - paramInt3)) - Maths.logFactorial(paramInt4) - Maths.logFactorial((paramInt3 - paramInt4)) - Maths.logFactorial((paramInt1 - paramInt4)) - Maths.logFactorial((paramInt2 - paramInt3 - paramInt1 + paramInt4));

            d3 = 0.0D;
            if (d1 >= ELIMIT) d3 = Math.exp(d1);

        } else {
            int i3;
            for (i3 = 1; i3 <= j - 1; i3++) {
                d3 *= (i - i3) * (m - i3) / (j - i3) * (k - i3);
            }

            if (j != i) {

                int i4 = k - m + j;
                for (i3 = j; i3 <= i - 1; i3++) {
                    d3 *= (i4 - i3) / (k - i3);
                }
            }
        }
        if (paramBoolean) return d3;

        if (d3 == 0.0D) {

            if (paramInt2 <= 3000)
                d1 = Maths.logFactorial(paramInt3) - Maths.logFactorial(paramInt2) + Maths.logFactorial(paramInt1) + Maths.logFactorial((paramInt2 - paramInt3)) - Maths.logFactorial(paramInt4) - Maths.logFactorial((paramInt3 - paramInt4)) - Maths.logFactorial((paramInt1 - paramInt4)) - Maths.logFactorial((paramInt2 - paramInt3 - paramInt1 + paramInt4)) + Maths.logFactorial((paramInt2 - paramInt1));

            d1 += Math.log(1.0E300D);

            d1 = Math.exp(d1);
        } else {

            d1 = d3 * 1.0E300D;
        }
        double d2 = 0.0D;
        int n = m - j;
        int i1 = i - j;
        int i2 = k - m - i1 + 1;
        if (j <= i1) {

            for (byte b = 1; b <= j - 1; b++) {
                d1 *= (j - b) * (i2 - b) / (n + b) * (i1 + b);

                d2 += d1;

            }

        } else {

            bool = !bool ? true : false;
            for (byte b = 0; b <= i1 - 1; b++) {

                d1 *= (n - b) * (i1 - b) / (j + b) * (i2 + b);

                d2 += d1;
            }
        }

        d3 = bool ? (d3 + d2 / 1.0E300D) : (1.0D - d2 / 1.0E300D);

        if (d3 > 1.0D) return 1.0D;
        if (d3 < 0.0D) return 0.0D;
        return d3;
    }

    public int getMarkedItemsCount() {
        return this.markedItemsCount;
    }

    public int getPopulationSize() {
        return this.populationSize;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public double mean() {
        return this.sampleSize * this.P;
    }

    public double pdf(double paramDouble) {
        return chyper(true, this.sampleSize, this.populationSize, this.markedItemsCount, (int) paramDouble);
    }

    public double random() {
        int i;
        double d3, d1 = 0.0D;
        double d2 = this.populationSize;

        if (this.sampleSize < this.markedItemsCount) {
            i = this.sampleSize;
            d3 = this.markedItemsCount;
        } else {
            i = this.markedItemsCount;
            d3 = this.sampleSize;
        }
        for (byte b = 1; b <= i; b++) {

            double d = d3 / d2;
            boolean bool = (this.rand.nextDouble() < d) ? true : false;
            d2--;

            if (bool) {
                d1++;
                d3--;
            }

            if (d3 == 0.0D)
                break;
        }
        return d1;
    }

    public String toString() {
        return new String("Hypergeometric distribution: sample size = " + this.sampleSize + ", population size = " + this.populationSize + ", marked items count = " + this.markedItemsCount + ".");
    }

    public double upperTailProb(double paramDouble) {
        return 1.0D - cdf(((int) paramDouble - 1));
    }

    public double variance() {
        return this.sampleSize * this.P * (1.0D - this.P) * (this.populationSize - this.sampleSize) / (this.populationSize - 1.0D);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c2 = 'ߐ', c1 = 'Ϩ', c3 = 'Ϩ';

            int i = 1000000;
            Hypergeometric hypergeometric = new Hypergeometric(16, 32, 11);

            System.out.println(hypergeometric.toString());

            int[] arrayOfInt = new int[i];

            long l1 = System.currentTimeMillis();
            for (byte b = 0; b < i; ) {
                arrayOfInt[b] = (int) hypergeometric.random();
                b++;
            }
            long l2 = System.currentTimeMillis();
            System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");

            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), hypergeometric, 0);
            System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
            System.out.println("s = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

