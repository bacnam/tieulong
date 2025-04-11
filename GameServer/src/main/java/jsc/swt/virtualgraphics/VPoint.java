package jsc.swt.virtualgraphics;

import java.awt.geom.Point2D;

public class VPoint
        extends Point2D.Double {
    public VPoint(double paramDouble1, double paramDouble2) {
        super(paramDouble1, paramDouble2);
    }

    public VPoint add(VDimension paramVDimension) {
        return new VPoint(this.x + paramVDimension.width, this.y + paramVDimension.height);
    }

    public VPoint subtract(VDimension paramVDimension) {
        return new VPoint(this.x - paramVDimension.width, this.y - paramVDimension.height);
    }

    public String toString() {
        return " x = " + this.x + ", y = " + this.y;
    }
}

