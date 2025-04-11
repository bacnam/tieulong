package jsc.independentsamples;

import jsc.datastructures.GroupedData;
import jsc.descriptive.MeanVar;
import jsc.distributions.FishersF;
import jsc.tests.SignificanceTest;

public class OneWayANOVA
        implements SignificanceTest {
    private final int treatmentCount;
    private final int N;
    private final double F;
    private final double SP;
    private final double ess;
    private final double tss;
    private final double ems;
    private final double rms;
    private final MeanVar[] mv;
    private double rss;

    public OneWayANOVA(GroupedData paramGroupedData) {
        this.treatmentCount = paramGroupedData.getGroupCount();
        if (this.treatmentCount < 2)
            throw new IllegalArgumentException("Less than two samples.");
        this.N = paramGroupedData.getN();

        this.mv = new MeanVar[this.treatmentCount];

        MeanVar meanVar = new MeanVar(paramGroupedData.getData());
        double d = meanVar.getMean();
        this.tss = (this.N - 1.0D) * meanVar.getVariance();

        this.rss = 0.0D;
        for (byte b = 0; b < this.treatmentCount; b++) {

            double[] arrayOfDouble = paramGroupedData.getData(b);
            this.mv[b] = new MeanVar(arrayOfDouble);
            for (byte b1 = 0; b1 < this.mv[b].getN(); b1++) {

                double d1 = arrayOfDouble[b1] - this.mv[b].getMean();
                this.rss += d1 * d1;
            }
        }

        this.ess = this.tss - this.rss;

        this.ems = this.ess / (this.treatmentCount - 1);
        this.rms = this.rss / (this.N - this.treatmentCount);
        this.F = this.ems / this.rms;

        this.SP = FishersF.upperTailProb(this.F, (this.treatmentCount - 1), (this.N - this.treatmentCount));
    }

    public int getTreatmentCount() {
        return this.treatmentCount;
    }

    public int getN() {
        return this.N;
    }

    public int getSize(int paramInt) {
        return this.mv[paramInt].getN();
    }

    public double getMean(int paramInt) {
        return this.mv[paramInt].getMean();
    }

    public double getSd(int paramInt) {
        return this.mv[paramInt].getSd();
    }

    public double getEss() {
        return this.ess;
    }

    public double getEms() {
        return this.ems;
    }

    public double getRss() {
        return this.rss;
    }

    public double getRms() {
        return this.rms;
    }

    public double getTss() {
        return this.tss;
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.F;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            String[] arrayOfString = {"1", "1", "1", "1", "2", "2", "2", "2", "3", "3", "3", "3", "4", "4", "4", "4"};
            double[] arrayOfDouble = {18.95D, 12.62D, 11.94D, 14.42D, 10.06D, 7.19D, 7.03D, 14.66D, 10.92D, 13.28D, 14.52D, 12.51D, 9.3D, 21.2D, 16.11D, 21.41D};

            GroupedData groupedData = new GroupedData(arrayOfDouble, arrayOfString);
            OneWayANOVA oneWayANOVA = new OneWayANOVA(groupedData);
            System.out.println(" ESS = " + oneWayANOVA.getEss() + " EMS = " + oneWayANOVA.getEms());
            System.out.println(" RSS = " + oneWayANOVA.getRss() + " RMS = " + oneWayANOVA.getRms());
            System.out.println(" TSS = " + oneWayANOVA.getTss());
            for (byte b = 0; b < oneWayANOVA.getTreatmentCount(); b++)
                System.out.println(groupedData.getLabel(b) + "\tN = " + oneWayANOVA.getSize(b) + "\tMean = " + oneWayANOVA.getMean(b) + "\tsd = " + oneWayANOVA.getSd(b));
            System.out.println("F = " + oneWayANOVA.getTestStatistic() + " SP = " + oneWayANOVA.getSP());
        }
    }
}

