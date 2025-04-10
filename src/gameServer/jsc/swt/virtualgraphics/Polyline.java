package jsc.swt.virtualgraphics;

import java.util.Vector;

public class Polyline
extends Path
{
public Polyline(VPoint[] paramArrayOfVPoint) {
super(paramArrayOfVPoint.length);
this.path.moveTo((float)(paramArrayOfVPoint[0]).x, (float)(paramArrayOfVPoint[0]).y);
for (byte b = 1; b < paramArrayOfVPoint.length; b++) {
this.path.lineTo((float)(paramArrayOfVPoint[b]).x, (float)(paramArrayOfVPoint[b]).y);
}
}

public Polyline(Vector paramVector) {
super(paramVector.size());
VPoint vPoint = paramVector.elementAt(0);
this.path.moveTo((float)vPoint.x, (float)vPoint.y);
for (byte b = 1; b < paramVector.size(); b++) {

vPoint = paramVector.elementAt(b);
this.path.lineTo((float)vPoint.x, (float)vPoint.y);
} 
}

public Polyline(VPoint[] paramArrayOfVPoint, int paramInt) {
super(paramInt);

this.path.moveTo((float)(paramArrayOfVPoint[0]).x, (float)(paramArrayOfVPoint[0]).y);
for (byte b = 1; b < paramArrayOfVPoint.length; b++) {
this.path.lineTo((float)(paramArrayOfVPoint[b]).x, (float)(paramArrayOfVPoint[b]).y);
}
}

public Polyline(VPoint paramVPoint, int paramInt) {
super(paramInt);
this.path.moveTo((float)paramVPoint.x, (float)paramVPoint.y);
}

public void lineTo(VPoint paramVPoint) {
this.path.lineTo((float)paramVPoint.x, (float)paramVPoint.y);
}
}

