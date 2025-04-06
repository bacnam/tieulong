/*    */ package jsc.swt.control;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.awt.event.FocusEvent;
/*    */ import java.awt.event.FocusListener;
/*    */ import javax.swing.ComboBoxEditor;
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
/*    */ class PosIntegerComboBoxEditor
/*    */   implements ComboBoxEditor, FocusListener
/*    */ {
/*    */   protected PosIntegerField editor;
/*    */   
/*    */   public PosIntegerComboBoxEditor(int paramInt) {
/* 34 */     this.editor = new PosIntegerField(0, paramInt);
/*    */     
/* 36 */     this.editor.setBorder(null);
/*    */   }
/*    */   public Component getEditorComponent() {
/* 39 */     return this.editor;
/*    */   }
/*    */   public void setItem(Object paramObject) {
/* 42 */     if (paramObject != null) {
/* 43 */       this.editor.setText(paramObject.toString());
/*    */     } else {
/* 45 */       this.editor.setText("");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getItem() {
/* 53 */     return new Integer(this.editor.getValue());
/*    */   }
/*    */   
/*    */   public void selectAll() {
/* 57 */     this.editor.selectAll();
/* 58 */     this.editor.requestFocus();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void focusGained(FocusEvent paramFocusEvent) {}
/*    */ 
/*    */   
/*    */   public void focusLost(FocusEvent paramFocusEvent) {}
/*    */ 
/*    */   
/*    */   public void addActionListener(ActionListener paramActionListener) {
/* 70 */     this.editor.addActionListener(paramActionListener);
/*    */   }
/*    */   
/*    */   public void removeActionListener(ActionListener paramActionListener) {
/* 74 */     this.editor.removeActionListener(paramActionListener);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/PosIntegerComboBoxEditor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */