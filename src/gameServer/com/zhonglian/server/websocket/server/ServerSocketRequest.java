/*    */ package com.zhonglian.server.websocket.server;
/*    */ 
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*    */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*    */ 
/*    */ public class ServerSocketRequest
/*    */ {
/* 10 */   private static long TIME_OUT = 8000L;
/*    */   
/* 12 */   private long beginTime = 0L;
/*    */   private String operation;
/* 14 */   private short sequence = 0;
/* 15 */   private ServerSession session = null;
/*    */   private ResponseHandler handler;
/*    */   
/*    */   public ServerSocketRequest(ServerSession session, String operation, short sequence, ResponseHandler handler) {
/* 19 */     this.session = session;
/* 20 */     this.operation = operation;
/* 21 */     this.sequence = sequence;
/* 22 */     this.handler = handler;
/* 23 */     this.beginTime = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public short getSequence() {
/* 27 */     return this.sequence;
/*    */   }
/*    */   
/*    */   public boolean isTimeout() {
/* 31 */     return (System.currentTimeMillis() > this.beginTime + TIME_OUT);
/*    */   }
/*    */   
/*    */   public ResponseHandler getResponseHandler() {
/* 35 */     return this.handler;
/*    */   }
/*    */   
/*    */   public void expired() {
/* 39 */     if (this.handler != null) {
/* 40 */       MessageHeader header = new MessageHeader();
/* 41 */       header.event = this.operation;
/* 42 */       header.sequence = this.sequence;
/*    */       
/* 44 */       header.descType = 0;
/* 45 */       header.descId = 0L;
/*    */       
/* 47 */       WebSocketResponse response = new WebSocketResponse(this.session, header);
/* 48 */       this.handler.handleError(response, ErrorCode.Request_RequestTimeout.value(), "handle timeout");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/server/ServerSocketRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */