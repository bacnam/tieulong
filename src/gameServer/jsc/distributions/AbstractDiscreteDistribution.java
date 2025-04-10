package jsc.distributions;

public abstract class AbstractDiscreteDistribution
extends AbstractDistribution
{
protected long minValue;
protected long maxValue;

public AbstractDiscreteDistribution(long paramLong1, long paramLong2) {
if (paramLong1 >= paramLong2)
throw new IllegalArgumentException("Invalid variate range: " + paramLong1 + " to " + paramLong2 + "."); 
this.minValue = paramLong1;
this.maxValue = paramLong2;
}

public double cdf(double paramDouble) {
if (paramDouble < this.minValue || paramDouble > this.maxValue)
throw new IllegalArgumentException("Invalid variate-value."); 
double d = 0.0D;
long l = this.minValue;
while (l <= paramDouble) {

d += pdf(l);
l++;
} 

return d;
}

public double getMaximumPdf() {
long l1 = this.minValue;
double d = 0.0D;
for (long l2 = 0L; l2 < this.maxValue - this.minValue + 1L; l2++) {

double d1 = pdf(l1);
if (d1 > d) d = d1;

l1++;
} 
return d;
}

public long getMaxValue() {
return this.maxValue;
}

public long getMinValue() {
return this.minValue;
}

public double moment(int paramInt) {
return moment(paramInt, 0.0D);
}

public double moment(int paramInt, double paramDouble) {
if (paramInt < 0)
throw new IllegalArgumentException("Invalid moment order."); 
if (paramInt == 0) return 1.0D; 
long l1 = this.minValue;
double d = 0.0D;
for (long l2 = 0L; l2 < this.maxValue - this.minValue + 1L; l2++) {

d += Math.pow(l1 - paramDouble, paramInt) * pdf(l1);

l1++;
} 
return d;
}

public double inverseCdf(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D) {
throw new IllegalArgumentException("Invalid probability " + paramDouble);
}

double d1 = cdf(this.minValue);
if (d1 >= paramDouble) return this.minValue;

double d2 = cdf(this.maxValue);
if (d2 < paramDouble) return this.maxValue;

long l1 = this.minValue;
long l2 = this.maxValue;

while (true) {
long l4 = l2 - l1;
if (l4 <= 1L) {

if (cdf(l1) >= paramDouble) {
return l1;
}
return l2;
} 
l4 /= 2L;
long l3 = l1 + l4;
double d = cdf(l3);
if (d < paramDouble) {
l1 = l3; continue;
} 
l2 = l3;
} 
}

public boolean isDiscrete() {
return true;
}

public double mean() {
long l1 = this.minValue;
double d = 0.0D;
for (long l2 = 0L; l2 < this.maxValue - this.minValue + 1L; l2++) {

d += l1 * pdf(l1);
l1++;
} 
return d;
}

public abstract double pdf(double paramDouble);

public double variance() {
return moment(2, mean());
}
}

