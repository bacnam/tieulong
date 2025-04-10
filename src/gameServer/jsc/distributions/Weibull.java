package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;
import jsc.util.Maths;

public class Weibull
extends AbstractDistribution
{
private double LOGC;
private double LOGB;
private double RECIPC;
private double scale;
private double shape;

public Weibull(double paramDouble1, double paramDouble2) {
if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.scale = paramDouble1;
this.shape = paramDouble2;
this.LOGC = Math.log(paramDouble2);
this.LOGB = Math.log(paramDouble1);
this.RECIPC = 1.0D / paramDouble2;
}

public double cdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
return 1.0D - Math.exp(-Math.pow(paramDouble / this.scale, this.shape));
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

return this.scale * Math.pow(Math.log(1.0D / (1.0D - paramDouble)), this.RECIPC);
}
public double mean() {
return Math.exp(this.LOGB + Maths.logGamma(1.0D + this.RECIPC));
}

public double pdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
double d1 = Math.log(paramDouble);
double d2 = this.shape * (d1 - this.LOGB);
return Math.exp(this.LOGC - d1 + d2 - Math.exp(d2));
}

public double random() {
return this.scale * Math.pow(-Math.log(1.0D - this.rand.nextDouble()), this.RECIPC);
} public String toString() {
return new String("Weibull distribution: scale = " + this.scale + ", shape = " + this.shape + ".");
}

public double variance() {
return Math.exp(this.LOGB + this.LOGB + Maths.logGamma(1.0D + this.RECIPC + this.RECIPC)) - Math.exp(2.0D * Maths.logGamma(1.0D + this.RECIPC));
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
char c = '✐';

Weibull weibull = new Weibull(5.0D, 0.5D);
double[] arrayOfDouble = new double[c];
for (byte b = 0; b < c; b++)
{
arrayOfDouble[b] = weibull.random();
}

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, weibull, H1.NOT_EQUAL, true);
System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

