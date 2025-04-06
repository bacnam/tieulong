/*     */ package javolution.util.internal.table;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import javolution.util.internal.collection.SharedCollectionImpl;
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
/*     */ public class SharedTableImpl<E>
/*     */   extends SharedCollectionImpl<E>
/*     */   implements TableService<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public SharedTableImpl(TableService<E> target) {
/*  28 */     super((CollectionService)target);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, E element) {
/*  33 */     this.lock.writeLock.lock();
/*     */     try {
/*  35 */       target().add(index, element);
/*     */     } finally {
/*  37 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> c) {
/*  43 */     this.lock.writeLock.lock();
/*     */     try {
/*  45 */       return target().addAll(index, c);
/*     */     } finally {
/*  47 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFirst(E element) {
/*  53 */     this.lock.writeLock.lock();
/*     */     try {
/*  55 */       target().addFirst(element);
/*     */     } finally {
/*  57 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLast(E element) {
/*  63 */     this.lock.writeLock.lock();
/*     */     try {
/*  65 */       target().addLast(element);
/*     */     } finally {
/*  67 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/*  73 */     return (new ReversedTableImpl<E>(this)).iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public E element() {
/*  78 */     return getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  83 */     this.lock.readLock.lock();
/*     */     try {
/*  85 */       return (E)target().get(index);
/*     */     } finally {
/*  87 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E getFirst() {
/*  93 */     this.lock.readLock.lock();
/*     */     try {
/*  95 */       return (E)target().getFirst();
/*     */     } finally {
/*  97 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E getLast() {
/* 103 */     this.lock.readLock.lock();
/*     */     try {
/* 105 */       return (E)target().getLast();
/*     */     } finally {
/* 107 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object element) {
/* 113 */     this.lock.readLock.lock();
/*     */     try {
/* 115 */       return target().indexOf(element);
/*     */     } finally {
/* 117 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> iterator() {
/* 123 */     return target().listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object element) {
/* 128 */     this.lock.readLock.lock();
/*     */     try {
/* 130 */       return target().lastIndexOf(element);
/*     */     } finally {
/* 132 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 138 */     return target().listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 143 */     return new TableIteratorImpl<E>(this, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(E e) {
/* 148 */     return offerLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerFirst(E e) {
/* 153 */     this.lock.writeLock.lock();
/*     */     try {
/* 155 */       return target().offerFirst(e);
/*     */     } finally {
/* 157 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerLast(E e) {
/* 163 */     this.lock.writeLock.lock();
/*     */     try {
/* 165 */       return target().offerLast(e);
/*     */     } finally {
/* 167 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/* 173 */     return peekFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/* 178 */     this.lock.readLock.lock();
/*     */     try {
/* 180 */       return (E)target().peekFirst();
/*     */     } finally {
/* 182 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekLast() {
/* 188 */     this.lock.readLock.lock();
/*     */     try {
/* 190 */       return (E)target().peekLast();
/*     */     } finally {
/* 192 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E poll() {
/* 198 */     return pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/* 203 */     this.lock.writeLock.lock();
/*     */     try {
/* 205 */       return (E)target().pollFirst();
/*     */     } finally {
/* 207 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast() {
/* 213 */     this.lock.writeLock.lock();
/*     */     try {
/* 215 */       return (E)target().pollLast();
/*     */     } finally {
/* 217 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E pop() {
/* 223 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(E e) {
/* 228 */     addFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove() {
/* 233 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove(int index) {
/* 238 */     this.lock.writeLock.lock();
/*     */     try {
/* 240 */       return (E)target().remove(index);
/*     */     } finally {
/* 242 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E removeFirst() {
/* 248 */     this.lock.writeLock.lock();
/*     */     try {
/* 250 */       return (E)target().removeFirst();
/*     */     } finally {
/* 252 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 258 */     this.lock.writeLock.lock();
/*     */     try {
/* 260 */       return target().removeFirstOccurrence(o);
/*     */     } finally {
/* 262 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E removeLast() {
/* 268 */     this.lock.writeLock.lock();
/*     */     try {
/* 270 */       return (E)target().removeLast();
/*     */     } finally {
/* 272 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeLastOccurrence(Object o) {
/* 278 */     this.lock.writeLock.lock();
/*     */     try {
/* 280 */       return target().removeLastOccurrence(o);
/*     */     } finally {
/* 282 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E set(int index, E element) {
/* 288 */     this.lock.writeLock.lock();
/*     */     try {
/* 290 */       return (E)target().set(index, element);
/*     */     } finally {
/* 292 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CollectionService<E>[] split(int n) {
/* 298 */     return SubTableImpl.splitOf(this, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public TableService<E> subList(int fromIndex, int toIndex) {
/* 303 */     return new SubTableImpl<E>(this, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public TableService<E> threadSafe() {
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TableService<E> target() {
/* 314 */     return (TableService<E>)super.target();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/SharedTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */