package jsc.swt.virtualgraphics;

import java.awt.geom.Ellipse2D;

public class Ellipse
extends Ellipse2D.Double
{
public Ellipse(VPoint paramVPoint, VDimension paramVDimension) {
this(paramVPoint.x - paramVDimension.width, paramVPoint.y - paramVDimension.height, 2.0D * paramVDimension.width, 2.0D * paramVDimension.height);
}

public Ellipse(VPoint paramVPoint1, VPoint paramVPoint2) {
this(paramVPoint1.x, paramVPoint1.y, paramVPoint2.x - paramVPoint1.x, paramVPoint1.y - paramVPoint2.y);
}

public Ellipse(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
super(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
}
public String toString() {
return " x = " + this.x + ", y = " + this.y + ", width = " + this.width + ", height = " + this.height;
}
}

