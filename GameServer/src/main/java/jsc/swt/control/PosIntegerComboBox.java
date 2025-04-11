package jsc.swt.control;

import javax.swing.*;

public class PosIntegerComboBox
        extends JComboBox {
    public PosIntegerComboBox(int paramInt1, int paramInt2, int paramInt3) {
        if (paramInt1 < 0)
            throw new IllegalArgumentException("Negative minimum.");
        for (byte b = 0; b < 1 + paramInt2 - paramInt1; ) {
            addItem((E) new Integer(paramInt1 + b));
            b++;
        }
        setEditor(new PosIntegerComboBoxEditor(paramInt3));
    }

    public int getValue() {
        Integer integer = (Integer) getSelectedItem();
        return integer.intValue();
    }

    public void setValue(int paramInt) {
        setSelectedItem(new Integer(paramInt));
    }

    public void setFocusAccelerator(char paramChar) {
        ComboBoxEditor comboBoxEditor = getEditor();
        JTextField jTextField = (JTextField) comboBoxEditor.getEditorComponent();
        jTextField.setFocusAccelerator(paramChar);
    }
}

