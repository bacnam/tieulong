/*    */ package jsc.swt.virtualgraphics;
/*    */ 
/*    */ import java.awt.geom.Line2D;
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
/*    */ public class VLine
/*    */   extends Line2D.Double
/*    */ {
/*    */   public VLine(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 21 */     super(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VLine(VPoint paramVPoint1, VPoint paramVPoint2) {
/* 29 */     super(paramVPoint1.x, paramVPoint1.y, paramVPoint2.x, paramVPoint2.y);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getSlope() {
/* 36 */     return (this.y2 - this.y1) / (this.x2 - this.x1);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/VLine.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */