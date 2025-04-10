package jsc.goodnessfit;

import jsc.ci.ConfidenceBand;
import jsc.util.Maths;

public class KolmogorovCB
implements ConfidenceBand
{
static final double TOLERANCE = 5.0E-15D;
private int n;
private SampleDistributionFunction sdf;
private boolean approx;
private double confidenceCoeff;
private double criticalValue;

public KolmogorovCB(SampleDistributionFunction paramSampleDistributionFunction, double paramDouble, boolean paramBoolean) {
this.approx = paramBoolean;
this.sdf = paramSampleDistributionFunction;
this.n = paramSampleDistributionFunction.getN();
setConfidenceCoeff(paramDouble);
}

public KolmogorovCB(double[] paramArrayOfdouble, double paramDouble, boolean paramBoolean) {
this(new SampleDistributionFunction(paramArrayOfdouble), paramDouble, paramBoolean);
}

public KolmogorovCB(double[] paramArrayOfdouble) {
this(paramArrayOfdouble, 0.95D, (paramArrayOfdouble.length <= 100));
}

public static double approxCriticalValue(int paramInt, double paramDouble) {
if (paramInt < 1) throw new IllegalArgumentException("n < 1."); 
if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid alpha."); 
if (paramDouble == 0.0D) return 1.0D; 
if (paramDouble == 1.0D) return 0.0D; 
if (paramInt == 1) return 1.0D - paramDouble; 
double d1 = Math.sqrt(Math.log(1.0D / paramDouble) / (2 * paramInt));
double d4 = Maths.log10(paramDouble);
double d3 = 0.09037D * Math.pow(-d4, 1.5D) + 0.01515D * d4 * d4 - 0.11143D;
double d2 = d1 - 0.16693D / paramInt - d3 / Math.pow(paramInt, 1.5D);
if (d2 < 0.0D) return 0.0D; 
if (d2 > 1.0D) return 1.0D; 
return d2;
}

public static double exactCriticalValue(int paramInt, double paramDouble) {
double d4, d2 = paramInt;

if (paramInt < 1) throw new IllegalArgumentException("n < 1."); 
if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid alpha."); 
if (paramDouble == 0.0D) return 1.0D; 
if (paramDouble == 0.0D) return 0.0D; 
if (paramInt == 1) return 1.0D - paramDouble; 
double d1 = Maths.logFactorial(paramInt);

double d3 = approxCriticalValue(paramInt, paramDouble);

if (d3 == 1.0D) { d3 = 0.5D; }
else if (d3 == 0.0D) { d3 = (paramDouble > 0.99D) ? (1.0D - paramDouble) : 0.5D; }
do {
double d5 = 0.0D, d6 = 0.0D;
for (byte b = 0; b < paramInt * (1.0D - d3) && b < paramInt; b++) {

double d10 = b / d2;
double d9 = Math.exp(d1 - Maths.logFactorial(b) - Maths.logFactorial((paramInt - b)) + (paramInt - b) * Math.log(1.0D - d3 - d10) + (b - 1) * Math.log(d3 + d10));

d5 += d9;
d6 += (1.0D / d3 + (b - 1.0D) / (d3 + d10) - (paramInt - b) / (1.0D - d3 - d10)) * d9;
} 

double d7 = paramDouble - d3 * d5;
double d8 = -d3 * d6;
if (d8 == 0.0D) {
throw new ArithmeticException("Zero derivative.");
}
d4 = d3;
d3 -= d7 / d8;
if (d3 < 0.0D || d3 > 1.0D) return d4;

} while (Math.abs(d3 - d4) >= 5.0E-15D);

if (d3 < 0.0D) return 0.0D; 
if (d3 > 1.0D) return 1.0D; 
return d3;
}
public double getConfidenceCoeff() {
return this.confidenceCoeff;
}

public void setConfidenceCoeff(double paramDouble) {
if (paramDouble <= 0.0D || paramDouble >= 1.0D)
throw new IllegalArgumentException("Invalid confidence coefficient."); 
this.confidenceCoeff = paramDouble;
double d = 0.5D - 0.5D * paramDouble;
if (this.approx) {
this.criticalValue = approxCriticalValue(this.n, d);
} else {
this.criticalValue = exactCriticalValue(this.n, d);
} 
}

public double getCriticalValue() {
return this.criticalValue;
}

public double getLowerLimit(int paramInt) {
return this.sdf.getOrderedS(paramInt) - this.criticalValue;
}

public double getUpperLimit(int paramInt) {
return this.sdf.getOrderedS(paramInt) + this.criticalValue;
}
public int getN() {
return this.n;
}

public SampleDistributionFunction getSdf() {
return this.sdf;
} public double getX(int paramInt) {
return this.sdf.getOrderedX(paramInt);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble = { 13.3D, 14.6D, 13.6D, 17.2D, 14.1D, 10.6D, 15.9D, 14.7D, 14.2D, 14.0D, 17.4D, 15.6D, 8.2D, 13.8D, 15.4D, 16.3D, 17.7D, 15.0D, 13.4D, 13.4D, 16.0D, 13.3D, 14.9D, 12.9D, 14.0D, 16.2D, 11.5D, 10.4D, 12.6D, 18.1D };

KolmogorovCB kolmogorovCB = new KolmogorovCB(arrayOfDouble);

for (byte b = 0; b < kolmogorovCB.getN(); b++)
System.out.println("x = " + kolmogorovCB.getX(b) + " [" + kolmogorovCB.getLowerLimit(b) + ", " + kolmogorovCB.getUpperLimit(b) + "]"); 
System.out.println("n = " + kolmogorovCB.getN() + " D = " + kolmogorovCB.getCriticalValue());
}
}
}

