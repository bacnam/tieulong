/*     */ package javolution.util.internal.table;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import javolution.util.internal.collection.AtomicCollectionImpl;
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
/*     */ public class AtomicTableImpl<E>
/*     */   extends AtomicCollectionImpl<E>
/*     */   implements TableService<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public AtomicTableImpl(TableService<E> target) {
/*  28 */     super((CollectionService)target);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void add(int index, E element) {
/*  33 */     target().add(index, element);
/*  34 */     if (!updateInProgress()) this.immutable = cloneTarget();
/*     */   
/*     */   }
/*     */   
/*     */   public synchronized boolean addAll(int index, Collection<? extends E> c) {
/*  39 */     boolean changed = target().addAll(index, c);
/*  40 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/*  41 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void addFirst(E element) {
/*  46 */     target().addFirst(element);
/*  47 */     if (!updateInProgress()) this.immutable = cloneTarget();
/*     */   
/*     */   }
/*     */   
/*     */   public synchronized void addLast(E element) {
/*  52 */     target().addLast(element);
/*  53 */     if (!updateInProgress()) this.immutable = cloneTarget();
/*     */   
/*     */   }
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/*  58 */     return (new ReversedTableImpl<E>(this)).iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public E element() {
/*  63 */     return getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  68 */     return (E)targetView().get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E getFirst() {
/*  73 */     return (E)targetView().getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E getLast() {
/*  78 */     return (E)targetView().getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object element) {
/*  83 */     return targetView().indexOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> iterator() {
/*  88 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object element) {
/*  93 */     return targetView().lastIndexOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/*  98 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 103 */     return new TableIteratorImpl<E>(this, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(E e) {
/* 108 */     return offerLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean offerFirst(E e) {
/* 113 */     boolean changed = target().offerFirst(e);
/* 114 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 115 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean offerLast(E e) {
/* 120 */     boolean changed = target().offerLast(e);
/* 121 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 122 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/* 127 */     return peekFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/* 132 */     return (E)targetView().peekFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekLast() {
/* 137 */     return (E)targetView().peekLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public E poll() {
/* 142 */     return pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E pollFirst() {
/* 147 */     E e = (E)target().pollFirst();
/* 148 */     if (e != null && !updateInProgress()) this.immutable = cloneTarget(); 
/* 149 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E pollLast() {
/* 154 */     E e = (E)target().pollLast();
/* 155 */     if (e != null && !updateInProgress()) this.immutable = cloneTarget(); 
/* 156 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public E pop() {
/* 161 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(E e) {
/* 166 */     addFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove() {
/* 171 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E remove(int index) {
/* 176 */     E e = (E)target().remove(index);
/* 177 */     if (!updateInProgress()) this.immutable = cloneTarget(); 
/* 178 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E removeFirst() {
/* 183 */     E e = (E)target().removeFirst();
/* 184 */     if (!updateInProgress()) this.immutable = cloneTarget(); 
/* 185 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean removeFirstOccurrence(Object o) {
/* 190 */     boolean changed = target().removeFirstOccurrence(o);
/* 191 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 192 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E removeLast() {
/* 197 */     E e = (E)target().removeLast();
/* 198 */     if (!updateInProgress()) this.immutable = cloneTarget(); 
/* 199 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean removeLastOccurrence(Object o) {
/* 204 */     boolean changed = target().removeLastOccurrence(o);
/* 205 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 206 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E set(int index, E element) {
/* 211 */     E e = (E)target().set(index, element);
/* 212 */     if (!updateInProgress()) this.immutable = cloneTarget(); 
/* 213 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public CollectionService<E>[] split(int n) {
/* 218 */     return SubTableImpl.splitOf(this, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public TableService<E> subList(int fromIndex, int toIndex) {
/* 223 */     return new SubTableImpl<E>(this, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public TableService<E> threadSafe() {
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected TableService<E> targetView() {
/* 233 */     return (TableService<E>)super.targetView();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TableService<E> target() {
/* 239 */     return (TableService<E>)super.target();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/AtomicTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */