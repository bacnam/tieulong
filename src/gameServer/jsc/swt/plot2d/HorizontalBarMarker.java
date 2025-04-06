/*    */ package jsc.swt.plot2d;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Shape;
/*    */ import java.awt.Stroke;
/*    */ import java.awt.geom.Line2D;
/*    */ import java.awt.geom.Point2D;
/*    */ import jsc.swt.virtualgraphics.VPoint;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HorizontalBarMarker
/*    */   extends Marker
/*    */ {
/*    */   public HorizontalBarMarker(VPoint paramVPoint, int paramInt) {
/* 27 */     super(paramVPoint, paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HorizontalBarMarker(VPoint paramVPoint, int paramInt, Color paramColor) {
/* 37 */     super(paramVPoint, paramInt, paramColor);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HorizontalBarMarker(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
/* 48 */     super(paramVPoint, paramInt, paramColor, paramStroke);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Shape getShape(Point2D.Float paramFloat) {
/* 61 */     float f = 0.5F * this.size;
/* 62 */     return new Line2D.Float(paramFloat.x - f, paramFloat.y, paramFloat.x + f, paramFloat.y);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/HorizontalBarMarker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */