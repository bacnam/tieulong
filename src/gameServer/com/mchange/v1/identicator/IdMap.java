/*     */ package com.mchange.v1.identicator;
/*     */ 
/*     */ import com.mchange.v1.util.AbstractMapEntry;
/*     */ import com.mchange.v1.util.SimpleMapEntry;
/*     */ import com.mchange.v1.util.WrapperIterator;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class IdMap
/*     */   extends AbstractMap
/*     */   implements Map
/*     */ {
/*     */   Map inner;
/*     */   Identicator id;
/*     */   
/*     */   protected IdMap(Map paramMap, Identicator paramIdenticator) {
/*  52 */     this.inner = paramMap;
/*  53 */     this.id = paramIdenticator;
/*     */   }
/*     */   
/*     */   public Object put(Object paramObject1, Object paramObject2) {
/*  57 */     return this.inner.put(createIdKey(paramObject1), paramObject2);
/*     */   }
/*     */   public boolean containsKey(Object paramObject) {
/*  60 */     return this.inner.containsKey(createIdKey(paramObject));
/*     */   }
/*     */   public Object get(Object paramObject) {
/*  63 */     return this.inner.get(createIdKey(paramObject));
/*     */   }
/*     */   public Object remove(Object paramObject) {
/*  66 */     return this.inner.remove(createIdKey(paramObject));
/*     */   }
/*     */   protected Object removeIdHashKey(IdHashKey paramIdHashKey) {
/*  69 */     return this.inner.remove(paramIdHashKey);
/*     */   }
/*     */   public Set entrySet() {
/*  72 */     return new UserEntrySet();
/*     */   }
/*     */   protected final Set internalEntrySet() {
/*  75 */     return this.inner.entrySet();
/*     */   }
/*     */   protected abstract IdHashKey createIdKey(Object paramObject);
/*     */   
/*     */   protected final Map.Entry createIdEntry(Object paramObject1, Object paramObject2) {
/*  80 */     return (Map.Entry)new SimpleMapEntry(createIdKey(paramObject1), paramObject2);
/*     */   }
/*     */   protected final Map.Entry createIdEntry(Map.Entry paramEntry) {
/*  83 */     return createIdEntry(paramEntry.getKey(), paramEntry.getValue());
/*     */   }
/*     */   
/*     */   private final class UserEntrySet extends AbstractSet {
/*  87 */     Set innerEntries = IdMap.this.inner.entrySet();
/*     */ 
/*     */     
/*     */     public Iterator iterator() {
/*  91 */       return (Iterator)new WrapperIterator(this.innerEntries.iterator(), true)
/*     */         {
/*     */           protected Object transformObject(Object param2Object) {
/*  94 */             return new IdMap.UserEntry((Map.Entry)param2Object);
/*     */           }
/*     */         };
/*     */     }
/*     */     public int size() {
/*  99 */       return this.innerEntries.size();
/*     */     }
/*     */     
/*     */     public boolean contains(Object param1Object) {
/* 103 */       if (param1Object instanceof Map.Entry) {
/*     */         
/* 105 */         Map.Entry entry = (Map.Entry)param1Object;
/* 106 */         return this.innerEntries.contains(IdMap.this.createIdEntry(entry));
/*     */       } 
/*     */       
/* 109 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object param1Object) {
/* 114 */       if (param1Object instanceof Map.Entry) {
/*     */         
/* 116 */         Map.Entry entry = (Map.Entry)param1Object;
/* 117 */         return this.innerEntries.remove(IdMap.this.createIdEntry(entry));
/*     */       } 
/*     */       
/* 120 */       return false;
/*     */     }
/*     */     private UserEntrySet() {}
/*     */     public void clear() {
/* 124 */       IdMap.this.inner.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class UserEntry extends AbstractMapEntry {
/*     */     private Map.Entry innerEntry;
/*     */     
/*     */     UserEntry(Map.Entry param1Entry) {
/* 132 */       this.innerEntry = param1Entry;
/*     */     }
/*     */     public final Object getKey() {
/* 135 */       return ((IdHashKey)this.innerEntry.getKey()).getKeyObj();
/*     */     }
/*     */     public final Object getValue() {
/* 138 */       return this.innerEntry.getValue();
/*     */     }
/*     */     public final Object setValue(Object param1Object) {
/* 141 */       return this.innerEntry.setValue(param1Object);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/IdMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */