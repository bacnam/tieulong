package jsc.util;

import jsc.combinatorics.Selection;

import java.util.BitSet;

public class BitVector
        extends BitSet
        implements Selection {
    private int n;

    public BitVector(int paramInt) {
        super(paramInt);
        this.n = paramInt;
    }

    public BitVector(boolean[] paramArrayOfboolean) {
        this(paramArrayOfboolean.length);
        for (byte b = 0; b < this.n; ) {
            set(b, paramArrayOfboolean[b]);
            b++;
        }

    }

    public BitVector(int[] paramArrayOfint) {
        this(paramArrayOfint.length);
        for (byte b = 0; b < this.n; b++) {

            if (paramArrayOfint[b] == 0) {
                set(b, false);
            } else if (paramArrayOfint[b] == 1) {
                set(b, true);
            } else {
                throw new IllegalArgumentException("Array must contain only 0 or 1.");
            }

        }
    }

    public int length() {
        return this.n;
    }

    public boolean[] toBooleanArray() {
        boolean[] arrayOfBoolean = new boolean[this.n];
        for (byte b = 0; b < this.n; ) {
            arrayOfBoolean[b] = get(b);
            b++;
        }
        return arrayOfBoolean;
    }

    public int[] toIntArray() {
        int[] arrayOfInt = new int[this.n];
        for (byte b = 0; b < this.n; ) {
            arrayOfInt[b] = get(b) ? 1 : 0;
            b++;
        }
        return arrayOfInt;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();

        for (byte b = 0; b < this.n; ) {
            stringBuffer.append(get(b) ? 1 : 0);
            b++;
        }
        return stringBuffer.toString();
    }
}

