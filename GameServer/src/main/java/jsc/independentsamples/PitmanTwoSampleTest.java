package jsc.independentsamples;

import java.util.Arrays;
import jsc.distributions.Tail;
import jsc.tests.H1;
import jsc.tests.PermutationTest;
import jsc.tests.SignificanceTest;
import jsc.util.Arrays;

public class PitmanTwoSampleTest
implements SignificanceTest
{
static final double PTOL = 0.001D;
private double cv;
private double T;
private double SP;
private H1 alternative;

public PitmanTwoSampleTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1) {
double d;
int i = paramArrayOfdouble1.length;
int j = paramArrayOfdouble2.length;
int k = i + j;
this.alternative = paramH1;

int[] arrayOfInt = new int[k];

if (Arrays.isIntegers(paramArrayOfdouble1) && Arrays.isIntegers(paramArrayOfdouble2)) {

int[] arrayOfInt1 = new int[k]; byte b;
for (b = 0; b < i; ) { arrayOfInt1[b] = (int)paramArrayOfdouble1[b]; arrayOfInt[b] = 1; b++; }
for (b = 0; b < j; ) { arrayOfInt1[b + i] = (int)paramArrayOfdouble2[b]; arrayOfInt[b + i] = 2; b++; }
d = pitmanProbInt(arrayOfInt1, arrayOfInt, k);
}
else {

double[] arrayOfDouble = new double[k]; byte b;
for (b = 0; b < i; ) { arrayOfDouble[b] = paramArrayOfdouble1[b]; arrayOfInt[b] = 1; b++; }
for (b = 0; b < j; ) { arrayOfDouble[b + i] = paramArrayOfdouble2[b]; arrayOfInt[b + i] = 2; b++; }
d = pitmanProb(arrayOfDouble, arrayOfInt, k, 0.001D);
} 

this.SP = d;
}

public PitmanTwoSampleTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL);
}

