package jsc.curvefitting;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import jsc.datastructures.PairedData;
import jsc.distributions.Normal;

public class GeneralLinearLeastSquares {
    private final int n;
    private final int ma;
    private final double[] weight;
    private final FunctionVector fv;
    private double[] a;
    private Matrix cvm;
    private double chisq;

    public GeneralLinearLeastSquares(PairedData paramPairedData, double[] paramArrayOfdouble, int paramInt, FunctionVector paramFunctionVector) {
        this.n = paramPairedData.getN();

        this.ma = paramInt;
        if (this.n < paramInt)
            throw new IllegalArgumentException("Insufficient data to estimate model.");
        if (paramArrayOfdouble == null) {

            this.weight = new double[this.n];
            for (byte b = 0; b < this.n; ) {
                this.weight[b] = 1.0D;
                b++;
            }

        } else {

            if (this.n != paramArrayOfdouble.length)
                throw new IllegalArgumentException("Weights array is wrong length.");
            this.weight = paramArrayOfdouble;
        }

        this.fv = paramFunctionVector;
        svdfit(paramPairedData.getX(), paramPairedData.getY());
    }

    public GeneralLinearLeastSquares(PairedData paramPairedData, int paramInt, FunctionVector paramFunctionVector) {
        this(paramPairedData, null, paramInt, paramFunctionVector);
    }

    public double[] getA() {
        return this.a;
    }

    public Matrix getCovariance() {
        return this.cvm;
    }

    public int getM() {
        return this.ma;
    }

    public int getN() {
        return this.n;
    }

    public double getSumOfSquares() {
        return this.chisq;
    }

    private double[] svbksb(Matrix paramMatrix1, double[] paramArrayOfdouble1, Matrix paramMatrix2, double[] paramArrayOfdouble2) {
        int i = paramMatrix1.getColumnDimension();
        int j = paramMatrix1.getRowDimension();

        double[] arrayOfDouble1 = new double[i];
        byte b;
        for (b = 0; b < i; b++) {

            double d = 0.0D;
            if (paramArrayOfdouble1[b] != 0.0D) {

                for (byte b1 = 0; b1 < j; ) {
                    d += paramMatrix1.get(b1, b) * paramArrayOfdouble2[b1];
                    b1++;
                }
                d /= paramArrayOfdouble1[b];
            }
            arrayOfDouble1[b] = d;
        }

        double[] arrayOfDouble2 = new double[i];
        for (b = 0; b < i; b++) {

            double d = 0.0D;
            for (byte b1 = 0; b1 < i; ) {
                d += paramMatrix2.get(b, b1) * arrayOfDouble1[b1];
                b1++;
            }
            arrayOfDouble2[b] = d;
        }
        return arrayOfDouble2;
    }

    private void svdfit(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
        double d3 = this.n * Double.MIN_VALUE;

        double[] arrayOfDouble1 = new double[this.n];

        Matrix matrix1 = new Matrix(this.n, this.ma);
        byte b2;
        for (b2 = 0; b2 < this.n; b2++) {

            double[] arrayOfDouble = this.fv.function(paramArrayOfdouble1[b2]);
            double d = this.weight[b2];
            for (byte b = 0; b < this.ma; ) {
                matrix1.set(b2, b, arrayOfDouble[b] * d);
                b++;
            }
            arrayOfDouble1[b2] = paramArrayOfdouble2[b2] * d;
        }

        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(matrix1);
        double[] arrayOfDouble2 = singularValueDecomposition.getSingularValues();
        Matrix matrix2 = singularValueDecomposition.getV();

        double d1 = 0.0D;
        byte b1;
        for (b1 = 0; b1 < this.ma; b1++) {
            if (arrayOfDouble2[b1] > d1) d1 = arrayOfDouble2[b1];
        }
        double d2 = d3 * d1;
        for (b1 = 0; b1 < this.ma; b1++) {
            if (arrayOfDouble2[b1] < d2) arrayOfDouble2[b1] = 0.0D;
        }
        matrix1 = singularValueDecomposition.getU();
        this.a = svbksb(matrix1, arrayOfDouble2, matrix2, arrayOfDouble1);

        this.chisq = 0.0D;
        for (b2 = 0; b2 < this.n; b2++) {

            double[] arrayOfDouble = this.fv.function(paramArrayOfdouble1[b2]);
            double d5;
            for (d5 = 0.0D, b1 = 0; b1 < this.ma; ) {
                d5 += this.a[b1] * arrayOfDouble[b1];
                b1++;
            }
            double d4 = (paramArrayOfdouble2[b2] - d5) * this.weight[b2];
            this.chisq += d4 * d4;
        }

        this.cvm = new Matrix(this.ma, this.ma);
        svdvar(matrix2, arrayOfDouble2);
    }

    private void svdvar(Matrix paramMatrix, double[] paramArrayOfdouble) {
        int i = paramArrayOfdouble.length;

        double[] arrayOfDouble = new double[i];
        byte b;
        for (b = 0; b < i; b++) {

            arrayOfDouble[b] = 0.0D;
            if (paramArrayOfdouble[b] != 0.0D) arrayOfDouble[b] = 1.0D / paramArrayOfdouble[b] * paramArrayOfdouble[b];
        }
        for (b = 0; b < i; b++) {

            for (byte b1 = 0; b1 <= b; b1++) {
                byte b2;
                double d;
                for (d = 0.0D, b2 = 0; b2 < i; ) {
                    d += paramMatrix.get(b, b2) * paramMatrix.get(b1, b2) * arrayOfDouble[b2];
                    b2++;
                }
                this.cvm.set(b1, b, d);
                this.cvm.set(b, b1, d);
            }
        }
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            byte b1 = 20;

            double[] arrayOfDouble1 = new double[b1];
            double[] arrayOfDouble2 = new double[b1];

            double[] arrayOfDouble3 = {1.0D, 2.0D, 3.0D, -1.0D, 0.5D};
            int i = arrayOfDouble3.length;
            Normal normal = new Normal(0.0D, 0.01D);

            PolynomialFunctionVector polynomialFunctionVector = new PolynomialFunctionVector(i - 1);
            for (byte b2 = 0; b2 < b1; b2++) {

                arrayOfDouble1[b2] = 1.0D + b2;
                double[] arrayOfDouble = polynomialFunctionVector.function(arrayOfDouble1[b2]);
                double d;
                byte b;
                for (d = 0.0D, b = 0; b < i; ) {
                    d += arrayOfDouble3[b] * arrayOfDouble[b];
                    b++;
                }
                arrayOfDouble2[b2] = d + normal.random();
            }
            GeneralLinearLeastSquares generalLinearLeastSquares = new GeneralLinearLeastSquares(new PairedData(arrayOfDouble1, arrayOfDouble2), i, polynomialFunctionVector);
            double[] arrayOfDouble4 = generalLinearLeastSquares.getA();
            for (byte b3 = 0; b3 < generalLinearLeastSquares.getM(); b3++)
                System.out.println("a(" + b3 + ") = " + arrayOfDouble4[b3]);
            System.out.println("Sum of squares = " + generalLinearLeastSquares.getSumOfSquares());
            System.out.println("\nCovariance matrix");
            Matrix matrix = generalLinearLeastSquares.getCovariance();
            matrix.print(12, 4);
        }
    }
}

