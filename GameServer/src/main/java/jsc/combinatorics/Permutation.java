package jsc.combinatorics;

public class Permutation
        implements Selection {
    private int[] p;

    Permutation(int[] paramArrayOfint, boolean paramBoolean) {
        this.p = paramArrayOfint;
    }

    public Permutation(int[] paramArrayOfint) {
        int i = paramArrayOfint.length;

        for (byte b = 1; b <= i; b++) {

            byte b1 = 0;
            while (true) {
                if (b1 >= i)
                    throw new IllegalArgumentException("Permutation array contains incorrect values.");
                if (paramArrayOfint[b1] == b)
                    break;
                b1++;
            }

        }
        this.p = paramArrayOfint;
    }

    public int length() {
        return this.p.length;
    }

    public int[] toIntArray() {
        return this.p;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Permutation:");
        for (byte b = 0; b < this.p.length; ) {
            stringBuffer.append(" " + this.p[b]);
            b++;
        }
        return stringBuffer.toString();
    }
}

