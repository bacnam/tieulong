package jsc.goodnessfit;

import jsc.util.Arrays;
import jsc.util.Sort;

public class SampleDistributionFunction {
    private int n;
    private double[] x;
    private double[] S;
    private double[] orderedX;
    private double[] orderedS;

    public SampleDistributionFunction(double[] paramArrayOfdouble) {
        this.n = paramArrayOfdouble.length;
        if (this.n < 2) {
            throw new IllegalArgumentException("Less than 2 observations.");
        }
        this.x = paramArrayOfdouble;

        this.orderedX = new double[this.n];
        this.S = new double[this.n];
        this.orderedS = new double[this.n];
        int[] arrayOfInt = Arrays.sequence(this.n);
        System.arraycopy(paramArrayOfdouble, 0, this.orderedX, 0, this.n);

        Sort.sort(this.orderedX, arrayOfInt, 0, this.n - 1, true);

        this.orderedS[this.n - 1] = 1.0D;
        int i;
        for (i = this.n - 2; i >= 0; i--) {
            if (this.orderedX[i] == this.orderedX[i + 1]) {
                this.orderedS[i] = this.orderedS[i + 1];
            } else {
                this.orderedS[i] = (1.0D + i) / this.n;
            }
        }
        for (i = 0; i < this.n; ) {
            this.S[arrayOfInt[i]] = this.orderedS[i];
            i++;
        }

    }

    public double getMinX() {
        return this.orderedX[0];
    }

    public double getMaxX() {
        return this.orderedX[this.n - 1];
    }

    public int getN() {
        return this.n;
    }

    public double getS(int paramInt) {
        return this.S[paramInt];
    }

    public double getX(int paramInt) {
        return this.x[paramInt];
    }

    public double getOrderedS(int paramInt) {
        return this.orderedS[paramInt];
    }

    public double getOrderedX(int paramInt) {
        return this.orderedX[paramInt];
    }

    public double[] getS() {
        return this.S;
    }

    public double[] getX() {
        return this.x;
    }

    public double[] getOrderedS() {
        return this.orderedS;
    }

    public double[] getOrderedX() {
        return this.orderedX;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\nSample distribution function\n");
        stringBuffer.append("x\tS(x)");
        for (byte b = 0; b < getN(); b++)
            stringBuffer.append("\n" + getOrderedX(b) + "\t" + getOrderedS(b));
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble = {72.2D, 64.0D, 53.4D, 76.8D, 86.3D, 58.1D, 63.2D, 73.1D, 78.0D, 44.3D, 85.1D, 66.6D, 80.4D, 76.0D, 68.8D, 76.8D, 58.9D, 58.1D, 74.9D, 72.2D, 73.1D, 39.3D, 52.8D, 54.2D, 65.3D, 74.0D, 63.2D, 64.7D, 68.8D, 85.1D, 62.2D, 76.0D, 70.5D, 48.9D, 78.0D, 66.6D, 58.1D, 32.5D, 63.2D, 64.0D, 68.8D, 65.3D, 71.9D, 72.2D, 63.2D, 72.2D, 70.5D, 80.4D, 45.4D, 59.6D};

            SampleDistributionFunction sampleDistributionFunction = new SampleDistributionFunction(arrayOfDouble);
            System.out.println(sampleDistributionFunction.toString());
        }
    }
}

