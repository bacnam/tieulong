package jsc.onesample;

import jsc.descriptive.MeanVar;
import jsc.distributions.StudentsT;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;

public class Ttest
        implements SignificanceTest {
    private final double t;
    private final MeanVar mv;
    private final double SP;
    protected int n;

    public Ttest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1) {
        this.mv = new MeanVar(paramArrayOfdouble);
        this.n = this.mv.getN();
        this.t = (this.mv.getMean() - paramDouble) / this.mv.getSd() / Math.sqrt(this.n);
        double d = StudentsT.tailProb(this.t, (this.n - 1));

        if (paramH1 == H1.NOT_EQUAL) {
            this.SP = d + d;
        } else if (paramH1 == H1.LESS_THAN) {
            this.SP = (this.t < 0.0D) ? d : (1.0D - d);
        } else {
            this.SP = (this.t > 0.0D) ? d : (1.0D - d);
        }
    }

    public Ttest(double[] paramArrayOfdouble, double paramDouble) {
        this(paramArrayOfdouble, paramDouble, H1.NOT_EQUAL);
    }

    public double getMean() {
        return this.mv.getMean();
    }

    public int getN() {
        return this.n;
    }

    public double getSd() {
        return this.mv.getSd();
    }

    public double getSP() {
        return this.SP;
    }

    public double getStatistic() {
        return this.t;
    }

    public double getTestStatistic() {
        return this.t;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble = {4.9D, 5.1D, 4.6D, 5.0D, 5.1D, 4.7D, 4.4D, 4.7D, 4.6D};
            double d = 5.0D;
            Ttest ttest1 = new Ttest(arrayOfDouble, d, H1.NOT_EQUAL);
            System.out.println("H1: mu <> " + d + " t = " + ttest1.getTestStatistic() + " SP = " + ttest1.getSP());
            Ttest ttest2 = new Ttest(arrayOfDouble, d, H1.LESS_THAN);
            System.out.println("H1: mu < " + d + " t = " + ttest2.getTestStatistic() + " SP = " + ttest2.getSP());
            Ttest ttest3 = new Ttest(arrayOfDouble, d, H1.GREATER_THAN);
            System.out.println("H1: mu > " + d + " t = " + ttest3.getTestStatistic() + " SP = " + ttest3.getSP());
        }
    }
}

