package jsc.contingencytables;

import jsc.util.Arrays;
import jsc.util.BitVector;

public class ContingencyTable2x2
extends ContingencyTable
{
public ContingencyTable2x2(String[] paramArrayOfString1, String[] paramArrayOfString2) {
super(paramArrayOfString1, paramArrayOfString2);
if (getRowCount() != 2 || getColumnCount() != 2) {
throw new IllegalArgumentException("Data not dichotomous.");
}
}

public ContingencyTable2x2(int[] paramArrayOfint1, int[] paramArrayOfint2) {
this(Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2));
}

public ContingencyTable2x2(BitVector paramBitVector1, BitVector paramBitVector2) {
this(paramBitVector1.toIntArray(), paramBitVector2.toIntArray());
}

public ContingencyTable2x2(String[] paramArrayOfString1, String[] paramArrayOfString2, int[][] paramArrayOfint) {
super(paramArrayOfString1, paramArrayOfString2, paramArrayOfint);
if (getRowCount() != 2 || getColumnCount() != 2) {
throw new IllegalArgumentException("Matrix not 2 x 2.");
}
}

public ContingencyTable2x2(int[] paramArrayOfint1, int[] paramArrayOfint2, int[][] paramArrayOfint) {
this(Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2), paramArrayOfint);
}

public ContingencyTable2x2(int[][] paramArrayOfint) {
this(Arrays.sequence(0, 1), Arrays.sequence(0, 1), paramArrayOfint);
}

public ContingencyTable2x2(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
this(paramArrayOfString1, paramArrayOfString2, (new TwoByTwoMatrix(paramInt1, paramInt2, paramInt3, paramInt4)).getMatrix());
}

public ContingencyTable2x2(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
this(Arrays.sequence(0, 1), Arrays.sequence(0, 1), (new TwoByTwoMatrix(paramInt1, paramInt2, paramInt3, paramInt4)).getMatrix());
}

static class TwoByTwoMatrix { int[][] A = new int[2][2];

TwoByTwoMatrix(int param1Int1, int param1Int2, int param1Int3, int param1Int4) { this.A[0][0] = param1Int1; this.A[0][1] = param1Int2; this.A[1][0] = param1Int3; this.A[1][1] = param1Int4; } int[][] getMatrix() {
return this.A;
} }

static class Test
{
public static void main(String[] param1ArrayOfString) {
String[] arrayOfString1 = { "Psychotics", "Neurotics" };
String[] arrayOfString2 = { "Suicidal", "Not suicidal" };
ContingencyTable2x2 contingencyTable2x2 = new ContingencyTable2x2(arrayOfString2, arrayOfString1, 2, 6, 18, 14);
System.out.println(contingencyTable2x2.toString());
}
}
}

