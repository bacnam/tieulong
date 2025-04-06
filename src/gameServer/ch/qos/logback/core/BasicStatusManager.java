/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.helpers.CyclicBuffer;
/*     */ import ch.qos.logback.core.spi.LogbackLock;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusListener;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicStatusManager
/*     */   implements StatusManager
/*     */ {
/*     */   public static final int MAX_HEADER_COUNT = 150;
/*     */   public static final int TAIL_SIZE = 150;
/*  31 */   int count = 0;
/*     */ 
/*     */   
/*  34 */   protected final List<Status> statusList = new ArrayList<Status>();
/*  35 */   protected final CyclicBuffer<Status> tailBuffer = new CyclicBuffer(150);
/*     */   
/*  37 */   protected final LogbackLock statusListLock = new LogbackLock();
/*     */   
/*  39 */   int level = 0;
/*     */ 
/*     */   
/*  42 */   protected final List<StatusListener> statusListenerList = new ArrayList<StatusListener>();
/*  43 */   protected final LogbackLock statusListenerListLock = new LogbackLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Status newStatus) {
/*  60 */     fireStatusAddEvent(newStatus);
/*     */     
/*  62 */     this.count++;
/*  63 */     if (newStatus.getLevel() > this.level) {
/*  64 */       this.level = newStatus.getLevel();
/*     */     }
/*     */     
/*  67 */     synchronized (this.statusListLock) {
/*  68 */       if (this.statusList.size() < 150) {
/*  69 */         this.statusList.add(newStatus);
/*     */       } else {
/*  71 */         this.tailBuffer.add(newStatus);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Status> getCopyOfStatusList() {
/*  78 */     synchronized (this.statusListLock) {
/*  79 */       List<Status> tList = new ArrayList<Status>(this.statusList);
/*  80 */       tList.addAll(this.tailBuffer.asList());
/*  81 */       return tList;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fireStatusAddEvent(Status status) {
/*  86 */     synchronized (this.statusListenerListLock) {
/*  87 */       for (StatusListener sl : this.statusListenerList) {
/*  88 */         sl.addStatusEvent(status);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clear() {
/*  94 */     synchronized (this.statusListLock) {
/*  95 */       this.count = 0;
/*  96 */       this.statusList.clear();
/*  97 */       this.tailBuffer.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 102 */     return this.level;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 106 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(StatusListener listener) {
/* 114 */     synchronized (this.statusListenerListLock) {
/* 115 */       if (listener instanceof ch.qos.logback.core.status.OnConsoleStatusListener) {
/* 116 */         boolean alreadyPresent = checkForPresence(this.statusListenerList, listener.getClass());
/* 117 */         if (alreadyPresent)
/*     */           return; 
/*     */       } 
/* 120 */       this.statusListenerList.add(listener);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkForPresence(List<StatusListener> statusListenerList, Class<?> aClass) {
/* 125 */     for (StatusListener e : statusListenerList) {
/* 126 */       if (e.getClass() == aClass)
/* 127 */         return true; 
/*     */     } 
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(StatusListener listener) {
/* 134 */     synchronized (this.statusListenerListLock) {
/* 135 */       this.statusListenerList.remove(listener);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<StatusListener> getCopyOfStatusListenerList() {
/* 140 */     synchronized (this.statusListenerListLock) {
/* 141 */       return new ArrayList<StatusListener>(this.statusListenerList);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/BasicStatusManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */