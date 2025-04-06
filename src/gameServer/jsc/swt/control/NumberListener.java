/*    */ package jsc.swt.control;
/*    */ 
/*    */ import java.awt.TextComponent;
/*    */ import java.awt.event.KeyAdapter;
/*    */ import java.awt.event.KeyEvent;
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
/*    */ public class NumberListener
/*    */   extends KeyAdapter
/*    */ {
/*    */   public boolean dpKey(KeyEvent paramKeyEvent) {
/* 22 */     if (paramKeyEvent.getKeyChar() == '.') {
/*    */       
/* 24 */       TextComponent textComponent = (TextComponent)paramKeyEvent.getComponent();
/* 25 */       String str = textComponent.getText();
/* 26 */       if (str.length() == 0 || str.indexOf('.') < 0) {
/* 27 */         return true;
/*    */       }
/* 29 */       return false;
/*    */     } 
/*    */     
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean minusKey(KeyEvent paramKeyEvent) {
/* 38 */     if (paramKeyEvent.getKeyChar() == '-') {
/*    */       
/* 40 */       TextComponent textComponent = (TextComponent)paramKeyEvent.getComponent();
/* 41 */       String str = textComponent.getText();
/* 42 */       if (str.length() == 0 || (textComponent.getCaretPosition() == 0 && str.charAt(0) != '-')) {
/* 43 */         return true;
/*    */       }
/* 45 */       return false;
/*    */     } 
/*    */     
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean numberKey(KeyEvent paramKeyEvent) {
/* 56 */     char c = paramKeyEvent.getKeyChar();
/* 57 */     int i = paramKeyEvent.getKeyCode();
/* 58 */     if (Character.isDigit(c) || c == '\b' || i == 8 || i == 127 || i == 35 || i == 10 || i == 27 || i == 36 || i == 155 || i == 37 || i == 39 || i == 9)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 64 */       return true;
/*    */     }
/* 66 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/NumberListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */