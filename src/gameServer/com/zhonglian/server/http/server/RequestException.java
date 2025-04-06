/*    */ package com.zhonglian.server.http.server;
/*    */ 
/*    */ public class RequestException
/*    */   extends Exception {
/*    */   private static final long serialVersionUID = 2420902066665001397L;
/*    */   private int code;
/*    */   
/*    */   public RequestException(int code, String msg, Object... params) {
/*  9 */     super(String.format(msg, params));
/* 10 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 14 */     return this.code;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/RequestException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */