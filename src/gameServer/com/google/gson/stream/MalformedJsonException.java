/*    */ package com.google.gson.stream;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public final class MalformedJsonException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MalformedJsonException(String msg) {
/* 29 */     super(msg);
/*    */   }
/*    */   
/*    */   public MalformedJsonException(String msg, Throwable throwable) {
/* 33 */     super(msg);
/*    */ 
/*    */     
/* 36 */     initCause(throwable);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MalformedJsonException(Throwable throwable) {
/* 42 */     initCause(throwable);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/gson/stream/MalformedJsonException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */