package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;
import jsc.util.Maths;

public class NoncentralStudentsT
extends AbstractContinuousDistribution
{
static final int VMAX = 340;
static final int ITRMAX = 1000;
static final double R2PI = Math.sqrt(0.6366197723675814D);
static final double ALNRPI = 0.5D * Math.log(Math.PI);
static final double LGHALF = Maths.logGamma(0.5D);

private double df;

private double delta;

private ChiSquared chiSquared;

private Normal normalApprox;

private StudentsT studentsT;

private double logGammaHalfV;

private double logSqrt2dv;

private double constant;

private double HD2;

private double albeta;

private double pConst;

public NoncentralStudentsT(double paramDouble1, double paramDouble2) {
super(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);

setParameters(paramDouble1, paramDouble2);
}

public double cdf(double paramDouble) {
if (this.delta == 0.0D) return this.studentsT.cdf(paramDouble);

double d1 = 0.0D;
double d4 = paramDouble;
double d2 = this.delta;
boolean bool = false;
if (paramDouble < 0.0D) {

bool = true;
d4 = -d4;
d2 = -d2;
} 

double d3 = 1.0D;
double d6 = paramDouble * paramDouble;
double d5 = d6 / (d6 + this.df);
if (d5 > 0.0D) {

double d9, d12 = d2 * d2;

double d13 = this.pConst;

double d14 = R2PI * d13 * d2;
double d16 = 0.5D - d13;
double d7 = 0.5D;
double d8 = 0.5D * this.df;
double d15 = Math.pow(1.0D - d5, d8);

double d18 = Beta.incompleteBeta(d5, d7, d8, this.albeta);
double d11 = 2.0D * d15 * Math.exp(d7 * Math.log(d5) - this.albeta);
double d17 = 1.0D - d15;
double d10 = d8 * d5 * d15;
d1 = d13 * d18 + d14 * d17;

do {
d7++;
d18 -= d11;
d17 -= d10;
d11 = d11 * d5 * (d7 + d8 - 1.0D) / d7;
d10 = d10 * d5 * (d7 + d8 - 0.5D) / (d7 + 0.5D);
double d = d3 + d3;
d13 = d13 * d12 / d;
d14 = d14 * d12 / (d + 1.0D);
d16 -= d13;
d3++;
d1 = d1 + d13 * d18 + d14 * d17;
d9 = 2.0D * d16 * (d18 - d11);
} while (d9 > this.tolerance && d3 <= 1000.0D);
} 

if (d3 > 1000.0D)
throw new RuntimeException("Cannot calculate cdf to required accuracy."); 
d1 += Normal.standardTailProb(d2, true);
if (bool) d1 = 1.0D - d1; 
return d1;
}

public double getDelta() {
return this.delta;
}

public double getDf() {
return this.df;
}

public double inverseCdf(double paramDouble) {
if (this.delta == 0.0D) {
return this.studentsT.inverseCdf(paramDouble);
}
return super.inverseCdf(paramDouble);
}

public double mean() {
if (this.df <= 1.0D) return Double.NaN; 
if (this.delta == 0.0D) return 0.0D; 
return this.delta * Math.sqrt(0.5D * this.df) * Math.exp(Maths.logGamma(0.5D * (this.df - 1.0D)) - this.logGammaHalfV);
}

public Normal normalApproximation() {
return this.normalApprox;
}

public double pdf(double paramDouble) {
if (this.delta == 0.0D) return this.studentsT.pdf(paramDouble); 
if (paramDouble == 0.0D) return Math.exp(-this.HD2 + Maths.logGamma(0.5D * (this.df + 1.0D))) / this.constant; 
if (this.df > 340.0D) return this.normalApprox.pdf(paramDouble);

double d1 = this.delta * paramDouble;
boolean bool = (d1 < 0.0D) ? true : false;
double d2 = 0.0D;
double d3 = this.logSqrt2dv + Math.log(Math.abs(d1));
double d4 = 0.5D * Math.log(1.0D + paramDouble * paramDouble / this.df);

for (byte b = 0; b < 'Ϩ'; b++) {

double d = Math.exp(b * d3 - this.HD2 + Maths.logGamma(0.5D * (this.df + b + 1.0D)) - Maths.logGamma((b + 1)) - (this.df + b + 1.0D) * d4);
if (bool && b % 2 > 0) {
d2 -= d;
} else {
d2 += d;
}  if (Math.abs(d) < this.tolerance) return d2 / this.constant; 
} 
throw new RuntimeException("Cannot calculate pdf to required accuracy.");
}

public double random() {
return (this.rand.nextGaussian() + this.delta) / Math.sqrt(this.chiSquared.random() / this.df);
}

public void setParameters(double paramDouble1, double paramDouble2) {
if (paramDouble1 <= 0.0D)
throw new IllegalArgumentException("Invalid degrees of freedom."); 
this.df = paramDouble1;
this.delta = paramDouble2;
if (paramDouble2 == 0.0D) {
this.studentsT = new StudentsT(paramDouble1);
} else {

this.studentsT = null;
this.HD2 = 0.5D * paramDouble2 * paramDouble2;
this.pConst = 0.5D * Math.exp(-this.HD2);

double d = 0.5D * paramDouble1;
this.logGammaHalfV = Maths.logGamma(0.5D * paramDouble1);
this.logSqrt2dv = Math.log(Math.sqrt(2.0D / paramDouble1));
this.albeta = ALNRPI + this.logGammaHalfV - Maths.logGamma(0.5D * (paramDouble1 + 1.0D));
this.constant = Math.exp(LGHALF + Maths.logGamma(0.5D * paramDouble1) + 0.5D * Math.log(paramDouble1));
} 
this.chiSquared = new ChiSquared(paramDouble1);
this.chiSquared.setSeed(this.rand.nextLong());
this.normalApprox = new Normal(paramDouble2, Math.sqrt(1.0D + paramDouble2 * paramDouble2 / (paramDouble1 + paramDouble1)));
}

public void setSeed(long paramLong) {
this.rand.setSeed(paramLong);
this.chiSquared.setSeed(this.rand.nextLong() + 1L);
}

public String toString() {
return new String("Noncentral Student's t distribution: df = " + this.df + ", delta = " + this.delta + ".");
}

public double variance() {
if (this.df <= 2.0D) return Double.NaN; 
double d = mean();
return (1.0D + this.delta * this.delta) * this.df / (this.df - 2.0D) - d * d;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
char c = '✐';

NoncentralStudentsT noncentralStudentsT = new NoncentralStudentsT(100.0D, 10.0D);

double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; b++)
{
arrayOfDouble[b] = noncentralStudentsT.random();
}

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, noncentralStudentsT, H1.NOT_EQUAL, true);
System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

