package jsc.swt.virtualgraphics;

import java.awt.geom.Line2D;

public class VLine
        extends Line2D.Double {
    public VLine(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        super(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
    }

    public VLine(VPoint paramVPoint1, VPoint paramVPoint2) {
        super(paramVPoint1.x, paramVPoint1.y, paramVPoint2.x, paramVPoint2.y);
    }

    public double getSlope() {
        return (this.y2 - this.y1) / (this.x2 - this.x1);
    }
}

