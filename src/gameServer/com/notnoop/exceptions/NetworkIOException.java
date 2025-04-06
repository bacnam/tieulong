/*    */ package com.notnoop.exceptions;
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
/*    */ public class NetworkIOException
/*    */   extends ApnsException
/*    */ {
/*    */   private static final long serialVersionUID = 3353516625486306533L;
/*    */   
/*    */   public NetworkIOException() {}
/*    */   
/*    */   public NetworkIOException(String message) {
/* 45 */     super(message);
/* 46 */   } public NetworkIOException(IOException cause) { super(cause); } public NetworkIOException(String m, IOException c) {
/* 47 */     super(m, c);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/exceptions/NetworkIOException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */