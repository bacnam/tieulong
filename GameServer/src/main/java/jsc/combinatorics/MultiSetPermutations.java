package jsc.combinatorics;

import jsc.util.Arrays;
import jsc.util.Maths;

import java.util.NoSuchElementException;
import java.util.Random;

public class MultiSetPermutations
        implements Enumerator {
    private final double permutationCount;
    private boolean hasNext;
    private Random rand;
    private long count;
    private int k;
    private int k1;
    private int N;
    private int[] randPerm;
    private int[] ns;
    private int[] r;
    private int[][] A;

    public MultiSetPermutations(int paramInt) {
        this(Arrays.fill(paramInt, 1));
    }

    public MultiSetPermutations(int[] paramArrayOfint) {
        this.k = paramArrayOfint.length;

        this.permutationCount = Maths.multinomialCoefficient(paramArrayOfint);

        this.ns = paramArrayOfint;
        this.r = new int[this.k + 1];
        System.arraycopy(paramArrayOfint, 0, this.r, 1, this.k);

        this.N = 0;
        byte b2;
        for (b2 = 1; b2 <= this.k; b2++) {

            if (this.r[b2] == 0)
                throw new IllegalArgumentException("Empty subset.");
            this.N += this.r[b2];
        }
        if (this.N < 2)
            throw new IllegalArgumentException("Less than 2 objects.");
        this.A = new int[this.k + 1][this.N + 1];

        this.rand = new Random();

        this.randPerm = new int[this.N];

        byte b3 = 0;
        for (byte b1 = 0; b1 < this.k; b1++) {
            for (b2 = 0; b2 < paramArrayOfint[b1]; b2++) {
                this.randPerm[b3] = b1 + 1;
                b3++;
            }
        }
        reset();
    }

    public double countSelections() {
        return this.permutationCount;
    }

    public int getN() {
        return this.N;
    }

    private MultiSetPermutation getPermutation() {
        int[] arrayOfInt = new int[this.N];
        for (byte b = 1; b <= this.N; ) {
            arrayOfInt[b - 1] = this.A[this.k][b];
            b++;
        }
        if (this.count > this.permutationCount) this.hasNext = false;
        return new MultiSetPermutation(arrayOfInt, this.ns, false);
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    private void moveMarks(int paramInt1, int paramInt2) {
        boolean bool;
        do {
            bool = false;
            for (byte b = 1; b <= paramInt2; b++) {

                int i = b + 1;
                if (this.A[paramInt1][b] == paramInt1 + 1 && this.A[paramInt1][i] == paramInt1) {
                    this.A[paramInt1][b] = this.A[paramInt1][i];
                    this.A[paramInt1][i] = this.A[paramInt1][b] + 1;
                    bool = true;
                }
            }
        } while (bool);
    }

    public Selection nextSelection() {
        return nextPermutation();
    }

    public MultiSetPermutation nextPermutation() {
        int i = this.k - 1;
        int j = this.r[this.k] + this.r[i];
        if (this.count == 1L) {

            step5();

            this.count++;
            return getPermutation();
        }

        while (true) {
            byte b;

            for (b = 1; b <= j - 1; ) {

                int k = b + 1;
                if (this.A[i][b] != i || this.A[i][k] != i + 1) {
                    b++;
                    continue;
                }
                int m = b - 2;
                this.A[i][b] = this.A[i][k];
                this.A[i][k] = this.A[i][b] - 1;

                if (m <= 0) {

                    step5();

                    this.count++;
                    return getPermutation();
                }

                moveMarks(i, m);
                step5();

                this.count++;
                return getPermutation();
            }

            if (j != 1) {

                int k = j / 2;
                for (b = 1; b <= k; b++) {

                    int n = j - b + 1;
                    int m = this.A[i][b];
                    this.A[i][b] = this.A[i][n];
                    this.A[i][n] = m;
                }
            }

            i--;
            if (i <= 0) throw new NoSuchElementException();
            j += this.r[i];
        }
    }

    public Selection randomSelection() {
        return randomPermutation();
    }

    public MultiSetPermutation randomPermutation() {
        for (byte b = 0; b < this.N; b++) {

            int i = b + this.rand.nextInt(this.N - b);

            int j = this.randPerm[i];
            this.randPerm[i] = this.randPerm[b];
            this.randPerm[b] = j;
        }
        return new MultiSetPermutation(this.randPerm, this.ns, false);
    }

    public void reset() {
        this.count = 1L;
        int i = this.N;
        this.k1 = this.k - 1;
        for (byte b = 1; b <= this.k1; b++) {

            int j = this.r[b];
            int m;
            for (m = 1; m <= j; ) {
                this.A[b][m] = b;
                m++;
            }
            int k = this.r[b] + 1;
            for (m = k; m <= i; ) {
                this.A[b][m] = b + 1;
                m++;
            }
            i -= this.r[b];
        }

        this.hasNext = true;
    }

    public void setSeed(long paramLong) {
        this.rand.setSeed(paramLong);
    }

    private void step5() {
        byte b;
        for (b = 1; b <= this.N; ) {
            this.A[this.k][b] = this.A[1][b];
            b++;
        }

        if (this.k != 2) {
            for (byte b1 = 2; b1 <= this.k1; b1++) {

                byte b2 = 1;
                for (b = 1; b <= this.N; b++) {

                    if (this.A[this.k][b] == b1) {
                        this.A[this.k][b] = this.A[b1][b2];
                        b2++;
                    }
                }
            }
        }
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            int[] arrayOfInt = {2, 2, 1};

            MultiSetPermutations multiSetPermutations = new MultiSetPermutations(3);
            System.out.println("Number of permutations = " + multiSetPermutations.countSelections());

            System.out.println("All permutations");
            while (multiSetPermutations.hasNext()) {

                MultiSetPermutation multiSetPermutation = multiSetPermutations.nextPermutation();
                System.out.println(multiSetPermutation.toString());
            }
            multiSetPermutations.reset();
            System.out.println("All permutations");
            while (multiSetPermutations.hasNext()) {

                MultiSetPermutation multiSetPermutation = multiSetPermutations.nextPermutation();
                System.out.println(multiSetPermutation.toString());
            }
        }
    }
}

