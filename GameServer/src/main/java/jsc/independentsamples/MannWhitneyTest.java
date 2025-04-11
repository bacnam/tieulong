package jsc.independentsamples;

import jsc.combinatorics.Enumerator;
import jsc.combinatorics.MultiSetPermutations;
import jsc.combinatorics.Selection;
import jsc.distributions.MannWhitneyU;
import jsc.distributions.Normal;
import jsc.distributions.Tail;
import jsc.statistics.PermutableStatistic;
import jsc.tests.H1;
import jsc.tests.PermutationTest;
import jsc.tests.SignificanceTest;
import jsc.util.Arrays;
import jsc.util.Maths;
import jsc.util.Rank;

public class MannWhitneyTest
        implements PermutableStatistic, SignificanceTest {
    static final int SMALL_SAMPLE_SIZE = 20;
    final int N;
    final int nAB;
    final H1 alternative;
    final int nA;
    final int nB;
    final Rank ranks;
    final double U;
    final double SP;
    final double tolerance;
    private final double[] originalRanks;
    double RA;
    double RB;
    private double[] permutedRanksA;

    public MannWhitneyTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, double paramDouble, boolean paramBoolean) {
        this.alternative = paramH1;
        this.tolerance = paramDouble;
        this.nA = paramArrayOfdouble1.length;
        this.nB = paramArrayOfdouble2.length;
        if (this.nA < 2 || this.nB < 2)
            throw new IllegalArgumentException("Less than two data values.");
        this.nAB = this.nA * this.nB;

        double[] arrayOfDouble1 = new double[this.nA];
        double[] arrayOfDouble2 = new double[this.nB];
        this.permutedRanksA = new double[this.nA];

        this.N = this.nA + this.nB;

        this.ranks = new Rank(Arrays.append(paramArrayOfdouble2, paramArrayOfdouble1), paramDouble);

        this.originalRanks = this.ranks.getRanks();

        this.RA = 0.0D;
        int i;
        for (i = 0; i < this.nA; i++) {
            double d = this.ranks.getRank(i);
            this.RA += d;
            arrayOfDouble1[i] = d;
        }

        this.RB = 0.0D;
        for (byte b = 0; i < this.N; b++, i++) {
            double d = this.ranks.getRank(i);
            this.RB += d;
            arrayOfDouble2[b] = d;
        }
        double d1 = this.RA - 0.5D * this.nA * (this.nA + 1.0D);
        double d2 = (this.nA * this.nB) - d1;
        if (paramH1 == H1.NOT_EQUAL) {
            this.U = Math.min(d1, d2);
        } else if (paramH1 == H1.LESS_THAN) {
            this.U = d1;
        } else {
            this.U = d2;
        }

        if (paramBoolean) {
            this.SP = approxSP();
        } else {
            this.SP = exactSP();
        }
    }

    public MannWhitneyTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1) {
        this(paramArrayOfdouble1, paramArrayOfdouble2, paramH1, 0.0D, (paramArrayOfdouble1.length > 20 || paramArrayOfdouble2.length > 20));
    }

    public MannWhitneyTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
        this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, 0.0D, (paramArrayOfdouble1.length > 20 || paramArrayOfdouble2.length > 20));
    }

    public double approxSP() {
        double d = getZ();
        if (this.alternative == H1.NOT_EQUAL) {
            return 2.0D * Normal.standardTailProb(d, (d > 0.0D));
        }
        return Normal.standardTailProb(d, false);
    }

    public double exactSP() {
        if (this.ranks.hasTies()) {

            PermutationTest permutationTest = new PermutationTest(this, Tail.LOWER, (this.nA * this.nB > 169), 10000, 1.0E-5D);
            return permutationTest.getSP();
        }

        MannWhitneyU mannWhitneyU = new MannWhitneyU(this.nA, this.nB);
        double d = mannWhitneyU.cdf(Math.ceil(this.U));

        if (this.alternative == H1.NOT_EQUAL) {
            return (d <= 0.5D) ? (2.0D * d) : (2.0D * (1.0D - d));
        }
        return d;
    }

    public int getCorrectionFactor() {
        return this.ranks.getCorrectionFactor1();
    }

    public Enumerator getEnumerator() {
        int[] arrayOfInt = new int[2];
        arrayOfInt[0] = this.nA;
        arrayOfInt[1] = this.nB;
        return (Enumerator) new MultiSetPermutations(arrayOfInt);
    }

    public int getN() {
        return this.N;
    }

    public Rank getRanks() {
        return this.ranks;
    }

    public double getRankSumA() {
        return this.RA;
    }

    public double getRankSumB() {
        return this.RB;
    }

    public double getSP() {
        return this.SP;
    }

    public double getStatistic() {
        return this.U;
    }

    public double getTestStatistic() {
        return this.U;
    }

    public double getZ() {
        int i = this.ranks.getCorrectionFactor1();
        double d1 = this.N;
        double d2 = (d1 * d1 * d1 - d1 - i) / 12.0D;

        double d3 = this.U - 0.5D * this.nAB;

        return (d3 - ((d3 < 0.0D) ? -0.5D : 0.5D)) / Math.sqrt(this.nAB / d1 * (d1 - 1.0D) * d2);
    }

    public double permuteStatistic(Selection paramSelection) {
        byte b1 = 0;

        int[] arrayOfInt = paramSelection.toIntArray();

        for (byte b2 = 0; b2 < this.N; b2++) {

            if (arrayOfInt[b2] == 1) {
                this.permutedRanksA[b1] = this.originalRanks[b2];
                b1++;
            }
        }

        return resampleStatistic(this.permutedRanksA);
    }

    public double resampleStatistic(double[] paramArrayOfdouble) {
        double d1, d2 = 0.0D;
        for (byte b = 0; b < this.nA; ) {
            d2 += paramArrayOfdouble[b];
            b++;
        }

        double d3 = d2 - 0.5D * this.nA * (this.nA + 1.0D);
        double d4 = (this.nA * this.nB) - d3;

        if (this.alternative == H1.NOT_EQUAL) {
            d1 = Math.min(d3, d4);
        } else if (this.alternative == H1.LESS_THAN) {
            d1 = d3;
        } else {
            d1 = d4;
        }

        return d1;
    }

    public int sizeA() {
        return this.nA;
    }

    public int sizeB() {
        return this.nB;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble1 = {90.0D, 72.0D, 61.0D, 66.0D, 81.0D, 69.0D, 59.0D, 70.0D};
            double[] arrayOfDouble2 = {62.0D, 85.0D, 78.0D, 66.0D, 80.0D, 91.0D, 69.0D, 77.0D, 84.0D};

            double[] arrayOfDouble3 = {13.0D, 12.0D, 12.0D, 10.0D, 10.0D, 10.0D, 10.0D, 9.0D, 8.0D, 8.0D, 7.0D, 7.0D, 7.0D, 7.0D, 7.0D, 6.0D};
            double[] arrayOfDouble4 = {17.0D, 16.0D, 15.0D, 15.0D, 15.0D, 14.0D, 14.0D, 14.0D, 13.0D, 13.0D, 13.0D, 12.0D, 12.0D, 12.0D, 12.0D, 11.0D, 11.0D, 10.0D, 10.0D, 10.0D, 8.0D, 8.0D, 6.0D};

            double[] arrayOfDouble5 = {78.0D, 64.0D, 75.0D, 45.0D, 82.0D};
            double[] arrayOfDouble6 = {110.0D, 70.0D, 53.0D, 51.0D};

            double[] arrayOfDouble7 = {3.0D, 7.0D, 15.0D, 10.0D, 4.0D, 6.0D, 4.0D, 7.0D};
            double[] arrayOfDouble8 = {19.0D, 11.0D, 36.0D, 8.0D, 25.0D, 23.0D, 38.0D, 14.0D, 17.0D, 41.0D, 25.0D, 21.0D};

            double[] arrayOfDouble9 = {3.0D, 7.0D, 15.0D, 10.0D, 4.0D, 6.0D, 4.0D, 7.0D};
            double[] arrayOfDouble10 = {19.0D, 11.0D, 36.0D, 7.0D, 25.0D, 23.0D, 38.0D, 15.0D, 17.0D, 41.0D, 25.0D, 21.0D};

            double[] arrayOfDouble11 = {13.0D, 6.0D, 12.0D, 7.0D, 12.0D, 7.0D, 10.0D, 7.0D, 10.0D, 7.0D, 16.0D, 7.0D, 10.0D, 8.0D, 9.0D, 8.0D};
            double[] arrayOfDouble12 = {17.0D, 6.0D, 10.0D, 8.0D, 15.0D, 8.0D, 15.0D, 10.0D, 15.0D, 10.0D, 14.0D, 10.0D, 14.0D, 11.0D, 14.0D, 11.0D, 13.0D, 12.0D, 13.0D, 12.0D, 13.0D, 12.0D, 12.0D};

            String[] arrayOfString = {"Minitab Ref. Manual, p.18-9", "Siegel,S.(1956), pp.121-125", "Siegel,S.(1956), pp.118-120.", "Neave & Worthington (1988), pp.109-113.", "Neave & Worthington (1988), p.116.", "NAG"};

            double[][] arrayOfDouble13 = {arrayOfDouble1, arrayOfDouble3, arrayOfDouble5, arrayOfDouble7, arrayOfDouble9, arrayOfDouble11};
            double[][] arrayOfDouble14 = {arrayOfDouble2, arrayOfDouble4, arrayOfDouble6, arrayOfDouble8, arrayOfDouble10, arrayOfDouble12};
            boolean bool = false;
            for (byte b = 0; b < arrayOfDouble13.length; b++) {

                System.out.println(arrayOfString[b]);
                MannWhitneyTest mannWhitneyTest1 = new MannWhitneyTest(arrayOfDouble13[b], arrayOfDouble14[b], H1.NOT_EQUAL, 0.0D, bool);
                System.out.println("H1:A <> B: U=" + mannWhitneyTest1.getTestStatistic() + " z=" + Maths.round(mannWhitneyTest1.getZ(), 4) + " SP=" + Maths.round(mannWhitneyTest1.getSP(), 4) + " Ra=" + mannWhitneyTest1.getRankSumA() + " Rb=" + mannWhitneyTest1.getRankSumB() + " t=" + mannWhitneyTest1.getCorrectionFactor());

                MannWhitneyTest mannWhitneyTest2 = new MannWhitneyTest(arrayOfDouble13[b], arrayOfDouble14[b], H1.LESS_THAN, 0.0D, bool);
                System.out.println("H1:A < B: U=" + mannWhitneyTest2.getTestStatistic() + " z=" + Maths.round(mannWhitneyTest1.getZ(), 4) + " SP=" + Maths.round(mannWhitneyTest2.getSP(), 4) + " Ra=" + mannWhitneyTest2.getRankSumA() + " Rb=" + mannWhitneyTest2.getRankSumB() + " t=" + mannWhitneyTest2.getCorrectionFactor());

                MannWhitneyTest mannWhitneyTest3 = new MannWhitneyTest(arrayOfDouble13[b], arrayOfDouble14[b], H1.GREATER_THAN, 0.0D, bool);
                System.out.println("H1:A > B: U=" + mannWhitneyTest3.getTestStatistic() + " z=" + Maths.round(mannWhitneyTest1.getZ(), 4) + " SP=" + Maths.round(mannWhitneyTest3.getSP(), 4) + " Ra=" + mannWhitneyTest3.getRankSumA() + " Rb=" + mannWhitneyTest3.getRankSumB() + " t=" + mannWhitneyTest3.getCorrectionFactor());
            }
        }
    }
}

