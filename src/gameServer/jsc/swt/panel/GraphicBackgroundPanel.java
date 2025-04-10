package jsc.swt.panel;

import java.awt.Graphics;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import jsc.Utilities;

public class GraphicBackgroundPanel
extends TransparentChildPanel
{
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

