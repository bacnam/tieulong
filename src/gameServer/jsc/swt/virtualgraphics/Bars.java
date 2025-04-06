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
/*    */ public class Bars
/*    */   extends Path
/*    */ {
/*    */   public Bars(VPoint[] paramArrayOfVPoint) {
/* 26 */     super(paramArrayOfVPoint.length);
/* 27 */     int i = paramArrayOfVPoint.length;
/*    */     
/* 29 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 31 */       float f = (float)(paramArrayOfVPoint[b]).x;
/* 32 */       this.path.moveTo(f, 0.0F);
/* 33 */       this.path.lineTo(f, (float)(paramArrayOfVPoint[b]).y);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/Bars.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */