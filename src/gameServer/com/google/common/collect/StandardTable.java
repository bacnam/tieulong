/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.base.Supplier;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ class StandardTable<R, C, V>
/*      */   implements Table<R, C, V>, Serializable
/*      */ {
/*      */   final Map<R, Map<C, V>> backingMap;
/*      */   final Supplier<? extends Map<C, V>> factory;
/*      */   private transient CellSet cellSet;
/*      */   private transient RowKeySet rowKeySet;
/*      */   private transient Set<C> columnKeySet;
/*      */   private transient Values values;
/*      */   private transient RowMap rowMap;
/*      */   private transient ColumnMap columnMap;
/*      */   private static final long serialVersionUID = 0L;
/*      */   
/*      */   StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*   70 */     this.backingMap = backingMap;
/*   71 */     this.factory = factory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/*   78 */     if (rowKey == null || columnKey == null) {
/*   79 */       return false;
/*      */     }
/*   81 */     Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
/*   82 */     return (map != null && Maps.safeContainsKey(map, columnKey));
/*      */   }
/*      */   
/*      */   public boolean containsColumn(@Nullable Object columnKey) {
/*   86 */     if (columnKey == null) {
/*   87 */       return false;
/*      */     }
/*   89 */     for (Map<C, V> map : this.backingMap.values()) {
/*   90 */       if (Maps.safeContainsKey(map, columnKey)) {
/*   91 */         return true;
/*      */       }
/*      */     } 
/*   94 */     return false;
/*      */   }
/*      */   
/*      */   public boolean containsRow(@Nullable Object rowKey) {
/*   98 */     return (rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey));
/*      */   }
/*      */   
/*      */   public boolean containsValue(@Nullable Object value) {
/*  102 */     if (value == null) {
/*  103 */       return false;
/*      */     }
/*  105 */     for (Map<C, V> map : this.backingMap.values()) {
/*  106 */       if (map.containsValue(value)) {
/*  107 */         return true;
/*      */       }
/*      */     } 
/*  110 */     return false;
/*      */   }
/*      */   
/*      */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/*  114 */     if (rowKey == null || columnKey == null) {
/*  115 */       return null;
/*      */     }
/*  117 */     Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
/*  118 */     return (map == null) ? null : Maps.<V>safeGet(map, columnKey);
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  122 */     return this.backingMap.isEmpty();
/*      */   }
/*      */   
/*      */   public int size() {
/*  126 */     int size = 0;
/*  127 */     for (Map<C, V> map : this.backingMap.values()) {
/*  128 */       size += map.size();
/*      */     }
/*  130 */     return size;
/*      */   }
/*      */   
/*      */   public boolean equals(@Nullable Object obj) {
/*  134 */     if (obj == this) {
/*  135 */       return true;
/*      */     }
/*  137 */     if (obj instanceof Table) {
/*  138 */       Table<?, ?, ?> other = (Table<?, ?, ?>)obj;
/*  139 */       return cellSet().equals(other.cellSet());
/*      */     } 
/*  141 */     return false;
/*      */   }
/*      */   
/*      */   public int hashCode() {
/*  145 */     return cellSet().hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  152 */     return rowMap().toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  158 */     this.backingMap.clear();
/*      */   }
/*      */   
/*      */   private Map<C, V> getOrCreate(R rowKey) {
/*  162 */     Map<C, V> map = this.backingMap.get(rowKey);
/*  163 */     if (map == null) {
/*  164 */       map = (Map<C, V>)this.factory.get();
/*  165 */       this.backingMap.put(rowKey, map);
/*      */     } 
/*  167 */     return map;
/*      */   }
/*      */   
/*      */   public V put(R rowKey, C columnKey, V value) {
/*  171 */     Preconditions.checkNotNull(rowKey);
/*  172 */     Preconditions.checkNotNull(columnKey);
/*  173 */     Preconditions.checkNotNull(value);
/*  174 */     return getOrCreate(rowKey).put(columnKey, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/*  179 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/*  180 */       put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/*  186 */     if (rowKey == null || columnKey == null) {
/*  187 */       return null;
/*      */     }
/*  189 */     Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
/*  190 */     if (map == null) {
/*  191 */       return null;
/*      */     }
/*  193 */     V value = map.remove(columnKey);
/*  194 */     if (map.isEmpty()) {
/*  195 */       this.backingMap.remove(rowKey);
/*      */     }
/*  197 */     return value;
/*      */   }
/*      */   
/*      */   private Map<R, V> removeColumn(Object column) {
/*  201 */     Map<R, V> output = new LinkedHashMap<R, V>();
/*  202 */     Iterator<Map.Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();
/*      */     
/*  204 */     while (iterator.hasNext()) {
/*  205 */       Map.Entry<R, Map<C, V>> entry = iterator.next();
/*  206 */       V value = (V)((Map)entry.getValue()).remove(column);
/*  207 */       if (value != null) {
/*  208 */         output.put(entry.getKey(), value);
/*  209 */         if (((Map)entry.getValue()).isEmpty()) {
/*  210 */           iterator.remove();
/*      */         }
/*      */       } 
/*      */     } 
/*  214 */     return output;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
/*  219 */     return (value != null && value.equals(get(rowKey, columnKey)));
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
/*  224 */     if (containsMapping(rowKey, columnKey, value)) {
/*  225 */       remove(rowKey, columnKey);
/*  226 */       return true;
/*      */     } 
/*  228 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private abstract class TableCollection<T>
/*      */     extends AbstractCollection<T>
/*      */   {
/*      */     private TableCollection() {}
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  239 */       return StandardTable.this.backingMap.isEmpty();
/*      */     }
/*      */     
/*      */     public void clear() {
/*  243 */       StandardTable.this.backingMap.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private abstract class TableSet<T>
/*      */     extends AbstractSet<T>
/*      */   {
/*      */     private TableSet() {}
/*      */     
/*      */     public boolean isEmpty() {
/*  253 */       return StandardTable.this.backingMap.isEmpty();
/*      */     }
/*      */     
/*      */     public void clear() {
/*  257 */       StandardTable.this.backingMap.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Table.Cell<R, C, V>> cellSet() {
/*  274 */     CellSet result = this.cellSet;
/*  275 */     return (result == null) ? (this.cellSet = new CellSet()) : result;
/*      */   }
/*      */   private class CellSet extends TableSet<Table.Cell<R, C, V>> { private CellSet() {}
/*      */     
/*      */     public Iterator<Table.Cell<R, C, V>> iterator() {
/*  280 */       return new StandardTable.CellIterator();
/*      */     }
/*      */     
/*      */     public int size() {
/*  284 */       return StandardTable.this.size();
/*      */     }
/*      */     
/*      */     public boolean contains(Object obj) {
/*  288 */       if (obj instanceof Table.Cell) {
/*  289 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)obj;
/*  290 */         return StandardTable.this.containsMapping(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*      */       } 
/*      */       
/*  293 */       return false;
/*      */     }
/*      */     
/*      */     public boolean remove(Object obj) {
/*  297 */       if (obj instanceof Table.Cell) {
/*  298 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)obj;
/*  299 */         return StandardTable.this.removeMapping(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*      */       } 
/*      */       
/*  302 */       return false;
/*      */     } }
/*      */ 
/*      */   
/*      */   private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
/*  307 */     final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator();
/*      */     
/*      */     Map.Entry<R, Map<C, V>> rowEntry;
/*  310 */     Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  314 */       return (this.rowIterator.hasNext() || this.columnIterator.hasNext());
/*      */     }
/*      */     
/*      */     public Table.Cell<R, C, V> next() {
/*  318 */       if (!this.columnIterator.hasNext()) {
/*  319 */         this.rowEntry = this.rowIterator.next();
/*  320 */         this.columnIterator = ((Map<C, V>)this.rowEntry.getValue()).entrySet().iterator();
/*      */       } 
/*  322 */       Map.Entry<C, V> columnEntry = this.columnIterator.next();
/*  323 */       return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  328 */       this.columnIterator.remove();
/*  329 */       if (((Map)this.rowEntry.getValue()).isEmpty())
/*  330 */         this.rowIterator.remove(); 
/*      */     }
/*      */     
/*      */     private CellIterator() {} }
/*      */   
/*      */   public Map<C, V> row(R rowKey) {
/*  336 */     return new Row(rowKey);
/*      */   }
/*      */   
/*      */   class Row extends AbstractMap<C, V> { final R rowKey;
/*      */     Map<C, V> backingRowMap;
/*      */     
/*      */     Row(R rowKey) {
/*  343 */       this.rowKey = (R)Preconditions.checkNotNull(rowKey);
/*      */     }
/*      */     Set<C> keySet;
/*      */     Set<Map.Entry<C, V>> entrySet;
/*      */     
/*      */     Map<C, V> backingRowMap() {
/*  349 */       return (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) ? (this.backingRowMap = computeBackingRowMap()) : this.backingRowMap;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Map<C, V> computeBackingRowMap() {
/*  356 */       return (Map<C, V>)StandardTable.this.backingMap.get(this.rowKey);
/*      */     }
/*      */ 
/*      */     
/*      */     void maintainEmptyInvariant() {
/*  361 */       if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
/*  362 */         StandardTable.this.backingMap.remove(this.rowKey);
/*  363 */         this.backingRowMap = null;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  369 */       Map<C, V> backingRowMap = backingRowMap();
/*  370 */       return (key != null && backingRowMap != null && Maps.safeContainsKey(backingRowMap, key));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/*  376 */       Map<C, V> backingRowMap = backingRowMap();
/*  377 */       return (key != null && backingRowMap != null) ? Maps.<V>safeGet(backingRowMap, key) : null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V put(C key, V value) {
/*  384 */       Preconditions.checkNotNull(key);
/*  385 */       Preconditions.checkNotNull(value);
/*  386 */       if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
/*  387 */         return this.backingRowMap.put(key, value);
/*      */       }
/*  389 */       return StandardTable.this.put(this.rowKey, key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/*      */       try {
/*  395 */         Map<C, V> backingRowMap = backingRowMap();
/*  396 */         if (backingRowMap == null) {
/*  397 */           return null;
/*      */         }
/*  399 */         V result = backingRowMap.remove(key);
/*  400 */         maintainEmptyInvariant();
/*  401 */         return result;
/*  402 */       } catch (ClassCastException e) {
/*  403 */         return null;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  409 */       Map<C, V> backingRowMap = backingRowMap();
/*  410 */       if (backingRowMap != null) {
/*  411 */         backingRowMap.clear();
/*      */       }
/*  413 */       maintainEmptyInvariant();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<C> keySet() {
/*  420 */       Set<C> result = this.keySet;
/*  421 */       if (result == null) {
/*  422 */         return this.keySet = new Maps.KeySet<C, V>()
/*      */           {
/*      */             Map<C, V> map() {
/*  425 */               return StandardTable.Row.this;
/*      */             }
/*      */           };
/*      */       }
/*  429 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<C, V>> entrySet() {
/*  436 */       Set<Map.Entry<C, V>> result = this.entrySet;
/*  437 */       if (result == null) {
/*  438 */         return this.entrySet = new RowEntrySet();
/*      */       }
/*  440 */       return result;
/*      */     }
/*      */     
/*      */     private class RowEntrySet extends Maps.EntrySet<C, V> { private RowEntrySet() {}
/*      */       
/*      */       Map<C, V> map() {
/*  446 */         return StandardTable.Row.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/*  451 */         Map<C, V> map = StandardTable.Row.this.backingRowMap();
/*  452 */         return (map == null) ? 0 : map.size();
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<C, V>> iterator() {
/*  457 */         Map<C, V> map = StandardTable.Row.this.backingRowMap();
/*  458 */         if (map == null) {
/*  459 */           return Iterators.emptyModifiableIterator();
/*      */         }
/*  461 */         final Iterator<Map.Entry<C, V>> iterator = map.entrySet().iterator();
/*  462 */         return new Iterator<Map.Entry<C, V>>() {
/*      */             public boolean hasNext() {
/*  464 */               return iterator.hasNext();
/*      */             }
/*      */             public Map.Entry<C, V> next() {
/*  467 */               final Map.Entry<C, V> entry = iterator.next();
/*  468 */               return new ForwardingMapEntry<C, V>() {
/*      */                   protected Map.Entry<C, V> delegate() {
/*  470 */                     return entry;
/*      */                   }
/*      */                   public V setValue(V value) {
/*  473 */                     return super.setValue((V)Preconditions.checkNotNull(value));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   public boolean equals(Object object) {
/*  478 */                     return standardEquals(object);
/*      */                   }
/*      */                 };
/*      */             }
/*      */ 
/*      */             
/*      */             public void remove() {
/*  485 */               iterator.remove();
/*  486 */               StandardTable.Row.this.maintainEmptyInvariant();
/*      */             }
/*      */           };
/*      */       } }
/*      */      }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<R, V> column(C columnKey) {
/*  500 */     return new Column(columnKey);
/*      */   }
/*      */   private class Column extends Maps.ImprovedAbstractMap<R, V> { final C columnKey;
/*      */     Values columnValues;
/*      */     KeySet keySet;
/*      */     
/*      */     Column(C columnKey) {
/*  507 */       this.columnKey = (C)Preconditions.checkNotNull(columnKey);
/*      */     }
/*      */     
/*      */     public V put(R key, V value) {
/*  511 */       return StandardTable.this.put(key, this.columnKey, value);
/*      */     }
/*      */     
/*      */     public V get(Object key) {
/*  515 */       return (V)StandardTable.this.get(key, this.columnKey);
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  519 */       return StandardTable.this.contains(key, this.columnKey);
/*      */     }
/*      */     
/*      */     public V remove(Object key) {
/*  523 */       return (V)StandardTable.this.remove(key, this.columnKey);
/*      */     }
/*      */     
/*      */     public Set<Map.Entry<R, V>> createEntrySet() {
/*  527 */       return new EntrySet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  533 */       Values result = this.columnValues;
/*  534 */       return (result == null) ? (this.columnValues = new Values()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean removePredicate(Predicate<? super Map.Entry<R, V>> predicate) {
/*  542 */       boolean changed = false;
/*  543 */       Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*      */       
/*  545 */       while (iterator.hasNext()) {
/*  546 */         Map.Entry<R, Map<C, V>> entry = iterator.next();
/*  547 */         Map<C, V> map = entry.getValue();
/*  548 */         V value = map.get(this.columnKey);
/*  549 */         if (value != null && predicate.apply(new ImmutableEntry<Object, V>(entry.getKey(), value))) {
/*      */ 
/*      */           
/*  552 */           map.remove(this.columnKey);
/*  553 */           changed = true;
/*  554 */           if (map.isEmpty()) {
/*  555 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*  559 */       return changed;
/*      */     }
/*      */     
/*      */     class EntrySet extends AbstractSet<Map.Entry<R, V>> {
/*      */       public Iterator<Map.Entry<R, V>> iterator() {
/*  564 */         return new StandardTable.Column.EntrySetIterator();
/*      */       }
/*      */       
/*      */       public int size() {
/*  568 */         int size = 0;
/*  569 */         for (Map<C, V> map : (Iterable<Map<C, V>>)StandardTable.this.backingMap.values()) {
/*  570 */           if (map.containsKey(StandardTable.Column.this.columnKey)) {
/*  571 */             size++;
/*      */           }
/*      */         } 
/*  574 */         return size;
/*      */       }
/*      */       
/*      */       public boolean isEmpty() {
/*  578 */         return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
/*      */       }
/*      */       
/*      */       public void clear() {
/*  582 */         Predicate<Map.Entry<R, V>> predicate = Predicates.alwaysTrue();
/*  583 */         StandardTable.Column.this.removePredicate(predicate);
/*      */       }
/*      */       
/*      */       public boolean contains(Object o) {
/*  587 */         if (o instanceof Map.Entry) {
/*  588 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  589 */           return StandardTable.this.containsMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*      */         } 
/*  591 */         return false;
/*      */       }
/*      */       
/*      */       public boolean remove(Object obj) {
/*  595 */         if (obj instanceof Map.Entry) {
/*  596 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  597 */           return StandardTable.this.removeMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*      */         } 
/*  599 */         return false;
/*      */       }
/*      */       
/*      */       public boolean removeAll(Collection<?> c) {
/*  603 */         boolean changed = false;
/*  604 */         for (Object obj : c) {
/*  605 */           changed |= remove(obj);
/*      */         }
/*  607 */         return changed;
/*      */       }
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/*  611 */         return StandardTable.Column.this.removePredicate(Predicates.not(Predicates.in(c)));
/*      */       }
/*      */     }
/*      */     
/*      */     class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
/*  616 */       final Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*      */       
/*      */       protected Map.Entry<R, V> computeNext() {
/*  619 */         while (this.iterator.hasNext()) {
/*  620 */           final Map.Entry<R, Map<C, V>> entry = this.iterator.next();
/*  621 */           if (((Map)entry.getValue()).containsKey(StandardTable.Column.this.columnKey)) {
/*  622 */             return new AbstractMapEntry<R, V>() {
/*      */                 public R getKey() {
/*  624 */                   return (R)entry.getKey();
/*      */                 }
/*      */                 public V getValue() {
/*  627 */                   return (V)((Map)entry.getValue()).get(StandardTable.Column.this.columnKey);
/*      */                 }
/*      */                 public V setValue(V value) {
/*  630 */                   return ((Map<C, V>)entry.getValue()).put(StandardTable.Column.this.columnKey, (V)Preconditions.checkNotNull(value));
/*      */                 }
/*      */               };
/*      */           }
/*      */         } 
/*  635 */         return endOfData();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<R> keySet() {
/*  642 */       KeySet result = this.keySet;
/*  643 */       return (result == null) ? (this.keySet = new KeySet()) : result;
/*      */     }
/*      */     
/*      */     class KeySet extends AbstractSet<R> {
/*      */       public Iterator<R> iterator() {
/*  648 */         return StandardTable.keyIteratorImpl(StandardTable.Column.this);
/*      */       }
/*      */       
/*      */       public int size() {
/*  652 */         return StandardTable.Column.this.entrySet().size();
/*      */       }
/*      */       
/*      */       public boolean isEmpty() {
/*  656 */         return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
/*      */       }
/*      */       
/*      */       public boolean contains(Object obj) {
/*  660 */         return StandardTable.this.contains(obj, StandardTable.Column.this.columnKey);
/*      */       }
/*      */       
/*      */       public boolean remove(Object obj) {
/*  664 */         return (StandardTable.this.remove(obj, StandardTable.Column.this.columnKey) != null);
/*      */       }
/*      */       
/*      */       public void clear() {
/*  668 */         StandardTable.Column.this.entrySet().clear();
/*      */       }
/*      */       
/*      */       public boolean removeAll(Collection<?> c) {
/*  672 */         boolean changed = false;
/*  673 */         for (Object obj : c) {
/*  674 */           changed |= remove(obj);
/*      */         }
/*  676 */         return changed;
/*      */       }
/*      */       
/*      */       public boolean retainAll(final Collection<?> c) {
/*  680 */         Preconditions.checkNotNull(c);
/*  681 */         Predicate<Map.Entry<R, V>> predicate = new Predicate<Map.Entry<R, V>>()
/*      */           {
/*      */             public boolean apply(Map.Entry<R, V> entry) {
/*  684 */               return !c.contains(entry.getKey());
/*      */             }
/*      */           };
/*  687 */         return StandardTable.Column.this.removePredicate(predicate);
/*      */       }
/*      */     }
/*      */     
/*      */     class Values extends AbstractCollection<V> {
/*      */       public Iterator<V> iterator() {
/*  693 */         return StandardTable.valueIteratorImpl(StandardTable.Column.this);
/*      */       }
/*      */       
/*      */       public int size() {
/*  697 */         return StandardTable.Column.this.entrySet().size();
/*      */       }
/*      */       
/*      */       public boolean isEmpty() {
/*  701 */         return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
/*      */       }
/*      */       
/*      */       public void clear() {
/*  705 */         StandardTable.Column.this.entrySet().clear();
/*      */       }
/*      */       
/*      */       public boolean remove(Object obj) {
/*  709 */         if (obj == null) {
/*  710 */           return false;
/*      */         }
/*  712 */         Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/*  713 */         while (iterator.hasNext()) {
/*  714 */           Map<C, V> map = iterator.next();
/*  715 */           if (map.entrySet().remove(new ImmutableEntry<C, Object>(StandardTable.Column.this.columnKey, obj))) {
/*      */             
/*  717 */             if (map.isEmpty()) {
/*  718 */               iterator.remove();
/*      */             }
/*  720 */             return true;
/*      */           } 
/*      */         } 
/*  723 */         return false;
/*      */       }
/*      */       
/*      */       public boolean removeAll(final Collection<?> c) {
/*  727 */         Preconditions.checkNotNull(c);
/*  728 */         Predicate<Map.Entry<R, V>> predicate = new Predicate<Map.Entry<R, V>>()
/*      */           {
/*      */             public boolean apply(Map.Entry<R, V> entry) {
/*  731 */               return c.contains(entry.getValue());
/*      */             }
/*      */           };
/*  734 */         return StandardTable.Column.this.removePredicate(predicate);
/*      */       }
/*      */       
/*      */       public boolean retainAll(final Collection<?> c) {
/*  738 */         Preconditions.checkNotNull(c);
/*  739 */         Predicate<Map.Entry<R, V>> predicate = new Predicate<Map.Entry<R, V>>()
/*      */           {
/*      */             public boolean apply(Map.Entry<R, V> entry) {
/*  742 */               return !c.contains(entry.getValue());
/*      */             }
/*      */           };
/*  745 */         return StandardTable.Column.this.removePredicate(predicate);
/*      */       }
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<R> rowKeySet() {
/*  753 */     Set<R> result = this.rowKeySet;
/*  754 */     return (result == null) ? (this.rowKeySet = new RowKeySet()) : result;
/*      */   }
/*      */   
/*      */   class RowKeySet extends TableSet<R> {
/*      */     public Iterator<R> iterator() {
/*  759 */       return StandardTable.keyIteratorImpl(StandardTable.this.rowMap());
/*      */     }
/*      */     
/*      */     public int size() {
/*  763 */       return StandardTable.this.backingMap.size();
/*      */     }
/*      */     
/*      */     public boolean contains(Object obj) {
/*  767 */       return StandardTable.this.containsRow(obj);
/*      */     }
/*      */     
/*      */     public boolean remove(Object obj) {
/*  771 */       return (obj != null && StandardTable.this.backingMap.remove(obj) != null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<C> columnKeySet() {
/*  788 */     Set<C> result = this.columnKeySet;
/*  789 */     return (result == null) ? (this.columnKeySet = new ColumnKeySet()) : result;
/*      */   }
/*      */   private class ColumnKeySet extends TableSet<C> { private ColumnKeySet() {}
/*      */     
/*      */     public Iterator<C> iterator() {
/*  794 */       return StandardTable.this.createColumnKeyIterator();
/*      */     }
/*      */     
/*      */     public int size() {
/*  798 */       return Iterators.size(iterator());
/*      */     }
/*      */     
/*      */     public boolean remove(Object obj) {
/*  802 */       if (obj == null) {
/*  803 */         return false;
/*      */       }
/*  805 */       boolean changed = false;
/*  806 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/*  807 */       while (iterator.hasNext()) {
/*  808 */         Map<C, V> map = iterator.next();
/*  809 */         if (map.keySet().remove(obj)) {
/*  810 */           changed = true;
/*  811 */           if (map.isEmpty()) {
/*  812 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*  816 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  820 */       Preconditions.checkNotNull(c);
/*  821 */       boolean changed = false;
/*  822 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/*  823 */       while (iterator.hasNext()) {
/*  824 */         Map<C, V> map = iterator.next();
/*      */ 
/*      */         
/*  827 */         if (Iterators.removeAll(map.keySet().iterator(), c)) {
/*  828 */           changed = true;
/*  829 */           if (map.isEmpty()) {
/*  830 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*  834 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  838 */       Preconditions.checkNotNull(c);
/*  839 */       boolean changed = false;
/*  840 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/*  841 */       while (iterator.hasNext()) {
/*  842 */         Map<C, V> map = iterator.next();
/*  843 */         if (map.keySet().retainAll(c)) {
/*  844 */           changed = true;
/*  845 */           if (map.isEmpty()) {
/*  846 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*  850 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean contains(Object obj) {
/*  854 */       if (obj == null) {
/*  855 */         return false;
/*      */       }
/*  857 */       for (Map<C, V> map : (Iterable<Map<C, V>>)StandardTable.this.backingMap.values()) {
/*  858 */         if (map.containsKey(obj)) {
/*  859 */           return true;
/*      */         }
/*      */       } 
/*  862 */       return false;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Iterator<C> createColumnKeyIterator() {
/*  871 */     return new ColumnKeyIterator();
/*      */   }
/*      */   
/*      */   private class ColumnKeyIterator
/*      */     extends AbstractIterator<C>
/*      */   {
/*  877 */     final Map<C, V> seen = (Map<C, V>)StandardTable.this.factory.get();
/*  878 */     final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
/*  879 */     Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();
/*      */     
/*      */     protected C computeNext() {
/*      */       while (true) {
/*  883 */         while (this.entryIterator.hasNext()) {
/*  884 */           Map.Entry<C, V> entry = this.entryIterator.next();
/*  885 */           if (!this.seen.containsKey(entry.getKey())) {
/*  886 */             this.seen.put(entry.getKey(), entry.getValue());
/*  887 */             return entry.getKey();
/*      */           } 
/*  889 */         }  if (this.mapIterator.hasNext()) {
/*  890 */           this.entryIterator = ((Map<C, V>)this.mapIterator.next()).entrySet().iterator(); continue;
/*      */         }  break;
/*  892 */       }  return endOfData();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ColumnKeyIterator() {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/*  907 */     Values result = this.values;
/*  908 */     return (result == null) ? (this.values = new Values()) : result;
/*      */   }
/*      */   private class Values extends TableCollection<V> { private Values() {}
/*      */     
/*      */     public Iterator<V> iterator() {
/*  913 */       final Iterator<Table.Cell<R, C, V>> cellIterator = StandardTable.this.cellSet().iterator();
/*  914 */       return new Iterator<V>() {
/*      */           public boolean hasNext() {
/*  916 */             return cellIterator.hasNext();
/*      */           }
/*      */           public V next() {
/*  919 */             return (V)((Table.Cell)cellIterator.next()).getValue();
/*      */           }
/*      */           public void remove() {
/*  922 */             cellIterator.remove();
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     public int size() {
/*  928 */       return StandardTable.this.size();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<R, Map<C, V>> rowMap() {
/*  935 */     RowMap result = this.rowMap;
/*  936 */     return (result == null) ? (this.rowMap = new RowMap()) : result;
/*      */   }
/*      */   
/*      */   class RowMap extends Maps.ImprovedAbstractMap<R, Map<C, V>> {
/*      */     public boolean containsKey(Object key) {
/*  941 */       return StandardTable.this.containsRow(key);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<C, V> get(Object key) {
/*  947 */       return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null;
/*      */     }
/*      */     
/*      */     public Set<R> keySet() {
/*  951 */       return StandardTable.this.rowKeySet();
/*      */     }
/*      */     
/*      */     public Map<C, V> remove(Object key) {
/*  955 */       return (key == null) ? null : (Map<C, V>)StandardTable.this.backingMap.remove(key);
/*      */     }
/*      */     
/*      */     protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
/*  959 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     class EntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>> {
/*      */       public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
/*  964 */         return new StandardTable.RowMap.EntryIterator();
/*      */       }
/*      */       
/*      */       public int size() {
/*  968 */         return StandardTable.this.backingMap.size();
/*      */       }
/*      */       
/*      */       public boolean contains(Object obj) {
/*  972 */         if (obj instanceof Map.Entry) {
/*  973 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  974 */           return (entry.getKey() != null && entry.getValue() instanceof Map && Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry));
/*      */         } 
/*      */ 
/*      */         
/*  978 */         return false;
/*      */       }
/*      */       
/*      */       public boolean remove(Object obj) {
/*  982 */         if (obj instanceof Map.Entry) {
/*  983 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  984 */           return (entry.getKey() != null && entry.getValue() instanceof Map && StandardTable.this.backingMap.entrySet().remove(entry));
/*      */         } 
/*      */ 
/*      */         
/*  988 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */     class EntryIterator implements Iterator<Map.Entry<R, Map<C, V>>> {
/*  993 */       final Iterator<R> delegate = StandardTable.this.backingMap.keySet().iterator();
/*      */       
/*      */       public boolean hasNext() {
/*  996 */         return this.delegate.hasNext();
/*      */       }
/*      */       
/*      */       public Map.Entry<R, Map<C, V>> next() {
/* 1000 */         R rowKey = this.delegate.next();
/* 1001 */         return new ImmutableEntry<R, Map<C, V>>(rowKey, StandardTable.this.row(rowKey));
/*      */       }
/*      */       
/*      */       public void remove() {
/* 1005 */         this.delegate.remove();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<C, Map<R, V>> columnMap() {
/* 1013 */     ColumnMap result = this.columnMap;
/* 1014 */     return (result == null) ? (this.columnMap = new ColumnMap()) : result;
/*      */   }
/*      */   
/*      */   private class ColumnMap extends Maps.ImprovedAbstractMap<C, Map<R, V>> { ColumnMapValues columnMapValues;
/*      */     
/*      */     private ColumnMap() {}
/*      */     
/*      */     public Map<R, V> get(Object key) {
/* 1022 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null;
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1026 */       return StandardTable.this.containsColumn(key);
/*      */     }
/*      */     
/*      */     public Map<R, V> remove(Object key) {
/* 1030 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null;
/*      */     }
/*      */     
/*      */     public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
/* 1034 */       return new ColumnMapEntrySet();
/*      */     }
/*      */     
/*      */     public Set<C> keySet() {
/* 1038 */       return StandardTable.this.columnKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<Map<R, V>> values() {
/* 1044 */       ColumnMapValues result = this.columnMapValues;
/* 1045 */       return (result == null) ? (this.columnMapValues = new ColumnMapValues()) : result;
/*      */     }
/*      */     
/*      */     class ColumnMapEntrySet
/*      */       extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>> {
/*      */       public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
/* 1051 */         final Iterator<C> columnIterator = StandardTable.this.columnKeySet().iterator();
/* 1052 */         return new UnmodifiableIterator<Map.Entry<C, Map<R, V>>>() {
/*      */             public boolean hasNext() {
/* 1054 */               return columnIterator.hasNext();
/*      */             }
/*      */             public Map.Entry<C, Map<R, V>> next() {
/* 1057 */               C columnKey = columnIterator.next();
/* 1058 */               return new ImmutableEntry<C, Map<R, V>>(columnKey, StandardTable.this.column(columnKey));
/*      */             }
/*      */           };
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/* 1065 */         return StandardTable.this.columnKeySet().size();
/*      */       }
/*      */       
/*      */       public boolean contains(Object obj) {
/* 1069 */         if (obj instanceof Map.Entry) {
/* 1070 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 1071 */           if (StandardTable.this.containsColumn(entry.getKey())) {
/*      */ 
/*      */ 
/*      */             
/* 1075 */             C columnKey = (C)entry.getKey();
/* 1076 */             return StandardTable.ColumnMap.this.get(columnKey).equals(entry.getValue());
/*      */           } 
/*      */         } 
/* 1079 */         return false;
/*      */       }
/*      */       
/*      */       public boolean remove(Object obj) {
/* 1083 */         if (contains(obj)) {
/* 1084 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 1085 */           StandardTable.this.removeColumn(entry.getKey());
/* 1086 */           return true;
/*      */         } 
/* 1088 */         return false;
/*      */       }
/*      */       
/*      */       public boolean removeAll(Collection<?> c) {
/* 1092 */         boolean changed = false;
/* 1093 */         for (Object obj : c) {
/* 1094 */           changed |= remove(obj);
/*      */         }
/* 1096 */         return changed;
/*      */       }
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/* 1100 */         boolean changed = false;
/* 1101 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 1102 */           if (!c.contains(new ImmutableEntry<C, Object>(columnKey, StandardTable.this.column(columnKey)))) {
/*      */             
/* 1104 */             StandardTable.this.removeColumn(columnKey);
/* 1105 */             changed = true;
/*      */           } 
/*      */         } 
/* 1108 */         return changed;
/*      */       } }
/*      */     
/*      */     private class ColumnMapValues extends StandardTable<R, C, V>.TableCollection<Map<R, V>> { private ColumnMapValues() {}
/*      */       
/*      */       public Iterator<Map<R, V>> iterator() {
/* 1114 */         return StandardTable.valueIteratorImpl(StandardTable.ColumnMap.this);
/*      */       }
/*      */       
/*      */       public boolean remove(Object obj) {
/* 1118 */         for (Map.Entry<C, Map<R, V>> entry : StandardTable.ColumnMap.this.entrySet()) {
/* 1119 */           if (((Map)entry.getValue()).equals(obj)) {
/* 1120 */             StandardTable.this.removeColumn(entry.getKey());
/* 1121 */             return true;
/*      */           } 
/*      */         } 
/* 1124 */         return false;
/*      */       }
/*      */       
/*      */       public boolean removeAll(Collection<?> c) {
/* 1128 */         Preconditions.checkNotNull(c);
/* 1129 */         boolean changed = false;
/* 1130 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 1131 */           if (c.contains(StandardTable.this.column(columnKey))) {
/* 1132 */             StandardTable.this.removeColumn(columnKey);
/* 1133 */             changed = true;
/*      */           } 
/*      */         } 
/* 1136 */         return changed;
/*      */       }
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/* 1140 */         Preconditions.checkNotNull(c);
/* 1141 */         boolean changed = false;
/* 1142 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 1143 */           if (!c.contains(StandardTable.this.column(columnKey))) {
/* 1144 */             StandardTable.this.removeColumn(columnKey);
/* 1145 */             changed = true;
/*      */           } 
/*      */         } 
/* 1148 */         return changed;
/*      */       }
/*      */       
/*      */       public int size() {
/* 1152 */         return StandardTable.this.columnKeySet().size();
/*      */       } }
/*      */      }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<K> keyIteratorImpl(Map<K, V> map) {
/* 1166 */     final Iterator<Map.Entry<K, V>> entryIterator = map.entrySet().iterator();
/* 1167 */     return new Iterator<K>() {
/*      */         public boolean hasNext() {
/* 1169 */           return entryIterator.hasNext();
/*      */         }
/*      */         public K next() {
/* 1172 */           return (K)((Map.Entry)entryIterator.next()).getKey();
/*      */         }
/*      */         public void remove() {
/* 1175 */           entryIterator.remove();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<V> valueIteratorImpl(Map<K, V> map) {
/* 1185 */     final Iterator<Map.Entry<K, V>> entryIterator = map.entrySet().iterator();
/* 1186 */     return new Iterator<V>() {
/*      */         public boolean hasNext() {
/* 1188 */           return entryIterator.hasNext();
/*      */         }
/*      */         public V next() {
/* 1191 */           return (V)((Map.Entry)entryIterator.next()).getValue();
/*      */         }
/*      */         public void remove() {
/* 1194 */           entryIterator.remove();
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/collect/StandardTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */