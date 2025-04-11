package jsc.mathfunction;

public final class SingleVariable
        implements MathFunctionVariables {
    private final String name;
    private double x;

    public SingleVariable(String paramString) {
        this.name = paramString;
    }

    public int getNumberOfVariables() {
        return 1;
    }

    public String getVariableName(int paramInt) {
        return this.name;
    }

    public double getVariableValue(int paramInt) {
        return this.x;
    }

    public void setVariableValue(double paramDouble) {
        this.x = paramDouble;
    }
}

