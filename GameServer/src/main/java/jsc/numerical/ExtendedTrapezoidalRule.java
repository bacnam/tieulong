package jsc.numerical;

public class ExtendedTrapezoidalRule
        implements IntegratingFunction {
    private double s;

    public double getIntegral(Function paramFunction, double paramDouble1, double paramDouble2, int paramInt) {
        if (paramInt == 1)
            return this.s = 0.5D * (paramDouble2 - paramDouble1) * (paramFunction.function(paramDouble1) + paramFunction.function(paramDouble2));
        int i;
        byte b;
        for (i = 1, b = 1; b < paramInt - 1; ) {
            i <<= 1;
            b++;
        }
        double d2 = i;
        double d4 = (paramDouble2 - paramDouble1) / d2;
        double d1 = paramDouble1 + 0.5D * d4;
        double d3;
        for (d3 = 0.0D, b = 1; b <= i; ) {
            d3 += paramFunction.function(d1);
            b++;
            d1 += d4;
        }
        this.s = 0.5D * (this.s + (paramDouble2 - paramDouble1) * d3 / d2);
        return this.s;
    }
}

