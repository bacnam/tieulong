/*    */ package org.apache.mina.filter.codec;
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
/*    */ 
/*    */ 
/*    */ public class ProtocolCodecException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 5939878548186330695L;
/*    */   
/*    */   public ProtocolCodecException() {}
/*    */   
/*    */   public ProtocolCodecException(String message) {
/* 43 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ProtocolCodecException(Throwable cause) {
/* 50 */     super(cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ProtocolCodecException(String message, Throwable cause) {
/* 58 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolCodecException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */