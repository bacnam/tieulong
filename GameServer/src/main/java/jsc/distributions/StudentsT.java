package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;
import jsc.util.Maths;

public class StudentsT
extends AbstractDistribution
{
static final double LOGPI = Math.log(Math.PI);

private ChiSquared chiSquared;

private double df;

public StudentsT(double paramDouble) {
setDf(paramDouble);
}

public double cdf(double paramDouble) {
double d = tailProb(paramDouble, this.df);
return (paramDouble > 0.0D) ? (1.0D - d) : d;
}

public double getDf() {
return this.df;
}

public double getMaximumPdf() {
return Math.exp(Maths.logGamma(0.5D * (this.df + 1.0D)) - Maths.logGamma(0.5D * this.df) - 0.5D * Math.log(Math.PI * this.df));
}

public double inverseCdf(double paramDouble) {
double d1;
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid probability."); 
if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY;

if (paramDouble > 0.5D) {
d1 = 1.0D - paramDouble;
} else {
d1 = paramDouble;
} 
double d2 = Beta.inverseIncompleteBeta(this.df / 2.0D, 0.5D, Maths.lnB(this.df / 2.0D, 0.5D), d1 + d1);
if (d2 == 1.0D) return 0.0D; 
if (paramDouble < 0.5D) {
if (d2 == 0.0D) {
return Double.NEGATIVE_INFINITY;
}
return -Math.sqrt(this.df * (1.0D - d2) / d2);
} 
if (d2 == 0.0D) {
return Double.POSITIVE_INFINITY;
}
return Math.sqrt(this.df * (1.0D - d2) / d2);
}

public double mean() {
return (this.df > 1.0D) ? 0.0D : Double.NaN;
}

public double pdf(double paramDouble) {
return Math.exp(Maths.logGamma(0.5D * (this.df + 1.0D)) - Maths.logGamma(0.5D * this.df) - 0.5D * ((this.df + 1.0D) * Math.log(1.0D + paramDouble * paramDouble / this.df) + LOGPI + Math.log(this.df)));
}

public double random() {
return this.rand.nextGaussian() / Math.sqrt(this.chiSquared.random() / this.df);
}

public void setDf(double paramDouble) {
if (paramDouble <= 0.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.df = paramDouble;
this.chiSquared = new ChiSquared(paramDouble);
this.chiSquared.setSeed(this.rand.nextLong() + 1L);
}

public void setSeed(long paramLong) {
this.rand.setSeed(paramLong);
this.chiSquared.setSeed(this.rand.nextLong() + 1L);
}

public static double tailProb(double paramDouble1, double paramDouble2) {
return Beta.incompleteBeta(paramDouble2 / (paramDouble2 + paramDouble1 * paramDouble1), paramDouble2 / 2.0D, 0.5D, Maths.lnB(paramDouble2 / 2.0D, 0.5D)) / 2.0D;
} public String toString() {
return new String("Student's t distribution: df = " + this.df + ".");
}

public double variance() {
return (this.df > 2.0D) ? (this.df / (this.df - 2.0D)) : Double.NaN;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
char c = '✐';
StudentsT studentsT = new StudentsT(10.0D);
double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; b++)
{
arrayOfDouble[b] = studentsT.random();
}

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, studentsT, H1.NOT_EQUAL, true);
System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

