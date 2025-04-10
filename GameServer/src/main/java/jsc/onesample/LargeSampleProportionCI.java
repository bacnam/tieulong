package jsc.onesample;

import jsc.ci.AbstractConfidenceInterval;
import jsc.distributions.Normal;

public class LargeSampleProportionCI
extends AbstractConfidenceInterval
{
private final double pHat;

public LargeSampleProportionCI(long paramLong1, long paramLong2, double paramDouble) {
super(paramDouble);
if (paramLong2 < 1L)
throw new IllegalArgumentException("Invalid number of trials."); 
if (paramLong1 < 0L || paramLong1 > paramLong2) {
throw new IllegalArgumentException("Invalid number of successes.");
}
Normal normal = new Normal();
double d1 = 1.0D - paramDouble;
double d2 = normal.inverseCdf(1.0D - 0.5D * d1);

this.pHat = paramLong1 / paramLong2;
double d3 = d2 * Math.sqrt(this.pHat * (1.0D - this.pHat) / paramLong2);
this.lowerLimit = this.pHat - d3;
this.upperLimit = this.pHat + d3;
}

public double getP() {
return this.pHat;
}
}

