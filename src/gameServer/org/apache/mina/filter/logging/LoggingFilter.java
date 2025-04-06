/*     */ package org.apache.mina.filter.logging;
/*     */ 
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoggingFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*     */   private final String name;
/*     */   private final Logger logger;
/*  52 */   private LogLevel exceptionCaughtLevel = LogLevel.WARN;
/*     */ 
/*     */   
/*  55 */   private LogLevel messageSentLevel = LogLevel.INFO;
/*     */ 
/*     */   
/*  58 */   private LogLevel messageReceivedLevel = LogLevel.INFO;
/*     */ 
/*     */   
/*  61 */   private LogLevel sessionCreatedLevel = LogLevel.INFO;
/*     */ 
/*     */   
/*  64 */   private LogLevel sessionOpenedLevel = LogLevel.INFO;
/*     */ 
/*     */   
/*  67 */   private LogLevel sessionIdleLevel = LogLevel.INFO;
/*     */ 
/*     */   
/*  70 */   private LogLevel sessionClosedLevel = LogLevel.INFO;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingFilter() {
/*  76 */     this(LoggingFilter.class.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingFilter(Class<?> clazz) {
/*  85 */     this(clazz.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingFilter(String name) {
/*  94 */     if (name == null) {
/*  95 */       this.name = LoggingFilter.class.getName();
/*     */     } else {
/*  97 */       this.name = name;
/*     */     } 
/*     */     
/* 100 */     this.logger = LoggerFactory.getLogger(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 107 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void log(LogLevel eventLevel, String message, Throwable cause) {
/* 119 */     switch (eventLevel) {
/*     */       case TRACE:
/* 121 */         this.logger.trace(message, cause);
/*     */         return;
/*     */       case DEBUG:
/* 124 */         this.logger.debug(message, cause);
/*     */         return;
/*     */       case INFO:
/* 127 */         this.logger.info(message, cause);
/*     */         return;
/*     */       case WARN:
/* 130 */         this.logger.warn(message, cause);
/*     */         return;
/*     */       case ERROR:
/* 133 */         this.logger.error(message, cause);
/*     */         return;
/*     */     } 
/*     */   }
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
/*     */   private void log(LogLevel eventLevel, String message, Object param) {
/* 149 */     switch (eventLevel) {
/*     */       case TRACE:
/* 151 */         this.logger.trace(message, param);
/*     */         return;
/*     */       case DEBUG:
/* 154 */         this.logger.debug(message, param);
/*     */         return;
/*     */       case INFO:
/* 157 */         this.logger.info(message, param);
/*     */         return;
/*     */       case WARN:
/* 160 */         this.logger.warn(message, param);
/*     */         return;
/*     */       case ERROR:
/* 163 */         this.logger.error(message, param);
/*     */         return;
/*     */     } 
/*     */   }
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
/*     */   private void log(LogLevel eventLevel, String message) {
/* 178 */     switch (eventLevel) {
/*     */       case TRACE:
/* 180 */         this.logger.trace(message);
/*     */         return;
/*     */       case DEBUG:
/* 183 */         this.logger.debug(message);
/*     */         return;
/*     */       case INFO:
/* 186 */         this.logger.info(message);
/*     */         return;
/*     */       case WARN:
/* 189 */         this.logger.warn(message);
/*     */         return;
/*     */       case ERROR:
/* 192 */         this.logger.error(message);
/*     */         return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
/* 201 */     log(this.exceptionCaughtLevel, "EXCEPTION :", cause);
/* 202 */     nextFilter.exceptionCaught(session, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
/* 207 */     log(this.messageReceivedLevel, "RECEIVED: {}", message);
/* 208 */     nextFilter.messageReceived(session, message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 213 */     log(this.messageSentLevel, "SENT: {}", writeRequest.getOriginalRequest().getMessage());
/* 214 */     nextFilter.messageSent(session, writeRequest);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 219 */     log(this.sessionCreatedLevel, "CREATED");
/* 220 */     nextFilter.sessionCreated(session);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 225 */     log(this.sessionOpenedLevel, "OPENED");
/* 226 */     nextFilter.sessionOpened(session);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
/* 231 */     log(this.sessionIdleLevel, "IDLE");
/* 232 */     nextFilter.sessionIdle(session, status);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 237 */     log(this.sessionClosedLevel, "CLOSED");
/* 238 */     nextFilter.sessionClosed(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionCaughtLogLevel(LogLevel level) {
/* 247 */     this.exceptionCaughtLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel getExceptionCaughtLogLevel() {
/* 256 */     return this.exceptionCaughtLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageReceivedLogLevel(LogLevel level) {
/* 265 */     this.messageReceivedLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel getMessageReceivedLogLevel() {
/* 274 */     return this.messageReceivedLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageSentLogLevel(LogLevel level) {
/* 283 */     this.messageSentLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel getMessageSentLogLevel() {
/* 292 */     return this.messageSentLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionCreatedLogLevel(LogLevel level) {
/* 301 */     this.sessionCreatedLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel getSessionCreatedLogLevel() {
/* 310 */     return this.sessionCreatedLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionOpenedLogLevel(LogLevel level) {
/* 319 */     this.sessionOpenedLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel getSessionOpenedLogLevel() {
/* 328 */     return this.sessionOpenedLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionIdleLogLevel(LogLevel level) {
/* 337 */     this.sessionIdleLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel getSessionIdleLogLevel() {
/* 346 */     return this.sessionIdleLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionClosedLogLevel(LogLevel level) {
/* 355 */     this.sessionClosedLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel getSessionClosedLogLevel() {
/* 364 */     return this.sessionClosedLevel;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/logging/LoggingFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */