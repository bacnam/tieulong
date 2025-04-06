/*    */ package jsc.swt.control;
/*    */ 
/*    */ import java.awt.event.KeyAdapter;
/*    */ import java.awt.event.KeyEvent;
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
/*    */ public class JNumberListener
/*    */   extends KeyAdapter
/*    */ {
/*    */   boolean dpKey(KeyEvent paramKeyEvent) {
/* 25 */     if (paramKeyEvent.getKeyChar() == '.') {
/*    */       
/* 27 */       JTextField jTextField = (JTextField)paramKeyEvent.getComponent();
/* 28 */       String str = jTextField.getText();
/* 29 */       if (str.length() == 0 || str.indexOf('.') < 0) {
/* 30 */         return true;
/*    */       }
/* 32 */       return false;
/*    */     } 
/*    */     
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean minusKey(KeyEvent paramKeyEvent) {
/* 41 */     if (paramKeyEvent.getKeyChar() == '-') {
/*    */       
/* 43 */       JTextField jTextField = (JTextField)paramKeyEvent.getComponent();
/* 44 */       String str = jTextField.getText();
/* 45 */       if (str.length() == 0 || (jTextField.getCaretPosition() == 0 && str.charAt(0) != '-')) {
/* 46 */         return true;
/*    */       }
/* 48 */       return false;
/*    */     } 
/*    */     
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean numberKey(KeyEvent paramKeyEvent) {
/* 59 */     char c = paramKeyEvent.getKeyChar();
/* 60 */     int i = paramKeyEvent.getKeyCode();
/* 61 */     if (Character.isDigit(c) || c == '\b' || i == 8 || i == 127 || i == 35 || i == 10 || i == 27 || i == 36 || i == 155 || i == 37 || i == 39 || i == 9)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 67 */       return true;
/*    */     }
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/JNumberListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */