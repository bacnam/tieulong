package jsc.mathfunction;

public final class TwoVariables
implements MathFunctionVariables
{
private final String xName;
private final String yName;
private double x;
private double y;

public TwoVariables(String paramString1, String paramString2) {
this.xName = paramString1; this.yName = paramString2;
}

public int getNumberOfVariables() {
return 2;
}

public String getVariableName(int paramInt) {
return (paramInt == 0) ? this.xName : this.yName;
}

public double getVariableValue(int paramInt) {
return (paramInt == 0) ? this.x : this.y;
}

public void setVariableValue(int paramInt, double paramDouble) {
if (paramInt == 0) { this.x = paramDouble; }
else if (paramInt == 1) { this.y = paramDouble; }
else
{ throw new IllegalArgumentException("Invalid variable index."); }

}
}

