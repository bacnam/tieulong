package jsc.swt.plot2d;

import jsc.swt.virtualgraphics.VirtualTransform;

import java.awt.*;
import java.awt.geom.Point2D;

public class PlotShape
        implements PlotObject {
    static Color defaultColour = Color.black;

    static Paint defaultPaint = Color.black;

    static Stroke defaultStroke = new BasicStroke();

    Shape shape;

    Color colour;

    Paint paint;

    Stroke stroke;

    boolean filled;

    public PlotShape(Shape paramShape) {
        this.shape = paramShape;
        this.colour = defaultColour;
        this.stroke = defaultStroke;
        this.paint = defaultPaint;
        this.filled = false;
    }

    public PlotShape(Shape paramShape, Color paramColor) {
        this.shape = paramShape;
        this.colour = paramColor;
        this.stroke = defaultStroke;
        this.paint = paramColor;
        this.filled = false;
    }

    public PlotShape(Shape paramShape, Paint paramPaint) {
        this.shape = paramShape;
        this.colour = defaultColour;
        this.stroke = defaultStroke;
        this.paint = paramPaint;
        this.filled = true;
    }

    public PlotShape(Shape paramShape, Color paramColor, Stroke paramStroke) {
        this.shape = paramShape;
        this.colour = paramColor;
        this.stroke = paramStroke;
        this.paint = paramColor;
        this.filled = false;
    }

    public PlotShape(Shape paramShape, Color paramColor, Stroke paramStroke, Paint paramPaint, boolean paramBoolean) {
        this.shape = paramShape;
        this.colour = paramColor;
        this.stroke = paramStroke;
        this.paint = paramPaint;
        this.filled = paramBoolean;
    }

    public static void setDefaultColour(Color paramColor) {
        defaultColour = paramColor;
    }

    public static void setDefaultPaint(Paint paramPaint) {
        defaultPaint = paramPaint;
    }

    public static void setDefaultStroke(Stroke paramStroke) {
        defaultStroke = paramStroke;
    }

    public Object clone() {
        return copy();
    }

    public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
        return paramVirtualTransform.createTransformedShape(this.stroke.createStrokedShape(this.shape)).contains(paramPoint2D);
    }

    public PlotShape copy() {
        return new PlotShape(this.shape, this.colour, this.stroke, this.paint, this.filled);
    }

    public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
        paramGraphics2D.setColor(this.colour);
        paramGraphics2D.setStroke(this.stroke);
        paramGraphics2D.setPaint(this.paint);
        if (this.filled) {
            paramGraphics2D.fill(paramVirtualTransform.createTransformedShape(this.shape));
        } else {
            paramGraphics2D.draw(paramVirtualTransform.createTransformedShape(this.shape));
        }
    }

    public Color getColour() {
        return this.colour;
    }

    public void setColour(Color paramColor) {
        this.colour = paramColor;
        this.paint = paramColor;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setPaint(Paint paramPaint) {
        this.paint = paramPaint;
    }

    public Shape getShape() {
        return this.shape;
    }

    public void setShape(Shape paramShape) {
        this.shape = paramShape;
    }

    public Stroke getStroke() {
        return this.stroke;
    }

    public void setStroke(Stroke paramStroke) {
        this.stroke = paramStroke;
    }

    public boolean isFilled() {
        return this.filled;
    }

    public void setFilled(boolean paramBoolean) {
        this.filled = paramBoolean;
    }
}

