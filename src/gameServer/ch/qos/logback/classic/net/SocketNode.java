/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
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
/*     */ public class SocketNode
/*     */   implements Runnable
/*     */ {
/*     */   Socket socket;
/*     */   LoggerContext context;
/*     */   ObjectInputStream ois;
/*     */   SocketAddress remoteSocketAddress;
/*     */   Logger logger;
/*     */   boolean closed = false;
/*     */   SimpleSocketServer socketServer;
/*     */   
/*     */   public SocketNode(SimpleSocketServer socketServer, Socket socket, LoggerContext context) {
/*  55 */     this.socketServer = socketServer;
/*  56 */     this.socket = socket;
/*  57 */     this.remoteSocketAddress = socket.getRemoteSocketAddress();
/*  58 */     this.context = context;
/*  59 */     this.logger = context.getLogger(SocketNode.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  71 */       this.ois = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
/*     */     }
/*  73 */     catch (Exception e) {
/*  74 */       this.logger.error("Could not open ObjectInputStream to " + this.socket, e);
/*  75 */       this.closed = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     while (!this.closed) {
/*     */       
/*  84 */       ILoggingEvent event = (ILoggingEvent)this.ois.readObject();
/*     */ 
/*     */       
/*  87 */       Logger remoteLogger = this.context.getLogger(event.getLoggerName());
/*     */       
/*  89 */       if (remoteLogger.isEnabledFor(event.getLevel()))
/*     */       {
/*  91 */         remoteLogger.callAppenders(event);
/*     */       }
/*     */     } 
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
/* 105 */     this.socketServer.socketNodeClosing(this);
/* 106 */     close();
/*     */   }
/*     */   
/*     */   void close() {
/* 110 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 113 */     this.closed = true;
/* 114 */     if (this.ois != null) {
/*     */       try {
/* 116 */         this.ois.close();
/* 117 */       } catch (IOException e) {
/* 118 */         this.logger.warn("Could not close connection.", e);
/*     */       } finally {
/* 120 */         this.ois = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return getClass().getName() + this.remoteSocketAddress.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/net/SocketNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */