package jsc.swt.text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class IntegerFormat
{
protected DecimalFormat decimalFormat;

public IntegerFormat() {
this(Locale.getDefault());
}

public IntegerFormat(Locale paramLocale) {
NumberFormat numberFormat = NumberFormat.getInstance(paramLocale);
if (numberFormat instanceof DecimalFormat) {
this.decimalFormat = (DecimalFormat)numberFormat;
} else {
throw new IllegalArgumentException("DecimalFormat not available for locale.");
} 
this.decimalFormat.setParseIntegerOnly(true);
this.decimalFormat.setMinimumIntegerDigits(1);
setGroupingUsed(false);
}

public String format(int paramInt) {
return this.decimalFormat.format(paramInt);
}

public Number parse(String paramString) throws ParseException {
return this.decimalFormat.parse(paramString);
}

public void setGroupingSeparator(char paramChar) {
DecimalFormatSymbols decimalFormatSymbols = this.decimalFormat.getDecimalFormatSymbols();
decimalFormatSymbols.setGroupingSeparator(paramChar);
this.decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
}

public void setGroupingUsed(boolean paramBoolean) {
this.decimalFormat.setGroupingUsed(paramBoolean);
}
}

