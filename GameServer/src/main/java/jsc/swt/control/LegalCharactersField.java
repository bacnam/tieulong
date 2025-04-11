package jsc.swt.control;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class LegalCharactersField
        extends JTextField {
    private Toolkit toolkit;
    private String legalCharacters;

    public LegalCharactersField(int paramInt, String paramString) {
        super(paramInt);
        this.legalCharacters = paramString;
        this.toolkit = Toolkit.getDefaultToolkit();
        setBackground(Color.white);
    }

    protected Document createDefaultModel() {
        return new LegalCharactersDocument(this);
    }

    public void setLegalCharacters(String paramString) {
        this.legalCharacters = paramString;
    }

    protected class LegalCharactersDocument extends PlainDocument {
        private final LegalCharactersField this$0;

        protected LegalCharactersDocument(LegalCharactersField this$0) {
            this.this$0 = this$0;
        }

        public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
            char[] arrayOfChar1 = param1String.toCharArray();
            char[] arrayOfChar2 = new char[arrayOfChar1.length];
            byte b1 = 0;

            for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {

                if (this.this$0.legalCharacters.indexOf(Character.toLowerCase(arrayOfChar1[b2])) >= 0) {
                    arrayOfChar2[b1++] = arrayOfChar1[b2];
                } else {

                    this.this$0.toolkit.beep();
                }
            }

            super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
        }
    }

}

