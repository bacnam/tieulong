package jsc.swt.plot2d;

import jsc.ci.ConfidenceBand;
import jsc.distributions.Beta;
import jsc.distributions.Distribution;
import jsc.goodnessfit.KolmogorovTest;
import jsc.goodnessfit.SampleDistributionFunction;
import jsc.swt.plot.AxisModel;
import jsc.swt.plot.LinearAxisModel;
import jsc.swt.plot.PlotFunction;
import jsc.swt.virtualgraphics.Polyline;
import jsc.swt.virtualgraphics.VPoint;
import jsc.tests.H1;
import jsc.util.Scale;

import javax.swing.*;
import java.awt.*;

public class CdfPlot
        extends FunctionPlot2D
        implements PlotFunction, Cloneable {
    private Distribution F;

    public CdfPlot(String paramString, AxisModel paramAxisModel) {
        super(paramAxisModel, (AxisModel) new LinearAxisModel("CDF", new Scale(0.0D, 1.0D, 11, false), "#.#"), paramString);
    }

    public CdfPlot(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2) {
        super(paramAxisModel1, paramAxisModel2, paramString);
    }

    public PlotObject addCdf(Distribution paramDistribution, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke) {
        this.F = paramDistribution;
        return addFunction(this, paramDouble1, paramDouble2, paramInt, paramColor, paramStroke);
    }

    public PlotObject addCdf(Distribution paramDistribution, int paramInt1, int paramInt2, Color paramColor, Stroke paramStroke) {
        int i = 1 + paramInt2 - paramInt1;
        double[] arrayOfDouble1 = new double[i];
        double[] arrayOfDouble2 = new double[i];
        for (byte b = 0; b < i; b++) {

            arrayOfDouble1[b] = (paramInt1 + b);

            arrayOfDouble2[b] = paramDistribution.cdf(arrayOfDouble1[b]);
        }

        return addStepFunction(arrayOfDouble1, arrayOfDouble2, true, paramColor, paramStroke);
    }

    public PlotObject[] addConfidenceBand(ConfidenceBand paramConfidenceBand, Color paramColor, Stroke paramStroke) {
        int i = paramConfidenceBand.getN();
        double[] arrayOfDouble1 = new double[i];
        double[] arrayOfDouble2 = new double[i];
        double[] arrayOfDouble3 = new double[i];
        PlotObject[] arrayOfPlotObject = new PlotObject[2];

        for (byte b = 0; b < i; b++) {

            arrayOfDouble1[b] = paramConfidenceBand.getX(b);
            arrayOfDouble2[b] = paramConfidenceBand.getLowerLimit(b);
            arrayOfDouble3[b] = paramConfidenceBand.getUpperLimit(b);
        }
        arrayOfPlotObject[0] = addStepFunction(arrayOfDouble1, arrayOfDouble2, false, paramColor, paramStroke);
        arrayOfPlotObject[1] = addStepFunction(arrayOfDouble1, arrayOfDouble3, false, paramColor, paramStroke);

        return arrayOfPlotObject;
    }

    public PlotObject addSampleDistributionFunction(SampleDistributionFunction paramSampleDistributionFunction, Color paramColor, Stroke paramStroke) {
        return addStepFunction(paramSampleDistributionFunction.getOrderedX(), paramSampleDistributionFunction.getOrderedS(), true, paramColor, paramStroke);
    }

    public PlotObject addStepFunction(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, boolean paramBoolean, Color paramColor, Stroke paramStroke) {
        byte b;
        int i = paramArrayOfdouble1.length;
        int j = paramBoolean ? (2 * i + 1) : (2 * i - 1);

        if (paramArrayOfdouble2.length != i) {
            throw new IllegalArgumentException("Arrays not equal length.");
        }
        VPoint[] arrayOfVPoint = new VPoint[j];
        if (paramBoolean) {

            arrayOfVPoint[0] = new VPoint(paramArrayOfdouble1[0], 0.0D);
            b = 0;
        } else {

            b = -1;
        }
        for (byte b1 = 0; b1 < i - 1; b1++) {

            double d = paramArrayOfdouble2[b1];
            arrayOfVPoint[++b] = new VPoint(paramArrayOfdouble1[b1], d);
            arrayOfVPoint[++b] = new VPoint(paramArrayOfdouble1[b1 + 1], d);
        }
        arrayOfVPoint[++b] = new VPoint(paramArrayOfdouble1[i - 1], paramArrayOfdouble2[i - 1]);
        if (paramBoolean) {
            arrayOfVPoint[++b] = new VPoint(this.horizontalAxis.getMax(), paramArrayOfdouble2[i - 1]);
        }

        PlotShape plotShape = new PlotShape((Shape) new Polyline(arrayOfVPoint), paramColor, paramStroke);
        addObject(plotShape);
        repaint();
        return plotShape;
    }

    public Object clone() {
        return super.clone();
    }

    public double getOrdinate(double paramDouble) {

        try {
            return this.F.cdf(paramDouble);
        } catch (IllegalArgumentException illegalArgumentException) {
            return Double.NaN;
        }

    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            char c = 'ᎈ';
            Beta beta = new Beta(0.5D, 0.1D);
            double d1 = 0.0D, d2 = 1.0D;
            Scale scale = new Scale(0.0D, 1.0D, 11, false);
            double[] arrayOfDouble = new double[c];
            for (byte b = 0; b < c; ) {
                arrayOfDouble[b] = beta.random();
                b++;
            }

            JFrame jFrame = new JFrame("CDF plot");

            LinearAxisModel linearAxisModel = new LinearAxisModel("x", scale, "##.#");
            CdfPlot cdfPlot = new CdfPlot("Cdf plot", (AxisModel) linearAxisModel);

            cdfPlot.setPreferredSize(new Dimension(500, 400));
            cdfPlot.addSampleDistributionFunction(new SampleDistributionFunction(arrayOfDouble), Color.black, new BasicStroke(1.0F));

            cdfPlot.addCdf((Distribution) beta, d1, d2, c, Color.red, new DashStroke(1.0F));

            cdfPlot.setZoomable(true);

            KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, (Distribution) beta, H1.NOT_EQUAL, false);
            double d3 = kolmogorovTest.xOfD();
            System.out.println("D = " + kolmogorovTest.getTestStatistic() + " x = " + d3 + " SP = " + kolmogorovTest.getSP());
            cdfPlot.addVerticalLine(d3, Color.blue, new DashStroke(1.0F));
            Container container = jFrame.getContentPane();
            container.setLayout(new BorderLayout());
            container.add(cdfPlot, "Center");

            jFrame.setVisible(true);
            jFrame.setDefaultCloseOperation(3);
            jFrame.pack();
        }
    }
}

