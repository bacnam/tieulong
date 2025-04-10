package jsc.swt.virtualgraphics;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class VirtualTransform
extends AffineTransform
{
public VirtualTransform(VRectangle paramVRectangle, Dimension paramDimension) {
scale(paramDimension.width / paramVRectangle.width, -paramDimension.height / paramVRectangle.height);
translate(-paramVRectangle.x, -paramVRectangle.y - paramVRectangle.height);
}

public VDimension pixelToVirtual(Dimension paramDimension) {
double d1 = getScaleX();
double d2 = getScaleY();
if (d1 == 0.0D || d2 == 0.0D)
throw new IllegalArgumentException("Zero scale. Probably \"device\" has no size because it has not been painted yet."); 
double d3 = paramDimension.width / d1;
double d4 = paramDimension.height / -d2;

return new VDimension(d3, d4);
}

public VPoint pixelToVirtual(Point paramPoint) {
VPoint vPoint = new VPoint(0.0D, 0.0D);
try {
return (VPoint)inverseTransform(paramPoint, vPoint);
}
catch (NoninvertibleTransformException noninvertibleTransformException) {

System.out.println(noninvertibleTransformException.getMessage());
return vPoint;
} 
}
}

