package jsc.swt.control;

import javax.swing.*;

public class IntegerSlider
        extends JSlider {
    public IntegerSlider(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        super(0, paramInt1, paramInt2, paramInt3);
        setMinorTickSpacing(paramInt4);
        setMajorTickSpacing(paramInt5);
        setPaintTicks(true);

        setPaintLabels(true);
    }
}

