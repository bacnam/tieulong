package jsc.correlation;

import jsc.datastructures.PairedData;
import jsc.distributions.HotellingPabstS;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;
import jsc.util.Rank;

public class SpearmanCorrelation
        implements SignificanceTest {
    static final int N_EXACT = 10;
    private final int n;
    private final double r;
    private double S;
    private double SP;

    public SpearmanCorrelation(PairedData paramPairedData, H1 paramH1, double paramDouble, boolean paramBoolean) {
        this.n = paramPairedData.getN();

        Rank rank1 = new Rank(paramPairedData.getX(), paramDouble);
        Rank rank2 = new Rank(paramPairedData.getY(), paramDouble);
        double[] arrayOfDouble1 = rank1.getRanks();
        double[] arrayOfDouble2 = rank2.getRanks();

        this.S = 0.0D;

        for (byte b = 0; b < this.n; b++)
            this.S += (arrayOfDouble1[b] - arrayOfDouble2[b]) * (arrayOfDouble1[b] - arrayOfDouble2[b]);
        double d1 = (this.n * this.n * this.n - this.n - rank1.getCorrectionFactor1());
        double d2 = (this.n * this.n * this.n - this.n - rank2.getCorrectionFactor1());
        this.r = (d1 + d2 - this.S * 12.0D) / 2.0D * Math.sqrt(d1 * d2);

        if (paramH1 == H1.LESS_THAN) {

            this.SP = HotellingPabstS.upperTailProb(this.n, (int) Math.round(this.S), paramBoolean);
        } else if (paramH1 == H1.GREATER_THAN) {

            this.SP = HotellingPabstS.lowerTailProb(this.n, (int) Math.round(this.S), paramBoolean);

        } else if (this.r < 0.0D) {
            this.SP = 2.0D * HotellingPabstS.upperTailProb(this.n, (int) Math.round(this.S), paramBoolean);
        } else if (this.r > 0.0D) {
            this.SP = 2.0D * HotellingPabstS.lowerTailProb(this.n, (int) Math.round(this.S), paramBoolean);
        } else {
            this.SP = 1.0D;
        }
        if (this.SP > 1.0D) this.SP = 1.0D;

    }

    public SpearmanCorrelation(PairedData paramPairedData, H1 paramH1, double paramDouble) {
        this(paramPairedData, paramH1, paramDouble, (paramPairedData.getN() > 10));
    }

    public SpearmanCorrelation(PairedData paramPairedData, H1 paramH1) {
        this(paramPairedData, paramH1, 0.0D, (paramPairedData.getN() > 10));
    }

    public SpearmanCorrelation(PairedData paramPairedData) {
        this(paramPairedData, H1.NOT_EQUAL, 0.0D, (paramPairedData.getN() > 10));
    }

    public int getN() {
        return this.n;
    }

    public double getR() {
        return this.r;
    }

    public double getS() {
        return this.S;
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.r;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble1 = {1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D};
            double[] arrayOfDouble2 = {124.0D, 117.0D, 117.0D, 120.0D, 120.0D, 114.0D, 114.0D};
            double[] arrayOfDouble3 = {131.0D, 125.0D, 116.0D, 113.0D, 124.0D, 118.0D, 102.0D};
            double[] arrayOfDouble4 = {100.0D, 97.0D, 103.0D, 108.0D, 106.0D, 95.0D, 96.0D};

            SpearmanCorrelation spearmanCorrelation = new SpearmanCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2), H1.LESS_THAN);
            System.out.println("n = " + spearmanCorrelation.getN() + " r = " + spearmanCorrelation.getR() + " SP = " + spearmanCorrelation.getSP());
            spearmanCorrelation = new SpearmanCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble3), H1.LESS_THAN);
            System.out.println("n = " + spearmanCorrelation.getN() + " r = " + spearmanCorrelation.getR() + " SP = " + spearmanCorrelation.getSP());
            spearmanCorrelation = new SpearmanCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble4), H1.LESS_THAN);
            System.out.println("n = " + spearmanCorrelation.getN() + " r = " + spearmanCorrelation.getR() + " SP = " + spearmanCorrelation.getSP());
        }
    }
}

