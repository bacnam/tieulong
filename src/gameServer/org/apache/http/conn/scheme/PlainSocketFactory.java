/*     */ package org.apache.http.conn.scheme;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class PlainSocketFactory
/*     */   implements SocketFactory, SchemeSocketFactory
/*     */ {
/*     */   private final HostNameResolver nameResolver;
/*     */   
/*     */   public static PlainSocketFactory getSocketFactory() {
/*  62 */     return new PlainSocketFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public PlainSocketFactory(HostNameResolver nameResolver) {
/*  71 */     this.nameResolver = nameResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlainSocketFactory() {
/*  76 */     this.nameResolver = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(HttpParams params) {
/*  87 */     return new Socket();
/*     */   }
/*     */   
/*     */   public Socket createSocket() {
/*  91 */     return new Socket();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, ConnectTimeoutException {
/* 102 */     Args.notNull(remoteAddress, "Remote address");
/* 103 */     Args.notNull(params, "HTTP parameters");
/* 104 */     Socket sock = socket;
/* 105 */     if (sock == null) {
/* 106 */       sock = createSocket();
/*     */     }
/* 108 */     if (localAddress != null) {
/* 109 */       sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
/* 110 */       sock.bind(localAddress);
/*     */     } 
/* 112 */     int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
/* 113 */     int soTimeout = HttpConnectionParams.getSoTimeout(params);
/*     */     
/*     */     try {
/* 116 */       sock.setSoTimeout(soTimeout);
/* 117 */       sock.connect(remoteAddress, connTimeout);
/* 118 */     } catch (SocketTimeoutException ex) {
/* 119 */       throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
/*     */     } 
/* 121 */     return sock;
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
/*     */   public final boolean isSecure(Socket sock) {
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/*     */     InetAddress remoteAddress;
/* 146 */     InetSocketAddress local = null;
/* 147 */     if (localAddress != null || localPort > 0) {
/* 148 */       local = new InetSocketAddress(localAddress, (localPort > 0) ? localPort : 0);
/*     */     }
/*     */     
/* 151 */     if (this.nameResolver != null) {
/* 152 */       remoteAddress = this.nameResolver.resolve(host);
/*     */     } else {
/* 154 */       remoteAddress = InetAddress.getByName(host);
/*     */     } 
/* 156 */     InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
/* 157 */     return connectSocket(socket, remote, local, params);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/scheme/PlainSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */