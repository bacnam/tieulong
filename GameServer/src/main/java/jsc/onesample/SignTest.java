package jsc.onesample;

import jsc.datastructures.PairedData;
import jsc.distributions.Binomial;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;

public class SignTest
        implements SignificanceTest {
    private int n1;
    private int S;
    private double SP;

    public SignTest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1) {
        int i = paramArrayOfdouble.length;
        byte b1 = 0;
        this.S = 0;
        this.n1 = 0;

        for (byte b2 = 0; b2 < i; b2++) {

            if (paramArrayOfdouble[b2] < paramDouble) {
                this.S++;
            } else if (paramArrayOfdouble[b2] <= paramDouble) {

                b1++;
            }
        }
        this.n1 = i - b1;
        if (this.n1 < 1)
            throw new IllegalArgumentException("No non-zero differences.");
        Binomial binomial = new Binomial(this.n1, 0.5D);

        if (paramH1 == H1.NOT_EQUAL) {

            this.S = Math.min(this.S, this.n1 - this.S);
            this.SP = 2.0D * binomial.cdf(this.S);
            if (this.SP > 1.0D) this.SP = 1.0D;

        } else if (paramH1 == H1.LESS_THAN) {
            this.SP = 1.0D - binomial.cdf((this.S - 1));
        } else {

            this.S = this.n1 - this.S;
            this.SP = 1.0D - binomial.cdf((this.S - 1));
        }
    }

    public SignTest(double[] paramArrayOfdouble, double paramDouble) {
        this(paramArrayOfdouble, paramDouble, H1.NOT_EQUAL);
    }

    public SignTest(PairedData paramPairedData, H1 paramH1) {
        this(paramPairedData.differences(), 0.0D, paramH1);
    }

    public SignTest(PairedData paramPairedData) {
        this(paramPairedData.differences(), 0.0D, H1.NOT_EQUAL);
    }

    public int getN() {
        return this.n1;
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.S;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble1 = {70.0D, 65.0D, 75.0D, 58.0D, 56.0D, 60.0D, 80.0D, 75.0D, 71.0D, 69.0D, 58.0D, 75.0D};
            double d = 60.0D;
            SignTest signTest = new SignTest(arrayOfDouble1, d, H1.NOT_EQUAL);
            System.out.println("H1: median not equal " + d + " N for test = " + signTest.getN() + " S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
            signTest = new SignTest(arrayOfDouble1, d, H1.LESS_THAN);
            System.out.println("H1: median < " + d + " N for test = " + signTest.getN() + " S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());

            double[] arrayOfDouble2 = {0.0D, 50.0D, 56.0D, 72.0D, 80.0D, 80.0D, 80.0D, 99.0D, 101.0D, 110.0D, 110.0D, 110.0D, 120.0D, 140.0D, 144.0D, 145.0D, 150.0D, 180.0D, 201.0D, 210.0D, 220.0D, 240.0D, 290.0D, 309.0D, 320.0D, 325.0D, 400.0D, 500.0D, 507.0D};
            d = 115.0D;
            signTest = new SignTest(arrayOfDouble2, d, H1.GREATER_THAN);
            System.out.println("H1: median > " + d + " N for test = " + signTest.getN() + " S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());

            double[] arrayOfDouble3 = {70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D};
            double[] arrayOfDouble4 = {75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D};
            PairedData pairedData = new PairedData(arrayOfDouble3, arrayOfDouble4);
            signTest = new SignTest(pairedData);
            System.out.println("H1: averages not equal: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
            signTest = new SignTest(pairedData, H1.LESS_THAN);
            System.out.println("H1: average A < average B: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
            signTest = new SignTest(pairedData, H1.GREATER_THAN);
            System.out.println("H1: average A > average B: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());

            double[] arrayOfDouble5 = {17.4D, 15.7D, 12.9D, 9.8D, 13.4D, 18.7D, 13.9D, 11.0D, 5.4D, 10.4D, 16.4D, 5.6D};
            double[] arrayOfDouble6 = {13.6D, 10.1D, 10.3D, 9.2D, 11.1D, 20.4D, 10.4D, 11.4D, 4.9D, 8.9D, 11.2D, 4.8D};
            pairedData = new PairedData(arrayOfDouble5, arrayOfDouble6);
            signTest = new SignTest(pairedData, H1.GREATER_THAN);
            System.out.println("H1: average A > average B: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());

            double[] arrayOfDouble7 = {4.0D, 4.0D, 5.0D, 5.0D, 3.0D, 2.0D, 5.0D, 3.0D, 1.0D, 5.0D, 5.0D, 5.0D, 4.0D, 5.0D, 5.0D, 5.0D, 5.0D};
            double[] arrayOfDouble8 = {2.0D, 3.0D, 3.0D, 3.0D, 3.0D, 3.0D, 3.0D, 3.0D, 2.0D, 3.0D, 2.0D, 2.0D, 5.0D, 2.0D, 5.0D, 3.0D, 1.0D};
            pairedData = new PairedData(arrayOfDouble7, arrayOfDouble8);
            signTest = new SignTest(pairedData, H1.GREATER_THAN);
            System.out.println("H1: average A < average B: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
        }
    }
}

