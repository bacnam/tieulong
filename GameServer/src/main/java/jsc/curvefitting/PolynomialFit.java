package jsc.curvefitting;

import Jama.Matrix;
import jsc.datastructures.PairedData;
import jsc.distributions.Normal;
import jsc.util.Polynomial;

public class PolynomialFit
        extends GeneralLinearLeastSquares {
    private final Polynomial poly;

    public PolynomialFit(PairedData paramPairedData, double[] paramArrayOfdouble, int paramInt) {
        super(paramPairedData, paramArrayOfdouble, paramInt + 1, new PolynomialFunctionVector(paramInt));
        this.poly = new Polynomial(getA());
    }

    public PolynomialFit(PairedData paramPairedData, int paramInt) {
        this(paramPairedData, (double[]) null, paramInt);
    }

    public Polynomial getPolynomial() {
        return this.poly;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            byte b1 = 20;

            double[] arrayOfDouble1 = new double[b1];
            double[] arrayOfDouble2 = new double[b1];

            double[] arrayOfDouble3 = {1.0D, 2.0D, 3.0D, -1.0D, 0.5D};
            int i = arrayOfDouble3.length;
            int j = i - 1;
            Normal normal = new Normal(0.0D, 0.01D);

            for (byte b2 = 0; b2 < b1; b2++) {

                arrayOfDouble1[b2] = 1.0D + b2;
                double d;
                byte b;
                for (d = 1.0D, b = 1; b <= j; ) {
                    d += arrayOfDouble3[b] * Math.pow(arrayOfDouble1[b2], b);
                    b++;
                }
                arrayOfDouble2[b2] = d + normal.random();
            }
            PolynomialFit polynomialFit = new PolynomialFit(new PairedData(arrayOfDouble1, arrayOfDouble2), j);
            Polynomial polynomial = polynomialFit.getPolynomial();
            for (byte b3 = 0; b3 < polynomialFit.getM(); b3++)
                System.out.println("a(" + b3 + ") = " + polynomial.getCoefficient(b3));
            System.out.println("Sum of squares = " + polynomialFit.getSumOfSquares());
            System.out.println("\nCovariance matrix");
            Matrix matrix = polynomialFit.getCovariance();
            matrix.print(12, 4);
        }
    }
}

