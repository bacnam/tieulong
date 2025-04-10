package jsc.distributions;

import jsc.descriptive.DoubleTally;
import jsc.goodnessfit.ChiSquaredFitTest;

public class Discrete
extends AbstractDistribution
{
protected double minValue;
protected double maxValue;
protected int valueCount;
protected double[] values;
protected double[] probs;

public Discrete() {
this.valueCount = 0;
this.values = new double[0];
this.probs = new double[0];
}

public Discrete(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
this(paramArrayOfdouble1, paramArrayOfdouble2, false, 1.0E-6D);
}

public Discrete(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, boolean paramBoolean, double paramDouble) {
setDistribution(paramArrayOfdouble1, paramArrayOfdouble2, paramBoolean, paramDouble);
}

public Discrete(double[] paramArrayOfdouble, double paramDouble) {
setDistribution(paramArrayOfdouble, paramDouble);
}

public Discrete(double[] paramArrayOfdouble) {
setDistribution(paramArrayOfdouble, 0.0D);
}

public Discrete(DoubleTally paramDoubleTally) {
setDistribution(paramDoubleTally);
}

public double cdf(double paramDouble) {
if (paramDouble < this.values[0] || paramDouble > this.values[this.valueCount - 1])
{
throw new IllegalArgumentException("Invalid variate-value."); } 
double d = 0.0D;
for (byte b = 0; b < this.valueCount && 
this.values[b] <= paramDouble; b++) {
d += this.probs[b];
}

if (d < 0.0D) return 0.0D; 
if (d > 1.0D) return 1.0D; 
return d;
}

public double getMaxValue() {
return this.maxValue;
}

public double getMinValue() {
return this.minValue;
}

public double getProb(int paramInt) {
return this.probs[paramInt];
}

public double getValue(int paramInt) {
return this.values[paramInt];
}

public int getValueCount() {
return this.valueCount;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid probability."); 
double d = 0.0D;

if (paramDouble == 1.0D) return this.values[this.valueCount - 1]; 
byte b;
for (b = 0; b < this.valueCount; b++) {

d += this.probs[b];
if (d >= paramDouble)
break; 
}  return this.values[b];
}

public boolean isDiscrete() {
return true;
}

public double mean() {
double d = 0.0D;
for (byte b = 0; b < this.valueCount; b++)
d += this.values[b] * this.probs[b]; 
return d;
}

public double moment(int paramInt) {
return moment(paramInt, 0.0D);
}

public double moment(int paramInt, double paramDouble) {
if (paramInt < 0)
throw new IllegalArgumentException("Invalid moment order."); 
double d = 0.0D;
for (byte b = 0; b < this.valueCount; b++)
d += Math.pow(this.values[b] - paramDouble, paramInt) * this.probs[b]; 
return d;
}

public double pdf(double paramDouble) {
for (byte b = 0; b < this.valueCount; b++) {
if (paramDouble == this.values[b]) return this.probs[b]; 
}  throw new IllegalArgumentException("Invalid variate-value.");
}

public double random() {
double d1 = 0.0D;
double d2 = this.rand.nextDouble();
for (byte b = 0; b < this.valueCount; b++) {

d1 += this.probs[b];
if (d2 < d1) return this.values[b]; 
} 
return this.values[this.valueCount - 1];
}

public void setDistribution(double[] paramArrayOfdouble, double paramDouble) {
setDistribution(new DoubleTally(paramArrayOfdouble, paramDouble));
}

public void setDistribution(DoubleTally paramDoubleTally) {
this.valueCount = paramDoubleTally.getValueCount();
this.values = new double[this.valueCount];
this.probs = new double[this.valueCount];
for (byte b = 0; b < this.valueCount; b++) {

this.values[b] = paramDoubleTally.getValue(b);
this.probs[b] = paramDoubleTally.getProportion(b);
} 
this.minValue = paramDoubleTally.getMin();
this.maxValue = paramDoubleTally.getMax();
}

public void setDistribution(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, boolean paramBoolean, double paramDouble) {
this.valueCount = paramArrayOfdouble1.length;
if (this.valueCount < 1 || this.valueCount != paramArrayOfdouble2.length)
throw new IllegalArgumentException("Invalid distribution parameter."); 
this.values = new double[this.valueCount];
this.probs = new double[this.valueCount];
System.arraycopy(paramArrayOfdouble1, 0, this.values, 0, this.valueCount);
System.arraycopy(paramArrayOfdouble2, 0, this.probs, 0, this.valueCount);

double d1 = Double.NEGATIVE_INFINITY;
double d2 = 0.0D;
byte b;
for (b = 0; b < this.valueCount; b++) {

if (this.values[b] > d1) {
d1 = this.values[b];
} else {
throw new IllegalArgumentException("Invalid values value: " + this.values[b]);
}  if (this.probs[b] < 0.0D || this.probs[b] > 1.0D)
throw new IllegalArgumentException("Invalid probability: " + this.probs[b]); 
d2 += this.probs[b];
} 
if ((!paramBoolean && Math.abs(1.0D - d2) > paramDouble) || d2 <= 0.0D) {
throw new IllegalArgumentException("Probabilities sum to " + d2);
}
if (paramBoolean && Math.abs(1.0D - d2) > paramDouble)
for (b = 0; b < this.valueCount; ) { this.probs[b] = this.probs[b] / d2; b++; }

this.minValue = this.values[0];
this.maxValue = this.values[this.valueCount - 1];
}

public String toString() {
StringBuffer stringBuffer = new StringBuffer();
stringBuffer.append("Discrete distribution\nx\tp");
for (byte b = 0; b < this.valueCount; b++)
stringBuffer.append("\n" + this.values[b] + "\t" + this.probs[b]); 
return stringBuffer.toString();
}

public double upperTailProb(double paramDouble) {
if (paramDouble < this.values[0] || paramDouble > this.values[this.valueCount - 1])
throw new IllegalArgumentException("Invalid variate-value."); 
double d = 0.0D;
for (int i = this.valueCount - 1; i >= 0 && 
this.values[i] >= paramDouble; i--) {
d += this.probs[i];
}

return d;
}

public double variance() {
return moment(2, mean());
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double d1 = 10.0D;
double d2 = 0.5D;
double[] arrayOfDouble1 = { 0.1D, 0.2D, 0.2D, 0.3D, 0.1D, 0.1D };
double[] arrayOfDouble2 = { -2.0D, -1.0D, 0.0D, 2.0D, 5.0D, 10.0D };
Discrete discrete = new Discrete(arrayOfDouble2, arrayOfDouble1);
System.out.println(discrete.toString());

char c = '✐';
double[] arrayOfDouble3 = new double[c];
for (byte b = 0; b < c; ) { arrayOfDouble3[b] = discrete.random(); b++; }
ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new DoubleTally(arrayOfDouble3), discrete, 0);
System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
}
}
}

