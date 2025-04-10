package jsc.swt.control;

import java.text.DecimalFormat;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class RealSlider
extends JSlider
{
double minVal;
double maxVal;
double range;

public RealSlider(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, String paramString) {
super(0);
rescale(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramString);
}

public double getMaximumValue() {
return this.maxVal;
}

public double getMinimumValue() {
return this.minVal;
}

public double getRealValue() {
return this.minVal + this.range * getValue() / getMaximum();
}

public void rescale(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, String paramString) {
this.minVal = paramDouble1;
this.maxVal = paramDouble2;
this.range = paramDouble2 - paramDouble1;
int i = (int)(this.range / paramDouble4);
setMinimum(0);
setMaximum(i);

setValue((int)((paramDouble3 - paramDouble1) / paramDouble4));
setMinorTickSpacing((int)(paramDouble5 / paramDouble4));
setMajorTickSpacing((int)(paramDouble6 / paramDouble4));
if (paramDouble7 > 0.0D) {

int j = (int)(1.0D + this.range / paramDouble7);
int k = i / (j - 1);
setPaintTicks(true);

Hashtable hashtable = new Hashtable(j);

DecimalFormat decimalFormat = new DecimalFormat(paramString);
for (byte b = 0; b < j; b++) {

double d = paramDouble1 + b * paramDouble7;
hashtable.put(new Integer(b * k), new JLabel(decimalFormat.format(d)));
} 
setLabelTable(hashtable);
setPaintLabels(true);
} else {

setPaintLabels(false);
} 
}

int getSliderValue(double paramDouble) {
double d = (paramDouble - this.minVal) / this.range * getMaximum();

return (int)Math.round(d);
}

public void setLabels(double[] paramArrayOfdouble, String paramString) {
int i = paramArrayOfdouble.length;
setPaintTicks(true);

Hashtable hashtable = new Hashtable(i);
DecimalFormat decimalFormat = new DecimalFormat(paramString);
for (byte b = 0; b < i; b++)
hashtable.put(new Integer(getSliderValue(paramArrayOfdouble[b])), new JLabel(decimalFormat.format(paramArrayOfdouble[b]))); 
setLabelTable(hashtable);
setPaintLabels(true);
updateUI();
}

public void setRealValue(double paramDouble) {
setValue(getSliderValue(paramDouble));
}
}

