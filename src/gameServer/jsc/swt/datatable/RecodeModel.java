/*    */ package jsc.swt.datatable;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import javax.swing.table.AbstractTableModel;
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
/*    */ public class RecodeModel
/*    */   extends AbstractTableModel
/*    */ {
/*    */   Vector labels;
/*    */   String[] newValues;
/*    */   
/*    */   public RecodeModel(Vector paramVector) {
/* 27 */     this.labels = paramVector;
/* 28 */     this.newValues = new String[paramVector.size()];
/*    */   }
/*    */ 
/*    */   
/*    */   public Class getColumnClass(int paramInt) {
/* 33 */     return String.class;
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
/*    */   public int getColumnCount() {
/* 46 */     return 2;
/*    */   }
/*    */   
/*    */   public String getColumnName(int paramInt) {
/* 50 */     switch (paramInt) {
/*    */       case 0:
/* 52 */         return "Value";
/* 53 */       case 1: return "New value";
/*    */     } 
/* 55 */     return null;
/*    */   }
/*    */   public int getRowCount() {
/* 58 */     return this.labels.size();
/*    */   }
/*    */   
/*    */   public Object getValueAt(int paramInt1, int paramInt2) {
/* 62 */     switch (paramInt2) {
/*    */       case 0:
/* 64 */         return this.labels.elementAt(paramInt1);
/* 65 */       case 1: return this.newValues[paramInt1];
/*    */     } 
/* 67 */     return null;
/*    */   }
/*    */   public boolean isCellEditable(int paramInt1, int paramInt2) {
/* 70 */     return (paramInt2 == 1);
/*    */   }
/*    */   public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
/* 73 */     if (paramInt2 == 1) this.newValues[paramInt1] = (String)paramObject; 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/RecodeModel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */