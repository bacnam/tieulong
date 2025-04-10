package jsc.tests;

import jsc.combinatorics.Enumerator;
import jsc.combinatorics.Selection;
import jsc.distributions.Tail;
import jsc.event.StatisticEvent;
import jsc.event.StatisticListener;
import jsc.independentsamples.MannWhitneyTest;
import jsc.statistics.PermutableStatistic;

public class PermutationTest
implements SignificanceTest
{
private long critCount = 0L;
private double totalRepCount = 0.0D;
private final double tObs;
private double SP = 0.0D;

private final Tail tail;

private final int N;

private final double permCount;

private Enumerator perm;

private PermutableStatistic permutableStatistic;

private StatisticListener statisticListener;

public PermutationTest(PermutableStatistic paramPermutableStatistic, Tail paramTail, boolean paramBoolean, int paramInt, double paramDouble, StatisticListener paramStatisticListener) {
this.tail = paramTail;
this.permutableStatistic = paramPermutableStatistic;
this.tObs = paramPermutableStatistic.getStatistic();

if (paramStatisticListener != null) this.statisticListener = paramStatisticListener;

this.N = paramPermutableStatistic.getN();

this.perm = paramPermutableStatistic.getEnumerator();
this.permCount = this.perm.countSelections();

if (paramBoolean && paramInt < this.permCount) {

if (paramDouble <= 0.0D) {
calculateSP(paramInt);
} else {
calculateSP(paramInt, paramDouble);
} 
} else {

calculateSP();
} 
}

public PermutationTest(PermutableStatistic paramPermutableStatistic, Tail paramTail, boolean paramBoolean, int paramInt, double paramDouble) {
this(paramPermutableStatistic, paramTail, paramBoolean, paramInt, paramDouble, null);
}

public PermutationTest(PermutableStatistic paramPermutableStatistic, Tail paramTail, boolean paramBoolean, int paramInt) {
this(paramPermutableStatistic, paramTail, paramBoolean, paramInt, -1.0D, null);
}

public PermutationTest(PermutableStatistic paramPermutableStatistic, Tail paramTail) {
this(paramPermutableStatistic, paramTail, false, 0, 0.0D, null);
}

public PermutationTest(PermutableStatistic paramPermutableStatistic) {
this(paramPermutableStatistic, Tail.UPPER, false, 0, 0.0D, null);
}

public double calculateSP() {
this.critCount = 0L;

while (this.perm.hasNext()) { Selection selection = this.perm.nextSelection(); processPermutation(selection); }

this.totalRepCount = this.permCount;
this.SP = this.critCount / this.permCount;
return this.SP;
}

public double calculateSP(int paramInt) {
if (this.totalRepCount >= this.permCount) return this.SP;

for (byte b = 0; b < paramInt; b++) {

Selection selection = this.perm.randomSelection();
if (selection == null)
break;  processPermutation(selection);
this.totalRepCount++;
} 
this.SP = this.critCount / this.totalRepCount;
return this.SP;
}

public double calculateSP(int paramInt, double paramDouble) {
while (true) {
double d = this.SP;
calculateSP(paramInt);
if (Math.abs(this.SP - d) < paramDouble) {
return this.SP;
}
} 
}

public double getPermutationCount() {
return this.permCount;
}

public double getSP() {
return this.SP;
}

public double getTestStatistic() {
return this.tObs;
}

public double getTotalRepCount() {
return this.totalRepCount;
}

private void processPermutation(Selection paramSelection) {
double d = this.permutableStatistic.permuteStatistic(paramSelection);

if (this.statisticListener != null) {
this.statisticListener.statisticCreated(new StatisticEvent(this, d));
}

if (this.tail == Tail.UPPER)
{ if (d >= this.tObs) this.critCount++;
}
else if (this.tail == Tail.LOWER)
{ if (d <= this.tObs) this.critCount++;
}

else if (Math.abs(d) >= Math.abs(this.tObs)) { this.critCount++; }

}

public void setSeed(long paramLong) {
this.perm.setSeed(paramLong);
}

static class Test
{
static class StatListener
implements StatisticListener
{
public void statisticCreated(StatisticEvent param2StatisticEvent) {
System.out.print(" " + param2StatisticEvent.getStatistic());
}
}

public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 78.0D, 64.0D, 75.0D, 45.0D, 82.0D };
double[] arrayOfDouble2 = { 110.0D, 70.0D, 53.0D, 51.0D };

MannWhitneyTest mannWhitneyTest = new MannWhitneyTest(arrayOfDouble1, arrayOfDouble2, H1.LESS_THAN, 0.0D, false);
System.out.println("SP = " + mannWhitneyTest.getSP() + " U = " + mannWhitneyTest.getTestStatistic());
PermutationTest permutationTest = new PermutationTest((PermutableStatistic)mannWhitneyTest, Tail.LOWER, false, 0, 0.0D, null);
System.out.println("SP = " + permutationTest.getSP() + " tObs = " + permutationTest.getTestStatistic());
mannWhitneyTest = new MannWhitneyTest(arrayOfDouble1, arrayOfDouble2, H1.GREATER_THAN, 0.0D, false);
System.out.println("SP = " + mannWhitneyTest.getSP() + " U = " + mannWhitneyTest.getTestStatistic());
permutationTest = new PermutationTest((PermutableStatistic)mannWhitneyTest, Tail.LOWER, false, 0, 0.0D, null);
System.out.println("SP = " + permutationTest.getSP() + " tObs = " + permutationTest.getTestStatistic());
}
}
}

