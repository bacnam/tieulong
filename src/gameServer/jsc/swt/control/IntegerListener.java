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
/*    */ public class IntegerListener
/*    */   extends NumberListener
/*    */ {
/*    */   public void keyTyped(KeyEvent paramKeyEvent) {
/* 19 */     if (minusKey(paramKeyEvent))
/* 20 */       return;  if (numberKey(paramKeyEvent)) {
/*    */       return;
/*    */     }
/* 23 */     paramKeyEvent.consume();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/IntegerListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */