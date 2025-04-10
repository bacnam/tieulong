package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class Cauchy
extends AbstractDistribution
{
private double location;
private double scale;

public Cauchy(double paramDouble1, double paramDouble2) {
if (paramDouble2 <= 0.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.location = paramDouble1;
this.scale = paramDouble2;
}

public Cauchy() {
this(0.0D, 1.0D);
} public double cdf(double paramDouble) {
return 0.5D + Math.atan((paramDouble - this.location) / this.scale) / Math.PI;
}

public double getLocation() {
return this.location;
}

public double getScale() {
return this.scale;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid probability."); 
if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
return this.location + this.scale * Math.tan(Math.PI * (paramDouble - 0.5D));
}

public double mean() {
return Double.NaN;
}

public double pdf(double paramDouble) {
double d = (paramDouble - this.location) / this.scale;
return 1.0D / Math.PI * this.scale * (1.0D + d * d);
}

public double random() {
while (true) {
double d = this.rand.nextGaussian(); if (d != 0.0D)
return this.location + this.scale * this.rand.nextGaussian() / d; 
} 
}
public String toString() {
return new String("Cauchy distribution: location = " + this.location + ", scale = " + this.scale + ".");
}

public double variance() {
return Double.NaN;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d1 = 2.0D;
double d2 = 3.0D;
Cauchy cauchy = new Cauchy(d1, d2);

int i = 100000;
cauchy = new Cauchy(2.0D, 3.0D);
double[] arrayOfDouble = new double[i];
for (byte b = 0; b < i; ) { arrayOfDouble[b] = cauchy.random(); b++; }

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, cauchy, H1.NOT_EQUAL, false);
System.out.println("n = " + i + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

