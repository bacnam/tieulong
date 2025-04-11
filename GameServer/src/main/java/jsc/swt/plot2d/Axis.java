package jsc.swt.plot2d;

import jsc.swt.plot.AxisModel;
import jsc.swt.plot.LinearAxisModel;
import jsc.swt.virtualgraphics.VirtualTransform;

import java.awt.*;
import java.awt.geom.Point2D;

public class Axis
        implements PlotObject {
    static final Font DEFAULT_AXIS_LABEL_FONT = new Font("SansSerif", 0, 14);
    static final Color DEFAULT_COLOUR = Color.black;
    static final Stroke DEFAULT_STROKE = new BasicStroke();
    static final Font DEFAULT_TICK_LABEL_FONT = new Font("Monospaced", 0, 12);

    protected AxisModel axisModel;

    protected int tickCount;

    protected boolean paintLabels = true;

    protected PlotText axisLabel;

    protected PlotShape axisLine;

    protected PlotText[] tickLabels;

    protected TickMark[] tickMarks;

    public Axis() {
        this((AxisModel) new LinearAxisModel());
    }

    public Axis(AxisModel paramAxisModel) {
        this.tickCount = paramAxisModel.getTickCount();

        this.axisModel = (AxisModel) paramAxisModel.clone();

        this.tickLabels = new PlotText[this.tickCount];
        this.tickMarks = new TickMark[this.tickCount];
    }

    public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
        return false;
    }

    public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
        this.axisLine.draw(paramGraphics2D, paramVirtualTransform);

        for (byte b = 0; b < this.tickCount; b++) {

            this.tickMarks[b].draw(paramGraphics2D, paramVirtualTransform);

            if (this.paintLabels) this.tickLabels[b].draw(paramGraphics2D, paramVirtualTransform);

        }

        this.axisLabel.draw(paramGraphics2D, paramVirtualTransform);
    }

    public Color getAxisColour() {
        return this.axisLine.colour;
    }

    public PlotText getAxisLabel() {
        return this.axisLabel;
    }

    public Color getAxisLabelColour() {
        return this.axisLabel.colour;
    }

    public Font getAxisLabelFont() {
        return this.axisLabel.font;
    }

    public PlotShape getAxisLine() {
        return this.axisLine;
    }

    public Stroke getAxisStroke() {
        return this.axisLine.stroke;
    }

    public double getMin() {
        return this.axisModel.getMin();
    }

    public double getMax() {
        return this.axisModel.getMax();
    }

    public AxisModel getModel() {
        return this.axisModel;
    }

    public int getTickCount() {
        return this.tickCount;
    }

    public PlotText getTickLabel(int paramInt) {
        return this.tickLabels[paramInt];
    }

    public Color getTickLabelColour() {
        return (this.tickLabels[0]).colour;
    }

    public Font getTickLabelFont() {
        return (this.tickLabels[0]).font;
    }

    public TickMark getTickMark(int paramInt) {
        return this.tickMarks[paramInt];
    }

    public int getTickSize() {
        return (this.tickMarks[0]).size;
    }

    public Stroke getTickStroke() {
        return (this.tickMarks[0]).stroke;
    }

    public void setPaintLabels(boolean paramBoolean) {
        this.paintLabels = paramBoolean;
    }
}

