package jsc.onesample;

import jsc.ci.AbstractConfidenceInterval;
import jsc.descriptive.MeanVar;
import jsc.distributions.ChiSquared;

public class NormalVarianceCI
        extends AbstractConfidenceInterval {
    private final double var;

    public NormalVarianceCI(double[] paramArrayOfdouble, double paramDouble) {
        super(paramDouble);
        MeanVar meanVar = new MeanVar(paramArrayOfdouble);
        int i = meanVar.getN() - 1;
        ChiSquared chiSquared = new ChiSquared(i);
        double d1 = 1.0D - paramDouble;
        double d2 = 0.5D * d1;
        double d3 = chiSquared.inverseCdf(d2);
        double d4 = chiSquared.inverseCdf(1.0D - d2);

        this.var = meanVar.getVariance();

        double d5 = i * this.var;
        this.lowerLimit = d5 / d4;
        this.upperLimit = d5 / d3;
    }

    public double getVariance() {
        return this.var;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble = {293.7D, 296.2D, 296.4D, 294.0D, 297.3D, 293.7D, 294.3D, 291.3D, 295.1D, 296.1D};
            NormalVarianceCI normalVarianceCI = new NormalVarianceCI(arrayOfDouble, 0.9D);
            System.out.println("Variance = " + normalVarianceCI.getVariance() + " CI = [" + normalVarianceCI.getLowerLimit() + ", " + normalVarianceCI.getUpperLimit() + "]");
        }
    }
}