private double pitmanProb(double[] paramArrayOfdouble, int[] paramArrayOfint, int paramInt, double paramDouble) {
double d17;
byte b1 = 0, b2 = 0; int j = 0;
double d1 = 0.0D, d2 = 0.0D, d8 = 0.0D, d9 = 0.0D;

int m = paramInt;

int[] arrayOfInt1 = new int[paramInt];

int[] arrayOfInt2 = new int[paramInt];

byte b3;

for (b3 = 0; b3 < paramInt; b3++) {
if (b3 == 0) {
d8 = paramArrayOfdouble[b3];
d9 = paramArrayOfdouble[b3];
}
else {

d8 = (paramArrayOfdouble[b3] > d8) ? d8 : paramArrayOfdouble[b3];
d9 = (paramArrayOfdouble[b3] > d9) ? paramArrayOfdouble[b3] : d9;
} 
if (paramArrayOfint[b3] == 1) {

b1++;
d1 += paramArrayOfdouble[b3];
}
else {

b2++;
d2 += paramArrayOfdouble[b3];
} 
} 

double d10 = d1 / b1;
double d11 = d2 / b2;
boolean bool2 = (d10 > d11) ? true : true;
for (b3 = 0; b3 < paramInt; ) { paramArrayOfdouble[b3] = paramArrayOfdouble[b3] - d8; b3++; }
boolean bool1 = (b1 < b2) ? true : true;
d1 -= b1 * d8;
d2 -= b2 * d8;
d9 -= d8;

if (bool2 != bool1) {

for (b3 = 0; b3 < paramInt; ) { paramArrayOfdouble[b3] = d9 - paramArrayOfdouble[b3]; b3++; }
d1 = b1 * d9 - d1;
d2 = b2 * d9 - d2;
} 
double d4 = (bool1 == true) ? d1 : d2;
this.T = d4;
byte b5 = (b1 < b2) ? b1 : b2;

double d14 = b5;
double d15 = paramInt; double d12, d13;
for (d12 = 0.0D, d13 = 0.0D, b3 = 0; b3 < paramInt; b3++) {
d12 += paramArrayOfdouble[b3];
d13 += paramArrayOfdouble[b3] * paramArrayOfdouble[b3];
for (int n = b3 + 1; n < paramInt; ) { d13 -= 2.0D * paramArrayOfdouble[b3] * paramArrayOfdouble[n] / (d15 - 1.0D); n++; }

}  d12 = d14 / d15 * d12;
d13 = d14 / d15 * (1.0D - d14 / d15) * d13;
double d16 = Math.sqrt(d14 / d15 * (1.0D - d14 / d15) * (d15 + (d12 - d4) * (d12 - d4) * (d15 + 1.0D) / d15 * d13) / 12.0D);
double d6 = d12 * (d12 - d4) / paramDouble * d13;
double d7 = 2.0D / paramDouble;
int i = (int)(d16 * ((d6 > d7) ? d6 : d7));

double d3 = i / d4;
for (b3 = 0; b3 < paramInt; b3++) {
arrayOfInt2[b3] = (int)(paramArrayOfdouble[b3] * d3 + 0.5D);
if (paramArrayOfint[b3] == bool1) j += arrayOfInt2[b3]; 
} 
i = j;

m = (i + 2) * b5;

double[] arrayOfDouble = new double[m];

Arrays.sort(arrayOfInt2);
for (b3 = 0; b3 < paramInt; b3++) {
arrayOfInt2[b3] = (arrayOfInt2[b3] < i + 1) ? arrayOfInt2[b3] : (i + 1);
}

for (b3 = 0; b3 < b5; ) { arrayOfInt1[b3] = b3 * (i + 2); b3++; }
arrayOfDouble[arrayOfInt1[0] + arrayOfInt2[0]] = arrayOfDouble[arrayOfInt1[0] + arrayOfInt2[0]] + 1.0D;

int k = arrayOfInt2[0];
for (byte b4 = 1; b4 < paramInt; b4++) {

byte b7 = (b5 + b4 - paramInt > 1) ? (b5 + b4 - paramInt) : 1;
int n = k + arrayOfInt2[b4];
for (byte b6 = (b4 > b5 - 1) ? (b5 - 1) : b4; b6 >= b7; b6--) {

if (n > i) for (int i3 = i - arrayOfInt2[b4] + 1; i3 <= i + 1; i3++)
{
arrayOfDouble[arrayOfInt1[b6] + i + 1] = arrayOfDouble[arrayOfInt1[b6] + i + 1] + arrayOfDouble[arrayOfInt1[b6 - 1] + i3];
} 
int i1 = (i > n) ? n : i;
for (int i2 = arrayOfInt2[b4]; i2 <= i1; i2++)
{
arrayOfDouble[arrayOfInt1[b6] + i2] = arrayOfDouble[arrayOfInt1[b6] + i2] + arrayOfDouble[arrayOfInt1[b6 - 1] + i2 - arrayOfInt2[b4]];
}
} 

arrayOfDouble[arrayOfInt1[0] + arrayOfInt2[b4]] = arrayOfDouble[arrayOfInt1[0] + arrayOfInt2[b4]] + 1.0D;

k = n;
} 

double d5 = 0.0D;

arrayOfInt2 = null;

if ((this.alternative == H1.LESS_THAN && bool2 == true) || (this.alternative == H1.GREATER_THAN && bool2 == 2)) {

for (b3 = 0; b3 <= i; ) { d5 += arrayOfDouble[arrayOfInt1[b5 - 1] + b3]; b3++; }
d17 = d5 / (d5 + arrayOfDouble[arrayOfInt1[b5 - 1] + i + 1]);
}
else if (this.alternative == H1.NOT_EQUAL) {

for (b3 = 0; b3 <= i; ) { d5 += arrayOfDouble[arrayOfInt1[b5 - 1] + b3]; b3++; }
d17 = 2.0D * d5 / (d5 + arrayOfDouble[arrayOfInt1[b5 - 1] + i + 1]);
}
else {

for (b3 = 0; b3 < i; ) { d5 += arrayOfDouble[arrayOfInt1[b5 - 1] + b3]; b3++; }
d17 = 1.0D - d5 / (d5 + arrayOfDouble[arrayOfInt1[b5 - 1] + i + 1] + arrayOfDouble[arrayOfInt1[b5 - 1] + i]);
} 
this.cv = 100.0D * arrayOfDouble[arrayOfInt1[b5 - 1] + i] * d16 / d5;
return d17;
}

