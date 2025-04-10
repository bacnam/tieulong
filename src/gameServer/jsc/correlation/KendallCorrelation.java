package jsc.correlation;

import jsc.datastructures.PairedData;
import jsc.distributions.Normal;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;

public class KendallCorrelation
implements SignificanceTest
{
private final int n;
private final double r;
private int T;
private double SP;

public KendallCorrelation(PairedData paramPairedData, H1 paramH1, double paramDouble) {
byte b1 = 0, b2 = 0;

this.n = paramPairedData.getN();
double[] arrayOfDouble1 = paramPairedData.getX();
double[] arrayOfDouble2 = paramPairedData.getY();

this.T = 0;
for (byte b3 = 0; b3 < this.n - 1; b3++) {
for (int i = b3 + 1; i < this.n; i++) {

double d3 = arrayOfDouble1[b3] - arrayOfDouble1[i];
double d2 = arrayOfDouble2[b3] - arrayOfDouble2[i];
if (Math.abs(d3) <= paramDouble) d3 = 0.0D; 
if (Math.abs(d2) <= paramDouble) d2 = 0.0D; 
double d1 = d3 * d2;
if (d1 != 0.0D) {

b2++; b1++;
if (d1 > 0.0D) {
this.T++;
} else {
this.T--;
} 
} else {

if (d3 != 0.0D) b2++; 
if (d2 != 0.0D) b1++; 
} 
} 
}  this.r = this.T / Math.sqrt(b2) * Math.sqrt(b1);

if (paramH1 == H1.LESS_THAN) {
this.SP = lowerTailProb(this.n, this.T);
} else if (paramH1 == H1.GREATER_THAN) {
this.SP = upperTailProb(this.n, this.T);

}
else if (this.r < 0.0D) {
this.SP = 2.0D * lowerTailProb(this.n, this.T);
} else if (this.r > 0.0D) {
this.SP = 2.0D * upperTailProb(this.n, this.T);
} else {
this.SP = 1.0D;
} 
if (this.SP > 1.0D) this.SP = 1.0D;

}

public KendallCorrelation(PairedData paramPairedData, H1 paramH1) {
this(paramPairedData, paramH1, 0.0D);
}

public KendallCorrelation(PairedData paramPairedData) {
this(paramPairedData, H1.NOT_EQUAL, 0.0D);
}

public int getN() {
return this.n;
}

public double getR() {
return this.r;
}

public double getSP() {
return this.SP;
}

public int getT() {
return this.T;
}

public double getTestStatistic() {
return this.r;
}

public static double lowerTailProb(int paramInt1, int paramInt2) {
return 1.0D - upperTailProb(paramInt1, paramInt2 + 2);
}

public static double upperTailProb(int paramInt1, int paramInt2) {
double d = 1.0D;
int[][] arrayOfInt = new int[3][16];
double[] arrayOfDouble = new double[16];

if (paramInt1 < 1)
throw new IllegalArgumentException("n < 1."); 
int i1 = paramInt1 * (paramInt1 - 1) / 2 - Math.abs(paramInt2);

if (i1 == 0 && paramInt2 <= 0) return d; 
if (paramInt1 > 8) {

double d3 = (paramInt2 - 1) / Math.sqrt((6 + paramInt1 * (5 - paramInt1 * (3 + 2 * paramInt1))) / -18.0D);

arrayOfDouble[1] = d3;
arrayOfInt[1][1] = (int)d3;
arrayOfDouble[2] = d3 * d3 - 1.0D;
for (byte b = 3; b <= 15; b++) {
arrayOfDouble[b] = d3 * arrayOfDouble[b - 1] - (b - 1) * arrayOfDouble[b - 2];
}

double d1 = 1.0D / paramInt1;
double d2 = d1 * (arrayOfDouble[3] * (-0.09D + d1 * (0.045D + d1 * (-0.5325D + d1 * 0.506D))) + d1 * (arrayOfDouble[5] * (0.036735D + d1 * (-0.036735D + d1 * 0.3214D)) + arrayOfDouble[7] * (0.00405D + d1 * (-0.023336D + d1 * 0.07787D)) + d1 * (arrayOfDouble[9] * (-0.0033061D - d1 * 0.0065166D) + arrayOfDouble[11] * (-1.215E-4D + d1 * 0.0025927D) + d1 * (arrayOfDouble[13] * 1.4878E-4D + arrayOfDouble[15] * 2.7338E-6D))));

d = Normal.standardTailProb(d3, true) + d2 * 0.398942D * Math.exp(-0.5D * d3 * d3);
if (d < 0.0D) d = 0.0D; 
if (d > 1.0D) d = 1.0D; 
return d;
} 

if (paramInt2 < 0) i1 -= 2; 
int k = i1 / 2 + 1;
arrayOfInt[1][1] = 1; arrayOfDouble[1] = 1.0D;
arrayOfInt[2][1] = 1;
if (k >= 2)
for (byte b = 2; b <= k; ) { arrayOfInt[1][b] = 0; arrayOfInt[2][b] = 0; b++; }
int j = 1, i = 1; i1 = 1; int m = 1;
int n = 2;

while (true) {
if (i == paramInt1) {

int i3 = 0;
for (i = 1; i <= k; ) { i3 += arrayOfInt[n][i]; i++; }
d = i3 / i1;
if (paramInt2 < 0) d = 1.0D - d; 
return d;
} 
j += i;
i++;
i1 *= i;
m = 3 - m;
n = 3 - n;
byte b1 = 1;
byte b2 = 0;
int i2 = Math.min(k, j);

b1++;
while (b1 <= i2) {

arrayOfInt[n][b1] = arrayOfInt[n][b1 - 1] + arrayOfInt[m][b1];
if (b1 <= i)
continue;  b2++;
arrayOfInt[n][b1] = arrayOfInt[n][b1] - arrayOfInt[m][b2];
} 
} 
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 6.0D, 3.0D, 2.0D, 5.0D, 1.0D, 7.0D, 4.0D };
double[] arrayOfDouble2 = { 4.0D, 5.0D, 1.0D, 7.0D, 3.0D, 6.0D, 2.0D };

KendallCorrelation kendallCorrelation = new KendallCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2), H1.LESS_THAN, 0.0D);
System.out.println("n = " + kendallCorrelation.getN() + " T = " + kendallCorrelation.getT() + " r = " + kendallCorrelation.getR() + " SP = " + kendallCorrelation.getSP());
kendallCorrelation = new KendallCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2), H1.NOT_EQUAL, 0.0D);
System.out.println("n = " + kendallCorrelation.getN() + " T = " + kendallCorrelation.getT() + " r = " + kendallCorrelation.getR() + " SP = " + kendallCorrelation.getSP());
kendallCorrelation = new KendallCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2), H1.GREATER_THAN, 0.0D);
System.out.println("n = " + kendallCorrelation.getN() + " T = " + kendallCorrelation.getT() + " r = " + kendallCorrelation.getR() + " SP = " + kendallCorrelation.getSP());

byte b1 = 8;
for (byte b2 = 0; b2 <= 28; b2++)
System.out.println("n = " + b1 + " S = " + b2 + " p = " + KendallCorrelation.upperTailProb(b1, b2)); 
}
}
}

