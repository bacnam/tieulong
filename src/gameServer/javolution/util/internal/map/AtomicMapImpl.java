/*     */ package javolution.util.internal.map;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.service.MapService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AtomicMapImpl<K, V>
/*     */   extends MapView<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   protected volatile MapService<K, V> immutable;
/*     */   protected transient Thread updatingThread;
/*     */   
/*     */   private class IteratorImpl
/*     */     implements Iterator<Map.Entry<K, V>>
/*     */   {
/*     */     private Map.Entry<K, V> current;
/*  26 */     private final Iterator<Map.Entry<K, V>> targetIterator = AtomicMapImpl.this.targetView().iterator();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  30 */       return this.targetIterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<K, V> next() {
/*  35 */       this.current = this.targetIterator.next();
/*  36 */       return this.current;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  41 */       if (this.current == null) throw new IllegalStateException(); 
/*  42 */       AtomicMapImpl.this.remove(this.current.getKey());
/*  43 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     private IteratorImpl() {}
/*     */   }
/*     */ 
/*     */   
/*     */   public AtomicMapImpl(MapService<K, V> target) {
/*  52 */     super(target);
/*  53 */     this.immutable = cloneTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/*  58 */     clear();
/*  59 */     if (!updateInProgress()) {
/*  60 */       this.immutable = cloneTarget();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized AtomicMapImpl<K, V> clone() {
/*  66 */     AtomicMapImpl<K, V> copy = (AtomicMapImpl<K, V>)super.clone();
/*  67 */     copy.updatingThread = null;
/*  68 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  73 */     return targetView().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  78 */     return targetView().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  83 */     return (V)targetView().get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  88 */     return targetView().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<K, V>> iterator() {
/*  93 */     return new IteratorImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super K> keyComparator() {
/*  98 */     return targetView().keyComparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized V put(K key, V value) {
/* 103 */     V v = (V)target().put(key, value);
/* 104 */     if (!updateInProgress()) this.immutable = cloneTarget(); 
/* 105 */     return v;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void putAll(Map<? extends K, ? extends V> m) {
/* 110 */     target().putAll(m);
/* 111 */     if (!updateInProgress()) this.immutable = cloneTarget();
/*     */   
/*     */   }
/*     */   
/*     */   public synchronized V putIfAbsent(K key, V value) {
/* 116 */     V v = (V)target().putIfAbsent(key, value);
/* 117 */     if (!updateInProgress()) this.immutable = cloneTarget(); 
/* 118 */     return v;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized V remove(Object key) {
/* 123 */     V v = (V)target().remove(key);
/* 124 */     if (!updateInProgress()) this.immutable = cloneTarget(); 
/* 125 */     return v;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean remove(Object key, Object value) {
/* 130 */     boolean changed = target().remove(key, value);
/* 131 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 132 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized V replace(K key, V value) {
/* 137 */     V v = (V)target().replace(key, value);
/* 138 */     if (!updateInProgress()) this.immutable = cloneTarget(); 
/* 139 */     return v;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean replace(K key, V oldValue, V newValue) {
/* 144 */     boolean changed = target().replace(key, oldValue, newValue);
/* 145 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 146 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 151 */     return targetView().size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapService<K, V>[] split(int n) {
/* 157 */     return (MapService<K, V>[])new MapService[] { this };
/*     */   }
/*     */ 
/*     */   
/*     */   public MapService<K, V> threadSafe() {
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void update(Consumer<MapService<K, V>> action, MapService<K, V> view) {
/* 168 */     this.updatingThread = Thread.currentThread();
/*     */     try {
/* 170 */       target().update(action, view);
/*     */     } finally {
/* 172 */       this.updatingThread = null;
/* 173 */       this.immutable = cloneTarget();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super V> valueComparator() {
/* 179 */     return targetView().valueComparator();
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapService<K, V> cloneTarget() {
/*     */     try {
/* 185 */       return target().clone();
/* 186 */     } catch (CloneNotSupportedException e) {
/* 187 */       throw new Error("Cannot happen since target is Cloneable.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapService<K, V> targetView() {
/* 194 */     return (this.updatingThread == null || this.updatingThread != Thread.currentThread()) ? this.immutable : target();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean updateInProgress() {
/* 201 */     return (this.updatingThread == Thread.currentThread());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/AtomicMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */