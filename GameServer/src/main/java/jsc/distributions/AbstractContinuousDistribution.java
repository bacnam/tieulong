package jsc.distributions;

import jsc.numerical.Function;
import jsc.numerical.Integration;
import jsc.numerical.NumericalException;
import jsc.numerical.Roots;
import jsc.util.Maths;

public abstract class AbstractContinuousDistribution
extends AbstractDistribution
{
protected double minX;
protected double maxX;
protected boolean open;
protected double tolerance = 1.0E-6D;

public AbstractContinuousDistribution(double paramDouble1, double paramDouble2, boolean paramBoolean) {
if (paramDouble1 >= paramDouble2)
throw new IllegalArgumentException("Invalid variate range: " + paramDouble1 + " to " + paramDouble2 + "."); 
this.minX = paramDouble1;
this.maxX = paramDouble2;
this.open = paramBoolean;
}

public double cdf(double paramDouble) {
if (paramDouble < this.minX || paramDouble > this.maxX)
throw new IllegalArgumentException("Invalid variate-value."); 
if (paramDouble == this.minX) return 0.0D; 
if (paramDouble == this.maxX) return 1.0D; 
try {
double d = Integration.integrate(new PDF(this), this.minX, paramDouble, this.open, this.tolerance, 20);

if (d > 1.0D) return 1.0D; 
if (d < 0.0D) return 0.0D; 
return d;
} catch (NumericalException numericalException) {
throw new RuntimeException("Cannot calculate cdf to required accuracy. " + numericalException.getMessage());
} 
}

public double getMaxX() {
return this.maxX;
}

public double getMinX() {
return this.minX;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid probability."); 
if (paramDouble == 0.0D) return this.minX; 
if (paramDouble == 1.0D) return this.maxX; 
CdfMinusP cdfMinusP = new CdfMinusP(this, paramDouble);
try {
if (Double.isInfinite(this.minX) || Double.isInfinite(this.maxX)) {

double d1 = mean();
double d2 = sd();
return Roots.secant(cdfMinusP, Math.max(this.minX, d1 - d2), Math.min(this.maxX, d1 + d2), this.tolerance, 1000);
} 

return Roots.bisection(cdfMinusP, this.minX, this.maxX, this.tolerance, 1000);
} catch (NumericalException numericalException) {
throw new RuntimeException("Cannot calculate inverse of cdf. " + numericalException.getMessage());
} 
}

public double mean() {

try { return Integration.integrate(new MeanIntegrand(this), this.minX, this.maxX, this.open, this.tolerance, 20); }
catch (NumericalException numericalException) { throw new RuntimeException("Cannot calculate mean. " + numericalException.getMessage()); }

}

public double moment(int paramInt) {
return moment(paramInt, 0.0D);
}

public double moment(int paramInt, double paramDouble) {
if (paramInt < 0)
throw new IllegalArgumentException("Invalid moment order."); 
if (paramInt == 0) return 1.0D;  
try { return Integration.integrate(new MomentIntegrand(this, paramInt, paramDouble), this.minX, this.maxX, this.open, this.tolerance, 20); }
catch (NumericalException numericalException) { throw new RuntimeException("Cannot calculate moment. " + numericalException.getMessage()); }

}

public abstract double pdf(double paramDouble);

public void setOpen(boolean paramBoolean) {
this.open = paramBoolean;
}

public void setTolerance(double paramDouble) {
this.tolerance = Math.abs(paramDouble);
}

public double variance() {
return moment(2, mean());
}

class CdfMinusP implements Function { double p;
private final AbstractContinuousDistribution this$0;

public CdfMinusP(AbstractContinuousDistribution this$0, double param1Double) { this.this$0 = this$0; this.p = param1Double; } public double function(double param1Double) {
return this.this$0.cdf(param1Double) - this.p;
} }

class MeanIntegrand implements Function {
private final AbstractContinuousDistribution this$0;

MeanIntegrand(AbstractContinuousDistribution this$0) { this.this$0 = this$0; } public double function(double param1Double) {
return param1Double * this.this$0.pdf(param1Double);
} }
class MomentIntegrand implements Function { int r;
double a;
private final AbstractContinuousDistribution this$0;

public MomentIntegrand(AbstractContinuousDistribution this$0, int param1Int, double param1Double) {
this.this$0 = this$0; this.r = param1Int; this.a = param1Double;
}
public double function(double param1Double) {
double d1 = param1Double - this.a;
double d2 = d1;
for (byte b = 1; b < this.r; ) { d2 *= d1; b++; }
return d2 * this.this$0.pdf(param1Double);
} }

class PDF implements Function { private final AbstractContinuousDistribution this$0;

PDF(AbstractContinuousDistribution this$0) { this.this$0 = this$0; } public double function(double param1Double) {
return this.this$0.pdf(param1Double);
} }

static class Test
{
static class TestNormal
extends AbstractContinuousDistribution {
private double mean;
private double sd;

TestNormal(double param2Double1, double param2Double2) {
super(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
this.mean = param2Double1; this.sd = param2Double2;
} public double pdf(double param2Double) {
double d = (param2Double - this.mean) / this.sd;
return Math.exp(-0.5D * d * d) / Normal.SQRPI2 * this.sd;
} }

static class TestBeta extends AbstractContinuousDistribution { private double p;

TestBeta(double param2Double1, double param2Double2) {
super(0.0D, 1.0D, false); this.p = param2Double1; this.q = param2Double2; this.logB = Maths.lnB(param2Double1, param2Double2);
} private double q; private double logB;
public double pdf(double param2Double) {
if ((param2Double > 0.0D && param2Double < 1.0D) || (param2Double == 0.0D && this.p >= 1.0D) || (param2Double == 1.0D && this.q >= 1.0D)) {
if (param2Double == 0.0D) return (this.p == 1.0D) ? this.q : 0.0D; 
if (param2Double == 1.0D) return (this.q == 1.0D) ? this.p : 0.0D; 
return Math.exp((this.p - 1.0D) * Math.log(param2Double) + (this.q - 1.0D) * Math.log(1.0D - param2Double) - this.logB);
} 
throw new IllegalArgumentException("Invalid variate-value.");
} }

public static void main(String[] param1ArrayOfString) {
double d1 = 1.0D;
double d2 = 2.0D;
Normal normal = new Normal(d1, d2);
TestNormal testNormal = new TestNormal(d1, d2);
System.out.println("Normal distribution: mean = " + d1 + " s.d. = " + d2);
System.out.println("Test: mean = " + testNormal.mean() + " s.d. = " + testNormal.sd());
double[] arrayOfDouble = { -10000.0D, -100.0D, -50.0D, -25.0D, -10.0D, -1.0D, -0.5D, -0.1D, 0.0D, 0.1D, 0.5D, 1.0D, 10.0D, 25.0D, 50.0D, 100.0D, 1000.0D, 100000.0D };
for (byte b = 0; b < arrayOfDouble.length; b++) {

System.out.println("Normal: X=" + arrayOfDouble[b] + " cdf=" + normal.cdf(arrayOfDouble[b]) + " X=" + normal.inverseCdf(normal.cdf(arrayOfDouble[b])));

System.out.println("  Test: X=" + arrayOfDouble[b] + " cdf=" + testNormal.cdf(arrayOfDouble[b]) + " X=" + testNormal.inverseCdf(testNormal.cdf(arrayOfDouble[b])));
} 
}
}
}

