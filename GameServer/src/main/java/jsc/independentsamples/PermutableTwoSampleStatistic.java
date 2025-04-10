package jsc.independentsamples;

import jsc.combinatorics.Selection;
import jsc.statistics.PermutableStatistic;
import jsc.util.Arrays;

public abstract class PermutableTwoSampleStatistic
implements PermutableStatistic
{
protected int N;
protected double[] originalSample;
protected double[] permutedSampleA;
protected double[] permutedSampleB;

public PermutableTwoSampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
this.originalSample = Arrays.append(paramArrayOfdouble2, paramArrayOfdouble1);
this.N = this.originalSample.length;
this.permutedSampleA = new double[paramArrayOfdouble1.length];
this.permutedSampleB = new double[paramArrayOfdouble2.length];
}
public int getN() {
return this.N;
}

public double permuteStatistic(Selection paramSelection) {
byte b1 = 0;
byte b2 = 0;
int[] arrayOfInt = paramSelection.toIntArray();

for (byte b3 = 0; b3 < this.N; b3++) {

if (arrayOfInt[b3] == 1) {
this.permutedSampleA[b1] = this.originalSample[b3]; b1++;
} else if (arrayOfInt[b3] == 2) {
this.permutedSampleB[b2] = this.originalSample[b3]; b2++;
} else {
throw new IllegalArgumentException("Invalid permutation.");
} 
} 

return resampleStatistic(this.permutedSampleA, this.permutedSampleB);
}

public abstract double resampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2);
}

