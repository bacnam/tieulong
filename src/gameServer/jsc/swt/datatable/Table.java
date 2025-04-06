/*     */ package jsc.swt.datatable;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.text.NumberFormat;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
/*     */ import jsc.swt.text.SigFigFormat;
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
/*     */ public class Table
/*     */   extends JTable
/*     */ {
/*     */   AbstractTableModel tableModel;
/*     */   SigFigFormat realFormat;
/*     */   NumberFormat integerFormat;
/*  33 */   public static DataClipboard copiedData = new DataClipboard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Table(AbstractTableModel paramAbstractTableModel) {
/*  42 */     super(paramAbstractTableModel);
/*     */     
/*  44 */     this.tableModel = paramAbstractTableModel;
/*  45 */     createDefaultColumnsFromModel();
/*     */     
/*  47 */     createDefaultRenderers();
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
/*  62 */     this.realFormat = new SigFigFormat(6);
/*     */     
/*  64 */     RealRenderer realRenderer = new RealRenderer(this);
/*  65 */     setDefaultRenderer(Double.class, realRenderer);
/*     */ 
/*     */ 
/*     */     
/*  69 */     this.integerFormat = NumberFormat.getNumberInstance();
/*  70 */     IntegerRenderer integerRenderer = new IntegerRenderer(this);
/*  71 */     setDefaultRenderer(Integer.class, integerRenderer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     setIntercellSpacing(new Dimension(0, 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copy() {
/*  86 */     copyToSystemClipboard();
/*  87 */     if (getCellSelectionEnabled()) {
/*  88 */       copyCells();
/*  89 */     } else if (getColumnSelectionAllowed()) {
/*  90 */       copyColumns();
/*  91 */     } else if (getRowSelectionAllowed()) {
/*  92 */       copyRows();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyCells() {
/*  99 */     int i = getSelectedColumnCount();
/* 100 */     int j = getSelectedRowCount();
/*     */     
/* 102 */     if (i < 1 && j < 1)
/* 103 */       return;  copiedData = new DataClipboard(3, j, i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     int[] arrayOfInt1 = getSelectedColumns();
/* 111 */     int[] arrayOfInt2 = getSelectedRows();
/* 112 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 114 */       int k = arrayOfInt1[b];
/* 115 */       copiedData.setColumnClass(b, getColumnClass(k));
/* 116 */       for (byte b1 = 0; b1 < j; b1++) {
/*     */         
/* 118 */         int m = arrayOfInt2[b1];
/* 119 */         copiedData.setValueAt(getValueAt(m, k), b1, b);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyColumns() {
/* 127 */     int i = getRowCount();
/* 128 */     int j = getSelectedColumnCount();
/* 129 */     if (j < 1)
/* 130 */       return;  copiedData = new DataClipboard(2, i, j);
/*     */     
/* 132 */     int[] arrayOfInt = getSelectedColumns();
/* 133 */     for (byte b = 0; b < j; b++) {
/*     */       
/* 135 */       int k = arrayOfInt[b];
/* 136 */       copiedData.setColumnClass(b, getColumnClass(k));
/* 137 */       copiedData.setColumnName(b, getColumnName(k));
/* 138 */       for (byte b1 = 0; b1 < i; b1++) {
/* 139 */         copiedData.setValueAt(getValueAt(b1, k), b1, b);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyRows() {
/* 146 */     int i = getColumnCount();
/* 147 */     int j = getSelectedRowCount();
/* 148 */     if (j < 1)
/* 149 */       return;  copiedData = new DataClipboard(1, j, i);
/*     */     
/* 151 */     int[] arrayOfInt = getSelectedRows();
/* 152 */     for (byte b = 0; b < j; b++) {
/*     */       
/* 154 */       int k = arrayOfInt[b];
/* 155 */       for (byte b1 = 0; b1 < i; b1++) {
/* 156 */         copiedData.setValueAt(getValueAt(k, b1), b, b1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyToSystemClipboard() {
/* 167 */     StringBuffer stringBuffer = getDataAsStringBuffer(false, false, "\t");
/* 168 */     StringSelection stringSelection = new StringSelection(stringBuffer.toString());
/* 169 */     getToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
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
/*     */   public StringBuffer getDataAsStringBuffer(boolean paramBoolean1, boolean paramBoolean2, String paramString) {
/* 188 */     StringBuffer stringBuffer = new StringBuffer();
/*     */ 
/*     */     
/* 191 */     int i = getColumnCount();
/*     */     
/* 193 */     if (paramBoolean2) {
/*     */       
/* 195 */       for (byte b1 = 0; b1 < i; b1++) {
/* 196 */         if (paramBoolean1 || isColumnSelected(b1))
/* 197 */           stringBuffer.append(getColumnName(b1) + paramString); 
/*     */       } 
/* 199 */       stringBuffer.append("\n");
/*     */     } 
/*     */     
/* 202 */     for (byte b = 0; b < getRowCount(); b++) {
/*     */       
/* 204 */       boolean bool = false;
/* 205 */       for (byte b1 = 0; b1 < i; b1++) {
/*     */         
/* 207 */         if (paramBoolean1 || isCellSelected(b, b1)) {
/*     */ 
/*     */ 
/*     */           
/* 211 */           bool = true;
/* 212 */           stringBuffer.append(getValueAt(b, b1) + paramString);
/*     */         } 
/*     */       } 
/* 215 */       if (bool) stringBuffer.append("\n"); 
/*     */     } 
/* 217 */     return stringBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSignificantDigits() {
/* 228 */     return this.realFormat.getSignificantDigits();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SigFigFormat getRealFormat() {
/* 238 */     return this.realFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean processKeyBinding(KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean) {
/* 245 */     if (paramKeyStroke.equals(KeyStroke.getKeyStroke(67, 2, false)) || paramKeyStroke.equals(KeyStroke.getKeyStroke(88, 2, false)) || paramKeyStroke.equals(KeyStroke.getKeyStroke(86, 2, false)))
/*     */     {
/*     */ 
/*     */       
/* 249 */       return false;
/*     */     }
/*     */     
/* 252 */     return super.processKeyBinding(paramKeyStroke, paramKeyEvent, paramInt, paramBoolean);
/*     */   }
/*     */   
/*     */   public void resetColumnOrder() {
/* 256 */     this.tableModel.fireTableStructureChanged();
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
/*     */   public void setSignificantDigits(int paramInt) {
/* 293 */     this.realFormat.setSignificantDigits(paramInt);
/* 294 */     this.tableModel.fireTableDataChanged();
/*     */   }
/*     */   class IntegerRenderer extends DefaultTableCellRenderer { IntegerRenderer(Table this$0) {
/* 297 */       this.this$0 = this$0;
/*     */     }
/*     */     private final Table this$0;
/*     */     public Component getTableCellRendererComponent(JTable param1JTable, Object param1Object, boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2) {
/*     */       String str;
/* 302 */       setHorizontalAlignment(4);
/*     */ 
/*     */       
/* 305 */       if (param1Object instanceof Integer) {
/*     */         
/* 307 */         int i = ((Integer)param1Object).intValue();
/* 308 */         str = this.this$0.integerFormat.format(i);
/*     */       } else {
/*     */         
/* 311 */         str = "";
/*     */       } 
/*     */       
/* 314 */       return super.getTableCellRendererComponent(param1JTable, str, param1Boolean1, param1Boolean2, param1Int1, param1Int2);
/*     */     } }
/*     */   class RealRenderer extends DefaultTableCellRenderer { private final Table this$0;
/*     */     RealRenderer(Table this$0) {
/* 318 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void RealRenderer() {}
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable param1JTable, Object param1Object, boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2) {
/*     */       String str;
/* 327 */       setHorizontalAlignment(4);
/*     */ 
/*     */       
/* 330 */       if (param1Object instanceof Double) {
/*     */         
/* 332 */         double d = ((Double)param1Object).doubleValue();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 337 */         str = this.this$0.realFormat.format(d);
/*     */       } else {
/*     */         
/* 340 */         str = "";
/*     */       } 
/*     */       
/* 343 */       return super.getTableCellRendererComponent(param1JTable, str, param1Boolean1, param1Boolean2, param1Int1, param1Int2);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/Table.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */