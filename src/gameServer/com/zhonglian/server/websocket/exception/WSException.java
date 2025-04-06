/*    */ package com.zhonglian.server.websocket.exception;
/*    */ 
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ 
/*    */ public class WSException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 7496451863918773713L;
/*  9 */   private IExceptionCallback _callback = null;
/* 10 */   ErrorCode errorCode = null;
/*    */   
/*    */   public WSException(ErrorCode code, String msg) {
/* 13 */     super(msg);
/* 14 */     this.errorCode = code;
/* 15 */     this._callback = null;
/*    */   }
/*    */   
/*    */   public WSException(ErrorCode code, String msg, Object... args) {
/* 19 */     super(String.format(msg, args));
/* 20 */     this.errorCode = code;
/* 21 */     this._callback = null;
/*    */   }
/*    */   
/*    */   public WSException(ErrorCode code, String msg, IExceptionCallback callback) {
/* 25 */     super(msg);
/* 26 */     this.errorCode = code;
/* 27 */     this._callback = callback;
/*    */   }
/*    */   
/*    */   public WSException(ErrorCode code, String msg, IExceptionCallback callback, Throwable cause) {
/* 31 */     super(msg, cause);
/* 32 */     this._callback = callback;
/*    */   }
/*    */   
/*    */   public void callback() {
/* 36 */     if (this._callback != null) {
/* 37 */       this._callback.callback();
/*    */     }
/*    */   }
/*    */   
/*    */   public ErrorCode getErrorCode() {
/* 42 */     return this.errorCode;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/exception/WSException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */