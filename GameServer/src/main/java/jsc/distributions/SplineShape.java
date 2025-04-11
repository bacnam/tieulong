package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.numerical.Function;
import jsc.numerical.Integration;
import jsc.numerical.NumericalException;
import jsc.numerical.Spline;
import jsc.tests.H1;

public class SplineShape
        extends AbstractContinuousDistribution {
    double pdfMax;
    Spline spline;
    double mean;
    double variance;

    public SplineShape(int paramInt, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) throws NumericalException {
        this(new Spline(paramInt, paramArrayOfdouble1, paramArrayOfdouble2));
    }

    public SplineShape(Spline paramSpline) throws NumericalException {
        super(paramSpline.getMinX(), paramSpline.getMaxX(), false);
        int i = paramSpline.getN();
        this.minX = paramSpline.getMinX();
        this.maxX = paramSpline.getMaxX();
        this.spline = paramSpline;

        SplineCurve splineCurve = new SplineCurve(this);
        double d = Integration.romberg(splineCurve, this.minX, this.maxX, this.tolerance, 20);

        if (d <= 0.0D) {
            throw new IllegalArgumentException("Invalid spline shape.");
        }
        if (d != 1.0D) {

            double[] arrayOfDouble1 = new double[i];
            double[] arrayOfDouble2 = new double[i];
            for (byte b = 0; b < i; b++) {

                arrayOfDouble1[b] = paramSpline.getX(b);
                arrayOfDouble2[b] = paramSpline.getY(b);
                arrayOfDouble2[b] = arrayOfDouble2[b] / d;
            }
            this.spline = new Spline(i, arrayOfDouble1, arrayOfDouble2);
        }

        calculateMaxPdf();

        this.mean = Integration.romberg(new SplineMean(this), this.minX, this.maxX, this.tolerance, 20);
        this.variance = Integration.romberg(new SplineVar(this), this.minX, this.maxX, this.tolerance, 20);
    }

    private void calculateMaxPdf() {
        byte b1 = 100;
        double d1 = (this.maxX - this.minX) / b1;
        this.pdfMax = 0.0D;

        double d2 = this.minX;
        for (byte b2 = 0; b2 <= b1; b2++) {

            d2 = this.minX + b2 * d1;
            double d = this.spline.splint(d2);
            if (d > this.pdfMax) this.pdfMax = d;

        }
    }

    public Spline getSpline() {
        return this.spline;
    }

    public double mean() {
        return this.mean;
    }

    public double pdf(double paramDouble) {
        if (paramDouble < this.minX || paramDouble > this.maxX)
            throw new IllegalArgumentException("Invalid variate-value.");
        return this.spline.splint(paramDouble);
    }

    public double random() {
        double d1;
        double d2;
        do {
            d1 = this.minX + (this.maxX - this.minX) * this.rand.nextDouble();
            d2 = this.pdfMax * this.rand.nextDouble();
        } while (d2 > this.spline.splint(d1));

        return d1;
    }

    public double variance() {
        return this.variance;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) throws NumericalException {
            byte b2 = 11;
            double[] arrayOfDouble1 = {0.0D, 0.1D, 0.2D, 0.3D, 0.4D, 0.5D, 0.6D, 0.7D, 0.8D, 0.9D, 1.0D};
            double[] arrayOfDouble2 = new double[b2];
            double d1 = 2.0D;
            double d2 = 3.0D;
            Beta beta = new Beta(d1, d2);
            byte b1;
            for (b1 = 0; b1 < b2; b1++) {
                arrayOfDouble2[b1] = 2.0D * beta.pdf(arrayOfDouble1[b1]);
            }

            SplineShape splineShape = new SplineShape(b2, arrayOfDouble1, arrayOfDouble2);

            char c = 'Ϩ';
            double[] arrayOfDouble3 = new double[c];
            for (b1 = 0; b1 < c; b1++) {
                arrayOfDouble3[b1] = splineShape.random();
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble3, splineShape, H1.NOT_EQUAL, true);
            System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }

    class SplineCurve implements Function {
        private final SplineShape this$0;

        SplineCurve(SplineShape this$0) {
            this.this$0 = this$0;
        }

        public double function(double param1Double) {
            return this.this$0.spline.splint(param1Double);
        }
    }

    class SplineMean implements Function {
        private final SplineShape this$0;

        SplineMean(SplineShape this$0) {
            this.this$0 = this$0;
        }

        public double function(double param1Double) {
            return param1Double * this.this$0.spline.splint(param1Double);
        }
    }

    class SplineVar implements Function {
        private final SplineShape this$0;

        SplineVar(SplineShape this$0) {
            this.this$0 = this$0;
        }

        public double function(double param1Double) {
            double d = param1Double - this.this$0.mean;
            return d * d * this.this$0.spline.splint(param1Double);
        }
    }
}

