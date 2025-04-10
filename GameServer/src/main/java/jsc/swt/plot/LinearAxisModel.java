package jsc.swt.plot;

import jsc.swt.text.PatternFormat;
import jsc.swt.text.RealFormat;
import jsc.util.Scale;

public class LinearAxisModel
implements AxisModel
{
String label;
private Scale scale;
private RealFormat tickLabelFormatter;

public LinearAxisModel(String paramString1, Scale paramScale, String paramString2) {
this(paramString1, paramScale, (RealFormat)new PatternFormat(paramString2));
}

public LinearAxisModel(String paramString, Scale paramScale, RealFormat paramRealFormat) {
this.label = paramString;
this.scale = paramScale;

this.tickLabelFormatter = paramRealFormat;
}

public LinearAxisModel() {
this("", new Scale(), "");
}
public Object clone() {
return copy();
}

public LinearAxisModel copy() {
return new LinearAxisModel(this.label, this.scale, this.tickLabelFormatter);
}

public double getLength() {
return this.scale.getLength();
} public double getMin() { return this.scale.getMin(); }
public double getMax() { return this.scale.getMax(); }
public int getTickCount() { return this.scale.getNumberOfTicks(); }
public double getFirstTickValue() { return this.scale.getFirstTickValue(); } public double getLastTickValue() {
return this.scale.getLastTickValue();
}

public String getTickLabel(int paramInt) {
double d = this.scale.getTickValue(paramInt);

if (d == 0.0D) return "0";

return this.tickLabelFormatter.format(d);
}

public double getTickValue(int paramInt) {
return this.scale.getTickValue(paramInt);
}
public String getLabel() { return this.label; } public void setLabel(String paramString) {
this.label = paramString;
}

public double getStep() {
return this.scale.getStep();
}

public void setTickLabelFormat(RealFormat paramRealFormat) {
this.tickLabelFormatter = paramRealFormat;
}
}

