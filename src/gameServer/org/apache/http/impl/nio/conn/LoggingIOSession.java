/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.SessionBufferStatus;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LoggingIOSession
/*     */   implements IOSession
/*     */ {
/*     */   private final IOSession session;
/*     */   private final ByteChannel channel;
/*     */   private final String id;
/*     */   private final Log log;
/*     */   private final Wire wirelog;
/*     */   
/*     */   public LoggingIOSession(IOSession session, String id, Log log, Log wirelog) {
/*  50 */     this.session = session;
/*  51 */     this.channel = new LoggingByteChannel();
/*  52 */     this.id = id;
/*  53 */     this.log = log;
/*  54 */     this.wirelog = new Wire(wirelog, this.id);
/*     */   }
/*     */   
/*     */   public ByteChannel channel() {
/*  58 */     return this.channel;
/*     */   }
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/*  62 */     return this.session.getLocalAddress();
/*     */   }
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/*  66 */     return this.session.getRemoteAddress();
/*     */   }
/*     */   
/*     */   public int getEventMask() {
/*  70 */     return this.session.getEventMask();
/*     */   }
/*     */   
/*     */   private static String formatOps(int ops) {
/*  74 */     StringBuilder buffer = new StringBuilder(6);
/*  75 */     buffer.append('[');
/*  76 */     if ((ops & 0x1) > 0) {
/*  77 */       buffer.append('r');
/*     */     }
/*  79 */     if ((ops & 0x4) > 0) {
/*  80 */       buffer.append('w');
/*     */     }
/*  82 */     if ((ops & 0x10) > 0) {
/*  83 */       buffer.append('a');
/*     */     }
/*  85 */     if ((ops & 0x8) > 0) {
/*  86 */       buffer.append('c');
/*     */     }
/*  88 */     buffer.append(']');
/*  89 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public void setEventMask(int ops) {
/*  93 */     this.session.setEventMask(ops);
/*  94 */     if (this.log.isDebugEnabled()) {
/*  95 */       this.log.debug(this.id + " " + this.session + ": Event mask set " + formatOps(ops));
/*     */     }
/*     */   }
/*     */   
/*     */   public void setEvent(int op) {
/* 100 */     this.session.setEvent(op);
/* 101 */     if (this.log.isDebugEnabled()) {
/* 102 */       this.log.debug(this.id + " " + this.session + ": Event set " + formatOps(op));
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearEvent(int op) {
/* 107 */     this.session.clearEvent(op);
/* 108 */     if (this.log.isDebugEnabled()) {
/* 109 */       this.log.debug(this.id + " " + this.session + ": Event cleared " + formatOps(op));
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() {
/* 114 */     if (this.log.isDebugEnabled()) {
/* 115 */       this.log.debug(this.id + " " + this.session + ": Close");
/*     */     }
/* 117 */     this.session.close();
/*     */   }
/*     */   
/*     */   public int getStatus() {
/* 121 */     return this.session.getStatus();
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 125 */     return this.session.isClosed();
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 129 */     if (this.log.isDebugEnabled()) {
/* 130 */       this.log.debug(this.id + " " + this.session + ": Shutdown");
/*     */     }
/* 132 */     this.session.shutdown();
/*     */   }
/*     */   
/*     */   public int getSocketTimeout() {
/* 136 */     return this.session.getSocketTimeout();
/*     */   }
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 140 */     if (this.log.isDebugEnabled()) {
/* 141 */       this.log.debug(this.id + " " + this.session + ": Set timeout " + timeout);
/*     */     }
/* 143 */     this.session.setSocketTimeout(timeout);
/*     */   }
/*     */   
/*     */   public void setBufferStatus(SessionBufferStatus status) {
/* 147 */     this.session.setBufferStatus(status);
/*     */   }
/*     */   
/*     */   public boolean hasBufferedInput() {
/* 151 */     return this.session.hasBufferedInput();
/*     */   }
/*     */   
/*     */   public boolean hasBufferedOutput() {
/* 155 */     return this.session.hasBufferedOutput();
/*     */   }
/*     */   
/*     */   public Object getAttribute(String name) {
/* 159 */     return this.session.getAttribute(name);
/*     */   }
/*     */   
/*     */   public void setAttribute(String name, Object obj) {
/* 163 */     if (this.log.isDebugEnabled()) {
/* 164 */       this.log.debug(this.id + " " + this.session + ": Set attribute " + name);
/*     */     }
/* 166 */     this.session.setAttribute(name, obj);
/*     */   }
/*     */   
/*     */   public Object removeAttribute(String name) {
/* 170 */     if (this.log.isDebugEnabled()) {
/* 171 */       this.log.debug(this.id + " " + this.session + ": Remove attribute " + name);
/*     */     }
/* 173 */     return this.session.removeAttribute(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 178 */     return this.id + " " + this.session.toString();
/*     */   }
/*     */   
/*     */   class LoggingByteChannel
/*     */     implements ByteChannel {
/*     */     public int read(ByteBuffer dst) throws IOException {
/* 184 */       int bytesRead = LoggingIOSession.this.session.channel().read(dst);
/* 185 */       if (LoggingIOSession.this.log.isDebugEnabled()) {
/* 186 */         LoggingIOSession.this.log.debug(LoggingIOSession.this.id + " " + LoggingIOSession.this.session + ": " + bytesRead + " bytes read");
/*     */       }
/* 188 */       if (bytesRead > 0 && LoggingIOSession.this.wirelog.isEnabled()) {
/* 189 */         ByteBuffer b = dst.duplicate();
/* 190 */         int p = b.position();
/* 191 */         b.limit(p);
/* 192 */         b.position(p - bytesRead);
/* 193 */         LoggingIOSession.this.wirelog.input(b);
/*     */       } 
/* 195 */       return bytesRead;
/*     */     }
/*     */     
/*     */     public int write(ByteBuffer src) throws IOException {
/* 199 */       int byteWritten = LoggingIOSession.this.session.channel().write(src);
/* 200 */       if (LoggingIOSession.this.log.isDebugEnabled()) {
/* 201 */         LoggingIOSession.this.log.debug(LoggingIOSession.this.id + " " + LoggingIOSession.this.session + ": " + byteWritten + " bytes written");
/*     */       }
/* 203 */       if (byteWritten > 0 && LoggingIOSession.this.wirelog.isEnabled()) {
/* 204 */         ByteBuffer b = src.duplicate();
/* 205 */         int p = b.position();
/* 206 */         b.limit(p);
/* 207 */         b.position(p - byteWritten);
/* 208 */         LoggingIOSession.this.wirelog.output(b);
/*     */       } 
/* 210 */       return byteWritten;
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 214 */       if (LoggingIOSession.this.log.isDebugEnabled()) {
/* 215 */         LoggingIOSession.this.log.debug(LoggingIOSession.this.id + " " + LoggingIOSession.this.session + ": Channel close");
/*     */       }
/* 217 */       LoggingIOSession.this.session.channel().close();
/*     */     }
/*     */     
/*     */     public boolean isOpen() {
/* 221 */       return LoggingIOSession.this.session.channel().isOpen();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/LoggingIOSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */