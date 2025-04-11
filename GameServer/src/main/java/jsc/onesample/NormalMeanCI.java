package jsc.onesample;

import jsc.ci.AbstractConfidenceInterval;
import jsc.datastructures.PairedData;
import jsc.descriptive.MeanVar;
import jsc.distributions.StudentsT;

public class NormalMeanCI
        extends AbstractConfidenceInterval {
    private final MeanVar mv;

    public NormalMeanCI(double[] paramArrayOfdouble, double paramDouble) {
        super(paramDouble);
        this.mv = new MeanVar(paramArrayOfdouble);
        int i = this.mv.getN();
        StudentsT studentsT = new StudentsT((i - 1));
        double d1 = 1.0D - paramDouble;
        double d2 = studentsT.inverseCdf(1.0D - 0.5D * d1);

        double d3 = this.mv.getMean();

        double d4 = d2 * this.mv.getSd() / Math.sqrt(i);
        this.lowerLimit = d3 - d4;
        this.upperLimit = d3 + d4;
    }

    public NormalMeanCI(PairedData paramPairedData, double paramDouble) {
        this(paramPairedData.differences(), paramDouble);
    }

    public double getMean() {
        return this.mv.getMean();
    }

    public double getSd() {
        return this.mv.getSd();
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble1 = {4.9D, 5.1D, 4.6D, 5.0D, 5.1D, 4.7D, 4.4D, 4.7D, 4.6D};
            NormalMeanCI normalMeanCI = new NormalMeanCI(arrayOfDouble1, 90.0D);
            System.out.println("CI = [" + normalMeanCI.getLowerLimit() + ", " + normalMeanCI.getUpperLimit() + "]");

            double[] arrayOfDouble2 = {70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D};
            double[] arrayOfDouble3 = {75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D};
            normalMeanCI = new NormalMeanCI(new PairedData(arrayOfDouble2, arrayOfDouble3), 0.9D);
            System.out.println("CI = [" + normalMeanCI.getLowerLimit() + ", " + normalMeanCI.getUpperLimit() + "]");
        }
    }
}

