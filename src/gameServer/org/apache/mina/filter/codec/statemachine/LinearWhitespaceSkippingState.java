/*    */ package org.apache.mina.filter.codec.statemachine;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LinearWhitespaceSkippingState
/*    */   extends SkippingState
/*    */ {
/*    */   protected boolean canSkip(byte b) {
/* 34 */     return (b == 32 || b == 9);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/LinearWhitespaceSkippingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */