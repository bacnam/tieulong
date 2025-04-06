/*    */ package jsc.swt.virtualgraphics;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RectangularBars
/*    */   extends Path
/*    */ {
/*    */   public RectangularBars(VPoint[] paramArrayOfVPoint, double paramDouble) {
/* 28 */     super(4 * paramArrayOfVPoint.length);
/* 29 */     int i = paramArrayOfVPoint.length;
/*    */     
/* 31 */     double d = 0.5D * paramDouble;
/* 32 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 34 */       VRectangle vRectangle = new VRectangle((paramArrayOfVPoint[b]).x - d, 0.0D, paramDouble, (paramArrayOfVPoint[b]).y);
/* 35 */       this.path.append(vRectangle, false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/RectangularBars.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */