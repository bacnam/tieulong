package jsc.swt.plot2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import jsc.descriptive.CategoricalTally;
import jsc.swt.plot.AxisModel;
import jsc.swt.virtualgraphics.LineBars;
import jsc.swt.virtualgraphics.RectangularBars;
import jsc.swt.virtualgraphics.VPoint;

public class VerticalBarChart
extends LabelledXaxisPlot
{
public static final int FREQUENCY = 0;
public static final int PROPORTION = 1;
public static final int PERCENTAGE = 2;
int type = 0;

public VerticalBarChart(String paramString1, String paramString2, String[] paramArrayOfString, Color paramColor, Font paramFont, AxisModel paramAxisModel) {
super(paramString1, paramString2, paramArrayOfString, paramColor, paramFont, paramAxisModel);
setAntialiasing(false);
}

public PlotObject addBars(CategoricalTally paramCategoricalTally, float paramFloat, Color paramColor, Paint paramPaint, boolean paramBoolean) {
PlotShape plotShape;
int i = paramCategoricalTally.getNumberOfBins();
if (i != this.labelCount)
throw new IllegalArgumentException("Number of frequencies not equal to number of labels."); 
VPoint[] arrayOfVPoint = new VPoint[i];

double d = 0.0D;
for (byte b = 0; b < i; b++) {

switch (this.type) {
case 0:
d = paramCategoricalTally.getFrequency(b); break;
case 1: d = paramCategoricalTally.getProportion(b); break;
case 2: d = paramCategoricalTally.getPercentage(b); break;
} 
arrayOfVPoint[b] = new VPoint((b + 1), d);
} 
if (paramBoolean) {
plotShape = new PlotShape((Shape)new LineBars(arrayOfVPoint), paramColor, new BasicStroke(paramFloat, 0, 1));
} else {
plotShape = new PlotShape((Shape)new RectangularBars(arrayOfVPoint, paramFloat), paramPaint);
} 
addObject(plotShape);
repaint();
return plotShape;
}

public int getType() {
return this.type;
}

public void setType(int paramInt) {
if (paramInt == 0 || paramInt == 1 || paramInt == 2) {
this.type = paramInt;
} else {
throw new IllegalArgumentException("Invalid bar chart type.");
} 
}
}

