package jsc.swt.plot2d;

import jsc.descriptive.FrequencyTable;
import jsc.descriptive.Tally;
import jsc.distributions.Distribution;
import jsc.swt.plot.AxisModel;
import jsc.swt.plot.PlotFunction;
import jsc.swt.virtualgraphics.LineBars;
import jsc.swt.virtualgraphics.RectangularBars;
import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VPolygon;

import java.awt.*;

public class PdfPlot
        extends FunctionPlot2D
        implements PlotFunction, Cloneable {
    Distribution D;

    public PdfPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, String paramString) {
        super(paramAxisModel1, paramAxisModel2, paramString);
    }

    public PlotShape addBars(FrequencyTable paramFrequencyTable, Paint paramPaint) {
        return addBars(paramFrequencyTable, PlotShape.defaultColour, paramPaint, PlotShape.defaultStroke, true);
    }

    public PlotShape addBars(FrequencyTable paramFrequencyTable, Color paramColor, Stroke paramStroke) {
        return addBars(paramFrequencyTable, paramColor, paramColor, paramStroke, false);
    }

    public PlotShape addBars(FrequencyTable paramFrequencyTable, Color paramColor, Paint paramPaint, Stroke paramStroke, boolean paramBoolean) {
        int i = paramFrequencyTable.getNumberOfBins();
        int j = 2 + i + i;
        VPoint[] arrayOfVPoint = new VPoint[j];
        byte b1 = 0;
        double d = 0.0D;
        arrayOfVPoint[0] = new VPoint(paramFrequencyTable.getBoundary(0), 0.0D);
        byte b2;
        for (b2 = 0; b2 < i; b2++) {

            d = paramFrequencyTable.getNormalizedFrequency(b2);
            arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2), d);
            arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2 + 1), d);
        }
        arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2), 0.0D);
        PlotShape plotShape = new PlotShape((Shape) new VPolygon(arrayOfVPoint), paramColor, paramStroke, paramPaint, paramBoolean);

        addObject(plotShape);
        repaint();
        return plotShape;
    }

    public PlotShape addBars(Tally paramTally, double paramDouble1, Color paramColor, Paint paramPaint, boolean paramBoolean, double paramDouble2) {
        PlotShape plotShape;
        int i = paramTally.getNumberOfBins();
        VPoint[] arrayOfVPoint = new VPoint[i];

        for (byte b = 0; b < i; b++) {
            arrayOfVPoint[b] = new VPoint(paramTally.getBinValue(b) + paramDouble2, paramTally.getProportion(b));
        }
        if (paramBoolean) {
            plotShape = new PlotShape((Shape) new LineBars(arrayOfVPoint), paramColor, new BasicStroke((float) paramDouble1, 0, 1));
        } else {
            plotShape = new PlotShape((Shape) new RectangularBars(arrayOfVPoint, paramDouble1), paramPaint);
        }
        addObject(plotShape);
        repaint();
        return plotShape;
    }

    public PlotShape addPdf(Distribution paramDistribution, int paramInt, Color paramColor) {
        return addPdf(paramDistribution, this.horizontalAxis.getMin(), this.horizontalAxis.getMax(), paramInt, paramColor, PlotShape.defaultStroke, PlotShape.defaultPaint, false);
    }

    public PlotShape addPdf(Distribution paramDistribution, int paramInt, Color paramColor, Stroke paramStroke) {
        return addPdf(paramDistribution, this.horizontalAxis.getMin(), this.horizontalAxis.getMax(), paramInt, paramColor, paramStroke, PlotShape.defaultPaint, false);
    }

    public PlotShape addPdf(Distribution paramDistribution, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, boolean paramBoolean) {
        return addPdf(paramDistribution, paramDouble1, paramDouble2, paramInt, paramColor, PlotShape.defaultStroke, paramColor, paramBoolean);
    }

    public PlotShape addPdf(Distribution paramDistribution, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke) {
        return addPdf(paramDistribution, paramDouble1, paramDouble2, paramInt, paramColor, paramStroke, PlotShape.defaultPaint, false);
    }

    public PlotShape addPdf(Distribution paramDistribution, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke, Paint paramPaint, boolean paramBoolean) {
        this.D = paramDistribution;
        return addFunction(this, paramDouble1, paramDouble2, paramInt, paramColor, paramStroke, paramPaint, paramBoolean);
    }

    public PlotShape addPmf(Distribution paramDistribution, int paramInt1, int paramInt2, double paramDouble, Color paramColor, Paint paramPaint, boolean paramBoolean) {
        return addPmf(paramDistribution, 1 + paramInt2 - paramInt1, paramInt1, 1.0D, paramDouble, paramColor, paramPaint, paramBoolean, 0.0D);
    }

    public PlotShape addPmf(Distribution paramDistribution, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, Color paramColor, Paint paramPaint, boolean paramBoolean) {
        return addPmf(paramDistribution, paramInt, paramDouble1, paramDouble2, paramDouble3, paramColor, paramPaint, paramBoolean, 0.0D);
    }

    public PlotShape addPmf(Distribution paramDistribution, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, Color paramColor, Paint paramPaint, boolean paramBoolean, double paramDouble4) {
        PlotShape plotShape;
        VPoint[] arrayOfVPoint = new VPoint[paramInt];

        for (byte b = 0; b < paramInt; b++) {

            double d = paramDouble1 + b * paramDouble2;

            arrayOfVPoint[b] = new VPoint(d + paramDouble4, paramDistribution.pdf(d));
        }
        if (paramBoolean) {
            plotShape = new PlotShape((Shape) new LineBars(arrayOfVPoint), paramColor, new BasicStroke((float) paramDouble3, 0, 1));
        } else {
            plotShape = new PlotShape((Shape) new RectangularBars(arrayOfVPoint, paramDouble3), paramPaint);
        }
        addObject(plotShape);
        repaint();
        return plotShape;
    }

    public double getOrdinate(double paramDouble) {

        try {
            return this.D.pdf(paramDouble);
        } catch (IllegalArgumentException illegalArgumentException) {
            return Double.NaN;
        }

    }
}

