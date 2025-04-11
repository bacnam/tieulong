package jsc.relatedsamples;

import jsc.datastructures.MatchedData;
import jsc.distributions.Beta;
import jsc.distributions.ChiSquared;
import jsc.distributions.FriedmanM;
import jsc.tests.SignificanceTest;
import jsc.util.Maths;

public class FriedmanTest
        implements SignificanceTest {
    private final MatchedData ranks;
    int k;
    int n;
    double C;
    double M;
    double S;
    double W;
    private double SP;

    public FriedmanTest(MatchedData paramMatchedData, double paramDouble, boolean paramBoolean) {
        int i = 0;

        double d1 = 0.0D;

        this.S = 0.0D;

        this.n = paramMatchedData.getBlockCount();
        this.k = paramMatchedData.getTreatmentCount();
        if (this.k < 2)
            throw new IllegalArgumentException("Less than two samples.");
        if (this.n < 2) {
            throw new IllegalArgumentException("Less than two blocks.");
        }

        this.ranks = paramMatchedData.copy();
        double d2 = 0.5D * (this.k + 1.0D);

        i = this.ranks.rankByBlocks(paramDouble);
        double[][] arrayOfDouble = this.ranks.getData();

        for (byte b = 0; b < this.k; b++) {

            d1 = 0.0D;
            for (byte b1 = 0; b1 < this.n; ) {
                d1 += arrayOfDouble[b1][b];
                b1++;
            }
            d1 /= this.n;

            this.S += (d1 - d2) * (d1 - d2);
        }

        double d3 = (this.k * (this.k + 1));
        double d4 = (this.k * (this.k * this.k - 1));

        this.C = 1.0D - i / this.n * d4;
        this.M = 12.0D * this.n * this.S / d3 / this.C;
        this.W = this.M / this.n * (this.k - 1.0D);

        if (paramBoolean) {
            this.SP = betaApproxSP(this.n, this.k, this.S, this.C);

        } else if ((this.k == 2 && this.n < 25) || (this.k == 3 && this.n < 11) || (this.k == 4 && this.n < 7) || (this.k == 5 && this.n < 5) || (this.k == 6 && this.n < 4) || (this.k == 7 && this.n < 3) || (this.k == 8 && this.n < 3) || (this.k == 9 && this.n < 3) || (this.k == 10 && this.n < 3)) {

            this.SP = exactSP(this.n, this.k, this.M);
        } else {
            this.SP = betaApproxSP(this.n, this.k, this.S, this.C);
        }
    }

    public FriedmanTest(MatchedData paramMatchedData) {
        this(paramMatchedData, 0.0D, false);
    }

    public static double betaApproxSP(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2) {
        if (paramInt2 < 2)
            throw new IllegalArgumentException("Less than two samples.");
        if (paramInt1 < 2)
            throw new IllegalArgumentException("Less than two blocks.");
        if (paramDouble1 < 0.0D)
            throw new IllegalArgumentException("Invalid S value.");
        if (paramDouble2 <= 0.0D || paramDouble2 > 1.0D)
            throw new IllegalArgumentException("Invalid correction factor for ties.");
        double d1 = 0.5D * (paramInt2 - 1.0D) - 1.0D / paramInt1;
        double d2 = (paramInt1 - 1.0D) * d1;
        double d3 = (paramInt1 * paramInt1) * paramDouble1;
        double d4 = paramDouble2 * 12.0D * (d3 - 1.0D) / ((paramInt1 * paramInt1 * paramInt2) * ((paramInt2 * paramInt2) - 1.0D) + 2.0D);

        try {
            return 1.0D - Beta.incompleteBeta(d4, d1, d2, Maths.lnB(d1, d2));
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Cannot calculate beta approximation.");
        }
    }

    public static double chiSquaredApproxSP(int paramInt, double paramDouble) {
        if (paramInt < 2)
            throw new IllegalArgumentException("Less than two samples.");
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid M value.");
        ChiSquared chiSquared = new ChiSquared((paramInt - 1));
        return 1.0D - chiSquared.cdf(paramDouble);
    }

    public static double exactSP(int paramInt1, int paramInt2, double paramDouble) {
        if (paramInt2 < 2)
            throw new IllegalArgumentException("Less than two samples.");
        if (paramInt1 < 2)
            throw new IllegalArgumentException("Less than two blocks.");
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid M value.");
        FriedmanM friedmanM = new FriedmanM(paramInt1, paramInt2);
        return 1.0D - friedmanM.cdf(paramDouble);
    }

    public double getC() {
        return this.C;
    }

    public MatchedData getRanks() {
        return this.ranks;
    }

    public double getS() {
        return this.S;
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.M;
    }

    public double getW() {
        return this.W;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            String[] arrayOfString1 = {"1", "2", "3"};
            String[] arrayOfString2 = {"A", "B", "C", "D"};
            double[][] arrayOfDouble = {{8.5D, 8.9D, 8.8D, 8.8D}, {8.2D, 8.4D, 8.2D, 8.2D}, {8.9D, 9.1D, 9.1D, 8.9D}};

            MatchedData matchedData = new MatchedData(arrayOfDouble, arrayOfString1, arrayOfString2);
            int i = matchedData.getTreatmentCount();
            int j = matchedData.getBlockCount();
            System.out.println("n = " + j + " k = " + i);
            FriedmanTest friedmanTest = new FriedmanTest(matchedData, 0.0D, false);
            System.out.print(friedmanTest.getRanks().toString());
            double d1 = friedmanTest.getTestStatistic();
            double d2 = friedmanTest.getS();
            double d3 = friedmanTest.getC();
            System.out.println("S = " + friedmanTest.getS() + " M = " + d1 + " W = " + friedmanTest.getW());
            System.out.println("Chi-squared approx SP = " + FriedmanTest.chiSquaredApproxSP(i, d1));
            System.out.println("                   SP = " + friedmanTest.getSP());
            System.out.println("       Beta approx SP = " + FriedmanTest.betaApproxSP(j, i, d2, d3));
            System.out.println("       Exact(test) SP = " + FriedmanTest.exactSP(j, i, d1));
        }
    }
}

