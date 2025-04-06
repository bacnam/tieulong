/*     */ package javolution.util.internal.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.service.MapService;
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
/*     */ public class FastMapImpl<K, V>
/*     */   extends MapView<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*  26 */   transient MapEntryImpl<K, V> firstEntry = null;
/*  27 */   transient FractalMapImpl fractal = new FractalMapImpl();
/*  28 */   transient MapEntryImpl<K, V> freeEntry = new MapEntryImpl<K, V>();
/*     */   final Equality<? super K> keyComparator;
/*  30 */   transient MapEntryImpl<K, V> lastEntry = null;
/*     */   
/*     */   transient int size;
/*     */   final Equality<? super V> valueComparator;
/*     */   
/*     */   public FastMapImpl(Equality<? super K> keyComparator, Equality<? super V> valueComparator) {
/*  36 */     super(null);
/*  37 */     this.keyComparator = keyComparator;
/*  38 */     this.valueComparator = valueComparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  43 */     this.firstEntry = null;
/*  44 */     this.lastEntry = null;
/*  45 */     this.fractal = new FractalMapImpl();
/*  46 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public FastMapImpl<K, V> clone() {
/*  51 */     FastMapImpl<K, V> copy = new FastMapImpl(keyComparator(), valueComparator());
/*     */     
/*  53 */     copy.putAll((Map<? extends K, ? extends V>)this);
/*  54 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  60 */     return (this.fractal.getEntry(key, this.keyComparator.hashCodeOf(key)) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  66 */     MapEntryImpl<K, V> entry = this.fractal.getEntry(key, this.keyComparator.hashCodeOf(key));
/*     */     
/*  68 */     if (entry == null) return null; 
/*  69 */     return entry.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<K, V>> iterator() {
/*  74 */     return new Iterator<Map.Entry<K, V>>() {
/*     */         MapEntryImpl<K, V> current;
/*  76 */         MapEntryImpl<K, V> next = FastMapImpl.this.firstEntry;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/*  80 */           return (this.next != null);
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/*  85 */           if (this.next == null) throw new NoSuchElementException(); 
/*  86 */           this.current = this.next;
/*  87 */           this.next = this.next.next;
/*  88 */           return this.current;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/*  93 */           if (this.current == null) throw new IllegalStateException(); 
/*  94 */           FastMapImpl.this.fractal.removeEntry(this.current.key, this.current.hash);
/*  95 */           FastMapImpl.this.detachEntry(this.current);
/*  96 */           FastMapImpl.this.size--;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Equality<? super K> keyComparator() {
/* 104 */     return this.keyComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 110 */     int hash = this.keyComparator.hashCodeOf(key);
/* 111 */     MapEntryImpl<K, V> tmp = this.fractal.addEntry(this.freeEntry, key, hash);
/* 112 */     if (tmp == this.freeEntry) {
/* 113 */       this.freeEntry = new MapEntryImpl<K, V>();
/* 114 */       attachEntry(tmp);
/* 115 */       this.size++;
/* 116 */       tmp.value = value;
/* 117 */       return null;
/*     */     } 
/* 119 */     V oldValue = tmp.value;
/* 120 */     tmp.value = value;
/* 121 */     return oldValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V putIfAbsent(K key, V value) {
/* 128 */     int hash = this.keyComparator.hashCodeOf(key);
/* 129 */     MapEntryImpl<K, V> tmp = this.fractal.addEntry(this.freeEntry, key, hash);
/* 130 */     if (tmp == this.freeEntry) {
/* 131 */       this.freeEntry = new MapEntryImpl<K, V>();
/* 132 */       attachEntry(tmp);
/* 133 */       this.size++;
/* 134 */       tmp.value = value;
/* 135 */       return null;
/*     */     } 
/* 137 */     return tmp.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 144 */     MapEntryImpl<K, V> entry = this.fractal.removeEntry(key, this.keyComparator.hashCodeOf(key));
/*     */     
/* 146 */     if (entry == null) return null; 
/* 147 */     detachEntry(entry);
/* 148 */     this.size--;
/* 149 */     return entry.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object key, Object value) {
/* 155 */     int hash = this.keyComparator.hashCodeOf(key);
/* 156 */     MapEntryImpl<K, V> entry = this.fractal.getEntry(key, hash);
/* 157 */     if (entry == null) return false; 
/* 158 */     if (!this.valueComparator.areEqual(entry.value, value)) return false; 
/* 159 */     this.fractal.removeEntry(key, hash);
/* 160 */     detachEntry(entry);
/* 161 */     this.size--;
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V replace(K key, V value) {
/* 168 */     MapEntryImpl<K, V> entry = this.fractal.getEntry(key, this.keyComparator.hashCodeOf(key));
/*     */     
/* 170 */     if (entry == null) return null; 
/* 171 */     V oldValue = entry.value;
/* 172 */     entry.value = value;
/* 173 */     return oldValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean replace(K key, V oldValue, V newValue) {
/* 179 */     MapEntryImpl<K, V> entry = this.fractal.getEntry(key, this.keyComparator.hashCodeOf(key));
/*     */     
/* 181 */     if (entry == null) return false; 
/* 182 */     if (!this.valueComparator.areEqual(entry.value, oldValue)) return false; 
/* 183 */     entry.value = newValue;
/* 184 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 189 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapService<K, V>[] split(int n) {
/* 195 */     return (MapService<K, V>[])new MapService[] { this };
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super V> valueComparator() {
/* 200 */     return this.valueComparator;
/*     */   }
/*     */   
/*     */   private void attachEntry(MapEntryImpl<K, V> entry) {
/* 204 */     if (this.lastEntry != null) {
/* 205 */       this.lastEntry.next = entry;
/* 206 */       entry.previous = this.lastEntry;
/*     */     } 
/* 208 */     this.lastEntry = entry;
/* 209 */     if (this.firstEntry == null) {
/* 210 */       this.firstEntry = entry;
/*     */     }
/*     */   }
/*     */   
/*     */   private void detachEntry(MapEntryImpl<K, V> entry) {
/* 215 */     if (entry == this.firstEntry) {
/* 216 */       this.firstEntry = entry.next;
/*     */     }
/* 218 */     if (entry == this.lastEntry) {
/* 219 */       this.lastEntry = entry.previous;
/*     */     }
/* 221 */     MapEntryImpl<K, V> previous = entry.previous;
/* 222 */     MapEntryImpl<K, V> next = entry.next;
/* 223 */     if (previous != null) {
/* 224 */       previous.next = next;
/*     */     }
/* 226 */     if (next != null) {
/* 227 */       next.previous = previous;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 235 */     s.defaultReadObject();
/* 236 */     this.fractal = new FractalMapImpl();
/* 237 */     this.freeEntry = new MapEntryImpl<K, V>();
/* 238 */     int n = s.readInt();
/* 239 */     for (int i = 0; i < n; i++) {
/* 240 */       put((K)s.readObject(), (V)s.readObject());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 247 */     s.defaultWriteObject();
/* 248 */     s.writeInt(this.size);
/* 249 */     Iterator<Map.Entry<K, V>> it = iterator();
/* 250 */     while (it.hasNext()) {
/* 251 */       Map.Entry<K, V> e = it.next();
/* 252 */       s.writeObject(e.getKey());
/* 253 */       s.writeObject(e.getValue());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/FastMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */