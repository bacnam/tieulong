package jsc.curvefitting;

import jsc.datastructures.PairedData;
import jsc.numerical.Function;
import jsc.numerical.NumericalException;
import jsc.numerical.Roots;

public class ExponentialFit
{
private final int MAX_STEPS = 1000;
private final int N_INTERVALS = 20;
private final double INTERVAL = 0.05D; private final int n;
private final double[] x;
private final double[] y;
private final double[] p;
private double a;
private double b;
private double ab;
private double eb;
private int k;
private double h1;
private double h2;
private double h3;
private double h4;
private double h5;
private double h6;
private double h7;
private double h8;
private double b1;
private double b2;
private double F1;
private double F2;
private double F3;
private double F4;
private double h;

public ExponentialFit(PairedData paramPairedData, double[] paramArrayOfdouble, double paramDouble) {
this.n = paramPairedData.getN();
this.x = paramPairedData.getX();
this.y = paramPairedData.getY();

if (paramArrayOfdouble == null) {

this.p = new double[this.n];
for (byte b = 0; b < this.n; ) { this.p[b] = 1.0D; b++; }

} else {

if (this.n != paramArrayOfdouble.length)
throw new IllegalArgumentException("Weights array is wrong length."); 
this.p = paramArrayOfdouble;
} 

if (!abfit(paramDouble, this.ab, this.eb, false))
{

if (!abfit(paramDouble, this.ab, this.eb, true)) {

LineFit lineFit = new LineFit(paramPairedData, this.p);
this.a = lineFit.getA();
if (this.a == 0.0D)
throw new IllegalArgumentException("Unable to fit an exponential curve to these data."); 
double d = lineFit.getB();
this.b = -Math.log((this.a + d) / this.a);
} 
}
}

public ExponentialFit(PairedData paramPairedData, double paramDouble) {
this(paramPairedData, null, paramDouble);
}

private boolean abfit(double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean) {
FbFunction fbFunction = new FbFunction(this);
if (paramBoolean) {

try { this.b = Roots.secant(fbFunction, paramDouble2, paramDouble3, paramDouble1, 1000); }
catch (NumericalException numericalException) { return false; }
catch (IllegalArgumentException illegalArgumentException) { return false; }
return true;
} 

this.h1 = 0.0D; this.h2 = 0.0D; this.h3 = 0.0D; this.h4 = 0.0D; this.h5 = 0.0D;

byte b;
for (b = 0; b < this.n; b++) {

if (this.y[b] <= 0.0D)
{
throw new IllegalArgumentException("Y data not all positive."); } 
this.h8 = Math.log(this.y[b]);
this.h6 = this.p[b] * this.y[b] * this.y[b];
this.h7 = this.h6 * this.x[b];
this.h1 += this.h6;
this.h2 += this.h7 * this.x[b];
this.h3 += this.h7;
this.h4 += this.h7 * this.h8;
this.h5 += this.h6 * this.h8;
} 
this.h8 = 1.0D / (this.h1 * this.h2 - this.h3 * this.h3);
this.b = -this.h8 * (this.h1 * this.h4 - this.h3 * this.h5);

this.b1 = this.b;
this.b2 = this.b;
this.h = 0.0D;
this.F1 = Fb(this.b);
if (this.F1 == 0.0D) return true; 
this.F2 = this.F1;
for (b = 1; b <= 20; b++) {

this.h += 0.05D;
paramDouble2 = this.b1 * (1.0D - this.h);

this.F3 = Fb(paramDouble2);
if (this.F1 * this.F3 < 0.0D) {

paramDouble3 = this.b1;

try { this.b = Roots.secant(fbFunction, paramDouble2, paramDouble3, paramDouble1, 1000); }
catch (NumericalException numericalException) { return false; }
catch (IllegalArgumentException illegalArgumentException) { throw illegalArgumentException; }
return true;
} 
paramDouble3 = this.b2 * (1.0D + this.h);

this.F4 = Fb(paramDouble3);
if (this.F2 * this.F4 < 0.0D) {

paramDouble2 = this.b2;

try { this.b = Roots.secant(fbFunction, paramDouble2, paramDouble3, paramDouble1, 1000); }
catch (NumericalException numericalException) { return false; }
catch (IllegalArgumentException illegalArgumentException) { throw illegalArgumentException; }
return true;
} 
this.b1 = paramDouble2;
this.b2 = paramDouble3;
this.F1 = this.F3;
this.F2 = this.F4;
} 
return false;
}

private double Fb(double paramDouble) {
this.h1 = 0.0D; this.h2 = 0.0D; this.h3 = 0.0D; this.h4 = 0.0D;
for (byte b = 0; b < this.n; b++) {

this.h5 = Math.exp(-paramDouble * this.x[b]);
this.h6 = this.p[b] * this.y[b];
this.h8 = this.h5 * this.h6;
this.h7 = this.p[b] * this.h5 * this.h5;
this.h1 += this.h8;
this.h2 += this.h7;
this.h3 += this.x[b] * this.h8;
this.h4 += this.x[b] * this.h7;
} 

this.a = this.h1 / this.h2;

return this.h3 * this.h2 - this.h1 * this.h4;
}

public double getA() {
return this.a;
}

public double getB() {
return this.b;
}

public int getN() {
return this.n;
}
class FbFunction implements Function {
FbFunction(ExponentialFit this$0) { this.this$0 = this$0; } private final ExponentialFit this$0; public double function(double param1Double) {
return this.this$0.Fb(param1Double);
}
}
}

