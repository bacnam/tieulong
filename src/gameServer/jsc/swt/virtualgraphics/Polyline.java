/*    */ package jsc.swt.virtualgraphics;
/*    */ 
/*    */ import java.util.Vector;
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
/*    */ public class Polyline
/*    */   extends Path
/*    */ {
/*    */   public Polyline(VPoint[] paramArrayOfVPoint) {
/* 23 */     super(paramArrayOfVPoint.length);
/* 24 */     this.path.moveTo((float)(paramArrayOfVPoint[0]).x, (float)(paramArrayOfVPoint[0]).y);
/* 25 */     for (byte b = 1; b < paramArrayOfVPoint.length; b++) {
/* 26 */       this.path.lineTo((float)(paramArrayOfVPoint[b]).x, (float)(paramArrayOfVPoint[b]).y);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Polyline(Vector paramVector) {
/* 36 */     super(paramVector.size());
/* 37 */     VPoint vPoint = paramVector.elementAt(0);
/* 38 */     this.path.moveTo((float)vPoint.x, (float)vPoint.y);
/* 39 */     for (byte b = 1; b < paramVector.size(); b++) {
/*    */       
/* 41 */       vPoint = paramVector.elementAt(b);
/* 42 */       this.path.lineTo((float)vPoint.x, (float)vPoint.y);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Polyline(VPoint[] paramArrayOfVPoint, int paramInt) {
/* 54 */     super(paramInt);
/*    */     
/* 56 */     this.path.moveTo((float)(paramArrayOfVPoint[0]).x, (float)(paramArrayOfVPoint[0]).y);
/* 57 */     for (byte b = 1; b < paramArrayOfVPoint.length; b++) {
/* 58 */       this.path.lineTo((float)(paramArrayOfVPoint[b]).x, (float)(paramArrayOfVPoint[b]).y);
/*    */     }
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
/*    */   
/*    */   public Polyline(VPoint paramVPoint, int paramInt) {
/* 74 */     super(paramInt);
/* 75 */     this.path.moveTo((float)paramVPoint.x, (float)paramVPoint.y);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void lineTo(VPoint paramVPoint) {
/* 85 */     this.path.lineTo((float)paramVPoint.x, (float)paramVPoint.y);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/Polyline.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */