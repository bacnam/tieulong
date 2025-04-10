package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;
import jsc.util.Maths;

public class NoncentralChiSquared
extends AbstractContinuousDistribution
{
static final int ITRMAX = 100;
static final double LOG2 = Math.log(2.0D);

private double delta;

private double df;

private double halfDf;

private double logGammaHalfDf1;

private double logDelta;

private double sqrtDelta;

private double expMhalfDelta;

private ChiSquared chiSquared;

private ChiSquared chiSquaredVm1;

public NoncentralChiSquared(double paramDouble1, double paramDouble2) {
super(0.0D, Double.POSITIVE_INFINITY, true);

setParameters(paramDouble1, paramDouble2);
}

public double cdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
if (paramDouble == 0.0D) return 0.0D; 
if (this.delta == 0.0D) return this.chiSquared.cdf(paramDouble); 
double d1 = this.df;

double d2 = this.delta / 2.0D;

double d3 = 1.0D;

double d4 = this.expMhalfDelta;
double d5 = d4;
double d6 = paramDouble / 2.0D;
double d7 = this.halfDf;
double d8 = Math.pow(d6, d7) * Math.exp(-d6) / this.logGammaHalfDf1;
double d9 = d5 * d8;
double d10 = d9;

while (d1 + 2.0D * d3 - paramDouble <= 0.0D) {

d4 = d4 * d2 / d3;
d5 += d4;
d8 = d8 * paramDouble / (d1 + 2.0D * d3);
d9 = d5 * d8;
d10 += d9;
d3++;
} 

while (true) {
double d = d8 * paramDouble / (d1 + 2.0D * d3 - paramDouble);
if (d < this.tolerance) return d10;

d4 = d4 * d2 / d3;
d5 += d4;
d8 = d8 * paramDouble / (d1 + 2.0D * d3);
d9 = d5 * d8;
d10 += d9;
d3++;
if (d3 > 100.0D) {
throw new RuntimeException("Cannot calculate cdf to required accuracy.");
}
} 
}

public double getDelta() {
return this.delta;
}

public double getDf() {
return this.df;
}

public double inverseCdf(double paramDouble) {
if (this.delta == 0.0D) {
return this.chiSquared.inverseCdf(paramDouble);
}
return super.inverseCdf(paramDouble);
}
public double mean() {
return this.df + this.delta;
}

public double pdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
if (this.delta == 0.0D) return this.chiSquared.pdf(paramDouble);

if (paramDouble == 0.0D) return 0.0D;

double d1 = 0.0D;

double d2 = -0.5D * (paramDouble + this.delta + this.df * LOG2);
double d3 = Math.log(paramDouble);
for (byte b = 0; b < 'Ϩ'; b++) {

double d = Math.exp(d2 + b * this.logDelta + ((b - 1) + this.halfDf) * d3 - 2.0D * b * LOG2 - Maths.logGamma((b + 1)) - Maths.logGamma(this.halfDf + b));

d1 += d;

if (d < this.tolerance * d1) return d1; 
} 
throw new RuntimeException("Cannot calculate pdf to required accuracy.");
}

public double random() {
if (this.delta == 0.0D) return this.chiSquared.random();

if (this.df > 1.0D && this.df == Math.floor(this.df)) {

double d = this.rand.nextGaussian() + this.sqrtDelta;
return d * d + this.chiSquaredVm1.random();
} 
return super.random();
}

public void setParameters(double paramDouble1, double paramDouble2) {
if (paramDouble2 < 0.0D)
throw new IllegalArgumentException("Invalid noncentrality parameter."); 
if (paramDouble1 < 0.0D || (paramDouble1 == 0.0D && paramDouble2 == 0.0D))
throw new IllegalArgumentException("Invalid degrees of freedom."); 
this.delta = paramDouble2;
this.df = paramDouble1;
if (paramDouble2 == 0.0D) {
this.chiSquared = new ChiSquared(paramDouble1);
} else {

this.chiSquared = null;
this.logDelta = Math.log(paramDouble2);
this.sqrtDelta = Math.sqrt(paramDouble2);
this.expMhalfDelta = Math.exp(-0.5D * paramDouble2);
if (paramDouble1 > 1.0D && paramDouble1 == Math.floor(paramDouble1)) this.chiSquaredVm1 = new ChiSquared(paramDouble1 - 1.0D); 
this.halfDf = 0.5D * paramDouble1;
this.logGammaHalfDf1 = Math.exp(Maths.logGamma(this.halfDf + 1.0D));
} 
}

public String toString() {
return new String("Noncentral chi-squared distribution: df = " + this.df + ", delta = " + this.delta + ".");
} public double variance() {
return 2.0D * (this.df + this.delta + this.delta);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
char c = '✐';

NoncentralChiSquared noncentralChiSquared = new NoncentralChiSquared(4.1D, 0.5D);

double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; b++)
{
arrayOfDouble[b] = noncentralChiSquared.random();
}

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, noncentralChiSquared, H1.NOT_EQUAL, true);
System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

