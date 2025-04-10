package jsc.swt.text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public abstract class RealFormat
{
public DecimalFormat decimalFormat;
public DecimalFormat scientificFormat;
DecimalFormat localFormat;

public RealFormat(Locale paramLocale) {
NumberFormat numberFormat1 = NumberFormat.getInstance(paramLocale);
if (numberFormat1 instanceof DecimalFormat) {
this.decimalFormat = (DecimalFormat)numberFormat1;
} else {
throw new IllegalArgumentException("DecimalFormat not available for locale.");
}  NumberFormat numberFormat2 = NumberFormat.getInstance(paramLocale);
this.scientificFormat = (DecimalFormat)numberFormat2;
NumberFormat numberFormat3 = NumberFormat.getInstance(paramLocale);
this.localFormat = (DecimalFormat)numberFormat3;
setGroupingUsed(false);
}

public char getDecimalSeparator() {
DecimalFormatSymbols decimalFormatSymbols = this.localFormat.getDecimalFormatSymbols();
return decimalFormatSymbols.getDecimalSeparator();
}

public char getGroupingSeparator() {
DecimalFormatSymbols decimalFormatSymbols = this.localFormat.getDecimalFormatSymbols();
return decimalFormatSymbols.getGroupingSeparator();
}

public abstract String format(double paramDouble);

public abstract Number parse(String paramString) throws ParseException;

public void setGroupingSeparator(char paramChar) {
DecimalFormatSymbols decimalFormatSymbols = this.decimalFormat.getDecimalFormatSymbols();
decimalFormatSymbols.setGroupingSeparator(paramChar);
this.decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
this.scientificFormat.setDecimalFormatSymbols(decimalFormatSymbols);
this.localFormat.setDecimalFormatSymbols(decimalFormatSymbols);
}

public void setGroupingUsed(boolean paramBoolean) {
this.decimalFormat.setGroupingUsed(paramBoolean);
this.scientificFormat.setGroupingUsed(paramBoolean);
this.localFormat.setGroupingUsed(paramBoolean);
}
}

