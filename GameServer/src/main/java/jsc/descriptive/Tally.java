package jsc.descriptive;

public class Tally
        implements Cloneable {
    private int n;
    private int min;
    private int max;
    private int k;
    private int[] freq;

    public Tally(int[] paramArrayOfint) {
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
        byte b;
        for (b = 0; b < paramArrayOfint.length; b++) {

            if (paramArrayOfint[b] < this.min) this.min = paramArrayOfint[b];
            if (paramArrayOfint[b] > this.max) this.max = paramArrayOfint[b];

        }
        this.k = 1 + this.max - this.min;

        this.freq = new int[this.k];
        clearData();
        for (b = 0; b < paramArrayOfint.length; ) {
            addValue(paramArrayOfint[b]);
            b++;
        }

    }

    public Tally(int paramInt1, int paramInt2) {
        if (paramInt2 <= paramInt1) {
            throw new IllegalArgumentException("Invalid bins");
        }
        this.k = 1 + paramInt2 - paramInt1;
        this.min = paramInt1;
        this.max = paramInt2;

        this.freq = new int[this.k];
        clearData();
    }

    public Tally(int paramInt1, int paramInt2, int[] paramArrayOfint) {
        this(paramInt1, paramInt2);

        for (byte b = 0; b < paramArrayOfint.length; ) {
            addValue(paramArrayOfint[b]);
            b++;
        }

    }

    public Tally(int paramInt, int[] paramArrayOfint) {
        this.k = paramArrayOfint.length;
        this.min = paramInt;
        this.max = paramInt + this.k - 1;
        this.freq = new int[this.k];
        this.n = 0;
        for (byte b = 0; b < this.k; ) {
            this.freq[b] = paramArrayOfint[b];
            this.n += paramArrayOfint[b];
            b++;
        }

    }

    public int addValue(int paramInt) {
        if (paramInt >= this.min && paramInt <= this.max) {

            this.n++;
            int i = paramInt - this.min;
            this.freq[i] = this.freq[i] + 1;
            return i;
        }

        return -1;
    }

    public void clearData() {
        this.n = 0;
        for (byte b = 0; b < this.k; ) {
            this.freq[b] = 0;
            b++;
        }

    }

    public Object clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            System.out.println("Tally can't clone");
        }
        return object;
    }

    public int getBinValue(int paramInt) {
        return this.min + paramInt;
    }

    public int[] getFrequencies() {
        return this.freq;
    }

    public int getFrequency(int paramInt) {
        return this.freq[paramInt];
    }

    public int getMax() {
        return this.max;
    }

    public int getMaxFreq() {
        int i = 0;
        for (byte b = 0; b < this.k; b++) {
            if (this.freq[b] > i) i = this.freq[b];
        }
        return i;
    }

    public int getMin() {
        return this.min;
    }

    public int getNumberOfBins() {
        return this.k;
    }

    public int getN() {
        return this.n;
    }

    public double getPercentage(int paramInt) {
        return (this.n > 0) ? (100.0D * this.freq[paramInt] / this.n) : 0.0D;
    }

    public double getProportion(int paramInt) {
        return (this.n > 0) ? (this.freq[paramInt] / this.n) : 0.0D;
    }

    public int indexOf(int paramInt) {
        if (paramInt >= this.min && paramInt <= this.max) {
            return paramInt - this.min;
        }
        return -1;
    }

    public void setData(int[] paramArrayOfint) {
        clearData();
        for (byte b = 0; b < paramArrayOfint.length; ) {
            addValue(paramArrayOfint[b]);
            b++;
        }

    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            int[] arrayOfInt = {4, 5, 8, 9, 3, 2, 5, 4, 6, 7, 8, 9, 2, 1, 0, -3, -2, 0, -6, -3};
            Tally tally = new Tally(arrayOfInt);

            System.out.println("Tally " + tally.getN() + " values, " + "Min = " + tally.getMin() + ", Max = " + tally.getMax());
            byte b;
            for (b = 0; b < tally.getNumberOfBins(); b++) {
                System.out.println(tally.getBinValue(b) + ", Freq = " + tally.getFrequency(b) + ", % = " + tally.getPercentage(b));
            }
            for (b = 0; b < arrayOfInt.length; b++)
                System.out.println("Index of " + arrayOfInt[b] + " is " + tally.indexOf(arrayOfInt[b]));
        }
    }
}

