package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;
import jsc.util.Maths;

public class NoncentralBeta
        extends AbstractContinuousDistribution {
    static final int ITRMAX = 200;
    private double p;
    private double q;
    private double lambda;
    private double emhl;
    private double halfLambda;
    private double logHalfLambda;
    private double logP;
    private ChiSquared Cq;
    private NoncentralChiSquared Cp;
    private double beta;
    private double logGammaQ;
    private Beta centralBeta;

    public NoncentralBeta(double paramDouble1, double paramDouble2, double paramDouble3) {
        super(0.0D, 1.0D, false);

        setParameters(paramDouble1, paramDouble2, paramDouble3);
    }

    private double betanc(double paramDouble) {
        double d4, d1 = this.p, d3 = this.q;

        double d8 = Beta.incompleteBeta(paramDouble, d1, d3, this.beta);
        double d5 = Math.exp(d1 * Math.log(paramDouble) + d3 * Math.log(1.0D - paramDouble) - this.beta - this.logP);

        double d6 = this.emhl;
        double d9 = 0.0D;
        double d2 = d6 * d8;
        double d7 = 1.0D - d6;
        double d10 = d2;

        do {
            d9++;

            d8 -= d5;
            d5 = paramDouble * (d1 + d3 + d9 - 1.0D) * d5 / (d1 + d9);
            d6 = d6 * this.halfLambda / d9;
            d7 -= d6;
            d2 = d8 * d6;
            d10 += d2;
            d4 = (d8 - d5) * d7;
        } while (d9 < 200.0D && d4 > this.tolerance);
        if (d4 > this.tolerance)
            throw new RuntimeException("Cannot calculate cdf to required accuracy.");
        return d10;
    }

    public double cdf(double paramDouble) {
        if (this.lambda == 0.0D) return this.centralBeta.cdf(paramDouble);

        double d1 = this.p;
        double d2 = this.q;

        double d18 = 0.0D;

        if (paramDouble < 0.0D || paramDouble > 1.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (paramDouble == 0.0D || paramDouble == 1.0D) return paramDouble;

        double d16 = this.halfLambda;
        byte b1 = 0;
        if (this.lambda < 54.0D) return betanc(paramDouble);
        int i = (int) Maths.truncate(d16 + 0.5D);
        int m = (int) (i - 5.0D * Math.sqrt(i));
        int n = (int) (i + 5.0D * Math.sqrt(i));
        double d10 = -d16 + i * this.logHalfLambda - Maths.logGamma(i + 1.0D);
        double d8 = Math.exp(d10);
        double d9 = d8;
        double d15 = d8;

        double d17 = Maths.lnB(d1 + i, d2);
        double d12 = (d1 + i) * Math.log(paramDouble) + d2 * Math.log(1.0D - paramDouble) - Math.log(d1 + i) - d17;
        double d4 = Math.exp(d12);
        double d3 = d4;
        double d5 = Beta.incompleteBeta(paramDouble, d1 + i, d2, d17);
        double d6 = d5;
        b1++;
        double d14 = d8 * d5;
        int j = i;

        while (j >= m && d8 >= this.tolerance) {

            d8 = d8 * j / d16;
            b1++;
            d4 = (d1 + j) / paramDouble * (d1 + d2 + j - 1.0D) * d4;
            j--;
            d5 += d4;
            d15 += d8;
            d14 += d8 * d5;
        }

        double d13 = Maths.logGamma(d1 + d2) - Maths.logGamma(d1 + 1.0D) - Maths.logGamma(d2);
        double d11 = d1 * Math.log(paramDouble) + d2 * Math.log(1.0D - paramDouble);

        for (byte b2 = 1; b2 <= j; b2++) {

            int i1 = b2 - 1;
            d18 += Math.exp(d13 + d11 + i1 * Math.log(paramDouble));
            double d = Math.log(d1 + d2 + i1) - Math.log(d1 + 1.0D + i1) + d13;
            d13 = d;
        }

        if (j <= 0) j = 1;
        double d7 = (1.0D - Gamma.incompleteGamma(d16, j)) * (d5 + d18);
        d8 = d9;
        d5 = d6;
        d4 = d3;
        int k = i;
        while (true) {
            double d = d7 + (1.0D - d15) * d5;
            if (d < this.tolerance || k >= n) return d14;
            k++;
            b1++;

            d8 = d8 * d16 / k;
            d15 += d8;
            d5 -= d4;
            d4 = paramDouble * (d1 + d2 + k - 1.0D) / (d1 + k) * d4;
            d14 += d8 * d5;
        }
    }

    public double getLambda() {
        return this.lambda;
    }

    public double getP() {
        return this.p;
    }

    public double getQ() {
        return this.q;
    }

    public double inverseCdf(double paramDouble) {
        if (this.lambda == 0.0D) {
            return this.centralBeta.inverseCdf(paramDouble);
        }
        return super.inverseCdf(paramDouble);
    }

    public double mean() {
        if (this.lambda == 0.0D) return this.centralBeta.mean();
        double d = 0.0D;

        for (byte b = 0; b < 'Ϩ'; b++) {

            double d1 = Math.exp(-this.halfLambda + b * this.logHalfLambda - Maths.logGamma((b + 1)) + Math.log(this.p + b) - Math.log(this.p + b + this.q));
            d += d1;

            if (Math.abs(d1) < this.tolerance * d) return d;
        }
        throw new RuntimeException("Cannot calculate mean to required accuracy.");
    }

    public Normal normalApproximation() {
        double d1 = this.halfLambda;
        double d2 = d1 * d1;
        double d3 = d1 * d2;
        double d4 = d1 * d3;
        double d5 = this.p + this.q;
        double d6 = d5 * d5;
        double d7 = this.q * this.q;
        double d8 = d5 + d1;
        double d9 = 1.0D - this.q / d8 * (1.0D + this.lambda / 2.0D * d8 * d8);
        double d10 = d8 * (d8 + 1.0D) + d1;
        double d11 = (d5 + d5 + 1.0D) * (d5 + d5 + 1.0D) + 1.0D;
        double d12 = 3.0D * d6 + 5.0D * d5 + 2.0D;
        double d13 = d6 * (d5 + 1.0D) + d12 * d1 + (3.0D * d5 + 4.0D) * d2 + d3;
        double d14 = (3.0D * d5 + 1.0D) * (9.0D * d5 + 17.0D) + 2.0D * d5 * (3.0D * d5 + 2.0D) * (3.0D * d5 + 4.0D) + 15.0D;
        double d15 = 54.0D * d6 + 162.0D * d5 + 130.0D;
        double d16 = 6.0D * (6.0D * d5 + 11.0D);
        double d17 = d1 * (d12 * d12 + 2.0D * d14 * d1 + d15 * d2 + d16 * d3 + 9.0D * d4);
        double d18 = d1 * d7 / d8 * d8 * d8 * d8;
        double d19 = this.q / d10 * (1.0D + d1 * (this.lambda * this.lambda + 3.0D * this.lambda + d11) / d10 * d10) - d7 / d13 * (1.0D + d17 / d13 * d13);
        return new Normal(d9, Math.sqrt(d18 + d19));
    }

    public double pdf(double paramDouble) {
        if (this.lambda == 0.0D) return this.centralBeta.pdf(paramDouble);
        if ((paramDouble > 0.0D && paramDouble < 1.0D) || (paramDouble == 0.0D && this.p >= 1.0D) || (paramDouble == 1.0D && this.q >= 1.0D)) {

            if (paramDouble == 0.0D)
                return (this.p == 1.0D) ? this.q : 0.0D;
            if (paramDouble == 1.0D) {
                return (this.q == 1.0D) ? this.p : 0.0D;
            }

            double d1 = 0.0D;

            double d2 = -this.halfLambda + (this.q - 1.0D) * Math.log(1.0D - paramDouble) - this.logGammaQ;
            double d3 = Math.log(paramDouble);
            for (byte b = 0; b < 'Ϩ'; b++) {

                double d = Math.exp(d2 + b * this.logHalfLambda - Maths.logGamma((b + 1)) + (this.p + b - 1.0D) * d3 + Maths.logGamma(this.p + this.q + b) - Maths.logGamma(this.p + b));

                d1 += d;

                if (Math.abs(d) < this.tolerance * d1) return d1;
            }
            throw new RuntimeException("Cannot calculate pdf to required accuracy.");
        }

        throw new IllegalArgumentException("Invalid variate-value.");
    }

    public double random() {
        if (this.lambda == 0.0D) return this.centralBeta.random();
        double d = this.Cp.random();
        return d / (d + this.Cq.random());
    }

    public void setParameters(double paramDouble1, double paramDouble2, double paramDouble3) {
        if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
            throw new IllegalArgumentException("Invalid shape parameter.");
        if (paramDouble3 < 0.0D)
            throw new IllegalArgumentException("Invalid noncentrality parameter.");
        this.p = paramDouble1;
        this.q = paramDouble2;
        this.lambda = paramDouble3;

        setOpen((paramDouble1 < 1.0D || paramDouble2 < 1.0D));

        if (paramDouble3 == 0.0D) {
            this.centralBeta = new Beta(paramDouble1, paramDouble2);
        } else {

            this.centralBeta = null;
            this.halfLambda = 0.5D * paramDouble3;
            this.logHalfLambda = Math.log(this.halfLambda);
            this.Cp = new NoncentralChiSquared(paramDouble1 + paramDouble1, paramDouble3);
            this.Cq = new ChiSquared(paramDouble2 + paramDouble2);
            this.Cq.setSeed(this.rand.nextLong());

            this.logP = Math.log(paramDouble1);

            this.logGammaQ = Maths.logGamma(paramDouble2);
            this.beta = Maths.logGamma(paramDouble1) + this.logGammaQ - Maths.logGamma(paramDouble1 + paramDouble2);
            this.emhl = Math.exp(-this.halfLambda);
        }
    }

    public void setSeed(long paramLong) {
        this.rand.setSeed(paramLong);
        if (this.lambda == 0.0D) {
            this.centralBeta.setSeed(this.rand.nextLong());
        } else {

            this.Cp.setSeed(this.rand.nextLong());
            this.Cq.setSeed(this.rand.nextLong());
        }
    }

    public String toString() {
        return new String("Noncentral beta distribution: p = " + this.p + ", q = " + this.q + ", lambda = " + this.lambda + ".");
    }

    public double variance() {
        if (this.lambda == 0.0D) return this.centralBeta.variance();

        double d1 = 0.0D;

        double d2 = mean();
        for (byte b = 0; b < 'Ϩ'; b++) {

            double d = Math.exp(-this.halfLambda + b * this.logHalfLambda - Maths.logGamma((b + 1)) + Math.log(this.p + b) + Math.log(this.p + b + 1.0D) - Math.log(this.p + b + this.q + 1.0D) - Math.log(this.p + b + this.q));

            d1 += d;

            if (Math.abs(d) < this.tolerance * d1) return d1 - d2 * d2;
        }
        throw new RuntimeException("Cannot calculate variance to required accuracy.");
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = '✐';
            NoncentralBeta noncentralBeta = new NoncentralBeta(5.0D, 1.5D, 4.0D);
            noncentralBeta.setTolerance(1.0E-11D);
            double[] arrayOfDouble = new double[c];
            for (byte b = 0; b < c; b++) {
                arrayOfDouble[b] = noncentralBeta.random();
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, noncentralBeta, H1.NOT_EQUAL, true);
            System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }
}

