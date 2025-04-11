package jsc.curvefitting;

import jsc.ci.AbstractConfidenceInterval;
import jsc.ci.ConfidenceInterval;
import jsc.datastructures.PairedData;
import jsc.distributions.StudentsT;

public class LineFitThruOrigin
        implements StraightLineFit {
    private final int n;
    private final double sxx;
    private final PairedData data;
    private double b;
    private double chi2;
    private double ax;
    private double ay;

    public LineFitThruOrigin(PairedData paramPairedData) {
        this.data = paramPairedData;
        this.n = paramPairedData.getN();
        double[] arrayOfDouble1 = paramPairedData.getX();
        double[] arrayOfDouble2 = paramPairedData.getY();

        this.b = 0.0D;

        double d1 = 0.0D;
        this.ay = 0.0D;
        this.ax = 0.0D;
        double d2 = 0.0D;
        byte b;
        for (b = 0; b < this.n; b++) {
            this.ax += arrayOfDouble1[b];
            this.ay += arrayOfDouble2[b];
        }
        this.ax /= this.n;
        this.ay /= this.n;

        for (b = 0; b < this.n; b++) {

            double d3 = arrayOfDouble1[b] - this.ax;
            double d4 = arrayOfDouble2[b] - this.ay;
            d2 += d3 * d3;
            d1 += d3 * d4;
        }

        if (d2 <= 0.0D)
            throw new IllegalArgumentException("X data are constant.");
        this.sxx = d2 + this.n * this.ax * this.ax;
        this.b = (d1 + this.n * this.ax * this.ay) / this.sxx;

        this.chi2 = 0.0D;
        for (b = 0; b < this.n; b++) {

            double d = arrayOfDouble2[b] - this.b * arrayOfDouble1[b];
            this.chi2 += d * d;
        }
    }

    public double getA() {
        return 0.0D;
    }

    public double getB() {
        return this.b;
    }

    public ConfidenceInterval getCIB(double paramDouble) {
        if (paramDouble <= 0.0D || paramDouble >= 1.0D)
            throw new IllegalArgumentException("Invalid confidence coefficient.");
        if (this.n < 2)
            throw new IllegalArgumentException("Insufficient data for CI.");
        double d1 = getQuantileOfT(paramDouble);
        double d2 = this.chi2 / (this.n - 1);
        double d3 = Math.sqrt(d2 / this.sxx);

        double d4 = d1 * d3;
        return (ConfidenceInterval) new AbstractConfidenceInterval(paramDouble, this.b - d4, this.b + d4);
    }

    public double[][] getIntervals(double paramDouble1, int paramInt, double paramDouble2, double paramDouble3) {
        if (paramDouble1 <= 0.0D || paramDouble1 >= 1.0D)
            throw new IllegalArgumentException("Invalid confidence coefficient");
        if (this.n < 2)
            throw new IllegalArgumentException("Insufficient data for CI.");
        if (paramDouble3 <= paramDouble2)
            throw new IllegalArgumentException("Invalid x values.");
        double d1 = getQuantileOfT(paramDouble1);
        double d2 = Math.sqrt(this.chi2 / (this.n - 1));
        double[][] arrayOfDouble = new double[paramInt][5];
        double d3 = (paramDouble3 - paramDouble2) / (paramInt - 1.0D);
        double d4 = d1 * d2;

        for (byte b = 0; b < paramInt; b++) {

            double d9 = paramDouble2 + b * d3;
            double d5 = this.b * d9;
            double d6 = d9 * d9 / this.sxx;
            double d7 = d4 * Math.sqrt(d6);
            double d8 = d4 * Math.sqrt(d6 + 1.0D);
            arrayOfDouble[b][0] = d9;
            arrayOfDouble[b][1] = d5 - d7;
            arrayOfDouble[b][2] = d5 + d7;
            arrayOfDouble[b][3] = d5 - d8;
            arrayOfDouble[b][4] = d5 + d8;
        }
        return arrayOfDouble;
    }

    public double getMeanX() {
        return this.ax;
    }

    public double getMeanY() {
        return this.ay;
    }

    public int getN() {
        return this.n;
    }

    double getQuantileOfT(double paramDouble) {
        StudentsT studentsT = new StudentsT((this.n - 1));
        double d = 1.0D - paramDouble;
        return studentsT.inverseCdf(1.0D - 0.5D * d);
    }

    public double getSumOfSquares() {
        return this.chi2;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble1 = {9.5D, 9.8D, 5.0D, 19.0D, 23.0D, 14.6D, 15.2D, 8.3D, 11.4D, 21.6D, 11.8D, 26.5D, 12.1D, 4.8D, 22.0D, 21.7D, 28.2D, 18.0D, 12.1D, 28.0D};

            double[] arrayOfDouble2 = {10.7D, 11.7D, 6.5D, 25.6D, 29.4D, 16.3D, 17.2D, 9.5D, 18.4D, 28.8D, 19.7D, 31.2D, 16.6D, 6.5D, 29.0D, 25.7D, 40.5D, 26.5D, 14.2D, 33.1D};

            LineFitThruOrigin lineFitThruOrigin = new LineFitThruOrigin(new PairedData(arrayOfDouble1, arrayOfDouble2));
            System.out.println("n = " + lineFitThruOrigin.getN());
            System.out.println("b = " + lineFitThruOrigin.getB());
            System.out.println("rss = " + lineFitThruOrigin.getSumOfSquares());
            ConfidenceInterval confidenceInterval = lineFitThruOrigin.getCIB(0.9D);
            System.out.println("CI for b = [" + confidenceInterval.getLowerLimit() + ", " + confidenceInterval.getUpperLimit() + "]");
        }
    }
}

