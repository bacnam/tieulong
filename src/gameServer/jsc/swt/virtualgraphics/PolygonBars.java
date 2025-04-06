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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PolygonBars
/*    */   extends Path
/*    */ {
/*    */   public PolygonBars(double paramDouble1, double paramDouble2, double[] paramArrayOfdouble) {
/* 32 */     super(2 + paramArrayOfdouble.length + paramArrayOfdouble.length);
/* 33 */     int i = paramArrayOfdouble.length;
/*    */     
/* 35 */     float f = (float)paramDouble1;
/* 36 */     this.path.moveTo(f, 0.0F);
/*    */     
/* 38 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 40 */       f = (float)(paramDouble1 + b * paramDouble2);
/* 41 */       float f1 = (float)paramArrayOfdouble[b];
/* 42 */       this.path.lineTo(f, f1);
/* 43 */       this.path.lineTo((float)(f + paramDouble2), f1);
/*    */     } 
/* 45 */     this.path.lineTo((float)(f + paramDouble2), 0.0F);
/* 46 */     this.path.closePath();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/PolygonBars.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */