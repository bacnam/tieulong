package jsc.independentsamples;

import jsc.distributions.Tail;
import jsc.event.StatisticListener;
import jsc.tests.H1;
import jsc.util.Arrays;

public class TwoSampleBootstrapDistsTest
extends TwoSampleBootstrapTest
{
private double[] xA;
private double[] xB;
private double[] x;

public TwoSampleBootstrapDistsTest(TwoSampleStatistic paramTwoSampleStatistic, Tail paramTail, int paramInt) {
this(paramTwoSampleStatistic, paramTail, paramInt, (StatisticListener)null);
}

public TwoSampleBootstrapDistsTest(TwoSampleStatistic paramTwoSampleStatistic, Tail paramTail, int paramInt, StatisticListener paramStatisticListener) {
super(paramTwoSampleStatistic, paramTail, paramStatisticListener);

this.x = Arrays.append(paramTwoSampleStatistic.getSampleA(), paramTwoSampleStatistic.getSampleB());
this.xA = new double[this.nA];
this.xB = new double[this.nB];

calculateSP(paramInt);
}

protected double bootstrapSample() {
byte b;
for (b = 0; b < this.nA; ) { this.xA[b] = this.x[this.rand.nextInt(this.N)]; b++; }
for (b = 0; b < this.nB; ) { this.xB[b] = this.x[this.rand.nextInt(this.N)]; b++; }

return this.t.resampleStatistic(this.xA, this.xB);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 94.0D, 197.0D, 16.0D, 38.0D, 99.0D, 141.0D, 23.0D };
double[] arrayOfDouble2 = { 52.0D, 104.0D, 146.0D, 10.0D, 50.0D, 31.0D, 40.0D, 27.0D, 46.0D };
TwoSampleTtest twoSampleTtest = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2, H1.GREATER_THAN, true);

long l1 = System.currentTimeMillis();
TwoSampleBootstrapDistsTest twoSampleBootstrapDistsTest = new TwoSampleBootstrapDistsTest(twoSampleTtest, Tail.UPPER, 5000000);
long l2 = System.currentTimeMillis();
System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
System.out.println("T = " + twoSampleTtest.getTestStatistic() + " SP = " + twoSampleTtest.getSP());
System.out.println("Bootstrap SP = " + twoSampleBootstrapDistsTest.getSP());
}
}
}

