package jsc.swt.plot2d;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import jsc.descriptive.FrequencyTable;
import jsc.swt.plot.AxisModel;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VPolygon;
import jsc.util.Maths;

public class Histogram2D
extends AxesPlot
{
public static final int FREQUENCY = 0;
public static final int RELATIVE = 1;
public static final int NORMALIZED = 2;
private static final double ONE_THIRD = 0.3333333333333333D;
int type = 0;

public Histogram2D(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2) {
super(paramAxisModel1, paramAxisModel2, paramString); setAntialiasing(false);
}

public PlotObject addBars(FrequencyTable paramFrequencyTable, Paint paramPaint) {
return addBars(paramFrequencyTable, PlotShape.defaultColour, paramPaint, PlotShape.defaultStroke, true);
}

public PlotObject addBars(FrequencyTable paramFrequencyTable, Color paramColor, Stroke paramStroke) {
return addBars(paramFrequencyTable, paramColor, paramColor, paramStroke, false);
}

public PlotObject addBars(FrequencyTable paramFrequencyTable, Color paramColor, Paint paramPaint, Stroke paramStroke, boolean paramBoolean) {
int i = paramFrequencyTable.getNumberOfBins();
int j = 2 + i + i;
VPoint[] arrayOfVPoint = new VPoint[j];
byte b1 = 0;
double d = 0.0D;
arrayOfVPoint[0] = new VPoint(paramFrequencyTable.getBoundary(0), 0.0D);
byte b2;
for (b2 = 0; b2 < i; b2++) {

switch (this.type) {
case 0:
d = paramFrequencyTable.getFrequency(b2); break;
case 1: d = paramFrequencyTable.getProportion(b2); break;
case 2: d = paramFrequencyTable.getNormalizedFrequency(b2); break;
} 
arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2), d);
arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2 + 1), d);
} 
arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2), 0.0D);
PlotShape plotShape = new PlotShape((Shape)new VPolygon(arrayOfVPoint), paramColor, paramStroke, paramPaint, paramBoolean);

addObject(plotShape);
repaint();
return plotShape;
}

public static int doaneBins(int paramInt) {
if (paramInt < 1)
throw new IllegalArgumentException("Too few observations."); 
return (int)Math.round(3.0D * Maths.log10(paramInt) * Maths.log2(paramInt));
}

public int getType() {
return this.type;
}

public static int scottBins(int paramInt) {
if (paramInt < 1)
throw new IllegalArgumentException("Too few observations."); 
return (int)Math.round(Math.pow(paramInt, 0.3333333333333333D) / 0.6D);
}

public static double scottBinWidth(int paramInt, double paramDouble) {
if (paramInt < 1 || paramDouble <= 0.0D)
throw new IllegalArgumentException("Cannot calculate class interval."); 
return 3.49D * paramDouble * Math.pow(paramInt, -0.3333333333333333D);
}

public static double scottBinWidth(int paramInt, double paramDouble1, double paramDouble2) {
if (paramInt < 1 || paramDouble1 >= paramDouble2)
throw new IllegalArgumentException("Cannot calculate class interval."); 
return 0.6D * (paramDouble2 - paramDouble1) * Math.pow(paramInt, -0.3333333333333333D);
}

public void setType(int paramInt) {
if (paramInt == 0 || paramInt == 1 || paramInt == 2) {
this.type = paramInt;
} else {
throw new IllegalArgumentException("Invalid histogram type.");
} 
}
}

