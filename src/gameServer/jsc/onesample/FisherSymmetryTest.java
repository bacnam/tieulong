package jsc.onesample;

import java.util.Arrays;
import jsc.datastructures.PairedData;
import jsc.distributions.Tail;
import jsc.tests.H1;
import jsc.tests.PermutationTest;
import jsc.tests.SignificanceTest;
import jsc.util.Arrays;

public class FisherSymmetryTest
implements SignificanceTest
{
static final double PTOL = 0.001D;
private boolean negsumSmaller;
private double cv;
private double T;
private final double SP;
private H1 alternative;

public FisherSymmetryTest(double[] paramArrayOfdouble, H1 paramH1) {
double d;
this.alternative = paramH1;

if (Arrays.isIntegers(paramArrayOfdouble)) {
d = fisherProbInt(paramArrayOfdouble);
} else {
d = fisherProb(paramArrayOfdouble, 0.001D);
} 

if (paramH1 == H1.NOT_EQUAL) {
this.SP = d + d;
} else if (paramH1 == H1.LESS_THAN) {
this.SP = this.negsumSmaller ? (1.0D - d) : d;
} else {
this.SP = this.negsumSmaller ? d : (1.0D - d);
} 
}

public FisherSymmetryTest(double[] paramArrayOfdouble) {
this(paramArrayOfdouble, H1.NOT_EQUAL);
}

public FisherSymmetryTest(PairedData paramPairedData, H1 paramH1) {
this(paramPairedData.differences(), paramH1);
}

public FisherSymmetryTest(PairedData paramPairedData) {
this(paramPairedData, H1.NOT_EQUAL);
}

private double fisherProb(double[] paramArrayOfdouble, double paramDouble) {
double d3, d4, d11;
int i = paramArrayOfdouble.length;

byte b1 = 0, b2 = 0; int k = 0, m = 0;

double d1 = 0.0D, d2 = 0.0D;

long l = i;

int[] arrayOfInt = new int[i];

boolean[] arrayOfBoolean = new boolean[i];
byte b4;
double d6, d7;
for (b4 = 0, d6 = 0.0D, d7 = 0.0D; b4 < i; b4++) {
d7 += paramArrayOfdouble[b4] * paramArrayOfdouble[b4];
if (paramArrayOfdouble[b4] >= 0.0D) {

b1++;
d1 += paramArrayOfdouble[b4];
arrayOfBoolean[b4] = true;
}
else {

b2++;
d2 -= paramArrayOfdouble[b4];
paramArrayOfdouble[b4] = -paramArrayOfdouble[b4];
arrayOfBoolean[b4] = false;
} 
} 

d6 = (d1 + d2) / 2.0D;
d7 /= 4.0D;

if (d1 < d2) {
d11 = d1; byte b = b1; this.negsumSmaller = false;
} else {
d11 = d2; byte b = b2; this.negsumSmaller = true;
} 

double d5 = Math.sqrt((i - (d6 - d11) * (d6 - d11) / d7) / 48.0D);
double d8 = d6 * (d6 - d11) / paramDouble * d7;
double d9 = 2.0D / paramDouble;

int j = (int)(d5 * ((d8 > d9) ? d8 : d9));

byte b3 = (b1 < b2) ? b1 : b2;

if (b3 > 0) {
d3 = j / d11;
} else {
d3 = 1.0D;
} 
for (b4 = 0; b4 < i; b4++) {
arrayOfInt[b4] = (int)(paramArrayOfdouble[b4] * d3 + 0.5D);

if (arrayOfBoolean[b4]) { k += arrayOfInt[b4]; }
else
{ m += arrayOfInt[b4]; }

}  j = (k < m) ? k : m;

l = (j + 2);

double[] arrayOfDouble = new double[j + 2];

Arrays.sort(arrayOfInt);
for (b4 = 0; b4 < i; b4++) {
arrayOfInt[b4] = (arrayOfInt[b4] < j + 1) ? arrayOfInt[b4] : (j + 1);
}
arrayOfDouble[0] = arrayOfDouble[0] + 1.0D;
arrayOfDouble[arrayOfInt[0]] = arrayOfDouble[arrayOfInt[0]] + 1.0D;
int n = arrayOfInt[0];
for (byte b5 = 1; b5 < i; b5++) {

int i1 = n + arrayOfInt[b5];
if (i1 > j) {

arrayOfDouble[j + 1] = arrayOfDouble[j + 1] * 2.0D;
for (int i4 = j; i4 >= j - arrayOfInt[b5] + 1; ) { arrayOfDouble[j + 1] = arrayOfDouble[j + 1] + arrayOfDouble[i4]; i4--; }

}  int i3 = (j > i1) ? i1 : j;
for (int i2 = i3; i2 >= arrayOfInt[b5]; ) { arrayOfDouble[i2] = arrayOfDouble[i2] + arrayOfDouble[i2 - arrayOfInt[b5]]; i2--; }
n = i1;
} 

double d10 = 0.0D;

if ((this.alternative == H1.GREATER_THAN && !this.negsumSmaller) || (this.alternative == H1.LESS_THAN && this.negsumSmaller)) {

for (b4 = 0; b4 < j; ) { d10 += arrayOfDouble[b4]; b4++; }
d4 = d10 / (d10 + arrayOfDouble[j + 1] + arrayOfDouble[j]);
}
else {

for (b4 = 0; b4 <= j; ) { d10 += arrayOfDouble[b4]; b4++; }
d4 = d10 / (d10 + arrayOfDouble[j + 1]);
} 

this.cv = 100.0D * arrayOfDouble[j] * d5 / d10;

arrayOfDouble = null;
arrayOfInt = null;
arrayOfBoolean = null;

this.T = d11;
return d4;
}

private double fisherProbInt(double[] paramArrayOfdouble) {
int m;
double d1;
int i = paramArrayOfdouble.length;

int n = 0, i1 = 0;

byte b2;

for (b2 = 0; b2 < i; b2++) {
if (paramArrayOfdouble[b2] >= 0.0D) {

n = (int)(n + paramArrayOfdouble[b2]);
}
else {

i1 = (int)(i1 - paramArrayOfdouble[b2]);
paramArrayOfdouble[b2] = -paramArrayOfdouble[b2];
} 
} 

if (n < i1) {
m = n; this.negsumSmaller = false;
} else {
m = i1; this.negsumSmaller = true;
} 
int k = m;

long l = (k + 2);
double[] arrayOfDouble = new double[k + 2];

Arrays.sort(paramArrayOfdouble);
for (b2 = 0; b2 < i; b2++) {
paramArrayOfdouble[b2] = (paramArrayOfdouble[b2] < (k + 1)) ? paramArrayOfdouble[b2] : (k + 1);
}
for (b2 = 0; b2 <= k + 1; ) { arrayOfDouble[b2] = 0.0D; b2++; }
arrayOfDouble[0] = arrayOfDouble[0] + 1.0D;
arrayOfDouble[(int)paramArrayOfdouble[0]] = arrayOfDouble[(int)paramArrayOfdouble[0]] + 1.0D;
int j = (int)paramArrayOfdouble[0];
for (byte b1 = 1; b1 < i; b1++) {

int i2 = (int)(j + paramArrayOfdouble[b1]);
if (i2 > k) {

arrayOfDouble[k + 1] = 2.0D * arrayOfDouble[k + 1];
for (int i5 = k; i5 >= k - paramArrayOfdouble[b1] + 1.0D; ) { arrayOfDouble[k + 1] = arrayOfDouble[k + 1] + arrayOfDouble[i5]; i5--; }

}  int i4 = (k > i2) ? i2 : k;
for (int i3 = i4; i3 >= paramArrayOfdouble[b1]; ) { arrayOfDouble[i3] = arrayOfDouble[i3] + arrayOfDouble[i3 - (int)paramArrayOfdouble[b1]]; i3--; }
j = i2;
} 

double d2 = 0.0D;

if ((this.alternative == H1.GREATER_THAN && !this.negsumSmaller) || (this.alternative == H1.LESS_THAN && this.negsumSmaller)) {

for (b2 = 0; b2 < k; ) { d2 += arrayOfDouble[b2]; b2++; }
d1 = d2 / (d2 + arrayOfDouble[k + 1] + arrayOfDouble[k]);
}
else {

for (b2 = 0; b2 <= k; ) { d2 += arrayOfDouble[b2]; b2++; }
d1 = d2 / (d2 + arrayOfDouble[k + 1]);
} 

arrayOfDouble = null;
this.T = m;
return d1;
}

public double getCV() {
return this.cv;
} public double getSP() {
return this.SP;
}

public double getTestStatistic() {
return this.T;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { -8.0D, -7.0D, -6.0D, -5.0D, -5.0D, -5.0D, -5.0D, -4.0D, -4.0D, -4.0D, -2.0D, -2.0D, -2.0D, -2.0D, -2.0D, -2.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 2.0D, 2.0D, 3.0D, 4.0D, 5.0D, 5.0D, 5.0D };

FisherSymmetryTest fisherSymmetryTest = new FisherSymmetryTest(arrayOfDouble1, H1.LESS_THAN);
System.out.println("Fisher symm: SP = " + fisherSymmetryTest.getSP() + " T = " + fisherSymmetryTest.getTestStatistic() + " CV = " + fisherSymmetryTest.getCV());

double[] arrayOfDouble2 = { 70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D };
double[] arrayOfDouble3 = { 75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D };

PairedData pairedData = new PairedData(arrayOfDouble2, arrayOfDouble3);

H1[] arrayOfH1 = { H1.LESS_THAN, H1.NOT_EQUAL, H1.GREATER_THAN };
Tail[] arrayOfTail = { Tail.LOWER, Tail.TWO, Tail.UPPER };
for (byte b = 0; b < 3; b++) {

fisherSymmetryTest = new FisherSymmetryTest(pairedData, arrayOfH1[b]);
System.out.println("Fisher symm: SP = " + fisherSymmetryTest.getSP() + " T = " + fisherSymmetryTest.getTestStatistic() + " CV = " + fisherSymmetryTest.getCV());
PairedTtest pairedTtest = new PairedTtest(pairedData, arrayOfH1[b]);
PermutationTest permutationTest = new PermutationTest(pairedTtest, arrayOfTail[b], false, 0, 0.0D, null);
System.out.println("Permutation: SP = " + permutationTest.getSP() + " tObs = " + permutationTest.getTestStatistic());
System.out.println("   Paired t: SP = " + pairedTtest.getSP() + " t = " + pairedTtest.getTestStatistic());
} 
}
}
}

