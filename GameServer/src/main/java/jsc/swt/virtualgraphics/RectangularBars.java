package jsc.swt.virtualgraphics;

public class RectangularBars
extends Path
{
public RectangularBars(VPoint[] paramArrayOfVPoint, double paramDouble) {
super(4 * paramArrayOfVPoint.length);
int i = paramArrayOfVPoint.length;

double d = 0.5D * paramDouble;
for (byte b = 0; b < i; b++) {

VRectangle vRectangle = new VRectangle((paramArrayOfVPoint[b]).x - d, 0.0D, paramDouble, (paramArrayOfVPoint[b]).y);
this.path.append(vRectangle, false);
} 
}
}

