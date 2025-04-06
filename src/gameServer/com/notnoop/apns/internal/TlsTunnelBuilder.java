/*     */ package com.notnoop.apns.internal;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import javax.net.ssl.SSLSocketFactory;
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
/*     */ public final class TlsTunnelBuilder
/*     */ {
/*     */   public Socket build(SSLSocketFactory factory, Proxy proxy, String host, int port) throws IOException {
/*  52 */     boolean success = false;
/*  53 */     Socket proxySocket = null;
/*     */     try {
/*  55 */       InetSocketAddress proxyAddress = (InetSocketAddress)proxy.address();
/*  56 */       proxySocket = new Socket(proxyAddress.getAddress(), proxyAddress.getPort());
/*  57 */       makeTunnel(host, port, proxySocket.getOutputStream(), proxySocket.getInputStream());
/*     */ 
/*     */       
/*  60 */       Socket socket = factory.createSocket(proxySocket, host, port, true);
/*  61 */       success = true;
/*  62 */       return socket;
/*     */     } finally {
/*  64 */       if (!success) {
/*  65 */         Utilities.close(proxySocket);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void makeTunnel(String host, int port, OutputStream out, InputStream in) throws IOException {
/*  72 */     String userAgent = "java-apns";
/*  73 */     String connect = String.format("CONNECT %1$s:%2$d HTTP/1.1\r\nHost: %1$s:%2$d\r\nUser-Agent: %3$s\r\nProxy-Connection: Keep-Alive\r\n\r\n", new Object[] { host, Integer.valueOf(port), userAgent });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     out.write(connect.getBytes("UTF-8"));
/*     */ 
/*     */     
/*  82 */     String statusLine = readAsciiUntilCrlf(in);
/*  83 */     if (!statusLine.matches("HTTP\\/1\\.\\d 2\\d\\d .*"))
/*     */     {
/*  85 */       throw new ProtocolException("TLS tunnel failed: " + statusLine);
/*     */     }
/*  87 */     while (readAsciiUntilCrlf(in).length() != 0);
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
/*     */   public static String readAsciiUntilCrlf(InputStream in) throws IOException {
/*  99 */     StringBuilder result = new StringBuilder(80); int c;
/* 100 */     while ((c = in.read()) != -1) {
/* 101 */       if (c == 10) {
/* 102 */         if (result.length() > 0 && result.charAt(result.length() - 1) == '\r') {
/* 103 */           result.deleteCharAt(result.length() - 1);
/*     */         }
/* 105 */         return result.toString();
/*     */       } 
/* 107 */       result.append((char)c);
/*     */     } 
/* 109 */     throw new EOFException("Expected CRLF");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/TlsTunnelBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */