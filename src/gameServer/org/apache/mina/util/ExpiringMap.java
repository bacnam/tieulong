/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpiringMap<K, V>
/*     */   implements Map<K, V>
/*     */ {
/*     */   public static final int DEFAULT_TIME_TO_LIVE = 60;
/*     */   public static final int DEFAULT_EXPIRATION_INTERVAL = 1;
/*  49 */   private static volatile int expirerCount = 1;
/*     */ 
/*     */   
/*     */   private final ConcurrentHashMap<K, ExpiringObject> delegate;
/*     */ 
/*     */   
/*     */   private final CopyOnWriteArrayList<ExpirationListener<V>> expirationListeners;
/*     */ 
/*     */   
/*     */   private final Expirer expirer;
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpiringMap() {
/*  63 */     this(60, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpiringMap(int timeToLive) {
/*  74 */     this(timeToLive, 1);
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
/*     */   public ExpiringMap(int timeToLive, int expirationInterval) {
/*  87 */     this(new ConcurrentHashMap<K, ExpiringObject>(), new CopyOnWriteArrayList<ExpirationListener<V>>(), timeToLive, expirationInterval);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ExpiringMap(ConcurrentHashMap<K, ExpiringObject> delegate, CopyOnWriteArrayList<ExpirationListener<V>> expirationListeners, int timeToLive, int expirationInterval) {
/*  93 */     this.delegate = delegate;
/*  94 */     this.expirationListeners = expirationListeners;
/*     */     
/*  96 */     this.expirer = new Expirer();
/*  97 */     this.expirer.setTimeToLive(timeToLive);
/*  98 */     this.expirer.setExpirationInterval(expirationInterval);
/*     */   }
/*     */   
/*     */   public V put(K key, V value) {
/* 102 */     ExpiringObject answer = this.delegate.put(key, new ExpiringObject(key, value, System.currentTimeMillis()));
/* 103 */     if (answer == null) {
/* 104 */       return null;
/*     */     }
/*     */     
/* 107 */     return answer.getValue();
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/* 111 */     ExpiringObject object = this.delegate.get(key);
/*     */     
/* 113 */     if (object != null) {
/* 114 */       object.setLastAccessTime(System.currentTimeMillis());
/*     */       
/* 116 */       return object.getValue();
/*     */     } 
/*     */     
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/* 123 */     ExpiringObject answer = this.delegate.remove(key);
/* 124 */     if (answer == null) {
/* 125 */       return null;
/*     */     }
/*     */     
/* 128 */     return answer.getValue();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 132 */     return this.delegate.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 136 */     return this.delegate.containsValue(value);
/*     */   }
/*     */   
/*     */   public int size() {
/* 140 */     return this.delegate.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 144 */     return this.delegate.isEmpty();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 148 */     this.delegate.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 153 */     return this.delegate.hashCode();
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 157 */     return this.delegate.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 162 */     return this.delegate.equals(obj);
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> inMap) {
/* 166 */     for (Map.Entry<? extends K, ? extends V> e : inMap.entrySet()) {
/* 167 */       put(e.getKey(), e.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public Collection<V> values() {
/* 172 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 176 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void addExpirationListener(ExpirationListener<V> listener) {
/* 180 */     this.expirationListeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeExpirationListener(ExpirationListener<V> listener) {
/* 184 */     this.expirationListeners.remove(listener);
/*     */   }
/*     */   
/*     */   public Expirer getExpirer() {
/* 188 */     return this.expirer;
/*     */   }
/*     */   
/*     */   public int getExpirationInterval() {
/* 192 */     return this.expirer.getExpirationInterval();
/*     */   }
/*     */   
/*     */   public int getTimeToLive() {
/* 196 */     return this.expirer.getTimeToLive();
/*     */   }
/*     */   
/*     */   public void setExpirationInterval(int expirationInterval) {
/* 200 */     this.expirer.setExpirationInterval(expirationInterval);
/*     */   }
/*     */   
/*     */   public void setTimeToLive(int timeToLive) {
/* 204 */     this.expirer.setTimeToLive(timeToLive);
/*     */   }
/*     */ 
/*     */   
/*     */   private class ExpiringObject
/*     */   {
/*     */     private K key;
/*     */     
/*     */     private V value;
/*     */     private long lastAccessTime;
/* 214 */     private final ReadWriteLock lastAccessTimeLock = new ReentrantReadWriteLock();
/*     */     
/*     */     ExpiringObject(K key, V value, long lastAccessTime) {
/* 217 */       if (value == null) {
/* 218 */         throw new IllegalArgumentException("An expiring object cannot be null.");
/*     */       }
/*     */       
/* 221 */       this.key = key;
/* 222 */       this.value = value;
/* 223 */       this.lastAccessTime = lastAccessTime;
/*     */     }
/*     */     
/*     */     public long getLastAccessTime() {
/* 227 */       this.lastAccessTimeLock.readLock().lock();
/*     */       
/*     */       try {
/* 230 */         return this.lastAccessTime;
/*     */       } finally {
/* 232 */         this.lastAccessTimeLock.readLock().unlock();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setLastAccessTime(long lastAccessTime) {
/* 237 */       this.lastAccessTimeLock.writeLock().lock();
/*     */       
/*     */       try {
/* 240 */         this.lastAccessTime = lastAccessTime;
/*     */       } finally {
/* 242 */         this.lastAccessTimeLock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 247 */       return this.key;
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 251 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 256 */       return this.value.equals(obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 261 */       return this.value.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class Expirer
/*     */     implements Runnable
/*     */   {
/* 271 */     private final ReadWriteLock stateLock = new ReentrantReadWriteLock();
/*     */ 
/*     */     
/*     */     private long timeToLiveMillis;
/*     */ 
/*     */     
/*     */     private long expirationIntervalMillis;
/*     */ 
/*     */     
/*     */     private boolean running = false;
/*     */     
/*     */     private final Thread expirerThread;
/*     */ 
/*     */     
/*     */     public Expirer() {
/* 286 */       this.expirerThread = new Thread(this, "ExpiringMapExpirer-" + ExpiringMap.expirerCount++);
/* 287 */       this.expirerThread.setDaemon(true);
/*     */     }
/*     */     
/*     */     public void run() {
/* 291 */       while (this.running) {
/* 292 */         processExpires();
/*     */         
/*     */         try {
/* 295 */           Thread.sleep(this.expirationIntervalMillis);
/* 296 */         } catch (InterruptedException e) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void processExpires() {
/* 303 */       long timeNow = System.currentTimeMillis();
/*     */       
/* 305 */       for (ExpiringMap<K, V>.ExpiringObject o : ExpiringMap.this.delegate.values()) {
/*     */         
/* 307 */         if (this.timeToLiveMillis <= 0L) {
/*     */           continue;
/*     */         }
/*     */         
/* 311 */         long timeIdle = timeNow - o.getLastAccessTime();
/*     */         
/* 313 */         if (timeIdle >= this.timeToLiveMillis) {
/* 314 */           ExpiringMap.this.delegate.remove(o.getKey());
/*     */           
/* 316 */           for (ExpirationListener<V> listener : (Iterable<ExpirationListener<V>>)ExpiringMap.this.expirationListeners) {
/* 317 */             listener.expired(o.getValue());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startExpiring() {
/* 328 */       this.stateLock.writeLock().lock();
/*     */       
/*     */       try {
/* 331 */         if (!this.running) {
/* 332 */           this.running = true;
/* 333 */           this.expirerThread.start();
/*     */         } 
/*     */       } finally {
/* 336 */         this.stateLock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startExpiringIfNotStarted() {
/* 345 */       this.stateLock.readLock().lock();
/*     */       try {
/* 347 */         if (this.running) {
/*     */           return;
/*     */         }
/*     */       } finally {
/* 351 */         this.stateLock.readLock().unlock();
/*     */       } 
/*     */       
/* 354 */       this.stateLock.writeLock().lock();
/*     */       try {
/* 356 */         if (!this.running) {
/* 357 */           this.running = true;
/* 358 */           this.expirerThread.start();
/*     */         } 
/*     */       } finally {
/* 361 */         this.stateLock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void stopExpiring() {
/* 369 */       this.stateLock.writeLock().lock();
/*     */       
/*     */       try {
/* 372 */         if (this.running) {
/* 373 */           this.running = false;
/* 374 */           this.expirerThread.interrupt();
/*     */         } 
/*     */       } finally {
/* 377 */         this.stateLock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isRunning() {
/* 388 */       this.stateLock.readLock().lock();
/*     */       
/*     */       try {
/* 391 */         return this.running;
/*     */       } finally {
/* 393 */         this.stateLock.readLock().unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getTimeToLive() {
/* 404 */       this.stateLock.readLock().lock();
/*     */       
/*     */       try {
/* 407 */         return (int)this.timeToLiveMillis / 1000;
/*     */       } finally {
/* 409 */         this.stateLock.readLock().unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTimeToLive(long timeToLive) {
/* 420 */       this.stateLock.writeLock().lock();
/*     */       
/*     */       try {
/* 423 */         this.timeToLiveMillis = timeToLive * 1000L;
/*     */       } finally {
/* 425 */         this.stateLock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getExpirationInterval() {
/* 437 */       this.stateLock.readLock().lock();
/*     */       
/*     */       try {
/* 440 */         return (int)this.expirationIntervalMillis / 1000;
/*     */       } finally {
/* 442 */         this.stateLock.readLock().unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setExpirationInterval(long expirationInterval) {
/* 454 */       this.stateLock.writeLock().lock();
/*     */       
/*     */       try {
/* 457 */         this.expirationIntervalMillis = expirationInterval * 1000L;
/*     */       } finally {
/* 459 */         this.stateLock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/ExpiringMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */