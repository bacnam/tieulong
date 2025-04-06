/*    */ package jsc.swt.control;
/*    */ 
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
/*    */ public class JRealListener
/*    */   extends JNumberListener
/*    */ {
/*    */   public void keyTyped(KeyEvent paramKeyEvent) {
/* 20 */     if (minusKey(paramKeyEvent))
/* 21 */       return;  if (dpKey(paramKeyEvent))
/* 22 */       return;  if (numberKey(paramKeyEvent)) {
/*    */       return;
/*    */     }
/* 25 */     paramKeyEvent.consume();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/JRealListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */