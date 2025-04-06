/*     */ package javolution.util.internal.table;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.collection.CollectionView;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.TableService;
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
/*     */ public abstract class TableView<E>
/*     */   extends CollectionView<E>
/*     */   implements TableService<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public TableView(TableService<E> target) {
/*  33 */     super((CollectionService)target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> c) {
/*  41 */     return subList(index, index).addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFirst(E element) {
/*  46 */     add(0, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLast(E element) {
/*  51 */     add(size(), element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean contains(Object o) {
/*  59 */     return (indexOf(o) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/*  64 */     return (new ReversedTableImpl<E>(this)).iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public final E element() {
/*  69 */     return getFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E getFirst() {
/*  77 */     if (size() == 0) emptyError(); 
/*  78 */     return get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public E getLast() {
/*  83 */     if (size() == 0) emptyError(); 
/*  84 */     return get(size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/*  90 */     Equality<Object> cmp = comparator();
/*  91 */     for (int i = 0, n = size(); i < n; i++) {
/*  92 */       if (cmp.areEqual(o, get(i))) return i; 
/*     */     } 
/*  94 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/*  99 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 104 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 110 */     Equality<Object> cmp = comparator();
/* 111 */     for (int i = size() - 1; i >= 0; i--) {
/* 112 */       if (cmp.areEqual(o, get(i))) return i; 
/*     */     } 
/* 114 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ListIterator<E> listIterator() {
/* 119 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 124 */     return new TableIteratorImpl<E>(this, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean offer(E e) {
/* 129 */     return offerLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean offerFirst(E e) {
/* 134 */     addFirst(e);
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean offerLast(E e) {
/* 140 */     addLast(e);
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final E peek() {
/* 146 */     return peekFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/* 151 */     return (size() == 0) ? null : getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekLast() {
/* 156 */     return (size() == 0) ? null : getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public final E poll() {
/* 161 */     return pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/* 166 */     return (size() == 0) ? null : removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast() {
/* 171 */     return (size() == 0) ? null : removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public final E pop() {
/* 176 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void push(E e) {
/* 181 */     addFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public final E remove() {
/* 186 */     return removeFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean remove(Object o) {
/* 194 */     int i = indexOf(o);
/* 195 */     if (i < 0) return false; 
/* 196 */     remove(i);
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public E removeFirst() {
/* 202 */     if (size() == 0) emptyError(); 
/* 203 */     return remove(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 208 */     int i = indexOf(o);
/* 209 */     if (i < 0) return false; 
/* 210 */     remove(i);
/* 211 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public E removeLast() {
/* 216 */     if (size() == 0) emptyError(); 
/* 217 */     return remove(size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeLastOccurrence(Object o) {
/* 222 */     int i = lastIndexOf(o);
/* 223 */     if (i < 0) return false; 
/* 224 */     remove(i);
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionService<E>[] split(int n) {
/* 236 */     return SubTableImpl.splitOf(this, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public TableService<E> subList(int fromIndex, int toIndex) {
/* 241 */     return new SubTableImpl<E>(this, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public TableService<E> threadSafe() {
/* 246 */     return new SharedTableImpl<E>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void emptyError() {
/* 251 */     throw new NoSuchElementException("Empty Table");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void indexError(int index) {
/* 256 */     throw new IndexOutOfBoundsException("index: " + index + ", size: " + size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TableService<E> target() {
/* 263 */     return (TableService<E>)super.target();
/*     */   }
/*     */   
/*     */   public abstract void add(int paramInt, E paramE);
/*     */   
/*     */   public abstract void clear();
/*     */   
/*     */   public abstract E get(int paramInt);
/*     */   
/*     */   public abstract E remove(int paramInt);
/*     */   
/*     */   public abstract E set(int paramInt, E paramE);
/*     */   
/*     */   public abstract int size();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/TableView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */