/*     */ package org.apache.http.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.scheme.SocketFactory;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public final class MultihomePlainSocketFactory
/*     */   implements SocketFactory
/*     */ {
/*  67 */   private static final MultihomePlainSocketFactory DEFAULT_FACTORY = new MultihomePlainSocketFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultihomePlainSocketFactory getSocketFactory() {
/*  74 */     return DEFAULT_FACTORY;
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
/*     */   public Socket createSocket() {
/*  87 */     return new Socket();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException {
/* 109 */     Args.notNull(host, "Target host");
/* 110 */     Args.notNull(params, "HTTP parameters");
/*     */     
/* 112 */     Socket sock = socket;
/* 113 */     if (sock == null) {
/* 114 */       sock = createSocket();
/*     */     }
/*     */     
/* 117 */     if (localAddress != null || localPort > 0) {
/* 118 */       InetSocketAddress isa = new InetSocketAddress(localAddress, (localPort > 0) ? localPort : 0);
/*     */       
/* 120 */       sock.bind(isa);
/*     */     } 
/*     */     
/* 123 */     int timeout = HttpConnectionParams.getConnectionTimeout(params);
/*     */     
/* 125 */     InetAddress[] inetadrs = InetAddress.getAllByName(host);
/* 126 */     List<InetAddress> addresses = new ArrayList<InetAddress>(inetadrs.length);
/* 127 */     addresses.addAll(Arrays.asList(inetadrs));
/* 128 */     Collections.shuffle(addresses);
/*     */     
/* 130 */     IOException lastEx = null;
/* 131 */     for (InetAddress remoteAddress : addresses) {
/*     */       try {
/* 133 */         sock.connect(new InetSocketAddress(remoteAddress, port), timeout);
/*     */         break;
/* 135 */       } catch (SocketTimeoutException ex) {
/* 136 */         throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
/* 137 */       } catch (IOException ex) {
/*     */         
/* 139 */         sock = new Socket();
/*     */         
/* 141 */         lastEx = ex;
/*     */       } 
/*     */     } 
/* 144 */     if (lastEx != null) {
/* 145 */       throw lastEx;
/*     */     }
/* 147 */     return sock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSecure(Socket sock) throws IllegalArgumentException {
/* 165 */     Args.notNull(sock, "Socket");
/*     */ 
/*     */     
/* 168 */     Asserts.check(!sock.isClosed(), "Socket is closed");
/* 169 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/MultihomePlainSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */