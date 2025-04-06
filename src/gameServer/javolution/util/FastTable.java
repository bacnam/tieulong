/*     */ package javolution.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equalities;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.table.AtomicTableImpl;
/*     */ import javolution.util.internal.table.FastTableImpl;
/*     */ import javolution.util.internal.table.QuickSort;
/*     */ import javolution.util.internal.table.ReversedTableImpl;
/*     */ import javolution.util.internal.table.SharedTableImpl;
/*     */ import javolution.util.internal.table.SubTableImpl;
/*     */ import javolution.util.internal.table.UnmodifiableTableImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastTable<E>
/*     */   extends FastCollection<E>
/*     */   implements List<E>, Deque<E>, RandomAccess
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private final TableService<E> service;
/*     */   
/*     */   public FastTable() {
/* 102 */     this(Equalities.STANDARD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastTable(Equality<? super E> comparator) {
/* 110 */     this.service = (TableService<E>)new FastTableImpl(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FastTable(TableService<E> service) {
/* 117 */     this.service = service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastTable<E> atomic() {
/* 126 */     return new FastTable((TableService<E>)new AtomicTableImpl(this.service));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastTable<E> reversed() {
/* 131 */     return new FastTable((TableService<E>)new ReversedTableImpl(this.service));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastTable<E> shared() {
/* 136 */     return new FastTable((TableService<E>)new SharedTableImpl(this.service));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastTable<E> unmodifiable() {
/* 141 */     return new FastTable((TableService<E>)new UnmodifiableTableImpl(this.service));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastTable<E> subTable(int fromIndex, int toIndex) {
/* 149 */     return new FastTable((TableService<E>)new SubTableImpl(this.service, fromIndex, toIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean isEmpty() {
/* 160 */     return this.service.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public int size() {
/* 166 */     return this.service.size();
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public void clear() {
/* 172 */     this.service.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public void add(int index, E element) {
/* 183 */     this.service.add(index, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.N_LOG_N)
/*     */   public boolean addAll(int index, Collection<? extends E> elements) {
/* 191 */     return this.service.addAll(index, elements);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public E remove(int index) {
/* 198 */     return (E)this.service.remove(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E get(int index) {
/* 205 */     return (E)this.service.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E set(int index, E element) {
/* 212 */     return (E)this.service.set(index, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public int indexOf(Object element) {
/* 220 */     return this.service.indexOf(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public int lastIndexOf(Object element) {
/* 228 */     return this.service.lastIndexOf(element);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public ListIterator<E> listIterator() {
/* 235 */     return this.service.listIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public ListIterator<E> listIterator(int index) {
/* 243 */     return this.service.listIterator(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public void addFirst(E element) {
/* 254 */     this.service.addFirst(element);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public void addLast(E element) {
/* 261 */     this.service.addLast(element);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E getFirst() {
/* 268 */     return (E)this.service.getFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E getLast() {
/* 275 */     return (E)this.service.getLast();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E peekFirst() {
/* 283 */     return (E)this.service.peekFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E peekLast() {
/* 291 */     return (E)this.service.peekLast();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E pollFirst() {
/* 299 */     return (E)this.service.pollFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E pollLast() {
/* 306 */     return (E)this.service.pollLast();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E removeFirst() {
/* 314 */     return (E)this.service.removeFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E removeLast() {
/* 321 */     return (E)this.service.removeLast();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean offerFirst(E e) {
/* 328 */     return this.service.offerFirst(e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean offerLast(E e) {
/* 335 */     return this.service.offerLast(e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 342 */     return this.service.removeFirstOccurrence(o);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public boolean removeLastOccurrence(Object o) {
/* 349 */     return this.service.removeLastOccurrence(o);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean offer(E e) {
/* 356 */     return this.service.offer(e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E remove() {
/* 363 */     return (E)this.service.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E poll() {
/* 370 */     return (E)this.service.poll();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E element() {
/* 377 */     return (E)this.service.element();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E peek() {
/* 384 */     return (E)this.service.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public void push(E e) {
/* 391 */     this.service.push(e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public E pop() {
/* 398 */     return (E)this.service.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public Iterator<E> descendingIterator() {
/* 405 */     return this.service.descendingIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.N_SQUARE)
/*     */   public void sort() {
/* 417 */     update((Consumer)new Consumer<TableService<E>>()
/*     */         {
/*     */           public void accept(TableService<E> table) {
/* 420 */             QuickSort<E> qs = new QuickSort(table, (Comparator)table.comparator());
/* 421 */             qs.sort();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public FastTable<E> addAll(E... elements) {
/* 429 */     return (FastTable<E>)super.addAll(elements);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public FastTable<E> addAll(FastCollection<? extends E> that) {
/* 435 */     return (FastTable<E>)super.addAll(that);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public FastTable<E> subList(int fromIndex, int toIndex) {
/* 445 */     return subTable(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TableService<E> service() {
/* 450 */     return this.service;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/FastTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */