package jsc.curvefitting;

public class PolynomialFunctionVector
implements FunctionVector
{
private final int degree;
private final double[] f;

PolynomialFunctionVector(int paramInt) {
if (paramInt < 1)
throw new IllegalArgumentException("Order must be > 0."); 
this.degree = paramInt;
this.f = new double[1 + paramInt];
this.f[0] = 1.0D;
}

public double[] function(double paramDouble) {
this.f[1] = paramDouble;
double d = paramDouble;
for (byte b = 2; b <= this.degree; ) { d *= paramDouble; this.f[b] = d; b++; }
return this.f;
}
}

