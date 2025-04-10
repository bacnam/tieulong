package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;
import jsc.util.Maths;

public class Gamma
extends AbstractDistribution
{
private static final double ELIMIT = Math.log(Double.MIN_VALUE);

private static final double OFLO = Math.sqrt(Double.MAX_VALUE);

private double A;

private double B;

private double C;

double logGammaShape;

double logScale;

double shape;

double scale;

public Gamma(double paramDouble1, double paramDouble2) {
if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.shape = paramDouble1;
this.scale = paramDouble2;

this.logGammaShape = Maths.logGamma(paramDouble1);
this.logScale = Math.log(paramDouble2);

if (paramDouble1 > 1.0D) {

this.A = 1.0D / Math.sqrt(paramDouble1 + paramDouble1 - 1.0D);
this.B = paramDouble1 - Math.log(4.0D);
this.C = paramDouble1 + 1.0D / this.A;
}
else {

this.A = 1.0D - paramDouble1;
this.B = (paramDouble1 + Math.E) / Math.E;
this.C = 1.0D / paramDouble1;
} 
}

public double cdf(double paramDouble) {
return incompleteGamma(paramDouble / this.scale, this.shape);
}

private double gamvar() {
int i;
if (this.shape < 4.0D && (i = (int)this.shape) == this.shape) {

double d = 1.0D;
for (byte b = 1; b <= i; ) { d *= 1.0D - this.rand.nextDouble(); b++; }
return -Math.log(d);
} 
if (this.shape > 1.0D)
{
while (true) {

double d = this.rand.nextDouble(); if (d != 0.0D) {
double d2 = 1.0D - this.rand.nextDouble();

double d3 = this.A * Math.log(d / (1.0D - d));
double d1 = this.shape * Math.exp(d3);

if (this.B + this.C * d3 - d1 >= Math.log(d * d * d2)) {
return d1;
}
} 
} 
}

while (true) {
double d3 = this.B * this.rand.nextDouble();
double d2 = -Math.log(1.0D - this.rand.nextDouble());
if (d3 > 1.0D) {

double d = -Math.log((this.B - d3) / this.shape);
if (this.A * Math.log(d) <= d2) return d;

continue;
} 
double d1 = Math.pow(d3, this.C);
if (d1 <= d2) return d1;

} 
}

public double getScale() {
return this.scale;
}

public double getShape() {
return this.shape;
}

public static double incompleteGamma(double paramDouble1, double paramDouble2) {
double d9 = 0.0D;

if (paramDouble2 <= 0.0D || paramDouble1 < 0.0D) {
throw new IllegalArgumentException("Invalid argument of incomplete gamma integral.");
}
if (paramDouble1 == 0.0D) return 0.0D;

if (paramDouble2 > 1000.0D) {

double d = 3.0D * Math.sqrt(paramDouble2) * (Math.pow(paramDouble1 / paramDouble2, 0.3333333333333333D) + 1.0D / 9.0D * paramDouble2 - 1.0D);
return Normal.standardTailProb(d, false);
} 

if (paramDouble1 > 1000000.0D) return 1.0D;

if (paramDouble1 <= 1.0D || paramDouble1 < paramDouble2)

{ 

double d10 = paramDouble2 * Math.log(paramDouble1) - paramDouble1 - Maths.logGamma(paramDouble2 + 1.0D);
double d11 = 1.0D;
d9 = 1.0D;
double d12 = paramDouble2;

while (true)
{ d12++;
d11 *= paramDouble1 / d12;
d9 += d11;
if (d11 <= 1.0E-17D)
{ d10 += Math.log(d9);
d9 = 0.0D;
if (d10 >= ELIMIT) d9 = Math.exp(d10);

return d9; }  }  }  double d5 = paramDouble2 * Math.log(paramDouble1) - paramDouble1 - Maths.logGamma(paramDouble2); double d7 = 1.0D - paramDouble2; double d8 = d7 + paramDouble1 + 1.0D; double d6 = 0.0D; double d1 = 1.0D; double d2 = paramDouble1; double d3 = paramDouble1 + 1.0D; double d4 = paramDouble1 * d8; d9 = d3 / d4; while (true) { d7++; d8 += 2.0D; d6++; double d12 = d7 * d6; double d10 = d8 * d3 - d12 * d1; double d11 = d8 * d4 - d12 * d2; if (Math.abs(d11) > 0.0D) { double d = d10 / d11; if (Math.abs(d9 - d) <= Math.min(1.0E-17D, 1.0E-17D * d)) break;  d9 = d; }  d1 = d3; d2 = d4; d3 = d10; d4 = d11; if (Math.abs(d10) >= OFLO) { d1 /= OFLO; d2 /= OFLO; d3 /= OFLO; d4 /= OFLO; }  }  d5 += Math.log(d9); d9 = 1.0D; if (d5 >= ELIMIT) d9 = 1.0D - Math.exp(d5);  return d9;
}

public double inverseCdf(double paramDouble) {
return 0.5D * this.scale * ChiSquared.inverseCdf(paramDouble, this.shape + this.shape, this.logGammaShape);
}
public double mean() {
return this.shape * this.scale;
}

public double pdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
return Math.exp((this.shape - 1.0D) * Math.log(paramDouble) - paramDouble / this.scale - this.logGammaShape - this.shape * this.logScale);
}

public double random() {
return this.scale * gamvar();
}
public String toString() {
return new String("Gamma distribution: shape = " + this.shape + ", scale = " + this.scale + ".");
} public double variance() {
return this.shape * this.scale * this.scale;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d1 = 50.0D;
double d2 = 20.0D;
Gamma gamma = new Gamma(d1, d2);

char c = '✐';

gamma = new Gamma(0.01D, 1.0D);

double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; ) { arrayOfDouble[b] = gamma.random(); b++; }

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, gamma, H1.NOT_EQUAL, false);
System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

