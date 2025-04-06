/*     */ package com.mchange.v2.util;
/*     */ 
/*     */ import com.mchange.v1.util.AbstractMapEntry;
/*     */ import com.mchange.v1.util.WrapperIterator;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DoubleWeakHashMap
/*     */   implements Map
/*     */ {
/*     */   HashMap inner;
/*  69 */   ReferenceQueue keyQ = new ReferenceQueue();
/*  70 */   ReferenceQueue valQ = new ReferenceQueue();
/*     */   
/*  72 */   CheckKeyHolder holder = new CheckKeyHolder();
/*     */   
/*  74 */   Set userKeySet = null;
/*  75 */   Collection valuesCollection = null;
/*     */   
/*     */   public DoubleWeakHashMap() {
/*  78 */     this.inner = new HashMap<Object, Object>();
/*     */   }
/*     */   public DoubleWeakHashMap(int paramInt) {
/*  81 */     this.inner = new HashMap<Object, Object>(paramInt);
/*     */   }
/*     */   public DoubleWeakHashMap(int paramInt, float paramFloat) {
/*  84 */     this.inner = new HashMap<Object, Object>(paramInt, paramFloat);
/*     */   }
/*     */   
/*     */   public DoubleWeakHashMap(Map paramMap) {
/*  88 */     this();
/*  89 */     putAll(paramMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanCleared() {
/*     */     WKey wKey;
/*  95 */     while ((wKey = (WKey)this.keyQ.poll()) != null) {
/*  96 */       this.inner.remove(wKey);
/*     */     }
/*     */     WVal wVal;
/*  99 */     while ((wVal = (WVal)this.valQ.poll()) != null) {
/* 100 */       this.inner.remove(wVal.getWKey());
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 105 */     cleanCleared();
/* 106 */     this.inner.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object paramObject) {
/* 111 */     cleanCleared();
/*     */     try {
/* 113 */       return this.inner.containsKey(this.holder.set(paramObject));
/*     */     } finally {
/* 115 */       this.holder.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object paramObject) {
/* 120 */     for (WVal wVal : this.inner.values()) {
/*     */ 
/*     */       
/* 123 */       if (paramObject.equals(wVal.get()))
/* 124 */         return true; 
/*     */     } 
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set entrySet() {
/* 131 */     cleanCleared();
/* 132 */     return new UserEntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(Object paramObject) {
/*     */     try {
/* 139 */       cleanCleared();
/* 140 */       WVal wVal = (WVal)this.inner.get(this.holder.set(paramObject));
/* 141 */       return (wVal == null) ? null : wVal.get();
/*     */     } finally {
/*     */       
/* 144 */       this.holder.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 149 */     cleanCleared();
/* 150 */     return this.inner.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set keySet() {
/* 155 */     cleanCleared();
/* 156 */     if (this.userKeySet == null)
/* 157 */       this.userKeySet = new UserKeySet(); 
/* 158 */     return this.userKeySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object put(Object paramObject1, Object paramObject2) {
/* 163 */     cleanCleared();
/* 164 */     WVal wVal = doPut(paramObject1, paramObject2);
/* 165 */     if (wVal != null) {
/* 166 */       return wVal.get();
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private WVal doPut(Object paramObject1, Object paramObject2) {
/* 173 */     WKey wKey = new WKey(paramObject1, this.keyQ);
/* 174 */     WVal wVal = new WVal(wKey, paramObject2, this.valQ);
/* 175 */     return this.inner.put(wKey, wVal);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map paramMap) {
/* 180 */     cleanCleared();
/* 181 */     for (Map.Entry entry : paramMap.entrySet())
/*     */     {
/*     */       
/* 184 */       doPut(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object remove(Object paramObject) {
/*     */     try {
/* 192 */       cleanCleared();
/* 193 */       WVal wVal = (WVal)this.inner.remove(this.holder.set(paramObject));
/* 194 */       return (wVal == null) ? null : wVal.get();
/*     */     } finally {
/*     */       
/* 197 */       this.holder.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int size() {
/* 202 */     cleanCleared();
/* 203 */     return this.inner.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection values() {
/* 208 */     if (this.valuesCollection == null)
/* 209 */       this.valuesCollection = new ValuesCollection(); 
/* 210 */     return this.valuesCollection;
/*     */   }
/*     */   
/*     */   static final class CheckKeyHolder
/*     */   {
/*     */     Object checkKey;
/*     */     
/*     */     public Object get() {
/* 218 */       return this.checkKey;
/*     */     }
/*     */     
/*     */     public CheckKeyHolder set(Object param1Object) {
/* 222 */       assert this.checkKey == null : "Illegal concurrenct use of DoubleWeakHashMap!";
/*     */       
/* 224 */       this.checkKey = param1Object;
/* 225 */       return this;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 229 */       this.checkKey = null;
/*     */     }
/*     */     public int hashCode() {
/* 232 */       return this.checkKey.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 236 */       assert get() != null : "CheckedKeyHolder should never do an equality check while its value is null.";
/*     */       
/* 238 */       if (this == param1Object)
/* 239 */         return true; 
/* 240 */       if (param1Object instanceof CheckKeyHolder)
/* 241 */         return get().equals(((CheckKeyHolder)param1Object).get()); 
/* 242 */       if (param1Object instanceof DoubleWeakHashMap.WKey) {
/* 243 */         return get().equals(((DoubleWeakHashMap.WKey)param1Object).get());
/*     */       }
/* 245 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WKey
/*     */     extends WeakReference
/*     */   {
/*     */     int cachedHash;
/*     */     
/*     */     WKey(Object param1Object, ReferenceQueue<? super T> param1ReferenceQueue) {
/* 255 */       super((T)param1Object, param1ReferenceQueue);
/* 256 */       this.cachedHash = param1Object.hashCode();
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 260 */       return this.cachedHash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 264 */       if (this == param1Object)
/* 265 */         return true; 
/* 266 */       if (param1Object instanceof WKey) {
/*     */         
/* 268 */         WKey wKey = (WKey)param1Object;
/* 269 */         T t1 = get();
/* 270 */         T t2 = wKey.get();
/* 271 */         if (t1 == null || t2 == null) {
/* 272 */           return false;
/*     */         }
/* 274 */         return t1.equals(t2);
/*     */       } 
/* 276 */       if (param1Object instanceof DoubleWeakHashMap.CheckKeyHolder) {
/*     */         
/* 278 */         DoubleWeakHashMap.CheckKeyHolder checkKeyHolder = (DoubleWeakHashMap.CheckKeyHolder)param1Object;
/* 279 */         T t = get();
/* 280 */         Object object = checkKeyHolder.get();
/* 281 */         if (t == null || object == null) {
/* 282 */           return false;
/*     */         }
/* 284 */         return t.equals(object);
/*     */       } 
/*     */       
/* 287 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WVal
/*     */     extends WeakReference {
/*     */     DoubleWeakHashMap.WKey key;
/*     */     
/*     */     WVal(DoubleWeakHashMap.WKey param1WKey, Object param1Object, ReferenceQueue<? super T> param1ReferenceQueue) {
/* 296 */       super((T)param1Object, param1ReferenceQueue);
/* 297 */       this.key = param1WKey;
/*     */     }
/*     */     
/*     */     public DoubleWeakHashMap.WKey getWKey() {
/* 301 */       return this.key;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class UserEntrySet extends AbstractSet {
/*     */     private UserEntrySet() {}
/*     */     
/*     */     private Set innerEntrySet() {
/* 309 */       DoubleWeakHashMap.this.cleanCleared();
/* 310 */       return DoubleWeakHashMap.this.inner.entrySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator iterator() {
/* 315 */       return (Iterator)new WrapperIterator(innerEntrySet().iterator(), true)
/*     */         {
/*     */           protected Object transformObject(Object param2Object)
/*     */           {
/* 319 */             Map.Entry entry = (Map.Entry)param2Object;
/* 320 */             T t1 = ((DoubleWeakHashMap.WKey)entry.getKey()).get();
/* 321 */             T t2 = ((DoubleWeakHashMap.WVal)entry.getValue()).get();
/*     */             
/* 323 */             if (t1 == null || t2 == null) {
/* 324 */               return WrapperIterator.SKIP_TOKEN;
/*     */             }
/* 326 */             return new DoubleWeakHashMap.UserEntry(entry, t1, t2);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public int size() {
/* 332 */       return innerEntrySet().size();
/*     */     }
/*     */   }
/*     */   
/*     */   class UserEntry
/*     */     extends AbstractMapEntry {
/*     */     Map.Entry innerEntry;
/*     */     Object key;
/*     */     Object val;
/*     */     
/*     */     UserEntry(Map.Entry param1Entry, Object param1Object1, Object param1Object2) {
/* 343 */       this.innerEntry = param1Entry;
/* 344 */       this.key = param1Object1;
/* 345 */       this.val = param1Object2;
/*     */     }
/*     */     
/*     */     public final Object getKey() {
/* 349 */       return this.key;
/*     */     }
/*     */     public final Object getValue() {
/* 352 */       return this.val;
/*     */     }
/*     */     public final Object setValue(Object param1Object) {
/* 355 */       return this.innerEntry.setValue(new DoubleWeakHashMap.WVal((DoubleWeakHashMap.WKey)this.innerEntry.getKey(), param1Object, DoubleWeakHashMap.this.valQ));
/*     */     }
/*     */   }
/*     */   
/*     */   class UserKeySet
/*     */     implements Set {
/*     */     public boolean add(Object param1Object) {
/* 362 */       DoubleWeakHashMap.this.cleanCleared();
/* 363 */       throw new UnsupportedOperationException("You cannot add to a Map's key set.");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection param1Collection) {
/* 368 */       DoubleWeakHashMap.this.cleanCleared();
/* 369 */       throw new UnsupportedOperationException("You cannot add to a Map's key set.");
/*     */     }
/*     */     
/*     */     public void clear() {
/* 373 */       DoubleWeakHashMap.this.clear();
/*     */     }
/*     */     
/*     */     public boolean contains(Object param1Object) {
/* 377 */       return DoubleWeakHashMap.this.containsKey(param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection param1Collection) {
/* 382 */       for (Iterator iterator = param1Collection.iterator(); iterator.hasNext();) {
/* 383 */         if (!contains(iterator.next()))
/* 384 */           return false; 
/* 385 */       }  return true;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 389 */       return DoubleWeakHashMap.this.isEmpty();
/*     */     }
/*     */     
/*     */     public Iterator iterator() {
/* 393 */       DoubleWeakHashMap.this.cleanCleared();
/* 394 */       return (Iterator)new WrapperIterator(DoubleWeakHashMap.this.inner.keySet().iterator(), true)
/*     */         {
/*     */           protected Object transformObject(Object param2Object)
/*     */           {
/* 398 */             T t = ((DoubleWeakHashMap.WKey)param2Object).get();
/*     */             
/* 400 */             if (t == null) {
/* 401 */               return WrapperIterator.SKIP_TOKEN;
/*     */             }
/* 403 */             return t;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object param1Object) {
/* 410 */       return (DoubleWeakHashMap.this.remove(param1Object) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection param1Collection) {
/* 415 */       boolean bool = false;
/* 416 */       for (Iterator iterator = param1Collection.iterator(); iterator.hasNext();)
/* 417 */         bool |= remove(iterator.next()); 
/* 418 */       return bool;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection param1Collection) {
/* 424 */       boolean bool = false;
/* 425 */       for (Iterator iterator = iterator(); iterator.hasNext();) {
/*     */         
/* 427 */         if (!param1Collection.contains(iterator.next())) {
/*     */           
/* 429 */           iterator.remove();
/* 430 */           bool = true;
/*     */         } 
/*     */       } 
/* 433 */       return bool;
/*     */     }
/*     */     
/*     */     public int size() {
/* 437 */       return DoubleWeakHashMap.this.size();
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 441 */       DoubleWeakHashMap.this.cleanCleared();
/* 442 */       return (new HashSet(this)).toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray(Object[] param1ArrayOfObject) {
/* 447 */       DoubleWeakHashMap.this.cleanCleared();
/* 448 */       return (new HashSet(this)).toArray(param1ArrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   class ValuesCollection
/*     */     implements Collection
/*     */   {
/*     */     public boolean add(Object param1Object) {
/* 457 */       DoubleWeakHashMap.this.cleanCleared();
/* 458 */       throw new UnsupportedOperationException("DoubleWeakHashMap does not support adding to its values Collection.");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection param1Collection) {
/* 463 */       DoubleWeakHashMap.this.cleanCleared();
/* 464 */       throw new UnsupportedOperationException("DoubleWeakHashMap does not support adding to its values Collection.");
/*     */     }
/*     */     
/*     */     public void clear() {
/* 468 */       DoubleWeakHashMap.this.clear();
/*     */     }
/*     */     public boolean contains(Object param1Object) {
/* 471 */       return DoubleWeakHashMap.this.containsValue(param1Object);
/*     */     }
/*     */     
/*     */     public boolean containsAll(Collection param1Collection) {
/* 475 */       for (Iterator iterator = param1Collection.iterator(); iterator.hasNext();) {
/* 476 */         if (!contains(iterator.next()))
/* 477 */           return false; 
/* 478 */       }  return true;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 482 */       return DoubleWeakHashMap.this.isEmpty();
/*     */     }
/*     */     
/*     */     public Iterator iterator() {
/* 486 */       return (Iterator)new WrapperIterator(DoubleWeakHashMap.this.inner.values().iterator(), true)
/*     */         {
/*     */           protected Object transformObject(Object param2Object)
/*     */           {
/* 490 */             T t = ((DoubleWeakHashMap.WVal)param2Object).get();
/*     */             
/* 492 */             if (t == null) {
/* 493 */               return WrapperIterator.SKIP_TOKEN;
/*     */             }
/* 495 */             return t;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object param1Object) {
/* 502 */       DoubleWeakHashMap.this.cleanCleared();
/* 503 */       return removeValue(param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection param1Collection) {
/* 508 */       DoubleWeakHashMap.this.cleanCleared();
/* 509 */       boolean bool = false;
/* 510 */       for (Iterator iterator = param1Collection.iterator(); iterator.hasNext();)
/* 511 */         bool |= removeValue(iterator.next()); 
/* 512 */       return bool;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection param1Collection) {
/* 517 */       DoubleWeakHashMap.this.cleanCleared();
/* 518 */       return retainValues(param1Collection);
/*     */     }
/*     */     
/*     */     public int size() {
/* 522 */       return DoubleWeakHashMap.this.size();
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 526 */       DoubleWeakHashMap.this.cleanCleared();
/* 527 */       return (new ArrayList(this)).toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray(Object[] param1ArrayOfObject) {
/* 532 */       DoubleWeakHashMap.this.cleanCleared();
/* 533 */       return (new ArrayList(this)).toArray(param1ArrayOfObject);
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean removeValue(Object param1Object) {
/* 538 */       boolean bool = false;
/* 539 */       for (Iterator<DoubleWeakHashMap.WVal> iterator = DoubleWeakHashMap.this.inner.values().iterator(); iterator.hasNext(); ) {
/*     */         
/* 541 */         DoubleWeakHashMap.WVal wVal = iterator.next();
/* 542 */         if (param1Object.equals(wVal.get())) {
/*     */           
/* 544 */           iterator.remove();
/* 545 */           bool = true;
/*     */         } 
/*     */       } 
/* 548 */       return bool;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean retainValues(Collection param1Collection) {
/* 553 */       boolean bool = false;
/* 554 */       for (Iterator<DoubleWeakHashMap.WVal> iterator = DoubleWeakHashMap.this.inner.values().iterator(); iterator.hasNext(); ) {
/*     */         
/* 556 */         DoubleWeakHashMap.WVal wVal = iterator.next();
/* 557 */         if (!param1Collection.contains(wVal.get())) {
/*     */           
/* 559 */           iterator.remove();
/* 560 */           bool = true;
/*     */         } 
/*     */       } 
/* 563 */       return bool;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/util/DoubleWeakHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */