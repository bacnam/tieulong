package jsc.curvefitting;

import jsc.datastructures.PairedData;
import jsc.numerical.NumericalException;

public class LpNormFit
        implements StraightLineFit {
    private final int n;
    private double a;
    private double b;
    private double p;
    private double sd;
    private double[] r;

    public LpNormFit(double paramDouble1, PairedData paramPairedData, double paramDouble2, int paramInt) throws NumericalException {
        double d1 = 0.0D, d2 = 0.0D;

        this.n = paramPairedData.getN();
        double[] arrayOfDouble1 = paramPairedData.getX();
        double[] arrayOfDouble2 = paramPairedData.getY();
        if (this.n < 2) {
            throw new IllegalArgumentException("Less than 2 observations.");
        }
        double d4 = paramDouble1 - 2.0D;
        double d3 = 2.0D * paramDouble2;
        this.sd = 0.0D;
        this.r = new double[this.n];

        byte b1;
        for (b1 = 0; b1 < this.n; ) {
            this.r[b1] = 1.0D;
            b1++;
        }

        for (byte b2 = 1; b2 <= paramInt; b2++) {

            byte b = 0;

            double d9 = 0.0D;
            double d10 = 0.0D;
            double d11 = 0.0D;
            double d8 = 0.0D;
            double d7 = 0.0D;
            for (b1 = 0; b1 < this.n; b1++) {

                double d = Math.abs(this.r[b1]);
                if (d <= paramDouble2) {
                    b++;
                } else {

                    double d15 = Math.pow(d, d4);
                    d9 += d15;
                    double d12 = d15 / d9;
                    double d16 = arrayOfDouble1[b1] - d10;
                    double d18 = arrayOfDouble2[b1] - d11;
                    double d17 = d16 * d15;
                    double d13 = d16 * d17;
                    double d14 = d18 * d17;
                    d8 += d13 - d13 * d12;
                    d7 += d14 - d14 * d12;
                    d10 += d16 * d12;
                    d11 += d18 * d12;
                }
            }
            if (d8 < paramDouble2) {
                throw new NumericalException("Weighted sample variance of x is zero.");
            }
            this.b = d7 / d8;
            this.a = d11 - this.b * d10;

            double d6 = 0.0D;
            boolean bool = false;
            for (b1 = 0; b1 < this.n; b1++) {

                double d13 = arrayOfDouble2[b1] - this.a + this.b * arrayOfDouble1[b1];
                double d12 = Math.abs(d13);
                if (Math.abs(d12 - Math.abs(this.r[b1])) > d3) bool = true;

                d6 += Math.pow(d12, paramDouble1);
                this.r[b1] = d13;
            }

            double d5 = Math.abs(d6 - this.sd) / d6;
            if (!bool)
                return;
            if (b2 != 1) {
                if (d6 > this.sd) {

                    this.a = d1;
                    this.b = d2;
                    for (b1 = 0; b1 < this.n; b1++) {

                        this.r[b1] = arrayOfDouble2[b1] - this.a + this.b * arrayOfDouble1[b1];
                    }

                    return;
                }
            }
            this.sd = d6;
            d1 = this.a;
            d2 = this.b;
        }

        throw new NumericalException("Maximum number of iterations exceeded.");
    }

    public double getA() {
        return this.a;
    }

    public double getB() {
        return this.b;
    }

    public int getN() {
        return this.n;
    }

    public double getNorm() {
        return this.sd;
    }

    public double getP() {
        return this.b;
    }

    public double[] getResiduals() {
        return this.r;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) throws NumericalException {
            double[] arrayOfDouble1 = {0.2D, 0.5D, 0.9D, 1.4D, 2.1D, 2.7D, 3.0D, 3.6D, 4.1D, 4.4D, 5.0D, 5.6D, 6.2D, 6.6D, 7.1D};
            double[] arrayOfDouble2 = {50.0D, 48.0D, 42.0D, 36.0D, 34.0D, 32.0D, 29.0D, 31.0D, 28.0D, 24.0D, 19.0D, 17.0D, 10.0D, 12.0D, 9.5D};
            LpNormFit lpNormFit = new LpNormFit(1.0D, new PairedData(arrayOfDouble1, arrayOfDouble2), 1.0E-16D, 50);
            int i = lpNormFit.getN();
            double d1 = lpNormFit.getA();
            double d2 = lpNormFit.getB();
            System.out.println("n = " + i);
            System.out.println("a = " + d1);
            System.out.println("b = " + d2);
            System.out.println("Norm = " + lpNormFit.getNorm());
            double[] arrayOfDouble3 = lpNormFit.getResiduals();
            for (byte b = 0; b < i; b++)
                System.out.println(arrayOfDouble3[b] + " " + (arrayOfDouble2[b] - d1 + d2 * arrayOfDouble1[b]));
        }
    }
}

