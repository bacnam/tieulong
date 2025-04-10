package jsc.ci;

import java.util.Arrays;
import java.util.Vector;
import jsc.util.Arrays;
import jsc.util.Sort;

public class DistributionFreeCI
extends AbstractConfidenceInterval
{
public static final int ONE_SAMPLE = 0;
public static final int PAIRED_SAMPLE = 1;
public static final int TWO_SAMPLE_SHIFT = 2;
public static final int TWO_SAMPLE_RATIO = 3;
static final int LOOP_MAX = 1000000;
protected int d;
protected double achievedConfidence;

public DistributionFreeCI(double paramDouble) {
super(paramDouble);
}

protected void computeInterval(int paramInt1, int paramInt2, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
int j, m;
if (paramInt1 < 0 || paramInt1 > 3) {
throw new IllegalArgumentException("Invalid problem type.");
}

if (paramInt1 == 0 && Arrays.isConstant(paramArrayOfdouble1)) {

this.lowerLimit = paramArrayOfdouble1[0];
this.upperLimit = this.lowerLimit;
return;
} 
if ((paramInt1 == 2 || paramInt1 == 3) && Arrays.isConstant(paramArrayOfdouble1) && Arrays.isConstant(paramArrayOfdouble2))
{

throw new IllegalArgumentException("Constant data.");
}

int i = paramArrayOfdouble1.length;

if (paramInt1 == 0) {
j = i;
} else {
j = paramArrayOfdouble2.length;
} 
if (paramInt1 == 1 && j != i) {
throw new IllegalArgumentException("Arrays not equal length.");
}

double d = 0.0D;

int n = i * j;
int i1 = Math.max(i, j);

int[] arrayOfInt = new int[i1 + 1];
double[] arrayOfDouble1 = new double[i1 + 1];
double[] arrayOfDouble2 = new double[i1 + 1];

int k = i; byte b;
for (b = 1; b <= k; ) { arrayOfDouble1[b] = paramArrayOfdouble1[b - 1]; b++; }

if (paramInt1 == 0 || paramInt1 == 1) {

if (paramInt1 == 1) {

m = j;
for (b = 1; b <= m; ) { arrayOfDouble2[b] = paramArrayOfdouble2[b - 1]; b++; }
for (b = 1; b <= k; ) { arrayOfDouble1[b] = arrayOfDouble1[b] - arrayOfDouble2[b]; b++; }

} else {
m = k;
} 

Sort.sort(arrayOfDouble1, null, 1, k, true);
if (arrayOfDouble1[1] <= 0.0D) {

d = Math.abs(arrayOfDouble1[1]);
for (b = 1; b <= k; ) { arrayOfDouble1[b] = arrayOfDouble1[b] + d; b++; }

} 
for (b = 1; b <= k; ) { arrayOfDouble2[b] = -arrayOfDouble1[b]; b++; }

} else {

m = j;
for (b = 1; b <= m; ) { arrayOfDouble2[b] = paramArrayOfdouble2[b - 1]; b++; }

Sort.sort(arrayOfDouble1, null, 1, k, true);
Sort.sort(arrayOfDouble2, null, 1, m, false);
if (paramInt1 == 3) {

if (arrayOfDouble1[1] <= 0.0D || arrayOfDouble2[m] <= 0.0D)
throw new IllegalArgumentException("Data not greater than zero."); 
for (b = 1; b <= k; ) { arrayOfDouble1[b] = Math.log(arrayOfDouble1[b]); b++; }
for (b = 1; b <= m; ) { arrayOfDouble2[b] = Math.log(arrayOfDouble2[b]); b++; }

} 
if (arrayOfDouble1[1] <= 0.0D || arrayOfDouble2[m] <= 0.0D) {

if (arrayOfDouble1[1] < 0.0D && arrayOfDouble2[m] > 0.0D)
d = Math.abs(arrayOfDouble1[1]); 
if (arrayOfDouble1[1] > 0.0D && arrayOfDouble2[m] < 0.0D)
d = Math.abs(arrayOfDouble2[m]); 
if (arrayOfDouble1[1] < arrayOfDouble2[m]) {
d = Math.abs(arrayOfDouble1[1]);
}

for (b = 1; b <= k; ) { arrayOfDouble1[b] = arrayOfDouble1[b] + d; b++; }
for (b = 1; b <= m; ) { arrayOfDouble2[b] = arrayOfDouble2[b] + d; b++; }

} 
} 

Double[] arrayOfDouble = diff(paramInt1, paramInt2, k, arrayOfDouble1, m, arrayOfDouble2, n, arrayOfInt);

this.lowerLimit = arrayOfDouble[paramInt2].doubleValue();

if (paramInt1 == 0 || paramInt1 == 1) {
this.lowerLimit = this.lowerLimit / 2.0D - d;
}
if (paramInt1 == 3) {
this.lowerLimit = Math.exp(this.lowerLimit);
}

Sort.sort(arrayOfDouble1, null, 1, k, false);

Sort.sort(arrayOfDouble2, null, 1, m, true);
arrayOfDouble = diff(paramInt1, paramInt2, m, arrayOfDouble2, k, arrayOfDouble1, n, arrayOfInt);

this.upperLimit = -arrayOfDouble[paramInt2].doubleValue();

if (paramInt1 == 0 || paramInt1 == 1) {

this.upperLimit = this.upperLimit / 2.0D - d;

return;
} 
if (paramInt1 == 3) {
this.upperLimit = Math.exp(this.upperLimit);
}
}

private Double[] diff(int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfdouble1, int paramInt4, double[] paramArrayOfdouble2, int paramInt5, int[] paramArrayOfint) {
Vector vector = new Vector(2 + 2 * paramInt2, 5);

vector.add(new Double(0.0D));
byte b1 = 0;
double d2 = 0.0D;

if (paramInt1 == 0 || paramInt1 == 1)
{ for (byte b = 1; b <= paramInt3; ) { paramArrayOfint[b] = b; b++; }
}
else { for (byte b = 1; b <= paramInt4; ) { paramArrayOfint[b] = 1; b++; }
}

double d1 = paramInt2 * Math.abs(paramArrayOfdouble1[paramInt3] - paramArrayOfdouble1[1] + paramArrayOfdouble2[1] - paramArrayOfdouble2[paramInt4]) / paramInt5;

byte b2 = 0;
label75: while (true) {
d2 += d1;
double d = paramArrayOfdouble1[1] + d2;
int k = paramArrayOfint[1];
boolean bool1 = false; int i;
for (i = k; i <= paramInt3; i++) {

if (paramArrayOfdouble1[i] > d) { bool1 = true; break; }
b1++;

vector.add(new Double(paramArrayOfdouble1[i] - paramArrayOfdouble2[1]));
} 
paramArrayOfint[1] = bool1 ? i : (paramInt3 + 1);
int j = 2;

boolean bool2 = true;

while (true) {
if (++b2 > 1000000)
throw new IllegalArgumentException("Cannot calculate confidence interval."); 
if (!bool2) j = i; 
bool2 = false;

for (int m = j; m <= paramInt4; m++) {

double d3 = d2 - paramArrayOfdouble2[1] + paramArrayOfdouble2[m];
if (d3 <= 0.0D)
break;  d = paramArrayOfdouble1[1] + d3;
k = paramArrayOfint[m];
bool1 = false;
for (i = k; i <= paramInt3; i++) {

if (paramArrayOfdouble1[i] > d) {

bool1 = true; break;
}  b1++;

vector.add(new Double(paramArrayOfdouble1[i] - paramArrayOfdouble2[m]));
} 
paramArrayOfint[m] = bool1 ? i : (paramInt3 + 1);
} 

if (b1 > paramInt2 + 1) {

Double[] arrayOfDouble = vector.<Double>toArray(new Double[1]);
Sort.sort(arrayOfDouble, null, 1, b1, true);
return arrayOfDouble;
} 

if (paramArrayOfint[1] <= paramInt3)
continue label75; 
d2 += d1;
boolean bool = false;
for (i = j; i <= paramInt4; i++) {
if (paramArrayOfint[i] <= paramInt3) { bool = true; break; } 
}  if (!bool) {

Double[] arrayOfDouble = vector.<Double>toArray(new Double[1]);
Sort.sort(arrayOfDouble, null, 1, b1, true);
return arrayOfDouble;
} 

if (!bool) {
continue label75;
}
} 
break;
} 
}

public double getAchievedConfidence() {
return this.achievedConfidence;
}

public int getD() {
return this.d;
}

public static double[] differences(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
int i = paramArrayOfdouble1.length;
int j = paramArrayOfdouble2.length;
double[] arrayOfDouble = new double[i * j];
byte b1 = 0;
for (byte b2 = 0; b2 < i; b2++) {
for (byte b = 0; b < j; b++)
arrayOfDouble[b1++] = paramArrayOfdouble1[b2] - paramArrayOfdouble2[b]; 
}  Arrays.sort(arrayOfDouble);
return arrayOfDouble;
}

public static double[] walshAverages(double[] paramArrayOfdouble) {
int i = paramArrayOfdouble.length;
double[] arrayOfDouble = new double[i * (i + 1) / 2];
byte b1 = 0;
for (byte b2 = 0; b2 < i; b2++) {
for (byte b = b2; b < i; b++)
arrayOfDouble[b1++] = (paramArrayOfdouble[b2] + paramArrayOfdouble[b]) / 2.0D; 
}  Arrays.sort(arrayOfDouble);
return arrayOfDouble;
}

public String toString() {
return new String(super.toString() + ". Achieved confidence = " + this.achievedConfidence);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { -1.0D, -2.3D, 0.8D, -0.1D, -2.0D, -0.9D, 0.3D, -2.4D, 0.5D, -2.5D, 1.3D, -2.1D };
double[] arrayOfDouble2 = DistributionFreeCI.walshAverages(arrayOfDouble1);
System.out.print("\nWalsh averages: "); byte b;
for (b = 0; b < arrayOfDouble2.length; ) { System.out.print(arrayOfDouble2[b] + ","); b++; }

double[] arrayOfDouble3 = { 122.0D, 127.0D, 110.0D, 115.0D, 132.0D, 131.0D, 105.0D, 124.0D, 112.0D, 123.0D };
double[] arrayOfDouble4 = { 104.0D, 103.0D, 121.0D, 114.0D, 95.0D, 102.0D, 119.0D, 108.0D, 130.0D, 109.0D, 99.0D, 113.0D };
double[] arrayOfDouble5 = DistributionFreeCI.differences(arrayOfDouble3, arrayOfDouble4);
System.out.print("\nDifferences: ");
for (b = 0; b < arrayOfDouble5.length; ) { System.out.print(arrayOfDouble5[b] + ","); b++; }

}
}
}

