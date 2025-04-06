/*    */ package jsc.swt.datatable;
/*    */ 
/*    */ import java.awt.Component;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DataCalculator
/*    */ {
/*    */   protected DataTable dataTable;
/*    */   protected Component parentComponent;
/*    */   
/*    */   public DataCalculator(Component paramComponent, DataTable paramDataTable) {
/* 41 */     this.dataTable = paramDataTable;
/* 42 */     this.parentComponent = paramComponent;
/*    */   }
/*    */   
/*    */   public abstract void show();
/*    */   
/*    */   public abstract void updateNames();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/DataCalculator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */