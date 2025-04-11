package jsc.distributions;

import jsc.goodnessfit.KolmogorovTest;
import jsc.tests.H1;
import jsc.util.Maths;

public class NoncentralFishersF
        extends AbstractContinuousDistribution {
    static final int ITRMAX = 200;
    private double df1;
    private double df2;
    private double lambda;
    private double logHalfLambda;
    private double ratio;
    private double logRatio;
    private double logGammaHalfV;
    private FishersF centralF;
    private NoncentralBeta B;
    private NoncentralChiSquared chiSquaredU;
    private ChiSquared chiSquaredV;

    public NoncentralFishersF(double paramDouble1, double paramDouble2, double paramDouble3) {
        super(0.0D, Double.POSITIVE_INFINITY, false);

        setParameters(paramDouble1, paramDouble2, paramDouble3);
    }

    public double cdf(double paramDouble) {
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (paramDouble == 0.0D) return 0.0D;
        if (this.lambda == 0.0D) return this.centralF.cdf(paramDouble);

        return this.B.cdf(this.df1 * paramDouble / (this.df1 * paramDouble + this.df2));
    }

    public double getDf1() {
        return this.df1;
    }

    public double getDf2() {
        return this.df2;
    }

    public double getLambda() {
        return this.lambda;
    }

    public double inverseCdf(double paramDouble) {
        if (this.lambda == 0.0D) {
            return this.centralF.inverseCdf(paramDouble);
        }
        return super.inverseCdf(paramDouble);
    }

    public double mean() {
        return (this.df2 > 2.0D) ? (this.df2 * (this.df1 + this.lambda) / this.df1 * (this.df2 - 2.0D)) : Double.NaN;
    }

    public double pdf(double paramDouble) {
        if (this.lambda == 0.0D) return this.centralF.pdf(paramDouble);
        if (paramDouble < 0.0D)
            throw new IllegalArgumentException("Invalid variate-value.");
        if (paramDouble == 0.0D) return 0.0D;

        double d1 = 0.0D;

        double d2 = Math.log(paramDouble);
        double d3 = 0.5D * this.lambda;
        double d4 = 0.5D * this.df1;
        double d5 = 0.5D * (this.df1 + this.df2);
        double d6 = Math.log(1.0D + this.ratio * paramDouble);
        for (byte b = 0; b < 'Ϩ'; b++) {

            double d = Math.exp(b * this.logHalfLambda - d3 + (d4 + b) * this.logRatio + Maths.logGamma(d5 + b) + (d4 + b - 1.0D) * d2 - Maths.logGamma((b + 1)) - Maths.logGamma(d4 + b) - this.logGammaHalfV - (b + d5) * d6);

            d1 += d;

            if (Math.abs(d) < this.tolerance * d1) return d1;

        }

        throw new RuntimeException("Cannot calculate pdf to required accuracy.");
    }

    public double random() {
        if (this.lambda == 0.0D) return this.centralF.random();
        return this.df2 * this.chiSquaredU.random() / this.df1 * this.chiSquaredV.random();
    }

    public void setParameters(double paramDouble1, double paramDouble2, double paramDouble3) {
        if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
            throw new IllegalArgumentException("Invalid \"degrees of freedom\" parameter.");
        if (paramDouble3 < 0.0D)
            throw new IllegalArgumentException("Invalid noncentrality parameter.");
        this.df1 = paramDouble1;
        this.df2 = paramDouble2;
        this.lambda = paramDouble3;
        this.ratio = paramDouble1 / paramDouble2;
        this.logRatio = Math.log(this.ratio);
        this.logGammaHalfV = Maths.logGamma(0.5D * paramDouble2);
        if (paramDouble3 == 0.0D) {

            this.centralF = new FishersF(paramDouble1, paramDouble2);
            this.chiSquaredU = null;
            this.chiSquaredV = null;
            this.B = null;
        } else {

            this.centralF = null;
            this.chiSquaredU = new NoncentralChiSquared(paramDouble1, paramDouble3);
            this.chiSquaredV = new ChiSquared(paramDouble2);
            this.chiSquaredV.setSeed(this.rand.nextLong());
            this.logHalfLambda = Math.log(0.5D * paramDouble3);
            this.B = new NoncentralBeta(0.5D * paramDouble1, 0.5D * paramDouble2, paramDouble3);
        }
    }

    public void setSeed(long paramLong) {
        this.rand.setSeed(paramLong);
        if (this.lambda == 0.0D) {
            this.centralF.setSeed(this.rand.nextLong());
        } else {

            this.chiSquaredU.setSeed(this.rand.nextLong());
            this.chiSquaredV.setSeed(this.rand.nextLong() + 1L);
        }
    }

    public String toString() {
        return new String("Noncentral Fisher's F distribution: df1 = " + this.df1 + ", df2 = " + this.df2 + ", lambda = " + this.lambda + ".");
    }

    public double variance() {
        return (this.df2 > 4.0D) ? (2.0D / this.ratio * this.ratio * ((this.df1 + this.lambda) * (this.df1 + this.lambda) + (this.df1 + this.lambda + this.lambda) * (this.df2 - 2.0D) / (this.df2 - 2.0D) * (this.df2 - 2.0D) * (this.df2 - 4.0D))) : Double.NaN;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = '✐';

            NoncentralFishersF noncentralFishersF = new NoncentralFishersF(8.0D, 30.0D, 36.0D);
            noncentralFishersF.setTolerance(1.0E-11D);
            double[] arrayOfDouble = new double[c];
            for (byte b = 0; b < c; b++) {
                arrayOfDouble[b] = noncentralFishersF.random();
            }

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, noncentralFishersF, H1.NOT_EQUAL, true);
            System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
        }
    }
}