private double pitmanProbInt(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt) {
double d4;
int i = 0, j = 0;
byte b1 = 0, b2 = 0;

int m = 0, n = 0;

int i3 = paramInt;

int[] arrayOfInt = new int[paramInt];

byte b3;
for (b3 = 0; b3 < paramInt; b3++) {
if (b3 == 0) {
i = paramArrayOfint1[b3];
j = paramArrayOfint1[b3];
}
else {

i = (paramArrayOfint1[b3] > i) ? i : paramArrayOfint1[b3];
j = (paramArrayOfint1[b3] > j) ? paramArrayOfint1[b3] : j;
} 
if (paramArrayOfint2[b3] == 1) {

b1++;
m += paramArrayOfint1[b3];
}
else {

b2++;
n += paramArrayOfint1[b3];
} 
} 

double d1 = (m / b1);
double d2 = (n / b2);
boolean bool1 = (d1 > d2) ? true : true;

for (b3 = 0; b3 < paramInt; ) { paramArrayOfint1[b3] = paramArrayOfint1[b3] - i; b3++; }
boolean bool2 = (b1 < b2) ? true : true;

m -= b1 * i;
n -= b2 * i;
j -= i;

if (bool1 != bool2) {

for (b3 = 0; b3 < paramInt; ) { paramArrayOfint1[b3] = j - paramArrayOfint1[b3]; b3++; }
m = b1 * j - m;
n = b2 * j - n;
} 

int i2 = (bool2 == true) ? m : n;

this.T = i2;
byte b5 = (b1 < b2) ? b1 : b2;

int i1 = i2;

Arrays.sort(paramArrayOfint1);
for (b3 = 0; b3 < paramInt; b3++) {
paramArrayOfint1[b3] = (paramArrayOfint1[b3] < i1 + 1) ? paramArrayOfint1[b3] : (i1 + 1);
}

i3 = (i1 + 2) * b5;

double[] arrayOfDouble = new double[i3];

for (b3 = 0; b3 < b5; ) { arrayOfInt[b3] = b3 * (i1 + 2); b3++; }
arrayOfDouble[arrayOfInt[0] + paramArrayOfint1[0]] = arrayOfDouble[arrayOfInt[0] + paramArrayOfint1[0]] + 1.0D;

int k = paramArrayOfint1[0];
for (byte b4 = 1; b4 < paramInt; b4++) {

byte b7 = (b5 + b4 - paramInt > 1) ? (b5 + b4 - paramInt) : 1;
int i4 = k + paramArrayOfint1[b4];
for (byte b6 = (b4 > b5 - 1) ? (b5 - 1) : b4; b6 >= b7; b6--) {

if (i4 > i1) for (int i7 = i1 - paramArrayOfint1[b4] + 1; i7 <= i1 + 1; i7++)
{
arrayOfDouble[arrayOfInt[b6] + i1 + 1] = arrayOfDouble[arrayOfInt[b6] + i1 + 1] + arrayOfDouble[arrayOfInt[b6 - 1] + i7];
} 
int i5 = (i1 > i4) ? i4 : i1;
for (int i6 = paramArrayOfint1[b4]; i6 <= i5; i6++)
{
arrayOfDouble[arrayOfInt[b6] + i6] = arrayOfDouble[arrayOfInt[b6] + i6] + arrayOfDouble[arrayOfInt[b6 - 1] + i6 - paramArrayOfint1[b4]];
}
} 

arrayOfDouble[arrayOfInt[0] + paramArrayOfint1[b4]] = arrayOfDouble[arrayOfInt[0] + paramArrayOfint1[b4]] + 1.0D;

k = i4;
} 

double d3 = 0.0D;

d3 = 0.0D;

if ((this.alternative == H1.LESS_THAN && bool1 == true) || (this.alternative == H1.GREATER_THAN && bool1 == 2)) {

for (b3 = 0; b3 <= i1; ) { d3 += arrayOfDouble[arrayOfInt[b5 - 1] + b3]; b3++; }
d4 = d3 / (d3 + arrayOfDouble[arrayOfInt[b5 - 1] + i1 + 1]);
}
else if (this.alternative == H1.NOT_EQUAL) {

for (b3 = 0; b3 <= i1; ) { d3 += arrayOfDouble[arrayOfInt[b5 - 1] + b3]; b3++; }
d4 = 2.0D * d3 / (d3 + arrayOfDouble[arrayOfInt[b5 - 1] + i1 + 1]);
}
else {

for (b3 = 0; b3 < i1; ) { d3 += arrayOfDouble[arrayOfInt[b5 - 1] + b3]; b3++; }
d4 = 1.0D - d3 / (d3 + arrayOfDouble[arrayOfInt[b5 - 1] + i1 + 1] + arrayOfDouble[arrayOfInt[b5 - 1] + i1]);
} 

return d4;
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
double[] arrayOfDouble1 = { 90.0D, 72.0D, 61.0D, 66.0D, 81.0D, 69.0D, 59.0D, 70.0D };
double[] arrayOfDouble2 = { 62.0D, 85.0D, 78.0D, 66.0D, 80.0D, 91.0D, 69.0D, 77.0D, 84.0D };

H1[] arrayOfH1 = { H1.LESS_THAN, H1.NOT_EQUAL, H1.GREATER_THAN };
Tail[] arrayOfTail = { Tail.LOWER, Tail.TWO, Tail.UPPER };
for (byte b = 0; b < 3; b++) {

PitmanTwoSampleTest pitmanTwoSampleTest = new PitmanTwoSampleTest(arrayOfDouble2, arrayOfDouble1, arrayOfH1[b]);
System.out.println("     Pitman: SP = " + pitmanTwoSampleTest.getSP() + " T = " + pitmanTwoSampleTest.getTestStatistic() + " CV = " + pitmanTwoSampleTest.getCV());
TwoSampleTtest twoSampleTtest = new TwoSampleTtest(arrayOfDouble2, arrayOfDouble1, arrayOfH1[b], true);
PermutationTest permutationTest = new PermutationTest(twoSampleTtest, arrayOfTail[b], false, 0, 0.0D, null);
System.out.println("Permutation: SP = " + permutationTest.getSP() + " tObs = " + permutationTest.getTestStatistic());
System.out.println("     t-test: SP = " + twoSampleTtest.getSP() + " t = " + twoSampleTtest.getTestStatistic());
} 
}
}
}

