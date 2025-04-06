/*    */ package com.zhonglian.server.websocket.server;
/*    */ 
/*    */ import BaseCommon.CommClass;
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.websocket.BaseSession;
/*    */ import com.zhonglian.server.websocket.IMessageDispatcher;
/*    */ import com.zhonglian.server.websocket.def.MessageType;
/*    */ import com.zhonglian.server.websocket.handler.IBaseHandler;
/*    */ import com.zhonglian.server.websocket.handler.MessageDispatcher;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.handler.requset.RequestHandler;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ServerMessageDispatcher<Session extends ServerSession>
/*    */   implements IMessageDispatcher<Session>
/*    */ {
/* 26 */   protected final Map<MessageType, MessageDispatcher<Session>> _messageDispatcher = new HashMap<>();
/*    */ 
/*    */   
/*    */   public int handleRawMessage(Session session, ByteBuffer stream) {
/* 30 */     MessageHeader header = new MessageHeader();
/* 31 */     header.messageType = MessageType.values()[stream.get()];
/* 32 */     header.srcType = stream.get();
/* 33 */     header.srcId = stream.getLong();
/* 34 */     header.descType = stream.get();
/* 35 */     header.descId = stream.getLong();
/*    */     
/* 37 */     MessageDispatcher<Session> dispatcher = this._messageDispatcher.get(header.messageType);
/* 38 */     if (dispatcher == null) {
/* 39 */       CommLog.error("[WSBaseSocketListener] 协议类型错误 类型:{},消息頭部:{}", header.messageType, header);
/* 40 */       return -1;
/*    */     } 
/*    */     
/*    */     try {
/* 44 */       byte[] event = new byte[stream.getShort()];
/* 45 */       stream.get(event);
/* 46 */       header.event = (new String(event, "UTF-8")).toLowerCase();
/*    */       
/* 48 */       header.sequence = stream.getShort();
/* 49 */       header.errcode = stream.getShort();
/*    */       
/* 51 */       byte[] msg = new byte[stream.getShort()];
/* 52 */       stream.get(msg);
/*    */       
/* 54 */       return dispatcher.dispatch((ServerSession)session, header, new String(msg, "UTF-8"));
/* 55 */     } catch (Exception e) {
/* 56 */       CommLog.error("[WSBaseSocketListener] 协议处理协议信息处理时错误:", e);
/*    */       
/* 58 */       return -2;
/*    */     } 
/*    */   }
/*    */   public void putDispatcher(MessageType messageType, MessageDispatcher<Session> dispatcher) {
/* 62 */     this._messageDispatcher.put(messageType, dispatcher);
/*    */   }
/*    */   
/*    */   public void addRequestHandler(RequestHandler handler) {
/* 66 */     ((MessageDispatcher)this._messageDispatcher.get(MessageType.Request)).addHandler((IBaseHandler)handler);
/*    */   }
/*    */   
/*    */   public void registerRequestHandlers(Class<? extends RequestHandler> clazz) {
/* 70 */     List<Class<?>> dealers = CommClass.getAllClassByInterface(clazz, clazz.getPackage().getName());
/*    */     
/* 72 */     int regCnt = 0;
/* 73 */     for (Class<?> cs : dealers) {
/* 74 */       RequestHandler dealer = null;
/*    */       try {
/* 76 */         dealer = CommClass.forName(cs.getName()).newInstance();
/* 77 */       } catch (Exception e) {
/* 78 */         CommLog.error("ServerMessageDispatcher register handler occured error:{}", e.getMessage(), e);
/*    */       } 
/* 80 */       if (dealer == null) {
/*    */         continue;
/*    */       }
/* 83 */       addRequestHandler(dealer);
/* 84 */       regCnt++;
/*    */     } 
/* 86 */     CommLog.info("ServerMessageDispatcher registerHandler count:{}", Integer.valueOf(regCnt));
/*    */   }
/*    */   
/*    */   public abstract void init();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/server/ServerMessageDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */