package jsc.swt.plot;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.plaf.basic.BasicSliderUI;

public class BeadSliderUI
extends BasicSliderUI
{
Color colour = Color.black;
Color focusColour = Color.black;

public BeadSliderUI() {
super(null);
}

protected Color getFocusColor() {
return this.focusColour;
}

public void paintThumb(Graphics paramGraphics) {
paramGraphics.setColor(this.colour);

int i = this.thumbRect.y + this.thumbRect.height / 2;

int j = this.contentRect.width;
paramGraphics.fillOval(this.contentRect.x, i - j / 2, j, j);
}

public void setBeadColour(Color paramColor) {
this.colour = paramColor;
}

public void setFocusColour(Color paramColor) {
this.focusColour = paramColor;
}
}

