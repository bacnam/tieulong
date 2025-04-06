/*     */ package com.notnoop.apns.internal;
/*     */ 
/*     */ import com.notnoop.apns.ApnsNotification;
/*     */ import com.notnoop.apns.ApnsService;
/*     */ import com.notnoop.apns.EnhancedApnsNotification;
/*     */ import com.notnoop.exceptions.NetworkIOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractApnsService
/*     */   implements ApnsService
/*     */ {
/*     */   private ApnsFeedbackConnection feedback;
/*  47 */   private AtomicInteger c = new AtomicInteger();
/*     */   
/*     */   public AbstractApnsService(ApnsFeedbackConnection feedback) {
/*  50 */     this.feedback = feedback;
/*     */   }
/*     */   
/*     */   public EnhancedApnsNotification push(String deviceToken, String payload) throws NetworkIOException {
/*  54 */     EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), 2147483647, deviceToken, payload);
/*     */     
/*  56 */     push((ApnsNotification)notification);
/*  57 */     return notification;
/*     */   }
/*     */   
/*     */   public EnhancedApnsNotification push(String deviceToken, String payload, Date expiry) throws NetworkIOException {
/*  61 */     EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), (int)(expiry.getTime() / 1000L), deviceToken, payload);
/*     */     
/*  63 */     push((ApnsNotification)notification);
/*  64 */     return notification;
/*     */   }
/*     */   
/*     */   public EnhancedApnsNotification push(byte[] deviceToken, byte[] payload) throws NetworkIOException {
/*  68 */     EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), 2147483647, deviceToken, payload);
/*     */     
/*  70 */     push((ApnsNotification)notification);
/*  71 */     return notification;
/*     */   }
/*     */   
/*     */   public EnhancedApnsNotification push(byte[] deviceToken, byte[] payload, int expiry) throws NetworkIOException {
/*  75 */     EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), expiry, deviceToken, payload);
/*     */     
/*  77 */     push((ApnsNotification)notification);
/*  78 */     return notification;
/*     */   }
/*     */   
/*     */   public Collection<EnhancedApnsNotification> push(Collection<String> deviceTokens, String payload) throws NetworkIOException {
/*  82 */     byte[] messageBytes = Utilities.toUTF8Bytes(payload);
/*  83 */     List<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
/*  84 */     for (String deviceToken : deviceTokens) {
/*  85 */       byte[] dtbytes = Utilities.decodeHex(deviceToken);
/*  86 */       EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), 2147483647, dtbytes, messageBytes);
/*     */       
/*  88 */       notifications.add(notification);
/*  89 */       push((ApnsNotification)notification);
/*     */     } 
/*  91 */     return notifications;
/*     */   }
/*     */   
/*     */   public Collection<EnhancedApnsNotification> push(Collection<String> deviceTokens, String payload, Date expiry) throws NetworkIOException {
/*  95 */     byte[] messageBytes = Utilities.toUTF8Bytes(payload);
/*  96 */     List<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
/*  97 */     for (String deviceToken : deviceTokens) {
/*  98 */       byte[] dtbytes = Utilities.decodeHex(deviceToken);
/*  99 */       EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), (int)(expiry.getTime() / 1000L), dtbytes, messageBytes);
/*     */       
/* 101 */       notifications.add(notification);
/* 102 */       push((ApnsNotification)notification);
/*     */     } 
/* 104 */     return notifications;
/*     */   }
/*     */   
/*     */   public Collection<EnhancedApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload) throws NetworkIOException {
/* 108 */     List<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
/* 109 */     for (byte[] deviceToken : deviceTokens) {
/* 110 */       EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), 2147483647, deviceToken, payload);
/*     */       
/* 112 */       notifications.add(notification);
/* 113 */       push((ApnsNotification)notification);
/*     */     } 
/* 115 */     return notifications;
/*     */   }
/*     */   
/*     */   public Collection<EnhancedApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload, int expiry) throws NetworkIOException {
/* 119 */     List<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
/* 120 */     for (byte[] deviceToken : deviceTokens) {
/* 121 */       EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), expiry, deviceToken, payload);
/*     */       
/* 123 */       notifications.add(notification);
/* 124 */       push((ApnsNotification)notification);
/*     */     } 
/* 126 */     return notifications;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Date> getInactiveDevices() throws NetworkIOException {
/* 132 */     return this.feedback.getInactiveDevices();
/*     */   }
/*     */   
/*     */   public abstract void push(ApnsNotification paramApnsNotification) throws NetworkIOException;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/AbstractApnsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */