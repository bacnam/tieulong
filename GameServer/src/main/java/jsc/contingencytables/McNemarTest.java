package jsc.contingencytables;

import jsc.distributions.ChiSquared;
import jsc.tests.SignificanceTest;

public class McNemarTest
        implements SignificanceTest {
    private double chiSquared;
    private double SP;

    public McNemarTest(ContingencyTable2x2 paramContingencyTable2x2) {
        this(paramContingencyTable2x2, true);
    }

    public McNemarTest(ContingencyTable2x2 paramContingencyTable2x2, boolean paramBoolean) {
        int i = paramContingencyTable2x2.getFrequency(0, 1);
        int j = paramContingencyTable2x2.getFrequency(1, 0);
        if (i + j == 0) {
            throw new IllegalArgumentException("Frequencies are zero.");
        }
        if (paramBoolean) {

            double d = (Math.abs(i - j) - 1);
            this.chiSquared = d * d / (i + j);
        } else {

            double d = (i - j);
            this.chiSquared = d * d / (i + j);
        }

        this.SP = ChiSquared.upperTailProb(this.chiSquared, 1.0D);
    }

    public double getSP() {
        return this.SP;
    }

    public double getTestStatistic() {
        return this.chiSquared;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            String[] arrayOfString1 = {"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "C", "C", "C", "C", "C", "C", "C"};
            String[] arrayOfString2 = {"A", "A", "A", "A", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "A", "A", "A", "A"};

            byte b1 = 2;
            ContingencyTable2x2[] arrayOfContingencyTable2x2 = new ContingencyTable2x2[b1];
            arrayOfContingencyTable2x2[0] = new ContingencyTable2x2(14, 5, 2, 2);

            arrayOfContingencyTable2x2[1] = new ContingencyTable2x2(arrayOfString1, arrayOfString2);
            for (byte b2 = 0; b2 < b1; b2++) {

                System.out.println(arrayOfContingencyTable2x2[b2].toString());
                McNemarTest mcNemarTest = new McNemarTest(arrayOfContingencyTable2x2[b2]);
                System.out.println("Chi-squared = " + mcNemarTest.getTestStatistic() + " SP = " + mcNemarTest.getSP());
            }
        }
    }
}

