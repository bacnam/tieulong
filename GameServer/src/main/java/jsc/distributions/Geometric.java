package jsc.distributions;

import jsc.descriptive.Tally;
import jsc.goodnessfit.ChiSquaredFitTest;

public class Geometric
extends AbstractDistribution
{
private double p;
private double LOGQ;
private double LOGP;

public Geometric(double paramDouble) {
setP(paramDouble);
}

public double cdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
return 1.0D - Math.pow(1.0D - this.p, paramDouble + 1.0D);
}

public double getP() {
return this.p;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble >= 1.0D)
throw new IllegalArgumentException("Invalid probability."); 
return Math.round(Math.log(1.0D - paramDouble) / this.LOGQ - 1.0D);
}

public boolean isDiscrete() {
return true;
} public double mean() {
return this.p / (1.0D - this.p);
}

public double pdf(double paramDouble) {
if (paramDouble < 0.0D)
throw new IllegalArgumentException("Invalid variate-value."); 
return this.p * Math.pow(1.0D - this.p, paramDouble);
}

public double random() {
return Math.max(0.0D, Math.ceil(Math.log(1.0D - this.rand.nextDouble()) / this.LOGQ - 1.0D));
}

public void setP(double paramDouble) {
if (paramDouble <= 0.0D || paramDouble >= 1.0D)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.p = paramDouble;
this.LOGQ = Math.log(1.0D - paramDouble);
this.LOGP = Math.log(paramDouble);
}
public String toString() {
return new String("Geometric distribution: p = " + this.p + ".");
} public double variance() {
return this.p / (1.0D - this.p) * (1.0D - this.p);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d = 0.25D;
Geometric geometric = new Geometric(d);

char c = '✐';
geometric = new Geometric(0.25D);

int[] arrayOfInt = new int[c];
for (byte b = 0; b < c; ) { arrayOfInt[b] = (int)geometric.random(); b++; }

ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), geometric, 0);
System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
}
}
}

