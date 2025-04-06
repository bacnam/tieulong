/*    */ package jsc.swt.control;
/*    */ 
/*    */ import java.awt.GridLayout;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
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
/*    */ public class PosIntegerLabelledField
/*    */   extends JPanel
/*    */ {
/*    */   public PosIntegerField field;
/*    */   public JLabel label;
/*    */   
/*    */   public PosIntegerLabelledField(String paramString, int paramInt1, int paramInt2) {
/* 30 */     this(paramString, paramInt1, paramInt2, 10, 2147483647);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PosIntegerLabelledField(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 40 */     setLayout(new GridLayout(1, 2, 5, 5));
/* 41 */     this.label = new JLabel(paramString, 2);
/* 42 */     setOpaque(false);
/* 43 */     this.label.setOpaque(false);
/* 44 */     this.field = new PosIntegerField(paramInt1, paramInt2, paramInt3, paramInt4);
/* 45 */     add(this.label);
/* 46 */     add(this.field);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 54 */     return this.field.getValue();
/*    */   }
/*    */   public void setValue(int paramInt) {
/* 57 */     this.field.setValue(paramInt);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/PosIntegerLabelledField.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */