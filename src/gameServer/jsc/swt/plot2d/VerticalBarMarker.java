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
/*    */ 
/*    */ public final class VerticalBarMarker
/*    */   extends Marker
/*    */ {
/*    */   public VerticalBarMarker(VPoint paramVPoint, int paramInt) {
/* 28 */     super(paramVPoint, paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VerticalBarMarker(VPoint paramVPoint, int paramInt, Color paramColor) {
/* 39 */     super(paramVPoint, paramInt, paramColor);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VerticalBarMarker(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
/* 50 */     super(paramVPoint, paramInt, paramColor, paramStroke);
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
/* 63 */     float f = 0.5F * this.size;
/* 64 */     return new Line2D.Float(paramFloat.x, paramFloat.y - f, paramFloat.x, paramFloat.y + f);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/VerticalBarMarker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */