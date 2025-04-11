package jsc.swt.control;

import javax.swing.*;

public class IntegerComboBox
        extends JComboBox {
    public IntegerComboBox(int paramInt1, int paramInt2, int paramInt3) {
        for (byte b = 0; b < 1 + paramInt2 - paramInt1; ) {
            addItem((E) new Integer(paramInt1 + b));
            b++;
        }
        setEditor(new IntegerComboBoxEditor(paramInt3));
    }

    public int getValue() {
        Integer integer = (Integer) getSelectedItem();
        return integer.intValue();
    }

    public void setValue(int paramInt) {
        setSelectedItem(new Integer(paramInt));
    }
}

