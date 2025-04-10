package jsc.independentsamples;

import jsc.ci.ConfidenceInterval;
import jsc.combinatorics.Enumerator;
import jsc.combinatorics.MultiSetPermutations;
import jsc.descriptive.MeanVar;
import jsc.distributions.StudentsT;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;

public class TwoSampleTtest
extends PermutableTwoSampleStatistic
implements TwoSampleStatistic, SignificanceTest, ConfidenceInterval
{
private final boolean sameVar;
private final double d;
private final double df;
private final double s;
private final double t;
private final MeanVar mvA;
private final MeanVar mvB;
private final double SP;
private double confidenceCoeff;
private double lowerLimit;
private double upperLimit;
private final double[] xA;
private final double[] xB;

public TwoSampleTtest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, boolean paramBoolean, double paramDouble) {
super(paramArrayOfdouble1, paramArrayOfdouble2);
this.sameVar = paramBoolean;
this.xA = paramArrayOfdouble1;
this.xB = paramArrayOfdouble2;
this.mvA = new MeanVar(paramArrayOfdouble1);
this.mvB = new MeanVar(paramArrayOfdouble2);
int i = this.mvA.getN();
int j = this.mvB.getN();

if (paramBoolean) {

this.df = (i + j - 2);
double d1 = ((i - 1) * this.mvA.getVariance() + (j - 1) * this.mvB.getVariance()) / this.df;
this.s = Math.sqrt(d1 * (1.0D / i + 1.0D / j));
}
else {

double d1 = this.mvA.getVariance() / i;
double d2 = this.mvB.getVariance() / j;
this.df = Math.floor((d1 + d2) * (d1 + d2) / (d1 * d1 / (i - 1) + d2 * d2 / (j - 1)));

this.s = Math.sqrt(d1 + d2);
} 

this.d = this.mvA.getMean() - this.mvB.getMean();
this.t = this.d / this.s;
double d = StudentsT.tailProb(this.t, this.df);

if (paramH1 == H1.NOT_EQUAL) {
this.SP = d + d;
} else if (paramH1 == H1.LESS_THAN) {
this.SP = (this.t < 0.0D) ? d : (1.0D - d);
} else {
this.SP = (this.t > 0.0D) ? d : (1.0D - d);
} 

setConfidenceCoeff(paramDouble);
}

public TwoSampleTtest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, boolean paramBoolean) {
this(paramArrayOfdouble1, paramArrayOfdouble2, paramH1, paramBoolean, 0.95D);
}

public TwoSampleTtest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, boolean paramBoolean) {
this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, paramBoolean);
}

public TwoSampleTtest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, false);
}

public double resampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
double d;
MeanVar meanVar1 = new MeanVar(paramArrayOfdouble1);
MeanVar meanVar2 = new MeanVar(paramArrayOfdouble2);
int i = meanVar1.getN();
int j = meanVar2.getN();

if (this.sameVar) {

double d1 = ((i - 1) * meanVar1.getVariance() + (j - 1) * meanVar2.getVariance()) / this.df;
d = Math.sqrt(d1 * (1.0D / i + 1.0D / j));
}
else {

double d1 = meanVar1.getVariance() / i;
double d2 = meanVar2.getVariance() / j;
d = Math.sqrt(d1 + d2);
} 

return (meanVar1.getMean() - meanVar2.getMean()) / d;
}

public double getDf() {
return this.df;
}

public Enumerator getEnumerator() {
int[] arrayOfInt = new int[2];
arrayOfInt[0] = this.mvA.getN(); arrayOfInt[1] = this.mvB.getN();
return (Enumerator)new MultiSetPermutations(arrayOfInt);
}

public double getMeanA() {
return this.mvA.getMean();
}

public double getMeanB() {
return this.mvB.getMean();
}

public double getMeanDiff() {
return this.d;
}
public double[] getSampleA() { return this.xA; } public double[] getSampleB() {
return this.xB;
}

public double getSdA() {
return this.mvA.getSd();
}

public double getSdB() {
return this.mvB.getSd();
}

public double getSdDiff() {
return this.s;
} public int sizeA() {
return this.mvA.getN();
} public int sizeB() {
return this.mvB.getN();
} public double getSP() {
return this.SP;
}

public double getStatistic() {
return this.t;
}

public double getTestStatistic() {
return this.t;
}

public double getLowerLimit() {
return this.lowerLimit;
}

public double getUpperLimit() {
return this.upperLimit;
}

public double getConfidenceCoeff() {
return this.confidenceCoeff;
}

public void setConfidenceCoeff(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D)
throw new IllegalArgumentException("Invalid confidence coefficient."); 
this.confidenceCoeff = paramDouble;
StudentsT studentsT = new StudentsT(this.df);
double d1 = studentsT.inverseCdf(0.5D + 0.5D * paramDouble);
double d2 = d1 * this.s;
this.lowerLimit = this.d - d2;
this.upperLimit = this.d + d2;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 90.0D, 72.0D, 61.0D, 66.0D, 81.0D, 69.0D, 59.0D, 70.0D };
double[] arrayOfDouble2 = { 62.0D, 85.0D, 78.0D, 66.0D, 80.0D, 91.0D, 69.0D, 77.0D, 84.0D };
TwoSampleTtest twoSampleTtest1 = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2);
System.out.println("H1: means not equal: t = " + twoSampleTtest1.getTestStatistic() + " SP = " + twoSampleTtest1.getSP());

System.out.println("CI = [" + twoSampleTtest1.getLowerLimit() + ", " + twoSampleTtest1.getUpperLimit() + "]");
TwoSampleTtest twoSampleTtest2 = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2, H1.LESS_THAN, false);
System.out.println("H1: mean A < mean B: t = " + twoSampleTtest2.getTestStatistic() + " SP = " + twoSampleTtest2.getSP());
System.out.println("CI = [" + twoSampleTtest2.getLowerLimit() + ", " + twoSampleTtest2.getUpperLimit() + "]");
TwoSampleTtest twoSampleTtest3 = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2, H1.GREATER_THAN, false);
System.out.println("H1: mean A > mean B: t = " + twoSampleTtest3.getTestStatistic() + " SP = " + twoSampleTtest3.getSP());
System.out.println("CI = [" + twoSampleTtest3.getLowerLimit() + ", " + twoSampleTtest3.getUpperLimit() + "]");
}
}
}

