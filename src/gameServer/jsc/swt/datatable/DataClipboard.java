/*    */ package jsc.swt.datatable;
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
/*    */ public class DataClipboard
/*    */   extends DataMatrix
/*    */ {
/*    */   public static final int ROWS = 1;
/*    */   public static final int COLUMNS = 2;
/*    */   public static final int CELLS = 3;
/*    */   int copyMode;
/*    */   
/*    */   public DataClipboard() {
/* 24 */     super(0, 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DataClipboard(int paramInt1, int paramInt2, int paramInt3) {
/* 34 */     super(paramInt2, paramInt3); this.copyMode = paramInt1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCopyMode() {
/* 41 */     return this.copyMode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasData() {
/* 48 */     return (this.rowCount > 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCopyMode(int paramInt) {
/* 55 */     this.copyMode = paramInt;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/DataClipboard.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */