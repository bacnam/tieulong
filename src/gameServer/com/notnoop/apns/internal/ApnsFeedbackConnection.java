/*     */ package com.notnoop.apns.internal;
/*     */ 
/*     */ import com.notnoop.exceptions.NetworkIOException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.SSLSocketFactory;
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
/*     */ public class ApnsFeedbackConnection
/*     */ {
/*  47 */   private static final Logger logger = LoggerFactory.getLogger(ApnsFeedbackConnection.class); private final SocketFactory factory;
/*     */   private final String host;
/*     */   private final int port;
/*     */   private final Proxy proxy;
/*     */   int DELAY_IN_MS;
/*     */   private static final int RETRIES = 3;
/*     */   
/*     */   public ApnsFeedbackConnection(SocketFactory factory, String host, int port) {
/*  55 */     this(factory, host, port, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsFeedbackConnection(SocketFactory factory, String host, int port, Proxy proxy) {
/*  66 */     this.DELAY_IN_MS = 1000;
/*     */     this.factory = factory;
/*     */     this.host = host;
/*     */     this.port = port;
/*  70 */     this.proxy = proxy; } public Map<String, Date> getInactiveDevices() throws NetworkIOException { int attempts = 0;
/*     */     while (true) {
/*     */       try {
/*  73 */         attempts++;
/*  74 */         Map<String, Date> result = getInactiveDevicesImpl();
/*     */         
/*  76 */         attempts = 0;
/*  77 */         return result;
/*  78 */       } catch (Exception e) {
/*  79 */         logger.warn("Failed to retreive invalid devices", e);
/*  80 */         if (attempts >= 3) {
/*  81 */           logger.error("Couldn't get feedback connection", e);
/*  82 */           Utilities.wrapAndThrowAsRuntimeException(e);
/*     */         } 
/*  84 */         Utilities.sleep(this.DELAY_IN_MS);
/*     */       } 
/*     */     }  }
/*     */ 
/*     */   
/*     */   public Map<String, Date> getInactiveDevicesImpl() throws IOException {
/*  90 */     Socket proxySocket = null;
/*  91 */     Socket socket = null;
/*     */     try {
/*  93 */       if (this.proxy == null) {
/*  94 */         socket = this.factory.createSocket(this.host, this.port);
/*  95 */       } else if (this.proxy.type() == Proxy.Type.HTTP) {
/*  96 */         TlsTunnelBuilder tunnelBuilder = new TlsTunnelBuilder();
/*  97 */         socket = tunnelBuilder.build((SSLSocketFactory)this.factory, this.proxy, this.host, this.port);
/*     */       } else {
/*  99 */         proxySocket = new Socket(this.proxy);
/* 100 */         proxySocket.connect(new InetSocketAddress(this.host, this.port));
/* 101 */         socket = ((SSLSocketFactory)this.factory).createSocket(proxySocket, this.host, this.port, false);
/*     */       } 
/*     */       
/* 104 */       InputStream stream = socket.getInputStream();
/* 105 */       return Utilities.parseFeedbackStream(stream);
/*     */     } finally {
/* 107 */       Utilities.close(socket);
/* 108 */       Utilities.close(proxySocket);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/ApnsFeedbackConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */