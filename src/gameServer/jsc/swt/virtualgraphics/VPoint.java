/*    */ package jsc.swt.virtualgraphics;
/*    */ 
/*    */ import java.awt.geom.Point2D;
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
/*    */ public class VPoint
/*    */   extends Point2D.Double
/*    */ {
/*    */   public VPoint(double paramDouble1, double paramDouble2) {
/* 19 */     super(paramDouble1, paramDouble2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VPoint add(VDimension paramVDimension) {
/* 28 */     return new VPoint(this.x + paramVDimension.width, this.y + paramVDimension.height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VPoint subtract(VDimension paramVDimension) {
/* 37 */     return new VPoint(this.x - paramVDimension.width, this.y - paramVDimension.height);
/*    */   }
/*    */   public String toString() {
/* 40 */     return " x = " + this.x + ", y = " + this.y;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/VPoint.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */