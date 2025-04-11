package jsc.swt.panel;

import javax.swing.*;
import java.awt.*;

public class TransparentChildPanel
        extends JPanel {
    public TransparentChildPanel(LayoutManager paramLayoutManager) {
        super(paramLayoutManager);
    }

    public void transparentComponents() {
        Component[] arrayOfComponent = getComponents();
        for (byte b = 0; b < arrayOfComponent.length; b++) {

            JComponent jComponent = (JComponent) arrayOfComponent[b];
            jComponent.setOpaque(false);
        }
    }
}

