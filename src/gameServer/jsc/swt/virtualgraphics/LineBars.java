package jsc.swt.virtualgraphics;

public class LineBars
extends Path
{
public LineBars(VPoint[] paramArrayOfVPoint) {
super(paramArrayOfVPoint.length);
int i = paramArrayOfVPoint.length;

for (byte b = 0; b < i; b++) {

float f = (float)(paramArrayOfVPoint[b]).x;
this.path.moveTo(f, 0.0F);
this.path.lineTo(f, (float)(paramArrayOfVPoint[b]).y);
} 
}
}

