package jsc.swt.plot2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VirtualTransform;

public abstract class TickMark
implements PlotObject
{
public static final int DEFAULT_SIZE = 5;
VPoint p;
int size;
Color colour;
Stroke stroke;

public TickMark(VPoint paramVPoint) {
this(paramVPoint, 5, Color.black, new BasicStroke());
}

public TickMark(VPoint paramVPoint, int paramInt) {
this(paramVPoint, paramInt, Color.black, new BasicStroke());
}

public TickMark(VPoint paramVPoint, int paramInt, Color paramColor) {
this(paramVPoint, paramInt, paramColor, new BasicStroke());
}

public TickMark(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
this.p = paramVPoint;
setSize(paramInt);
this.colour = paramColor;
this.stroke = paramStroke;
}

public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
return false;
}

public void setColour(Color paramColor) {
this.colour = paramColor;
}

public void setLocation(VPoint paramVPoint) {
this.p = paramVPoint;
}

public void setSize(int paramInt) {
this.size = paramInt;
}

public void setStroke(Stroke paramStroke) {
this.stroke = paramStroke;
}
}

