package jsc.util;

public class Polynomial {
    private final int degree;
    private final double[] p;
    private final double[] q;
    private final double[] r;

    public Polynomial(double[] paramArrayOfdouble) {
        int i = paramArrayOfdouble.length;
        this.degree = i - 1;
        this.p = new double[i];
        this.q = new double[this.degree];
        this.r = new double[this.degree - 1];
        for (byte b = 0; b < i; ) {
            this.p[b] = paramArrayOfdouble[b];
            b++;
        }

    }

    public double eval(double paramDouble) {
        this.q[this.degree - 1] = this.p[this.degree];
        for (int i = this.degree - 1; i >= 1; i--)
            this.q[i - 1] = this.p[i] + paramDouble * this.q[i];
        return this.p[0] + paramDouble * this.q[0];
    }

    public double[] evalDerivative(double paramDouble) {
        double[] arrayOfDouble = new double[2];
        arrayOfDouble[0] = eval(paramDouble);
        if (this.degree == 1) {
            arrayOfDouble[1] = this.p[1];
        } else {

            this.r[this.degree - 2] = this.q[this.degree - 1];
            for (int i = this.degree - 2; i >= 1; i--)
                this.r[i - 1] = this.q[i] + paramDouble * this.r[i];
            arrayOfDouble[1] = this.q[0] + paramDouble * this.r[0];
        }
        return arrayOfDouble;
    }

    public double getCoefficient(int paramInt) {
        return this.p[paramInt];
    }

    public int getDegree() {
        return this.degree;
    }
}

