/*    */ package com.zhonglian.server.websocket.handler.requset;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.def.TerminalType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.server.ServerSession;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public abstract class NotifyDispatcher<Session extends ServerSession>
/*    */   extends RequestDispatcher<Session>
/*    */ {
/*    */   public NotifyDispatcher(TerminalType serverType, int serverId, RequestDispatcher<Session> requestDispatcher) {
/* 16 */     super(serverType, serverId, requestDispatcher._requestHandlers);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Session session, MessageHeader header, String data) {
/*    */     try {
/* 22 */       RequestHandler handler = this._requestHandlers.get(header.event);
/* 23 */       if (handler == null) {
/* 24 */         session.sendError(header, ErrorCode.Request_NotFoundHandler, "协议[" + header.event + "]未找到处理器");
/*    */         return;
/*    */       } 
/* 27 */       handler.handleMessage(new WebSocketRequest((ServerSession)session, header), data);
/* 28 */     } catch (WSException e) {
/* 29 */       CommLog.warn("handle [0x{}] notify failed, reason:{}", header.event, e.getMessage());
/* 30 */       e.callback();
/* 31 */     } catch (IOException e) {
/* 32 */       CommLog.error("handle [0x{}] notify parse message error, detail:{}", new Object[] { header.event, e.getMessage(), e });
/* 33 */     } catch (Throwable e) {
/* 34 */       CommLog.error("handle [0x{}] notify failed, reason:{}", new Object[] { header.event, e.getMessage(), e });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/requset/NotifyDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */