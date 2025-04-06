/*     */ package jsc.swt.datatable;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ import javax.swing.event.TableModelListener;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataModel
/*     */   extends AbstractTableModel
/*     */ {
/*     */   boolean editable;
/*     */   boolean changed;
/*     */   DataMatrix dataMatrix;
/*     */   
/*     */   public DataModel(DataMatrix paramDataMatrix) {
/*  39 */     this.editable = true;
/*  40 */     this.changed = false;
/*  41 */     this.dataMatrix = paramDataMatrix;
/*  42 */     addTableModelListener(new DataChangeListener(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int addColumn() {
/*  52 */     int i = this.dataMatrix.addColumn();
/*  53 */     fireTableStructureChanged();
/*  54 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRow() {
/*  60 */     this.dataMatrix.addRow();
/*  61 */     fireTableDataChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyColumn(int paramInt1, int paramInt2) {
/* 101 */     this.dataMatrix.copyColumn(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyRow(int paramInt1, int paramInt2) {
/* 113 */     this.dataMatrix.copyRow(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getColumnClass(int paramInt) {
/* 123 */     return this.dataMatrix.getColumnClass(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/* 130 */     return this.dataMatrix.getColumnCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnName(int paramInt) {
/* 138 */     return this.dataMatrix.getColumnName(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getColumnNames(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt) {
/* 150 */     return this.dataMatrix.getColumnNames(paramBoolean1, paramBoolean2, paramBoolean3, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getDefaultColumnClass() {
/* 160 */     return this.dataMatrix.getDefaultColumnClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultNamePrefix() {
/* 167 */     return this.dataMatrix.getDefaultNamePrefix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/* 174 */     return this.dataMatrix.getRowCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValueAt(int paramInt1, int paramInt2) {
/* 183 */     return this.dataMatrix.getValueAt(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertColumn(int paramInt) {
/* 191 */     this.dataMatrix.insertColumn(paramInt); fireTableStructureChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int insertRow(int paramInt) {
/* 201 */     this.dataMatrix.insertRow(paramInt);
/*     */     
/* 203 */     fireTableDataChanged();
/* 204 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertValue(Object paramObject, int paramInt1, int paramInt2) {
/* 224 */     this.dataMatrix.insertValue(paramObject, paramInt1, paramInt2);
/*     */     
/* 226 */     fireTableDataChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCellEditable(int paramInt1, int paramInt2) {
/* 236 */     return this.editable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChanged() {
/* 243 */     return this.changed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isColumnDataDouble(int paramInt) {
/* 251 */     return this.dataMatrix.isColumnDataDouble(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isColumnDataInteger(int paramInt) {
/* 259 */     return this.dataMatrix.isColumnDataInteger(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCell(int paramInt1, int paramInt2) {
/* 269 */     this.dataMatrix.removeCell(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeColumn(int paramInt) {
/* 280 */     this.dataMatrix.removeColumn(paramInt);
/* 281 */     fireTableStructureChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeColumn(String paramString) {
/* 291 */     this.dataMatrix.removeColumn(paramString);
/* 292 */     fireTableStructureChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeRow(int paramInt) {
/* 302 */     this.dataMatrix.removeRow(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged(boolean paramBoolean) {
/* 311 */     this.changed = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumnClass(int paramInt, Class paramClass) {
/* 321 */     this.dataMatrix.setColumnClass(paramInt, paramClass);
/*     */     
/* 323 */     fireTableDataChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumnName(int paramInt, String paramString) {
/* 334 */     this.dataMatrix.setColumnName(paramInt, paramString);
/* 335 */     fireTableStructureChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultColumnClass(Class paramClass) {
/* 347 */     this.dataMatrix.setDefaultColumnClass(paramClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEditable(boolean paramBoolean) {
/* 354 */     this.editable = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
/* 365 */     this.dataMatrix.setValueAt(paramObject, paramInt1, paramInt2);
/* 366 */     fireTableCellUpdated(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void swapRow(int paramInt1, int paramInt2) {
/* 377 */     this.dataMatrix.copyRow(paramInt1, getRowCount() - 1);
/* 378 */     this.dataMatrix.copyRow(paramInt2, paramInt1);
/* 379 */     this.dataMatrix.copyRow(getRowCount() - 1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trimTableRows(int paramInt) {
/* 390 */     int i = getRowCount();
/* 391 */     int j = getColumnCount();
/* 392 */     for (int k = i - 1; k >= i - paramInt; k--) {
/*     */       
/* 394 */       for (byte b = 0; b < j; b++)
/* 395 */       { if (getValueAt(k, b) != null)
/* 396 */           return;  }  removeRow(k);
/*     */     } 
/*     */   }
/*     */   class DataChangeListener implements TableModelListener { DataChangeListener(DataModel this$0) {
/* 400 */       this.this$0 = this$0;
/*     */     } private final DataModel this$0;
/*     */     public void tableChanged(TableModelEvent param1TableModelEvent) {
/* 403 */       this.this$0.changed = true;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/DataModel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */