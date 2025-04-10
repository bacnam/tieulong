package jsc.swt.control;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class PosIntegerField
extends JTextField
{
private Toolkit toolkit;
private NumberFormat integerFormatter;
int maxDigits;
int maxValue;

public PosIntegerField(int paramInt1, int paramInt2) {
this(paramInt1, paramInt2, 9, 999999999);
}

public PosIntegerField(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
super(paramInt2);
this.maxDigits = paramInt3;
this.maxValue = paramInt4;
this.toolkit = Toolkit.getDefaultToolkit();
this.integerFormatter = NumberFormat.getNumberInstance(Locale.UK);
this.integerFormatter.setParseIntegerOnly(true);
this.integerFormatter.setGroupingUsed(false);
setBackground(Color.white);
setValue(paramInt1);
}

public int getValue() {
int i = 0;

try {
i = this.integerFormatter.parse(getText()).intValue();

}
catch (ParseException parseException) {

this.toolkit.beep();
} 
if (i > this.maxValue || i < 0) {
i = this.maxValue; setText((new Integer(this.maxValue)).toString());
} 
return i;
}

public void setValue(int paramInt) {
setText(this.integerFormatter.format(paramInt));
}

protected Document createDefaultModel() {
return new WholeNumberDocument(this);
} protected class WholeNumberDocument extends PlainDocument { protected WholeNumberDocument(PosIntegerField this$0) {
this.this$0 = this$0;
}

private final PosIntegerField this$0;

public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
if (getLength() + param1String.length() > this.this$0.maxDigits) { this.this$0.toolkit.beep(); return; }

char[] arrayOfChar1 = param1String.toCharArray();
char[] arrayOfChar2 = new char[arrayOfChar1.length];
byte b1 = 0;

for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {

if (Character.isDigit(arrayOfChar1[b2])) {
arrayOfChar2[b1++] = arrayOfChar1[b2];
} else {

this.this$0.toolkit.beep();
} 
} 

super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
} }

}

