/*    */ package com.zhonglian.server.websocket;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import java.net.SocketAddress;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BaseSession
/*    */ {
/*    */   protected IoSession session;
/*    */   protected long sessionID;
/* 27 */   protected String _remoteIP = "";
/*    */   protected int _remotePort;
/* 29 */   private PkgCacheMgr pkgCacheMgr = new PkgCacheMgr();
/*    */   
/*    */   public BaseSession(IoSession session, long sessionID) {
/* 32 */     this.session = session;
/* 33 */     this.sessionID = sessionID;
/* 34 */     CommLog.info("create session id:{} from ip:{}", Long.valueOf(getSessionId()), remoteIP());
/*    */   }
/*    */   
/*    */   public void close() {
/* 38 */     CommLog.info("close session id:{} from ip:{}", Long.valueOf(getSessionId()), remoteIP());
/* 39 */     this.session.close(true);
/*    */   }
/*    */   
/*    */   public long getSessionId() {
/* 43 */     return this.sessionID;
/*    */   }
/*    */   
/*    */   public PkgCacheMgr getPkgCacheMgr() {
/* 47 */     return this.pkgCacheMgr;
/*    */   }
/*    */   
/*    */   public SocketAddress remoteAddress() {
/* 51 */     return this.session.getRemoteAddress();
/*    */   }
/*    */   
/*    */   public SocketAddress localAddress() {
/* 55 */     return this.session.getLocalAddress();
/*    */   }
/*    */   
/*    */   public boolean isConnected() {
/* 59 */     return (this.session.isConnected() && !this.session.isClosing());
/*    */   }
/*    */   
/*    */   public String remoteIP() {
/*    */     try {
/* 64 */       if (this._remoteIP == null || this._remoteIP.isEmpty()) {
/* 65 */         String remoteIp = remoteAddress().toString().replaceFirst("/", "");
/* 66 */         int indexOfFound = remoteIp.indexOf(':');
/* 67 */         remoteIp = remoteIp.substring(0, indexOfFound);
/* 68 */         this._remoteIP = remoteIp;
/*    */       } 
/* 70 */       return this._remoteIP;
/* 71 */     } catch (Throwable e) {
/* 72 */       CommLog.error("{} - remoteIP:() session not connected!", CommTime.getStringMS());
/* 73 */       return "";
/*    */     } 
/*    */   }
/*    */   
/*    */   public int remotePort() {
/*    */     try {
/* 79 */       if (this._remotePort == 0) {
/* 80 */         String remoteAddr = remoteAddress().toString().replaceFirst("/", "");
/* 81 */         int indexOfFound = remoteAddr.indexOf(':');
/* 82 */         if (indexOfFound >= 0) {
/* 83 */           String strPort = remoteAddr.substring(indexOfFound + 1, remoteAddr.length());
/* 84 */           this._remotePort = Integer.parseInt(strPort);
/*    */         } 
/*    */       } 
/* 87 */       return this._remotePort;
/* 88 */     } catch (Throwable e) {
/* 89 */       CommLog.error("remotePort:() session not connected!");
/* 90 */       return 0;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onSent(String message) {
/* 95 */     CommLog.debug("session {} sent {} chars, webaccept:{}", new Object[] { Integer.valueOf(hashCode()), Integer.valueOf(message.length()), message });
/*    */   }
/*    */   
/*    */   public void onReceived(MessageHeader header, String message) {
/* 99 */     CommLog.debug("session {} received ({}), {} bytes", new Object[] { Integer.valueOf(hashCode()), header.event, Integer.valueOf(message.length()) });
/*    */   }
/*    */   
/*    */   public abstract void onCreated();
/*    */   
/*    */   public abstract void onClosed();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/BaseSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */