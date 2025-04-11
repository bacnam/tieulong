package jsc.independentsamples;

import jsc.combinatorics.MultiSetPermutation;
import jsc.combinatorics.MultiSetPermutations;
import jsc.datastructures.GroupedData;
import jsc.distributions.Beta;
import jsc.distributions.ChiSquared;
import jsc.distributions.Gamma;
import jsc.tests.SignificanceTest;
import jsc.util.Maths;
import jsc.util.Rank;

public class KruskalWallisTest
        implements SignificanceTest {
    public static final int MAX_PERMUTATION_COUNT = 17153136;
    final int k;
    final int N;
    final double H;
    final int[] ns;
    final double[] R;
    final double[] z;
    final double SP;
    final double[] originalSample;
    private final double Rbar;
    private final double c;

    public KruskalWallisTest(GroupedData paramGroupedData, double paramDouble, boolean paramBoolean) {
        double d3 = 0.0D;

        this.k = paramGroupedData.getGroupCount();
        if (this.k < 2)
            throw new IllegalArgumentException("Less than two samples.");
        this.N = paramGroupedData.getN();

        this.ns = new int[this.k];
        this.R = new double[this.k];
        this.z = new double[this.k];

        double[] arrayOfDouble = paramGroupedData.getData();

        Rank rank = new Rank(arrayOfDouble, paramDouble);

        this.originalSample = rank.getRanks();
        int i = rank.getCorrectionFactor1();

        double d2 = this.N;
        double d1 = (i > 0) ? (1.0D - i / d2 * (d2 * d2 - 1.0D)) : 1.0D;

        byte b2 = 0;
        byte b1;
        for (b1 = 0; b1 < this.k; b1++) {

            this.R[b1] = 0.0D;
            this.ns[b1] = paramGroupedData.getSize(b1);
            for (byte b = 0; b < this.ns[b1]; ) {
                this.R[b1] = this.R[b1] + rank.getRank(b2++);
                b++;
            }

        }

        for (b1 = 0; b1 < this.k; ) {
            this.R[b1] = this.R[b1] / this.ns[b1];
            b1++;
        }

        this.Rbar = 0.5D * (d2 + 1.0D);
        this.c = 12.0D / d2 * (d2 + 1.0D);

        for (b1 = 0; b1 < this.k; b1++) {

            double d = this.R[b1] - this.Rbar;
            this.z[b1] = d / Math.sqrt((d2 + 1.0D) / 12.0D * (d2 / this.ns[b1] - 1.0D));
            d3 += this.ns[b1] * d * d;
        }

        if (d1 <= 0.0D)
            throw new IllegalArgumentException("Cannot calculate Kruskal-Wallis statistic.");
        this.H = this.c * d3 / d1;

        double d4 = Maths.multinomialCoefficient(this.ns);

        if (paramBoolean) {
            this.SP = gammaApproxSP(this.ns, this.H);

        } else if (d4 <= 1.7153136E7D) {
            this.SP = exactSP();
        } else {
            this.SP = gammaApproxSP(this.ns, this.H);
        }
    }

    public KruskalWallisTest(GroupedData paramGroupedData) {
        this(paramGroupedData, 0.0D, false);
    }

    public static double betaApproxSP(int[] paramArrayOfint, double paramDouble) {
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Negative H.");
        int i = paramArrayOfint.length;
        if (i < 2)
            throw new IllegalArgumentException("Less than 2 samples.");
        int j = 0;
        double d1 = (i - 1);

        double d2 = 0.0D;
        double d3 = 0.0D;
        for (byte b = 0; b < i; b++) {

            if (paramArrayOfint[b] < 1)
                throw new IllegalArgumentException("Less than one data value in a sample.");
            d2 += 1.0D / paramArrayOfint[b];
            d3 += (paramArrayOfint[b] * paramArrayOfint[b] * paramArrayOfint[b]);
            j += paramArrayOfint[b];
        }

        double d4 = 2.0D * d1 - 2.0D * (3.0D * i * i - 6.0D * i + j * (2.0D * i * i - 6.0D * i + 1.0D)) / 5.0D * j * (j + 1.0D) - 1.2D * d2;

        double d5 = ((j * j * j) - d3) / j * (j + 1.0D);
        if (d4 <= 0.0D || d5 <= 0.0D)
            throw new IllegalArgumentException("Invalid sample sizes.");
        double d6 = d1 * (d1 * (d5 - d1) - d4) / 0.5D * d5 * d4;
        double d7 = (d5 - d1) / d1 * d6;
        double d8 = 0.5D * d6;
        double d9 = 0.5D * d7;
        if (d8 <= 0.0D || d9 <= 0.0D)
            throw new IllegalArgumentException("Invalid sample sizes.");
        double d10 = paramDouble / d5;
        if (d10 >= 1.0D) return 0.0D;

        return 1.0D - Beta.incompleteBeta(d10, d8, d9, Maths.lnB(d8, d9));
    }

    public static double chiSquaredApproxSP(int paramInt, double paramDouble) {
        if (paramInt < 2)
            throw new IllegalArgumentException("Less than two samples.");
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Negative H.");
        ChiSquared chiSquared = new ChiSquared((paramInt - 1));
        return 1.0D - chiSquared.cdf(paramDouble);
    }

    public static double gammaApproxSP(int[] paramArrayOfint, double paramDouble) {
        int i = 0;
        int j = paramArrayOfint.length;
        if (j < 2)
            throw new IllegalArgumentException("Less than 2 samples.");
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Negative H.");
        double d1 = (j - 1);
        double d2 = 0.0D;
        for (byte b = 0; b < j; b++) {

            i += paramArrayOfint[b];
            if (paramArrayOfint[b] < 1)
                throw new IllegalArgumentException("Less than one data value in a sample.");
            d2 += 1.0D / paramArrayOfint[b];
        }
        double d3 = 2.0D * d1 - 2.0D * (3.0D * j * j - 6.0D * j + i * (2.0D * j * j - 6.0D * j + 1.0D)) / 5.0D * i * (i + 1.0D) - 1.2D * d2;

        if (d3 <= 0.0D) {
            throw new IllegalArgumentException("Invalid sample sizes.");
        }

        return 1.0D - Gamma.incompleteGamma(paramDouble * d1 / d3, d1 * d1 / d3);
    }

    public double exactSP() {
        MultiSetPermutations multiSetPermutations = new MultiSetPermutations(this.ns);

        double d = Maths.multinomialCoefficient(this.ns);

        double[] arrayOfDouble1 = new double[this.N];
        int[] arrayOfInt = new int[this.k];
        double[] arrayOfDouble2 = new double[this.k];

        long l = 0L;

        while (multiSetPermutations.hasNext()) {

            double d1 = 0.0D;

            MultiSetPermutation multiSetPermutation = multiSetPermutations.nextPermutation();
            int[] arrayOfInt1 = multiSetPermutation.toIntArray();

            arrayOfInt[0] = 0;
            byte b1;
            for (b1 = 1; b1 < this.k; ) {
                arrayOfInt[b1] = arrayOfInt[b1 - 1] + this.ns[b1 - 1];
                b1++;
            }

            for (b1 = 0; b1 < this.N; b1++) {

                int i = arrayOfInt1[b1] - 1;

                arrayOfDouble1[arrayOfInt[i]] = this.originalSample[b1];
                arrayOfInt[i] = arrayOfInt[i] + 1;
            }

            byte b2 = 0;
            for (b1 = 0; b1 < this.k; b1++) {

                arrayOfDouble2[b1] = 0.0D;
                for (byte b = 0; b < this.ns[b1]; ) {
                    arrayOfDouble2[b1] = arrayOfDouble2[b1] + arrayOfDouble1[b2++];
                    b++;
                }

            }

            for (b1 = 0; b1 < this.k; ) {
                arrayOfDouble2[b1] = arrayOfDouble2[b1] / this.ns[b1];
                b1++;
            }

            for (b1 = 0; b1 < this.k; b1++) {

                double d3 = arrayOfDouble2[b1] - this.Rbar;

                d1 += this.ns[b1] * d3 * d3;
            }

            double d2 = this.c * d1;
            if (d2 >= this.H) l++;

        }

        arrayOfDouble1 = null;
        arrayOfInt = null;
        arrayOfDouble2 = null;

        return l / d;
    }

    public double getMeanRank(int paramInt) {
        return this.R[paramInt];
    }

    public double getPermutationCount() {
        return Maths.multinomialCoefficient(this.ns);
    }

    public int getN() {
        return this.N;
    }

    public int getSize(int paramInt) {
        return this.ns[paramInt];
    }

    public int getK() {
        return this.k;
    }

    public double getSP() {
        return this.SP;
    }

    public double getStatistic() {
        return this.H;
    }

    public double getTestStatistic() {
        return this.H;
    }

    public double getZ(int paramInt) {
        return this.z[paramInt];
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            String[] arrayOfString = {"1", "1", "3", "1", "1", "3", "2", "2", "2", "1", "3", "2", "3", "2", "3", "3"};
            double[] arrayOfDouble = {15.1D, 13.0D, 16.2D, 14.9D, 13.2D, 13.8D, 13.1D, 13.0D, 12.9D, 11.9D, 17.0D, 12.8D, 14.7D, 12.0D, 15.0D, 16.6D};

            GroupedData groupedData = new GroupedData(arrayOfDouble, arrayOfString);
            int i = groupedData.getGroupCount();
            int[] arrayOfInt = groupedData.getSizes();
            KruskalWallisTest kruskalWallisTest = new KruskalWallisTest(groupedData, 0.0D, true);
            for (byte b = 0; b < i; b++)
                System.out.println(groupedData.getLabel(b) + "\tn = " + groupedData.getSize(b) + "\tAve.rank = " + kruskalWallisTest.getMeanRank(b) + "\tz = " + kruskalWallisTest.getZ(b));
            double d1 = kruskalWallisTest.getTestStatistic();
            System.out.println("H = " + d1 + " " + (long) kruskalWallisTest.getPermutationCount() + " permutations");
            System.out.println("                   SP = " + kruskalWallisTest.getSP());
            System.out.println("       Beta approx SP = " + KruskalWallisTest.betaApproxSP(arrayOfInt, d1));
            System.out.println("      Gamma approx SP = " + KruskalWallisTest.gammaApproxSP(arrayOfInt, d1));
            System.out.println("Chi-squared approx SP = " + KruskalWallisTest.chiSquaredApproxSP(i, d1));

            long l1 = System.currentTimeMillis();
            double d2 = kruskalWallisTest.exactSP();
            long l2 = System.currentTimeMillis();
            System.out.println("       Exact(test) SP = " + d2);
            System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
        }
    }
}

