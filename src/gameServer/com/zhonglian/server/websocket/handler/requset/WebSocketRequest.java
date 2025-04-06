/*    */ package com.zhonglian.server.websocket.handler.requset;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.server.ServerSession;
/*    */ 
/*    */ 
/*    */ public class WebSocketRequest
/*    */ {
/*    */   private ServerSession session;
/*    */   private MessageHeader header;
/*    */   
/*    */   public WebSocketRequest(ServerSession session, MessageHeader header) {
/* 15 */     this.session = session;
/* 16 */     this.header = header;
/*    */   }
/*    */   
/*    */   public void response(Object protocol) {
/* 20 */     if (protocol == null) {
/* 21 */       protocol = "{}";
/*    */     }
/* 23 */     this.session.sendResponse(this.header, protocol);
/*    */   }
/*    */   
/*    */   public void response(String protocol) {
/* 27 */     this.session.sendResponse(this.header, protocol);
/*    */   }
/*    */ 
/*    */   
/*    */   public void response() {
/* 32 */     this.session.sendResponse(this.header, "{}");
/*    */   }
/*    */   
/*    */   public void error(short errorcode, String format, Object... params) {
/* 36 */     String message = null;
/* 37 */     if (format == null) {
/* 38 */       message = "null";
/*    */     } else {
/*    */       try {
/* 41 */         message = String.format(format, params);
/* 42 */       } catch (Exception e) {
/* 43 */         CommLog.error("[WebSocketRequest]格式化错误字符串:{},时错误", e);
/*    */       } 
/*    */     } 
/* 46 */     this.session.sendError(this.header, errorcode, message);
/*    */   }
/*    */   
/*    */   public void error(ErrorCode errorcode, String format, Object... params) {
/* 50 */     error(errorcode.value(), format, params);
/*    */   }
/*    */   
/*    */   public ServerSession getSession() {
/* 54 */     return this.session;
/*    */   }
/*    */   
/*    */   public int getRemoteServerID() {
/* 58 */     return this.session.getRemoteServerID();
/*    */   }
/*    */   
/*    */   public MessageHeader getHeader() {
/* 62 */     return this.header;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/requset/WebSocketRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */