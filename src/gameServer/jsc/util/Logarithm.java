package jsc.util;

public class Logarithm
{
double base;
double logBase;

public Logarithm(double paramDouble) {
this.base = paramDouble;
this.logBase = Math.log(paramDouble);
}

public double antilog(double paramDouble) {
return Math.pow(this.base, paramDouble);
}

public double log(double paramDouble) {
return Math.log(paramDouble) / this.logBase;
}
}

