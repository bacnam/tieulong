package jsc.swt.plot2d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.JFrame;
import jsc.swt.plot.AxisModel;
import jsc.swt.plot.LinearAxisModel;
import jsc.swt.plot.PiAxisModel;
import jsc.swt.plot.PlotFunction;
import jsc.swt.virtualgraphics.Path;
import jsc.util.Scale;

public class FunctionPlot2D
extends AxesPlot
{
public FunctionPlot2D(AxisModel paramAxisModel1, AxisModel paramAxisModel2, String paramString) {
super(paramAxisModel1, paramAxisModel2, paramString);
}

public FunctionPlot2D(AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, String paramString) {
super(paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, paramString);
}

public PlotShape addFunction(PlotFunction paramPlotFunction, int paramInt, Color paramColor) {
return addFunction(paramPlotFunction, this.horizontalAxis.getMin(), this.horizontalAxis.getMax(), paramInt, paramColor, PlotShape.defaultStroke, PlotShape.defaultPaint, false);
}

public PlotShape addFunction(PlotFunction paramPlotFunction, int paramInt, Color paramColor, Stroke paramStroke) {
return addFunction(paramPlotFunction, this.horizontalAxis.getMin(), this.horizontalAxis.getMax(), paramInt, paramColor, paramStroke, PlotShape.defaultPaint, false);
}

public PlotShape addFunction(PlotFunction paramPlotFunction, double paramDouble1, double paramDouble2, int paramInt, Color paramColor) {
return addFunction(paramPlotFunction, paramDouble1, paramDouble2, paramInt, paramColor, PlotShape.defaultStroke, paramColor, false);
}

public PlotShape addFunction(PlotFunction paramPlotFunction, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke) {
return addFunction(paramPlotFunction, paramDouble1, paramDouble2, paramInt, paramColor, paramStroke, PlotShape.defaultPaint, false);
}

public PlotShape addFunction(PlotFunction paramPlotFunction, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, boolean paramBoolean) {
return addFunction(paramPlotFunction, paramDouble1, paramDouble2, paramInt, paramColor, PlotShape.defaultStroke, paramColor, paramBoolean);
}

public PlotShape addFunction(PlotFunction paramPlotFunction, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke, Paint paramPaint, boolean paramBoolean) {
PlotShape plotShape;
double d = (paramDouble2 - paramDouble1) / (paramInt - 1.0D);
Path path = new Path(paramInt);
boolean bool = true;
for (byte b = 0; b < paramInt; b++) {

double d1 = paramDouble1 + b * d;
double d2 = paramPlotFunction.getOrdinate(d1);
if (Double.isNaN(d2)) {
bool = true;

}
else if (bool) {

path.moveTo(d1, d2);
bool = false;
} else {

path.lineTo(d1, d2);
} 
} 

if (paramBoolean) {

path.lineTo(paramDouble2, 0.0D);
path.lineTo(paramDouble1, 0.0D);
path.closePath();
plotShape = new PlotShape((Shape)path, paramPaint);
} else {

plotShape = new PlotShape((Shape)path, paramColor, paramStroke);
} 
addObject(plotShape);
repaint();
return plotShape;
}

static class Test
{
static class Cos
implements PlotFunction
{
public double getOrdinate(double param2Double) {
return Math.cos(param2Double);
} }
static class Sin implements PlotFunction { public double getOrdinate(double param2Double) {
return Math.sin(param2Double);
} }

public static void main(String[] param1ArrayOfString) {
byte b = 100;

JFrame jFrame = new JFrame("FunctionPlot2D Test");

PiAxisModel piAxisModel = new PiAxisModel("Angle", -5, 5, 2);
LinearAxisModel linearAxisModel = new LinearAxisModel("", new Scale(-1.0D, 1.0D, 3, false), "##.#");
FunctionPlot2D functionPlot2D = new FunctionPlot2D((AxisModel)piAxisModel, (AxisModel)linearAxisModel, 0.0D, 0.0D, "Plot of sine and cosine functions");
functionPlot2D.setZoomable(true);

functionPlot2D.setFocusable(true);

functionPlot2D.addFunction(new Sin(), b, Color.blue);
functionPlot2D.addFunction(new Cos(), b, Color.red);

Container container = jFrame.getContentPane();
container.setLayout(new BorderLayout());
container.add(functionPlot2D, "Center");

jFrame.setVisible(true);
jFrame.setDefaultCloseOperation(3);
jFrame.pack();
}
}
}

