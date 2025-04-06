/*     */ package org.apache.mina.filter.util;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SessionAttributeInitializingFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*  41 */   private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionAttributeInitializingFilter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionAttributeInitializingFilter(Map<String, ? extends Object> attributes) {
/*  58 */     setAttributes(attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttribute(String key) {
/*  68 */     return this.attributes.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setAttribute(String key, Object value) {
/*  79 */     if (value == null) {
/*  80 */       return removeAttribute(key);
/*     */     }
/*     */     
/*  83 */     return this.attributes.put(key, value);
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
/*     */   public Object setAttribute(String key) {
/*  95 */     return this.attributes.put(key, Boolean.TRUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String key) {
/* 104 */     return this.attributes.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean containsAttribute(String key) {
/* 112 */     return this.attributes.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAttributeKeys() {
/* 119 */     return this.attributes.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributes(Map<String, ? extends Object> attributes) {
/* 128 */     if (attributes == null) {
/* 129 */       attributes = new ConcurrentHashMap<String, Object>();
/*     */     }
/*     */     
/* 132 */     this.attributes.clear();
/* 133 */     this.attributes.putAll(attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 142 */     for (Map.Entry<String, Object> e : this.attributes.entrySet()) {
/* 143 */       session.setAttribute(e.getKey(), e.getValue());
/*     */     }
/*     */     
/* 146 */     nextFilter.sessionCreated(session);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/util/SessionAttributeInitializingFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */