/*    */ package jsc.swt.control;
/*    */ 
/*    */ import javax.swing.JComboBox;
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
/*    */ public class IntegerComboBox
/*    */   extends JComboBox
/*    */ {
/*    */   public IntegerComboBox(int paramInt1, int paramInt2, int paramInt3) {
/* 32 */     for (byte b = 0; b < 1 + paramInt2 - paramInt1; ) { addItem((E)new Integer(paramInt1 + b)); b++; }
/* 33 */      setEditor(new IntegerComboBoxEditor(paramInt3));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 43 */     Integer integer = (Integer)getSelectedItem();
/* 44 */     return integer.intValue();
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
/* 57 */     setSelectedItem(new Integer(paramInt));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/IntegerComboBox.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */