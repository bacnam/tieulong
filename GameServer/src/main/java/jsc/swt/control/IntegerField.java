package jsc.swt.control;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class IntegerField
        extends JTextField {
    private Toolkit toolkit;
    private NumberFormat integerFormatter;

    public IntegerField(int paramInt1, int paramInt2) {
        super(paramInt2);
        this.toolkit = Toolkit.getDefaultToolkit();
        this.integerFormatter = NumberFormat.getNumberInstance(Locale.UK);
        this.integerFormatter.setParseIntegerOnly(true);
        this.integerFormatter.setGroupingUsed(false);
        setBackground(Color.white);
        setValue(paramInt1);
    }

    public int getValue() {
        try {
            return this.integerFormatter.parse(getText()).intValue();

        } catch (ParseException parseException) {

            this.toolkit.beep();
            return 0;
        }
    }

    public void setValue(int paramInt) {
        setText(this.integerFormatter.format(paramInt));
    }

    protected boolean isNegative() throws BadLocationException {
        return (getText(0, 1).charAt(0) == '-');
    }

    protected Document createDefaultModel() {
        return new WholeNumberDocument(this);
    }

    protected class WholeNumberDocument extends PlainDocument {
        private final IntegerField this$0;

        protected WholeNumberDocument(IntegerField this$0) {
            this.this$0 = this$0;
        }

        public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
            char[] arrayOfChar1 = param1String.toCharArray();
            char[] arrayOfChar2 = new char[arrayOfChar1.length];

            if (getLength() + param1String.length() > ((this.this$0.isNegative() || (param1Int == 0 && arrayOfChar1[0] == '-')) ? 10 : 9)) {
                this.this$0.toolkit.beep();
                return;
            }
            byte b1 = 0;
            for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {

                if (Character.isDigit(arrayOfChar1[b2])) {
                    arrayOfChar2[b1++] = arrayOfChar1[b2];
                } else if (arrayOfChar1[b2] == '-' && param1Int == 0 && !this.this$0.isNegative()) {
                    arrayOfChar2[b1++] = arrayOfChar1[b2];
                } else {

                    this.this$0.toolkit.beep();
                }
            }

            super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
        }
    }

}

