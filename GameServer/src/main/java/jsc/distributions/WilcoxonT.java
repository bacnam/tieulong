package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;

public class WilcoxonT
        extends AbstractDiscreteDistribution {
    public static final int MAX_PRODUCT = 10000;
    static final double LOG2 = Math.log(2.0D);

    private int n;

    private int n2;

    private double[] p;

    public WilcoxonT(int paramInt) {
        super(0L, (paramInt * (paramInt + 1) / 2));

        if (paramInt < 1)
            throw new IllegalArgumentException("Sample size too small.");
        if (this.maxValue > 10000L)
            throw new IllegalArgumentException("Cannot calculate exact distribution: try normal approximation.");
        this.n = paramInt;
        this.n2 = (int) Math.ceil(0.5D * this.maxValue);
        this.p = new double[1 + this.n2];

        for (byte b = 0; b <= this.n2; b++) {
            this.p[b] = Math.exp(Math.log(wcount(paramInt, b)) - paramInt * LOG2);
        }
    }

    public static Normal normalApproximation(int paramInt) {
        if (paramInt < 1)
            throw new IllegalArgumentException("Sample size too small.");
        double d1 = 0.25D * paramInt * (paramInt + 1.0D);
        double d2 = Math.sqrt(paramInt * (paramInt + 1.0D) / 24.0D * ((paramInt + paramInt) + 1.0D));
        return new Normal(d1, d2);
    }

    public long criticalValue(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability.");
        long l = this.minValue;

        double d = pdf(l);
        while (l < this.maxValue && d < paramDouble) {
            l++;
            d += pdf(l);
        }

        return --l;
    }

    public double mean() {
        return 0.25D * this.n * (this.n + 1.0D);
    }

    public double pdf(double paramDouble) {
        int i = (int) paramDouble;
        if (i < this.minValue || i > this.maxValue)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (i <= this.n2) {
            return this.p[i];
        }
        return this.p[(int) (this.maxValue - i)];
    }

    private int wcount(int paramInt1, int paramInt2) {
        if (paramInt1 == 0) {
            if (paramInt2 == 0) {
                return 1;
            }
            return 0;
        }
        if (paramInt2 < 0) {
            return 0;
        }
        return wcount(paramInt1 - 1, paramInt2) + wcount(paramInt1 - 1, paramInt2 - paramInt1);
    }

    public String toString() {
        return new String("Wilcoxon T distribution: n = " + this.n + ".");
    }

    public double variance() {
        return this.n * (this.n + 1.0D) / 24.0D * ((this.n + this.n) + 1.0D);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = '✐';

            WilcoxonT wilcoxonT = new WilcoxonT(25);
            int[] arrayOfInt = new int[c];
            for (byte b = 0; b < c; ) {
                arrayOfInt[b] = (int) wilcoxonT.random();
                b++;
            }

            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), wilcoxonT, 0);
            System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
            System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

