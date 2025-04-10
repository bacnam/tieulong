package jsc.onesample;

import java.util.Arrays;
import jsc.ci.AbstractConfidenceInterval;
import jsc.datastructures.PairedData;
import jsc.distributions.Binomial;
import jsc.util.Maths;

public class SignMedianCI
extends AbstractConfidenceInterval
{
int d;
double achievedConfidence;
double median;

public SignMedianCI(double[] paramArrayOfdouble, double paramDouble) {
super(paramDouble);

int i = paramArrayOfdouble.length;
if (i < 2) {
throw new IllegalArgumentException("Less than two values.");
}

Binomial binomial = new Binomial(i, 0.5D);
this.d = (int)binomial.inverseCdf(0.5D - paramDouble / 2.0D);

if (this.d < 1 || this.d > i)
throw new IllegalArgumentException("Cannot calculate interval."); 
double d = binomial.cdf((this.d - 1));

double[] arrayOfDouble = new double[i];
System.arraycopy(paramArrayOfdouble, 0, arrayOfDouble, 0, i);
Arrays.sort(arrayOfDouble);
this.lowerLimit = arrayOfDouble[this.d - 1];
this.upperLimit = arrayOfDouble[i - this.d];

int j = i / 2;
if (i % 2 == 0) {
this.median = (arrayOfDouble[j - 1] + arrayOfDouble[j]) / 2.0D;
} else {
this.median = arrayOfDouble[j];
}  this.achievedConfidence = 1.0D - 2.0D * d;
}

public SignMedianCI(PairedData paramPairedData, double paramDouble) {
this(paramPairedData.differences(), paramDouble);
}

public double getAchievedConfidence() {
return this.achievedConfidence;
}

public int getD() {
return this.d;
}

public double getMedian() {
return this.median;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
long l1 = System.currentTimeMillis();

double[] arrayOfDouble1 = { 5.0D, 3.0D, -1.0D, 14.0D, 7.0D, -4.0D, 11.0D, 10.0D };

int i = arrayOfDouble1.length;
SignMedianCI signMedianCI = new SignMedianCI(arrayOfDouble1, 0.92D);
long l2 = System.currentTimeMillis();
System.out.println("n = " + i + " Time = " + ((l2 - l1) / 1000L) + " secs");
System.out.println("CI = [" + signMedianCI.getLowerLimit() + ", " + signMedianCI.getUpperLimit() + "]" + " d = " + signMedianCI.getD() + " Median = " + signMedianCI.getMedian() + " Achieved confidence = " + signMedianCI.getAchievedConfidence());

double[] arrayOfDouble2 = { 51.0D, 48.0D, 52.0D, 62.0D, 64.0D, 51.0D, 55.0D, 60.0D };
double[] arrayOfDouble3 = { 46.0D, 45.0D, 53.0D, 48.0D, 57.0D, 55.0D, 44.0D, 50.0D };
double[] arrayOfDouble4 = { 0.89D, 0.9D, 0.92D, 0.94D, 0.95D, 0.96D, 0.98D, 0.99D };
PairedData pairedData = new PairedData(arrayOfDouble2, arrayOfDouble3);
System.out.println("************* Suntan lotions: n = 8 *******************"); byte b;
for (b = 0; b < arrayOfDouble4.length; b++) {

System.out.println("Nominal confidence coeff. = " + arrayOfDouble4[b]);
signMedianCI = new SignMedianCI(pairedData, arrayOfDouble4[b]);
System.out.println("Exact CI=[" + signMedianCI.getLowerLimit() + "," + signMedianCI.getUpperLimit() + "]" + " d=" + signMedianCI.getD() + " Achieved conf.=" + Maths.round(signMedianCI.getAchievedConfidence(), 3));
} 

double[] arrayOfDouble5 = { 54.5D, 70.6D, 85.6D, 78.2D, 69.6D, 73.1D, 97.5D, 85.6D, 74.9D, 86.8D, 53.6D, 89.4D };
double[] arrayOfDouble6 = { 55.5D, 72.9D, 84.8D, 78.3D, 71.6D, 74.0D, 97.2D, 88.0D, 74.4D, 89.3D, 52.3D, 91.5D };
pairedData = new PairedData(arrayOfDouble5, arrayOfDouble6);
System.out.println("*************** Weight change data: n = 12 ***************");
for (b = 0; b < arrayOfDouble4.length; b++) {

System.out.println("Nominal confidence coeff. = " + arrayOfDouble4[b]);
signMedianCI = new SignMedianCI(pairedData, arrayOfDouble4[b]);
System.out.println(" Exact CI=[" + signMedianCI.getLowerLimit() + "," + signMedianCI.getUpperLimit() + "]" + " d=" + signMedianCI.getD() + " Achieved conf.=" + Maths.round(signMedianCI.getAchievedConfidence(), 3));

signMedianCI = new SignMedianCI(pairedData, arrayOfDouble4[b]);
System.out.println("Approx CI=[" + signMedianCI.getLowerLimit() + "," + signMedianCI.getUpperLimit() + "]" + " d=" + signMedianCI.getD() + " Achieved conf.=" + Maths.round(signMedianCI.getAchievedConfidence(), 3));
} 
}
}
}

