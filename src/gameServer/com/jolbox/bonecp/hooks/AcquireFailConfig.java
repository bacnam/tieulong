/*     */ package com.jolbox.bonecp.hooks;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AcquireFailConfig
/*     */ {
/*     */   private long acquireRetryDelayInMs;
/*  32 */   private AtomicInteger acquireRetryAttempts = new AtomicInteger();
/*     */   
/*  34 */   private String logMessage = "";
/*     */ 
/*     */ 
/*     */   
/*     */   private Object debugHandle;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public long getAcquireRetryDelay() {
/*  44 */     return getAcquireRetryDelayInMs();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getAcquireRetryDelayInMs() {
/*  51 */     return this.acquireRetryDelayInMs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setAcquireRetryDelay(long acquireRetryDelayInMs) {
/*  59 */     setAcquireRetryDelayInMs(acquireRetryDelayInMs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcquireRetryDelayInMs(long acquireRetryDelayInMs) {
/*  66 */     this.acquireRetryDelayInMs = acquireRetryDelayInMs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicInteger getAcquireRetryAttempts() {
/*  73 */     return this.acquireRetryAttempts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcquireRetryAttempts(AtomicInteger acquireRetryAttempts) {
/*  80 */     this.acquireRetryAttempts = acquireRetryAttempts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLogMessage() {
/*  87 */     return this.logMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogMessage(String logMessage) {
/*  94 */     this.logMessage = logMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getDebugHandle() {
/* 101 */     return this.debugHandle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebugHandle(Object debugHandle) {
/* 108 */     this.debugHandle = debugHandle;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/hooks/AcquireFailConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */