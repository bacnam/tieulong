package jsc.swt.text;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class StatisticalDocument
extends DefaultStyledDocument
{
private IntegerFormat integerFormatter = new IntegerFormat();
private RealFormat realFormatter = new SigFigFormat(5);

public SimpleAttributeSet attributes = new SimpleAttributeSet();
public void add(String paramString) {
insertString(getLength(), paramString);
} public void add(int paramInt) {
add(this.integerFormatter.format(paramInt));
} public void add(double paramDouble) {
add(this.realFormatter.format(paramDouble));
}

public void add(Component paramComponent) {
SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
StyleConstants.setComponent(simpleAttributeSet, paramComponent);
insertString(getLength(), " ", simpleAttributeSet);
}

public void add(DefaultStyledDocument paramDefaultStyledDocument) {
for (byte b = 0; b < paramDefaultStyledDocument.getLength(); b++) {

try {
String str = paramDefaultStyledDocument.getText(b, 1);
AttributeSet attributeSet = paramDefaultStyledDocument.getCharacterElement(b).getAttributes();
insertString(getLength(), str, attributeSet);
}
catch (BadLocationException badLocationException) {}
} 
}

public void addBreak(int paramInt) {
add("\n");
if (paramInt > 0) {

int i = getFontSize();
setFontSize(paramInt);
add("\n");
setFontSize(i);
} 
}
public void addBullet() {
bold("•");
} public void addDash() {
bold("–");
}

public void addDegrees() {
String str = getFontFamily();
int i = getFontSize();
setFontFamily("Times");

add("°");
setFontFamily(str);
setFontSize(i);
}

public void addDiv() {
String str = getFontFamily();
int i = getFontSize();
setFontFamily("Times");

add("÷");
setFontFamily(str);
setFontSize(i);
}

public void addDivider() {
add("\n");
add(new Divider(this));
add("\n");
}

public void addMinus() {
String str = getFontFamily();
int i = getFontSize();
setFontFamily("Symbol");

add("−");
setFontFamily(str);
setFontSize(i);
}

public void addMult() {
String str = getFontFamily();
int i = getFontSize();
setFontFamily("Times");

add("×");
setFontFamily(str);
setFontSize(i);
}

public void addPM() {
String str = getFontFamily();
int i = getFontSize();
setFontFamily("Times");

add("±");
setFontFamily(str);
setFontSize(i);
}

public void addSqrt() {
String str = getFontFamily();
int i = getFontSize();
setFontFamily("Symbol");

add("√");
setFontFamily(str);
setFontSize(i);
}

public void addSymbol(int paramInt) {
String str = getFontFamily();
int i = getFontSize();
setFontFamily("Symbol");

add("" + (char)paramInt);
setFontFamily(str);
setFontSize(i);
}

public String getMatchString() {
return getMatchString(true);
}

public String getMatchString(boolean paramBoolean) {
StringBuffer stringBuffer = new StringBuffer();
char c = Character.MIN_VALUE;
for (byte b = 0; b < getLength(); b++) {

try {
String str = getText(b, 1);
if (!paramBoolean || !str.equals(" ")) {
AttributeSet attributeSet = getCharacterElement(b).getAttributes();

char c1 = Character.MIN_VALUE;
if (StyleConstants.isSuperscript(attributeSet)) { c1 = '^'; }
else if (StyleConstants.isSubscript(attributeSet)) { c1 = '¬'; }

if (c1 != '\000') {

if (c != c1) stringBuffer.append(c1); 
c = c1;
}
else if (c != '\000') {

c = Character.MIN_VALUE;
stringBuffer.append(':');
} 

if (str.equals("^")) { stringBuffer.append('^'); }
else if (str.equals("¬")) { stringBuffer.append('¬'); }
else if (str.equals(":")) { stringBuffer.append(':'); }

stringBuffer.append(str);
} 
} catch (BadLocationException badLocationException) {}
} 
return stringBuffer.toString();
}

public static String greek(char paramChar) {
return "" + (char)(paramChar - 65 + 913);
}
public void insertString(int paramInt, String paramString, AttributeSet paramAttributeSet) {

try { super.insertString(paramInt, paramString, paramAttributeSet); } catch (BadLocationException badLocationException) {}
}
public void insertString(int paramInt, String paramString) {
insertString(paramInt, paramString, this.attributes);
}
public void reset() {
try {
remove(0, getLength());
} catch (BadLocationException badLocationException) {}
}

public void bold(String paramString) { bold(); add(paramString); unbold(); }
public void bold() { StyleConstants.setBold(this.attributes, true); } public void unbold() {
StyleConstants.setBold(this.attributes, false);
}
public void italic(String paramString) { italic(); add(paramString); unitalic(); }
public void italic() { StyleConstants.setItalic(this.attributes, true); } public void unitalic() {
StyleConstants.setItalic(this.attributes, false);
}
public void underline(String paramString) { underline(); add(paramString); ununderline(); }
public void underline() { StyleConstants.setUnderline(this.attributes, true); } public void ununderline() {
StyleConstants.setUnderline(this.attributes, false);
}
public void subscript(String paramString) { subscript(); add(paramString); unsubscript(); }
public void subscript() { StyleConstants.setSubscript(this.attributes, true); } public void unsubscript() {
StyleConstants.setSubscript(this.attributes, false);
}
public void superscript(String paramString) { superscript(); add(paramString); unsuperscript(); }
public void superscript() { StyleConstants.setSuperscript(this.attributes, true); } public void unsuperscript() {
StyleConstants.setSuperscript(this.attributes, false);
}
public void setAlignmentLeft() { setAlignment(0); }
public void setAlignmentCentre() { setAlignment(1); }
public void setAlignmentRight() { setAlignment(2); } public void setAlignment(int paramInt) {
setAlignment(paramInt, 0, getLength());
}
public void setAlignment(int paramInt1, int paramInt2, int paramInt3) {
StyleConstants.setAlignment(this.attributes, paramInt1);
setParagraphAttributes(paramInt2, paramInt3, this.attributes, false);
}

public void setFontSize(int paramInt) { StyleConstants.setFontSize(this.attributes, paramInt); } public int getFontSize() {
return StyleConstants.getFontSize(this.attributes);
}
public void setFontFamily(String paramString) { StyleConstants.setFontFamily(this.attributes, paramString); } public String getFontFamily() {
return StyleConstants.getFontFamily(this.attributes);
} public Font getFont() {
return getFont(this.attributes);
}

public void setRealFormat(RealFormat paramRealFormat) {
this.realFormatter = paramRealFormat;
}

class Divider
extends Component
{
private final StatisticalDocument this$0;

Divider(StatisticalDocument this$0) {
this.this$0 = this$0;
} public void paint(Graphics param1Graphics) {
param1Graphics.drawLine(0, 0, getWidth(), 0);
}

public Dimension getMaximumSize() {
Dimension dimension = new Dimension(getSize());
dimension.width = 32767;
return dimension;
}

public Dimension getSize() {
Dimension dimension = new Dimension(1, 1);
setSize(dimension);
return dimension;
}
public Dimension getMinimumSize() { return getSize(); } public Dimension getPreferredSize() {
return getSize();
}
}
}

