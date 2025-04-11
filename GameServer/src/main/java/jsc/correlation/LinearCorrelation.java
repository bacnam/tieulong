package jsc.correlation;

import jsc.ci.ConfidenceInterval;
import jsc.datastructures.PairedData;
import jsc.distributions.Normal;
import jsc.distributions.StudentsT;
import jsc.onesample.Ztest;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;

public class LinearCorrelation
        implements SignificanceTest, ConfidenceInterval {
    private final int n;
    private final double r;
    private double confidenceCoeff;
    private double lowerLimit;
    private double upperLimit;
    private double SP;

    public LinearCorrelation(PairedData paramPairedData, double paramDouble1, H1 paramH1, double paramDouble2) {
        this.n = paramPairedData.getN();
        this.r = correlationCoeff(paramPairedData);

        if (paramDouble1 == 0.0D) {

            double d1, d2 = 0.0D;
            if (this.r == 1.0D) {
                d1 = Double.POSITIVE_INFINITY;
            } else if (this.r == -1.0D) {
                d1 = Double.NEGATIVE_INFINITY;
            } else {

                d1 = this.r * Math.sqrt((this.n - 2.0D) / (1.0D - this.r * this.r));
                d2 = StudentsT.tailProb(d1, (this.n - 2));
            }

            if (paramH1 == H1.NOT_EQUAL) {
                this.SP = d2 + d2;
            } else if (paramH1 == H1.LESS_THAN) {
                this.SP = (d1 < 0.0D) ? d2 : (1.0D - d2);
            } else {
                this.SP = (d1 > 0.0D) ? d2 : (1.0D - d2);
            }

        } else {

            if (paramDouble1 <= -1.0D || paramDouble1 >= 1.0D) {
                throw new IllegalArgumentException("Invalid null hypothesis.");
            }

            if (Math.abs(this.r) < 1.0D) {

                if (this.n < 4)
                    throw new IllegalArgumentException("Need more than 3 observations to use Fisher's z transformation.");
                double d = 0.5D * Math.sqrt(this.n - 3.0D) * Math.log((1.0D + this.r) / (1.0D - this.r) * (1.0D + paramDouble1) / (1.0D - paramDouble1));

                this.SP = Ztest.getSP(d, paramH1);
            } else {
                double d;

                if (this.r == 1.0D) {
                    d = Double.POSITIVE_INFINITY;
                } else {
                    d = Double.NEGATIVE_INFINITY;
                }
                if (paramH1 == H1.NOT_EQUAL) {
                    this.SP = 0.0D;
                } else if (paramH1 == H1.LESS_THAN) {
                    this.SP = (d < 0.0D) ? 0.0D : 1.0D;
                } else {
                    this.SP = (d > 0.0D) ? 0.0D : 1.0D;
                }
            }
        }

        setConfidenceCoeff(paramDouble2);
    }

    public LinearCorrelation(PairedData paramPairedData, double paramDouble, H1 paramH1) {
        this(paramPairedData, paramDouble, paramH1, 0.95D);
    }

    public LinearCorrelation(PairedData paramPairedData, double paramDouble) {
        this(paramPairedData, paramDouble, H1.NOT_EQUAL, 0.95D);
    }

    public LinearCorrelation(PairedData paramPairedData) {
        this(paramPairedData, 0.0D, H1.NOT_EQUAL, 0.95D);
    }

    public static double correlationCoeff(PairedData paramPairedData) {
        int i = paramPairedData.getN();
        double[] arrayOfDouble1 = paramPairedData.getX();
        double[] arrayOfDouble2 = paramPairedData.getY();

        double d1 = 0.0D, d2 = 0.0D, d3 = 0.0D, d4 = 0.0D, d5 = 0.0D;
        byte b;
        for (b = 0; b < i; b++) {
            d5 += arrayOfDouble1[b];
            d4 += arrayOfDouble2[b];
        }
        d5 /= i;
        d4 /= i;
        for (b = 0; b < i; b++) {

            double d6 = arrayOfDouble1[b] - d5;
            double d7 = arrayOfDouble2[b] - d4;
            d3 += d6 * d6;
            d1 += d7 * d7;
            d2 += d6 * d7;
        }

        if (d3 <= 0.0D)
            throw new IllegalArgumentException("X data are constant.");
        if (d1 <= 0.0D)
            throw new IllegalArgumentException("Y data are constant.");
        return d2 / Math.sqrt(d3 * d1);
    }

    public int getN() {
        return this.n;
    }

    public double getR() {
        return this.r;
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.r;
    }

    public double getLowerLimit() {
        return this.lowerLimit;
    }

    public double getUpperLimit() {
        return this.upperLimit;
    }

    public double getConfidenceCoeff() {
        return this.confidenceCoeff;
    }

    public void setConfidenceCoeff(double paramDouble) {
        if (paramDouble < 0.0D || paramDouble > 1.0D)
            throw new IllegalArgumentException("Invalid confidence coefficient.");
        this.confidenceCoeff = paramDouble;
        double d1 = 0.5D * Math.log((1.0D + this.r) / (1.0D - this.r));
        Normal normal = new Normal();
        double d2 = 1.0D - paramDouble;
        double d3 = normal.inverseCdf(1.0D - 0.5D * d2);
        if (this.n < 4)
            throw new IllegalArgumentException("Need more than 3 observations to calculate confidence interval.");
        double d4 = d3 / Math.sqrt(this.n - 3.0D);

        double d5 = Math.exp(2.0D * (d1 - d4));
        double d6 = Math.exp(2.0D * (d1 + d4));
        this.lowerLimit = (d5 - 1.0D) / (d5 + 1.0D);
        this.upperLimit = (d6 - 1.0D) / (d6 + 1.0D);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble1 = {8.000001D, 8.000003D, 8.000002D, 8.000004D, 8.000005D};
            double[] arrayOfDouble2 = {8.0D, 9.0D, 10.0D, 11.0D, 12.0D};

            double[] arrayOfDouble3 = {0.8D, 1.7D, 2.4D, 0.9D, 1.2D, 1.6D, 1.7D, 2.9D};
            double[] arrayOfDouble4 = {1.3D, 3.3D, 3.8D, 1.1D, 2.4D, 3.1D, 3.5D, 3.9D};

            LinearCorrelation linearCorrelation = new LinearCorrelation(new PairedData(arrayOfDouble3, arrayOfDouble4), 0.0D, H1.GREATER_THAN, 0.95D);
            System.out.println("n = " + linearCorrelation.getN() + " r = " + linearCorrelation.getR() + " SP = " + linearCorrelation.getSP());
            System.out.println("CI = [" + linearCorrelation.getLowerLimit() + ", " + linearCorrelation.getUpperLimit() + "]");
            linearCorrelation = new LinearCorrelation(new PairedData(arrayOfDouble3, arrayOfDouble4), 0.0D, H1.NOT_EQUAL, 0.95D);
            System.out.println("n = " + linearCorrelation.getN() + " r = " + linearCorrelation.getR() + " SP = " + linearCorrelation.getSP());
            System.out.println("CI = [" + linearCorrelation.getLowerLimit() + ", " + linearCorrelation.getUpperLimit() + "]");

            double[] arrayOfDouble5 = {8.2D, 9.6D, 7.0D, 9.4D, 10.9D, 7.1D, 9.0D, 6.6D, 8.4D, 10.5D};
            double[] arrayOfDouble6 = {8.7D, 9.6D, 6.9D, 8.5D, 11.3D, 7.6D, 9.2D, 6.3D, 8.4D, 12.3D};
            linearCorrelation = new LinearCorrelation(new PairedData(arrayOfDouble5, arrayOfDouble6), 0.0D, H1.NOT_EQUAL, 0.95D);
            System.out.println("n = " + linearCorrelation.getN() + " r = " + linearCorrelation.getR() + " SP = " + linearCorrelation.getSP());
            System.out.println("CI = [" + linearCorrelation.getLowerLimit() + ", " + linearCorrelation.getUpperLimit() + "]");

            double[] arrayOfDouble7 = {0.0D, 4.0D, 6.0D, 8.0D, 12.0D, 14.0D, 16.0D, 22.0D, 26.0D};
            double[] arrayOfDouble8 = {11.0D, 13.0D, 8.0D, 4.0D, 7.0D, 6.0D, 3.0D, 2.0D, 0.0D};
            linearCorrelation = new LinearCorrelation(new PairedData(arrayOfDouble7, arrayOfDouble8), 0.0D, H1.NOT_EQUAL, 0.99D);
            System.out.println("n = " + linearCorrelation.getN() + " r = " + linearCorrelation.getR() + " SP = " + linearCorrelation.getSP());
            System.out.println("CI = [" + linearCorrelation.getLowerLimit() + ", " + linearCorrelation.getUpperLimit() + "]");
        }
    }
}

