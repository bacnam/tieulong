package jsc.swt.text;

import java.text.ParseException;
import java.util.Locale;

public class PatternFormat
        extends RealFormat {
    public PatternFormat(String paramString) {
        this(paramString, Locale.getDefault());
    }

    public PatternFormat(String paramString, Locale paramLocale) {
        super(paramLocale);
        this.decimalFormat.applyPattern(paramString);
    }

    public String format(double paramDouble) {
        return this.decimalFormat.format(paramDouble);
    }

    public Number parse(String paramString) throws ParseException {
        return this.decimalFormat.parse(paramString);
    }
}

