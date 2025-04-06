/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.Point2D;
/*     */ import jsc.swt.virtualgraphics.VPoint;
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
/*     */ public abstract class Marker
/*     */   implements PlotObject
/*     */ {
/*  36 */   static Color defaultColour = Color.black;
/*     */ 
/*     */   
/*  39 */   static int defaultSize = 7;
/*     */ 
/*     */   
/*  42 */   static Stroke defaultStroke = new BasicStroke();
/*     */ 
/*     */   
/*     */   boolean filled = false;
/*     */ 
/*     */   
/*     */   VPoint p;
/*     */ 
/*     */   
/*     */   int size;
/*     */ 
/*     */   
/*     */   float halfSize;
/*     */ 
/*     */   
/*     */   Color colour;
/*     */ 
/*     */   
/*     */   Stroke stroke;
/*     */ 
/*     */ 
/*     */   
/*     */   public Marker(VPoint paramVPoint) {
/*  65 */     this(paramVPoint, defaultSize, defaultColour, defaultStroke);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Marker(VPoint paramVPoint, int paramInt) {
/*  74 */     this(paramVPoint, paramInt, defaultColour, defaultStroke);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Marker(VPoint paramVPoint, int paramInt, Color paramColor) {
/*  84 */     this(paramVPoint, paramInt, paramColor, defaultStroke);
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
/*     */   public Marker(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
/*  96 */     this.p = paramVPoint;
/*  97 */     setSize(paramInt);
/*  98 */     this.colour = paramColor;
/*  99 */     this.stroke = paramStroke;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
/* 104 */     Point2D.Float float_ = new Point2D.Float();
/* 105 */     paramVirtualTransform.transform(paramPoint2D, float_);
/*     */     
/* 107 */     return getBoundary(float_).contains(paramPoint2D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
/* 112 */     Point2D.Float float_ = new Point2D.Float();
/* 113 */     paramVirtualTransform.transform((Point2D)this.p, float_);
/* 114 */     paramGraphics2D.setColor(this.colour);
/* 115 */     paramGraphics2D.setStroke(this.stroke);
/* 116 */     if (this.filled) {
/* 117 */       paramGraphics2D.fill(getShape(float_));
/*     */     } else {
/* 119 */       paramGraphics2D.draw(getShape(float_));
/*     */     } 
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
/*     */   public Shape getBoundary(Point2D.Float paramFloat) {
/* 135 */     Shape shape = getShape(paramFloat);
/* 136 */     if (shape instanceof java.awt.geom.GeneralPath) {
/* 137 */       return shape.getBounds2D();
/*     */     }
/* 139 */     return shape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColour() {
/* 147 */     return this.colour;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColour(Color paramColor) {
/* 154 */     this.colour = paramColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VPoint getLocation() {
/* 161 */     return this.p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(VPoint paramVPoint) {
/* 168 */     this.p = paramVPoint;
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
/*     */   public abstract Shape getShape(Point2D.Float paramFloat);
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
/*     */   public int getSize() {
/* 192 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(int paramInt) {
/* 201 */     this.size = paramInt;
/* 202 */     this.halfSize = 0.5F * paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stroke getStroke() {
/* 210 */     return this.stroke;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStroke(Stroke paramStroke) {
/* 217 */     this.stroke = paramStroke;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilled() {
/* 224 */     return this.filled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilled(boolean paramBoolean) {
/* 232 */     this.filled = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int paramInt) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultColour(Color paramColor) {
/* 250 */     defaultColour = paramColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultSize(int paramInt) {
/* 259 */     defaultSize = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultStroke(Stroke paramStroke) {
/* 268 */     defaultStroke = paramStroke;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/Marker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */