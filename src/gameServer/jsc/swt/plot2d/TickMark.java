/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
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
/*     */ public abstract class TickMark
/*     */   implements PlotObject
/*     */ {
/*     */   public static final int DEFAULT_SIZE = 5;
/*     */   VPoint p;
/*     */   int size;
/*     */   Color colour;
/*     */   Stroke stroke;
/*     */   
/*     */   public TickMark(VPoint paramVPoint) {
/*  37 */     this(paramVPoint, 5, Color.black, new BasicStroke());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TickMark(VPoint paramVPoint, int paramInt) {
/*  46 */     this(paramVPoint, paramInt, Color.black, new BasicStroke());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TickMark(VPoint paramVPoint, int paramInt, Color paramColor) {
/*  56 */     this(paramVPoint, paramInt, paramColor, new BasicStroke());
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
/*     */   public TickMark(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
/*  68 */     this.p = paramVPoint;
/*  69 */     setSize(paramInt);
/*  70 */     this.colour = paramColor;
/*  71 */     this.stroke = paramStroke;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
/*  77 */     return false;
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
/*     */   public void setColour(Color paramColor) {
/* 112 */     this.colour = paramColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(VPoint paramVPoint) {
/* 119 */     this.p = paramVPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(int paramInt) {
/* 126 */     this.size = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStroke(Stroke paramStroke) {
/* 133 */     this.stroke = paramStroke;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/TickMark.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */