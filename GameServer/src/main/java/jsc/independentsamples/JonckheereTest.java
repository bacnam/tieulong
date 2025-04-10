package jsc.independentsamples;

import jsc.datastructures.GroupedData;
import jsc.distributions.MannWhitneyU;
import jsc.distributions.Normal;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;
import jsc.util.Arrays;
import jsc.util.Maths;

public class JonckheereTest
implements SignificanceTest
{
static final int MAX_PRODUCT = (int)Math.pow(15.0D, 6.0D);

final int N;

double W;

final int[] n;

private double SP;

public JonckheereTest(GroupedData paramGroupedData, String[] paramArrayOfString, double paramDouble, boolean paramBoolean) {
int i = paramGroupedData.getGroupCount();
if (i < 2)
throw new IllegalArgumentException("Less than two samples."); 
this.N = paramGroupedData.getN();
if (this.N < 5)
throw new IllegalArgumentException("Less than five data values."); 
if (paramArrayOfString.length != i)
throw new IllegalArgumentException("Alternative array wrong length."); 
this.n = new int[i];

this.W = 0.0D;
for (byte b = 0; b < i; b++) {

double[] arrayOfDouble = paramGroupedData.getData(paramArrayOfString[b]);
int k = arrayOfDouble.length;
this.n[b] = k;
for (int j = b + 1; j < i; j++) {

double[] arrayOfDouble1 = paramGroupedData.getData(paramArrayOfString[j]);
if (arrayOfDouble1 == null)
throw new IllegalArgumentException("Invalid alternative sample label."); 
int m = arrayOfDouble1.length;
MannWhitneyTest mannWhitneyTest = new MannWhitneyTest(arrayOfDouble, arrayOfDouble1, H1.LESS_THAN, paramDouble, true);
double d = mannWhitneyTest.getTestStatistic();
this.W += d;
} 
} 

if (paramBoolean) {
this.SP = approxSP(this.n, this.W);
} else {

try { this.SP = exactSP(this.n, this.W); }
catch (RuntimeException runtimeException) { this.SP = approxSP(this.n, this.W); }

} 
}

public JonckheereTest(GroupedData paramGroupedData, String[] paramArrayOfString) {
this(paramGroupedData, paramArrayOfString, 0.0D, (paramGroupedData.getMaxSize() > 4));
}

public static double approxSP(int[] paramArrayOfint, double paramDouble) {
int i = 0;
double d1 = 0.0D;
double d2 = 0.0D;
for (byte b = 0; b < paramArrayOfint.length; b++) {

if (paramArrayOfint[b] < 2)
throw new IllegalArgumentException("Less than two data values in a sample."); 
d1 += (paramArrayOfint[b] * paramArrayOfint[b]);
d2 += (paramArrayOfint[b] * paramArrayOfint[b]) * ((paramArrayOfint[b] + paramArrayOfint[b]) + 3.0D);
i += paramArrayOfint[b];
} 
double d3 = 0.25D * ((i * i) - d1);
double d4 = Math.sqrt(((i * i) * ((i + i) + 3.0D) - d2) / 72.0D);
double d5 = (paramDouble - d3 + 0.5D) / d4;

return Normal.standardTailProb(d5, false);
}

public static double exactSP(int[] paramArrayOfint, double paramDouble) {
int i = (int)Arrays.sum(paramArrayOfint);

if (paramDouble > (MAX_PRODUCT / 2))
throw new RuntimeException("Insufficient memory for exact distribution: try normal approximation."); 
int j = (int)Math.ceil(paramDouble);

double[] arrayOfDouble = new double[1 + j];

i -= paramArrayOfint[0];
MannWhitneyU.harding(false, paramArrayOfint[0], i, j, arrayOfDouble);

for (byte b1 = 1; b1 < paramArrayOfint.length - 1; b1++) {

i -= paramArrayOfint[b1];
MannWhitneyU.harding(true, paramArrayOfint[b1], i, j, arrayOfDouble);
} 

double d2 = Maths.logMultinomialCoefficient(paramArrayOfint);

double d1 = 0.0D;

for (byte b2 = 0; b2 <= j; b2++) {

double d = Math.exp(Math.log(arrayOfDouble[b2]) - d2);

if (Double.isNaN(d))
{

throw new RuntimeException("Cannot calculate exact distribution: try normal approximation.");
}
d1 += d;
} 
return d1;
}

public double getSP() {
return this.SP;
}

public double getTestStatistic() {
return this.W;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble = { 19.0D, 21.0D, 40.0D, 49.0D, 20.0D, 61.0D, 99.0D, 110.0D, 60.0D, 80.0D, 100.0D, 151.0D, 130.0D, 129.0D, 149.0D, 160.0D };
String[] arrayOfString1 = { "I", "II", "III", "IV", "I", "II", "III", "IV", "I", "II", "III", "IV", "I", "II", "III", "IV" };
String[] arrayOfString2 = { "I", "II", "III", "IV" };

GroupedData groupedData = new GroupedData(arrayOfDouble, arrayOfString1);
JonckheereTest jonckheereTest = new JonckheereTest(groupedData, arrayOfString2, 0.0D, false);
double d = jonckheereTest.getTestStatistic();
System.out.println("W = " + d);
System.out.println("       SP = " + jonckheereTest.getSP());
System.out.println("Approx SP = " + JonckheereTest.approxSP(groupedData.getSizes(), d));
System.out.println(" Exact SP = " + JonckheereTest.exactSP(groupedData.getSizes(), d));
}
}
}

