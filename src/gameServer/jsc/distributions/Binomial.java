package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;
import jsc.util.Maths;

public class Binomial
extends AbstractDiscreteDistribution
{
private long n;
private double p;
private double plog;
private double pclog;
private double oldg;

public Binomial(long paramLong, double paramDouble) {
super(0L, paramLong);
setN(paramLong);
setP(paramDouble);
}

public double cdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > this.n) {
throw new IllegalArgumentException("Invalid variate-value.");
}

double d = this.n - paramDouble;
if (paramDouble == this.n) {
return 1.0D;
}
return 1.0D - Beta.incompleteBeta(this.p, paramDouble + 1.0D, d, Maths.lnB(paramDouble + 1.0D, d));
}

public long getN() {
return this.n;
}
public double getP() {
return this.p;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D) {
throw new IllegalArgumentException("Invalid probability.");
}
double d1 = 0.0D;

if (paramDouble == 1.0D) return this.n; 
if (this.n < 26L) {

for (; d1 < this.n && cdf(d1) < paramDouble - 1.0E-8D; d1++);
return d1;
} 
if (this.p < 0.1D) {

Poisson poisson = new Poisson(this.n * this.p);
d1 = poisson.inverseCdf(paramDouble);
}
else {

Normal normal = new Normal(this.n * this.p, Math.sqrt(this.n * this.p * (1.0D - this.p)));
d1 = Math.floor(normal.inverseCdf(paramDouble) + 0.5D);
} 
if (d1 < 0.0D) d1 = 0.0D; 
if (d1 > this.n) d1 = this.n; 
double d2 = cdf(d1);

while (d2 > paramDouble && d1 > 0.0D) {
d1--; d2 = cdf(d1);
}  while (d2 < paramDouble && d1 < this.n) {
d1++; d2 = cdf(d1);
}  return d1;
}

public double mean() {
return this.n * this.p;
}

public double pdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > this.n) {
return 0.0D;
}
return Math.exp(Maths.logBinomialCoefficient(this.n, (long)paramDouble) + paramDouble * Math.log(this.p) + (this.n - paramDouble) * Math.log(1.0D - this.p));
}

public double random() {
double d3, d1 = this.p;

double d4 = (d1 <= 0.5D) ? d1 : (1.0D - d1);

double d2 = this.n * d4;
if (this.n < 25L)

{ 

d3 = 0.0D;
for (byte b = 1; b <= this.n; b++) {
if (this.rand.nextDouble() < d4) d3++;

}

}

else

{ 

double d5 = 1.0D - d4;

double d6 = Math.sqrt(2.0D * d2 * d5);

while (true)
{ double d8 = Math.PI * this.rand.nextDouble();
double d9 = Math.tan(d8);
double d7 = d6 * d9 + d2;
if (d7 >= 0.0D && d7 < this.n + 1.0D)
{ d7 = Math.floor(d7);
double d = 1.2D * d6 * (1.0D + d9 * d9) * Math.exp(this.oldg - Maths.logGamma(d7 + 1.0D) - Maths.logGamma(this.n - d7 + 1.0D) + d7 * this.plog + (this.n - d7) * this.pclog);

if (this.rand.nextDouble() <= d)
{ d3 = d7; } else { continue; }  }
else { continue; }
if (d4 != d1) d3 = this.n - d3; 
return d3; }  }  if (d4 != d1) d3 = this.n - d3;  return d3;
}

public void setN(long paramLong) {
if (paramLong < 1L)
throw new IllegalArgumentException("Invalid number of trials."); 
this.n = paramLong;
this.maxValue = paramLong;

this.oldg = Maths.logFactorial(paramLong);
}

public void setP(double paramDouble) {
if (paramDouble <= 0.0D || paramDouble >= 1.0D)
throw new IllegalArgumentException("Invalid probability of success."); 
this.p = paramDouble;

double d1 = paramDouble;
double d2 = (d1 <= 0.5D) ? d1 : (1.0D - d1);
double d3 = 1.0D - d2;
this.plog = Math.log(d2);
this.pclog = Math.log(d3);
}
public String toString() {
return new String("Binomial distribution: n = " + this.n + ", p = " + this.p + ".");
} public double variance() {
return this.n * this.p * (1.0D - this.p);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
long l1 = 1000L;
double d1 = 0.99D;
long l2 = 9L;
double d2 = 0.9D;
Binomial binomial = new Binomial(l1, d1);

int i = 1000000;

binomial = new Binomial(25L, 0.038461538461538464D);

int[] arrayOfInt = new int[i];
for (byte b = 0; b < i; ) { arrayOfInt[b] = (int)binomial.random(); b++; }

ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), binomial, 0);
System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
System.out.println("m = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
}
}
}

