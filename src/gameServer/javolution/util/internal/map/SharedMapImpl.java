/*     */ package javolution.util.internal.map;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.collection.ReadWriteLockImpl;
/*     */ import javolution.util.service.MapService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SharedMapImpl<K, V>
/*     */   extends MapView<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   protected ReadWriteLockImpl lock;
/*     */   protected transient Thread updatingThread;
/*     */   
/*     */   private class IteratorImpl
/*     */     implements Iterator<Map.Entry<K, V>>
/*     */   {
/*     */     private Map.Entry<K, V> next;
/*     */     private final Iterator<Map.Entry<K, V>> targetIterator;
/*     */     
/*     */     public IteratorImpl() {
/*  29 */       SharedMapImpl.this.lock.readLock.lock();
/*     */       try {
/*  31 */         this.targetIterator = SharedMapImpl.this.cloneTarget().entrySet().iterator();
/*     */       } finally {
/*  33 */         SharedMapImpl.this.lock.readLock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  39 */       return this.targetIterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<K, V> next() {
/*  44 */       this.next = this.targetIterator.next();
/*  45 */       return this.next;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  50 */       if (this.next == null) throw new IllegalStateException(); 
/*  51 */       SharedMapImpl.this.remove(this.next.getKey());
/*  52 */       this.next = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SharedMapImpl(MapService<K, V> target) {
/*  61 */     this(target, new ReadWriteLockImpl());
/*     */   }
/*     */   
/*     */   public SharedMapImpl(MapService<K, V> target, ReadWriteLockImpl lock) {
/*  65 */     super(target);
/*  66 */     this.lock = lock;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  71 */     this.lock.writeLock.lock();
/*     */     try {
/*  73 */       target().clear();
/*     */     } finally {
/*  75 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  81 */     this.lock.readLock.lock();
/*     */     try {
/*  83 */       return target().containsKey(key);
/*     */     } finally {
/*  85 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  91 */     this.lock.readLock.lock();
/*     */     try {
/*  93 */       return target().containsValue(value);
/*     */     } finally {
/*  95 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 101 */     this.lock.readLock.lock();
/*     */     try {
/* 103 */       return (V)target().get(key);
/*     */     } finally {
/* 105 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 111 */     this.lock.readLock.lock();
/*     */     try {
/* 113 */       return target().isEmpty();
/*     */     } finally {
/* 115 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<K, V>> iterator() {
/* 121 */     return new IteratorImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super K> keyComparator() {
/* 126 */     return target().keyComparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 131 */     this.lock.writeLock.lock();
/*     */     try {
/* 133 */       return (V)target().put(key, value);
/*     */     } finally {
/* 135 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/* 141 */     this.lock.writeLock.lock();
/*     */     try {
/* 143 */       target().putAll(m);
/*     */     } finally {
/* 145 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public V putIfAbsent(K key, V value) {
/* 151 */     this.lock.writeLock.lock();
/*     */     try {
/* 153 */       return (V)target().putIfAbsent(key, value);
/*     */     } finally {
/* 155 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 161 */     this.lock.writeLock.lock();
/*     */     try {
/* 163 */       return (V)target().remove(key);
/*     */     } finally {
/* 165 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object key, Object value) {
/* 171 */     this.lock.writeLock.lock();
/*     */     try {
/* 173 */       return target().remove(key, value);
/*     */     } finally {
/* 175 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public V replace(K key, V value) {
/* 181 */     this.lock.writeLock.lock();
/*     */     try {
/* 183 */       return (V)target().replace(key, value);
/*     */     } finally {
/* 185 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean replace(K key, V oldValue, V newValue) {
/* 191 */     this.lock.writeLock.lock();
/*     */     try {
/* 193 */       return target().replace(key, oldValue, newValue);
/*     */     } finally {
/* 195 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 201 */     this.lock.readLock.lock();
/*     */     try {
/* 203 */       return target().size();
/*     */     } finally {
/* 205 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapService<K, V>[] split(int n) {
/*     */     MapService[] arrayOfMapService1;
/* 213 */     this.lock.readLock.lock();
/*     */     try {
/* 215 */       arrayOfMapService1 = (MapService[])target().split(n);
/*     */     } finally {
/* 217 */       this.lock.readLock.unlock();
/*     */     } 
/* 219 */     MapService[] arrayOfMapService2 = new MapService[arrayOfMapService1.length];
/* 220 */     for (int i = 0; i < arrayOfMapService1.length; i++) {
/* 221 */       arrayOfMapService2[i] = new SharedMapImpl(arrayOfMapService1[i], this.lock);
/*     */     }
/* 223 */     return (MapService<K, V>[])arrayOfMapService2;
/*     */   }
/*     */ 
/*     */   
/*     */   public MapService<K, V> threadSafe() {
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super V> valueComparator() {
/* 233 */     return target().valueComparator();
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapService<K, V> cloneTarget() {
/*     */     try {
/* 239 */       return target().clone();
/* 240 */     } catch (CloneNotSupportedException e) {
/* 241 */       throw new Error("Cannot happen since target is Cloneable.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/SharedMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */