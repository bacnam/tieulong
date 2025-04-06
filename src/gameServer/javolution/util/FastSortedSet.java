/*     */ package javolution.util;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.SortedSet;
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.util.function.Equalities;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.map.sorted.FastSortedMapImpl;
/*     */ import javolution.util.internal.set.sorted.AtomicSortedSetImpl;
/*     */ import javolution.util.internal.set.sorted.SharedSortedSetImpl;
/*     */ import javolution.util.internal.set.sorted.UnmodifiableSortedSetImpl;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.SetService;
/*     */ import javolution.util.service.SortedSetService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastSortedSet<E>
/*     */   extends FastSet<E>
/*     */   implements SortedSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public FastSortedSet() {
/*  38 */     this(Equalities.STANDARD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedSet(Equality<? super E> comparator) {
/*  45 */     super((SetService<E>)(new FastSortedMapImpl(comparator, Equalities.IDENTITY)).keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FastSortedSet(SortedSetService<E> service) {
/*  53 */     super((SetService<E>)service);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedSet<E> atomic() {
/*  62 */     return new FastSortedSet((SortedSetService<E>)new AtomicSortedSetImpl(service()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedSet<E> shared() {
/*  67 */     return new FastSortedSet((SortedSetService<E>)new SharedSortedSetImpl((SetService)service()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedSet<E> unmodifiable() {
/*  72 */     return new FastSortedSet((SortedSetService<E>)new UnmodifiableSortedSetImpl((SetService)service()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean add(E e) {
/*  82 */     return super.add(e);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean contains(Object obj) {
/*  88 */     return super.contains(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean remove(Object obj) {
/*  94 */     return super.remove(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public FastSortedSet<E> subSet(E fromElement, E toElement) {
/* 105 */     return new FastSortedSet(service().subSet(fromElement, toElement));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public FastSortedSet<E> headSet(E toElement) {
/* 112 */     return subSet(first(), toElement);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public FastSortedSet<E> tailSet(E fromElement) {
/* 119 */     return subSet(fromElement, last());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E first() {
/* 125 */     return (E)service().first();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E last() {
/* 131 */     return (E)service().last();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedSet<E> addAll(E... elements) {
/* 140 */     return (FastSortedSet<E>)super.addAll(elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedSet<E> addAll(FastCollection<? extends E> that) {
/* 145 */     return (FastSortedSet<E>)super.addAll(that);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedSetService<E> service() {
/* 150 */     return (SortedSetService<E>)super.service();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/FastSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */