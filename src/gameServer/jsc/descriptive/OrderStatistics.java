package jsc.descriptive;

import java.util.Arrays;

public final class OrderStatistics
implements Cloneable
{
private final int n;
private double maximum;
private double minimum;
private final double median;
private final double lowerQuartile;
private final double upperQuartile;

public OrderStatistics(double[] paramArrayOfdouble) {
this.n = paramArrayOfdouble.length;
if (this.n < 1) {
throw new IllegalArgumentException("No values");
}
this.minimum = paramArrayOfdouble[0];
this.maximum = paramArrayOfdouble[this.n - 1];
if (this.n == 1) {
this.lowerQuartile = paramArrayOfdouble[0]; this.upperQuartile = paramArrayOfdouble[0]; this.median = paramArrayOfdouble[0];
} else if (this.n == 2) {

if (paramArrayOfdouble[0] > paramArrayOfdouble[1]) {

paramArrayOfdouble[0] = this.maximum;
paramArrayOfdouble[1] = this.minimum;
this.minimum = paramArrayOfdouble[0];
this.maximum = paramArrayOfdouble[this.n - 1];
} 
this.lowerQuartile = paramArrayOfdouble[0]; this.upperQuartile = paramArrayOfdouble[1]; this.median = (paramArrayOfdouble[0] + paramArrayOfdouble[1]) / 2.0D;

}
else {

Arrays.sort(paramArrayOfdouble);
this.minimum = paramArrayOfdouble[0];
this.maximum = paramArrayOfdouble[this.n - 1];

int j = (this.n + 1) % 4;
if (j != 0) {

int k = (this.n + 1) / 4 - 1;
this.lowerQuartile = paramArrayOfdouble[k] + j * (paramArrayOfdouble[k + 1] - paramArrayOfdouble[k]) / 4.0D;
} else {

this.lowerQuartile = paramArrayOfdouble[(this.n + 1) / 4 - 1];
} 

j = 3 * (this.n + 1) % 4;
if (j != 0) {

int k = 3 * (this.n + 1) / 4 - 1;
this.upperQuartile = paramArrayOfdouble[k] + j * (paramArrayOfdouble[k + 1] - paramArrayOfdouble[k]) / 4.0D;
} else {

this.upperQuartile = paramArrayOfdouble[3 * (this.n + 1) / 4 - 1];
} 

int i = this.n / 2;
this.median = (this.n % 2 != 0) ? paramArrayOfdouble[j] : (0.5D * (paramArrayOfdouble[i - 1] + paramArrayOfdouble[j]));
} 
}

public Object clone() {
Object object = null; try {
object = super.clone();
} catch (CloneNotSupportedException cloneNotSupportedException) {
System.out.println("OrderStatistics can't clone");
}  return object;
}

public double getLowerQuartile() {
return this.lowerQuartile;
}
public double getMaximum() {
return this.maximum;
}
public double getMedian() {
return this.median;
}
public double getMinimum() {
return this.minimum;
}
public int getN() {
return this.n;
}
public double getUpperQuartile() {
return this.upperQuartile;
}
public double getInterquartileRange() {
return this.upperQuartile - this.lowerQuartile;
}
public double getRange() {
return this.maximum - this.minimum;
}

private static void swap(double paramDouble1, double paramDouble2) {
double d = paramDouble1;
paramDouble1 = paramDouble2; paramDouble2 = d;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble = { 73.0D, 62.7D, 59.3D, 68.2D, 63.9D };

byte b = 2;

OrderStatistics orderStatistics = new OrderStatistics(arrayOfDouble);
System.out.println("N = " + orderStatistics.getN());
System.out.println("Median = " + orderStatistics.getMedian());
System.out.println("Minimum = " + orderStatistics.getMinimum());
System.out.println("Maximum = " + orderStatistics.getMaximum());
System.out.println("Lower quartile = " + orderStatistics.getLowerQuartile());
System.out.println("Upper quartile = " + orderStatistics.getUpperQuartile());
}
}
}

