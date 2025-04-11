package jsc.numerical;

public class Spline {
    private int n;
    private double[] x;
    private double[] y;
    private double[] y2;

    public Spline(int paramInt, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
        double[] arrayOfDouble = new double[paramInt + 1];

        this.x = new double[paramInt + 1];
        this.y = new double[paramInt + 1];
        this.y2 = new double[paramInt + 1];

        this.n = paramInt;
        this.x[0] = Double.NEGATIVE_INFINITY;
        byte b;
        for (b = 0; b < this.n; b++) {

            this.x[b + 1] = paramArrayOfdouble1[b];
            if (this.x[b + 1] <= this.x[b])
                throw new IllegalArgumentException("x values not in ascending order or distinct.");
            this.y[b + 1] = paramArrayOfdouble2[b];
        }

        this.y2[1] = 0.0D;
        arrayOfDouble[1] = 0.0D;

        for (b = 2; b <= this.n - 1; b++) {

            double d4 = (this.x[b] - this.x[b - 1]) / (this.x[b + 1] - this.x[b - 1]);
            double d3 = d4 * this.y2[b - 1] + 2.0D;
            this.y2[b] = (d4 - 1.0D) / d3;
            arrayOfDouble[b] = (this.y[b + 1] - this.y[b]) / (this.x[b + 1] - this.x[b]) - (this.y[b] - this.y[b - 1]) / (this.x[b] - this.x[b - 1]);
            arrayOfDouble[b] = (6.0D * arrayOfDouble[b] / (this.x[b + 1] - this.x[b - 1]) - d4 * arrayOfDouble[b - 1]) / d3;
        }
        double d2 = 0.0D, d1 = d2;
        this.y2[this.n] = (d2 - d1 * arrayOfDouble[this.n - 1]) / (d1 * this.y2[this.n - 1] + 1.0D);
        int i = this.n - 1;
    }

    public double getMaxX() {
        return this.x[this.n];
    }

    public double getMinX() {
        return this.x[1];
    }

    public int getN() {
        return this.n;
    }

    public double getX(int paramInt) {
        return this.x[paramInt + 1];
    }

    public double getY(int paramInt) {
        return this.y[paramInt + 1];
    }

    public double[] evalDerivative(double paramDouble) {
        double[] arrayOfDouble = new double[2];

        int i = 1;
        int j = this.n;
        while (j - i > 1) {
            int k = j + i >> 1;
            if (this.x[k] > paramDouble) {
                j = k;
                continue;
            }
            i = k;
        }
        double d1 = this.x[j] - this.x[i];
        if (d1 == 0.0D)
            throw new IllegalArgumentException("Cannot evaluate spline at " + paramDouble);
        double d3 = (this.x[j] - paramDouble) / d1;
        double d2 = (paramDouble - this.x[i]) / d1;

        double d4 = d3 * d3;
        double d5 = d2 * d2;
        arrayOfDouble[0] = d3 * this.y[i] + d2 * this.y[j] + ((d4 * d3 - d3) * this.y2[i] + (d5 * d2 - d2) * this.y2[j]) * d1 * d1 / 6.0D;
        arrayOfDouble[1] = (this.y[j] - this.y[i]) / d1 + ((3.0D * d5 - 1.0D) * this.y2[j] - (3.0D * d4 - 1.0D) * this.y2[i]) * d1 / 6.0D;
        return arrayOfDouble;
    }

    public double splint(double paramDouble) {
        int i = 1;
        int j = this.n;
        while (j - i > 1) {
            int k = j + i >> 1;
            if (this.x[k] > paramDouble) {
                j = k;
                continue;
            }
            i = k;
        }
        double d1 = this.x[j] - this.x[i];
        if (d1 == 0.0D)
            throw new IllegalArgumentException("Cannot evaluate spline at " + paramDouble);
        double d3 = (this.x[j] - paramDouble) / d1;
        double d2 = (paramDouble - this.x[i]) / d1;
        return d3 * this.y[i] + d2 * this.y[j] + ((d3 * d3 * d3 - d3) * this.y2[i] + (d2 * d2 * d2 - d2) * this.y2[j]) * d1 * d1 / 6.0D;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = 'Ϩ';
            double[] arrayOfDouble1 = new double[c];
            double[] arrayOfDouble2 = new double[c];
            for (byte b = 0; b < c; b++) {

                arrayOfDouble1[b] = b;
                arrayOfDouble2[b] = Math.sin(b);
            }

            Spline spline = new Spline(c, arrayOfDouble1, arrayOfDouble2);
            double d = 3.5D;
            System.out.println("f(" + d + ") = " + spline.splint(d));
        }
    }
}

