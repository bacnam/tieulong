package jsc.swt.plot2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.Stroke;
import jsc.swt.plot.AxisModel;
import jsc.swt.plot.LinearAxisModel;
import jsc.swt.virtualgraphics.VLine;
import jsc.swt.virtualgraphics.VPoint;

public class VerticalAxis
extends Axis
{
double abscissa;

public VerticalAxis() {
this((AxisModel)new LinearAxisModel(), 0.0D);
}

public VerticalAxis(AxisModel paramAxisModel, double paramDouble) {
this(paramAxisModel, paramDouble, Axis.DEFAULT_COLOUR, Axis.DEFAULT_STROKE, 5, Axis.DEFAULT_STROKE, Axis.DEFAULT_COLOUR, Axis.DEFAULT_AXIS_LABEL_FONT, Axis.DEFAULT_COLOUR, Axis.DEFAULT_TICK_LABEL_FONT);
}

public VerticalAxis(AxisModel paramAxisModel, double paramDouble, Color paramColor, Stroke paramStroke, int paramInt, Font paramFont1, Font paramFont2) {
this(paramAxisModel, paramDouble, paramColor, paramStroke, paramInt, paramStroke, paramColor, paramFont1, paramColor, paramFont2);
}

public VerticalAxis(AxisModel paramAxisModel, double paramDouble, Color paramColor1, Stroke paramStroke1, int paramInt, Stroke paramStroke2, Color paramColor2, Font paramFont1, Color paramColor3, Font paramFont2) {
super(paramAxisModel);

int i = 5 + paramInt + paramFont2.getSize();

this.abscissa = paramDouble;

this.axisLine = new PlotShape((Shape)new VLine(paramDouble, (float)paramAxisModel.getTickValue(0), paramDouble, (float)paramAxisModel.getTickValue(this.tickCount - 1)), paramColor1, paramStroke1);

for (byte b = 0; b < this.tickCount; b++) {

VPoint vPoint = new VPoint(paramDouble, paramAxisModel.getTickValue(b));
this.tickMarks[b] = new HorizontalTickMark(vPoint, paramInt, paramColor1, paramStroke2);

this.tickLabels[b] = new PlotText(paramAxisModel.getTickLabel(b), vPoint, 1, 3 + paramInt, paramColor3, paramFont2, 90.0D);
} 

this.axisLabel = new PlotText(paramAxisModel.getLabel(), new VPoint(paramDouble, 0.5D * (paramAxisModel.getTickValue(0) + paramAxisModel.getTickValue(this.tickCount - 1))), 1, i, paramColor2, paramFont1, 90.0D);
}

public VerticalAxis setModel(AxisModel paramAxisModel) {
return new VerticalAxis(paramAxisModel, this.abscissa, getAxisColour(), getAxisStroke(), getTickSize(), getTickStroke(), getAxisLabelColour(), getAxisLabelFont(), getTickLabelColour(), getTickLabelFont());
}
}

