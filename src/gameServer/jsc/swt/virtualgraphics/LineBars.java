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
/*    */ public class LineBars
/*    */   extends Path
/*    */ {
/*    */   public LineBars(VPoint[] paramArrayOfVPoint) {
/* 27 */     super(paramArrayOfVPoint.length);
/* 28 */     int i = paramArrayOfVPoint.length;
/*    */     
/* 30 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 32 */       float f = (float)(paramArrayOfVPoint[b]).x;
/* 33 */       this.path.moveTo(f, 0.0F);
/* 34 */       this.path.lineTo(f, (float)(paramArrayOfVPoint[b]).y);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/LineBars.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */