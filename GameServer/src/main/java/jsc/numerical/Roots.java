package jsc.numerical;

public class Roots
{
public static double bisection(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) throws NumericalException {
double d1, d4, d2 = paramFunction.function(paramDouble1);
double d3 = paramFunction.function(paramDouble2);
if (d2 * d3 >= 0.0D)
throw new IllegalArgumentException("Root not bracketed in bisection method"); 
if (d2 < 0.0D) {
d1 = paramDouble2 - paramDouble1; d4 = paramDouble1;
} else {
d1 = paramDouble1 - paramDouble2; d4 = paramDouble2;
} 
for (byte b = 1; b <= paramInt; b++) {
double d;
d3 = paramFunction.function(d = d4 + (d1 *= 0.5D));
if (d3 <= 0.0D) d4 = d; 
if (Math.abs(d1) < paramDouble3 || d3 == 0.0D) return d4;

} 
throw new NumericalException("Maximum number of iterations exceeded in bisection method");
}

public static double secant(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) throws NumericalException {
double d3, d4;
if (paramDouble1 == paramDouble2) {
throw new IllegalArgumentException("Equal starting values.");
}

double d1 = paramFunction.function(paramDouble1);
double d2 = paramFunction.function(paramDouble2);

if (Math.abs(d1) < Math.abs(d2)) {

d3 = paramDouble1;
d4 = paramDouble2;
double d = d1;
d1 = d2;
d2 = d;
} else {

d4 = paramDouble1; d3 = paramDouble2;
} 
for (byte b = 1; b <= paramInt; b++) {

double d6 = d2 - d1;
if (d6 == 0.0D)
throw new NumericalException("Identical function values " + d2 + " in secant method."); 
double d5 = (d4 - d3) * d2 / d6;
d4 = d3;
d1 = d2;
d3 += d5;
d2 = paramFunction.function(d3);
if (Math.abs(d5) < paramDouble3 || d2 == 0.0D) {
return d3;
}
} 
throw new NumericalException("Maximum number of iterations exceeded in secant method.");
}

static class Test
{
public static void main(String[] param1ArrayOfString) throws NumericalException {
Func1 func1 = new Func1();
System.out.println("secant root =" + Roots.secant(func1, 2.0D, 1.0D, 1.0E-7D, 1000));
System.out.println("bisection root =" + Roots.bisection(func1, 0.0D, 10.0D, 1.0E-7D, 1000));

Func2 func2 = new Func2();
System.out.println("secant root =" + Roots.secant(func2, 2.0D, 1.0D, 1.0E-7D, 1000));

Func3 func3 = new Func3();
System.out.println("secant root =" + Roots.secant(func3, 2.0D, 1.0D, 1.0E-7D, 1000));
System.out.println("bisection root =" + Roots.bisection(func3, 0.0D, 10.0D, 1.0E-7D, 1000));
}

static class Func1
implements Function
{
public double function(double param2Double) {
return param2Double * param2Double * param2Double * param2Double - param2Double - 10.0D;
} }

static class Func2 implements Function {
public double function(double param2Double) {
return Math.exp(-param2Double) - Math.sin(param2Double);
}
}

static class Func3 implements Function { public double function(double param2Double) {
return param2Double * param2Double * param2Double - 2.0D * param2Double - 5.0D;
} }

static class Func0 implements Function {
public double function(double param2Double) {
return param2Double * param2Double + 1.0D;
}
}
}
}

