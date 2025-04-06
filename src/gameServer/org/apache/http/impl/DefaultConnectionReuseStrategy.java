/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.TokenIterator;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicTokenIterator;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class DefaultConnectionReuseStrategy
/*     */   implements ConnectionReuseStrategy
/*     */ {
/*  66 */   public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keepAlive(HttpResponse response, HttpContext context) {
/*  76 */     Args.notNull(response, "HTTP response");
/*  77 */     Args.notNull(context, "HTTP context");
/*     */ 
/*     */ 
/*     */     
/*  81 */     ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
/*  82 */     Header teh = response.getFirstHeader("Transfer-Encoding");
/*  83 */     if (teh != null) {
/*  84 */       if (!"chunked".equalsIgnoreCase(teh.getValue())) {
/*  85 */         return false;
/*     */       }
/*     */     }
/*  88 */     else if (canResponseHaveBody(response)) {
/*  89 */       Header[] clhs = response.getHeaders("Content-Length");
/*     */       
/*  91 */       if (clhs.length == 1) {
/*  92 */         Header clh = clhs[0];
/*     */         try {
/*  94 */           int contentLen = Integer.parseInt(clh.getValue());
/*  95 */           if (contentLen < 0) {
/*  96 */             return false;
/*     */           }
/*  98 */         } catch (NumberFormatException ex) {
/*  99 */           return false;
/*     */         } 
/*     */       } else {
/* 102 */         return false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     HeaderIterator hit = response.headerIterator("Connection");
/* 111 */     if (!hit.hasNext()) {
/* 112 */       hit = response.headerIterator("Proxy-Connection");
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
/* 138 */     if (hit.hasNext()) {
/*     */       try {
/* 140 */         TokenIterator ti = createTokenIterator(hit);
/* 141 */         boolean keepalive = false;
/* 142 */         while (ti.hasNext()) {
/* 143 */           String token = ti.nextToken();
/* 144 */           if ("Close".equalsIgnoreCase(token))
/* 145 */             return false; 
/* 146 */           if ("Keep-Alive".equalsIgnoreCase(token))
/*     */           {
/* 148 */             keepalive = true;
/*     */           }
/*     */         } 
/* 151 */         if (keepalive)
/*     */         {
/* 153 */           return true;
/*     */         
/*     */         }
/*     */       }
/* 157 */       catch (ParseException px) {
/*     */ 
/*     */         
/* 160 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 165 */     return !ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0);
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
/*     */   protected TokenIterator createTokenIterator(HeaderIterator hit) {
/* 179 */     return (TokenIterator)new BasicTokenIterator(hit);
/*     */   }
/*     */   
/*     */   private boolean canResponseHaveBody(HttpResponse response) {
/* 183 */     int status = response.getStatusLine().getStatusCode();
/* 184 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/DefaultConnectionReuseStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */