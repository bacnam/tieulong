package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;
import jsc.util.Maths;

public class FishersF
extends AbstractDistribution
{
private ChiSquared chiSquared1;
private ChiSquared chiSquared2;
private double df1;
private double df2;
private double lnB;

public FishersF(double paramDouble1, double paramDouble2) {
if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.df1 = paramDouble1;
this.df2 = paramDouble2;
this.lnB = Maths.lnB(0.5D * paramDouble2, 0.5D * paramDouble1);
this.chiSquared1 = new ChiSquared(paramDouble1);
this.chiSquared2 = new ChiSquared(paramDouble2);
this.chiSquared2.setSeed(this.rand.nextLong());
}

public double cdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
if (paramDouble == 0.0D) return 0.0D; 
return 1.0D - Beta.incompleteBeta(this.df2 / (this.df2 + this.df1 * paramDouble), 0.5D * this.df2, 0.5D * this.df1, this.lnB);
}

public double getDf1() {
return this.df1;
}

public double getDf2() {
return this.df2;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid probability."); 
if (paramDouble == 0.0D) return 0.0D; 
if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
double d = Beta.inverseIncompleteBeta(0.5D * this.df2, 0.5D * this.df1, this.lnB, 1.0D - paramDouble);
if (d == 0.0D) {
return Double.POSITIVE_INFINITY;
}
return this.df2 * (1.0D - d) / d * this.df1;
}

public double mean() {
return (this.df2 > 2.0D) ? (this.df2 / (this.df2 - 2.0D)) : Double.NaN;
}

public double pdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
if (paramDouble == 0.0D) return 0.0D;

return Math.exp(0.5D * (this.df1 * (Math.log(this.df1) - Math.log(this.df2)) + (this.df1 - 2.0D) * Math.log(paramDouble) - (this.df1 + this.df2) * Math.log(1.0D + this.df1 / this.df2 * paramDouble)) - this.lnB);
}

public double random() {
return this.df2 * this.chiSquared1.random() / this.df1 * this.chiSquared2.random();
}

public void setSeed(long paramLong) {
this.rand.setSeed(paramLong);
this.chiSquared1.setSeed(this.rand.nextLong());
this.chiSquared2.setSeed(this.rand.nextLong());
}

public String toString() {
return new String("Fisher's F distribution: df1 = " + this.df1 + ", df2 = " + this.df2 + ".");
}

public static double upperTailProb(double paramDouble1, double paramDouble2, double paramDouble3) {
return Beta.incompleteBeta(paramDouble3 / (paramDouble3 + paramDouble2 * paramDouble1), 0.5D * paramDouble3, 0.5D * paramDouble2, Maths.lnB(0.5D * paramDouble3, 0.5D * paramDouble2));
}

public double variance() {
return (this.df2 > 4.0D) ? (2.0D * this.df2 * this.df2 * (this.df1 + this.df2 - 2.0D) / this.df1 * (this.df2 - 2.0D) * (this.df2 - 2.0D) * (this.df2 - 4.0D)) : Double.NaN;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d1 = 2.0D;
double d2 = 4.0D;
FishersF fishersF = new FishersF(d1, d2);

char c = '✐';

fishersF = new FishersF(100.0D, 1000.0D);
double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; ) { arrayOfDouble[b] = fishersF.random(); b++; }

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, fishersF, H1.NOT_EQUAL, false);
System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

