package jsc.swt.virtualgraphics;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Path
implements Shape
{
GeneralPath path;

public Path() {
this.path = new GeneralPath();
}

public Path(int paramInt1, int paramInt2) {
this.path = new GeneralPath(paramInt1, paramInt2);
}

public Path(int paramInt) {
this.path = new GeneralPath(0, paramInt);
}

public Path(Shape paramShape) {
this.path = new GeneralPath(paramShape);
}

public void append(Shape paramShape, boolean paramBoolean) {
this.path.append(paramShape, paramBoolean);
}

public void closePath() {
this.path.closePath();
}

public Shape createTransformedShape(AffineTransform paramAffineTransform) {
return this.path.createTransformedShape(paramAffineTransform);
}

public void curveTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
this.path.curveTo((float)paramDouble1, (float)paramDouble2, (float)paramDouble3, (float)paramDouble4, (float)paramDouble5, (float)paramDouble6);
}

public void lineTo(double paramDouble1, double paramDouble2) {
this.path.lineTo((float)paramDouble1, (float)paramDouble2);
}

public void moveTo(double paramDouble1, double paramDouble2) {
this.path.moveTo((float)paramDouble1, (float)paramDouble2);
}

public void quadTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
this.path.quadTo((float)paramDouble1, (float)paramDouble2, (float)paramDouble3, (float)paramDouble4);
}

public void reset() {
this.path.reset();
}

public void setWindingRule(int paramInt) {
this.path.setWindingRule(paramInt);
}

public void transform(AffineTransform paramAffineTransform) {
this.path.transform(paramAffineTransform);
}

public boolean contains(double paramDouble1, double paramDouble2) { return this.path.contains(paramDouble1, paramDouble2); }
public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) { return this.path.contains(paramDouble1, paramDouble2, paramDouble3, paramDouble4); }
public boolean contains(Point2D paramPoint2D) { return this.path.contains(paramPoint2D); }
public boolean contains(Rectangle2D paramRectangle2D) { return this.path.contains(paramRectangle2D); }
public Rectangle getBounds() { return this.path.getBounds(); }
public Rectangle2D getBounds2D() { return this.path.getBounds2D(); }
public PathIterator getPathIterator(AffineTransform paramAffineTransform) { return this.path.getPathIterator(paramAffineTransform); }
public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble) { return this.path.getPathIterator(paramAffineTransform, paramDouble); }
public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) { return this.path.intersects(paramDouble1, paramDouble2, paramDouble3, paramDouble4); } public boolean intersects(Rectangle2D paramRectangle2D) {
return this.path.intersects(paramRectangle2D);
}
}

