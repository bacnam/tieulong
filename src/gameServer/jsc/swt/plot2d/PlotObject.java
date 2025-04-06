package jsc.swt.plot2d;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import jsc.swt.virtualgraphics.VirtualTransform;

public interface PlotObject {
  boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform);
  
  void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/PlotObject.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */