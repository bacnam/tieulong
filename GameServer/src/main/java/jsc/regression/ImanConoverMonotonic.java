package jsc.regression;

import jsc.datastructures.PairedData;
import jsc.util.Rank;

public class ImanConoverMonotonic
{
private final int n;
private final double a;
private final double b;
private final double[] x;
private final double[] y;
private final double[] py;
private final Rank rankX;
private final Rank rankY;

public ImanConoverMonotonic(PairedData paramPairedData) {
this.n = paramPairedData.getN();

this.x = new double[this.n];
this.y = new double[this.n];
this.py = new double[this.n];
System.arraycopy(paramPairedData.getX(), 0, this.x, 0, this.n);
System.arraycopy(paramPairedData.getY(), 0, this.y, 0, this.n);

this.rankX = new Rank(this.x, 0.0D);
this.rankY = new Rank(this.y, 0.0D);
double[] arrayOfDouble1 = this.rankX.getRanks();
double[] arrayOfDouble2 = this.rankY.getRanks();
int[] arrayOfInt = this.rankY.getSortIndexes();

PearsonCorrelation pearsonCorrelation = new PearsonCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2));
this.b = pearsonCorrelation.getR();
this.a = 0.5D * (this.n + 1.0D) * (1.0D - this.b);

for (byte b = 0; b < this.n; b++) {

double d = this.b * arrayOfDouble1[b] + this.a;

this.py[b] = interpol(this.n, d, arrayOfDouble2, this.y, arrayOfInt);
} 
}

public int getN() {
return this.n;
}

public double[] getPredY() {
return this.py;
}

public double getPredY(double paramDouble) {
double d1 = interpol(this.n, paramDouble, this.x, this.rankX.getRanks(), this.rankX.getSortIndexes());
double d2 = this.b * d1 + this.a;
return interpol(this.n, d2, this.rankY.getRanks(), this.y, this.rankY.getSortIndexes());
}

public Rank getRankX() {
return this.rankX;
}

public Rank getRankY() {
return this.rankY;
}

public double getSortedPredY(int paramInt) {
return this.py[this.rankX.getSortIndex(paramInt)];
}

public double getSortedResidual(int paramInt) {
int i = this.rankX.getSortIndex(paramInt);
return this.y[i] - this.py[i];
}

public double getSortedX(int paramInt) {
return this.x[this.rankX.getSortIndex(paramInt)];
}

public double[] getX() {
return this.x;
}

public double[] getY() {
return this.y;
}

public double getSpearmanCoeff() {
return this.b;
}

double interpol(int paramInt, double paramDouble, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int[] paramArrayOfint) {
if (paramInt < 1) {
throw new IllegalArgumentException("No data");
}
int i = paramArrayOfint[0], j = paramArrayOfint[paramInt - 1];
if (paramDouble < paramArrayOfdouble1[i])
return paramArrayOfdouble2[i]; 
if (paramDouble > paramArrayOfdouble1[j])
return paramArrayOfdouble2[j]; 
if (paramDouble == paramArrayOfdouble1[i]) {
return paramArrayOfdouble2[i];
}
for (byte b = 1; b < paramInt; b++) {

i = paramArrayOfint[b - 1];
j = paramArrayOfint[b];
if (paramArrayOfdouble1[i] > paramArrayOfdouble1[j])
{
throw new IllegalArgumentException("X-values not in ascending order."); } 
if (paramDouble <= paramArrayOfdouble1[j])
{
return paramArrayOfdouble2[i] + (paramArrayOfdouble2[j] - paramArrayOfdouble2[i]) * (paramDouble - paramArrayOfdouble1[i]) / (paramArrayOfdouble1[j] - paramArrayOfdouble1[i]);
}
} 
return paramArrayOfdouble2[j];
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 104.0D, 161.0D, 156.0D, 96.0D, 149.0D, 143.0D, 113.0D, 142.0D, 115.0D, 175.0D, 135.0D, 145.0D, 137.0D, 151.0D, 126.0D };
double[] arrayOfDouble2 = { 25.0D, 47.0D, 40.0D, 17.0D, 49.0D, 39.0D, 33.0D, 30.0D, 31.0D, 44.0D, 34.0D, 43.0D, 35.0D, 42.0D, 36.0D };
ImanConoverMonotonic imanConoverMonotonic = new ImanConoverMonotonic(new PairedData(arrayOfDouble1, arrayOfDouble2));

System.out.println("n = " + imanConoverMonotonic.getN());
System.out.println("Spearman coeff = " + imanConoverMonotonic.getSpearmanCoeff());
for (byte b = 0; b < imanConoverMonotonic.getN(); b++)
{

System.out.println("X = " + imanConoverMonotonic.getSortedX(b) + " Y = " + imanConoverMonotonic.getSortedPredY(b));
}
double d = 120.0D;
System.out.println("Estimated Y for x = " + d + " is " + imanConoverMonotonic.getPredY(d));
}
}
}

