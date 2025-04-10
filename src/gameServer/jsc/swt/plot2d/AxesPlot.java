package jsc.swt.plot2d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.JFrame;
import jsc.swt.plot.AxisModel;
import jsc.swt.plot.LinearAxisModel;
import jsc.swt.virtualgraphics.VLine;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VRectangle;
import jsc.swt.virtualgraphics.VirtualTransform;
import jsc.util.Scale;

public class AxesPlot
extends PlotPanel
implements Cloneable
{
protected PlotText title;
protected HorizontalAxis horizontalAxis;
protected VerticalAxis verticalAxis;
static final Font DEFAULT_TITLE_FONT = new Font("SansSerif", 0, 14);
static final Color DEFAULT_TITLE_COLOUR = Color.black;

private VRectangle plottingArea;

private boolean paintXaxis = true;

private boolean paintYaxis = true;

private boolean clipping = false;

public AxesPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, String paramString) {
this(paramAxisModel1, paramAxisModel2, Axis.DEFAULT_COLOUR, Axis.DEFAULT_STROKE, 5, Axis.DEFAULT_STROKE, Axis.DEFAULT_COLOUR, Axis.DEFAULT_AXIS_LABEL_FONT, Axis.DEFAULT_COLOUR, Axis.DEFAULT_TICK_LABEL_FONT, paramString, DEFAULT_TITLE_COLOUR, DEFAULT_TITLE_FONT);
}

public AxesPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, String paramString) {
this(paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, Axis.DEFAULT_COLOUR, Axis.DEFAULT_STROKE, 5, Axis.DEFAULT_STROKE, Axis.DEFAULT_COLOUR, Axis.DEFAULT_AXIS_LABEL_FONT, Axis.DEFAULT_COLOUR, Axis.DEFAULT_TICK_LABEL_FONT, paramString, DEFAULT_TITLE_COLOUR, DEFAULT_TITLE_FONT);
}

public AxesPlot(HorizontalAxis paramHorizontalAxis, VerticalAxis paramVerticalAxis, String paramString) {
this(paramHorizontalAxis, paramVerticalAxis, paramString, DEFAULT_TITLE_COLOUR, DEFAULT_TITLE_FONT);
}

public AxesPlot(HorizontalAxis paramHorizontalAxis, VerticalAxis paramVerticalAxis, String paramString, Color paramColor, Font paramFont) {
super(new Dimension(300, 300));
this.horizontalAxis = paramHorizontalAxis;
this.verticalAxis = paramVerticalAxis;
setMargins();
setTitle(paramString, paramColor, paramFont);
}

public AxesPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, Color paramColor1, Stroke paramStroke1, int paramInt, Stroke paramStroke2, Color paramColor2, Font paramFont1, Color paramColor3, Font paramFont2, String paramString, Color paramColor4, Font paramFont3) {
this(paramAxisModel1, paramAxisModel2, paramAxisModel1.getMin(), paramAxisModel2.getMin(), paramColor1, paramStroke1, paramInt, paramStroke2, paramColor2, paramFont1, paramColor3, paramFont2, paramString, paramColor4, paramFont3);
}

public AxesPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, Color paramColor1, Stroke paramStroke1, int paramInt, Stroke paramStroke2, Color paramColor2, Font paramFont1, Color paramColor3, Font paramFont2, String paramString, Color paramColor4, Font paramFont3) {
this(new HorizontalAxis(paramAxisModel1, paramDouble2, paramColor1, paramStroke1, paramInt, paramStroke2, paramColor2, paramFont1, paramColor3, paramFont2), new VerticalAxis(paramAxisModel2, paramDouble1, paramColor1, paramStroke1, paramInt, paramStroke2, paramColor2, paramFont1, paramColor3, paramFont2), paramString, paramColor4, paramFont3);
}

public AxesPlot(String paramString) {
this(new HorizontalAxis(), new VerticalAxis(), paramString);
}

public PlotObject addVerticalLine(double paramDouble1, double paramDouble2, double paramDouble3, Color paramColor, Stroke paramStroke) {
PlotShape plotShape = new PlotShape((Shape)new VLine(paramDouble1, paramDouble2, paramDouble1, paramDouble3), paramColor, paramStroke);
addObject(plotShape);

return plotShape;
}

public PlotObject addVerticalLine(double paramDouble, Color paramColor, Stroke paramStroke) {
return addVerticalLine(paramDouble, this.verticalAxis.getMin(), this.verticalAxis.getMax(), paramColor, paramStroke);
}

