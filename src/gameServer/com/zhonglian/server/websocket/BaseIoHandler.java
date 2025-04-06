/*     */ package com.zhonglian.server.websocket;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Calendar;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.mina.core.service.IoHandler;
/*     */ import org.apache.mina.core.service.IoServiceStatistics;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.util.Base64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseIoHandler<Session extends BaseSession>
/*     */   implements IoHandler
/*     */ {
/*  35 */   public static final Object SESSION_ID = new Object();
/*  36 */   private static final AtomicLong _IDFactory = new AtomicLong(1L);
/*  37 */   protected final Map<Long, Session> connections = Maps.newConcurrentHashMap();
/*     */   protected IMessageDispatcher<Session> messageDispatcher;
/*     */   
/*     */   public Session getSession(long sessionId) {
/*  41 */     return this.connections.get(Long.valueOf(sessionId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageDispatcher(IMessageDispatcher<Session> messageDispatcher) {
/*  48 */     this.messageDispatcher = messageDispatcher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageSent(IoSession session, Object message) throws Exception {
/*  53 */     if (!(message instanceof String)) {
/*     */       return;
/*     */     }
/*  56 */     long sessionId = ((Long)session.getAttribute(SESSION_ID)).longValue();
/*  57 */     BaseSession client = (BaseSession)this.connections.get(Long.valueOf(sessionId));
/*  58 */     if (client == null) {
/*     */       return;
/*     */     }
/*  61 */     SyncTaskManager.task(() -> paramBaseSession.onSent((String)paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageReceived(IoSession session, Object message) throws Exception {
/*  66 */     long sessionId = ((Long)session.getAttribute(SESSION_ID)).longValue();
/*  67 */     if (this.messageDispatcher == null) {
/*  68 */       CommLog.error("协议派发器未初始化");
/*     */       return;
/*     */     } 
/*  71 */     BaseSession baseSession = (BaseSession)this.connections.get(Long.valueOf(sessionId));
/*  72 */     if (baseSession == null) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     if (message instanceof String) {
/*  77 */       session.write(getSecWebSocketAccept((String)message));
/*  78 */     } else if (message instanceof ByteBuffer) {
/*  79 */       this.messageDispatcher.handleRawMessage((Session)baseSession, (ByteBuffer)message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getSecWebSocketAccept(String msg) throws UnsupportedEncodingException {
/*  84 */     String secKey = getSecWebSocketKey(msg);
/*     */     
/*  86 */     secKey = String.valueOf(secKey) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */     try {
/*  88 */       MessageDigest md = MessageDigest.getInstance("SHA-1");
/*  89 */       md.update(secKey.getBytes("iso-8859-1"), 0, secKey.length());
/*  90 */       byte[] sha1Hash = md.digest();
/*  91 */       secKey = new String(Base64.encodeBase64(sha1Hash));
/*  92 */     } catch (Exception e) {
/*  93 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  96 */     return "HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: " + 
/*     */ 
/*     */       
/*  99 */       secKey + "\r\n\r\n";
/*     */   }
/*     */   
/*     */   private String getSecWebSocketKey(String req) {
/* 103 */     Pattern p = Pattern.compile("^(Sec-WebSocket-Key:).+", 10);
/* 104 */     Matcher m = p.matcher(req);
/* 105 */     if (m.find()) {
/* 106 */       String foundstring = m.group();
/* 107 */       return foundstring.split(":")[1].trim();
/*     */     } 
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
/* 115 */     StringBuilder s = new StringBuilder();
/* 116 */     s.append(Calendar.getInstance().getTime().toString());
/* 117 */     s.append(":\n session status:\n");
/* 118 */     IoServiceStatistics stat = session.getService().getStatistics();
/* 119 */     s.append("Read Bytes:").append(stat.getReadBytesThroughput()).append("B/S\n");
/* 120 */     s.append("Write Bytes:").append(stat.getWrittenBytesThroughput()).append("B/S\n");
/* 121 */     s.append("Read Message:").append(stat.getReadMessagesThroughput()).append("pkt/S\n");
/* 122 */     s.append("Write Message:").append(stat.getWrittenMessagesThroughput()).append("pkt/S\n");
/* 123 */     CommLog.info(s.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionCreated(IoSession session) throws Exception {
/* 128 */     if (session.getRemoteAddress() == null) {
/* 129 */       CommLog.error("session {} created, but no remote address!", session.getRemoteAddress());
/*     */       return;
/*     */     } 
/* 132 */     long sessionId = _IDFactory.incrementAndGet();
/* 133 */     session.setAttribute(SESSION_ID, Long.valueOf(sessionId));
/* 134 */     Session client = createSession(session, sessionId);
/* 135 */     this.connections.put(Long.valueOf(sessionId), client);
/* 136 */     if (client != null) {
/* 137 */       SyncTaskManager.task(() -> {
/*     */             if (paramBaseSession != null) {
/*     */               paramBaseSession.onCreated();
/*     */             }
/*     */           });
/*     */     }
/* 143 */     CommLog.debug("create session:{}, id:{}!!!", client.getClass().getName(), Long.valueOf(sessionId));
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionOpened(IoSession session) throws Exception {
/* 148 */     CommLog.debug("session {} opened at {}", SESSION_ID, Integer.valueOf(CommTime.nowSecond()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoSession session) throws Exception {
/* 153 */     long sessionId = ((Long)session.getAttribute(SESSION_ID)).longValue();
/* 154 */     BaseSession client = (BaseSession)this.connections.remove(Long.valueOf(sessionId));
/* 155 */     if (client == null) {
/* 156 */       CommLog.error("close session:{}, id:{}!!!", session.getClass().getName(), Long.valueOf(sessionId));
/*     */       return;
/*     */     } 
/* 159 */     CommLog.debug("close client:{}, id:{}!!!", client.getClass().getName(), Long.valueOf(sessionId));
/* 160 */     SyncTaskManager.task(() -> paramBaseSession.onClosed());
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
/* 165 */     session.close(true);
/* 166 */     CommLog.info("client exceptionCaughted closed sessionid:{}", session.getAttribute(SESSION_ID));
/*     */   }
/*     */   
/*     */   public void inputClosed(IoSession session) throws Exception {
/* 170 */     session.close(true);
/* 171 */     CommLog.info("client inputClosed sessionid:{}", session.getAttribute(SESSION_ID));
/*     */   }
/*     */   
/*     */   public abstract Session createSession(IoSession paramIoSession, long paramLong);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/BaseIoHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */