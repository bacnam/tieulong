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
/*    */ 
/*    */ 
/*    */ public class JPosRealListener
/*    */   extends JNumberListener
/*    */ {
/*    */   public void keyTyped(KeyEvent paramKeyEvent) {
/* 22 */     if (dpKey(paramKeyEvent))
/* 23 */       return;  if (numberKey(paramKeyEvent)) {
/*    */       return;
/*    */     }
/* 26 */     paramKeyEvent.consume();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/JPosRealListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */