package jsc.swt.plot2d;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VRectangle;
import jsc.swt.virtualgraphics.VirtualTransform;

public class PlotText
implements PlotObject
{
public static final int CENTER = 0;
public static final int ABOVE = 1;
public static final int BELOW = 2;
public static final int LEFT = 3;
public static final int RIGHT = 4;
static Color defaultColour = Color.black;

static Font defaultFont = new Font("SansSerif", 0, 12);

static int defaultGap = 1;

String string;

VPoint anchorPoint;

Font font;

Color colour;

int orientation;

int gap;

double angrad;

public PlotText(String paramString, VPoint paramVPoint) {
this(paramString, paramVPoint, 0, defaultGap, defaultColour, defaultFont, 0.0D);
}

public PlotText(String paramString, VPoint paramVPoint, int paramInt) {
this(paramString, paramVPoint, paramInt, defaultGap, defaultColour, defaultFont, 0.0D);
}

public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2) {
this(paramString, paramVPoint, paramInt1, paramInt2, defaultColour, defaultFont, 0.0D);
}

public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2, double paramDouble) {
this(paramString, paramVPoint, paramInt1, paramInt2, defaultColour, defaultFont, paramDouble);
}

public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor) {
this(paramString, paramVPoint, paramInt1, paramInt2, paramColor, defaultFont, 0.0D);
}

public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor, Font paramFont) {
this(paramString, paramVPoint, paramInt1, paramInt2, paramColor, paramFont, 0.0D);
}

public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor, Font paramFont, double paramDouble) {
if (paramInt1 < 0 || paramInt1 > 4) {
throw new IllegalArgumentException("Invalid orientation.");
}
this.string = paramString;
this.anchorPoint = paramVPoint;
this.orientation = paramInt1;
this.angrad = -Math.toRadians(paramDouble);
this.gap = paramInt2;
this.colour = paramColor;
this.font = paramFont;
}

public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
return false;
}

public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
paramGraphics2D.setColor(this.colour);
paramGraphics2D.setFont(this.font);

Point point = new Point();
paramVirtualTransform.transform((Point2D)this.anchorPoint, point);

AffineTransform affineTransform1 = paramGraphics2D.getTransform();

AffineTransform affineTransform2 = AffineTransform.getTranslateInstance(point.x, point.y);
if (this.angrad != 0.0D) affineTransform2.rotate(this.angrad);

FontMetrics fontMetrics = paramGraphics2D.getFontMetrics();

switch (this.orientation) {

case 1:
affineTransform2.translate((-fontMetrics.stringWidth(this.string) / 2), -this.gap);
break;
case 4:
affineTransform2.translate(this.gap, ((fontMetrics.getAscent() - fontMetrics.getDescent()) / 2));
break;
case 2:
affineTransform2.translate((-fontMetrics.stringWidth(this.string) / 2), (fontMetrics.getAscent() + this.gap));
break;
case 3:
affineTransform2.translate((-fontMetrics.stringWidth(this.string) - this.gap), ((fontMetrics.getAscent() - fontMetrics.getDescent()) / 2));
break;
default:
affineTransform2.translate((-fontMetrics.stringWidth(this.string) / 2), ((fontMetrics.getAscent() - fontMetrics.getDescent()) / 2));
break;
} 

paramGraphics2D.transform(affineTransform2);

paramGraphics2D.drawString(this.string, 0, 0);
paramGraphics2D.setTransform(affineTransform1);
}

public VPoint getAnchorPoint() {
return this.anchorPoint;
}

public void setAnchorPoint(VPoint paramVPoint) {
this.anchorPoint = paramVPoint;
}

public double getAngle() {
return -Math.toDegrees(this.angrad);
}

public void setAngle(double paramDouble) {
this.angrad = -Math.toRadians(paramDouble);
}

public Color getColour() {
return this.colour;
}

public void setColour(Color paramColor) {
this.colour = paramColor;
}

public int getGap() {
return this.gap;
}

public void setGap(int paramInt) {
this.gap = paramInt;
}

public Font getFont() {
return this.font;
}

public void setFont(Font paramFont) {
this.font = paramFont;
}

public int getOrientation() {
return this.orientation;
}

public void setOrientation(int paramInt) {
if (paramInt < 0 || paramInt > 4)
throw new IllegalArgumentException("Invalid orientation."); 
this.orientation = paramInt;
}

public String getString() {
return this.string;
}

public void setString(String paramString) {
this.string = paramString;
}

public void setDefaultColour(Color paramColor) {
defaultColour = paramColor;
}

public void setDefaultFont(Font paramFont) {
defaultFont = paramFont;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
JFrame jFrame = new JFrame("PlotText Test");

PlotPanel plotPanel = new PlotPanel(new Dimension(500, 400), new VRectangle(-50.0D, -50.0D, 100.0D, 100.0D));
plotPanel.setZoomable(true);

plotPanel.setFocusable(true);

byte b1 = 5;
VPoint vPoint = new VPoint(-20.0D, -10.0D);
Font font = new Font("SansSerif", 0, 24);

plotPanel.addObject(new StandardMarker(vPoint, 0, 10, Color.red, new BasicStroke(1.0F)));
plotPanel.addObject(new PlotText("North horizontal text", vPoint, 1, b1, Color.black, font));
plotPanel.addObject(new PlotText("South horizontal text", vPoint, 2, b1, Color.black, font));
plotPanel.addObject(new PlotText("East horizontal text", vPoint, 4, b1, Color.black, font));
plotPanel.addObject(new PlotText("West horizontal text", vPoint, 3, b1, Color.black, font));

vPoint = new VPoint(20.0D, 0.0D);
plotPanel.addObject(new StandardMarker(vPoint, 6, 10, Color.red, new BasicStroke(1.0F)));
plotPanel.addObject(new PlotText("North vertical text", vPoint, 1, b1, Color.black, font, 90.0D));
plotPanel.addObject(new PlotText("South vertical text", vPoint, 2, b1, Color.black, font, 90.0D));
plotPanel.addObject(new PlotText("East vertical text", vPoint, 4, b1, Color.black, font, 90.0D));
plotPanel.addObject(new PlotText("West vertical text", vPoint, 3, b1, Color.black, font, 90.0D));

vPoint = new VPoint(-25.0D, 25.0D);
plotPanel.addObject(new StandardMarker(vPoint, 4, 6, Color.red));
for (byte b2 = 0; b2 < 'Ũ'; b2 += 30) {

PlotText plotText = new PlotText("Rotated text " + b2, vPoint, 4, 7, b2);

plotPanel.addObject(plotText);
} 

Container container = jFrame.getContentPane();
container.setLayout(new BorderLayout());
container.add(plotPanel, "Center");

jFrame.setVisible(true);
jFrame.setDefaultCloseOperation(3);
jFrame.pack();
}
}
}

