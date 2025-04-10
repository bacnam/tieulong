package jsc.swt.plot2d;

import java.awt.Color;
import java.awt.Font;
import jsc.swt.plot.AxisModel;
import jsc.swt.plot.LinearAxisModel;
import jsc.swt.virtualgraphics.VPoint;
import jsc.util.Scale;

public class LabelledXaxisPlot
extends AxesPlot
{
int labelCount;

public LabelledXaxisPlot(String paramString1, String paramString2, String[] paramArrayOfString, Color paramColor, Font paramFont, AxisModel paramAxisModel) {
super((AxisModel)new LinearAxisModel(), paramAxisModel, paramString1);

setPaintXaxis(false);

this.labelCount = paramArrayOfString.length;

double d = paramAxisModel.getTickValue(0);
for (byte b = 0; b < this.labelCount; b++) {

PlotText plotText = new PlotText(paramArrayOfString[b], new VPoint((b + 1), d), 2, 3, paramColor, paramFont);
addObject(plotText);
} 

rescaleHorizontal((AxisModel)new LinearAxisModel(paramString2, new Scale(0.0D, (this.labelCount + 1), this.labelCount + 2, false), ""));
}

public int getLabelCount() {
return this.labelCount;
}
}

