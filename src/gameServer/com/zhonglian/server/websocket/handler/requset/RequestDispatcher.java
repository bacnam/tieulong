/*    */ package com.zhonglian.server.websocket.handler.requset;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.def.TerminalType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.IBaseHandler;
/*    */ import com.zhonglian.server.websocket.handler.MessageDispatcher;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.server.ServerSession;
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public abstract class RequestDispatcher<Session extends ServerSession>
/*    */   extends MessageDispatcher<Session>
/*    */ {
/*    */   protected final Map<String, RequestHandler> _requestHandlers;
/*    */   
/*    */   public RequestDispatcher(TerminalType thisServerType, int thisServerId) {
/* 21 */     super(thisServerType, thisServerId);
/* 22 */     this._requestHandlers = new HashMap<>();
/*    */   }
/*    */   
/*    */   public RequestDispatcher(TerminalType serverType, int serverId, Map<String, RequestHandler> requestHandlers) {
/* 26 */     super(serverType, serverId);
/* 27 */     this._requestHandlers = requestHandlers;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Session session, MessageHeader header, String data) {
/*    */     try {
/* 33 */       RequestHandler handler = this._requestHandlers.get(header.event);
/* 34 */       if (handler == null) {
/* 35 */         session.sendError(header, ErrorCode.Request_NotFoundHandler, "协议[" + header.event + "]未找到处理器");
/*    */         return;
/*    */       } 
/* 38 */       handler.handleMessage(new WebSocketRequest((ServerSession)session, header), data);
/* 39 */     } catch (WSException e) {
/* 40 */       CommLog.warn("handle [0x{}] request failed, reason: {}", header.event, e.getMessage());
/* 41 */       session.sendError(header, e.getErrorCode(), e.getMessage());
/* 42 */       e.callback();
/* 43 */     } catch (IOException e) {
/* 44 */       CommLog.error("handle [0x{}] request parse message error, detail:{}", new Object[] { header.event, e.getMessage(), e });
/* 45 */       session.sendError(header, ErrorCode.Unknown, "proto trans error");
/* 46 */     } catch (Throwable e) {
/* 47 */       CommLog.error("handle [0x{}] request failed, reason: {}", new Object[] { header.event, e.getMessage(), e });
/* 48 */       session.sendError(header, ErrorCode.Unknown, "internal error");
/*    */     } 
/*    */   }
/*    */   
/*    */   public void addHandler(IBaseHandler handler) {
/* 53 */     if (this._requestHandlers.containsKey(handler.getEvent())) {
/* 54 */       CommLog.error("有Handler重名:", handler.getEvent());
/* 55 */       System.exit(0);
/*    */     } 
/* 57 */     this._requestHandlers.put(handler.getEvent(), (RequestHandler)handler);
/*    */   }
/*    */   
/*    */   public RequestHandler getHandler(int opcode) {
/* 61 */     return this._requestHandlers.get(Integer.valueOf(opcode));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/requset/RequestDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */