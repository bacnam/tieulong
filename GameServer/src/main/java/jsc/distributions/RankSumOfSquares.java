package jsc.distributions;

import jsc.combinatorics.Permutation;
import jsc.combinatorics.Permutations;
import jsc.descriptive.DoubleTally;

public class RankSumOfSquares
extends Discrete
{
protected int n;
protected int k;
private double tolerance = 1.0E-14D;

private double Rbar;

private int[] perm;

private int[][] R;

private DoubleTally S;

private Permutations[] perms;

public RankSumOfSquares(int paramInt1, int paramInt2) {
this.n = paramInt1;
this.k = paramInt2;
if (paramInt1 < 2)
throw new IllegalArgumentException("Less than two rankings."); 
if (paramInt2 < 2) {
throw new IllegalArgumentException("Less than two objects.");
}

this.S = new DoubleTally(166, 10, this.tolerance);

this.R = new int[paramInt1][paramInt2];
this.perm = new int[paramInt2];
this.Rbar = 0.5D * paramInt1 * (paramInt2 + 1.0D);

byte b2 = 0;
for (byte b1 = 0; b1 < paramInt2; ) { this.R[0][b1] = b1 + 1; b1++; }

this.perms = new Permutations[paramInt1];
for (b2 = 0; b2 < paramInt1; ) { this.perms[b2] = new Permutations(paramInt2); b2++; }

recurse(1);

setDistribution(this.S);

this.S = null;
this.R = null;
this.perm = null;
this.perms = null;
}

private void recurse(int paramInt) {
while (this.perms[paramInt].hasNext()) {

Permutation permutation = this.perms[paramInt].nextPermutation();

this.perm = permutation.toIntArray();
for (byte b = 0; b < this.k; ) { this.R[paramInt][b] = this.perm[b]; b++; }
if (paramInt == this.n - 1) {

this.S.addValue(getS());
continue;
} 
recurse(paramInt + 1);
this.perms[paramInt + 1].reset();
} 
}

public double criticalValue(double paramDouble) {
if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability."); 
double d = 0.0D;
for (int i = this.valueCount - 1; i >= 0; i--) {

d += this.probs[i];
if (d == paramDouble) return this.values[i];

if (d > paramDouble) return (i < this.valueCount - 1) ? this.values[i + 1] : -1.0D; 
} 
return this.minValue;
}

private double getS() {
double d1 = 0.0D;
double d2 = 0.0D;

for (byte b = 0; b < this.k; b++) {

d1 = 0.0D;
for (byte b1 = 0; b1 < this.n; ) { d1 += this.R[b1][b]; b1++; }
double d = d1 - this.Rbar;
d2 += d * d;
} 

return d2;
}

public String toString() {
return new String("Rank sum of squares distribution: " + this.n + " blocks, " + this.k + " treatments.");
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
byte b = -2; byte b1 = 2;
System.out.println("n = " + b + " m = " + b1);

long l1 = System.currentTimeMillis();
RankSumOfSquares rankSumOfSquares = new RankSumOfSquares(b1, b);
long l2 = System.currentTimeMillis();
System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
for (byte b2 = 0; b2 < rankSumOfSquares.getValueCount(); b2++) {

int i = (int)rankSumOfSquares.getValue(b2);
System.out.println("S = " + i + " P = " + rankSumOfSquares.upperTailProb(i));
} 
}
}
}

