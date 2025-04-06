/*    */ package org.apache.mina.filter.codec.demux;
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
/*    */ 
/*    */ 
/*    */ public class MessageDecoderResult
/*    */ {
/* 35 */   public static final MessageDecoderResult OK = new MessageDecoderResult("OK");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static final MessageDecoderResult NEED_DATA = new MessageDecoderResult("NEED_DATA");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public static final MessageDecoderResult NOT_OK = new MessageDecoderResult("NOT_OK");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private MessageDecoderResult(String name) {
/* 54 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/demux/MessageDecoderResult.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */