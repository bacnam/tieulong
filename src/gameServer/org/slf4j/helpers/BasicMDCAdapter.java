/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.spi.MDCAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicMDCAdapter
/*     */   implements MDCAdapter
/*     */ {
/*  46 */   private InheritableThreadLocal<Map<String, String>> inheritableThreadLocal = new InheritableThreadLocal<Map<String, String>>();
/*     */   
/*     */   static boolean isJDK14() {
/*     */     try {
/*  50 */       String javaVersion = System.getProperty("java.version");
/*  51 */       return javaVersion.startsWith("1.4");
/*  52 */     } catch (SecurityException se) {
/*     */       
/*  54 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*  58 */   static boolean IS_JDK14 = isJDK14();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String key, String val) {
/*  73 */     if (key == null) {
/*  74 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/*  76 */     Map<String, String> map = this.inheritableThreadLocal.get();
/*  77 */     if (map == null) {
/*  78 */       map = Collections.synchronizedMap(new HashMap<String, String>());
/*  79 */       this.inheritableThreadLocal.set(map);
/*     */     } 
/*  81 */     map.put(key, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String key) {
/*  88 */     Map<String, String> Map = this.inheritableThreadLocal.get();
/*  89 */     if (Map != null && key != null) {
/*  90 */       return Map.get(key);
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 100 */     Map<String, String> map = this.inheritableThreadLocal.get();
/* 101 */     if (map != null) {
/* 102 */       map.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 110 */     Map<String, String> map = this.inheritableThreadLocal.get();
/* 111 */     if (map != null) {
/* 112 */       map.clear();
/*     */ 
/*     */       
/* 115 */       if (isJDK14()) {
/* 116 */         this.inheritableThreadLocal.set(null);
/*     */       } else {
/* 118 */         this.inheritableThreadLocal.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getKeys() {
/* 130 */     Map<String, String> map = this.inheritableThreadLocal.get();
/* 131 */     if (map != null) {
/* 132 */       return map.keySet();
/*     */     }
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopyOfContextMap() {
/* 144 */     Map<String, String> oldMap = this.inheritableThreadLocal.get();
/* 145 */     if (oldMap != null) {
/* 146 */       Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
/* 147 */       synchronized (oldMap) {
/* 148 */         newMap.putAll(oldMap);
/*     */       } 
/* 150 */       return newMap;
/*     */     } 
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContextMap(Map<String, String> contextMap) {
/* 157 */     Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>(contextMap));
/* 158 */     this.inheritableThreadLocal.set(map);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/slf4j/helpers/BasicMDCAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */