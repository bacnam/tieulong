package jsc.independentsamples;

import jsc.distributions.Gamma;
import jsc.util.Arrays;

public class RatioScaleCI
        extends MannWhitneyMedianDifferenceCI {
    public RatioScaleCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble, int paramInt) {
        super(Arrays.log(paramArrayOfdouble1), Arrays.log(paramArrayOfdouble2), paramDouble, paramInt);
        this.lowerLimit = Math.exp(this.lowerLimit);
        this.upperLimit = Math.exp(this.upperLimit);
    }

    public RatioScaleCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble) {
        this(paramArrayOfdouble1, paramArrayOfdouble2, paramDouble, 0);
    }

    public static double getPointEstimate(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
        return Math.exp(MannWhitneyMedianDifferenceCI.getPointEstimate(Arrays.log(paramArrayOfdouble1), Arrays.log(paramArrayOfdouble2)));
    }

    public double getPointEstimate() {
        if (this.method == 3) {
            return Math.exp(this.dpoint);
        }
        return Math.exp(MannWhitneyMedianDifferenceCI.getPointEstimate(this.xA, this.xB));
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            byte b2 = 25;
            int i = b2 + 1;
            double[] arrayOfDouble1 = new double[b2];
            double[] arrayOfDouble2 = new double[i];

            double d = 1.0D;
            Gamma gamma1 = new Gamma(d, 2.0D);
            Gamma gamma2 = new Gamma(d, 1.0D);
            gamma1.setSeed(123L);
            gamma2.setSeed(321L);
            byte b1;
            for (b1 = 0; b1 < b2; ) {
                arrayOfDouble1[b1] = gamma1.random();
                b1++;
            }
            for (b1 = 0; b1 < i; ) {
                arrayOfDouble2[b1] = gamma2.random();
                b1++;
            }

            long l1 = System.currentTimeMillis();
            RatioScaleCI ratioScaleCI = new RatioScaleCI(arrayOfDouble1, arrayOfDouble2, 0.95D, 3);
            long l2 = System.currentTimeMillis();
            System.out.println("n = " + b2 + " Time = " + ((l2 - l1) / 1000L) + " secs");
            System.out.println("  Fast CI=[" + ratioScaleCI.getLowerLimit() + "," + ratioScaleCI.getUpperLimit() + "]" + " d = " + ratioScaleCI.getD() + " Point estimate = " + ratioScaleCI.getPointEstimate() + " Achieved conf = " + ratioScaleCI.getAchievedConfidence());

            ratioScaleCI = new RatioScaleCI(arrayOfDouble1, arrayOfDouble2, 0.95D, 2);
            l2 = System.currentTimeMillis();
            System.out.println("n = " + b2 + " Time = " + ((l2 - l1) / 1000L) + " secs");
            System.out.println("Approx CI=[" + ratioScaleCI.getLowerLimit() + "," + ratioScaleCI.getUpperLimit() + "]" + " d = " + ratioScaleCI.getD() + " Point estimate = " + ratioScaleCI.getPointEstimate() + " Achieved conf = " + ratioScaleCI.getAchievedConfidence());

            ratioScaleCI = new RatioScaleCI(arrayOfDouble1, arrayOfDouble2, 0.95D, 1);
            l2 = System.currentTimeMillis();
            System.out.println("n = " + b2 + " Time = " + ((l2 - l1) / 1000L) + " secs");
            System.out.println(" Exact CI=[" + ratioScaleCI.getLowerLimit() + "," + ratioScaleCI.getUpperLimit() + "]" + " d = " + ratioScaleCI.getD() + " Point estimate = " + ratioScaleCI.getPointEstimate() + " Achieved conf = " + ratioScaleCI.getAchievedConfidence());
        }
    }
}

