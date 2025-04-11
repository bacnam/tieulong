package jsc.onesample;

import jsc.datastructures.PairedData;
import jsc.distributions.Normal;
import jsc.distributions.Poisson;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;
import jsc.util.Rank;

public class WilcoxonTest
        implements SignificanceTest {
    static final int SMALL_SAMPLE_SIZE = 499;
    static final double LOG2 = Math.log(2.0D);

    final H1 alternative;
    private final double T;
    private final double SP;
    int n1;
    private double s2Sum;
    private double[] signedRanks;

    public WilcoxonTest(double[] paramArrayOfdouble, double paramDouble1, H1 paramH1, double paramDouble2, boolean paramBoolean1, boolean paramBoolean2) {
        this.alternative = paramH1;
        int i = paramArrayOfdouble.length;

        double d1 = 0.0D;
        double d2 = 0.0D;

        this.n1 = 0;
        int[] arrayOfInt = new int[i];
        double[] arrayOfDouble = new double[i];

        byte b;
        for (b = 0; b < i; b++) {

            double d = paramArrayOfdouble[b] - paramDouble1;
            if (paramBoolean1 || Math.abs(d) > paramDouble2) {
                arrayOfDouble[this.n1] = Math.abs(d);

                arrayOfInt[this.n1] = (d >= 0.0D) ? 1 : -1;
                this.n1++;
            }
        }
        if (this.n1 < 1) {
            throw new IllegalArgumentException("No non-zero differences.");
        }

        Rank rank = new Rank(this.n1, arrayOfDouble, paramDouble2);

        this.signedRanks = new double[this.n1];

        this.s2Sum = 0.0D;
        for (b = 0; b < this.n1; b++) {

            double d = rank.getRank(b);
            this.s2Sum += d * d;
            if (arrayOfInt[b] > 0) {

                d1 += d;
                this.signedRanks[b] = d;
            } else {

                d2 += d;
                this.signedRanks[b] = -d;
            }
        }
        if (paramH1 == H1.LESS_THAN) {
            this.T = d1;
        } else if (paramH1 == H1.GREATER_THAN) {
            this.T = d2;
        } else {
            this.T = Math.min(d1, d2);
        }

        if (paramBoolean2) {
            this.SP = approxSP();
        } else {
            this.SP = exactSP();
        }
    }

    public WilcoxonTest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1, boolean paramBoolean) {
        this(paramArrayOfdouble, paramDouble, paramH1, 0.0D, paramBoolean, (paramArrayOfdouble.length > 499));
    }

    public WilcoxonTest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1) {
        this(paramArrayOfdouble, paramDouble, paramH1, 0.0D, false, (paramArrayOfdouble.length > 499));
    }

    public WilcoxonTest(double[] paramArrayOfdouble, double paramDouble) {
        this(paramArrayOfdouble, paramDouble, H1.NOT_EQUAL, 0.0D, false, (paramArrayOfdouble.length > 499));
    }

    public WilcoxonTest(PairedData paramPairedData, H1 paramH1, double paramDouble, boolean paramBoolean1, boolean paramBoolean2) {
        this(paramPairedData.differences(), 0.0D, paramH1, paramDouble, paramBoolean1, paramBoolean2);
    }

    public WilcoxonTest(PairedData paramPairedData, H1 paramH1, boolean paramBoolean) {
        this(paramPairedData.differences(), 0.0D, paramH1, 0.0D, paramBoolean, (paramPairedData.getN() > 24));
    }

    public WilcoxonTest(PairedData paramPairedData, H1 paramH1) {
        this(paramPairedData.differences(), 0.0D, paramH1, 0.0D, false, (paramPairedData.getN() > 24));
    }

    public WilcoxonTest(PairedData paramPairedData) {
        this(paramPairedData.differences(), 0.0D, H1.NOT_EQUAL, 0.0D, false, (paramPairedData.getN() > 24));
    }

    public double approxSP() {
        double d = getZ();

        if (this.alternative == H1.NOT_EQUAL) {
            return 2.0D * Normal.standardTailProb(d, (d > 0.0D));
        }
        return Normal.standardTailProb(d, false);
    }

    public double exactSP() {
        FisherSymmetryTest fisherSymmetryTest = new FisherSymmetryTest(this.signedRanks, this.alternative);
        return fisherSymmetryTest.getSP();
    }

    public int getN() {
        return this.n1;
    }

    public double[] getSignedRanks() {
        return this.signedRanks;
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.T;
    }

    public double getZ() {
        double d = this.T - 0.25D * this.n1 * (this.n1 + 1.0D);
        return (d - ((d < 0.0D) ? -0.5D : 0.5D)) / Math.sqrt(0.25D * this.s2Sum);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = 'Ǵ';
            double[] arrayOfDouble1 = new double[c];
            double[] arrayOfDouble2 = new double[c];

            Poisson poisson1 = new Poisson(1.0D);
            poisson1.setSeed(100L);

            Poisson poisson2 = new Poisson(1.5D);
            poisson2.setSeed(200L);
            byte b;
            for (b = 0; b < c; ) {
                arrayOfDouble1[b] = poisson1.random();
                b++;
            }
            for (b = 0; b < c; ) {
                arrayOfDouble2[b] = poisson2.random();
                b++;
            }
            PairedData pairedData = new PairedData(arrayOfDouble1, arrayOfDouble2);
            WilcoxonTest wilcoxonTest = new WilcoxonTest(pairedData, H1.NOT_EQUAL, 0.0D, false, false);
            System.out.println("T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP() + " Approx SP=" + wilcoxonTest.approxSP());

            double[] arrayOfDouble3 = {-2.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 4.0D, 4.0D, 1.0D, 1.0D, 5.0D, 3.0D, 5.0D, 3.0D, -1.0D, 1.0D, -1.0D, 5.0D, 8.0D, 2.0D, 2.0D, 2.0D, -3.0D, -2.0D, 1.0D, 4.0D, 8.0D, 2.0D, 3.0D, -1.0D};

            double d1 = 0.0D;
            double d2 = 0.0D;
            boolean bool1 = false;
            boolean bool2 = false;
            System.out.println("Normal approx: " + bool1);
            System.out.println("Include zeros: " + bool2);
            wilcoxonTest = new WilcoxonTest(arrayOfDouble3, d1, H1.NOT_EQUAL, d2, bool2, bool1);
            System.out.println("H1:m <> " + d1 + " N=" + wilcoxonTest.getN() + " T=" + wilcoxonTest.getTestStatistic() + " SP=" + wilcoxonTest.getSP());
            wilcoxonTest = new WilcoxonTest(arrayOfDouble3, d1, H1.LESS_THAN, d2, bool2, bool1);
            System.out.println("H1:m < " + d1 + " N=" + wilcoxonTest.getN() + " T=" + wilcoxonTest.getTestStatistic() + " SP=" + wilcoxonTest.getSP());
            wilcoxonTest = new WilcoxonTest(arrayOfDouble3, d1, H1.GREATER_THAN, d2, bool2, bool1);
            System.out.println("H1:m > " + d1 + " N=" + wilcoxonTest.getN() + " T=" + wilcoxonTest.getTestStatistic() + " SP=" + wilcoxonTest.getSP());

            double[] arrayOfDouble4 = {70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D};
            double[] arrayOfDouble5 = {75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D};
            pairedData = new PairedData(arrayOfDouble4, arrayOfDouble5);
            wilcoxonTest = new WilcoxonTest(pairedData, H1.NOT_EQUAL, 0.0D, false, false);
            System.out.println("H1: averages not equal: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());
            wilcoxonTest = new WilcoxonTest(pairedData, H1.LESS_THAN);
            System.out.println("H1: average A < average B: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());
            wilcoxonTest = new WilcoxonTest(pairedData, H1.GREATER_THAN);
            System.out.println("H1: average A > average B: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());

            double[] arrayOfDouble6 = {17.4D, 15.7D, 12.9D, 9.8D, 13.4D, 18.7D, 13.9D, 11.0D, 5.4D, 10.4D, 16.4D, 5.6D};
            double[] arrayOfDouble7 = {13.6D, 10.1D, 10.3D, 9.2D, 11.1D, 20.4D, 10.4D, 11.4D, 4.9D, 8.9D, 11.2D, 4.8D};
            pairedData = new PairedData(arrayOfDouble6, arrayOfDouble7);
            wilcoxonTest = new WilcoxonTest(pairedData, H1.GREATER_THAN);
            System.out.println("H1: average A > average B: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());

            double[] arrayOfDouble8 = {20.1D, 19.5D, 19.0D, 21.1D, 23.1D, 22.6D, 18.9D, 22.8D, 27.1D, 19.8D, 21.7D, 18.9D, 20.4D};
            double[] arrayOfDouble9 = {21.2D, 18.7D, 19.0D, 20.8D, 19.9D, 21.4D, 17.9D, 23.1D, 24.3D, 18.5D, 20.3D, 18.7D, 19.4D};
            pairedData = new PairedData(arrayOfDouble8, arrayOfDouble9);
            wilcoxonTest = new WilcoxonTest(pairedData, H1.GREATER_THAN);
            System.out.println("H1: average A > average B: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());

            double[] arrayOfDouble10 = {82.0D, 69.0D, 73.0D, 43.0D, 58.0D, 56.0D, 76.0D, 65.0D};

            double[] arrayOfDouble11 = {63.0D, 42.0D, 74.0D, 37.0D, 51.0D, 43.0D, 80.0D, 62.0D};
            pairedData = new PairedData(arrayOfDouble10, arrayOfDouble11);
            wilcoxonTest = new WilcoxonTest(pairedData, H1.NOT_EQUAL, 0.0D, false, false);
            System.out.println("H1:A <> B: T=" + wilcoxonTest.getTestStatistic() + " z=" + wilcoxonTest.getZ() + " SP=" + wilcoxonTest.getSP());
        }
    }
}

