/*     */ package javolution.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import javolution.lang.Immutable;
/*     */ import javolution.lang.Parallelizable;
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.text.Cursor;
/*     */ import javolution.text.DefaultTextFormat;
/*     */ import javolution.text.TextContext;
/*     */ import javolution.text.TextFormat;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.function.Function;
/*     */ import javolution.util.function.Predicate;
/*     */ import javolution.util.function.Reducer;
/*     */ import javolution.util.function.Reducers;
/*     */ import javolution.util.internal.collection.AtomicCollectionImpl;
/*     */ import javolution.util.internal.collection.DistinctCollectionImpl;
/*     */ import javolution.util.internal.collection.FilteredCollectionImpl;
/*     */ import javolution.util.internal.collection.MappedCollectionImpl;
/*     */ import javolution.util.internal.collection.ParallelCollectionImpl;
/*     */ import javolution.util.internal.collection.ReversedCollectionImpl;
/*     */ import javolution.util.internal.collection.SequentialCollectionImpl;
/*     */ import javolution.util.internal.collection.SharedCollectionImpl;
/*     */ import javolution.util.internal.collection.SortedCollectionImpl;
/*     */ import javolution.util.internal.collection.UnmodifiableCollectionImpl;
/*     */ import javolution.util.service.CollectionService;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Realtime
/*     */ @DefaultTextFormat(FastCollection.Format.class)
/*     */ public abstract class FastCollection<E>
/*     */   implements Collection<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   @Parallelizable(mutexFree = true, comment = "Except for write operations, all read operations are mutex-free.")
/*     */   public FastCollection<E> atomic() {
/* 188 */     return (FastCollection<E>)new AtomicCollectionImpl(service());
/*     */   }
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
/*     */   @Parallelizable(mutexFree = false, comment = "Use multiple-readers/single-writer lock.")
/*     */   public FastCollection<E> shared() {
/* 202 */     return (FastCollection<E>)new SharedCollectionImpl(service());
/*     */   }
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
/*     */   public FastCollection<E> parallel() {
/* 223 */     return (FastCollection<E>)new ParallelCollectionImpl(service());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastCollection<E> sequential() {
/* 231 */     return (FastCollection<E>)new SequentialCollectionImpl(service());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastCollection<E> unmodifiable() {
/* 240 */     return (FastCollection<E>)new UnmodifiableCollectionImpl(service());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastCollection<E> filtered(Predicate<? super E> filter) {
/* 251 */     return (FastCollection<E>)new FilteredCollectionImpl(service(), filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <R> FastCollection<R> mapped(Function<? super E, ? extends R> function) {
/* 260 */     return (FastCollection<R>)new MappedCollectionImpl(service(), function);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastCollection<E> sorted() {
/* 268 */     return (FastCollection<E>)new SortedCollectionImpl(service(), (Comparator)comparator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastCollection<E> sorted(Comparator<? super E> cmp) {
/* 276 */     return (FastCollection<E>)new SortedCollectionImpl(service(), cmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastCollection<E> reversed() {
/* 283 */     return (FastCollection<E>)new ReversedCollectionImpl(service());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastCollection<E> distinct() {
/* 294 */     return (FastCollection<E>)new DistinctCollectionImpl(service());
/*     */   }
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void perform(Consumer<? extends Collection<E>> action) {
/* 316 */     service().perform(action, service());
/*     */   }
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void update(Consumer<? extends Collection<E>> action) {
/* 335 */     service().update(action, service());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void forEach(final Consumer<? super E> consumer) {
/* 347 */     perform(new Consumer<Collection<E>>() {
/*     */           public void accept(Collection<E> view) {
/* 349 */             Iterator<E> it = view.iterator();
/* 350 */             while (it.hasNext()) {
/* 351 */               consumer.accept(it.next());
/*     */             }
/*     */           }
/*     */         });
/*     */   }
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public boolean removeIf(final Predicate<? super E> filter) {
/* 369 */     final boolean[] removed = new boolean[1];
/* 370 */     update(new Consumer<Collection<E>>() {
/*     */           public void accept(Collection<E> view) {
/* 372 */             Iterator<E> it = view.iterator();
/* 373 */             while (it.hasNext()) {
/* 374 */               if (filter.test(it.next())) {
/* 375 */                 it.remove();
/* 376 */                 removed[0] = true;
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/* 381 */     return removed[0];
/*     */   }
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public E reduce(Reducer<E> reducer) {
/* 397 */     perform((Consumer)reducer);
/* 398 */     return (E)reducer.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR, comment = "May have to search the whole collection (e.g. distinct view).")
/*     */   public boolean add(E element) {
/* 409 */     return service().add(element);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR, comment = "May actually iterates the whole collection (e.g. filtered view).")
/*     */   public boolean isEmpty() {
/* 416 */     return iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR, comment = "Potentially counts the elements.")
/*     */   public int size() {
/* 423 */     return service().size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR, comment = "Potentially removes elements one at a time.")
/*     */   public void clear() {
/* 430 */     service().clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR, comment = "Potentially searches the whole collection.")
/*     */   public boolean contains(Object searched) {
/* 437 */     return service().contains(searched);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR, comment = "Potentially searches the whole collection.")
/*     */   public boolean remove(Object searched) {
/* 444 */     return service().remove(searched);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.N_SQUARE, comment = "Construction of the iterator may require sorting the elements (e.g. sorted view)")
/*     */   public Iterator<E> iterator() {
/* 456 */     return service().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public boolean addAll(Collection<? extends E> that) {
/* 463 */     return service().addAll(that);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.N_SQUARE)
/*     */   public boolean containsAll(Collection<?> that) {
/* 470 */     return service().containsAll(that);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.N_SQUARE)
/*     */   public boolean removeAll(Collection<?> that) {
/* 477 */     return service().removeAll(that);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.N_SQUARE)
/*     */   public boolean retainAll(Collection<?> that) {
/* 484 */     return service().retainAll(that);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public Object[] toArray() {
/* 491 */     return service().toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public <T> T[] toArray(T[] array) {
/* 499 */     return (T[])service().toArray((Object[])array);
/*     */   }
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public <T extends E> T any(Class<T> type) {
/* 516 */     return (T)reduce(Reducers.any(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public E min() {
/* 529 */     return reduce(Reducers.min((Comparator)comparator()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public E max() {
/* 542 */     return reduce(Reducers.max((Comparator)comparator()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public FastCollection<E> addAll(E... elements) {
/* 553 */     for (E e : elements) {
/* 554 */       add(e);
/*     */     }
/* 556 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public FastCollection<E> addAll(FastCollection<? extends E> that) {
/* 565 */     addAll(that);
/* 566 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public Equality<? super E> comparator() {
/* 575 */     return service().comparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public <T extends Collection<E>> Immutable<T> toImmutable() {
/* 586 */     return new Immutable<T>() {
/* 587 */         final T value = (T)FastCollection.this.unmodifiable();
/*     */ 
/*     */ 
/*     */         
/*     */         public T value() {
/* 592 */           return this.value;
/*     */         }
/*     */       };
/*     */   }
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public boolean equals(Object obj) {
/* 612 */     return service().equals(obj);
/*     */   }
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public int hashCode() {
/* 626 */     return service().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public String toString() {
/* 635 */     return TextContext.getFormat(FastCollection.class).format(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract CollectionService<E> service();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static <E> CollectionService<E> serviceOf(FastCollection<E> collection) {
/* 649 */     return collection.service();
/*     */   }
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
/*     */   @Parallelizable
/*     */   public static class Format
/*     */     extends TextFormat<FastCollection<?>>
/*     */   {
/*     */     public FastCollection<Object> parse(CharSequence csq, Cursor cursor) throws IllegalArgumentException {
/* 668 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Appendable format(FastCollection<?> that, Appendable dest) throws IOException {
/* 674 */       dest.append('[');
/* 675 */       Class<?> elementType = null;
/* 676 */       TextFormat<Object> format = null;
/* 677 */       for (Object element : that) {
/* 678 */         if (elementType == null) { elementType = Void.class; }
/* 679 */         else { dest.append(", "); }
/* 680 */          if (element == null) {
/* 681 */           dest.append("null");
/*     */           continue;
/*     */         } 
/* 684 */         Class<?> cls = element.getClass();
/* 685 */         if (elementType.equals(cls)) {
/* 686 */           format.format(element, dest);
/*     */           continue;
/*     */         } 
/* 689 */         elementType = cls;
/* 690 */         format = TextContext.getFormat(cls);
/* 691 */         format.format(element, dest);
/*     */       } 
/* 693 */       return dest.append(']');
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/FastCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */