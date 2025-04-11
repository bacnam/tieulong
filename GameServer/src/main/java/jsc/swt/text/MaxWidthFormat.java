package jsc.swt.text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public class MaxWidthFormat
        extends RealFormat {
    int maxWidth;

    public MaxWidthFormat(int paramInt) {
        this(paramInt, Locale.getDefault());
    }

    public MaxWidthFormat(int paramInt, Locale paramLocale) {
        super(paramLocale);
        this.decimalFormat.setMinimumIntegerDigits(1);
        setMaximumWidth(paramInt);
    }

    public String format(double paramDouble) {
        if (Double.isNaN(paramDouble)) return "NaN";
        if (Math.abs(paramDouble) == 0.0D) return "0";

        String str = this.decimalFormat.format(paramDouble);
        if (str.length() <= this.maxWidth) return str;

        int i = this.maxWidth;
        SigFigFormat sigFigFormat = new SigFigFormat(i);
        while (true) {
            sigFigFormat.setSignificantDigits(i--);
            str = sigFigFormat.format(paramDouble);
            if (str.length() <= this.maxWidth)
                return str;
        }
    }

    public Number parse(String paramString) throws ParseException {

        try {
            return this.decimalFormat.parse(paramString);
        } catch (ParseException parseException) {
            return this.scientificFormat.parse(paramString);
        }

    }

    public void setMaximumWidth(int paramInt) {
        if (paramInt < 7)
            throw new IllegalArgumentException("Width must be at least 7.");
        this.maxWidth = paramInt;
        this.decimalFormat.setMaximumFractionDigits(paramInt - 2);

        StringBuffer stringBuffer = new StringBuffer("0.");
        for (byte b = 0; b < paramInt - 6; ) {
            stringBuffer.append('#');
            b++;
        }
        stringBuffer.append("E0");

        this.scientificFormat.applyPattern(stringBuffer.toString());
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble = {1.0E-5D, 1.2E-4D, 0.00123D, 0.01234D, 0.12345D, 0.123456D, 1.23456D, 12.3456D, 12345.0D, 123456.0D, 100000.0D, 0.0D};

            byte b1 = 7;
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(16);
            decimalFormat.setMaximumIntegerDigits(16);
            MaxWidthFormat maxWidthFormat = new MaxWidthFormat(b1);

            for (byte b2 = 0; b2 < arrayOfDouble.length; b2++) {
                for (byte b = 0; b < 2; b++) {
                    Number number;
                    double d = (b == 0) ? arrayOfDouble[b2] : -arrayOfDouble[b2];
                    String str = maxWidthFormat.format(d);
                    System.out.println(decimalFormat.format(d) + " formatted to " + b1 + " characters is " + str);
                    try {
                        number = maxWidthFormat.parse(str);
                    } catch (ParseException parseException) {
                        System.out.println("Cannot parse " + str);
                    }
                    System.out.println(" " + str + " parsed is " + decimalFormat.format(number.doubleValue()));
                }
            }
        }
    }
}

