package jsc.ci;

public class AbstractConfidenceInterval
implements ConfidenceInterval
{
protected double confidenceCoeff;
protected double lowerLimit;
protected double upperLimit;

public AbstractConfidenceInterval() {
setConfidenceCoeff(0.95D);
}

public AbstractConfidenceInterval(double paramDouble) {
setConfidenceCoeff(paramDouble);
}

public AbstractConfidenceInterval(double paramDouble1, double paramDouble2, double paramDouble3) {
this(paramDouble1);
this.lowerLimit = paramDouble2;
this.upperLimit = paramDouble3;
}

public double getAlpha() {
return 1.0D - this.confidenceCoeff;
}

public double getConfidenceCoeff() {
return this.confidenceCoeff;
}

public double getConfidenceLevel() {
return 100.0D * this.confidenceCoeff;
}

public double getLowerLimit() {
return this.lowerLimit;
}

public double getUpperLimit() {
return this.upperLimit;
}

public void setConfidenceCoeff(double paramDouble) {
if (paramDouble <= 0.0D || paramDouble >= 1.0D)
throw new IllegalArgumentException("Invalid confidence coefficient."); 
this.confidenceCoeff = paramDouble;
}

public String toString() {
return new String((100.0D * this.confidenceCoeff) + "% confidence interval [" + this.lowerLimit + ", " + this.upperLimit + "]");
}
}

