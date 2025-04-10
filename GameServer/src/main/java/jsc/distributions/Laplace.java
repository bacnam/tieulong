package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class Laplace
extends AbstractDistribution
{
private double mean;
private double scale;

public Laplace() {
this(0.0D, 1.0D);
}

public Laplace(double paramDouble1, double paramDouble2) {
if (paramDouble2 <= 0.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.mean = paramDouble1;
this.scale = paramDouble2;
}

public double cdf(double paramDouble) {
if (paramDouble >= this.mean) {
return 0.5D + 0.5D * (1.0D - Math.exp((this.mean - paramDouble) / this.scale));
}
return 0.5D - 0.5D * (1.0D - Math.exp((paramDouble - this.mean) / this.scale));
}

public double getScale() {
return this.scale;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid probability."); 
if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
if (paramDouble > 0.5D) {
return this.mean - this.scale * Math.log(2.0D - paramDouble - paramDouble);
}
return this.mean + this.scale * Math.log(paramDouble + paramDouble);
}
public double mean() {
return this.mean;
} public double pdf(double paramDouble) {
return 0.5D * Math.exp(-Math.abs(paramDouble - this.mean) / this.scale) / this.scale;
}
public double random() {
return this.mean - this.scale * Math.log((1.0D - this.rand.nextDouble()) / (1.0D - this.rand.nextDouble()));
}
public String toString() {
return new String("Laplace distribution: mean = " + this.mean + ", scale = " + this.scale + ".");
} public double variance() {
return 2.0D * this.scale * this.scale;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d1 = 5.0D;
double d2 = 10.0D;
Laplace laplace = new Laplace(d1, d2);

char c = '✐';
laplace = new Laplace(5.0D, 10.0D);
double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; ) { arrayOfDouble[b] = laplace.random(); b++; }

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, laplace, H1.NOT_EQUAL, false);
System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

