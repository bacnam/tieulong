package jsc.combinatorics;

import jsc.util.Maths;

import java.util.NoSuchElementException;
import java.util.Random;

public class Permutations
        implements Enumerator {
    private final double permutationCount;
    private boolean hasNext;
    private long count;
    private Random rand;
    private boolean firstCall;
    private int n;
    private int[] pi;
    private int[] rho;
    private int[] randPerm;

    public Permutations(int paramInt) {
        this.n = paramInt;

        this.permutationCount = Maths.factorial(paramInt);

        if (paramInt < 1)
            throw new IllegalArgumentException("Less than one object.");
        this.pi = new int[paramInt + 1];
        this.rho = new int[paramInt + 1];

        this.rand = new Random();
        this.randPerm = new int[paramInt];
        for (byte b = 0; b < paramInt; ) {
            this.randPerm[b] = b + 1;
            b++;
        }
        reset();
    }

    public double countSelections() {
        return this.permutationCount;
    }

    public int getN() {
        return this.n;
    }

    private Permutation getPermutation() {
        int[] arrayOfInt = new int[this.n];
        for (byte b = 1; b <= this.n; ) {
            arrayOfInt[b - 1] = this.pi[b];
            b++;
        }
        if (this.count > this.permutationCount) this.hasNext = false;
        return new Permutation(arrayOfInt, false);
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    public Permutation nextPermutation() {
        this.pi[0] = 0;
        if (this.firstCall) {

            this.firstCall = false;
            this.count++;
            return getPermutation();
        }
        int i = this.n - 1;
        for (; this.pi[i + 1] < this.pi[i]; i--) ;
        if (i == 0) throw new NoSuchElementException();
        int j = this.n;
        for (; this.pi[j] < this.pi[i]; j--) ;
        int k = this.pi[j];
        this.pi[j] = this.pi[i];
        this.pi[i] = k;
        int m;
        for (m = i + 1; m <= this.n; ) {
            this.rho[m] = this.pi[m];
            m++;
        }
        for (m = i + 1; m <= this.n; ) {
            this.pi[m] = this.rho[this.n + i + 1 - m];
            m++;
        }
        this.count++;
        return getPermutation();
    }

    public Selection nextSelection() {
        return nextPermutation();
    }

    public Permutation randomPermutation() {
        for (byte b = 0; b < this.n; b++) {

            int i = b + this.rand.nextInt(this.n - b);

            int j = this.randPerm[i];
            this.randPerm[i] = this.randPerm[b];
            this.randPerm[b] = j;
        }
        return new Permutation(this.randPerm, false);
    }

    public Selection randomSelection() {
        return randomPermutation();
    }

    public void reset() {
        for (byte b = 1; b <= this.n; ) {
            this.pi[b] = b;
            b++;
        }
        this.firstCall = true;
        this.count = 1L;
        this.hasNext = true;
    }

    public void setSeed(long paramLong) {
        this.rand.setSeed(paramLong);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            byte b = 3;
            Permutations permutations = new Permutations(b);
            int i = (int) permutations.countSelections();
            System.out.println("Number of permutations = " + i);

            System.out.println("All permutations");
            while (permutations.hasNext()) {

                Permutation permutation = permutations.nextPermutation();
                System.out.println(permutation.toString());
            }
            permutations.reset();
            System.out.println("All permutations");
            while (permutations.hasNext()) {

                Permutation permutation = permutations.nextPermutation();
                System.out.println(permutation.toString());
            }
        }
    }
}

