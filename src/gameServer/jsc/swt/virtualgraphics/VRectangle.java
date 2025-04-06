/*    */ package jsc.swt.virtualgraphics;
/*    */ 
/*    */ import java.awt.geom.Rectangle2D;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VRectangle
/*    */   extends Rectangle2D.Double
/*    */ {
/*    */   public VRectangle(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 27 */     super(Math.min(paramDouble1, paramDouble1 + paramDouble3), Math.min(paramDouble2, paramDouble2 + paramDouble4), Math.abs(paramDouble3), Math.abs(paramDouble4));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VRectangle(VPoint paramVPoint, VDimension paramVDimension) {
/* 38 */     this(paramVPoint.x, paramVPoint.y, paramVDimension.width, paramVDimension.height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VRectangle(VPoint paramVPoint1, VPoint paramVPoint2) {
/* 48 */     this(paramVPoint1.x, paramVPoint1.y, paramVPoint2.x - paramVPoint1.x, paramVPoint2.y - paramVPoint1.y);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VRectangle(Rectangle2D.Double paramDouble) {
/* 57 */     super(paramDouble.x, paramDouble.y, paramDouble.width, paramDouble.height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VRectangle copy() {
/* 66 */     return new VRectangle(this.x, this.y, this.width, this.height);
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
/*    */   
/*    */   public VRectangle enlarge(double paramDouble1, double paramDouble2) {
/* 80 */     double d1 = Math.abs(paramDouble1) * this.width;
/* 81 */     double d2 = Math.abs(paramDouble2) * this.height;
/* 82 */     return new VRectangle(this.x - 0.5D * (d1 - this.width), this.y + 0.5D * (d2 - this.height), d1, d2);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 86 */     return " x = " + this.x + ", y = " + this.y + ", width = " + this.width + ", height = " + this.height;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/VRectangle.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */