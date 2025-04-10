package jsc.swt.plot2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VirtualTransform;

public abstract class Marker
implements PlotObject
{
static Color defaultColour = Color.black;

static int defaultSize = 7;

static Stroke defaultStroke = new BasicStroke();

boolean filled = false;

VPoint p;

int size;

float halfSize;

Color colour;

Stroke stroke;

public Marker(VPoint paramVPoint) {
this(paramVPoint, defaultSize, defaultColour, defaultStroke);
}

public Marker(VPoint paramVPoint, int paramInt) {
this(paramVPoint, paramInt, defaultColour, defaultStroke);
}

public Marker(VPoint paramVPoint, int paramInt, Color paramColor) {
this(paramVPoint, paramInt, paramColor, defaultStroke);
}

public Marker(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
this.p = paramVPoint;
setSize(paramInt);
this.colour = paramColor;
this.stroke = paramStroke;
}

public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
Point2D.Float float_ = new Point2D.Float();
paramVirtualTransform.transform(paramPoint2D, float_);

return getBoundary(float_).contains(paramPoint2D);
}

public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
Point2D.Float float_ = new Point2D.Float();
paramVirtualTransform.transform((Point2D)this.p, float_);
paramGraphics2D.setColor(this.colour);
paramGraphics2D.setStroke(this.stroke);
if (this.filled) {
paramGraphics2D.fill(getShape(float_));
} else {
paramGraphics2D.draw(getShape(float_));
} 
}

public Shape getBoundary(Point2D.Float paramFloat) {
Shape shape = getShape(paramFloat);
if (shape instanceof java.awt.geom.GeneralPath) {
return shape.getBounds2D();
}
return shape;
}

public Color getColour() {
return this.colour;
}

public void setColour(Color paramColor) {
this.colour = paramColor;
}

public VPoint getLocation() {
return this.p;
}

public void setLocation(VPoint paramVPoint) {
this.p = paramVPoint;
}

public abstract Shape getShape(Point2D.Float paramFloat);

public int getSize() {
return this.size;
}

public void setSize(int paramInt) {
this.size = paramInt;
this.halfSize = 0.5F * paramInt;
}

public Stroke getStroke() {
return this.stroke;
}

public void setStroke(Stroke paramStroke) {
this.stroke = paramStroke;
}

public boolean isFilled() {
return this.filled;
}

public void setFilled(boolean paramBoolean) {
this.filled = paramBoolean;
}

public void setType(int paramInt) {}

public static void setDefaultColour(Color paramColor) {
defaultColour = paramColor;
}

public static void setDefaultSize(int paramInt) {
defaultSize = paramInt;
}

public static void setDefaultStroke(Stroke paramStroke) {
defaultStroke = paramStroke;
}
}

