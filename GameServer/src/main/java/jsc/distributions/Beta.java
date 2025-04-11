package jsc.distributions;

import jsc.util.Maths;

import java.util.Arrays;

public class Beta
        extends AbstractDistribution {
    private static final double ACU63 = 5.0E-17D;
    private static final double ACU109 = 1.0E-28D;
    private static final double LOWER = 1.0E-4D;
    private static final double UPPER = 0.9999D;
    private static final double CONST1 = 2.30753D;
    private static final double CONST2 = 0.27061D;
    private static final double CONST3 = 0.99229D;
    private static final double CONST4 = 0.04481D;
    private static final int GR = 0;
    private static final int OS = 1;
    private static final int AS134 = 2;
    private static final int ATKINSON79_5_2 = 52;
    private static final double TOL = 1.0E-14D;
    private double p;
    private double q;
    private double logB;
    private int randomMethod;
    private boolean reverseOrder = false;
    private int n;
    private double am1;
    private double bm1;
    private double arecip;
    private double brecip;
    private double t;
    private double r;
    private Gamma Gp;
    private Gamma Gq;
    private double pStar;
    private double qStar;
    private double s1;
    private double s2;

    public Beta(double paramDouble1, double paramDouble2) {
        setShape(paramDouble1, paramDouble2);
    }

    public static double incompleteBeta(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        boolean bool;
        double d3, d5, d9, d10 = paramDouble1;

        if (paramDouble2 <= 0.0D || paramDouble3 <= 0.0D || paramDouble1 < 0.0D || paramDouble1 > 1.0D) {
            throw new IllegalArgumentException("Invalid argument of incomplete beta function ratio.");
        }
        if (paramDouble1 == 0.0D || paramDouble1 == 1.0D) return d10;

        double d4 = paramDouble2 + paramDouble3;
        double d2 = 1.0D - paramDouble1;
        if (paramDouble2 >= d4 * paramDouble1) {

            d9 = paramDouble1;
            d3 = paramDouble2;
            d5 = paramDouble3;
            bool = false;
        } else {

            d9 = d2;
            d2 = paramDouble1;
            d3 = paramDouble3;
            d5 = paramDouble2;
            bool = true;
        }
        double d8 = 1.0D;
        double d1 = 1.0D;
        d10 = 1.0D;
        int i = (int) (d5 + d2 * d4);

        double d6 = d9 / d2;
        double d7 = d5 - d1;
        if (i == 0) d6 = d9;
        while (true) {
            d8 = d8 * d7 * d6 / (d3 + d1);
            d10 += d8;
            d7 = Math.abs(d8);
            if (d7 <= 5.0E-17D && d7 <= 5.0E-17D * d10)
                break;
            d1++;
            i--;
            if (i >= 0) {

                d7 = d5 - d1;
                if (i == 0) d6 = d9;
                continue;
            }
            d7 = d4;
            d4++;
        }

        d10 = d10 * Math.exp(d3 * Math.log(d9) + (d5 - 1.0D) * Math.log(d2) - paramDouble4) / d3;
        if (bool) d10 = 1.0D - d10;
        return d10;
    }

    public static double inverseIncompleteBeta(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        boolean bool;
        double d1, d2, d4, d10 = paramDouble4;

        if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D || paramDouble4 < 0.0D || paramDouble4 > 1.0D) {
            throw new IllegalArgumentException("Invalid argument of inverse of incomplete beta function ratio.");
        }
        if (paramDouble4 == 0.0D || paramDouble4 == 1.0D) return paramDouble4;

        if (paramDouble4 <= 0.5D) {

            d1 = paramDouble4;
            d2 = paramDouble1;
            d4 = paramDouble2;
            bool = false;
        } else {

            d1 = 1.0D - paramDouble4;
            d2 = paramDouble2;
            d4 = paramDouble1;
            bool = true;
        }

        double d5 = Math.sqrt(-Math.log(d1 * d1));
        double d8 = d5 - (2.30753D + 0.27061D * d5) / (1.0D + (0.99229D + 0.04481D * d5) * d5);
        if (d2 > 1.0D && d4 > 1.0D) {

            d5 = (d8 * d8 - 3.0D) / 6.0D;
            double d12 = 1.0D / (d2 + d2 - 1.0D);
            double d13 = 1.0D / (d4 + d4 - 1.0D);
            double d11 = 2.0D / (d12 + d13);
            double d14 = d8 * Math.sqrt(d11 + d5) / d11 - (d13 - d12) * (d5 + 0.8333333333333334D - 2.0D / 3.0D * d11);
            d10 = d2 / (d2 + d4 * Math.exp(d14 + d14));
        } else {

            d5 = d4 + d4;
            double d = 1.0D / 9.0D * d4;
            d = d5 * Math.pow(1.0D - d + d8 * Math.sqrt(d), 3.0D);
            if (d <= 0.0D) {

                d10 = 1.0D - Math.exp((Math.log((1.0D - d1) * d4) + paramDouble3) / d4);
            } else {

                d = (4.0D * d2 + d5 - 2.0D) / d;
                if (d <= 1.0D) {

                    d10 = Math.exp((Math.log(d1 * d2) + paramDouble3) / d2);
                } else {
                    d10 = 1.0D - 2.0D / (d + 1.0D);
                }
            }
        }

        d5 = 1.0D - d2;
        double d7 = 1.0D - d4;
        double d9 = 0.0D;
        double d6 = 1.0D;
        double d3 = 1.0D;
        if (d10 < 1.0E-4D) d10 = 1.0E-4D;
        if (d10 > 0.9999D) d10 = 0.9999D;

        label65:
        while (true) {
            d8 = incompleteBeta(d10, d2, d4, paramDouble3);
            d8 = (d8 - d1) * Math.exp(paramDouble3 + d5 * Math.log(d10) + d7 * Math.log(1.0D - d10));
            if (d8 * d9 <= 0.0D) d3 = d6;
            double d = 1.0D;

            while (true) {
                double d11 = d * d8;
                d6 = d11 * d11;
                if (d6 >= d3) {
                    d /= 3.0D;
                    continue;
                }
                double d12 = d10 - d11;
                if (d12 >= 0.0D && d12 <= 1.0D) {

                    if (d3 <= 1.0E-28D || d8 * d8 <= 1.0E-28D)
                        return (bool == true) ? (1.0D - d10) : d10;
                    if (d12 == 0.0D || d12 == 1.0D) {
                        d /= 3.0D;
                        continue;
                    }
                    if (d12 == d10)
                        return (bool == true) ? (1.0D - d10) : d10;
                    d10 = d12;
                    d9 = d8;

                    continue label65;
                }
                d /= 3.0D;
            }
            break;
        }
    }

    public double cdf(double paramDouble) {
        return incompleteBeta(paramDouble, this.p, this.q, this.logB);
    }

    public double getP() {
        return this.p;
    }

    public double getQ() {
        return this.q;
    }

    public double inverseCdf(double paramDouble) {
        return inverseIncompleteBeta(this.p, this.q, this.logB, paramDouble);
    }

    public double mean() {
        return this.p / (this.p + this.q);
    }

    public double pdf(double paramDouble) {
        if ((paramDouble > 0.0D && paramDouble < 1.0D) || (paramDouble == 0.0D && this.p >= 1.0D) || (paramDouble == 1.0D && this.q >= 1.0D)) {

            if (paramDouble == 0.0D)
                return (this.p == 1.0D) ? this.q : 0.0D;
            if (paramDouble == 1.0D) {
                return (this.q == 1.0D) ? this.p : 0.0D;
            }
            return Math.exp((this.p - 1.0D) * Math.log(paramDouble) + (this.q - 1.0D) * Math.log(1.0D - paramDouble) - this.logB);
        }

        throw new IllegalArgumentException("Invalid variate-value.");
    }

    private double random52() {
        while (true) {
            double d2 = this.rand.nextDouble();
            double d3 = this.rand.nextDouble();
            if (d2 > this.r) {

                double d6 = 1.0D - (1.0D - this.t) * Math.pow((1.0D - d2) / (1.0D - this.r), 1.0D / this.qStar);

                double d5 = Math.pow(d6, this.p - 1.0D);
                if (this.s2 * d3 <= d5) return d6;

                continue;
            }
            double d4 = this.t * Math.pow(d2 / this.r, 1.0D / this.pStar);

            double d1 = Math.pow(1.0D - d4, this.q - 1.0D);
            if (this.s1 * d3 <= d1) return d4;

        }
    }

    public double random() {
        if (this.randomMethod == 0) {

            double d1 = this.Gp.random();
            double d2 = d1 + this.Gq.random();

            if (d2 == 0.0D) return 1.0D;

            return d1 / d2;
        }

        if (this.randomMethod == 2) {
            while (true) {

                double d2 = this.rand.nextDouble();
                double d3 = -Math.log(1.0D - this.rand.nextDouble());
                if (d2 > this.r) {

                    double d = 1.0D - (1.0D - this.t) * Math.pow((1.0D - d2) / (1.0D - this.r), this.brecip);
                    if (-this.am1 * Math.log(d / this.t) <= d3) return this.reverseOrder ? (1.0D - d) : d;

                    continue;
                }

                double d1 = this.t * Math.pow(d2 / this.r, this.arecip);
                if (-this.bm1 * Math.log(1.0D - d1) <= d3) return this.reverseOrder ? (1.0D - d1) : d1;

            }
        }

        if (this.randomMethod == 52) return random52();

        double[] arrayOfDouble = new double[this.n];
        for (byte b = 0; b < this.n; ) {
            arrayOfDouble[b] = this.rand.nextDouble();
            b++;
        }

        Arrays.sort(arrayOfDouble);
        return arrayOfDouble[(int) this.p - 1];
    }

    private void tOpt(double paramDouble1, double paramDouble2) {
        double d1, d2, d6;
        if (paramDouble1 <= 0.0D || paramDouble1 >= 1.0D)
            throw new IllegalArgumentException("Alpha out of range.");
        if (paramDouble2 <= 1.0D)
            throw new IllegalArgumentException("Beta out of range.");
        this.am1 = paramDouble1 - 1.0D;
        double d5 = this.am1 / (this.am1 - paramDouble2);
        if (1.0D - d5 * d5 >= 1.0D)
            throw new IllegalArgumentException("Alpha too near 1 or beta too large.");
        this.bm1 = paramDouble2 - 1.0D;
        this.arecip = 1.0D / paramDouble1;
        this.brecip = 1.0D / paramDouble2;

        double d7 = 0.0D;
        double d3 = this.am1;
        double d8 = 1.0D;
        double d4 = paramDouble2;

        while (true) {
            d6 = (d7 * d4 - d8 * d3) / (d4 - d3);
            d1 = 1.0D - d6;
            d2 = paramDouble2 * d6;

            double d = d2 + Math.pow(d1, this.bm1) * (this.am1 * d1 - d2);
            if (Math.abs(d) < 1.0E-14D)
                break;
            if (d * d3 < 0.0D) {

                d8 = d7;
                d4 = d3;
                d7 = d6;
                d3 = d;

                continue;
            }
            d7 = d6;
            d3 = d;
            d4 /= 1.0D;
        }

        this.t = d6;
        this.r = d2 / (d2 + paramDouble1 * Math.pow(d1, paramDouble2));
    }

    public void setSeed(long paramLong) {
        this.rand.setSeed(paramLong);
        if (this.randomMethod == 0) {

            this.Gp.setSeed(this.rand.nextLong());
            this.Gq.setSeed(this.rand.nextLong() + 1L);
        }
    }

    private double S(double paramDouble1, double paramDouble2) {
        double d1 = paramDouble1 - 1.0D;
        double d2 = paramDouble2 - 1.0D;
        double d3 = paramDouble1 + paramDouble2 - 2.0D;
        return Math.pow(d1, d1) * Math.pow(d2, d2) / Math.pow(d3, d3);
    }

    public void setShape(double paramDouble1, double paramDouble2) {
        if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
            throw new IllegalArgumentException("Invalid shape parameter.");
        this.p = paramDouble1;
        this.q = paramDouble2;
        this.logB = Maths.lnB(paramDouble1, paramDouble2);

        if (paramDouble1 == Math.floor(paramDouble1) && paramDouble2 == Math.floor(paramDouble2) && paramDouble1 + paramDouble2 < 10.0D) {
            this.randomMethod = 1;
            this.n = (int) (paramDouble1 + paramDouble2 - 1.0D);
        } else if (paramDouble1 < 1.0D && paramDouble2 > 1.0D) {
            this.randomMethod = 2;
            this.reverseOrder = false;
            tOpt(paramDouble1, paramDouble2);
        } else if (paramDouble1 > 1.0D && paramDouble2 < 1.0D) {
            this.randomMethod = 2;
            this.reverseOrder = true;
            tOpt(paramDouble2, paramDouble1);
        } else if (paramDouble1 < 1.0D && paramDouble2 < 1.0D) {

            this.randomMethod = 52;
            this.pStar = paramDouble1;
            this.qStar = paramDouble2;
            this.t = Math.sqrt(paramDouble1 * (1.0D - paramDouble1)) / (Math.sqrt(paramDouble1 * (1.0D - paramDouble1)) + Math.sqrt(paramDouble2 * (1.0D - paramDouble2)));
            this.s1 = Math.pow(1.0D - this.t, paramDouble2 - 1.0D);
            this.s2 = Math.pow(this.t, paramDouble1 - 1.0D);
            this.r = paramDouble2 * this.t / (paramDouble2 * this.t + paramDouble1 * (1.0D - this.t));

        } else {

            this.randomMethod = 0;
            this.Gp = new Gamma(paramDouble1, 1.0D);
            this.Gq = new Gamma(paramDouble2, 1.0D);
            this.Gq.setSeed(this.rand.nextLong());
        }
    }

    public String toString() {
        return new String("Beta distribution: p = " + this.p + ", q = " + this.q + ".");
    }

    public double variance() {
        return this.p * this.q / (this.p + this.q + 1.0D) * (this.p + this.q) * (this.p + this.q);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            int i = 10000000;
            double d1 = 50.1D;
            double d2 = 50.1D;
            Beta beta = new Beta(d1, d2);

            long l1 = System.currentTimeMillis();
            for (byte b = 0; b < i; ) {
                beta.random();
                b++;
            }
            long l2 = System.currentTimeMillis();
            System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
        }
    }
}

