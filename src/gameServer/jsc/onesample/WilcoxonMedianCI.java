package jsc.onesample;

import java.util.Arrays;
import java.util.Random;
import jsc.ci.DistributionFreeCI;
import jsc.datastructures.PairedData;
import jsc.distributions.Normal;
import jsc.distributions.WilcoxonT;
import jsc.util.Maths;

public class WilcoxonMedianCI
extends DistributionFreeCI
{
protected static final int SMALL_SAMPLE_SIZE = 24;
double[] x;

public WilcoxonMedianCI(double[] paramArrayOfdouble, double paramDouble, boolean paramBoolean) {
super(paramDouble);
double d;
int i = paramArrayOfdouble.length;

if (paramBoolean) {

double d1 = Normal.inverseStandardCdf(0.5D + 0.5D * paramDouble);

this.d = (int)Math.round(0.5D * (0.5D * i * (i + 1.0D) + 1.0D - d1 * Math.sqrt(i * (i + 1.0D) * (2.0D * i + 1.0D) / 6.0D)));

Normal normal = WilcoxonT.normalApproximation(i);
d = normal.cdf(this.d - 1.0D);

}
else {

WilcoxonT wilcoxonT = new WilcoxonT(i);

d = 0.5D * (1.0D - paramDouble);
long l = wilcoxonT.getMinValue();

double d1 = wilcoxonT.pdf(l);
while (l < wilcoxonT.getMaxValue() && d1 < d) { l++; d1 += wilcoxonT.pdf(l); }
l--;
this.d = (int)(l + 1L);
d = d1 - wilcoxonT.pdf((l + 1L));
} 

this.achievedConfidence = 1.0D - 2.0D * d;
computeInterval(0, this.d, paramArrayOfdouble, null);
this.x = paramArrayOfdouble;
}

public WilcoxonMedianCI(double[] paramArrayOfdouble, double paramDouble) {
this(paramArrayOfdouble, paramDouble, (paramArrayOfdouble.length > 24));
}

public WilcoxonMedianCI(PairedData paramPairedData, double paramDouble, boolean paramBoolean) {
this(paramPairedData.differences(), paramDouble, paramBoolean);
}

public WilcoxonMedianCI(PairedData paramPairedData, double paramDouble) {
this(paramPairedData.differences(), paramDouble, (paramPairedData.getN() > 24));
}

public double hodgesLehmannEstimate() {
return hodgesLehmannEstimate(this.x);
}

public static double hodgesLehmannEstimate(double[] paramArrayOfdouble) {
int k, i = paramArrayOfdouble.length;

double[] arrayOfDouble = new double[1 + i];
System.arraycopy(paramArrayOfdouble, 0, arrayOfDouble, 1, i);
arrayOfDouble[0] = Double.NEGATIVE_INFINITY;
Arrays.sort(arrayOfDouble);

int m = 0;
int[] arrayOfInt1 = new int[1 + i];
int[] arrayOfInt2 = new int[1 + i];
int[] arrayOfInt3 = new int[1 + i];

if (i <= 2) {

if (i == 1) return arrayOfDouble[1]; 
return (arrayOfDouble[1] + arrayOfDouble[2]) / 2.0D;
} 

Random random = new Random();

int i3 = i * (i + 1) / 2;
int n = (i3 + 1) / 2;
int i1 = (i3 + 2) / 2;

byte b;
for (b = 1; b <= i; ) { arrayOfInt1[b] = b; arrayOfInt2[b] = i; b++; }

int j = i3;
int i2 = 0;

int i4 = (i + 1) / 2;
int i5 = (i + 2) / 2;
double d3 = arrayOfDouble[i4] + arrayOfDouble[i5];
boolean bool = true;

label106: while (true) {
if (!bool) {

double d6 = arrayOfDouble[1] + arrayOfDouble[1];
double d5 = arrayOfDouble[i] + arrayOfDouble[i];
for (b = 1; b <= i; b++) {

if (arrayOfInt1[b] <= arrayOfInt2[b]) {
int i6 = arrayOfInt1[b];

d5 = Math.min(d5, arrayOfDouble[i6] + arrayOfDouble[b]);
int i7 = arrayOfInt2[b];

d6 = Math.max(d6, arrayOfDouble[i7] + arrayOfDouble[b]);
} 
}  d3 = (d6 + d5) / 2.0D;

if (d3 <= d5 || d3 > d6) d3 = d6;

if (d5 != d6 && j != 2) {
bool = true;
} else {

return d3 / 2.0D;
} 
} 

while (true) {
if (!bool) {

int i6 = random.nextInt(j - 1);

for (b = 1; b <= i; b++) {

m = b;
if (i6 <= arrayOfInt2[b] - arrayOfInt1[b])
break;  i6 = i6 - arrayOfInt2[b] + arrayOfInt1[b] - 1;
} 

int i7 = (arrayOfInt1[m] + arrayOfInt2[m]) / 2;
d3 = arrayOfDouble[m] + arrayOfDouble[i7];
} 

bool = false;
m = i;

k = 0;

for (b = 1; b <= i; b++) {

arrayOfInt3[b] = 0;

while (m >= b) {

if (arrayOfDouble[b] + arrayOfDouble[m] < d3) {

arrayOfInt3[b] = m - b + 1;

k += arrayOfInt3[b]; break;
} 
m--;
} 
} 
if (k == i2) {
continue label106;
}

if (k == i1 - 1) {
break;
}

if (k - n < 0)

{ 

for (b = 1; b <= i; ) { arrayOfInt1[b] = b + arrayOfInt3[b]; b++; }
}
else { if (k - n == 0) {
break;
}

for (b = 1; b <= i; ) { arrayOfInt2[b] = b + arrayOfInt3[b] - 1; b++; }
}

i2 = 0;
j = 0;

for (b = 1; b <= i; b++) {

i2 = i2 + arrayOfInt1[b] - b;
j = j + arrayOfInt2[b] - arrayOfInt1[b] + 1;
} 

if (j <= 2) {
continue label106;
}
} 

break;
} 
double d1 = arrayOfDouble[i] + arrayOfDouble[i];
double d2 = arrayOfDouble[1] + arrayOfDouble[1];
for (b = 1; b <= i; b++) {

int i6 = arrayOfInt3[b];
int i7 = b + i6;
if (i6 > 0) d2 = Math.max(d2, arrayOfDouble[b] + arrayOfDouble[i7 - 1]); 
i7 = b + i6;
if (i6 < i - b + 1) d1 = Math.min(d1, arrayOfDouble[b] + arrayOfDouble[i7]); 
} 
double d4 = (d1 + d2) / 4.0D;

if (n < i1) return d4; 
if (k == n) d4 = d2 / 2.0D; 
if (k == n - 1) d4 = d1 / 2.0D; 
return d4;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
int i = 500;
double[] arrayOfDouble1 = new double[i];

i = arrayOfDouble1.length;
Normal normal = new Normal(2.0D, 1.0D);

normal.setSeed(123L); byte b;
for (b = 0; b < i; ) { arrayOfDouble1[b] = normal.random(); b++; }

long l1 = System.currentTimeMillis();

WilcoxonMedianCI wilcoxonMedianCI = new WilcoxonMedianCI(arrayOfDouble1, 0.95D, true);
long l2 = System.currentTimeMillis();
System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
System.out.println("CI = [" + wilcoxonMedianCI.getLowerLimit() + ", " + wilcoxonMedianCI.getUpperLimit() + "]" + " d = " + wilcoxonMedianCI.getD() + " Point estimate = " + wilcoxonMedianCI.hodgesLehmannEstimate() + " Achieved confidence = " + wilcoxonMedianCI.getAchievedConfidence());

double[] arrayOfDouble2 = { 51.0D, 48.0D, 52.0D, 62.0D, 64.0D, 51.0D, 55.0D, 60.0D };
double[] arrayOfDouble3 = { 46.0D, 45.0D, 53.0D, 48.0D, 57.0D, 55.0D, 44.0D, 50.0D };

PairedData pairedData = new PairedData(arrayOfDouble2, arrayOfDouble3);
double[] arrayOfDouble4 = { 0.89D, 0.9D, 0.92D, 0.94D, 0.95D, 0.96D, 0.98D, 0.99D };
System.out.println("************* Suntan lotions: n = 8 *******************");
for (b = 0; b < arrayOfDouble4.length; b++) {

System.out.println("Nominal confidence coeff. = " + arrayOfDouble4[b]);
wilcoxonMedianCI = new WilcoxonMedianCI(pairedData, arrayOfDouble4[b], false);
System.out.println("Exact CI=[" + wilcoxonMedianCI.getLowerLimit() + "," + wilcoxonMedianCI.getUpperLimit() + "]" + " d=" + wilcoxonMedianCI.getD() + " Achieved conf.=" + Maths.round(wilcoxonMedianCI.getAchievedConfidence(), 3));
} 

double[] arrayOfDouble5 = { 54.5D, 70.6D, 85.6D, 78.2D, 69.6D, 73.1D, 97.5D, 85.6D, 74.9D, 86.8D, 53.6D, 89.4D };
double[] arrayOfDouble6 = { 55.5D, 72.9D, 84.8D, 78.3D, 71.6D, 74.0D, 97.2D, 88.0D, 74.4D, 89.3D, 52.3D, 91.5D };
pairedData = new PairedData(arrayOfDouble5, arrayOfDouble6);
System.out.println("*************** Weight change data: n = 12 ***************");
for (b = 0; b < arrayOfDouble4.length; b++) {

System.out.println("Nominal confidence coeff. = " + arrayOfDouble4[b]);
wilcoxonMedianCI = new WilcoxonMedianCI(pairedData, arrayOfDouble4[b], false);
System.out.println(" Exact CI=[" + wilcoxonMedianCI.getLowerLimit() + "," + wilcoxonMedianCI.getUpperLimit() + "]" + " d=" + wilcoxonMedianCI.getD() + " Achieved conf.=" + Maths.round(wilcoxonMedianCI.getAchievedConfidence(), 3));

wilcoxonMedianCI = new WilcoxonMedianCI(pairedData, arrayOfDouble4[b], true);
System.out.println("Approx CI=[" + wilcoxonMedianCI.getLowerLimit() + "," + wilcoxonMedianCI.getUpperLimit() + "]" + " d=" + wilcoxonMedianCI.getD() + " Achieved conf.=" + Maths.round(wilcoxonMedianCI.getAchievedConfidence(), 3));
} 
}
}
}

