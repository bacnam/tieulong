package jsc.swt.plot2d;

import jsc.swt.virtualgraphics.VirtualTransform;

import java.awt.*;
import java.awt.geom.Point2D;

public interface PlotObject {
    boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform);

    void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform);
}

