package jsc.relatedsamples;

import jsc.datastructures.MatchedData;
import jsc.descriptive.MeanVar;
import jsc.distributions.FishersF;
import jsc.tests.SignificanceTest;

public class TwoWayANOVA
implements SignificanceTest
{
private final int N;
private final int treatmentCount;
private final int blockCount;
private final double F;
private final double SP;
private double tess;
private double bess;
private final double tems;
private final double bems;
private double tss;
private final double rss;
private final double rms;
private final MatchedData resids;

public TwoWayANOVA(MatchedData paramMatchedData) {
this.treatmentCount = paramMatchedData.getTreatmentCount();
this.blockCount = paramMatchedData.getBlockCount();
if (this.treatmentCount < 2)
throw new IllegalArgumentException("Less than two treatments."); 
if (this.blockCount < 2)
throw new IllegalArgumentException("Less than two blocks."); 
this.N = paramMatchedData.getN();

this.resids = paramMatchedData.copy();

MeanVar meanVar = new MeanVar(paramMatchedData.getBlockPackedCopy());
this.tss = (this.N - 1.0D) * meanVar.getVariance();

double d = this.resids.sweepByBlocks();
System.out.println("brss = " + d);
this.bess = this.tss - d;

this.rss = this.resids.sweepByTreatments();
System.out.println("rss = " + this.rss);
this.tess = d - this.rss;

this.bems = this.bess / (this.blockCount - 1);
this.tems = this.tess / (this.treatmentCount - 1);
this.rms = this.rss / ((this.blockCount - 1) * (this.treatmentCount - 1));

this.F = this.tems / this.rms;
this.SP = FishersF.upperTailProb(this.F, (this.treatmentCount - 1), ((this.treatmentCount - 1) * (this.blockCount - 1)));
}

public int getN() {
return this.N;
}

public int getBlockCount() {
return this.blockCount;
}

public double getBlockEss() {
return this.bess;
}

public double getBlockEms() {
return this.bems;
}

public int getTreatmentCount() {
return this.treatmentCount;
}

public double getTreatmentEss() {
return this.tess;
}

public double getTreatmentEms() {
return this.tems;
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

public MatchedData getResiduals() {
return this.resids;
}

public double getSP() {
return this.SP;
}

public double getTestStatistic() {
return this.F;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[][] arrayOfDouble = { { 3.93D, 3.99D, 4.08D }, { 3.78D, 3.96D, 3.94D }, { 3.88D, 3.96D, 4.02D }, { 3.93D, 4.03D, 4.06D }, { 3.84D, 4.1D, 3.94D }, { 3.75D, 4.02D, 4.09D }, { 3.98D, 4.06D, 4.17D }, { 3.84D, 3.92D, 4.12D } };

MatchedData matchedData = new MatchedData(arrayOfDouble);
TwoWayANOVA twoWayANOVA = new TwoWayANOVA(matchedData);
System.out.print(twoWayANOVA.getResiduals().toString());
System.out.println("Treatment SS = " + twoWayANOVA.getTreatmentEss() + " MS = " + twoWayANOVA.getTreatmentEms());
System.out.println("    Block SS = " + twoWayANOVA.getBlockEss() + " MS = " + twoWayANOVA.getBlockEms());
System.out.println("         RSS = " + twoWayANOVA.getRss() + " RMS = " + twoWayANOVA.getRms());
System.out.println("         TSS = " + twoWayANOVA.getTss());
System.out.println("F = " + twoWayANOVA.getTestStatistic() + " SP = " + twoWayANOVA.getSP());
}
}
}

