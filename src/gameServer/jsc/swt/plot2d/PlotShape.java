/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.Point2D;
/*     */ import jsc.swt.virtualgraphics.VirtualTransform;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlotShape
/*     */   implements PlotObject
/*     */ {
/*  25 */   static Color defaultColour = Color.black;
/*     */ 
/*     */   
/*  28 */   static Paint defaultPaint = Color.black;
/*     */ 
/*     */   
/*  31 */   static Stroke defaultStroke = new BasicStroke();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Shape shape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Color colour;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Paint paint;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Stroke stroke;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean filled;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape(Shape paramShape) {
/*  65 */     this.shape = paramShape;
/*  66 */     this.colour = defaultColour;
/*  67 */     this.stroke = defaultStroke;
/*  68 */     this.paint = defaultPaint;
/*  69 */     this.filled = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape(Shape paramShape, Color paramColor) {
/*  84 */     this.shape = paramShape;
/*  85 */     this.colour = paramColor;
/*  86 */     this.stroke = defaultStroke;
/*  87 */     this.paint = paramColor;
/*  88 */     this.filled = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape(Shape paramShape, Paint paramPaint) {
/* 101 */     this.shape = paramShape;
/* 102 */     this.colour = defaultColour;
/* 103 */     this.stroke = defaultStroke;
/* 104 */     this.paint = paramPaint;
/* 105 */     this.filled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape(Shape paramShape, Color paramColor, Stroke paramStroke) {
/* 120 */     this.shape = paramShape;
/* 121 */     this.colour = paramColor;
/* 122 */     this.stroke = paramStroke;
/* 123 */     this.paint = paramColor;
/* 124 */     this.filled = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape(Shape paramShape, Color paramColor, Stroke paramStroke, Paint paramPaint, boolean paramBoolean) {
/* 141 */     this.shape = paramShape;
/* 142 */     this.colour = paramColor;
/* 143 */     this.stroke = paramStroke;
/* 144 */     this.paint = paramPaint;
/* 145 */     this.filled = paramBoolean;
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 149 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
/* 155 */     return paramVirtualTransform.createTransformedShape(this.stroke.createStrokedShape(this.shape)).contains(paramPoint2D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape copy() {
/* 165 */     return new PlotShape(this.shape, this.colour, this.stroke, this.paint, this.filled);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
/* 171 */     paramGraphics2D.setColor(this.colour);
/* 172 */     paramGraphics2D.setStroke(this.stroke);
/* 173 */     paramGraphics2D.setPaint(this.paint);
/* 174 */     if (this.filled) {
/* 175 */       paramGraphics2D.fill(paramVirtualTransform.createTransformedShape(this.shape));
/*     */     } else {
/* 177 */       paramGraphics2D.draw(paramVirtualTransform.createTransformedShape(this.shape));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColour() {
/* 187 */     return this.colour;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColour(Color paramColor) {
/* 195 */     this.colour = paramColor; this.paint = paramColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paint getPaint() {
/* 202 */     return this.paint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPaint(Paint paramPaint) {
/* 209 */     this.paint = paramPaint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 216 */     return this.shape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShape(Shape paramShape) {
/* 224 */     this.shape = paramShape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stroke getStroke() {
/* 231 */     return this.stroke;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStroke(Stroke paramStroke) {
/* 238 */     this.stroke = paramStroke;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilled() {
/* 245 */     return this.filled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilled(boolean paramBoolean) {
/* 252 */     this.filled = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultColour(Color paramColor) {
/* 261 */     defaultColour = paramColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultPaint(Paint paramPaint) {
/* 270 */     defaultPaint = paramPaint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultStroke(Stroke paramStroke) {
/* 279 */     defaultStroke = paramStroke;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/PlotShape.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */