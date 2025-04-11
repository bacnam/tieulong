package jsc.swt.panel;

import jsc.Utilities;

import javax.swing.*;
import java.awt.*;

public class GraphicBackgroundPanel
        extends TransparentChildPanel {
    ImageIcon tile;

    public GraphicBackgroundPanel(LayoutManager paramLayoutManager, ImageIcon paramImageIcon) {
        super(paramLayoutManager);
        this.tile = paramImageIcon;
    }

    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
        Utilities.tile(paramGraphics, this, this.tile);
    }
}

