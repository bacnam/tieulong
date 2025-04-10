package jsc.swt.virtualgraphics;

public class HorizontalBars
extends Path
{
public HorizontalBars(VPoint[] paramArrayOfVPoint, double paramDouble) {
super(4 * paramArrayOfVPoint.length);
int i = paramArrayOfVPoint.length;

double d = 0.5D * paramDouble;

for (byte b = 0; b < i; b++) {
VRectangle vRectangle;
double d1 = (paramArrayOfVPoint[b]).x;
if (d1 < 0.0D) {
vRectangle = new VRectangle(d1, (paramArrayOfVPoint[b]).y - d, -d1, paramDouble);
} else {
vRectangle = new VRectangle(0.0D, (paramArrayOfVPoint[b]).y - d, d1, paramDouble);
}  this.path.append(vRectangle, false);
} 
}
}

