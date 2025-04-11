package jsc.relatedsamples;

import jsc.datastructures.MatchedData;
import jsc.distributions.Normal;
import jsc.tests.SignificanceTest;

public class PageTest
        implements SignificanceTest {
    private final MatchedData ranks;
    int k;
    int n;
    double L;
    private double SP;

    public PageTest(MatchedData paramMatchedData, String[] paramArrayOfString, double paramDouble) {
        this.L = 0.0D;

        this.n = paramMatchedData.getBlockCount();
        this.k = paramMatchedData.getTreatmentCount();
        if (this.k < 2)
            throw new IllegalArgumentException("Less than two samples.");
        if (this.n < 2)
            throw new IllegalArgumentException("Less than two blocks.");
        if (paramArrayOfString.length != this.k) {
            throw new IllegalArgumentException("Alternative array wrong length.");
        }

        this.ranks = paramMatchedData.copy();

        this.ranks.rankByBlocks(paramDouble);
        double[][] arrayOfDouble = this.ranks.getData();

        for (byte b = 0; b < this.k; b++) {

            double d = 0.0D;
            for (byte b1 = 0; b1 < this.n; ) {
                d += arrayOfDouble[b1][b];
                b1++;
            }

            int i = paramMatchedData.indexOfTreatment(paramArrayOfString[b]);
            if (i < 0)
                throw new IllegalArgumentException("Invalid alternative treatment label.");
            this.L += (1 + i) * d;
        }

        this.SP = approxSP(this.n, this.k, this.L);
    }

    public PageTest(MatchedData paramMatchedData, String[] paramArrayOfString) {
        this(paramMatchedData, paramArrayOfString, 0.0D);
    }

    public static double approxSP(int paramInt1, int paramInt2, double paramDouble) {
        if (paramInt2 < 2)
            throw new IllegalArgumentException("Less than two samples.");
        if (paramInt1 < 2)
            throw new IllegalArgumentException("Less than two blocks.");
        if (paramDouble < 0.0D) {
            throw new IllegalArgumentException("Invalid L value.");
        }

        double d1 = paramInt2;
        double d2 = (paramDouble - 0.25D * paramInt1 * d1 * (d1 + 1.0D) * (d1 + 1.0D) - 0.5D) / Math.sqrt(paramInt1 * d1 * d1 * (d1 + 1.0D) * (d1 * d1 - 1.0D) / 144.0D);

        return Normal.standardTailProb(d2, true);
    }

    public MatchedData getRanks() {
        return this.ranks;
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.L;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            String[] arrayOfString1 = {"1", "2", "3", "4", "5"};
            String[] arrayOfString2 = {"A", "B", "C", "D", "E", "F"};
            double[][] arrayOfDouble = {{62.59D, 67.11D, 73.02D, 92.34D, 83.48D}, {55.75D, 63.03D, 63.93D, 71.61D, 93.73D}, {65.88D, 69.89D, 82.53D, 77.33D, 84.82D}, {66.13D, 66.79D, 85.37D, 76.72D, 98.88D}, {65.73D, 57.85D, 63.08D, 71.75D, 87.04D}, {78.36D, 76.17D, 82.97D, 91.49D, 89.61D}};

            String[] arrayOfString3 = {"1", "2", "3", "4", "5"};

            MatchedData matchedData = new MatchedData(arrayOfDouble, arrayOfString2, arrayOfString1);
            int i = matchedData.getTreatmentCount();
            int j = matchedData.getBlockCount();
            System.out.println("n = " + j + " k = " + i);
            PageTest pageTest = new PageTest(matchedData, arrayOfString3);
            double d = pageTest.getTestStatistic();
            System.out.print(pageTest.getRanks().toString());
            System.out.println("L = " + d + " Approx SP = " + PageTest.approxSP(j, i, d));
        }
    }
}

