package jsc.swt.plot2d;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import jsc.swt.virtualgraphics.VPoint;

public final class HorizontalBarMarker
extends Marker
{
public HorizontalBarMarker(VPoint paramVPoint, int paramInt) {
super(paramVPoint, paramInt);
}

public HorizontalBarMarker(VPoint paramVPoint, int paramInt, Color paramColor) {
super(paramVPoint, paramInt, paramColor);
}

public HorizontalBarMarker(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
super(paramVPoint, paramInt, paramColor, paramStroke);
}

public Shape getShape(Point2D.Float paramFloat) {
float f = 0.5F * this.size;
return new Line2D.Float(paramFloat.x - f, paramFloat.y, paramFloat.x + f, paramFloat.y);
}
}

