package jsc.onesample;

import jsc.ci.AbstractConfidenceInterval;
import jsc.descriptive.MeanVar;
import jsc.distributions.Normal;

public class LargeSampleMeanCI
        extends AbstractConfidenceInterval {
    private final MeanVar mv;

    public LargeSampleMeanCI(double[] paramArrayOfdouble, double paramDouble) {
        super(paramDouble);
        Normal normal = new Normal();
        double d1 = 1.0D - paramDouble;
        double d2 = normal.inverseCdf(1.0D - 0.5D * d1);

        this.mv = new MeanVar(paramArrayOfdouble);
        double d3 = this.mv.getMean();

        double d4 = d2 * this.mv.getSd() / Math.sqrt(this.mv.getN());
        this.lowerLimit = d3 - d4;
        this.upperLimit = d3 + d4;
    }

    public double getMean() {
        return this.mv.getMean();
    }

    public double getSd() {
        return this.mv.getSd();
    }
}

