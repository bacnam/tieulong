package jsc.util;

public class Scale
{
private int n;
private boolean extend;
private double step;
private double valmin;
private double valmax;

public Scale() {
this(0.0D, 1.0D, 2, false, false);
}

public Scale(double paramDouble1, double paramDouble2, int paramInt, boolean paramBoolean) {
this(paramDouble1, paramDouble2, paramInt, paramBoolean, false);
}

public Scale(double paramDouble1, double paramDouble2, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
this.extend = paramBoolean1;
if (paramBoolean1) {
scale(paramInt, paramDouble1, paramDouble2, paramBoolean2);
} else {

if (paramDouble2 <= paramDouble1 || paramInt <= 1)
throw new IllegalArgumentException("Invalid scale values."); 
this.n = paramInt;
this.valmin = paramDouble1; this.valmax = paramDouble2;
this.step = (paramDouble2 - paramDouble1) / (paramInt - 1.0D);
} 
}

public double getFirstTickValue() {
return this.valmin;
}

public double getLastTickValue() {
return this.valmax;
}

public double getLength() {
return this.extend ? (this.valmax - this.valmin + this.step) : (this.valmax - this.valmin);
}

public double getMax() {
return this.extend ? (this.valmax + 0.5D * this.step) : this.valmax;
}

public double getMin() {
return this.extend ? (this.valmin - 0.5D * this.step) : this.valmin;
}

public int getNumberOfTicks() {
return this.n;
}
public double getStep() {
return this.step;
}

public double getTickValue(int paramInt) {
return this.valmin + paramInt * this.step;
}

public void scale(int paramInt, double paramDouble1, double paramDouble2, boolean paramBoolean) {
double[] arrayOfDouble1 = { 1.0D, 1.2D, 1.6D, 2.0D, 2.5D, 3.0D, 4.0D, 5.0D, 6.0D, 8.0D, 10.0D };
int j = arrayOfDouble1.length;
double[] arrayOfDouble2 = { 1.0D, 2.0D, 2.5D, 5.0D, 10.0D };
int k = arrayOfDouble2.length;

if (paramDouble2 < paramDouble1 || paramInt <= 1) {
throw new IllegalArgumentException("Invalid axis values.");
}
this.n = paramInt;

double d2 = (paramInt - 1);
double d4 = Math.abs(paramDouble2);
if (d4 == 0.0D) d4 = 1.0D; 
if ((paramDouble2 - paramDouble1) / d4 <= 1.0E-8D)
{

if (paramDouble2 < 0.0D) {
paramDouble2 = 0.0D;
} else if (paramDouble2 == 0.0D) {
paramDouble2 = 1.0D;
} else {
paramDouble1 = 0.0D;
}  } 
this.step = (paramDouble2 - paramDouble1) / d2;
double d3 = this.step;

for (; d3 < 1.0D; d3 *= 10.0D);
for (; d3 >= 10.0D; d3 /= 10.0D);

d4 = d3 - 1.0E-4D;
if (paramBoolean) {
byte b;
for (b = 0; b < k && d4 > arrayOfDouble2[b]; b++);
this.step *= arrayOfDouble2[b] / d3;
} else {
byte b;

for (b = 0; b < j && d4 > arrayOfDouble1[b]; b++);
this.step *= arrayOfDouble1[b] / d3;
} 

double d1 = this.step * d2;

d4 = 0.5D * (1.0D + (paramDouble1 + paramDouble2 - d1) / this.step);
int i = (int)(d4 - 1.0E-4D);
if (d4 < 0.0D) i--; 
this.valmin = this.step * i;

if (paramDouble1 >= 0.0D && d1 >= paramDouble2) this.valmin = 0.0D; 
this.valmax = this.valmin + d1;

if (paramDouble2 > 0.0D || d1 < -paramDouble1)
return;  this.valmax = 0.0D;
this.valmin = -d1;
}

public String toString() {
return "Scale: " + this.n + " tick marks at " + this.step + " intervals. Min = " + getMin() + ", max = " + getMax() + ". First tick mark at " + this.valmin + ". Last tick mark at " + this.valmax;
}
}

