package jsc.distributions;

import jsc.descriptive.DoubleTally;
import jsc.goodnessfit.ChiSquaredFitTest;

public class FriedmanM
        extends RankSumOfSquares {
    public FriedmanM(int paramInt1, int paramInt2) {
        super(paramInt1, paramInt2);

        double d = 12.0D * (paramInt2 - 1.0D) / (paramInt1 * paramInt2) * ((paramInt2 * paramInt2) - 1.0D);
        for (byte b = 0; b < getValueCount(); ) {
            this.values[b] = d * this.values[b];
            b++;
        }

    }

    public String toString() {
        return new String("Friedman's M distribution: n = " + this.n + ", k = " + this.k + ".");
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            byte b2 = 10, b3 = 3;

            char c = '✐';
            FriedmanM friedmanM = new FriedmanM(10, 3);

            double[] arrayOfDouble = new double[c];
            for (byte b1 = 0; b1 < c; ) {
                arrayOfDouble[b1] = friedmanM.random();
                b1++;
            }
            ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new DoubleTally(arrayOfDouble), friedmanM, 0);
            System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
            System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
        }
    }
}

