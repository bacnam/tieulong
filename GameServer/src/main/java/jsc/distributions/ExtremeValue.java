package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class ExtremeValue
extends AbstractDistribution
{
static final double C1 = 1.6449340668482264D;
static final double C2 = Math.sqrt(1.6449340668482264D);

private double location;

private double scale;

public ExtremeValue(double paramDouble1, double paramDouble2) {
if (paramDouble2 <= 0.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.location = paramDouble1;
this.scale = paramDouble2;
}

public double cdf(double paramDouble) {
return Math.exp(-Math.exp(-(paramDouble - this.location) / this.scale));
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
return this.location - this.scale * Math.log(Math.log(1.0D / paramDouble));
}
public double mean() {
return this.location + this.scale * 0.577215664901533D;
}

public double pdf(double paramDouble) {
double d = -(paramDouble - this.location) / this.scale;
return Math.exp(d) * Math.exp(-Math.exp(d)) / this.scale;
}

public double random() {
return this.location - this.scale * Math.log(-Math.log(1.0D - this.rand.nextDouble()));
} public double sd() {
return this.scale * C2;
}
public String toString() {
return new String("Extreme value distribution: location = " + this.location + ", scale = " + this.scale + ".");
} public double variance() {
return this.scale * this.scale * 1.6449340668482264D;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d1 = 5.0D, d2 = 1.5D;
ExtremeValue extremeValue = new ExtremeValue(d1, d2);

char c = '✐';
extremeValue = new ExtremeValue(5.0D, 1.5D);
double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; ) { arrayOfDouble[b] = extremeValue.random(); b++; }

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, extremeValue, H1.NOT_EQUAL, false);
System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

