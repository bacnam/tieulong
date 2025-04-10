package jsc.independentsamples;

import jsc.event.StatisticListener;
import jsc.tests.H1;

public class TwoSampleBootstrapMeansTest
extends TwoSampleBootstrapTest
{
private double[] rA;
private double[] rB;
private final double[] zA;
private final double[] zB;

public TwoSampleBootstrapMeansTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int paramInt) {
this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, paramInt, (StatisticListener)null);
}

public TwoSampleBootstrapMeansTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, int paramInt, StatisticListener paramStatisticListener) {
super(new TwoSampleTtest(paramArrayOfdouble1, paramArrayOfdouble2, paramH1, false), H1.toTail(paramH1), paramStatisticListener);

double d1 = 0.0D, d2 = 0.0D, d3 = 0.0D; byte b;
for (b = 0; b < this.nA; ) { d1 += paramArrayOfdouble1[b]; d3 += paramArrayOfdouble1[b]; b++; }
d1 /= this.nA;
for (b = 0; b < this.nB; ) { d2 += paramArrayOfdouble2[b]; d3 += paramArrayOfdouble2[b]; b++; }
d2 /= this.nB;
d3 /= this.N;

this.rA = new double[this.nA];
this.rB = new double[this.nB];
this.zA = new double[this.nA];
this.zB = new double[this.nB];

for (b = 0; b < this.nA; ) { this.zA[b] = paramArrayOfdouble1[b] - d1 + d3; b++; }
for (b = 0; b < this.nB; ) { this.zB[b] = paramArrayOfdouble2[b] - d2 + d3; b++; }

calculateSP(paramInt);
}

protected double bootstrapSample() {
byte b;
for (b = 0; b < this.nA; ) { this.rA[b] = this.zA[this.rand.nextInt(this.nA)]; b++; }
for (b = 0; b < this.nB; ) { this.rB[b] = this.zB[this.rand.nextInt(this.nB)]; b++; }
return this.t.resampleStatistic(this.rA, this.rB);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 94.0D, 197.0D, 16.0D, 38.0D, 99.0D, 141.0D, 23.0D };
double[] arrayOfDouble2 = { 52.0D, 104.0D, 146.0D, 10.0D, 50.0D, 31.0D, 40.0D, 27.0D, 46.0D };
H1 h1 = H1.NOT_EQUAL;
TwoSampleTtest twoSampleTtest = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2, h1, false);
System.out.println("T = " + twoSampleTtest.getTestStatistic() + " SP = " + twoSampleTtest.getSP());

long l1 = System.currentTimeMillis();
TwoSampleBootstrapMeansTest twoSampleBootstrapMeansTest = new TwoSampleBootstrapMeansTest(arrayOfDouble1, arrayOfDouble2, h1, 5000000, null);
long l2 = System.currentTimeMillis();
System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
System.out.println("Bootstrap SP = " + twoSampleBootstrapMeansTest.getSP());
}
}
}

