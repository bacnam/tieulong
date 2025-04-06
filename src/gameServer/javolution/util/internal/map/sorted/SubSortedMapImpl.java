/*     */ package javolution.util.internal.map.sorted;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.service.SortedMapService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SubSortedMapImpl<K, V>
/*     */   extends SortedMapView<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private final K from;
/*     */   private final K to;
/*     */   
/*     */   private class IteratorImpl
/*     */     implements Iterator<Map.Entry<K, V>>
/*     */   {
/*     */     private boolean ahead;
/*  26 */     private final Equality<? super K> cmp = SubSortedMapImpl.this.keyComparator();
/*     */     private Map.Entry<K, V> next;
/*  28 */     private final Iterator<Map.Entry<K, V>> targetIterator = SubSortedMapImpl.this.target().iterator();
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  33 */       if (this.ahead) return true; 
/*  34 */       while (this.targetIterator.hasNext()) {
/*  35 */         this.next = this.targetIterator.next();
/*  36 */         if (SubSortedMapImpl.this.from != null && this.cmp.compare(this.next.getKey(), SubSortedMapImpl.this.from) < 0)
/*  37 */           continue;  if (SubSortedMapImpl.this.to != null && this.cmp.compare(this.next.getKey(), SubSortedMapImpl.this.to) >= 0)
/*  38 */           break;  this.ahead = true;
/*  39 */         return true;
/*     */       } 
/*  41 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<K, V> next() {
/*  46 */       hasNext();
/*  47 */       this.ahead = false;
/*  48 */       return this.next;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  53 */       this.targetIterator.remove();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private IteratorImpl() {}
/*     */   }
/*     */ 
/*     */   
/*     */   public SubSortedMapImpl(SortedMapService<K, V> target, K from, K to) {
/*  63 */     super(target);
/*  64 */     if (from != null && to != null && keyComparator().compare(from, to) > 0) {
/*  65 */       throw new IllegalArgumentException("from: " + from + ", to: " + to);
/*     */     }
/*  67 */     this.from = from;
/*  68 */     this.to = to;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  74 */     Equality<? super K> cmp = keyComparator();
/*  75 */     if (this.from != null && cmp.compare(key, this.from) < 0) return false; 
/*  76 */     if (this.to != null && cmp.compare(key, this.to) >= 0) return false; 
/*  77 */     return target().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public K firstKey() {
/*  82 */     if (this.from == null) return (K)target().firstKey(); 
/*  83 */     Iterator<Map.Entry<K, V>> it = iterator();
/*  84 */     if (!it.hasNext()) throw new NoSuchElementException(); 
/*  85 */     return (K)((Map.Entry)it.next()).getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  91 */     Equality<? super K> cmp = keyComparator();
/*  92 */     if (this.from != null && cmp.compare(key, this.from) < 0) return null; 
/*  93 */     if (this.to != null && cmp.compare(key, this.to) >= 0) return null; 
/*  94 */     return (V)target().get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<K, V>> iterator() {
/*  99 */     return new IteratorImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super K> keyComparator() {
/* 104 */     return target().keyComparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 109 */     if (this.to == null) return (K)target().lastKey(); 
/* 110 */     Iterator<Map.Entry<K, V>> it = iterator();
/* 111 */     if (!it.hasNext()) throw new NoSuchElementException(); 
/* 112 */     Map.Entry<K, V> last = it.next();
/* 113 */     while (it.hasNext()) {
/* 114 */       last = it.next();
/*     */     }
/* 116 */     return last.getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 121 */     Equality<? super K> cmp = keyComparator();
/* 122 */     if (this.from != null && cmp.compare(key, this.from) < 0) throw new IllegalArgumentException("Key: " + key + " outside of this sub-map bounds");
/*     */     
/* 124 */     if (this.to != null && cmp.compare(key, this.to) >= 0) throw new IllegalArgumentException("Key: " + key + " outside of this sub-map bounds");
/*     */     
/* 126 */     return (V)target().put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 132 */     Equality<? super K> cmp = keyComparator();
/* 133 */     if (this.from != null && cmp.compare(key, this.from) < 0) return null; 
/* 134 */     if (this.to != null && cmp.compare(key, this.to) >= 0) return null; 
/* 135 */     return (V)target().remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super V> valueComparator() {
/* 140 */     return target().valueComparator();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/sorted/SubSortedMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */