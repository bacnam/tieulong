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
/*    */ public class PosRealListener
/*    */   extends NumberListener
/*    */ {
/*    */   public void keyTyped(KeyEvent paramKeyEvent) {
/* 20 */     if (dpKey(paramKeyEvent))
/* 21 */       return;  if (numberKey(paramKeyEvent)) {
/*    */       return;
/*    */     }
/* 24 */     paramKeyEvent.consume();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/PosRealListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */