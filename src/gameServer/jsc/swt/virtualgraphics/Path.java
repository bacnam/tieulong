/*     */ package jsc.swt.virtualgraphics;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
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
/*     */ public class Path
/*     */   implements Shape
/*     */ {
/*     */   GeneralPath path;
/*     */   
/*     */   public Path() {
/*  29 */     this.path = new GeneralPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path(int paramInt1, int paramInt2) {
/*  39 */     this.path = new GeneralPath(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path(int paramInt) {
/*  49 */     this.path = new GeneralPath(0, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path(Shape paramShape) {
/*  57 */     this.path = new GeneralPath(paramShape);
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
/*     */   public void append(Shape paramShape, boolean paramBoolean) {
/*  72 */     this.path.append(paramShape, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closePath() {
/*  79 */     this.path.closePath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape createTransformedShape(AffineTransform paramAffineTransform) {
/*  87 */     return this.path.createTransformedShape(paramAffineTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void curveTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
/*  97 */     this.path.curveTo((float)paramDouble1, (float)paramDouble2, (float)paramDouble3, (float)paramDouble4, (float)paramDouble5, (float)paramDouble6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lineTo(double paramDouble1, double paramDouble2) {
/* 105 */     this.path.lineTo((float)paramDouble1, (float)paramDouble2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void moveTo(double paramDouble1, double paramDouble2) {
/* 112 */     this.path.moveTo((float)paramDouble1, (float)paramDouble2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void quadTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 122 */     this.path.quadTo((float)paramDouble1, (float)paramDouble2, (float)paramDouble3, (float)paramDouble4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 128 */     this.path.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWindingRule(int paramInt) {
/* 135 */     this.path.setWindingRule(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void transform(AffineTransform paramAffineTransform) {
/* 143 */     this.path.transform(paramAffineTransform);
/*     */   }
/*     */   
/* 146 */   public boolean contains(double paramDouble1, double paramDouble2) { return this.path.contains(paramDouble1, paramDouble2); }
/* 147 */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) { return this.path.contains(paramDouble1, paramDouble2, paramDouble3, paramDouble4); }
/* 148 */   public boolean contains(Point2D paramPoint2D) { return this.path.contains(paramPoint2D); }
/* 149 */   public boolean contains(Rectangle2D paramRectangle2D) { return this.path.contains(paramRectangle2D); }
/* 150 */   public Rectangle getBounds() { return this.path.getBounds(); }
/* 151 */   public Rectangle2D getBounds2D() { return this.path.getBounds2D(); }
/* 152 */   public PathIterator getPathIterator(AffineTransform paramAffineTransform) { return this.path.getPathIterator(paramAffineTransform); }
/* 153 */   public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble) { return this.path.getPathIterator(paramAffineTransform, paramDouble); }
/* 154 */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) { return this.path.intersects(paramDouble1, paramDouble2, paramDouble3, paramDouble4); } public boolean intersects(Rectangle2D paramRectangle2D) {
/* 155 */     return this.path.intersects(paramRectangle2D);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/Path.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */