package jsc.swt.control;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class IllegalCharactersField
        extends JTextField {
    private Toolkit toolkit;
    private String illegalCharacters;

    public IllegalCharactersField(int paramInt, String paramString) {
        super(paramInt);
        this.illegalCharacters = paramString;
        this.toolkit = Toolkit.getDefaultToolkit();
        setBackground(Color.white);
    }

    protected Document createDefaultModel() {
        return new IllegalCharactersDocument(this);
    }

    public void setIllegalCharacters(String paramString) {
        this.illegalCharacters = paramString;
    }

    protected class IllegalCharactersDocument extends PlainDocument {
        private final IllegalCharactersField this$0;

        protected IllegalCharactersDocument(IllegalCharactersField this$0) {
            this.this$0 = this$0;
        }

        public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
            char[] arrayOfChar1 = param1String.toCharArray();
            char[] arrayOfChar2 = new char[arrayOfChar1.length];
            byte b1 = 0;

            for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {

                if (this.this$0.illegalCharacters.indexOf(Character.toLowerCase(arrayOfChar1[b2])) >= 0) {

                    this.this$0.toolkit.beep();
                } else {

                    arrayOfChar2[b1++] = arrayOfChar1[b2];
                }
            }
            super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
        }
    }

}

