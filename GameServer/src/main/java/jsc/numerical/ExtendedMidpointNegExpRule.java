package jsc.numerical;

public class ExtendedMidpointNegExpRule
        implements IntegratingFunction {
    private double s;

    public double getIntegral(Function paramFunction, double paramDouble1, double paramDouble2, int paramInt) throws NumericalException {
        paramDouble2 = (paramDouble2 == 0.0D) ? 1.0D : Math.exp(paramDouble2);
        paramDouble1 = 0.0D;

        if (paramInt == 1) {

            double d = 0.5D * (paramDouble1 + paramDouble2);
            return this.s = (paramDouble2 - paramDouble1) * paramFunction.function(Math.log(d)) / d;
        }
        int i;
        byte b;
        for (i = 1, b = 1; b < paramInt - 1; ) {
            i *= 3;
            b++;
        }
        double d2 = i;
        double d4 = (paramDouble2 - paramDouble1) / 3.0D * d2;
        double d5 = d4 + d4;
        double d1 = paramDouble1 + 0.5D * d4;
        double d3 = 0.0D;
        for (b = 1; b <= i; b++) {

            if (d1 <= 0.0D) throw new NumericalException("-infinity");
            d3 += paramFunction.function(Math.log(d1)) / d1;
            d1 += d5;
            d3 += paramFunction.function(Math.log(d1)) / d1;
            d1 += d4;
        }
        this.s = (this.s + (paramDouble2 - paramDouble1) * d3 / d2) / 3.0D;
        return this.s;
    }
}

