package jsc.util;

public class Rank {
    private final int[] r;
    private final double[] rank;
    private int n;
    private int s;
    private int t;

    public Rank(double[] paramArrayOfdouble, double paramDouble) {
        this(paramArrayOfdouble.length, paramArrayOfdouble, paramDouble);
    }

    public Rank(int paramInt, double[] paramArrayOfdouble, double paramDouble) {
        if (paramInt < 1)
            throw new IllegalArgumentException("No data to rank");
        this.n = paramInt;

        double[] arrayOfDouble1 = new double[paramInt];
        this.r = new int[paramInt];
        int i;
        for (i = 0; i < paramInt; ) {
            this.r[i] = i;
            i++;
        }

        this.s = 0;
        this.t = 0;

        double[] arrayOfDouble2 = new double[paramInt];
        for (i = 0; i < paramInt; ) {
            arrayOfDouble2[i] = paramArrayOfdouble[i];
            i++;
        }

        Sort.sort(arrayOfDouble2, this.r, 0, paramInt - 1, true);

        arrayOfDouble1[paramInt - 1] = (paramInt - 1);
        paramInt--;
        for (i = 0; i < paramInt; i++) {

            if (Math.abs(arrayOfDouble2[i] - arrayOfDouble2[i + 1]) > paramDouble) {
                arrayOfDouble1[i] = i;

            } else {

                byte b1 = 1;
                for (int j = i + 1; j < paramInt; ) {

                    if (Math.abs(arrayOfDouble2[j] - arrayOfDouble2[j + 1]) <= paramDouble) {
                        b1++;
                        j++;
                    }
                    break;
                }
                double d = i + 0.5D * b1;
                for (byte b2 = 0; b2 <= b1; ) {
                    arrayOfDouble1[i + b2] = d;
                    b2++;
                }
                int k = b1 * (b1 + 1);
                this.s += k;
                this.t += k * (b1 + 2);
                i += b1;
            }
        }
        paramInt++;
        this.rank = new double[paramInt];

        for (byte b = 0; b < paramInt; ) {
            this.rank[this.r[b]] = arrayOfDouble1[b] + 1.0D;
            b++;
        }

    }

    public int getCorrectionFactor1() {
        return this.t;
    }

    public int getCorrectionFactor2() {
        return this.s;
    }

    public int getN() {
        return this.n;
    }

    public double getRank(int paramInt) {
        return this.rank[paramInt];
    }

    public double[] getRanks() {
        return this.rank;
    }

    public int getSortIndex(int paramInt) {
        return this.r[paramInt];
    }

    public int[] getSortIndexes() {
        return this.r;
    }

    public boolean hasTies() {
        return (this.t > 0);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble1 = {5.0D, 3.0D, 0.0D, 7.0D, 5.0D, 8.0D, 9.0D, 1.0D, 5.0D, 4.0D};
            Rank rank = new Rank(arrayOfDouble1, 0.0D);
            double[] arrayOfDouble2 = rank.getRanks();
            int[] arrayOfInt = rank.getSortIndexes();
            byte b;
            for (b = 0; b < rank.getN(); b++)
                System.out.println("Rank of " + arrayOfDouble1[b] + " is " + arrayOfDouble2[b]);
            for (b = 0; b < rank.getN(); b++)
                System.out.println("Rank " + b + ": " + arrayOfDouble2[arrayOfInt[b]]);
        }
    }
}

