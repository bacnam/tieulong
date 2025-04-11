package jsc.combinatorics;

public class MultiSetPermutation
        implements Selection {
    private int[] p;
    private int[] subsetSizes;

    MultiSetPermutation(int[] paramArrayOfint1, int[] paramArrayOfint2, boolean paramBoolean) {
        this.p = paramArrayOfint1;
        this.subsetSizes = paramArrayOfint2;
    }

    public MultiSetPermutation(int[] paramArrayOfint1, int[] paramArrayOfint2) {
        int i = paramArrayOfint1.length;
        int j = paramArrayOfint2.length;
        int[] arrayOfInt = new int[j];

        byte b;
        for (b = 0; b < j; ) {
            arrayOfInt[b] = 0;
            b++;
        }
        for (b = 0; b < i; b++) {

            int k = paramArrayOfint1[b];
            if (k < 1 || k > j)
                throw new IllegalArgumentException("Multi-set permutation array contains incorrect values.");
            arrayOfInt[k - 1] = arrayOfInt[k - 1] + 1;
        }
        for (b = 0; b < j; b++) {

            if (arrayOfInt[b] != paramArrayOfint2[b]) {
                throw new IllegalArgumentException("Multi-set permutation array contains incorrect values.");
            }
        }
        this.p = paramArrayOfint1;
        this.subsetSizes = paramArrayOfint2;
    }

    public int getSubsetCount() {
        return this.subsetSizes.length;
    }

    public int[] getSubsetSizes() {
        return this.subsetSizes;
    }

    public int length() {
        return this.p.length;
    }

    public int[] toIntArray() {
        return this.p;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("MultiSetPermutation:");
        for (byte b = 0; b < this.p.length; ) {
            stringBuffer.append(" " + this.p[b]);
            b++;
        }
        return stringBuffer.toString();
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            int[] arrayOfInt1 = {2, 1, 3};
            int[] arrayOfInt2 = {2, 3, 1, 3, 1, 3};
            MultiSetPermutation multiSetPermutation = new MultiSetPermutation(arrayOfInt2, arrayOfInt1);
            System.out.println(multiSetPermutation.toString());
        }
    }
}

