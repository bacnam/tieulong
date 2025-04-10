package jsc.curvefitting;

import jsc.ci.AbstractConfidenceInterval;
import jsc.ci.ConfidenceInterval;
import jsc.datastructures.PairedData;
import jsc.distributions.StudentsT;

public class LineFit
implements StraightLineFit
{
private final int n;
private double a;
private double b;
private double chi2;
private double ax;
private double ay;
private double sxx;

public LineFit(PairedData paramPairedData) {
this(paramPairedData, null);
}

public LineFit(PairedData paramPairedData, double[] paramArrayOfdouble) {
this.n = paramPairedData.getN();
double[] arrayOfDouble1 = paramPairedData.getX();
double[] arrayOfDouble2 = paramPairedData.getY();
if (paramArrayOfdouble == null) {
unweightedLineFit(arrayOfDouble1, arrayOfDouble2);
} else {

if (this.n != paramArrayOfdouble.length) {
throw new IllegalArgumentException("Weights array is wrong length.");
}
weightedLineFit(arrayOfDouble1, arrayOfDouble2, paramArrayOfdouble);
} 
}

private void unweightedLineFit(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
this.a = 0.0D;
this.b = 0.0D;

if (this.n == 2) {

double d = paramArrayOfdouble1[1] - paramArrayOfdouble1[0];
if (d == 0.0D)
throw new IllegalArgumentException("X data are constant."); 
this.b = (paramArrayOfdouble2[1] - paramArrayOfdouble2[0]) / d;
this.a = paramArrayOfdouble2[0] - this.b * paramArrayOfdouble1[0];
}
else {

double d = 0.0D;
this.ay = 0.0D; this.ax = 0.0D; this.sxx = 0.0D;
byte b1;
for (b1 = 0; b1 < this.n; b1++) {
this.ax += paramArrayOfdouble1[b1]; this.ay += paramArrayOfdouble2[b1];
}  this.ax /= this.n;
this.ay /= this.n;
for (b1 = 0; b1 < this.n; b1++) {

double d1 = paramArrayOfdouble1[b1] - this.ax;
double d2 = paramArrayOfdouble2[b1] - this.ay;
this.sxx += d1 * d1;
d += d1 * d2;
} 

if (this.sxx <= 0.0D) {
throw new IllegalArgumentException("X data are constant.");
}

this.b = d / this.sxx;
this.a = this.ay - this.b * this.ax;
} 

this.chi2 = 0.0D;
for (byte b = 0; b < this.n; b++) {

double d = paramArrayOfdouble2[b] - this.a - this.b * paramArrayOfdouble1[b];
this.chi2 += d * d;
} 
}

private void weightedLineFit(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double[] paramArrayOfdouble3) {
double d2 = 0.0D, d3 = 0.0D, d4 = 0.0D;

this.b = 0.0D;
double d5 = 0.0D; byte b;
for (b = 0; b < this.n; b++) {

double d = paramArrayOfdouble3[b] * paramArrayOfdouble3[b];
d5 += d;
d2 += paramArrayOfdouble1[b] * d;
d3 += paramArrayOfdouble2[b] * d;
} 
if (d5 <= 0.0D)
throw new IllegalArgumentException("Zero weights."); 
double d1 = d2 / d5;
for (b = 0; b < this.n; b++) {

double d = (paramArrayOfdouble1[b] - d1) * paramArrayOfdouble3[b];
d4 += d * d;
this.b += d * paramArrayOfdouble2[b] * paramArrayOfdouble3[b];
} 
if (d4 <= 0.0D)
throw new IllegalArgumentException("Weighted X data are constant."); 
this.b /= d4;
this.a = (d3 - d2 * this.b) / d5;
this.chi2 = 0.0D;
for (b = 0; b < this.n; b++) {

double d = (paramArrayOfdouble2[b] - this.a - this.b * paramArrayOfdouble1[b]) * paramArrayOfdouble3[b];
this.chi2 += d * d;
} 
}

public double getA() {
return this.a;
}

public double getB() {
return this.b;
}

public ConfidenceInterval getCIA(double paramDouble) {
if (paramDouble <= 0.0D || paramDouble >= 1.0D)
throw new IllegalArgumentException("Invalid confidence coefficient"); 
if (this.n < 3)
throw new IllegalArgumentException("Insufficient data for CI."); 
double d1 = getQuantileOfT(paramDouble);
double d2 = this.chi2 / (this.n - 2);
double d3 = Math.sqrt(d2 * (1.0D / this.n + this.ax * this.ax / this.sxx));

double d4 = d1 * d3;
return (ConfidenceInterval)new AbstractConfidenceInterval(paramDouble, this.a - d4, this.a + d4);
}

public ConfidenceInterval getCIB(double paramDouble) {
if (paramDouble <= 0.0D || paramDouble >= 1.0D)
throw new IllegalArgumentException("Invalid confidence coefficient"); 
if (this.n < 3)
throw new IllegalArgumentException("Insufficient data for CI."); 
double d1 = getQuantileOfT(paramDouble);
double d2 = this.chi2 / (this.n - 2);
double d3 = Math.sqrt(d2 / this.sxx);

double d4 = d1 * d3;
return (ConfidenceInterval)new AbstractConfidenceInterval(paramDouble, this.b - d4, this.b + d4);
}

public double[][] getIntervals(double paramDouble1, int paramInt, double paramDouble2, double paramDouble3) {
if (paramDouble1 <= 0.0D || paramDouble1 >= 1.0D)
throw new IllegalArgumentException("Invalid confidence coefficient"); 
if (this.n < 3)
throw new IllegalArgumentException("Insufficient data for CI."); 
if (paramDouble3 <= paramDouble2)
throw new IllegalArgumentException("Invalid x values."); 
double d1 = 1.0D / this.n;
double d2 = getQuantileOfT(paramDouble1);
double d3 = Math.sqrt(this.chi2 / (this.n - 2));
double[][] arrayOfDouble = new double[paramInt][5];
double d4 = (paramDouble3 - paramDouble2) / (paramInt - 1.0D);
double d5 = d2 * d3;

for (byte b = 0; b < paramInt; b++) {

double d11 = paramDouble2 + b * d4;
double d6 = d11 - this.ax;
double d7 = this.a + this.b * d11;
double d8 = d6 * d6 / this.sxx + d1;
double d9 = d5 * Math.sqrt(d8);
double d10 = d5 * Math.sqrt(d8 + 1.0D);
arrayOfDouble[b][0] = d11;
arrayOfDouble[b][1] = d7 - d9;
arrayOfDouble[b][2] = d7 + d9;
arrayOfDouble[b][3] = d7 - d10;
arrayOfDouble[b][4] = d7 + d10;
} 
return arrayOfDouble;
}

public double getMeanX() {
return this.ax;
}

public double getMeanY() {
return this.ay;
}

public int getN() {
return this.n;
}

double getQuantileOfT(double paramDouble) {
StudentsT studentsT = new StudentsT((this.n - 2));
double d = 1.0D - paramDouble;
return studentsT.inverseCdf(1.0D - 0.5D * d);
}

public double getSumOfSquares() {
return this.chi2;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 8.000001D, 8.000003D, 8.000002D, 8.000004D, 8.000005D };
double[] arrayOfDouble2 = { 8.0D, 9.0D, 10.0D, 11.0D, 12.0D };
double[] arrayOfDouble3 = { 1.0D, 1.0D, 1.0D, 1.0D, 1.0D };
LineFit lineFit1 = new LineFit(new PairedData(arrayOfDouble1, arrayOfDouble2));
System.out.println("n = " + lineFit1.getN());
System.out.println("a = " + lineFit1.getA());
System.out.println("b = " + lineFit1.getB());
System.out.println("ss = " + lineFit1.getSumOfSquares());

LineFit lineFit2 = new LineFit(new PairedData(arrayOfDouble1, arrayOfDouble2), arrayOfDouble3);
System.out.println("n = " + lineFit2.getN());
System.out.println("a = " + lineFit2.getA());
System.out.println("b = " + lineFit2.getB());
System.out.println("ss = " + lineFit2.getSumOfSquares());

double[] arrayOfDouble4 = { 0.1D, 0.2D };
double[] arrayOfDouble5 = { 1.0D, 2.0D };
LineFit lineFit3 = new LineFit(new PairedData(arrayOfDouble4, arrayOfDouble5));
System.out.println("n = " + lineFit3.getN());
System.out.println("a = " + lineFit3.getA());
System.out.println("b = " + lineFit3.getB());
System.out.println("ss = " + lineFit3.getSumOfSquares());
}
}
}

