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
/*    */ public class VPolygon
/*    */   extends Path
/*    */ {
/*    */   public VPolygon(VPoint[] paramArrayOfVPoint) {
/* 22 */     super(paramArrayOfVPoint.length);
/* 23 */     this.path.moveTo((float)(paramArrayOfVPoint[0]).x, (float)(paramArrayOfVPoint[0]).y);
/* 24 */     for (byte b = 1; b < paramArrayOfVPoint.length; ) { this.path.lineTo((float)(paramArrayOfVPoint[b]).x, (float)(paramArrayOfVPoint[b]).y); b++; }
/* 25 */      this.path.closePath();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VPolygon(VPoint[] paramArrayOfVPoint, int paramInt) {
/* 36 */     super(paramInt);
/* 37 */     this.path.moveTo((float)(paramArrayOfVPoint[0]).x, (float)(paramArrayOfVPoint[0]).y);
/* 38 */     for (byte b = 1; b <= paramInt; ) { this.path.lineTo((float)(paramArrayOfVPoint[b]).x, (float)(paramArrayOfVPoint[b]).y); b++; }
/* 39 */      this.path.closePath();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/VPolygon.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */