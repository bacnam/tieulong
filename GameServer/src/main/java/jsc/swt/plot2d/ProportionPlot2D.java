package jsc.swt.plot2d;

import jsc.swt.plot.AxisModel;
import jsc.swt.plot.LinearAxisModel;
import jsc.swt.virtualgraphics.Polyline;
import jsc.swt.virtualgraphics.VLine;
import jsc.swt.virtualgraphics.VPoint;
import jsc.util.Scale;

import java.awt.*;

public class ProportionPlot2D
        extends AxesPlot {
    int numberOfTrials;
    double targetProportion;
    int trialCount;
    Polyline polyline;
    PlotObject curve;
    PlotObject line = null;

    Color curveColour;

    Stroke lineStroke;

    Color lineColour;

    Stroke curveStroke;

    String xTitle;

    int xTicks;

    public ProportionPlot2D(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2, int paramInt3, Color paramColor1, Stroke paramStroke1, double paramDouble, Color paramColor2, Stroke paramStroke2) {
        super((AxisModel) new LinearAxisModel(paramString2, new Scale(0.0D, paramInt3, paramInt1, false), "##"), (AxisModel) new LinearAxisModel(paramString3, new Scale(0.0D, 1.0D, paramInt2, false), "#.##"), paramString1);

        this.curveColour = paramColor1;
        this.curveStroke = paramStroke1;
        this.lineColour = paramColor2;
        this.lineStroke = paramStroke2;

        this.numberOfTrials = paramInt3;
        this.xTitle = paramString2;
        this.xTicks = paramInt1;
        this.targetProportion = paramDouble;

        setAntialiasing(false);
        setPreferredSize(new Dimension(400, 300));
        setTargetProportion(paramDouble);
    }

    public double addProportion(int paramInt) {
        if (this.trialCount >= this.numberOfTrials) return -1.0D;

        this.trialCount++;
        double d = paramInt / this.trialCount;
        if (d < 0.0D || d > 1.0D) {

            System.out.println("numberOfSuccesses = " + paramInt + " trialCount = " + this.trialCount + " proportion = " + d);
            throw new IllegalArgumentException("Invalid number of successes.");
        }
        if (this.trialCount == 1) {

            VPoint vPoint = new VPoint(1.0D, d);
            this.polyline = new Polyline(vPoint, this.numberOfTrials + 1);

            addObject(new StandardMarker(vPoint, 4, 5, this.curveColour));
        } else {

            this.polyline.lineTo(new VPoint(this.trialCount, d));

            removeObject(this.curve);
            this.curve = new PlotShape((Shape) this.polyline, this.curveColour, this.curveStroke);
            addObject(this.curve);
        }
        repaint();
        return d;
    }

    public int getTrialCount() {
        return this.trialCount;
    }

    public void clear() {
        this.trialCount = 0;

        removeAllObjects();
    }

    public void rescale(int paramInt1, int paramInt2) {
        this.numberOfTrials = paramInt1;
        this.xTicks = paramInt2;
        rescaleHorizontal((AxisModel) new LinearAxisModel(this.xTitle, new Scale(0.0D, paramInt1, paramInt2, false), "##"));

        removeAllObjects();
        setTargetProportion(this.targetProportion);
    }

    public void setTargetProportion(double paramDouble) {
        if (paramDouble > 0.0D && paramDouble < 1.0D) {

            this.line = new PlotShape((Shape) new VLine(new VPoint(0.0D, paramDouble), new VPoint(this.numberOfTrials, paramDouble)), this.lineColour, this.lineStroke);

            addObject(this.line);
        } else {

            if (this.line != null) removeObject(this.line);
            this.line = null;
        }
        repaint();
    }
}

