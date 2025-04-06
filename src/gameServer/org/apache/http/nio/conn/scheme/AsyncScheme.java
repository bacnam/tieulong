/*     */ package org.apache.http.nio.conn.scheme;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class AsyncScheme
/*     */ {
/*     */   private final String name;
/*     */   private final LayeringStrategy strategy;
/*     */   private final int defaultPort;
/*     */   private String stringRep;
/*     */   
/*     */   public AsyncScheme(String name, int port, LayeringStrategy strategy) {
/*  55 */     Args.notNull(name, "Scheme name");
/*  56 */     if (port <= 0 || port > 65535) {
/*  57 */       throw new IllegalArgumentException("Port is invalid: " + port);
/*     */     }
/*  59 */     this.name = name.toLowerCase(Locale.ENGLISH);
/*  60 */     this.strategy = strategy;
/*  61 */     this.defaultPort = port;
/*     */   }
/*     */   
/*     */   public final int getDefaultPort() {
/*  65 */     return this.defaultPort;
/*     */   }
/*     */   
/*     */   public final LayeringStrategy getLayeringStrategy() {
/*  69 */     return this.strategy;
/*     */   }
/*     */   
/*     */   public final String getName() {
/*  73 */     return this.name;
/*     */   }
/*     */   
/*     */   public final int resolvePort(int port) {
/*  77 */     return (port <= 0) ? this.defaultPort : port;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/*  82 */     if (this.stringRep == null) {
/*  83 */       StringBuilder buffer = new StringBuilder();
/*  84 */       buffer.append(this.name);
/*  85 */       buffer.append(':');
/*  86 */       buffer.append(Integer.toString(this.defaultPort));
/*  87 */       this.stringRep = buffer.toString();
/*     */     } 
/*  89 */     return this.stringRep;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/*  94 */     if (this == obj) {
/*  95 */       return true;
/*     */     }
/*  97 */     if (obj instanceof AsyncScheme) {
/*  98 */       AsyncScheme that = (AsyncScheme)obj;
/*  99 */       return (this.name.equals(that.name) && this.defaultPort == that.defaultPort && this.strategy.equals(that.strategy));
/*     */     } 
/*     */ 
/*     */     
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 109 */     int hash = 17;
/* 110 */     hash = LangUtils.hashCode(hash, this.defaultPort);
/* 111 */     hash = LangUtils.hashCode(hash, this.name);
/* 112 */     hash = LangUtils.hashCode(hash, this.strategy);
/* 113 */     return hash;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/scheme/AsyncScheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */