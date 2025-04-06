/*    */ package jsc.swt.control;
/*    */ 
/*    */ import javax.swing.ComboBoxEditor;
/*    */ import javax.swing.JComboBox;
/*    */ import javax.swing.JTextField;
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
/*    */ public class PosIntegerComboBox
/*    */   extends JComboBox
/*    */ {
/*    */   public PosIntegerComboBox(int paramInt1, int paramInt2, int paramInt3) {
/* 33 */     if (paramInt1 < 0)
/* 34 */       throw new IllegalArgumentException("Negative minimum."); 
/* 35 */     for (byte b = 0; b < 1 + paramInt2 - paramInt1; ) { addItem((E)new Integer(paramInt1 + b)); b++; }
/* 36 */      setEditor(new PosIntegerComboBoxEditor(paramInt3));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 46 */     Integer integer = (Integer)getSelectedItem();
/* 47 */     return integer.intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFocusAccelerator(char paramChar) {
/* 52 */     ComboBoxEditor comboBoxEditor = getEditor();
/* 53 */     JTextField jTextField = (JTextField)comboBoxEditor.getEditorComponent();
/* 54 */     jTextField.setFocusAccelerator(paramChar);
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
/*    */   public void setValue(int paramInt) {
/* 67 */     setSelectedItem(new Integer(paramInt));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/PosIntegerComboBox.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */