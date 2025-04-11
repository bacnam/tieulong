package jsc.distributions;

import jsc.combinatorics.MultiSetPermutation;
import jsc.combinatorics.MultiSetPermutations;
import jsc.descriptive.DoubleTally;
import jsc.util.Arrays;

public class KruskalWallisH
        extends Discrete {
    private final int[] ns;
    private final double Rbar;
    private final double c;
    private double tolerance = 1.0E-14D;
    private int[] ranks;

    private int[] count;

    private double[] R;

    private DoubleTally H;

    public KruskalWallisH(int[] paramArrayOfint) {
        this.ns = paramArrayOfint;
        MultiSetPermutations multiSetPermutations = new MultiSetPermutations(paramArrayOfint);

        this.H = new DoubleTally(1500, 100, this.tolerance);

        int i = paramArrayOfint.length;
        this.R = new double[i];

        int j = (int) Arrays.sum(paramArrayOfint);
        this.ranks = new int[j];
        this.count = new int[i];
        this.c = 12.0D / j * (j + 1.0D);
        this.Rbar = 0.5D * (j + 1.0D);

        while (multiSetPermutations.hasNext()) {
            double d = 0.0D;

            MultiSetPermutation multiSetPermutation = multiSetPermutations.nextPermutation();

            int[] arrayOfInt = multiSetPermutation.toIntArray();

            this.count[0] = 0;
            byte b1;
            for (b1 = 1; b1 < i; ) {
                this.count[b1] = this.count[b1 - 1] + this.ns[b1 - 1];
                b1++;
            }

            for (b1 = 0; b1 < j; b1++) {

                int k = arrayOfInt[b1] - 1;
                this.ranks[this.count[k]] = b1 + 1;
                this.count[k] = this.count[k] + 1;
            }

            byte b2 = 0;
            for (b1 = 0; b1 < i; b1++) {

                this.R[b1] = 0.0D;
                for (byte b = 0; b < this.ns[b1]; ) {
                    this.R[b1] = this.R[b1] + this.ranks[b2++];
                    b++;
                }

            }

            for (b1 = 0; b1 < i; ) {
                this.R[b1] = this.R[b1] / this.ns[b1];
                b1++;
            }

            for (b1 = 0; b1 < i; b1++) {

                double d1 = this.R[b1] - this.Rbar;

                d += this.ns[b1] * d1 * d1;
            }

            this.H.addValue(this.c * d);
        }

        setDistribution(this.H);

        this.H = null;
        this.R = null;

        this.ranks = null;
        this.count = null;
    }

    public double criticalValue(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability.");
        double d = 0.0D;
        for (int i = this.valueCount - 1; i >= 0; i--) {

            d += this.probs[i];

            if (Math.abs(d - paramDouble) < 1.0E-16D) return this.values[i];
            if (d > paramDouble) return (i < this.valueCount - 1) ? this.values[i + 1] : -1.0D;
        }
        return this.minValue;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Kruskal-Wallis H distribution; sample sizes");
        for (byte b = 0; b < this.valueCount; ) {
            stringBuffer.append(" " + this.ns[b]);
            b++;
        }
        return stringBuffer.toString();
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            int[] arrayOfInt = {4, 3, 2};

            long l1 = System.currentTimeMillis();
            KruskalWallisH kruskalWallisH = new KruskalWallisH(arrayOfInt);
            long l2 = System.currentTimeMillis();
            System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
            System.out.println("Kruskal-Wallis H distribution");
            double d = 0.0D;
            int i = kruskalWallisH.getValueCount();
            for (byte b = 0; b < i; b++) {

                System.out.println("P(X = " + kruskalWallisH.getValue(b) + ") = " + kruskalWallisH.getProb(b));
                d += kruskalWallisH.getProb(b);
            }
            System.out.println("N = " + i + " sum = " + d + " Crit.value = " + kruskalWallisH.criticalValue(0.01D));
        }
    }
}

