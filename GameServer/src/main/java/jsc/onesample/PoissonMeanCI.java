package jsc.onesample;

import jsc.ci.AbstractConfidenceInterval;
import jsc.distributions.ChiSquared;

public class PoissonMeanCI
extends AbstractConfidenceInterval
{
public PoissonMeanCI(double paramDouble1, double paramDouble2) {
super(paramDouble2);

double d1 = 1.0D - paramDouble2;
double d2 = 0.5D * d1;
if (paramDouble1 > 0.0D) {

ChiSquared chiSquared1 = new ChiSquared(paramDouble1 + paramDouble1);
this.lowerLimit = 0.5D * chiSquared1.inverseCdf(d2);
} else {

this.lowerLimit = 0.0D;
}  ChiSquared chiSquared = new ChiSquared(paramDouble1 + paramDouble1 + 2.0D);
this.upperLimit = 0.5D * chiSquared.inverseCdf(1.0D - d2);
}

public PoissonMeanCI(double paramDouble1, int paramInt, double paramDouble2) {
this(paramDouble2, paramInt * paramDouble1);
if (paramInt < 1)
throw new IllegalArgumentException("Invalid number of observations."); 
this.lowerLimit /= paramInt;
this.upperLimit /= paramInt;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
PoissonMeanCI poissonMeanCI1 = new PoissonMeanCI(3.0D, 0.95D);
System.out.println("CI = [" + poissonMeanCI1.getLowerLimit() + ", " + poissonMeanCI1.getUpperLimit() + "]");

PoissonMeanCI poissonMeanCI2 = new PoissonMeanCI(3.0D, 7, 0.95D);
System.out.println("CI = [" + poissonMeanCI2.getLowerLimit() + ", " + poissonMeanCI2.getUpperLimit() + "]");
}
}
}

