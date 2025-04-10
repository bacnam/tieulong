package jsc.onesample;

import jsc.combinatorics.Enumerator;
import jsc.combinatorics.GrayCode;
import jsc.combinatorics.Selection;
import jsc.datastructures.PairedData;
import jsc.descriptive.MeanVar;
import jsc.statistics.PermutableStatistic;
import jsc.tests.H1;

public class PairedTtest
extends Ttest
implements PermutableStatistic
{
double rootN;
double[] diffs;
double[] permutedDiffs;

public PairedTtest(PairedData paramPairedData, H1 paramH1) {
super(paramPairedData.differences(), 0.0D, paramH1);

this.rootN = Math.sqrt(this.n);
this.diffs = paramPairedData.differences();
this.permutedDiffs = new double[this.n];
}

public PairedTtest(PairedData paramPairedData) {
this(paramPairedData, H1.NOT_EQUAL);
}

public Enumerator getEnumerator() {
return (Enumerator)new GrayCode(this.n);
}

public double permuteStatistic(Selection paramSelection) {
int[] arrayOfInt = paramSelection.toIntArray();

for (byte b = 0; b < this.n; b++) {

if (arrayOfInt[b] == 0) {
this.permutedDiffs[b] = this.diffs[b];
} else {
this.permutedDiffs[b] = -this.diffs[b];
} 
} 
MeanVar meanVar = new MeanVar(this.permutedDiffs);
return meanVar.getMean() / meanVar.getSd() / this.rootN;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D };
double[] arrayOfDouble2 = { 75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D };
PairedData pairedData = new PairedData(arrayOfDouble1, arrayOfDouble2);
PairedTtest pairedTtest1 = new PairedTtest(pairedData);
System.out.println("H1: means not equal: t = " + pairedTtest1.getTestStatistic() + " SP = " + pairedTtest1.getSP());
PairedTtest pairedTtest2 = new PairedTtest(pairedData, H1.LESS_THAN);
System.out.println("H1: mean A < mean B: t = " + pairedTtest2.getTestStatistic() + " SP = " + pairedTtest2.getSP());
PairedTtest pairedTtest3 = new PairedTtest(pairedData, H1.GREATER_THAN);
System.out.println("H1: mean A > mean B: t = " + pairedTtest3.getTestStatistic() + " SP = " + pairedTtest3.getSP());
}
}
}

