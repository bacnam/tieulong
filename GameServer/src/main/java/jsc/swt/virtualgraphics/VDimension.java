package jsc.swt.virtualgraphics;

import java.awt.geom.Dimension2D;

public class VDimension
        extends Dimension2D {
    public double width;
    public double height;

    public VDimension(double paramDouble1, double paramDouble2) {
        this.width = paramDouble1;
        this.height = paramDouble2;
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public VDimension multiply(double paramDouble) {
        return new VDimension(paramDouble * this.width, paramDouble * this.height);
    }

    public void setSize(double paramDouble1, double paramDouble2) {
        this.width = paramDouble1;
        this.height = paramDouble2;
    }

    public String toString() {
        return " width = " + this.width + ", height = " + this.height;
    }
}

