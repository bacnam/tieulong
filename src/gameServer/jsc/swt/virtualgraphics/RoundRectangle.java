/*    */ package jsc.swt.virtualgraphics;
/*    */ 
/*    */ import java.awt.geom.RoundRectangle2D;
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
/*    */ public class RoundRectangle
/*    */   extends RoundRectangle2D.Double
/*    */ {
/*    */   public RoundRectangle(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
/* 24 */     super(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RoundRectangle(VPoint paramVPoint, VDimension paramVDimension1, VDimension paramVDimension2) {
/* 35 */     this(paramVPoint.x, paramVPoint.y, paramVDimension1.width, paramVDimension1.height, paramVDimension2.width, paramVDimension2.height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RoundRectangle(VPoint paramVPoint1, VPoint paramVPoint2, VDimension paramVDimension) {
/* 45 */     this(paramVPoint1.x, paramVPoint1.y, paramVPoint2.x - paramVPoint1.x, paramVPoint1.y - paramVPoint2.y, paramVDimension.width, paramVDimension.height);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/RoundRectangle.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */