package jsc.swt.plot2d;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import jsc.swt.virtualgraphics.VPoint;

public final class StandardMarker
extends Marker
{
public static final int ASTERISK = 0;
public static final int CIRCLE = 1;
public static final int CROSS = 2;
public static final int DIAMOND = 3;
public static final int FILLED_CIRCLE = 4;
public static final int FILLED_DIAMOND = 5;
public static final int FILLED_SQUARE = 6;
public static final int PIXEL = 7;
public static final int PLUS = 8;
public static final int SQUARE = 9;
static final int TYPE_COUNT = 10;
static final String[] names = new String[] { "Asterisk", "Circle", "Cross", "Diamond", "Filled circle", "Filled diamond", "Filled square", "Pixel", "Plus", "Square" };

int type;

public StandardMarker(VPoint paramVPoint, int paramInt) {
this(paramVPoint, paramInt, Marker.defaultSize, Marker.defaultColour, Marker.defaultStroke);
}

public StandardMarker(VPoint paramVPoint, int paramInt1, int paramInt2) {
this(paramVPoint, paramInt1, paramInt2, Marker.defaultColour, Marker.defaultStroke);
}

public StandardMarker(VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor) {
this(paramVPoint, paramInt1, paramInt2, paramColor, Marker.defaultStroke);
}

public StandardMarker(VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor, Stroke paramStroke) {
super(paramVPoint, paramInt2, paramColor, paramStroke);
setType(paramInt1);
}

public Shape getShape(Point2D.Float paramFloat) {
GeneralPath generalPath;
switch (this.type) {

case 1:
case 4:
return new Ellipse2D.Float(paramFloat.x - this.halfSize, paramFloat.y - this.halfSize, this.size, this.size);
case 8:
generalPath = new GeneralPath(1, 2);
generalPath.moveTo(paramFloat.x, paramFloat.y - this.halfSize);
generalPath.lineTo(paramFloat.x, paramFloat.y + this.halfSize);
generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y);
generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y);
return generalPath;
case 0:
generalPath = new GeneralPath(1, 4);
generalPath.moveTo(paramFloat.x, paramFloat.y - this.halfSize);
generalPath.lineTo(paramFloat.x, paramFloat.y + this.halfSize);
generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y);
generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y);
generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y - this.halfSize);
generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y + this.halfSize);
generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y + this.halfSize);
generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y - this.halfSize);
return generalPath;
case 2:
generalPath = new GeneralPath(1, 2);
generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y - this.halfSize);
generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y + this.halfSize);
generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y + this.halfSize);
generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y - this.halfSize);
return generalPath;
case 3:
case 5:
generalPath = new GeneralPath(1, 4);
generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y);
generalPath.lineTo(paramFloat.x, paramFloat.y - this.halfSize);
generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y);
generalPath.lineTo(paramFloat.x, paramFloat.y + this.halfSize);
generalPath.closePath();
return generalPath;
case 6:
case 9:
return new Rectangle2D.Float(paramFloat.x - this.halfSize, paramFloat.y - this.halfSize, this.size, this.size);
case 7:
return new Rectangle2D.Float(paramFloat.x - 0.5F, paramFloat.y - 0.5F, 1.0F, 1.0F);
} 
throw new IllegalArgumentException("Invalid marker type.");
}

public int getType() {
return this.type;
}

public void setType(int paramInt) {
if (paramInt < 0 || paramInt >= 10)
throw new IllegalArgumentException("Invalid marker type."); 
this.type = paramInt;
if (paramInt == 4 || paramInt == 6 || paramInt == 5) {
this.filled = true;
} else {
this.filled = false;
} 
}

public static int getType(String paramString) {
for (byte b = 0; b < 10; ) { if (paramString.equals(names[b])) return b;  b++; }
return -1;
}

public static int getTypeCount() {
return 10;
}

public static String getTypeName(int paramInt) {
if (paramInt < 0 || paramInt >= names.length) return null; 
return names[paramInt];
}
}

