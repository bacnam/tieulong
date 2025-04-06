/*    */ package com.zhonglian.server.websocket.handler.response;
/*    */ 
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.server.ServerSession;
/*    */ 
/*    */ public class WebSocketResponse
/*    */ {
/*    */   private ServerSession session;
/*    */   private MessageHeader header;
/*    */   
/*    */   public WebSocketResponse(ServerSession session, MessageHeader header) {
/* 12 */     this.session = session;
/* 13 */     this.header = header;
/*    */   }
/*    */   
/*    */   public MessageHeader getHeader() {
/* 17 */     return this.header;
/*    */   }
/*    */   
/*    */   public int getRemoteServerID() {
/* 21 */     return this.session.getRemoteServerID();
/*    */   }
/*    */   
/*    */   public ServerSession getSession() {
/* 25 */     return this.session;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/response/WebSocketResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */