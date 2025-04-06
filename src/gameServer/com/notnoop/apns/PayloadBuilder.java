/*     */ package com.notnoop.apns;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.notnoop.apns.internal.Utilities;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PayloadBuilder
/*     */ {
/*  45 */   private static final ObjectMapper mapper = new ObjectMapper();
/*     */   
/*     */   private final Map<String, Object> root;
/*     */   
/*     */   private final Map<String, Object> aps;
/*     */   
/*     */   private final Map<String, Object> customAlert;
/*     */ 
/*     */   
/*     */   PayloadBuilder() {
/*  55 */     this.root = new HashMap<String, Object>();
/*  56 */     this.aps = new HashMap<String, Object>();
/*  57 */     this.customAlert = new HashMap<String, Object>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PayloadBuilder alertBody(String alert) {
/*  68 */     this.customAlert.put("body", alert);
/*  69 */     return this;
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
/*     */   public PayloadBuilder sound(String sound) {
/*  82 */     if (sound != null) {
/*  83 */       this.aps.put("sound", sound);
/*     */     } else {
/*  85 */       this.aps.remove("sound");
/*     */     } 
/*  87 */     return this;
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
/*     */   public PayloadBuilder badge(int badge) {
/* 102 */     this.aps.put("badge", Integer.valueOf(badge));
/* 103 */     return this;
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
/*     */   public PayloadBuilder clearBadge() {
/* 115 */     return badge(0);
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
/*     */   public PayloadBuilder actionKey(String actionKey) {
/* 133 */     this.customAlert.put("action-loc-key", actionKey);
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PayloadBuilder noActionButton() {
/* 145 */     return actionKey(null);
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
/*     */   public PayloadBuilder forNewsstand() {
/* 157 */     this.aps.put("content-available", Integer.valueOf(1));
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PayloadBuilder localizedKey(String key) {
/* 169 */     this.customAlert.put("loc-key", key);
/* 170 */     return this;
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
/*     */   public PayloadBuilder localizedArguments(Collection<String> arguments) {
/* 182 */     this.customAlert.put("loc-args", arguments);
/* 183 */     return this;
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
/*     */   public PayloadBuilder localizedArguments(String... arguments) {
/* 195 */     return localizedArguments(Arrays.asList(arguments));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PayloadBuilder launchImage(String launchImage) {
/* 206 */     this.customAlert.put("launch-image", launchImage);
/* 207 */     return this;
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
/*     */   public PayloadBuilder customField(String key, Object value) {
/* 224 */     this.root.put(key, value);
/* 225 */     return this;
/*     */   }
/*     */   
/*     */   public PayloadBuilder mdm(String s) {
/* 229 */     return customField("mdm", s);
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
/*     */   public PayloadBuilder customFields(Map<String, ? extends Object> values) {
/* 245 */     this.root.putAll(values);
/* 246 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 255 */     return (copy().buildBytes()).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTooLong() {
/* 265 */     return (length() > 256);
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
/*     */   public PayloadBuilder resizeAlertBody(int payloadLength) {
/* 281 */     return resizeAlertBody(payloadLength, "");
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
/*     */   public PayloadBuilder resizeAlertBody(int payloadLength, String postfix) {
/* 298 */     int currLength = length();
/* 299 */     if (currLength <= payloadLength) {
/* 300 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 304 */     String body = (String)this.customAlert.get("body");
/*     */     
/* 306 */     int acceptableSize = (Utilities.toUTF8Bytes(body)).length - currLength - payloadLength + (Utilities.toUTF8Bytes(postfix)).length;
/*     */ 
/*     */     
/* 309 */     body = Utilities.truncateWhenUTF8(body, acceptableSize) + postfix;
/*     */ 
/*     */     
/* 312 */     this.customAlert.put("body", body);
/*     */ 
/*     */     
/* 315 */     currLength = length();
/*     */     
/* 317 */     if (currLength > payloadLength)
/*     */     {
/*     */       
/* 320 */       this.customAlert.remove("body");
/*     */     }
/*     */     
/* 323 */     return this;
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
/*     */   public PayloadBuilder shrinkBody() {
/* 338 */     return shrinkBody("");
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
/*     */   public PayloadBuilder shrinkBody(String postfix) {
/* 355 */     return resizeAlertBody(256, postfix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String build() {
/* 365 */     if (!this.root.containsKey("mdm")) {
/* 366 */       insertCustomAlert();
/* 367 */       this.root.put("aps", this.aps);
/*     */     } 
/*     */     try {
/* 370 */       return mapper.writeValueAsString(this.root);
/* 371 */     } catch (Exception e) {
/* 372 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void insertCustomAlert() {
/* 377 */     switch (this.customAlert.size()) {
/*     */       case 0:
/* 379 */         this.aps.remove("alert");
/*     */         return;
/*     */       case 1:
/* 382 */         if (this.customAlert.containsKey("body")) {
/* 383 */           this.aps.put("alert", this.customAlert.get("body"));
/*     */           return;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */     
/* 389 */     this.aps.put("alert", this.customAlert);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] buildBytes() {
/* 400 */     return Utilities.toUTF8Bytes(build());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 405 */     return build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private PayloadBuilder(Map<String, Object> root, Map<String, Object> aps, Map<String, Object> customAlert) {
/* 411 */     this.root = new HashMap<String, Object>(root);
/* 412 */     this.aps = new HashMap<String, Object>(aps);
/* 413 */     this.customAlert = new HashMap<String, Object>(customAlert);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PayloadBuilder copy() {
/* 422 */     return new PayloadBuilder(this.root, this.aps, this.customAlert);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PayloadBuilder newPayload() {
/* 429 */     return new PayloadBuilder();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/PayloadBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */