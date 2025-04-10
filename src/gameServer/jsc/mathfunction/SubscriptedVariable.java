package jsc.mathfunction;

public final class SubscriptedVariable
implements MathFunctionVariables
{
private final int n;
private final int firstSubscript;
private final int lastSubscript;
private final String name;
private double[] x;

public SubscriptedVariable(String paramString, int paramInt1, int paramInt2) {
this.n = 1 + paramInt2 - paramInt1;
if (this.n < 1)
throw new IllegalArgumentException("Invalid subscripts."); 
this.x = new double[this.n];
this.name = paramString;
this.firstSubscript = paramInt1;
this.lastSubscript = paramInt2;
}

public SubscriptedVariable(String paramString, int paramInt) {
this(paramString, 1, paramInt);
}

public int getNumberOfVariables() {
return this.n;
}

public String getVariableName(int paramInt) {
return this.name + (paramInt + this.firstSubscript);
}

public int getVariableSubscript(int paramInt) {
return paramInt + this.firstSubscript;
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

