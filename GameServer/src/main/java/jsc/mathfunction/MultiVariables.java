package jsc.mathfunction;

public final class MultiVariables
        implements MathFunctionVariables {
    private final int n;
    private String[] names;
    private double[] x;

    public MultiVariables(String[] paramArrayOfString) {
        this.n = paramArrayOfString.length;
        if (this.n < 1)
            throw new IllegalArgumentException("No names.");
        this.x = new double[this.n];
        this.names = new String[this.n];
        System.arraycopy(paramArrayOfString, 0, this.names, 0, this.n);
    }

    public int getNumberOfVariables() {
        return this.n;
    }

    public String getVariableName(int paramInt) {
        return this.names[paramInt];
    }

    public double getVariableValue(int paramInt) {
        return this.x[paramInt];
    }

    public void setVariableValue(int paramInt, double paramDouble) {
        if (paramInt < 0 || paramInt >= this.n)
            throw new IllegalArgumentException("Invalid variable index.");
        this.x[paramInt] = paramDouble;
    }
}