public Object clone() {
return copy();
}

public AxesPlot copy() {
AxesPlot axesPlot = new AxesPlot(this.horizontalAxis, this.verticalAxis, this.title.getString(), this.title.getColour(), this.title.getFont());

copyContents(axesPlot);
axesPlot.paintXaxis = this.paintXaxis;
axesPlot.paintYaxis = this.paintYaxis;
axesPlot.clipping = this.clipping;
axesPlot.hints = (RenderingHints)this.hints.clone();
return axesPlot;
}

public PlotText getTitle() {
return this.title;
}

public void setTitle(String paramString, Color paramColor, Font paramFont) {
this.title = new PlotText(paramString, new VPoint(this.virtualSpace.x + 0.5D * this.virtualSpace.width, this.virtualSpace.y + this.virtualSpace.height), 2, 3, paramColor, paramFont);
}

public HorizontalAxis getHorizontalAxis() {
return this.horizontalAxis;
}

public VerticalAxis getVerticalAxis() {
return this.verticalAxis;
}

public boolean outsideAxes(VPoint paramVPoint) {
if (paramVPoint.x < this.horizontalAxis.getMin() || paramVPoint.x > this.horizontalAxis.getMax() || paramVPoint.y < this.verticalAxis.getMin() || paramVPoint.y > this.verticalAxis.getMax())
{
return true;
}
return false;
}

public void paintComponent(Graphics paramGraphics) {
Graphics2D graphics2D = (Graphics2D)paramGraphics;

if (this.clipping) {

paintBackground(paramGraphics);
this.vt = new VirtualTransform(this.virtualSpace, getSize());

graphics2D.setClip(this.vt.createTransformedShape((Shape)this.plottingArea));
} 

super.paintComponent(paramGraphics);
graphics2D.setClip(null);

this.horizontalAxis.draw(graphics2D, this.vt);
this.verticalAxis.draw(graphics2D, this.vt);
this.title.draw(graphics2D, this.vt);
}

public void rescaleHorizontal(AxisModel paramAxisModel) {
this.horizontalAxis = this.horizontalAxis.setModel(paramAxisModel);
setMargins();
repaint();
}

public void rescaleVertical(AxisModel paramAxisModel) {
this.verticalAxis = this.verticalAxis.setModel(paramAxisModel);
setMargins();
repaint();
}

public void setClipping(boolean paramBoolean) {
this.clipping = paramBoolean;
}

private void setMargins() {
AxisModel axisModel1 = this.horizontalAxis.getModel();
AxisModel axisModel2 = this.verticalAxis.getModel();

this.plottingArea = new VRectangle(axisModel1.getMin(), axisModel2.getMin(), axisModel1.getLength(), axisModel2.getLength());

this.virtualSpace = new VRectangle(axisModel1.getMin() - 0.15D * axisModel1.getLength(), axisModel2.getMin() - 0.15D * axisModel2.getLength(), axisModel1.getLength() + 0.2D * axisModel1.getLength(), axisModel2.getLength() + 0.25D * axisModel2.getLength());

this.virtualSpace = new VRectangle(axisModel1.getMin() - 0.15D * axisModel1.getLength(), axisModel2.getMin() - 0.15D * axisModel2.getLength(), axisModel1.getLength() + 0.2D * axisModel1.getLength(), axisModel2.getLength() + 0.25D * axisModel2.getLength());
}

public void setPaintXaxis(boolean paramBoolean) {
this.paintXaxis = paramBoolean;
}

public void setPaintYaxis(boolean paramBoolean) {
this.paintYaxis = paramBoolean;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
JFrame jFrame = new JFrame("AxesPlot Test");

LinearAxisModel linearAxisModel1 = new LinearAxisModel("X axis label", new Scale(-10.0D, 10.0D, 11, true), "##.#");

LinearAxisModel linearAxisModel2 = new LinearAxisModel("Y axis label", new Scale(-30.0D, 30.0D, 11, true), "##.#");

AxesPlot axesPlot = new AxesPlot((AxisModel)linearAxisModel1, (AxisModel)linearAxisModel2, "Title of plot");
axesPlot.setZoomable(true);

axesPlot.setFocusable(true);

Container container = jFrame.getContentPane();
container.setLayout(new BorderLayout());
container.add(axesPlot, "Center");

jFrame.setVisible(true);
jFrame.setDefaultCloseOperation(3);
jFrame.pack();
}
}
}

