package jsc.swt.control;

import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class IntegerLogSlider
extends JSlider
{
double dminVal;
double logRange;

public IntegerLogSlider(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
super(0);
if (paramInt1 <= 0 || paramInt2 <= paramInt1)
throw new IllegalArgumentException("Illegal range for logarithmic slider."); 
this.dminVal = paramInt1;
this.logRange = Math.log(paramInt2 / this.dminVal);
setMinimum(0);

setMaximum(paramInt4);

setValue(getSliderValue(paramInt3));

setPaintTicks(false);

int i = paramArrayOfint.length;
Hashtable hashtable = new Hashtable(i);

for (byte b = 0; b < i; b++) {

int j = paramArrayOfint[b];
if (j >= paramInt1 && j <= paramInt2)
hashtable.put(new Integer(getSliderValue(j)), new JLabel(Integer.toString(j))); 
} 
setLabelTable(hashtable);
setPaintLabels(true);
}

public int getIntegerValue() {
return getIntegerValue(getValue());
}

int getIntegerValue(int paramInt) {
return (int)Math.round(this.dminVal * Math.exp(paramInt * this.logRange / getMaximum()));
}

int getSliderValue(int paramInt) {
double d = getMaximum() * Math.log(paramInt / this.dminVal) / this.logRange;

return (int)Math.round(d);
}

public void setIntegerValue(int paramInt) {
setValue(getSliderValue(paramInt));
}
}

