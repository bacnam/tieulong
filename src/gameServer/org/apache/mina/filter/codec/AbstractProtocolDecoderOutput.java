/*    */ package org.apache.mina.filter.codec;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
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
/*    */ public abstract class AbstractProtocolDecoderOutput
/*    */   implements ProtocolDecoderOutput
/*    */ {
/* 31 */   private final Queue<Object> messageQueue = new LinkedList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Queue<Object> getMessageQueue() {
/* 38 */     return this.messageQueue;
/*    */   }
/*    */   
/*    */   public void write(Object message) {
/* 42 */     if (message == null) {
/* 43 */       throw new IllegalArgumentException("message");
/*    */     }
/*    */     
/* 46 */     this.messageQueue.add(message);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/AbstractProtocolDecoderOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */