package jsc.swt.plot2d;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import jsc.swt.control.RealField;
import jsc.swt.dialogue.FieldDialogue;
import jsc.swt.virtualgraphics.Polyline;
import jsc.swt.virtualgraphics.VDimension;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VRectangle;
import jsc.swt.virtualgraphics.VirtualTransform;

public class PlotPanel
extends JPanel
implements Cloneable
{
public static final Composite SEE_THRU = AlphaComposite.getInstance(3, 0.5F);

private static final String VT_ERROR_MESSAGE = "PlotPanel component must be displayed at this stage.";

protected VRectangle virtualSpace;

protected VirtualTransform vt = null;

private Composite objectComposite = AlphaComposite.SrcOver;

protected RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

protected Vector componentBounds;

protected Vector objects;

private Component parent;

private Color focusColour = Color.red;

private VRectangle zoomBox;

private Stack zoomBoxes;

private Color zoomBoxColour = Color.black;
private PlotShape zoomBoxObject;
private VPoint zoomBoxStart;
private Stroke zoomBoxStroke = new DashStroke(1.0F);

private KeyListener zoomKeyListener;

private MouseMotionListener zoomMotionListener;

private MouseListener zoomMouseListener;

public PlotPanel() {
this(new Dimension(300, 300), new VRectangle(0.0D, 0.0D, 1.0D, 1.0D));
}

public PlotPanel(Dimension paramDimension) {
this(paramDimension, new VRectangle(0.0D, 0.0D, 1.0D, 1.0D));
}

public PlotPanel(Dimension paramDimension, VRectangle paramVRectangle) {
super((LayoutManager)null, true);

this.parent = this;
setOpaque(true);
setBackground(Color.white);
setBorder(BorderFactory.createLineBorder(Color.black, 1));
setPreferredSize(paramDimension);
this.virtualSpace = paramVRectangle;

this.componentBounds = new Vector();
this.objects = new Vector();

addFocusListener(new KeyboardFocusListener(this));
}

public void ensureComponentCapacity(int paramInt) {
this.componentBounds.ensureCapacity(paramInt);
}

public void addComponent(Component paramComponent, VRectangle paramVRectangle) {
add(paramComponent); this.componentBounds.add(paramVRectangle);
}

public void addComponent(Component paramComponent, VPoint paramVPoint, VDimension paramVDimension) {
addComponent(paramComponent, new VRectangle(paramVPoint, paramVDimension));
}

public void removeComponent(int paramInt) {
remove(paramInt);
this.componentBounds.remove(paramInt);
repaint();
}

public void removeLastComponent() {
int i = getComponentCount() - 1;
if (i >= 0) removeComponent(i);

}

public void removeAllComponents() {
super.removeAll(); this.componentBounds.removeAllElements(); repaint();
}

public Vector getComponentBounds() {
return this.componentBounds;
}

public VRectangle getComponentBounds(int paramInt) {
return this.componentBounds.elementAt(paramInt);
}

public void setComponentBounds(int paramInt, VRectangle paramVRectangle) {
this.componentBounds.setElementAt(paramVRectangle, paramInt);
}

public void setComponentBounds(int paramInt, VPoint paramVPoint, VDimension paramVDimension) {
setComponentBounds(paramInt, new VRectangle(paramVPoint, paramVDimension));
}

public void ensureObjectCapacity(int paramInt) {
this.objects.ensureCapacity(paramInt);
}

public boolean isEmpty() {
return this.objects.isEmpty();
}

public void addObject(PlotObject paramPlotObject) {
this.objects.add(paramPlotObject);
}

public void addObjects(PlotObject[] paramArrayOfPlotObject) {
int i = paramArrayOfPlotObject.length;
this.objects.ensureCapacity(this.objects.capacity() + i);
for (byte b = 0; b < i; ) { this.objects.add(paramArrayOfPlotObject[b]); b++; }
repaint();
}

public PlotObject getObject(int paramInt) {
return this.objects.get(paramInt);
}

public PlotObject getObject(Point2D paramPoint2D) {
if (this.objects.isEmpty()) return null;

for (byte b = 0; b < this.objects.size(); b++) {

PlotObject plotObject = this.objects.elementAt(b);
if (plotObject.contains(paramPoint2D, this.vt)) return plotObject; 
} 
return null;
}

public int getObjectCount() {
return this.objects.size();
}

public int getObjectIndex(Point2D paramPoint2D) {
if (this.objects.isEmpty()) return -1;

for (byte b = 0; b < this.objects.size(); b++) {

PlotObject plotObject = this.objects.elementAt(b);
if (plotObject.contains(paramPoint2D, this.vt)) return b; 
} 
return -1;
}

public Vector getObjects() {
return this.objects;
}

public boolean removeObject(PlotObject paramPlotObject) {
return this.objects.remove(paramPlotObject);
}

public void removeObject(int paramInt) {
this.objects.remove(paramInt);
}

public void removeLastObject() {
int i = this.objects.size() - 1;
if (i >= 0) this.objects.remove(i);

}

public void removeAllObjects() {
this.objects.removeAllElements(); repaint();
}

public void setObjectComposite(Composite paramComposite) {
this.objectComposite = paramComposite;
}

public void addRenderingHint(RenderingHints.Key paramKey, Object paramObject) {
this.hints.add(new RenderingHints(paramKey, paramObject));
}

public RenderingHints getRenderingHints() {
return this.hints;
}

public Object removeRenderingHint(Object paramObject) {
return this.hints.remove(paramObject);
}

public Object clone() {
return copyPlot();
}

protected void copyContents(PlotPanel paramPlotPanel) {
paramPlotPanel.objects = (Vector)this.objects.clone();
paramPlotPanel.componentBounds = (Vector)this.componentBounds.clone();
for (byte b = 0; b < getComponentCount(); b++) {
paramPlotPanel.add(getComponent(b));
}
}

public PlotPanel copyPlot() {
PlotPanel plotPanel = new PlotPanel(getSize(), this.virtualSpace);

plotPanel.setDoubleBuffered(isDoubleBuffered());
plotPanel.setOpaque(isOpaque());
plotPanel.setBackground(getBackground());
plotPanel.setBorder(getBorder());

copyContents(plotPanel);

plotPanel.hints = (RenderingHints)this.hints.clone();
plotPanel.objectComposite = this.objectComposite;

plotPanel.focusColour = this.focusColour;

plotPanel.zoomBoxColour = this.zoomBoxColour;
plotPanel.zoomBoxStroke = this.zoomBoxStroke;

return plotPanel;
}

public BufferedImage getBufferedImage() {
BufferedImage bufferedImage = (BufferedImage)createImage(getWidth(), getHeight());
if (bufferedImage == null) return null; 
Graphics2D graphics2D = bufferedImage.createGraphics();
paintComponent(graphics2D);
return bufferedImage;
}

public double getParHeight(double paramDouble) {
int i = virtualToPixelWidth(paramDouble);
VDimension vDimension = pixelToVirtual(new Dimension(0, i));
return vDimension.height;
}

public double getParWidth(double paramDouble) {
int i = virtualToPixelHeight(paramDouble);
VDimension vDimension = pixelToVirtual(new Dimension(i, 0));
return vDimension.width;
}

public VRectangle getVirtualSpace() {
return this.virtualSpace;
}

public void setVirtualSpace(VRectangle paramVRectangle) {
this.virtualSpace = paramVRectangle;
}

public VirtualTransform getVirtualTransform() {
return this.vt;
}

public void merge(PlotPanel paramPlotPanel) {
byte b;
for (b = 0; b < paramPlotPanel.objects.size(); b++)
this.objects.add(paramPlotPanel.objects.elementAt(b)); 
for (b = 0; b < paramPlotPanel.componentBounds.size(); b++)
this.componentBounds.add(paramPlotPanel.componentBounds.elementAt(b)); 
for (b = 0; b < paramPlotPanel.getComponentCount(); b++) {
add(paramPlotPanel.getComponent(b));
}

Rectangle2D.Double double_ = (Rectangle2D.Double)this.virtualSpace.createUnion((Rectangle2D)paramPlotPanel.virtualSpace);
this.virtualSpace = new VRectangle(double_);

repaint();
}

protected void paintBackground(Graphics paramGraphics) {
super.paintComponent(paramGraphics);
}

public void paintComponent(Graphics paramGraphics) {
super.paintComponent(paramGraphics);
Graphics2D graphics2D = (Graphics2D)paramGraphics;
this.vt = new VirtualTransform(this.virtualSpace, getSize());

graphics2D.setRenderingHints(this.hints);

Color color = paramGraphics.getColor();
Paint paint = graphics2D.getPaint();
Stroke stroke = graphics2D.getStroke();
Font font = graphics2D.getFont();
Composite composite = graphics2D.getComposite();

if (!this.objects.isEmpty()) {

graphics2D.setComposite(this.objectComposite);

for (byte b = 0; b < this.objects.size(); b++) {

PlotObject plotObject = this.objects.elementAt(b);
plotObject.draw(graphics2D, this.vt);
} 
} 

paramGraphics.setColor(color);
graphics2D.setPaint(paint);
graphics2D.setStroke(stroke);
graphics2D.setFont(font);
graphics2D.setComposite(composite);

if (!this.componentBounds.isEmpty()) {

int i = getComponentCount();
if (i != this.componentBounds.size()) {
throw new NoSuchElementException("Mismatch between numbers of components and virtual bounds. Do not use Container methods to add/remove components. Do not add the same component to the plot and to another container.");
}

for (byte b = 0; b < i; b++) {

VRectangle vRectangle = this.componentBounds.elementAt(b);
Component component = getComponent(b);
Shape shape = this.vt.createTransformedShape((Shape)vRectangle);
component.setBounds(shape.getBounds());
} 
} 
}

public VDimension pixelToVirtual(Dimension paramDimension) {
this.vt = new VirtualTransform(this.virtualSpace, getSize());
return this.vt.pixelToVirtual(paramDimension);
}

public VPoint pixelToVirtual(Point paramPoint) {
this.vt = new VirtualTransform(this.virtualSpace, getSize());
return this.vt.pixelToVirtual(paramPoint);
}

public void removeAll() {
this.objects.removeAllElements();
super.removeAll();
this.componentBounds.removeAllElements();
repaint();
}

public boolean isAntialiased() {
return (this.hints.get(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON);
}

public void setAntialiasing(boolean paramBoolean) {
this.hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, paramBoolean ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
}

public boolean isTextAntialiased() {
return (this.hints.get(RenderingHints.KEY_TEXT_ANTIALIASING) == RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
}

public void setTextAntialiasing(boolean paramBoolean) {
this.hints.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, paramBoolean ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));
}

public void setFocusColour(Color paramColor) {
this.focusColour = paramColor;
}

public void setZoomable(boolean paramBoolean) {
if (paramBoolean) {

this.zoomBoxes = new Stack();
this.zoomMotionListener = new ZoomMotionListener(this);
this.zoomMouseListener = new ZoomMouseListener(this);
this.zoomKeyListener = new ZoomKeyListener(this);
addMouseMotionListener(this.zoomMotionListener);
addMouseListener(this.zoomMouseListener);
addKeyListener(this.zoomKeyListener);
}
else {

removeMouseMotionListener(this.zoomMotionListener);
removeMouseListener(this.zoomMouseListener);
removeKeyListener(this.zoomKeyListener);
} 
}

public void setZoomBoxColour(Color paramColor) {
this.zoomBoxColour = paramColor;
}

public void setZoomBoxStroke(Stroke paramStroke) {
this.zoomBoxStroke = paramStroke;
}

public Dimension virtualToPixel(VDimension paramVDimension) {
this.vt = new VirtualTransform(this.virtualSpace, getSize());
int i = (int)(paramVDimension.width * this.vt.getScaleX());
int j = (int)(-paramVDimension.height * this.vt.getScaleY());
return new Dimension(i, j);
}

public Point virtualToPixel(VPoint paramVPoint) {
Point point = new Point();

this.vt = new VirtualTransform(this.virtualSpace, getSize());
return (Point)this.vt.transform((Point2D)paramVPoint, point);
}

public int virtualToPixelHeight(double paramDouble) {
this.vt = new VirtualTransform(this.virtualSpace, getSize());
return (int)(-paramDouble * this.vt.getScaleY());
}

public int virtualToPixelWidth(double paramDouble) {
this.vt = new VirtualTransform(this.virtualSpace, getSize());
return (int)(paramDouble * this.vt.getScaleX());
}

public boolean write(File paramFile) throws IOException {
String str = paramFile.getName();
String[] arrayOfString = ImageIO.getWriterFormatNames();
for (byte b = 0; b < arrayOfString.length; b++) {
if (str.endsWith("." + arrayOfString[b])) return write(paramFile, arrayOfString[b]); 
}  return false;
}

public boolean write(File paramFile, String paramString) throws IOException {
return ImageIO.write(getBufferedImage(), paramString, paramFile);
}

private void zoomIn() {
if (this.zoomBox.isEmpty())
return;  this.zoomBoxes.push(this.virtualSpace);

setVirtualSpace(this.zoomBox);
repaint();
}

private void zoomOut() {
if (this.zoomBoxes.empty())
return; 
setVirtualSpace(this.zoomBoxes.pop());
repaint();
}
class KeyboardFocusListener extends FocusAdapter { private final PlotPanel this$0;
KeyboardFocusListener(PlotPanel this$0) {
this.this$0 = this$0;
}
public void focusGained(FocusEvent param1FocusEvent) {
this.this$0.setBorder(BorderFactory.createLineBorder(this.this$0.focusColour, 1));
} public void focusLost(FocusEvent param1FocusEvent) {
this.this$0.setBorder(BorderFactory.createLineBorder(Color.black, 1));
} }

class ZoomKeyListener extends KeyAdapter { ZoomKeyListener(PlotPanel this$0) {
this.this$0 = this$0;
}
private final PlotPanel this$0;

public void keyTyped(KeyEvent param1KeyEvent) {
char c = param1KeyEvent.getKeyChar();

if (c == 'z' || c == 'Z') {

FieldDialogue fieldDialogue = new FieldDialogue(this.this$0.parent, "Zooming rectangle", "Enter origin & dimensions of zooming box.");

RealField realField1 = new RealField(this.this$0.virtualSpace.x, 10);
RealField realField2 = new RealField(this.this$0.virtualSpace.y, 10);
RealField realField3 = new RealField(this.this$0.virtualSpace.width, 10);
RealField realField4 = new RealField(this.this$0.virtualSpace.height, 10);
fieldDialogue.addField("Lower-left corner x", (JTextField)realField1, 0);
fieldDialogue.addField("Lower-left corner y", (JTextField)realField2, 1);
fieldDialogue.addField("Width", (JTextField)realField3, 2);
fieldDialogue.addField("Height", (JTextField)realField4, 3);

fieldDialogue.setLocationRelativeTo(null);

if (fieldDialogue.show() == null)
return;  double d1 = realField1.getValue();
double d2 = realField2.getValue();
double d3 = realField3.getValue();
double d4 = realField4.getValue();
this.this$0.zoomBox = new VRectangle(d1, d2, d3, d4);
this.this$0.zoomIn();
param1KeyEvent.consume();
}
else if (c == 'o' || c == 'O') {

this.this$0.zoomOut();
param1KeyEvent.consume();
} else {
return;
} 
} }
class ZoomMouseListener extends MouseAdapter { private final PlotPanel this$0;

ZoomMouseListener(PlotPanel this$0) {
this.this$0 = this$0;
}

public void mousePressed(MouseEvent param1MouseEvent) {
if (SwingUtilities.isRightMouseButton(param1MouseEvent) || param1MouseEvent.isShiftDown()) {
this.this$0.zoomOut(); return;
}  this.this$0.zoomBoxStart = this.this$0.pixelToVirtual(param1MouseEvent.getPoint());
this.this$0.removeObject(this.this$0.zoomBoxObject);
this.this$0.zoomBox = new VRectangle(this.this$0.zoomBoxStart, this.this$0.zoomBoxStart);
this.this$0.zoomBoxObject = new PlotShape((Shape)this.this$0.zoomBox, this.this$0.zoomBoxColour, this.this$0.zoomBoxStroke);
this.this$0.addObject(this.this$0.zoomBoxObject);
this.this$0.repaint();
}

public void mouseReleased(MouseEvent param1MouseEvent) {
if (SwingUtilities.isRightMouseButton(param1MouseEvent) || param1MouseEvent.isShiftDown())
return;  this.this$0.removeObject(this.this$0.zoomBoxObject);

this.this$0.zoomIn();
} }

class ZoomMotionListener extends MouseMotionAdapter { ZoomMotionListener(PlotPanel this$0) {
this.this$0 = this$0;
}
private final PlotPanel this$0;
public void mouseDragged(MouseEvent param1MouseEvent) {
if (SwingUtilities.isRightMouseButton(param1MouseEvent) || param1MouseEvent.isShiftDown())
return;  VPoint vPoint = this.this$0.pixelToVirtual(param1MouseEvent.getPoint());
this.this$0.removeObject(this.this$0.zoomBoxObject);
this.this$0.zoomBox = new VRectangle(this.this$0.zoomBoxStart, vPoint);
this.this$0.zoomBoxObject = new PlotShape((Shape)this.this$0.zoomBox, this.this$0.zoomBoxColour, this.this$0.zoomBoxStroke);
this.this$0.addObject(this.this$0.zoomBoxObject);
this.this$0.repaint();
} }

static class Test
{
public static void main(String[] param1ArrayOfString) {
JFrame jFrame = new JFrame("PlotPanel Test");

PlotPanel plotPanel = new PlotPanel(new Dimension(500, 400), new VRectangle(-50.0D, -50.0D, 100.0D, 100.0D));
plotPanel.setZoomable(true);

plotPanel.setFocusable(true);

char c = 'Ϩ';
double d = -50.0D;
VPoint[] arrayOfVPoint = new VPoint[c];
for (byte b1 = 0; b1 < c; b1++) {

arrayOfVPoint[b1] = new VPoint(d, 40.0D * Math.sin(d));
d += 100.0D / c;
} 
Polyline polyline = new Polyline(arrayOfVPoint);
plotPanel.addObject(new PlotShape((Shape)polyline, Color.black, new BasicStroke(1.0F)));

plotPanel.addObject(new StandardMarker(new VPoint(0.0D, 0.0D), 4, 40, Color.blue));

byte b2 = 5;
VPoint vPoint = new VPoint(-20.0D, 0.0D);

Font font = new Font("SansSerif", 0, 12);
plotPanel.addObject(new StandardMarker(vPoint, 0, 10, Color.red, new BasicStroke(1.0F)));
plotPanel.addObject(new PlotText("North horizontal text", vPoint, 1, b2, Color.black, font));
plotPanel.addObject(new PlotText("South horizontal text", vPoint, 2, b2, Color.black, font));
plotPanel.addObject(new PlotText("East horizontal text", vPoint, 4, b2, Color.black, font));
plotPanel.addObject(new PlotText("West horizontal text", vPoint, 3, b2, Color.black, font));

vPoint = new VPoint(20.0D, 0.0D);
plotPanel.addObject(new StandardMarker(vPoint, 6, 10, Color.red, new BasicStroke(1.0F)));
plotPanel.addObject(new PlotText("North vertical text", vPoint, 1, b2, Color.black, font, 90.0D));
plotPanel.addObject(new PlotText("South vertical text", vPoint, 2, b2, Color.black, font, 90.0D));
plotPanel.addObject(new PlotText("East vertical text", vPoint, 4, b2, Color.black, font, 90.0D));
plotPanel.addObject(new PlotText("West vertical text", vPoint, 3, b2, Color.black, font, 90.0D));

plotPanel.addComponent(new JButton("Button"), new VPoint(-40.0D, -40.0D), new VDimension(30.0D, 10.0D));

vPoint = new VPoint(-25.0D, 25.0D);
plotPanel.addObject(new StandardMarker(vPoint, 4, 6, Color.red));
for (byte b3 = 0; b3 < 'Ũ'; b3 += 30) {

PlotText plotText = new PlotText("Rotated text " + b3, vPoint, 4, 7, b3);

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

