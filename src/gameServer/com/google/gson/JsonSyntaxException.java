/*    */ package com.google.gson;
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
/*    */ public final class JsonSyntaxException
/*    */   extends JsonParseException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public JsonSyntaxException(String msg) {
/* 30 */     super(msg);
/*    */   }
/*    */   
/*    */   public JsonSyntaxException(String msg, Throwable cause) {
/* 34 */     super(msg, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonSyntaxException(Throwable cause) {
/* 45 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/gson/JsonSyntaxException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */