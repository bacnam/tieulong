package jsc.independentsamples;

import jsc.combinatorics.Enumerator;
import jsc.combinatorics.MultiSetPermutations;
import jsc.distributions.ChiSquared;
import jsc.distributions.Normal;
import jsc.distributions.Tail;
import jsc.goodnessfit.SampleDistributionFunction;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;
import jsc.util.Arrays;
import jsc.util.Maths;

public class SmirnovTest
extends PermutableTwoSampleStatistic
implements SignificanceTest
{
private static final double EPS = 1.0E-6D;
private static final double SMALL = 4.9E-324D;
private static final double SMALL_N = Math.log(Double.MIN_VALUE);
private static final double ALN2 = Math.log(2.0D);

private static final double CHKNUM = 1.0E64D;

private static final int ITERUP = 1000;

private static final int SMALL_SAMPLE_SIZE = 5000;

private final H1 alternative;

final int nA;

final int nB;

private double D;

private long Dstar;

private final double SP;

private SampleDistributionFunction sdfA;

private SampleDistributionFunction sdfB;

public SmirnovTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, boolean paramBoolean) {
super(paramArrayOfdouble1, paramArrayOfdouble2);
this.alternative = paramH1;

this.sdfA = new SampleDistributionFunction(paramArrayOfdouble1);
this.sdfB = new SampleDistributionFunction(paramArrayOfdouble2);
this.nA = this.sdfA.getN();
this.nB = this.sdfB.getN();
this.permutedSampleA = new double[this.nA];
this.permutedSampleB = new double[this.nB];
this.originalSample = Arrays.append(paramArrayOfdouble2, paramArrayOfdouble1);
this.N = this.nA + this.nB;
this.Dstar = calculateTestStatistic(this.sdfA, this.sdfB);
this.D = this.Dstar / (this.nA * this.nB);

if (this.D == 0.0D) {
this.SP = 1.0D;

}
else if (paramBoolean) {
this.SP = approxSP(this.nA, this.nB, this.D, paramH1);
} else {
this.SP = exactSP(this.nA, this.nB, this.D, paramH1);
} 
}

public SmirnovTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1) {
this(paramArrayOfdouble1, paramArrayOfdouble2, paramH1, (paramArrayOfdouble1.length > 5000 && paramArrayOfdouble2.length > 5000));
}

public SmirnovTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, (paramArrayOfdouble1.length > 5000 && paramArrayOfdouble2.length > 5000));
}

public static double approxSP(int paramInt1, int paramInt2, double paramDouble, H1 paramH1) {
if (paramInt1 < 1 || paramInt2 < 1)
throw new IllegalArgumentException("Invalid sample sizes."); 
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid D value."); 
if (paramH1 == H1.NOT_EQUAL)
{

return probks(paramInt1, paramInt2, paramDouble);
}

double d1 = (paramInt1 + paramInt2);
double d2 = (paramInt1 * paramInt2);

return ChiSquared.upperTailProb(4.0D * paramDouble * paramDouble * d2 / d1, 2.0D);
}

private long calculateTestStatistic(SampleDistributionFunction paramSampleDistributionFunction1, SampleDistributionFunction paramSampleDistributionFunction2) {
byte b1 = 0, b2 = 0;
double d1 = 0.0D, d2 = 0.0D;
double d3 = 0.0D;

int i = paramSampleDistributionFunction1.getN();
int j = paramSampleDistributionFunction2.getN();

while (b1 < i && b2 < j) {

double d6, d4 = paramSampleDistributionFunction1.getOrderedX(b1);
double d5 = paramSampleDistributionFunction2.getOrderedX(b2);

if (d4 <= d5) d1 = paramSampleDistributionFunction1.getOrderedS(b1++); 
if (d5 <= d4) d2 = paramSampleDistributionFunction2.getOrderedS(b2++);

if (this.alternative == H1.GREATER_THAN) {

d6 = d2 - d1;
} else if (this.alternative == H1.LESS_THAN) {

d6 = d1 - d2;
} else {

d6 = Math.abs(d2 - d1);
} 

if (d6 > d3) d3 = d6;

} 
return Math.round((i * j) * d3);
}

public static double exactSP(int paramInt1, int paramInt2, double paramDouble, H1 paramH1) {
if (paramInt1 < 1 || paramInt2 < 1)
throw new IllegalArgumentException("Invalid sample sizes."); 
if (paramDouble < 0.0D || paramDouble > 1.0D) {
throw new IllegalArgumentException("Invalid D value.");
}

return gsmirn(paramInt1, paramInt2, paramDouble, paramH1, Arrays.fill(paramInt1 + paramInt2 + 1, 1));
}

public Enumerator getEnumerator() {
int[] arrayOfInt = new int[2];
arrayOfInt[0] = this.nA; arrayOfInt[1] = this.nB;
return (Enumerator)new MultiSetPermutations(arrayOfInt);
}

public SampleDistributionFunction getSdfA() {
return this.sdfA;
}

public SampleDistributionFunction getSdfB() {
return this.sdfB;
}

public double getSP() {
return this.SP;
}

public double getStatistic() {
return this.Dstar;
}

public double getTestStatistic() {
return this.D;
}

private static double gsmirn(int paramInt1, int paramInt2, double paramDouble, H1 paramH1, int[] paramArrayOfint) {
int m = 0, i1 = 0, i2 = 0, i3 = 0;

int i4 = paramInt1 + paramInt2;

double[] arrayOfDouble = new double[paramInt1 + 3];
int j = 0;
byte b = 0;
do {
b++;
if (paramArrayOfint[b] <= 0)
throw new IllegalArgumentException("Invalid number of observations."); 
j += paramArrayOfint[b];
if (j > i4)
throw new IllegalArgumentException("Invalid number of observations."); 
} while (j < i4);

double d1 = 1.0D;
double d3 = paramDouble - 1.0E-6D;
if (d3 <= 0.0D) return d1; 
arrayOfDouble[1] = 1.0D;

double d2 = paramInt1 / i4;
double d4 = d2 * d3 * paramInt2;

boolean bool = true;
j = 1;
int i6 = paramArrayOfint[1];
int k = 0;
int n = 0;

int i = 1000;
int i5 = 0;
double d6 = 1.0D;

for (b = 1; b <= i4 - 1; b++) {

if (i6 == 1) {

double d = b * d2;

n = Math.min((int)(d + d4), Math.min(b, paramInt1));
k = Math.max((int)(d - d4 + 1.0D), Math.max(b - paramInt2, 0));
j++;
i6 = paramArrayOfint[j];
bool = true;
}
else {

i6--;

if (bool) {

bool = false;
int i12 = b + i6;

double d = i12 * d2;

int i11 = Math.min((int)(d + d4), Math.min(i12, paramInt1));
int i10 = Math.max((int)(d - d4 + 1.0D), Math.max(i12 - paramInt2, 0));

m = k;
i1 = i11;
i3 = i12 - i10;
i2 = b - n - 1;
} 

k = Math.max(m, b - i3);
n = Math.min(i1, b - i2);
} 

if (paramH1 == H1.GREATER_THAN) { n = Math.min(paramInt1, b); }
else if (paramH1 == H1.LESS_THAN) { k = Math.max(0, b - paramInt2); }

int i8 = Math.max(1, k);
int i9 = Math.min(b - 1, n);
int i7;
for (i7 = i9; i7 >= i8; i7--) {
arrayOfDouble[i7 + 1] = arrayOfDouble[i7 + 1] + arrayOfDouble[i7];
}

i--;
if (i <= 0) {

double d = 0.0D;
for (i7 = i8 + 1; i7 <= i9 + 1; i7++)
d = Math.max(arrayOfDouble[i7], d); 
if (d == 0.0D) return d1; 
if (d > 1.0E64D) {

for (i7 = i8 + 1; i7 <= i9 + 1; i7++)
arrayOfDouble[i7] = arrayOfDouble[i7] * Double.MIN_VALUE; 
i = 1000;
i5++;
d6 *= Double.MIN_VALUE;
}
else {

i = (int)((-SMALL_N - Math.log(d)) / ALN2);
} 
} 

if (k == 0) {
arrayOfDouble[i8] = d6;
} else {
arrayOfDouble[i8] = 0.0D;
} 
if (n == b) {
arrayOfDouble[i9 + 2] = d6;
} else {
arrayOfDouble[i9 + 2] = 0.0D;
} 
} 
double d5 = arrayOfDouble[paramInt1 + 1] + arrayOfDouble[paramInt1];
if (d5 == 0.0D) return d1;

d1 = 1.0D - Math.exp(Maths.logFactorial(paramInt1) + Maths.logFactorial(paramInt2) + Math.log(d5) - i5 * SMALL_N - Maths.logFactorial(i4));

if (d1 < 0.0D)
throw new IllegalArgumentException("Invalid SP " + d1); 
return d1;
}

private static double probks(int paramInt1, int paramInt2, double paramDouble) {
double d2 = 0.0D;
double d3 = 2.0D;

double d4 = 0.0D;
double d5 = paramDouble * Math.sqrt((paramInt1 * paramInt2 / (paramInt1 + paramInt2)));

double d1 = -2.0D * d5 * d5;
for (byte b = 1; b <= 'Ϩ'; b++) {

double d = d3 * Math.exp(d1 * b * b);
d2 += d;
if (Math.abs(d) <= 0.001D * d4 || Math.abs(d) < 1.0E-8D * d2) {

if (d2 > 1.0D) { d2 = 1.0D; }
else if (d2 < 0.0D) { d2 = 0.0D; }
return d2;
} 
d3 = -d3;
d4 = Math.abs(d);
} 

throw new RuntimeException("Cannot calculate approximate SP");
}

public double resampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
SampleDistributionFunction sampleDistributionFunction1 = new SampleDistributionFunction(paramArrayOfdouble1);
SampleDistributionFunction sampleDistributionFunction2 = new SampleDistributionFunction(paramArrayOfdouble2);
return calculateTestStatistic(sampleDistributionFunction1, sampleDistributionFunction2);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d2 = 0.0D;
H1 h1 = H1.NOT_EQUAL;

byte b1 = 11;
byte b2 = 11;
double[] arrayOfDouble1 = new double[b1];
double[] arrayOfDouble2 = new double[b2];
Normal normal1 = new Normal(0.0D, 1.0D);
normal1.setSeed(100L);
Normal normal2 = new Normal(1.0D, 1.0D);
normal2.setSeed(200L);
byte b3;
for (b3 = 0; b3 < b1; ) { arrayOfDouble1[b3] = normal1.random(); b3++; }
for (b3 = 0; b3 < b2; ) { arrayOfDouble2[b3] = normal2.random(); b3++; }
Tail tail = Tail.TWO;

b1 = 25; b2 = 25; d2 = 300.0D;
double d1 = d2 / (b1 * b2);

b1 = 100; b2 = 50; d1 = 0.36D;
System.out.println("D* = " + d2 + " D = " + d1);
long l1 = System.currentTimeMillis();
System.out.println("Approx SP = " + SmirnovTest.approxSP(b1, b2, d1, h1));

long l2 = System.currentTimeMillis();
System.out.println("Time = " + (l2 - l1) + " millisecs");
l1 = System.currentTimeMillis();
System.out.println(" Exact SP = " + SmirnovTest.exactSP(b1, b2, d1, h1));

l2 = System.currentTimeMillis();
System.out.println("Time = " + (l2 - l1) + " millisecs");
}
}
}

