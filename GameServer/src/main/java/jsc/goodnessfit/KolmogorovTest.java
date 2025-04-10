package jsc.goodnessfit;

import jsc.distributions.Distribution;
import jsc.distributions.Normal;
import jsc.tests.H1;
import jsc.util.Maths;

public class KolmogorovTest
extends KolmogorovTypeTest
{
static final int SMALL_SAMPLE_SIZE = 100;
double SP;

public KolmogorovTest(double[] paramArrayOfdouble, Distribution paramDistribution, H1 paramH1, boolean paramBoolean) {
super(paramArrayOfdouble, paramDistribution, paramH1);

if (paramBoolean) {
this.SP = approxUpperTailProb(this.n, this.testStatistic);
} else {
this.SP = exactUpperTailProb(this.n, this.testStatistic);
} 
if (paramH1 == H1.NOT_EQUAL) {

if (this.SP > 0.5D) this.SP = 1.0D - this.SP; 
this.SP = 2.0D * this.SP;
} 
}

public KolmogorovTest(double[] paramArrayOfdouble, Distribution paramDistribution, H1 paramH1) {
this(paramArrayOfdouble, paramDistribution, paramH1, (paramArrayOfdouble.length <= 100));
}

public KolmogorovTest(double[] paramArrayOfdouble, Distribution paramDistribution) {
this(paramArrayOfdouble, paramDistribution, H1.NOT_EQUAL, (paramArrayOfdouble.length <= 100));
}

public static double approxUpperTailProb(int paramInt, double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid D value.");

if (paramInt < 1) throw new IllegalArgumentException("n < 1."); 
if (paramInt == 1) return 1.0D - paramDouble;

double d1 = Math.sqrt(paramInt);
double d2 = paramDouble * (d1 + 0.12D + 0.11D / d1);
return Math.exp(-2.0D * d2 * d2);
}

public static double exactUpperTailProb(int paramInt, double paramDouble) {
double d1 = 0.0D;

double d3 = paramInt;

if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid D value."); 
if (paramInt < 1) throw new IllegalArgumentException("n < 1."); 
if (paramInt == 1 || paramDouble == 0.0D || paramDouble == 1.0D) return 1.0D - paramDouble;

double d2 = Maths.logFactorial(paramInt);

for (byte b = 0; b < paramInt * (1.0D - paramDouble) && b < paramInt; b++) {

double d = b / d3;
d1 += Math.exp(d2 - Maths.logFactorial(b) - Maths.logFactorial((paramInt - b)) + (paramInt - b) * Math.log(1.0D - paramDouble - d) + (b - 1) * Math.log(paramDouble + d));
} 

return paramDouble * d1;
}
public double getSP() {
return this.SP;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble = { 72.2D, 64.0D, 53.4D, 76.8D, 86.3D, 58.1D, 63.2D, 73.1D, 78.0D, 44.3D, 85.1D, 66.6D, 80.4D, 76.0D, 68.8D, 76.8D, 58.9D, 58.1D, 74.9D, 72.2D, 73.1D, 39.3D, 52.8D, 54.2D, 65.3D, 74.0D, 63.2D, 64.7D, 68.8D, 85.1D, 62.2D, 76.0D, 70.5D, 48.9D, 78.0D, 66.6D, 58.1D, 32.5D, 63.2D, 64.0D, 68.8D, 65.3D, 71.9D, 72.2D, 63.2D, 72.2D, 70.5D, 80.4D, 45.4D, 59.6D };

KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, (Distribution)new Normal(62.0D, 11.0D));

System.out.println("D+ = " + kolmogorovTest.getPositiveD() + " D- = " + kolmogorovTest.getNegativeD() + " D = " + kolmogorovTest.getD() + " Test D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
}
}
}

