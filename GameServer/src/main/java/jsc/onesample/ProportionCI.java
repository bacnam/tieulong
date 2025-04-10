package jsc.onesample;

import jsc.ci.AbstractConfidenceInterval;
import jsc.distributions.FishersF;

public class ProportionCI
extends AbstractConfidenceInterval
{
public ProportionCI(long paramLong1, long paramLong2, double paramDouble) {
super(paramDouble);
if (paramLong2 < 1L)
throw new IllegalArgumentException("Invalid number of trials."); 
if (paramLong1 < 0L || paramLong1 > paramLong2)
throw new IllegalArgumentException("Invalid number of successes."); 
double d1 = 1.0D - paramDouble;

if (paramLong1 > 0L) {

double d3 = (paramLong2 - paramLong1 + 1L);
FishersF fishersF = new FishersF((paramLong1 + paramLong1), d3 + d3);
double d4 = fishersF.inverseCdf(0.5D * d1);
this.lowerLimit = 1.0D / (1.0D + d3 / paramLong1 * d4);
} else {

this.lowerLimit = 0.0D;
} 
double d2 = (paramLong2 - paramLong1);

if (d2 > 0.0D) {

FishersF fishersF = new FishersF((2L * (paramLong1 + 1L)), d2 + d2);
double d = fishersF.inverseCdf(1.0D - 0.5D * d1);
this.upperLimit = 1.0D / (1.0D + d2 / (paramLong1 + 1L) * d);
} else {

this.upperLimit = 1.0D;
} 
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
ProportionCI proportionCI = new ProportionCI(1L, 1000L, 0.95D);
System.out.println("CI = [" + proportionCI.getLowerLimit() + ", " + proportionCI.getUpperLimit() + "]");
}
}
}

