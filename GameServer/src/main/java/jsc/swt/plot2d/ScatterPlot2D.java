package jsc.swt.plot2d;

import jsc.swt.plot.AxisModel;
import jsc.swt.virtualgraphics.VPoint;

import java.awt.*;

public class ScatterPlot2D
        extends FunctionPlot2D
        implements Cloneable {
    public ScatterPlot2D(AxisModel paramAxisModel1, AxisModel paramAxisModel2, String paramString) {
        super(paramAxisModel1, paramAxisModel2, paramString);
    }

    public ScatterPlot2D(AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, String paramString) {
        super(paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, paramString);
    }

    public void addMarkers(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int paramInt1, int paramInt2, Color paramColor, Stroke paramStroke) {
        int i = paramArrayOfdouble1.length;
        if (paramArrayOfdouble2.length != i) {
            throw new IllegalArgumentException("Arrays not equal length.");
        }
        StandardMarker[] arrayOfStandardMarker = new StandardMarker[i];
        for (byte b = 0; b < i; b++)
            arrayOfStandardMarker[b] = new StandardMarker(new VPoint(paramArrayOfdouble1[b], paramArrayOfdouble2[b]), paramInt1, paramInt2, paramColor, paramStroke);
        addObjects((PlotObject[]) arrayOfStandardMarker);
    }

    public Object clone() {
        return super.clone();
    }

    public void setMarkerColour(Color paramColor) {
        for (byte b = 0; b < this.objects.size(); b++) {

            if (this.objects.elementAt(b) instanceof Marker) {

                Marker marker = this.objects.elementAt(b);
                marker.setColour(paramColor);
            }
        }
        repaint();
    }

    public void setMarkerColour(int paramInt, Color paramColor) {
        if (this.objects.elementAt(paramInt) instanceof Marker) {

            Marker marker = this.objects.elementAt(paramInt);
            marker.setColour(paramColor);
        }
    }

    public void setMarkerLocation(int paramInt, VPoint paramVPoint) {
        if (this.objects.elementAt(paramInt) instanceof Marker) {

            Marker marker = this.objects.elementAt(paramInt);
            marker.setLocation(paramVPoint);
        }
    }

    public void setMarkerSize(int paramInt) {
        for (byte b = 0; b < this.objects.size(); b++) {

            if (this.objects.elementAt(b) instanceof Marker) {

                Marker marker = this.objects.elementAt(b);
                marker.setSize(paramInt);
            }
        }
        repaint();
    }

    public void setMarkerSize(int paramInt1, int paramInt2) {
        if (this.objects.elementAt(paramInt1) instanceof Marker) {

            Marker marker = this.objects.elementAt(paramInt1);
            marker.setSize(paramInt2);
        }
    }

    public void setMarkerStroke(Stroke paramStroke) {
        for (byte b = 0; b < this.objects.size(); b++) {

            if (this.objects.elementAt(b) instanceof Marker) {

                Marker marker = this.objects.elementAt(b);
                marker.setStroke(paramStroke);
            }
        }
        repaint();
    }

    public void setMarkerStroke(int paramInt, Stroke paramStroke) {
        if (this.objects.elementAt(paramInt) instanceof Marker) {

            Marker marker = this.objects.elementAt(paramInt);
            marker.setStroke(paramStroke);
        }
    }

    public void setMarkerType(int paramInt) {
        for (byte b = 0; b < this.objects.size(); b++) {

            if (this.objects.elementAt(b) instanceof Marker) {

                Marker marker = this.objects.elementAt(b);
                marker.setType(paramInt);
            }
        }
        repaint();
    }

    public void setMarkerType(int paramInt1, int paramInt2) {
        if (this.objects.elementAt(paramInt1) instanceof Marker) {

            Marker marker = this.objects.elementAt(paramInt1);
            marker.setType(paramInt2);
        }
    }
}

