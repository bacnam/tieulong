package jsc.swt.control;

import javax.swing.*;
import java.awt.*;

public class PosIntegerLabelledField
        extends JPanel {
    public PosIntegerField field;
    public JLabel label;

    public PosIntegerLabelledField(String paramString, int paramInt1, int paramInt2) {
        this(paramString, paramInt1, paramInt2, 10, 2147483647);
    }

    public PosIntegerLabelledField(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        setLayout(new GridLayout(1, 2, 5, 5));
        this.label = new JLabel(paramString, 2);
        setOpaque(false);
        this.label.setOpaque(false);
        this.field = new PosIntegerField(paramInt1, paramInt2, paramInt3, paramInt4);
        add(this.label);
        add(this.field);
    }

    public int getValue() {
        return this.field.getValue();
    }

    public void setValue(int paramInt) {
        this.field.setValue(paramInt);
    }
}

