package jsc.swt.plot2d;

import jsc.numerical.Function;
import jsc.numerical.Spline;
import jsc.swt.plot.AxisModel;
import jsc.swt.plot.BeadSlider;
import jsc.swt.plot.PlotFunction;
import jsc.swt.virtualgraphics.VDimension;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VRectangle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SplinePlot2D
        extends FunctionPlot2D
        implements Function {
    int m;
    int beadSize;
    double a;
    double b;
    boolean cutOff = false;
    double minorTickGap;
    double majorTickGap;
    SplineFunc splineFunc;
    boolean changed = false;
    int n;
    Color curveColour;
    Stroke stroke;
    Paint paint;
    boolean filled;
    PlotObject po;
    private Spline spline;
    private double[] x;
    private double[] y;

    public SplinePlot2D(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, int paramInt1, double[] paramArrayOfdouble, int paramInt2, Color paramColor1, int paramInt3, Color paramColor2, Stroke paramStroke, Paint paramPaint, boolean paramBoolean) {
        super(paramAxisModel1, paramAxisModel2, paramString);
        this.m = paramInt1;
        this.beadSize = paramInt2;

        if (paramDouble2 <= paramDouble1)
            throw new IllegalArgumentException("Invalid spline interval.");
        this.a = paramDouble1;
        this.b = paramDouble2;
        double d1 = (this.b - this.a) / (paramInt1 - 1.0D);

        double d2 = paramAxisModel2.getMin();
        double d3 = paramAxisModel2.getMax();
        double d4 = paramAxisModel2.getLength();
        this.minorTickGap = d4 / 100.0D;
        this.majorTickGap = d4 / 10.0D;
        this.x = new double[paramInt1];
        this.y = new double[paramInt1];
        SliderListener sliderListener = new SliderListener(this);

        VDimension vDimension = new VDimension(d1, d4);
        for (byte b = 0; b < paramInt1; b++) {

            this.x[b] = this.a + b * d1;
            this.y[b] = paramArrayOfdouble[b];
            BeadSlider beadSlider = new BeadSlider(b, d2, d3, paramArrayOfdouble[b], this.minorTickGap, this.majorTickGap, paramColor1);
            beadSlider.addChangeListener(sliderListener);
            addComponent((Component) beadSlider, new VPoint(this.x[b] - 0.5D * d1, d2), vDimension);
        }

        this.spline = new Spline(paramInt1, this.x, paramArrayOfdouble);
        this.splineFunc = new SplineFunc(this);
        this.n = paramInt3;
        this.curveColour = paramColor2;
        this.stroke = paramStroke;
        this.paint = paramPaint;
        this.filled = paramBoolean;
        this.po = addFunction(this.splineFunc, this.a, this.b, paramInt3, paramColor2, paramStroke, paramPaint, paramBoolean);
    }

    public SplinePlot2D(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, int paramInt1, double[] paramArrayOfdouble, int paramInt2, Color paramColor1, int paramInt3, Color paramColor2, Stroke paramStroke) {
        this(paramString, paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, paramInt1, paramArrayOfdouble, paramInt2, paramColor1, paramInt3, paramColor2, paramStroke, PlotShape.defaultPaint, false);
    }

    public SplinePlot2D(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, int paramInt1, double[] paramArrayOfdouble, int paramInt2, Color paramColor1, int paramInt3, Color paramColor2) {
        this(paramString, paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, paramInt1, paramArrayOfdouble, paramInt2, paramColor1, paramInt3, paramColor2, PlotShape.defaultStroke, PlotShape.defaultPaint, false);
    }

    public void addChangeListener(ChangeListener paramChangeListener) {
        for (byte b = 0; b < this.m; b++) {

            BeadSlider beadSlider = (BeadSlider) getComponent(b);
            beadSlider.addChangeListener(paramChangeListener);
        }
    }

    public double function(double paramDouble) {
        updateSpline();
        return this.spline.splint(paramDouble);
    }

    public BeadSlider getSlider(int paramInt) {
        return (BeadSlider) getComponent(paramInt);
    }

    public Spline getSpline() {
        updateSpline();
        return this.spline;
    }

    public double getX(int paramInt) {
        return this.spline.getX(paramInt);
    }

    public double getY(int paramInt) {
        return this.spline.getY(paramInt);
    }

    public double getY(double paramDouble) {
        return this.spline.splint(paramDouble);
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean paramBoolean) {
        this.changed = paramBoolean;
    }

    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
        VDimension vDimension = pixelToVirtual(new Dimension(this.beadSize, this.beadSize));

        double d1 = vDimension.width;
        double d2 = 0.5D * d1;

        for (byte b = 0; b < this.m; b++) {

            BeadSlider beadSlider = (BeadSlider) getComponent(b);
            setComponentBounds(b, new VRectangle(this.x[b] - d2, beadSlider.getMinimumValue(), d1, beadSlider.getMaximumValue() - beadSlider.getMinimumValue()));
        }
    }

    public void setBeads(AxisModel paramAxisModel, double paramDouble1, double paramDouble2, double[] paramArrayOfdouble) {
        if (paramArrayOfdouble.length != this.m) {
            throw new IllegalArgumentException("Array has incorrect length.");
        }
        if (paramDouble2 <= paramDouble1)
            throw new IllegalArgumentException("Invalid spline interval.");
        this.a = paramDouble1;
        this.b = paramDouble2;
        double d = (this.b - this.a) / (this.m - 1.0D);

        for (byte b = 0; b < this.m; b++) {

            this.x[b] = this.a + b * d;
            this.y[b] = paramArrayOfdouble[b];
            BeadSlider beadSlider = (BeadSlider) getComponent(b);
            beadSlider.setRealValue(paramArrayOfdouble[b]);
        }

        rescaleHorizontal(paramAxisModel);
        this.spline = new Spline(this.m, this.x, paramArrayOfdouble);

        removeObject(this.po);
        this.po = addFunction(this.splineFunc, this.a, this.b, this.n, this.curveColour, this.stroke, this.paint, this.filled);
        this.changed = true;
    }

    public void setCutOff(boolean paramBoolean) {
        this.cutOff = paramBoolean;
    }

    public void setFocusColour(Color paramColor) {
        for (byte b = 0; b < this.m; b++) {

            BeadSlider beadSlider = (BeadSlider) getComponent(b);
            beadSlider.setFocusColour(paramColor);
        }
    }

    public void setSliderY(double paramDouble1, double paramDouble2, double[] paramArrayOfdouble) {
        double[] arrayOfDouble1 = new double[this.m];
        double[] arrayOfDouble2 = new double[this.m];
        for (byte b = 0; b < this.m; ) {
            arrayOfDouble1[b] = paramDouble1;
            arrayOfDouble2[b] = paramDouble2;
            b++;
        }
        setSliderY(arrayOfDouble1, arrayOfDouble2, paramArrayOfdouble);
    }

    public void setSliderY(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double[] paramArrayOfdouble3) {
        if (paramArrayOfdouble1.length != this.m || paramArrayOfdouble2.length != this.m || paramArrayOfdouble3.length != this.m) {
            throw new IllegalArgumentException("Array has incorrect length.");
        }
        for (byte b = 0; b < this.m; b++) {

            this.y[b] = paramArrayOfdouble3[b];
            BeadSlider beadSlider = (BeadSlider) getComponent(b);

            if (paramArrayOfdouble1[b] >= paramArrayOfdouble2[b])
                throw new IllegalArgumentException("Invalid slider ordinates.");
            beadSlider.rescale(paramArrayOfdouble1[b], paramArrayOfdouble2[b], paramArrayOfdouble3[b], this.minorTickGap, this.majorTickGap);
        }

        this.spline = new Spline(this.m, this.x, paramArrayOfdouble3);

        removeObject(this.po);
        this.po = addFunction(this.splineFunc, this.a, this.b, this.n, this.curveColour, this.stroke, this.paint, this.filled);
        this.changed = true;
    }

    private void updateSpline() {
        for (byte b = 0; b < this.m; b++) {

            BeadSlider beadSlider = (BeadSlider) getComponent(b);
            this.y[b] = beadSlider.getRealValue();
        }

        this.spline = new Spline(this.m, this.x, this.y);
    }

    class SliderListener implements ChangeListener {
        private final SplinePlot2D this$0;

        SliderListener(SplinePlot2D this$0) {
            this.this$0 = this$0;
        }

        public void stateChanged(ChangeEvent param1ChangeEvent) {
            this.this$0.updateSpline();
            this.this$0.removeObject(this.this$0.po);
            this.this$0.po = this.this$0.addFunction(this.this$0.splineFunc, this.this$0.a, this.this$0.b, this.this$0.n, this.this$0.curveColour, this.this$0.stroke, this.this$0.paint, this.this$0.filled);
            this.this$0.changed = true;
        }
    }

    class SplineFunc implements PlotFunction {
        private final SplinePlot2D this$0;

        SplineFunc(SplinePlot2D this$0) {
            this.this$0 = this$0;
        }

        public double getOrdinate(double param1Double) {
            if (this.this$0.cutOff) {
                return Math.max(this.this$0.verticalAxis.getMin(), this.this$0.spline.splint(param1Double));
            }
            return this.this$0.spline.splint(param1Double);
        }
    }

}

