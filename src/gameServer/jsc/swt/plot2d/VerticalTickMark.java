/*    */ package jsc.swt.plot2d;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Stroke;
/*    */ import java.awt.geom.Line2D;
/*    */ import java.awt.geom.Point2D;
/*    */ import jsc.swt.virtualgraphics.VPoint;
/*    */ import jsc.swt.virtualgraphics.VirtualTransform;
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
/*    */ public class VerticalTickMark
/*    */   extends TickMark
/*    */ {
/*    */   public VerticalTickMark(VPoint paramVPoint) {
/* 28 */     super(paramVPoint);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VerticalTickMark(VPoint paramVPoint, int paramInt) {
/* 36 */     super(paramVPoint, paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VerticalTickMark(VPoint paramVPoint, int paramInt, Color paramColor) {
/* 45 */     super(paramVPoint, paramInt, paramColor);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VerticalTickMark(VPoint paramVPoint, int paramInt, Color paramColor, Stroke paramStroke) {
/* 56 */     super(paramVPoint, paramInt, paramColor, paramStroke);
/*    */   }
/*    */   
/*    */   public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
/* 60 */     Point2D.Float float_ = new Point2D.Float();
/* 61 */     paramVirtualTransform.transform((Point2D)this.p, float_);
/* 62 */     paramGraphics2D.setColor(this.colour);
/* 63 */     paramGraphics2D.setStroke(this.stroke);
/* 64 */     paramGraphics2D.draw(new Line2D.Float(float_.x, float_.y, float_.x, float_.y + this.size));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/VerticalTickMark.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */