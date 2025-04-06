/*     */ package javolution.util.internal.map;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.function.Function;
/*     */ import javolution.util.internal.collection.MappedCollectionImpl;
/*     */ import javolution.util.internal.set.MappedSetImpl;
/*     */ import javolution.util.internal.set.SetView;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.MapService;
/*     */ import javolution.util.service.SetService;
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
/*     */ public abstract class MapView<K, V>
/*     */   implements MapService<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private MapService<K, V> target;
/*     */   
/*     */   protected class EntryComparator
/*     */     implements Equality<Map.Entry<K, V>>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1536L;
/*     */     
/*     */     public boolean areEqual(Map.Entry<K, V> left, Map.Entry<K, V> right) {
/*  43 */       return MapView.this.keyComparator().areEqual(left.getKey(), right.getKey());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(Map.Entry<K, V> left, Map.Entry<K, V> right) {
/*  49 */       return MapView.this.keyComparator().compare(left.getKey(), right.getKey());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCodeOf(Map.Entry<K, V> e) {
/*  55 */       return MapView.this.keyComparator().hashCodeOf(e.getKey());
/*     */     }
/*     */   }
/*     */   
/*     */   protected class EntrySet extends SetView<Map.Entry<K, V>> {
/*     */     private static final long serialVersionUID = 1536L;
/*     */     
/*     */     public EntrySet() {
/*  63 */       super(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(Map.Entry<K, V> entry) {
/*  68 */       MapView.this.put(entry.getKey(), entry.getValue());
/*  69 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Equality<? super Map.Entry<K, V>> comparator() {
/*  74 */       return new MapView.EntryComparator();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/*  80 */       if (obj instanceof Map.Entry) {
/*  81 */         Map.Entry<K, V> e = (Map.Entry<K, V>)obj;
/*  82 */         return contains(e.getKey());
/*     */       } 
/*  84 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/*  89 */       return MapView.this.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/*  94 */       return MapView.this.iterator();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void perform(final Consumer<CollectionService<Map.Entry<K, V>>> action, final CollectionService<Map.Entry<K, V>> view) {
/* 101 */       Consumer<MapService<K, V>> mapAction = new Consumer<MapService<K, V>>()
/*     */         {
/*     */           public void accept(MapService<K, V> param) {
/* 104 */             action.accept(view);
/*     */           }
/*     */         };
/* 107 */       MapView.this.perform(mapAction, MapView.this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 113 */       if (obj instanceof Map.Entry) {
/* 114 */         Map.Entry<K, V> e = (Map.Entry<K, V>)obj;
/* 115 */         if (!contains(e.getKey())) return false; 
/* 116 */         MapView.this.remove(e.getKey());
/* 117 */         return true;
/*     */       } 
/* 119 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 124 */       return MapView.this.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public CollectionService<Map.Entry<K, V>>[] split(int n) {
/* 130 */       MapService[] arrayOfMapService = MapView.this.split(n);
/* 131 */       CollectionService[] arrayOfCollectionService = new CollectionService[arrayOfMapService.length];
/* 132 */       for (int i = 0; i < arrayOfCollectionService.length; i++) {
/* 133 */         arrayOfCollectionService[i] = (CollectionService)arrayOfMapService[i].entrySet();
/*     */       }
/* 135 */       return (CollectionService<Map.Entry<K, V>>[])arrayOfCollectionService;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void update(final Consumer<CollectionService<Map.Entry<K, V>>> action, final CollectionService<Map.Entry<K, V>> view) {
/* 142 */       Consumer<MapService<K, V>> mapAction = new Consumer<MapService<K, V>>()
/*     */         {
/*     */           public void accept(MapService<K, V> param) {
/* 145 */             action.accept(view);
/*     */           }
/*     */         };
/* 148 */       MapView.this.update(mapAction, MapView.this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class KeySet
/*     */     extends MappedSetImpl<Map.Entry<K, V>, K> {
/*     */     private static final long serialVersionUID = 1536L;
/*     */     
/*     */     public KeySet() {
/* 157 */       super(MapView.this.entrySet(), new Function<Map.Entry<K, V>, K>(MapView.this)
/*     */           {
/*     */             public K apply(Map.Entry<K, V> e) {
/* 160 */               return e.getKey();
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(K key) {
/* 168 */       if (MapView.this.containsKey(key)) return false; 
/* 169 */       MapView.this.put(key, null);
/* 170 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Equality<? super K> comparator() {
/* 175 */       return MapView.this.keyComparator();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 181 */       return MapView.this.containsKey(obj);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 187 */       if (!MapView.this.containsKey(obj)) return false; 
/* 188 */       MapView.this.remove(obj);
/* 189 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class Values
/*     */     extends MappedCollectionImpl<Map.Entry<K, V>, V> {
/*     */     private static final long serialVersionUID = 1536L;
/*     */     
/*     */     public Values() {
/* 198 */       super((CollectionService)MapView.this.entrySet(), new Function<Map.Entry<K, V>, V>(MapView.this)
/*     */           {
/*     */             public V apply(Map.Entry<K, V> e) {
/* 201 */               return e.getValue();
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public Equality<? super V> comparator() {
/* 208 */       return MapView.this.valueComparator();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapView(MapService<K, V> target) {
/* 219 */     this.target = target;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 224 */     Iterator<Map.Entry<K, V>> it = iterator();
/* 225 */     while (it.hasNext()) {
/* 226 */       it.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapView<K, V> clone() {
/*     */     try {
/* 234 */       MapView<K, V> copy = (MapView<K, V>)super.clone();
/* 235 */       if (this.target != null) {
/* 236 */         copy.target = this.target.clone();
/*     */       }
/* 238 */       return copy;
/* 239 */     } catch (CloneNotSupportedException e) {
/* 240 */       throw new Error("Should not happen since target is cloneable");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 249 */     return values().contains(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public SetService<Map.Entry<K, V>> entrySet() {
/* 254 */     return (SetService<Map.Entry<K, V>>)new EntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 262 */     return iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetService<K> keySet() {
/* 273 */     return (SetService<K>)new KeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void perform(Consumer<MapService<K, V>> action, MapService<K, V> view) {
/* 278 */     if (this.target == null) {
/* 279 */       action.accept(view);
/*     */     } else {
/* 281 */       this.target.perform(action, view);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/* 291 */     Iterator<?> it = m.entrySet().iterator();
/* 292 */     while (it.hasNext()) {
/* 293 */       Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
/* 294 */       put(e.getKey(), e.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public V putIfAbsent(K key, V value) {
/* 300 */     if (!containsKey(key)) return put(key, value); 
/* 301 */     return get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object key, Object value) {
/* 309 */     if (containsKey(key) && get(key).equals(value)) {
/* 310 */       remove(key);
/* 311 */       return true;
/* 312 */     }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public V replace(K key, V value) {
/* 317 */     if (containsKey(key))
/* 318 */       return put(key, value); 
/* 319 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean replace(K key, V oldValue, V newValue) {
/* 324 */     if (containsKey(key) && get(key).equals(oldValue)) {
/* 325 */       put(key, newValue);
/* 326 */       return true;
/* 327 */     }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 332 */     int count = 0;
/* 333 */     Iterator<Map.Entry<K, V>> it = iterator();
/* 334 */     while (it.hasNext()) {
/* 335 */       count++;
/* 336 */       it.next();
/*     */     } 
/* 338 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapService<K, V>[] split(int n) {
/* 344 */     if (this.target == null) return (MapService<K, V>[])new MapService[] { this }; 
/* 345 */     MapService[] arrayOfMapService1 = (MapService[])this.target.split(n);
/* 346 */     MapService[] arrayOfMapService2 = new MapService[arrayOfMapService1.length];
/* 347 */     for (int i = 0; i < arrayOfMapService1.length; i++) {
/* 348 */       MapView<K, V> copy = clone();
/* 349 */       copy.target = arrayOfMapService1[i];
/* 350 */       arrayOfMapService2[i] = copy;
/*     */     } 
/* 352 */     return (MapService<K, V>[])arrayOfMapService2;
/*     */   }
/*     */ 
/*     */   
/*     */   public MapService<K, V> threadSafe() {
/* 357 */     return new SharedMapImpl<K, V>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(Consumer<MapService<K, V>> action, MapService<K, V> view) {
/* 362 */     if (this.target == null) {
/* 363 */       action.accept(view);
/*     */     } else {
/* 365 */       this.target.update(action, view);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionService<V> values() {
/* 374 */     return (CollectionService<V>)new Values();
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapService<K, V> target() {
/* 379 */     return this.target;
/*     */   }
/*     */   
/*     */   public abstract boolean containsKey(Object paramObject);
/*     */   
/*     */   public abstract V get(Object paramObject);
/*     */   
/*     */   public abstract Iterator<Map.Entry<K, V>> iterator();
/*     */   
/*     */   public abstract Equality<? super K> keyComparator();
/*     */   
/*     */   public abstract V put(K paramK, V paramV);
/*     */   
/*     */   public abstract V remove(Object paramObject);
/*     */   
/*     */   public abstract Equality<? super V> valueComparator();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/MapView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */