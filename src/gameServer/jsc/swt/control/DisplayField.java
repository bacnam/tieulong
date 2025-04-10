package jsc.swt.control;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.ParseException;
import javax.swing.JTextField;
import jsc.swt.text.IntegerFormat;
import jsc.swt.text.MaxWidthFormat;
import jsc.swt.text.RealFormat;

public class DisplayField
extends JTextField
{
private RealFormat realFormatter;
private IntegerFormat integerFormatter;
private Toolkit toolkit;

public DisplayField(String paramString, int paramInt) {
this(paramString, paramInt, (RealFormat)new MaxWidthFormat(7));
}

public DisplayField(String paramString, int paramInt, RealFormat paramRealFormat) {
this(paramString, paramInt, new IntegerFormat(), paramRealFormat);
}

public DisplayField(String paramString, int paramInt, IntegerFormat paramIntegerFormat, RealFormat paramRealFormat) {
super(paramString, paramInt);
setEditable(false);
setBackground(Color.white);
this.toolkit = Toolkit.getDefaultToolkit();

this.integerFormatter = paramIntegerFormat;

this.realFormatter = paramRealFormat;
}

public int getIntegerValue() {
int i = 0;
try {
i = this.integerFormatter.parse(getText()).intValue();
} catch (ParseException parseException) {
this.toolkit.beep();
}  return i;
}

public double getRealValue() {
double d = 0.0D;
try {
d = this.realFormatter.parse(getText()).doubleValue();
} catch (ParseException parseException) {
this.toolkit.beep();
}  return d;
}

public boolean isFocusable() {
return false;
}

public void setGroupingSeparator(char paramChar) {
this.integerFormatter.setGroupingSeparator(paramChar);
this.realFormatter.setGroupingSeparator(paramChar);
}

public void setGroupingUsed(boolean paramBoolean) {
this.integerFormatter.setGroupingUsed(paramBoolean);
this.realFormatter.setGroupingUsed(paramBoolean);
}

public void setIntegerFormat(IntegerFormat paramIntegerFormat) {
this.integerFormatter = paramIntegerFormat;
}

public void setIntegerValue(int paramInt) {
setText(this.integerFormatter.format(paramInt));
}

public void setRealFormat(RealFormat paramRealFormat) {
this.realFormatter = paramRealFormat;
}

public void setRealValue(double paramDouble) {
setText(this.realFormatter.format(paramDouble));
}
}

