package jsc.swt.plot2d;

import jsc.swt.virtualgraphics.VPoint;
import jsc.swt.virtualgraphics.VirtualTransform;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class HorizontalTickMark
        extends TickMark {
    public HorizontalTickMark(VPoint paramVPoint) {
        super(paramVPoint);
    }

    public HorizontalTickMark(VPoint paramVPoint, int paramInt) {
        super(paramVPoint, paramInt);
    }

    public HorizontalTickMark(VPoint paramVPoint, int paramInt, Color paramColor) {
        super(paramVPoint, paramInt, paramColor);
    }

    public HorizontalTickMark(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
        super(paramVPoint, paramInt, paramColor, paramStroke);
    }

    public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
        Point2D.Float float_ = new Point2D.Float();
        paramVirtualTransform.transform((Point2D) this.p, float_);
        paramGraphics2D.setColor(this.colour);
        paramGraphics2D.setStroke(this.stroke);
        paramGraphics2D.draw(new Line2D.Float(float_.x - this.size, float_.y, float_.x, float_.y));
    }
}

