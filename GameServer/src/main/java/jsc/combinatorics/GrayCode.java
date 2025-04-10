package jsc.combinatorics;

import java.util.NoSuchElementException;
import java.util.Random;
import jsc.util.BitVector;

public class GrayCode
implements Enumerator
{
private int k;
private Random rand;
private boolean firstCall;
private boolean finalExit;
private int n;
private int[] a;
private final double subsetCount;

public GrayCode(int paramInt) {
this.n = paramInt;

this.subsetCount = Math.pow(2.0D, paramInt);

if (paramInt < 1)
throw new IllegalArgumentException("n < 1."); 
this.a = new int[paramInt + 1];

this.rand = new Random();

reset();
}

public double countSelections() {
return this.subsetCount;
}

private BitVector getBitVector() {
BitVector bitVector = new BitVector(this.n);
for (byte b = 1; b <= this.n; ) { bitVector.set(b - 1, (this.a[b] == 1)); b++; }
return bitVector;
}

public int getN() {
return this.n;
}

public boolean hasNext() {
return !this.finalExit;
}

public Selection nextSelection() {
return (Selection)nextBitVector();
}

public BitVector nextBitVector() {
if (this.finalExit) throw new NoSuchElementException();

this.a[0] = 0;
if (this.firstCall) {

this.firstCall = false;
return getBitVector();
} 

int i = this.k % 2;
byte b = 1;
if (i != 0) {

do { b++; } while (this.a[b - 1] != 1);
}

this.a[b] = 1 - this.a[b];
this.k = this.k + 2 * this.a[b] - 1;
if (this.k == this.a[this.n]) this.finalExit = true; 
return getBitVector();
}

public Selection randomSelection() {
return (Selection)randomBitVector();
}

public BitVector randomBitVector() {
BitVector bitVector = new BitVector(this.n);
for (byte b = 0; b < this.n; ) { bitVector.set(b, this.rand.nextBoolean()); b++; }
return bitVector;
}

public void reset() {
for (byte b = 1; b <= this.n; ) { this.a[b] = 0; b++; }
this.firstCall = true;
this.finalExit = false;
this.k = 0;
}

public void setSeed(long paramLong) {
this.rand.setSeed(paramLong);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
byte b1 = 4;
GrayCode grayCode = new GrayCode(b1);
int i = (int)grayCode.countSelections();
System.out.println("Number of bit vectors = " + i);

while (grayCode.hasNext()) {

BitVector bitVector = (BitVector)grayCode.nextSelection();
System.out.println(bitVector.toString());
} 

System.out.println("Random Gray codes");
for (byte b2 = 0; b2 < 10; b2++) {

BitVector bitVector = (BitVector)grayCode.randomSelection();
System.out.println(bitVector.toString());
} 
}
}
}

