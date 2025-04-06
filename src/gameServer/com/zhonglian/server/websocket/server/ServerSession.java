/*     */ package com.zhonglian.server.websocket.server;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.websocket.BaseSession;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.def.MessageType;
/*     */ import com.zhonglian.server.websocket.def.TerminalType;
/*     */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*     */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ServerSession
/*     */   extends BaseSession
/*     */ {
/*  27 */   private Map<String, ServerSocketRequestMgr> requestMgrs = new HashMap<>();
/*     */   
/*  29 */   private int remoteServerID = -1;
/*     */   
/*     */   private TerminalType remoteServerType;
/*     */   private int localServerid;
/*     */   private TerminalType localServerType;
/*     */   
/*     */   public ServerSession(TerminalType localServerType, int localServerid, TerminalType remoteServerType, IoSession session, long sessionID) {
/*  36 */     super(session, sessionID);
/*  37 */     this.localServerType = localServerType;
/*  38 */     this.localServerid = localServerid;
/*  39 */     this.remoteServerType = remoteServerType;
/*     */   }
/*     */   
/*     */   public void setRemoteServerID(int serverID) {
/*  43 */     this.remoteServerID = serverID;
/*     */   }
/*     */   
/*     */   public int getRemoteServerID() {
/*  47 */     return this.remoteServerID;
/*     */   }
/*     */   
/*     */   public TerminalType getRemoteServerType() {
/*  51 */     return this.remoteServerType;
/*     */   }
/*     */   
/*     */   public int getLocalServerid() {
/*  55 */     return this.localServerid;
/*     */   }
/*     */   
/*     */   private ServerSocketRequestMgr getRequestMgr(String opcode) {
/*  59 */     opcode = opcode.toLowerCase();
/*  60 */     ServerSocketRequestMgr requestMgr = this.requestMgrs.get(opcode);
/*  61 */     if (requestMgr == null) {
/*  62 */       synchronized (this.requestMgrs) {
/*  63 */         requestMgr = this.requestMgrs.get(opcode);
/*  64 */         if (requestMgr == null) {
/*  65 */           requestMgr = new ServerSocketRequestMgr(this, opcode);
/*  66 */           this.requestMgrs.put(opcode, requestMgr);
/*     */         } 
/*     */       } 
/*     */     }
/*  70 */     return requestMgr;
/*     */   }
/*     */   
/*     */   public void request(TerminalType desctype, long descid, String opcode, Object protocol, ResponseHandler handler) {
/*  74 */     ServerSocketRequest request = getRequestMgr(opcode).genRequest(handler);
/*  75 */     if (request != null) {
/*  76 */       sendRequest(MessageType.Request, desctype, descid, opcode, request.getSequence(), protocol);
/*     */     } else {
/*  78 */       CommLog.error("[ServerSession]消息请求队列已满，发送失败!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void request(String opcode, Object protocol, ResponseHandler handler) {
/*  83 */     request(this.remoteServerType, this.remoteServerID, opcode, protocol, handler);
/*     */   }
/*     */   
/*     */   public void notifyMessage(TerminalType serverType, long serverid, String opcode, Object protocol) {
/*  87 */     sendRequest(MessageType.Notify, serverType, serverid, opcode, (short)-1, protocol);
/*     */   }
/*     */   
/*     */   public void notifyMessage(String opcode, Object protocol) {
/*  91 */     notifyMessage(this.remoteServerType, this.remoteServerID, opcode, protocol);
/*     */   }
/*     */   
/*     */   public ServerSocketRequest popRequest(String opcode, short sequence) {
/*  95 */     return getRequestMgr(opcode).popRequest(sequence);
/*     */   }
/*     */   
/*     */   public void checkTimeoutRequest() {
/*  99 */     for (ServerSocketRequestMgr requestMgr : new ArrayList(this.requestMgrs.values())) {
/* 100 */       requestMgr.checkTimeoutRequest();
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendRequest(MessageType messageType, TerminalType desctype, long descId, String opcode, short sequence, Object proto) {
/* 105 */     MessageHeader header = new MessageHeader();
/* 106 */     header.messageType = messageType;
/* 107 */     header.srcType = this.localServerType.value();
/* 108 */     header.srcId = this.localServerid;
/* 109 */     header.descType = (byte)desctype.ordinal();
/* 110 */     header.descId = descId;
/* 111 */     header.event = opcode;
/* 112 */     header.sequence = sequence;
/* 113 */     sendPacket(header, proto);
/*     */   }
/*     */   
/*     */   public void sendResponse(MessageHeader srcHeader, String body) {
/* 117 */     sendPacket(srcHeader.genResponseHeader(), body);
/*     */   }
/*     */   
/*     */   public void sendResponse(MessageHeader srcHeader, Object body) {
/* 121 */     sendPacket(srcHeader.genResponseHeader(), body);
/*     */   }
/*     */   
/*     */   public void sendError(MessageHeader srcHeader, ErrorCode errorCode, String message) {
/* 125 */     sendPacket(srcHeader.genResponseHeader(errorCode.value()), message);
/*     */   }
/*     */   
/*     */   public void sendError(MessageHeader srcHeader, short errorCode, String message) {
/* 129 */     sendPacket(srcHeader.genResponseHeader(errorCode), message);
/*     */   }
/*     */   
/*     */   public void forward(MessageHeader header, Object body) {
/* 133 */     sendPacket(header, body);
/*     */   }
/*     */   
/*     */   public void forward(MessageHeader header, String body) {
/* 137 */     sendPacket(header, body);
/*     */   }
/*     */   
/*     */   private void sendPacket(MessageHeader header, Object body) {
/* 141 */     sendPacket(header, (new Gson()).toJson(body));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void sendPacket(MessageHeader header, String body) {
/*     */     try {
/* 147 */       byte[] event = header.event.getBytes("utf-8");
/* 148 */       byte[] bodybytes = body.getBytes("utf-8");
/* 149 */       if (bodybytes.length > 65535) {
/* 150 */         CommLog.error("发送协议过长无法发送. srctype:{}, srcid:{}, event:{}, sequance:{}, body:{}", new Object[] {
/* 151 */               Byte.valueOf(header.srcType), Long.valueOf(header.srcId), header.event, Short.valueOf(header.sequence), body
/*     */             });
/*     */         
/*     */         return;
/*     */       } 
/* 156 */       int length = 21 + event.length + 2 + 2 + 2 + bodybytes.length;
/* 157 */       ByteBuffer buff = ByteBuffer.allocate(length);
/* 158 */       buff.put((byte)header.messageType.ordinal());
/* 159 */       buff.put(header.srcType);
/* 160 */       buff.putLong(header.srcId);
/* 161 */       buff.put(header.descType);
/* 162 */       buff.putLong(header.descId);
/*     */       
/* 164 */       buff.putShort((short)event.length);
/* 165 */       buff.put(event);
/* 166 */       buff.putShort(header.sequence);
/* 167 */       buff.putShort(header.errcode);
/* 168 */       buff.putShort((short)bodybytes.length);
/* 169 */       buff.put(bodybytes);
/* 170 */       buff.flip();
/* 171 */       this.session.write(buff);
/* 172 */       onSendPacket(header, body);
/* 173 */     } catch (Exception e) {
/* 174 */       CommLog.error("发送协议发送错误. srctype:{}, srcid:{}, event:{}, sequance:{}, body:{}", new Object[] {
/* 175 */             Byte.valueOf(header.srcType), Long.valueOf(header.srcId), header.event, Short.valueOf(header.sequence), body, 
/* 176 */             e
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onSendPacket(MessageHeader header, String body) {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/server/ServerSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */