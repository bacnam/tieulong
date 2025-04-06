/*     */ package org.apache.mina.core.session;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequestQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultIoSessionDataStructureFactory
/*     */   implements IoSessionDataStructureFactory
/*     */ {
/*     */   public IoSessionAttributeMap getAttributeMap(IoSession session) throws Exception {
/*  43 */     return new DefaultIoSessionAttributeMap();
/*     */   }
/*     */   
/*     */   public WriteRequestQueue getWriteRequestQueue(IoSession session) throws Exception {
/*  47 */     return new DefaultWriteRequestQueue();
/*     */   }
/*     */   
/*     */   private static class DefaultIoSessionAttributeMap implements IoSessionAttributeMap {
/*  51 */     private final ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getAttribute(IoSession session, Object key, Object defaultValue) {
/*  64 */       if (key == null) {
/*  65 */         throw new IllegalArgumentException("key");
/*     */       }
/*     */       
/*  68 */       if (defaultValue == null) {
/*  69 */         return this.attributes.get(key);
/*     */       }
/*     */       
/*  72 */       Object object = this.attributes.putIfAbsent(key, defaultValue);
/*     */       
/*  74 */       if (object == null) {
/*  75 */         return defaultValue;
/*     */       }
/*  77 */       return object;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object setAttribute(IoSession session, Object key, Object value) {
/*  85 */       if (key == null) {
/*  86 */         throw new IllegalArgumentException("key");
/*     */       }
/*     */       
/*  89 */       if (value == null) {
/*  90 */         return this.attributes.remove(key);
/*     */       }
/*     */       
/*  93 */       return this.attributes.put(key, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object setAttributeIfAbsent(IoSession session, Object key, Object value) {
/* 100 */       if (key == null) {
/* 101 */         throw new IllegalArgumentException("key");
/*     */       }
/*     */       
/* 104 */       if (value == null) {
/* 105 */         return null;
/*     */       }
/*     */       
/* 108 */       return this.attributes.putIfAbsent(key, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object removeAttribute(IoSession session, Object key) {
/* 115 */       if (key == null) {
/* 116 */         throw new IllegalArgumentException("key");
/*     */       }
/*     */       
/* 119 */       return this.attributes.remove(key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean removeAttribute(IoSession session, Object key, Object value) {
/* 126 */       if (key == null) {
/* 127 */         throw new IllegalArgumentException("key");
/*     */       }
/*     */       
/* 130 */       if (value == null) {
/* 131 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 135 */         return this.attributes.remove(key, value);
/* 136 */       } catch (NullPointerException e) {
/* 137 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean replaceAttribute(IoSession session, Object key, Object oldValue, Object newValue) {
/*     */       try {
/* 146 */         return this.attributes.replace(key, oldValue, newValue);
/* 147 */       } catch (NullPointerException e) {
/*     */ 
/*     */         
/* 150 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsAttribute(IoSession session, Object key) {
/* 157 */       return this.attributes.containsKey(key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<Object> getAttributeKeys(IoSession session) {
/* 164 */       synchronized (this.attributes) {
/* 165 */         return new HashSet(this.attributes.keySet());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose(IoSession session) throws Exception {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefaultWriteRequestQueue
/*     */     implements WriteRequestQueue
/*     */   {
/* 179 */     private final Queue<WriteRequest> q = new ConcurrentLinkedQueue<WriteRequest>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose(IoSession session) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear(IoSession session) {
/* 199 */       this.q.clear();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized boolean isEmpty(IoSession session) {
/* 206 */       return this.q.isEmpty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void offer(IoSession session, WriteRequest writeRequest) {
/* 213 */       this.q.offer(writeRequest);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized WriteRequest poll(IoSession session) {
/* 220 */       return this.q.poll();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 225 */       return this.q.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 232 */       return this.q.size();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/DefaultIoSessionDataStructureFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */