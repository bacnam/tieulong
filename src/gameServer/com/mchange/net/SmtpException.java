/*    */ package com.mchange.net;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SmtpException
/*    */   extends ProtocolException
/*    */ {
/*    */   int resp_num;
/*    */   
/*    */   public SmtpException() {}
/*    */   
/*    */   public SmtpException(String paramString) {
/* 46 */     super(paramString);
/*    */   }
/*    */   
/*    */   public SmtpException(int paramInt, String paramString) {
/* 50 */     this(paramString);
/* 51 */     this.resp_num = paramInt;
/*    */   }
/*    */   
/*    */   public int getSmtpResponseNumber() {
/* 55 */     return this.resp_num;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/net/SmtpException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */