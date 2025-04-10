package jsc.goodnessfit;

import jsc.distributions.Distribution;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;

public abstract class KolmogorovTypeTest
implements SignificanceTest
{
protected int n;
protected Distribution F;
protected double D;
protected double positiveD;
protected double negativeD;
protected int indexOfD;
protected int indexOfPositiveD;
protected int indexOfNegativeD;
protected double testStatistic;
protected SampleDistributionFunction sdf;

public KolmogorovTypeTest(double[] paramArrayOfdouble, Distribution paramDistribution, H1 paramH1) {
double d = 0.0D;

this.sdf = new SampleDistributionFunction(paramArrayOfdouble);
this.n = this.sdf.getN();
this.F = paramDistribution;

this.indexOfD = 0;
this.indexOfPositiveD = 0;
this.indexOfNegativeD = 0;

double[] arrayOfDouble = new double[this.n];
for (byte b = 0; b < this.n; b++) {

arrayOfDouble[b] = paramDistribution.cdf(this.sdf.getOrderedX(b));
if (arrayOfDouble[b] < 0.0D || arrayOfDouble[b] > 1.0D)
throw new IllegalArgumentException("Invalid distribution cdf."); 
double d3 = this.sdf.getOrderedS(b);

double d1 = paramDistribution.isDiscrete() ? 0.0D : (d - arrayOfDouble[b]);

double d2 = Math.max(Math.abs(d1), Math.abs(d3 - arrayOfDouble[b]));
if (d2 > this.D) { this.D = d2; this.indexOfD = b; }
d2 = Math.max(d1, d3 - arrayOfDouble[b]);
if (d2 > this.positiveD) { this.positiveD = d2; this.indexOfPositiveD = b; }
d2 = Math.max(-d1, arrayOfDouble[b] - d3);
if (d2 > this.negativeD) { this.negativeD = d2; this.indexOfNegativeD = b; }

d = d3;
} 

if (paramH1 == H1.NOT_EQUAL) { this.testStatistic = this.D; }
else if (paramH1 == H1.LESS_THAN) { this.testStatistic = this.positiveD; }
else { this.testStatistic = this.negativeD; }

}

public KolmogorovTypeTest(double[] paramArrayOfdouble, Distribution paramDistribution) {
this(paramArrayOfdouble, paramDistribution, H1.NOT_EQUAL);
}

public double getD() {
return this.D;
}

public double getPositiveD() {
return this.positiveD;
}

public double getNegativeD() {
return this.negativeD;
}

public double getTestStatistic() {
return this.testStatistic;
}

public int getN() {
return this.n;
}

public Distribution getF() {
return this.F;
}

public SampleDistributionFunction getSdf() {
return this.sdf;
}

public int indexOfD() {
return this.indexOfD;
}

public int indexOfPositiveD() {
return this.indexOfPositiveD;
}

public int indexOfNegativeD() {
return this.indexOfNegativeD;
}

public double xOfD() {
return this.sdf.getOrderedX(this.indexOfD);
}

public double xOfPositiveD() {
return this.sdf.getOrderedX(this.indexOfPositiveD);
}

public double xOfNegativeD() {
return this.sdf.getOrderedX(this.indexOfNegativeD);
}
}

