package jsc.independentsamples;

import jsc.ci.DistributionFreeCI;
import jsc.distributions.MannWhitneyU;
import jsc.distributions.Normal;

import java.util.Arrays;

public class MannWhitneyMedianDifferenceCI
        extends DistributionFreeCI {
    public static final int AUTO = 0;
    public static final int EXACT = 1;
    public static final int APPROX = 2;
    public static final int FAST_APPROX = 3;
    static final int SMALL_SAMPLE_SIZE_PRODUCT = 400;
    static final int MEDIUM_SAMPLE_SIZE_PRODUCT = 10000;
    static final double TOL1 = 1.0E-16D;
    static final double TOL2 = 1.0E-16D;
    static final double TOL3 = 1.0E-15D;
    int method;
    double[] xA;
    double[] xB;
    double dpoint;
    double x1;
    double fx1;
    double x2;
    double fx2;

    public MannWhitneyMedianDifferenceCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble, int paramInt) {
        super(paramDouble);

        int i = paramArrayOfdouble1.length;
        int j = paramArrayOfdouble2.length;

        if (paramInt == 0) {

            long l = (i * j);
            if (l <= 400L) {
                paramInt = 1;
            } else if (l <= 10000L) {
                paramInt = 2;
            } else {
                paramInt = 3;
            }

        }
        this.method = paramInt;

        if (paramInt == 3) {
            rankCI(paramArrayOfdouble2, paramArrayOfdouble1, paramDouble);
        } else {
            double d;

            if (paramInt == 2) {

                double d1 = Normal.inverseStandardCdf(0.5D + 0.5D * paramDouble);

                this.d = (int) Math.round(0.5D * ((i * j) + 1.0D - d1 * Math.sqrt((i * j) * ((i + j) + 1.0D) / 3.0D)));
                Normal normal = MannWhitneyU.normalApproximation(i, j, 0);
                d = normal.cdf(this.d - 1.0D);
            } else if (paramInt == 1) {

                MannWhitneyU mannWhitneyU = new MannWhitneyU(i, j);

                d = 0.5D * (1.0D - paramDouble);
                long l = mannWhitneyU.getMinValue();

                double d1 = mannWhitneyU.pdf(l);
                while (l < mannWhitneyU.getMaxValue() && d1 < d) {
                    l++;
                    d1 += mannWhitneyU.pdf(l);
                }

                l--;
                this.d = (int) (l + 1L);
                d = d1 - mannWhitneyU.pdf((l + 1L));
            } else {

                throw new IllegalArgumentException("Invalid method parameter.");
            }

            this.achievedConfidence = 1.0D - 2.0D * d;
            computeInterval(2, this.d, paramArrayOfdouble1, paramArrayOfdouble2);
            this.xA = paramArrayOfdouble1;
            this.xB = paramArrayOfdouble2;
        }
    }

    public MannWhitneyMedianDifferenceCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble) {
        this(paramArrayOfdouble1, paramArrayOfdouble2, paramDouble, 0);
    }

    public static double getPointEstimate(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
        double[] arrayOfDouble = DistributionFreeCI.differences(paramArrayOfdouble1, paramArrayOfdouble2);
        int i = arrayOfDouble.length;
        int j = i / 2;
        if (i % 2 == 0) {
            return (arrayOfDouble[j - 1] + arrayOfDouble[j]) / 2.0D;
        }
        return arrayOfDouble[j];
    }

    private void rankCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble) {
        double d17;
        int i = paramArrayOfdouble1.length;
        int j = paramArrayOfdouble2.length;

        double[] arrayOfDouble1 = new double[1 + i];
        double[] arrayOfDouble2 = new double[1 + j];
        System.arraycopy(paramArrayOfdouble1, 0, arrayOfDouble1, 1, i);
        System.arraycopy(paramArrayOfdouble2, 0, arrayOfDouble2, 1, j);
        arrayOfDouble1[0] = Double.NEGATIVE_INFINITY;
        arrayOfDouble2[0] = Double.NEGATIVE_INFINITY;
        Arrays.sort(arrayOfDouble1);
        Arrays.sort(arrayOfDouble2);

        this.dpoint = 0.0D;
        this.lowerLimit = 0.0D;
        this.upperLimit = 0.0D;
        boolean bool1 = false;
        double d1 = i;
        double d2 = j;

        if (i < 2 || j < 2) {
            throw new IllegalArgumentException("Too few data values.");
        }

        TrimmedMean trimmedMean1 = new TrimmedMean(this, arrayOfDouble1, i);
        double d3 = trimmedMean1.getMean();
        double d4 = trimmedMean1.getVariance();

        TrimmedMean trimmedMean2 = new TrimmedMean(this, arrayOfDouble2, j);
        double d5 = trimmedMean2.getMean();
        double d6 = trimmedMean2.getVariance();

        double d7 = d5 - d3;
        double d8 = d4 + d6;
        double d9 = Math.sqrt(d8);
        d9 = Math.max(d9, 1.0E-20D);
        double d10 = 2.0D;
        if (i < 10 || j < 10 || paramDouble > 0.96D) d10 = 3.0D;
        double d11 = d7 - d10 * d9;
        double d12 = d7 + d10 * d9;

        double d13 = arrayOfDouble1[i] - arrayOfDouble1[1];
        double d14 = arrayOfDouble2[j] - arrayOfDouble2[1];
        double d15 = 1.0E-16D * Math.max(d13, d14);

        int n = i / 2;
        d13 = arrayOfDouble1[n];
        n = j / 2;
        d14 = arrayOfDouble2[n];

        double d16 = 1.0E-16D * Math.max(Math.max(d13, -d13), Math.max(d14, -d14));
        d15 = Math.max(d15, d16);

        d16 = d9 * 1.0E-15D;
        d15 = Math.max(d15, d16);

        d15 = Math.max(d15, 1.0E-20D);

        d16 = 10.0D * d15;

        boolean bool2 = true;

        double d18 = 0.0D;
        if (i * j % 2 == 0) {

            d17 = d1 * d2 / 2.0D - 0.5D;
            d18 = d17 + 1.0D;
            bool2 = false;
        } else {

            d17 = d1 * d2 / 2.0D;
        }

        double d19 = (1.0D - paramDouble) / 2.0D;

        double d20 = d1 * d2 / 2.0D;
        double d21 = d1 * d2 * (d1 + d2 + 1.0D) / 12.0D;
        double d22 = Math.sqrt(d21);

        double d23 = d22 * Normal.inverseStandardCdf(d19) + d20 - 0.5D;

        int i1 = (int) d23;
        if (i1 < 0) i1 = 0;
        d23 = i1;
        d13 = (d23 + 0.5D - d20) / d22;
        d19 = Normal.standardTailProb(d13, false);

        this.achievedConfidence = 1.0D - 2.0D * d19;

        d23 += 0.5D;

        double d24 = d1 * d2 - d23;
        int k = (int) (d23 + 0.5D);
        int m = (int) (d24 + 0.5D);

        this.d = k;

        double d25 = Math.abs(d13) * Math.sqrt(d1 * d2 * (d1 + d2)) / (d12 - d11) * Math.sqrt(3.0D);

        brack(d11, d23, d25, arrayOfDouble1, i, arrayOfDouble2, j);
        this.lowerLimit = ill(d23, arrayOfDouble1, i, arrayOfDouble2, j, d16);

        brack(d12, d24, d25, arrayOfDouble1, i, arrayOfDouble2, j);
        this.upperLimit = ill(d24, arrayOfDouble1, i, arrayOfDouble2, j, d16);

        if (this.upperLimit > this.lowerLimit + d16) d25 = (d12 - d11) / (this.upperLimit - this.lowerLimit) * d25;

        d7 = (this.lowerLimit + this.upperLimit) / 2.0D;
        brack(d7, d17, d25, arrayOfDouble1, i, arrayOfDouble2, j);
        this.dpoint = ill(d17, arrayOfDouble1, i, arrayOfDouble2, j, d15);

        if (bool2 == true) {
            return;
        }

        brack(this.dpoint, d18, d25, arrayOfDouble1, i, arrayOfDouble2, j);
        double d26 = ill(d18, arrayOfDouble1, i, arrayOfDouble2, j, d15);
        this.dpoint = (this.dpoint + d26) / 2.0D;
    }

    private double ill(double paramDouble1, double[] paramArrayOfdouble1, int paramInt1, double[] paramArrayOfdouble2, int paramInt2, double paramDouble2) {
        this.fx1 -= paramDouble1;
        this.fx2 -= paramDouble1;
        boolean bool = false;

        while (Math.abs(this.x2 - this.x1) >= paramDouble2) {

            double d1 = this.x2 - this.fx2 * (this.x2 - this.x1) / (this.fx2 - this.fx1);
            if (bool == true) d1 = (this.x1 + this.x2) / 2.0D;
            bool = false;
            double d2 = fmann(d1, paramArrayOfdouble1, paramInt1, paramArrayOfdouble2, paramInt2);
            d2 -= paramDouble1;

            if (d2 * this.fx2 <= 0.0D) {

                this.x1 = this.x2;
                this.fx1 = this.fx2;
                this.x2 = d1;
                this.fx2 = d2;

                continue;
            }

            this.x2 = d1;
            this.fx2 = d2;
            this.fx1 /= 2.0D;
            if (Math.abs(this.fx2) > Math.abs(this.fx1)) {

                this.fx1 = 2.0D * this.fx1;
                bool = true;
            }
        }

        return (this.x1 + this.x2) / 2.0D;
    }

    private void brack(double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfdouble1, int paramInt1, double[] paramArrayOfdouble2, int paramInt2) {
        this.x1 = paramDouble1;
        this.fx1 = fmann(this.x1, paramArrayOfdouble1, paramInt1, paramArrayOfdouble2, paramInt2);
        double d = 1.5D * (paramDouble2 - this.fx1) / paramDouble3;
        while (true) {
            this.x2 = this.x1 + d;
            this.fx2 = fmann(this.x2, paramArrayOfdouble1, paramInt1, paramArrayOfdouble2, paramInt2);
            if ((this.fx1 - paramDouble2) * (this.fx2 - paramDouble2) < 0.0D)
                return;
            this.x1 = this.x2;
        }
    }

    private double fmann(double paramDouble, double[] paramArrayOfdouble1, int paramInt1, double[] paramArrayOfdouble2, int paramInt2) {
        double d = paramDouble;
        int i = paramInt1;
        int j = paramInt2;

        int k = 0;
        int m = 0;

        for (byte b = 1; b <= i; b++) {

            double d1 = paramArrayOfdouble1[b] + d;

            while (d1 >= paramArrayOfdouble2[k + 1]) {

                k++;
                if (k >= j) {

                    m += (i - b + 1) * j;
                    return m;
                }
            }

            m += k;
        }
        return m;
    }

    public double getPointEstimate() {
        if (this.method == 3) {
            return this.dpoint;
        }
        return getPointEstimate(this.xA, this.xB);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = 'È';
            int i = c + 1;

            double[] arrayOfDouble1 = new double[c];
            double[] arrayOfDouble2 = new double[i];
            Normal normal1 = new Normal(2.0D, 1.0D);
            Normal normal2 = new Normal(1.0D, 1.0D);
            byte b;
            for (b = 0; b < c; ) {
                arrayOfDouble1[b] = normal1.random();
                b++;
            }
            for (b = 0; b < i; ) {
                arrayOfDouble2[b] = normal2.random();
                b++;
            }

            long l1 = System.currentTimeMillis();
            MannWhitneyMedianDifferenceCI mannWhitneyMedianDifferenceCI = new MannWhitneyMedianDifferenceCI(arrayOfDouble1, arrayOfDouble2, 0.9D, 3);
            long l2 = System.currentTimeMillis();
            System.out.println("n = " + c + " Time = " + ((l2 - l1) / 1000L) + " secs");
            System.out.println("CI=[" + mannWhitneyMedianDifferenceCI.getLowerLimit() + "," + mannWhitneyMedianDifferenceCI.getUpperLimit() + "]" + " d = " + mannWhitneyMedianDifferenceCI.getD() + " Point estimate = " + mannWhitneyMedianDifferenceCI.getPointEstimate() + " Achieved conf = " + mannWhitneyMedianDifferenceCI.getAchievedConfidence());
        }
    }

    class TrimmedMean {
        private final MannWhitneyMedianDifferenceCI this$0;
        double zbar;
        double varzb;

        TrimmedMean(MannWhitneyMedianDifferenceCI this$0, double[] param1ArrayOfdouble, int param1Int) {
            this.this$0 = this$0;

            double d1 = 0.1D;
            double d2 = param1Int;

            int j = (int) (d1 * d2);
            int k = j + 1;
            int m = param1Int - j;

            double d3 = 0.0D;
            int i;
            for (i = k; i <= m; ) {
                d3 += param1ArrayOfdouble[i];
                i++;
            }
            double d4 = (param1Int - 2 * j);
            this.zbar = d3 / d4;

            d3 = 0.0D;
            for (i = k; i <= m; ) {
                d3 += (param1ArrayOfdouble[i] - this.zbar) * (param1ArrayOfdouble[i] - this.zbar);
                i++;
            }

            if (j != 0) {

                double d = j;
                d3 += d * (param1ArrayOfdouble[k - 1] - this.zbar) * (param1ArrayOfdouble[k - 1] - this.zbar) + d * (param1ArrayOfdouble[m + 1] - this.zbar) * (param1ArrayOfdouble[m + 1] - this.zbar);
            }

            this.varzb = d3 / d2 * d2;
        }

        double getMean() {
            return this.zbar;
        }

        double getVariance() {
            return this.varzb;
        }
    }
}

