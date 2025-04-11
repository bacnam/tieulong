package jsc.swt.plot2d;

import jsc.swt.plot.AxisModel;
import jsc.swt.plot.LinearAxisModel;
import jsc.swt.virtualgraphics.VLine;
import jsc.swt.virtualgraphics.VPoint;

import java.awt.*;

public class HorizontalAxis
        extends Axis {
    double ordinate;

    public HorizontalAxis() {
        this((AxisModel) new LinearAxisModel(), 0.0D);
    }

    public HorizontalAxis(AxisModel paramAxisModel, double paramDouble) {
        this(paramAxisModel, paramDouble, Axis.DEFAULT_COLOUR, Axis.DEFAULT_STROKE, 5, Axis.DEFAULT_STROKE, Axis.DEFAULT_COLOUR, Axis.DEFAULT_AXIS_LABEL_FONT, Axis.DEFAULT_COLOUR, Axis.DEFAULT_TICK_LABEL_FONT);
    }

    public HorizontalAxis(AxisModel paramAxisModel, double paramDouble, Color paramColor, Stroke paramStroke, int paramInt, Font paramFont1, Font paramFont2) {
        this(paramAxisModel, paramDouble, paramColor, paramStroke, paramInt, paramStroke, paramColor, paramFont1, paramColor, paramFont2);
    }

    public HorizontalAxis(AxisModel paramAxisModel, double paramDouble, Color paramColor1, Stroke paramStroke1, int paramInt, Stroke paramStroke2, Color paramColor2, Font paramFont1, Color paramColor3, Font paramFont2) {
        super(paramAxisModel);
        int i = 2 + paramInt + paramFont2.getSize();

        this.ordinate = paramDouble;

        this.axisLine = new PlotShape((Shape) new VLine((float) paramAxisModel.getTickValue(0), paramDouble, (float) paramAxisModel.getTickValue(this.tickCount - 1), paramDouble), paramColor1, paramStroke1);

        for (byte b = 0; b < this.tickCount; b++) {

            VPoint vPoint = new VPoint(paramAxisModel.getTickValue(b), paramDouble);
            this.tickMarks[b] = new VerticalTickMark(vPoint, paramInt, paramColor1, paramStroke2);
            this.tickLabels[b] = new PlotText(paramAxisModel.getTickLabel(b), vPoint, 2, paramInt, paramColor3, paramFont2);
        }

        this.axisLabel = new PlotText(paramAxisModel.getLabel(), new VPoint(0.5D * (paramAxisModel.getTickValue(0) + paramAxisModel.getTickValue(this.tickCount - 1)), paramDouble), 2, i, paramColor2, paramFont1);
    }

    public HorizontalAxis setModel(AxisModel paramAxisModel) {
        return new HorizontalAxis(paramAxisModel, this.ordinate, getAxisColour(), getAxisStroke(), getTickSize(), getTickStroke(), getAxisLabelColour(), getAxisLabelFont(), getTickLabelColour(), getTickLabelFont());
    }
}

