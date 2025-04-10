package jsc.swt.plot2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VirtualTransform;

public class VerticalTickMark
extends TickMark
{
public VerticalTickMark(VPoint paramVPoint) {
super(paramVPoint);
}

public VerticalTickMark(VPoint paramVPoint, int paramInt) {
super(paramVPoint, paramInt);
}

public VerticalTickMark(VPoint paramVPoint, int paramInt, Color paramColor) {
super(paramVPoint, paramInt, paramColor);
}

public VerticalTickMark(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
super(paramVPoint, paramInt, paramColor, paramStroke);
}

public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
Point2D.Float float_ = new Point2D.Float();
paramVirtualTransform.transform((Point2D)this.p, float_);
paramGraphics2D.setColor(this.colour);
paramGraphics2D.setStroke(this.stroke);
paramGraphics2D.draw(new Line2D.Float(float_.x, float_.y, float_.x, float_.y + this.size));
}
}

