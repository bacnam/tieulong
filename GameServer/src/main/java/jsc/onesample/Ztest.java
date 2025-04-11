package jsc.onesample;

import jsc.descriptive.MeanVar;
import jsc.distributions.Normal;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;

public class Ztest
        implements SignificanceTest {
    private final double z;
    private final MeanVar mv;
    private double SP;

    public Ztest(double[] paramArrayOfdouble, double paramDouble1, double paramDouble2, H1 paramH1) {
        this.mv = new MeanVar(paramArrayOfdouble);
        this.z = (this.mv.getMean() - paramDouble1) / paramDouble2 / Math.sqrt(this.mv.getN());
        this.SP = getSP(this.z, paramH1);
    }

    public Ztest(double[] paramArrayOfdouble, double paramDouble1, double paramDouble2) {
        this(paramArrayOfdouble, paramDouble1, paramDouble2, H1.NOT_EQUAL);
    }

    public Ztest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1) {
        this.mv = new MeanVar(paramArrayOfdouble);
        this.z = (this.mv.getMean() - paramDouble) / this.mv.getSd() / Math.sqrt(this.mv.getN());
        this.SP = getSP(this.z, paramH1);
    }

    public Ztest(double[] paramArrayOfdouble, double paramDouble) {
        this(paramArrayOfdouble, paramDouble, H1.NOT_EQUAL);
    }

    public static double getSP(double paramDouble, H1 paramH1) {
        if (paramH1 == H1.NOT_EQUAL)
            return 2.0D * Normal.standardTailProb(Math.abs(paramDouble), true);
        if (paramH1 == H1.LESS_THAN) {
            return Normal.standardTailProb(paramDouble, false);
        }
        return Normal.standardTailProb(paramDouble, true);
    }

    public double getMean() {
        return this.mv.getMean();
    }

    public double getSd() {
        return this.mv.getSd();
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.z;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble = {4.9D, 5.1D, 4.6D, 5.0D, 5.1D, 4.7D, 4.4D, 4.7D, 4.6D};
            Ztest ztest1 = new Ztest(arrayOfDouble, 5.0D, 0.2D, H1.NOT_EQUAL);
            System.out.println("z = " + ztest1.getTestStatistic() + " SP = " + ztest1.getSP());
            Ztest ztest2 = new Ztest(arrayOfDouble, 5.0D, 0.2D, H1.LESS_THAN);
            System.out.println("z = " + ztest2.getTestStatistic() + " SP = " + ztest2.getSP());
            Ztest ztest3 = new Ztest(arrayOfDouble, 5.0D, 0.2D, H1.GREATER_THAN);
            System.out.println("z = " + ztest3.getTestStatistic() + " SP = " + ztest3.getSP());
        }
    }
}

