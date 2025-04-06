/*     */ package org.apache.http.pool;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class PoolStats
/*     */ {
/*     */   private final int leased;
/*     */   private final int pending;
/*     */   private final int available;
/*     */   private final int max;
/*     */   
/*     */   public PoolStats(int leased, int pending, int free, int max) {
/*  49 */     this.leased = leased;
/*  50 */     this.pending = pending;
/*  51 */     this.available = free;
/*  52 */     this.max = max;
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
/*     */   public int getLeased() {
/*  65 */     return this.leased;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPending() {
/*  75 */     return this.pending;
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
/*     */   public int getAvailable() {
/*  87 */     return this.available;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMax() {
/*  96 */     return this.max;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     StringBuilder buffer = new StringBuilder();
/* 102 */     buffer.append("[leased: ");
/* 103 */     buffer.append(this.leased);
/* 104 */     buffer.append("; pending: ");
/* 105 */     buffer.append(this.pending);
/* 106 */     buffer.append("; available: ");
/* 107 */     buffer.append(this.available);
/* 108 */     buffer.append("; max: ");
/* 109 */     buffer.append(this.max);
/* 110 */     buffer.append("]");
/* 111 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/pool/PoolStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */