package jsc.regression;

import jsc.curvefitting.StraightLineFit;
import jsc.datastructures.PairedData;

public class PearsonCorrelation
implements StraightLineFit
{
private final int n;
private final double r;
private final double a;
private final double b;

public PearsonCorrelation(PairedData paramPairedData) {
this.n = paramPairedData.getN();
double[] arrayOfDouble1 = paramPairedData.getX();
double[] arrayOfDouble2 = paramPairedData.getY();

double d1 = 0.0D, d2 = 0.0D, d3 = 0.0D, d4 = 0.0D, d5 = 0.0D;
byte b;
for (b = 0; b < this.n; b++) {
d5 += arrayOfDouble1[b]; d4 += arrayOfDouble2[b];
}  d5 /= this.n;
d4 /= this.n;
for (b = 0; b < this.n; b++) {

double d6 = arrayOfDouble1[b] - d5;
double d7 = arrayOfDouble2[b] - d4;
d3 += d6 * d6;
d1 += d7 * d7;
d2 += d6 * d7;
} 

if (d3 <= 0.0D) {
throw new IllegalArgumentException("X data are constant.");
}

this.b = d2 / d3;
this.a = d4 - this.b * d5;

if (d1 <= 0.0D) {
throw new IllegalArgumentException("Y data are constant.");
}
this.r = d2 / Math.sqrt(d3 * d1);
}

public double getA() {
return this.a;
}

public double getB() {
return this.b;
}

public int getN() {
return this.n;
}

public double getR() {
return this.r;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 8.000001D, 8.000003D, 8.000002D, 8.000004D, 8.000005D };
double[] arrayOfDouble2 = { 8.0D, 9.0D, 10.0D, 11.0D, 12.0D };
PearsonCorrelation pearsonCorrelation = new PearsonCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2));
System.out.println("n = " + pearsonCorrelation.getN());
System.out.println("r = " + pearsonCorrelation.getR());
System.out.println("a = " + pearsonCorrelation.getA());
System.out.println("b = " + pearsonCorrelation.getB());
}
}
}

