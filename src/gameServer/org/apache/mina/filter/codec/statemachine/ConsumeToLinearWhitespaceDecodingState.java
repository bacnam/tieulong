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
/*    */ 
/*    */ public abstract class ConsumeToLinearWhitespaceDecodingState
/*    */   extends ConsumeToDynamicTerminatorDecodingState
/*    */ {
/*    */   protected boolean isTerminator(byte b) {
/* 35 */     return (b == 32 || b == 9);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/ConsumeToLinearWhitespaceDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */