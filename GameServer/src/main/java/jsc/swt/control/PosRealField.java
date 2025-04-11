package jsc.swt.control;

import jsc.swt.text.MaxWidthFormat;
import jsc.swt.text.RealFormat;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.text.ParseException;

public class PosRealField
        extends JTextField {
    JTextField field;
    private Toolkit toolkit;
    private RealFormat realFormatter;
    private char decimalSeparator;

    public PosRealField(double paramDouble, int paramInt, RealFormat paramRealFormat) {
        super(paramInt);
        this.toolkit = Toolkit.getDefaultToolkit();

        this.realFormatter = paramRealFormat;
        this.field = this;
        setBackground(Color.white);
        this.decimalSeparator = paramRealFormat.getDecimalSeparator();
        setValue(paramDouble);
    }

    public PosRealField(double paramDouble, int paramInt) {
        this(paramDouble, paramInt, (RealFormat) new MaxWidthFormat(7));
    }

    public double getValue() {
        try {
            return this.realFormatter.parse(getText()).doubleValue();

        } catch (ParseException parseException) {

            this.toolkit.beep();

            return 0.0D;
        }
    }

    public void setValue(double paramDouble) {
        setText(this.realFormatter.format(paramDouble));
    }

    public void setRealFormat(RealFormat paramRealFormat) {
        this.realFormatter = paramRealFormat;
    }

    protected Document createDefaultModel() {
        return new RealNumberDocument(this);
    }

    protected class RealNumberDocument extends PlainDocument {
        private final PosRealField this$0;

        protected RealNumberDocument(PosRealField this$0) {
            this.this$0 = this$0;
        }

        public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
            char[] arrayOfChar1 = param1String.toCharArray();
            char[] arrayOfChar2 = new char[arrayOfChar1.length];
            byte b1 = 0;

            for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {

                char c = Character.toUpperCase(arrayOfChar1[b2]);
                if (Character.isDigit(c)) {
                    arrayOfChar2[b1++] = c;
                } else if (c == this.this$0.decimalSeparator) {

                    String str = this.this$0.field.getText();
                    if (str.indexOf(this.this$0.decimalSeparator) < 0) {
                        arrayOfChar2[b1++] = c;
                    }
                } else if (c == 'E') {

                    String str = this.this$0.field.getText().toUpperCase();
                    if (str.indexOf('E') < 0) {
                        arrayOfChar2[b1++] = c;
                    }
                } else if (c == '-' && param1Int > 0) {

                    String str = this.this$0.field.getText().toUpperCase();
                    if (str.indexOf('-') < 0 && str.indexOf('E') == param1Int - 1) {
                        arrayOfChar2[b1++] = c;
                    }
                } else {

                    this.this$0.toolkit.beep();
                }
            }

            super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
        }
    }

}

