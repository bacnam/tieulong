package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;

public class PowerFunction
extends AbstractDistribution
{
private double scale;
private double shape;
private double BC;
private double RECIPC;

public PowerFunction(double paramDouble1, double paramDouble2) {
if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.scale = paramDouble1;
this.shape = paramDouble2;
this.RECIPC = 1.0D / paramDouble2;
this.BC = Math.pow(paramDouble1, paramDouble2);
}

public double cdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > this.scale)
throw new IllegalArgumentException("Invalid variate-value."); 
return Math.pow(paramDouble / this.scale, this.shape);
}

public double getScale() {
return this.scale;
}

public double getShape() {
return this.shape;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid probability."); 
if (paramDouble == 0.0D) return 0.0D; 
if (paramDouble == 1.0D) return this.scale; 
return this.scale * Math.pow(paramDouble, this.RECIPC);
}
public double mean() {
return this.scale * this.shape / (this.shape + 1.0D);
}

public double pdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > this.scale)
throw new IllegalArgumentException("Invalid variate-value."); 
return this.shape * Math.pow(paramDouble, this.shape - 1.0D) / this.BC;
}
public double random() {
return this.scale * Math.pow(this.rand.nextDouble(), this.RECIPC);
}
public String toString() {
return new String("Power function distribution: scale = " + this.scale + ", shape = " + this.shape + ".");
} public double variance() {
return this.scale * this.scale * this.shape / (this.shape + 2.0D) * (this.shape + 1.0D) * (this.shape + 1.0D);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
char c = '✐';

PowerFunction powerFunction = new PowerFunction(2.0D, 0.5D);
double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; b++)
{
arrayOfDouble[b] = powerFunction.random();
}

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, powerFunction, H1.NOT_EQUAL, true);
System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

