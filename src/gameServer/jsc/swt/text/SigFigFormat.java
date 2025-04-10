package jsc.swt.text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.StringTokenizer;

public class SigFigFormat
extends RealFormat
{
private static final double TOLERANCE = 8.0E-15D;
int n;
double minDecimal;
double maxDecimal;

public SigFigFormat(int paramInt) {
this(paramInt, Locale.getDefault());
}

public SigFigFormat(int paramInt, Locale paramLocale) {
super(paramLocale);
this.decimalFormat.setMinimumIntegerDigits(1);
setSignificantDigits(paramInt);
}

public String format(double paramDouble) {
double d2;
if (Double.isNaN(paramDouble)) return "NaN"; 
double d1 = Math.abs(paramDouble);
if (d1 == 0.0D) return "0"; 
String str1 = this.scientificFormat.format(paramDouble);

if (d1 < this.minDecimal || d1 >= this.maxDecimal) return str1;

if (d1 < 1.0D)
{
if (d1 >= 0.1D) {

this.decimalFormat.setMaximumFractionDigits(this.n);
return this.decimalFormat.format(paramDouble);
} 
}

StringTokenizer stringTokenizer = new StringTokenizer(str1, "E");
String str2 = stringTokenizer.nextToken();

String str3 = stringTokenizer.nextToken();

int i = Integer.parseInt(str3);

try { Number number = this.localFormat.parse(str2); d2 = number.doubleValue(); }
catch (ParseException parseException) { d2 = Double.NaN; }

int j = str2.length() - 1 - i;
if (paramDouble < 0.0D) j--; 
this.decimalFormat.setMaximumFractionDigits(j);

return this.decimalFormat.format(d2 * Math.pow(10.0D, i));
}

public int getSignificantDigits() {
return this.n;
}
public Number parse(String paramString) throws ParseException {
try {
return this.decimalFormat.parse(paramString);
} catch (ParseException parseException) {
return this.scientificFormat.parse(paramString);
} 
}

public double round(double paramDouble) {
String str = format(paramDouble); 
try { Number number = parse(str); return number.doubleValue(); }
catch (ParseException parseException) { return Double.NaN; }

}

public void setSignificantDigits(int paramInt) {
if (paramInt < 1 || paramInt > 16)
throw new IllegalArgumentException("Invalid number of significant figures."); 
this.n = paramInt;

StringBuffer stringBuffer = new StringBuffer("0.");
for (byte b = 0; b < paramInt - 1; ) { stringBuffer.append('#'); b++; }
stringBuffer.append("E0");

this.scientificFormat.applyPattern(stringBuffer.toString());

this.minDecimal = Math.pow(10.0D, -paramInt);
this.maxDecimal = Math.pow(10.0D, paramInt);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble = { 1.0E-5D, 1.2E-4D, 0.00123D, 0.01234D, 0.12345D, 0.123456D, 1.23456D, 12.3456D, 12345.0D, 123456.0D, 100000.0D, 0.0D, 1.23456789E8D, 0.123D, 5.0E-4D, 6.0E-4D, 6.789E-4D, 5.67E-4D, 0.003454D, 0.0034501D, 0.00345005D, 0.00345004D, 0.0025000000000000005D, 0.003000000000000001D, 1.234567E-14D };

byte b1 = 5;
DecimalFormat decimalFormat = new DecimalFormat("##########.####################");

SigFigFormat sigFigFormat = new SigFigFormat(b1);

Object object = null;

for (byte b2 = 0; b2 < arrayOfDouble.length; b2++) {
for (byte b = 0; b < 2; b++) {

double d = (b == 0) ? arrayOfDouble[b2] : -arrayOfDouble[b2];
String str = sigFigFormat.format(d);
System.out.println(decimalFormat.format(d) + " formatted to " + b1 + " sig.figs. is " + str);
} 
} 
}
}
}

