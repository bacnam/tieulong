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
/*    */ public class HorizontalBars
/*    */   extends Path
/*    */ {
/*    */   public HorizontalBars(VPoint[] paramArrayOfVPoint, double paramDouble) {
/* 28 */     super(4 * paramArrayOfVPoint.length);
/* 29 */     int i = paramArrayOfVPoint.length;
/*    */     
/* 31 */     double d = 0.5D * paramDouble;
/*    */     
/* 33 */     for (byte b = 0; b < i; b++) {
/*    */       VRectangle vRectangle;
/* 35 */       double d1 = (paramArrayOfVPoint[b]).x;
/* 36 */       if (d1 < 0.0D) {
/* 37 */         vRectangle = new VRectangle(d1, (paramArrayOfVPoint[b]).y - d, -d1, paramDouble);
/*    */       } else {
/* 39 */         vRectangle = new VRectangle(0.0D, (paramArrayOfVPoint[b]).y - d, d1, paramDouble);
/* 40 */       }  this.path.append(vRectangle, false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/HorizontalBars.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */