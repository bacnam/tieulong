package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;

public class DiscreteUniform
extends AbstractDiscreteDistribution
{
private long a;
private long b;
private double n;

public DiscreteUniform(long paramLong1, long paramLong2) {
super(paramLong1, paramLong2); setInterval(paramLong1, paramLong2);
}

public double cdf(double paramDouble) {
if (paramDouble < this.a || paramDouble > this.b)
throw new IllegalArgumentException("Invalid variate-value."); 
return (1.0D + paramDouble - this.a) / this.n;
}

public long getA() {
return this.a;
}

public long getB() {
return this.b;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D) {
throw new IllegalArgumentException("Invalid probability.");
}

if (paramDouble == 1.0D) return this.b; 
long l = this.a;
double d = 1.0D / this.n;
while (d < paramDouble && l < this.b) {
l++; d = (1.0D + l - this.a) / this.n;
}  return l;
}

public double mean() {
return 0.5D * (this.a + this.b);
}

public double pdf(double paramDouble) {
if (paramDouble < this.a || paramDouble > this.b)
throw new IllegalArgumentException("Invalid variate-value."); 
return 1.0D / this.n;
}
public double random() {
return Math.floor(this.a + this.rand.nextDouble() * this.n);
}

public void setInterval(long paramLong1, long paramLong2) {
if (paramLong2 <= paramLong1)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.a = paramLong1;
this.b = paramLong2;
this.minValue = paramLong1;
this.maxValue = paramLong2;
this.n = (paramLong2 - paramLong1 + 1L);
}

public String toString() {
return new String("Discrete uniform distribution: a = " + this.a + ", b = " + this.b + ".");
}

public double variance() {
double d1 = mean();
double d2 = 0.0D;

for (long l = this.a; l <= this.b; l++) {

double d = l - d1;
d2 += d * d;
} 
return d2 / this.n;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
long l1 = 1L;
long l2 = 10L;
double d = 0.5D;
DiscreteUniform discreteUniform = new DiscreteUniform(l1, l2);

char c = '✐';
int[] arrayOfInt = new int[c];
discreteUniform = new DiscreteUniform(10L, 20L);
for (byte b = 0; b < c; ) { arrayOfInt[b] = (int)discreteUniform.random(); b++; }
ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), discreteUniform, 0);
System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
}
}
}

