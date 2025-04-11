package jsc.goodnessfit;

import jsc.descriptive.CategoricalTally;
import jsc.descriptive.DoubleFrequencyTable;
import jsc.descriptive.DoubleTally;
import jsc.descriptive.Tally;
import jsc.distributions.ChiSquared;
import jsc.distributions.DiscreteUniform;
import jsc.distributions.Distribution;
import jsc.tests.SignificanceTest;

import java.util.Arrays;

public class ChiSquaredFitTest
        implements SignificanceTest {
    private int df;
    private int k;
    private int n;
    private int estimatedParasCount;
    private int smallExpectedFrequencyCount = 0;

    private double chi;

    private int[] O;

    private double[] E;

    private double[] probs;

    private double[] resids;

    private double SP;

    public ChiSquaredFitTest(int[] paramArrayOfint) {
        this(paramArrayOfint, getUniformProbs(paramArrayOfint.length), 0);
    }

    public ChiSquaredFitTest(int[] paramArrayOfint, double[] paramArrayOfdouble, int paramInt) {
        int i = paramArrayOfint.length;
        int j = 0;
        for (byte b = 0; b < i; ) {
            j += paramArrayOfint[b];
            b++;
        }
        calculateChiSquared(j, i, paramArrayOfint, paramArrayOfdouble, paramInt);
    }

    public ChiSquaredFitTest(CategoricalTally paramCategoricalTally) {
        this(paramCategoricalTally, getUniformProbs(paramCategoricalTally.getNumberOfBins()), 0);
    }

    public ChiSquaredFitTest(CategoricalTally paramCategoricalTally, double[] paramArrayOfdouble, int paramInt) {
        calculateChiSquared(paramCategoricalTally.getN(), paramCategoricalTally.getNumberOfBins(), paramCategoricalTally.getFrequencies(), paramArrayOfdouble, paramInt);
    }

    public ChiSquaredFitTest(Tally paramTally) {
        this(paramTally, (Distribution) new DiscreteUniform(paramTally.getMin(), paramTally.getMax()), 0);
    }

    public ChiSquaredFitTest(Tally paramTally, Distribution paramDistribution, int paramInt) {
        int i = paramTally.getNumberOfBins();
        double[] arrayOfDouble = new double[i];
        for (byte b = 0; b < i; b++)
            arrayOfDouble[b] = paramDistribution.pdf(paramTally.getBinValue(b));
        calculateChiSquared(paramTally.getN(), i, paramTally.getFrequencies(), arrayOfDouble, paramInt);
    }

    public ChiSquaredFitTest(DoubleTally paramDoubleTally, Distribution paramDistribution, int paramInt) {
        int i = paramDoubleTally.getValueCount();
        int[] arrayOfInt = new int[i];
        double[] arrayOfDouble = new double[i];
        for (byte b = 0; b < i; b++) {

            arrayOfInt[b] = paramDoubleTally.getFrequency(b);
            arrayOfDouble[b] = paramDistribution.pdf(paramDoubleTally.getValue(b));
        }
        calculateChiSquared(paramDoubleTally.getN(), i, arrayOfInt, arrayOfDouble, paramInt);
    }

    public ChiSquaredFitTest(DoubleFrequencyTable paramDoubleFrequencyTable, Distribution paramDistribution, int paramInt) {
        int i = paramDoubleFrequencyTable.getNumberOfBins();
        double[] arrayOfDouble = new double[i];

        double d = paramDistribution.cdf(paramDoubleFrequencyTable.getBoundary(0));
        for (byte b = 0; b < i; b++) {

            double d1 = paramDistribution.cdf(paramDoubleFrequencyTable.getBoundary(1 + b));
            arrayOfDouble[b] = d1 - d;
            d = d1;
        }
        calculateChiSquared(paramDoubleFrequencyTable.getN(), i, paramDoubleFrequencyTable.getFrequencies(), arrayOfDouble, paramInt);
    }

    private static double[] getUniformProbs(int paramInt) {
        double[] arrayOfDouble = new double[paramInt];
        Arrays.fill(arrayOfDouble, 1.0D / paramInt);
        return arrayOfDouble;
    }

    private void calculateChiSquared(int paramInt1, int paramInt2, int[] paramArrayOfint, double[] paramArrayOfdouble, int paramInt3) {
        if (paramInt3 < 0)
            throw new IllegalArgumentException("Negative number of estimated parameters.");
        this.df = paramInt2 - paramInt3 - 1;
        if (this.df < 1) {
            throw new IllegalArgumentException("Zero degrees of freedom.");
        }
        this.n = paramInt1;
        this.k = paramInt2;
        this.O = paramArrayOfint;
        this.estimatedParasCount = paramInt3;
        this.probs = paramArrayOfdouble;

        this.E = new double[paramInt2];
        this.resids = new double[paramInt2];

        this.chi = 0.0D;
        this.smallExpectedFrequencyCount = 0;
        for (byte b = 0; b < paramInt2; b++) {

            if (paramArrayOfdouble[b] < 0.0D || paramArrayOfdouble[b] > 1.0D)
                throw new IllegalArgumentException("Invalid probability.");
            this.E[b] = paramInt1 * paramArrayOfdouble[b];
            if (this.E[b] == 0.0D)
                throw new IllegalArgumentException("An expected frequency is zero.");
            if (this.E[b] < 5.0D) this.smallExpectedFrequencyCount++;
            this.resids[b] = (this.O[b] - this.E[b]) * (this.O[b] - this.E[b]) / this.E[b];
            this.chi += this.resids[b];
        }

        this.SP = ChiSquared.upperTailProb(this.chi, this.df);
    }

    public int getDegreesOfFreedom() {
        return this.df;
    }

    public int getN() {
        return this.n;
    }

    public int getNumberOfBins() {
        return this.k;
    }

    public double[] getExpectedFrequencies() {
        return this.E;
    }

    public double getExpectedFrequency(int paramInt) {
        return this.E[paramInt];
    }

    public int[] getObservedFrequencies() {
        return this.O;
    }

    public int getObservedFrequency(int paramInt) {
        return this.O[paramInt];
    }

    public double[] getResiduals() {
        return this.resids;
    }

    public double getResidual(int paramInt) {
        return this.resids[paramInt];
    }

    public int getSmallExpectedFrequencyCount() {
        return this.smallExpectedFrequencyCount;
    }

    public double getTestStatistic() {
        return this.chi;
    }

    public double getSP() {
        return this.SP;
    }

    public boolean poolBins() {
        int i;
        do {
            i = this.k;
            if (this.df == 1) return false;
            for (byte b = 0; b < this.k - 1; b++) {

                if (this.E[b] < 5.0D) {

                    double d = this.E[b];
                    byte b1 = b;
                    int j;
                    for (j = b1 + 1; j < this.k; j++) {

                        d += this.E[j];
                        if (d >= 5.0D)
                            break;
                    }
                    if (j == this.k) j--;
                    poolBins(b1, j);
                }
            }

            if (this.E[this.k - 1] >= 5.0D || this.k <= 2) continue;
            poolBins(this.k - 2, this.k - 1);
        }
        while (i != this.k && this.k > 2);

        return (this.smallExpectedFrequencyCount == 0);
    }

    public double poolBins(int paramInt1, int paramInt2) {
        int j = paramInt2 - paramInt1;
        int k = this.k - j;
        int m = 0;
        double d = 0.0D;
        int[] arrayOfInt = new int[k];
        double[] arrayOfDouble = new double[k];

        if (j < 1 || paramInt1 < 0 || paramInt2 >= this.k)
            throw new IllegalArgumentException("Invalid pool indexes.");
        int i;
        for (i = 0; i < paramInt1; ) {
            arrayOfDouble[i] = this.probs[i];
            arrayOfInt[i] = this.O[i];
            i++;
        }
        for (i = paramInt1; i <= paramInt2; ) {
            d += this.probs[i];
            m += this.O[i];
            i++;
        }
        arrayOfDouble[paramInt1] = d;
        arrayOfInt[paramInt1] = m;
        for (i = paramInt1 + 1; i < k; ) {
            arrayOfDouble[i] = this.probs[i + j];
            arrayOfInt[i] = this.O[i + j];
            i++;
        }

        calculateChiSquared(this.n, k, arrayOfInt, arrayOfDouble, this.estimatedParasCount);
        return this.n * d;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            int[] arrayOfInt = {1, 2, 3, 2, 1, 3, 4, 2, 1};
            double[] arrayOfDouble = {0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D};
            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(arrayOfInt, arrayOfDouble, 0);
            chiSquaredFitTest.poolBins();

            for (byte b = 0; b < chiSquaredFitTest.getNumberOfBins(); b++) {
                System.out.println("O = " + chiSquaredFitTest.getObservedFrequency(b) + " E = " + chiSquaredFitTest.getExpectedFrequency(b) + " resid = " + chiSquaredFitTest.getResidual(b));
            }
            System.out.println("Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

