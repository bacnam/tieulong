package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;
import jsc.util.Maths;

public class Poisson
extends AbstractDistribution
{
private double mean;
private double alxm;
private double gg;
private double sq;

public Poisson(double paramDouble) {
setMean(paramDouble);
}

public double cdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
return 1.0D - Gamma.incompleteGamma(this.mean, 1.0D + paramDouble);
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D) {
throw new IllegalArgumentException("Invalid probability.");
}

double d = 0.0D;

if (this.mean < 20.0D) {

for (; cdf(d) < paramDouble - 1.0E-8D; d++);

}
else {

Normal normal = new Normal(this.mean, Math.sqrt(this.mean));
d = Math.floor(normal.inverseCdf(paramDouble) + 0.5D);
if (d < 0.0D) d = 0.0D; 
double d1 = cdf(d);

while (d1 > paramDouble && d > 0.0D) {
d--; d1 = cdf(d);
}  while (d1 < paramDouble) {
d++; d1 = cdf(d);
} 
}  return d;
}

public boolean isDiscrete() {
return true;
} public double mean() {
return this.mean;
}

public double pdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
return Math.exp(paramDouble * Math.log(this.mean) - this.mean - Maths.logFactorial((long)paramDouble));
}

public double random() {
double d;
if (this.mean < 12.0D) {

d = -1.0D;
double d1 = 1.0D;
do {
d++;
d1 *= this.rand.nextDouble();
} while (d1 > this.gg);
}
else {

while (true)

{ double d1 = Math.tan(Math.PI * this.rand.nextDouble());
d = this.sq * d1 + this.mean;
if (d >= 0.0D)
{ d = Math.floor(d);
double d2 = 0.9D * (1.0D + d1 * d1) * Math.exp(d * this.alxm - Maths.logGamma(d + 1.0D) - this.gg);
if (this.rand.nextDouble() <= d2)
break;  }  } 
}  return d;
}

public void setMean(double paramDouble) {
if (paramDouble <= 0.0D)
throw new IllegalArgumentException("Invalid Poisson parameter."); 
this.mean = paramDouble;

if (paramDouble < 12.0D) {
this.gg = Math.exp(-paramDouble);
} else {

this.sq = Math.sqrt(2.0D * paramDouble);
this.alxm = Math.log(paramDouble);
this.gg = paramDouble * this.alxm - Maths.logGamma(paramDouble + 1.0D);
} 
}
public String toString() {
return new String("Poisson distribution: mean = " + this.mean + ".");
} public double variance() {
return this.mean;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
int i = 1000000;

Poisson poisson = new Poisson(15.0D);
int[] arrayOfInt = new int[i];
for (byte b = 0; b < i; ) { arrayOfInt[b] = (int)poisson.random(); b++; }

ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), poisson, 0);
System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
System.out.println("m = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
}
}
}

