/*    */ package jsc.swt.datatable;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.DefaultCellEditor;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.JTextField;
/*    */ import javax.swing.ListSelectionModel;
/*    */ import javax.swing.event.ListSelectionEvent;
/*    */ import javax.swing.event.ListSelectionListener;
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
/*    */ public class RecodeTable
/*    */   extends JTable
/*    */ {
/*    */   public RecodeTable(RecodeModel paramRecodeModel) {
/* 30 */     super(paramRecodeModel);
/* 31 */     createDefaultColumnsFromModel();
/* 32 */     createDefaultRenderers();
/*    */ 
/*    */     
/* 35 */     DefaultCellEditor defaultCellEditor = new DefaultCellEditor(new JTextField());
/* 36 */     defaultCellEditor.setClickCountToStart(1);
/*    */     
/* 38 */     setDefaultEditor(String.class, defaultCellEditor);
/*    */ 
/*    */ 
/*    */     
/* 42 */     ListSelectionModel listSelectionModel = getSelectionModel();
/* 43 */     listSelectionModel.addListSelectionListener(new SelectionDebugger(this, listSelectionModel));
/*    */ 
/*    */ 
/*    */     
/* 47 */     setIntercellSpacing(new Dimension(0, 0));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   class SelectionDebugger
/*    */     implements ListSelectionListener
/*    */   {
/*    */     ListSelectionModel model;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private final RecodeTable this$0;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public SelectionDebugger(RecodeTable this$0, ListSelectionModel param1ListSelectionModel) {
/* 68 */       this.this$0 = this$0; this.model = param1ListSelectionModel;
/*    */     }
/*    */     public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
/* 71 */       int i = this.model.getMinSelectionIndex();
/* 72 */       int j = this.model.getMaxSelectionIndex();
/*    */ 
/*    */       
/* 75 */       Object object = this.this$0.getValueAt(i, 1);
/* 76 */       for (int k = i; k <= j; ) { this.this$0.setValueAt(object, k, 1); k++; }
/*    */     
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/RecodeTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */