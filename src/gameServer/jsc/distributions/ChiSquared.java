package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class ChiSquared
extends Gamma
{
private double df;
private static final int MAXIT = 20;
private static final double E = 5.0E-17D;

public ChiSquared(double paramDouble) {
super(0.5D * paramDouble, 2.0D);
this.df = paramDouble;
}

public double getDf() {
return this.df;
}

public double inverseCdf(double paramDouble) {
return inverseCdf(paramDouble, this.df, this.logGammaShape);
}

public static double inverseCdf(double paramDouble1, double paramDouble2, double paramDouble3) {
double d2, d3;
if (paramDouble1 < 0.0D) throw new IllegalArgumentException("Invalid probability."); 
if (paramDouble1 > 0.999998D) return Double.POSITIVE_INFINITY;

if (paramDouble2 <= 0.0D) {
throw new IllegalArgumentException("Invalid distribution parameter.");
}

double d4 = 0.5D * paramDouble2;
double d1 = d4 - 1.0D;

if (paramDouble2 >= -(1.24D * Math.log(paramDouble1))) {

if (paramDouble2 > 0.32D) {

double d7 = Normal.inverseStandardCdf(paramDouble1);

double d6 = 0.222222D / paramDouble2;
d2 = paramDouble2 * Math.pow(d7 * Math.sqrt(d6) + 1.0D - d6, 3.0D);

if (d2 > 2.2D * paramDouble2 + 6.0D) {
d2 = -2.0D * (Math.log(1.0D - paramDouble1) - d1 * Math.log(0.5D * d2) + paramDouble3);
}
} else {

d2 = 0.4D;
double d = Math.log(1.0D - paramDouble1);

do {
d3 = d2;
double d6 = 1.0D + d2 * (4.67D + d2);
double d7 = d2 * (6.73D + d2 * (6.66D + d2));
double d8 = -0.5D + (4.67D + d2 + d2) / d6 - (6.73D + d2 * (13.32D + 3.0D * d2)) / d7;
d2 -= (1.0D - Math.exp(d + paramDouble3 + 0.5D * d2 + d1 * 0.6931471806D) * d7 / d6) / d8;
} while (Math.abs(d3 / d2 - 1.0D) > 0.01D);
}

} else {

d2 = Math.pow(paramDouble1 * d4 * Math.exp(paramDouble3 + d4 * 0.6931471806D), 1.0D / d4);

if (d2 < 5.0E-17D) return d2; 
} 
double d5 = d2;
byte b = 0;

do {
d3 = d2;
double d8 = 0.5D * d2;
double d9 = paramDouble1 - Gamma.incompleteGamma(d8, d4);

double d16 = d9 * Math.exp(d4 * 0.6931471806D + paramDouble3 + d8 - d1 * Math.log(d2));
double d7 = d16 / d2;
double d6 = 0.5D * d16 - d7 * d1;
double d10 = (210.0D + d6 * (140.0D + d6 * (105.0D + d6 * (84.0D + d6 * (70.0D + 60.0D * d6))))) / 420.0D;
double d11 = (420.0D + d6 * (735.0D + d6 * (966.0D + d6 * (1141.0D + 1278.0D * d6)))) / 2520.0D;
double d12 = (210.0D + d6 * (462.0D + d6 * (707.0D + 932.0D * d6))) / 2520.0D;
double d13 = (252.0D + d6 * (672.0D + 1182.0D * d6) + d1 * (294.0D + d6 * (889.0D + 1740.0D * d6))) / 5040.0D;
double d14 = (84.0D + 264.0D * d6 + d1 * (175.0D + 606.0D * d6)) / 2520.0D;
double d15 = (120.0D + d1 * (346.0D + 127.0D * d1)) / 5040.0D;
d2 += d16 * (1.0D + 0.5D * d16 * d10 - d7 * d1 * (d10 - d7 * (d11 - d7 * (d12 - d7 * (d13 - d7 * (d14 - d7 * d15))))));

if (Double.isNaN(d2) || d2 < 0.0D)
{

return d5;

}

}
while (Math.abs(d3 / d2 - 1.0D) > 5.0E-17D && b++ < 20);

return d2;
}

public String toString() {
return new String("Chi-squared distribution: df = " + this.df + ".");
}

public static double upperTailProb(double paramDouble1, double paramDouble2) {
return 1.0D - Gamma.incompleteGamma(0.5D * paramDouble1, 0.5D * paramDouble2);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d = 50.0D;
ChiSquared chiSquared = new ChiSquared(d);

int i = 100000;

chiSquared = new ChiSquared(100.0D);
double[] arrayOfDouble = new double[i];
for (byte b = 0; b < i; ) { arrayOfDouble[b] = chiSquared.random(); b++; }

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, chiSquared, H1.NOT_EQUAL, false);
System.out.println("n = " + i + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

