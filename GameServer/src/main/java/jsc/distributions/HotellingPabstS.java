package jsc.distributions;

public class HotellingPabstS
        extends RankSumOfSquares {
    public HotellingPabstS(int paramInt) {
        super(2, paramInt);
    }

    public static double lowerTailProb(int paramInt1, int paramInt2, boolean paramBoolean) {
        return upperTailProb(paramInt1, (int) (paramInt1 * ((paramInt1 * paramInt1) - 1.0D) / 3.0D - paramInt2), paramBoolean);
    }

    public static double upperTailProb(int paramInt1, int paramInt2, boolean paramBoolean) {
        double d = 1.0D;

        if (paramInt1 <= 1) {
            throw new IllegalArgumentException("Less than two observations.");
        }
        if (paramInt2 <= 0) return d;
        d = 0.0D;
        if (paramInt2 > paramInt1 * (paramInt1 * paramInt1 - 1) / 3) return d;
        int i = paramInt2;
        if (i != 2 * i / 2) i++;

        if (paramBoolean) {

            double d1 = 1.0D / paramInt1;
            double d2 = (6.0D * (i - 1.0D) * d1 / (1.0D / d1 * d1 - 1.0D) - 1.0D) * Math.sqrt(1.0D / d1 - 1.0D);

            double d3 = d2 * d2;
            double d4 = d2 * d1 * (0.2274D + d1 * (0.2531D + 0.1745D * d1) + d3 * (-0.0758D + d1 * (0.1033D + 0.3932D * d1) - d3 * d1 * (0.0879D + 0.0151D * d1 - d3 * (0.0072D - 0.0831D * d1 + d3 * d1 * (0.0131D - 4.6E-4D * d3)))));

            d = d4 / Math.exp(d3 / 2.0D) + Normal.standardTailProb(d2, true);
            if (d < 0.0D) d = 0.0D;
            if (d > 1.0D) d = 1.0D;
            return d;
        }

        int[] arrayOfInt = new int[paramInt1 + 1];
        int j = 1;
        byte b1;
        for (b1 = 1; b1 <= paramInt1; b1++) {
            j *= b1;
            arrayOfInt[b1] = b1;
        }
        d = 1.0D / j;
        if (i == paramInt1 * (paramInt1 * paramInt1 - 1) / 3) return d;
        byte b2 = 0;
        for (byte b3 = 1; b3 <= j; b3++) {

            int k = 0;
            for (b1 = 1; b1 <= paramInt1; b1++)
                k += (b1 - arrayOfInt[b1]) * (b1 - arrayOfInt[b1]);
            if (i <= k) b2++;
            int m = paramInt1;
            do {
                int n = arrayOfInt[1];
                int i1 = m - 1;
                for (b1 = 1; b1 <= i1; b1++)
                    arrayOfInt[b1] = arrayOfInt[b1 + 1];
                arrayOfInt[m] = n;
                if (arrayOfInt[m] != m || m == 2)
                    break;
                m--;
            }
            while (b3 != j);
        }

        return b2 / j;
    }

    public double mean() {
        return this.k * ((this.k * this.k) - 1.0D) / 6.0D;
    }

    public double variance() {
        return mean() * mean() / (this.k - 1.0D);
    }

    public String toString() {
        return new String("Hotelling-Pabst distribution: n = " + this.k + ".");
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            byte b2 = 6;

            System.out.println("Discrete distribution");
            HotellingPabstS hotellingPabstS = new HotellingPabstS(4);
            for (byte b1 = 0; b1 < hotellingPabstS.getValueCount(); b1++)
                System.out.println("P( X = " + hotellingPabstS.getValue(b1) + " ) = " + hotellingPabstS.getProb(b1));
        }
    }
}

