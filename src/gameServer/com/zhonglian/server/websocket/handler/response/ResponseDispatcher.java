/*    */ package com.zhonglian.server.websocket.handler.response;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.def.TerminalType;
/*    */ import com.zhonglian.server.websocket.handler.IBaseHandler;
/*    */ import com.zhonglian.server.websocket.handler.MessageDispatcher;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.server.ServerSession;
/*    */ import com.zhonglian.server.websocket.server.ServerSocketRequest;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public abstract class ResponseDispatcher<Session extends ServerSession>
/*    */   extends MessageDispatcher<Session> {
/*    */   protected final Map<String, ResponseHandler> _responseHandlers;
/*    */   
/*    */   public ResponseDispatcher(TerminalType thisServerType, int thisServerId) {
/* 19 */     super(thisServerType, thisServerId);
/* 20 */     this._responseHandlers = new HashMap<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Session session, MessageHeader header, String body) {
/*    */     try {
/* 28 */       WebSocketResponse response = new WebSocketResponse((ServerSession)session, header);
/* 29 */       ResponseHandler handler = null;
/*    */ 
/*    */       
/* 32 */       ServerSocketRequest request = session.popRequest(header.event, header.sequence);
/* 33 */       if (request != null) {
/* 34 */         handler = request.getResponseHandler();
/*    */       }
/*    */ 
/*    */       
/* 38 */       if (handler == null) {
/* 39 */         handler = this._responseHandlers.get(header.event);
/*    */       }
/*    */       
/* 42 */       if (handler != null) {
/* 43 */         if (header.errcode == ErrorCode.Success.value()) {
/* 44 */           handler.handleResponse(response, body);
/*    */         } else {
/* 46 */           handler.handleError(response, header.errcode, body);
/*    */         }
/*    */       
/*    */       }
/* 50 */       else if (header.errcode == ErrorCode.Success.value()) {
/* 51 */         CommLog.info("[{}]handle success", header.event);
/*    */       } else {
/* 53 */         CommLog.info("[{}]handle failed, errorCode:{} , message:{}", new Object[] { header.event, Short.valueOf(header.errcode), body });
/*    */       }
/*    */     
/* 56 */     } catch (Exception e) {
/* 57 */       CommLog.error("handle [{}] response error, message: {}", new Object[] { header.event, e.getMessage(), e });
/*    */     } 
/*    */   }
/*    */   
/*    */   public void addHandler(IBaseHandler handler) {
/* 62 */     this._responseHandlers.put(handler.getEvent(), (ResponseHandler)handler);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/response/ResponseDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */