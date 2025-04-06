/*     */ package javolution.util;
/*     */ 
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.util.function.Equalities;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.table.sorted.AtomicSortedTableImpl;
/*     */ import javolution.util.internal.table.sorted.FastSortedTableImpl;
/*     */ import javolution.util.internal.table.sorted.SharedSortedTableImpl;
/*     */ import javolution.util.internal.table.sorted.UnmodifiableSortedTableImpl;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.SortedTableService;
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
/*     */ public class FastSortedTable<E>
/*     */   extends FastTable<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public FastSortedTable() {
/*  41 */     this(Equalities.STANDARD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedTable(Equality<? super E> comparator) {
/*  48 */     super((TableService<E>)new FastSortedTableImpl(comparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FastSortedTable(SortedTableService<E> service) {
/*  55 */     super((TableService<E>)service);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedTable<E> atomic() {
/*  64 */     return new FastSortedTable((SortedTableService<E>)new AtomicSortedTableImpl((TableService)service()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedTable<E> shared() {
/*  69 */     return new FastSortedTable((SortedTableService<E>)new SharedSortedTableImpl(service()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedTable<E> unmodifiable() {
/*  74 */     return new FastSortedTable((SortedTableService<E>)new UnmodifiableSortedTableImpl(service()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean contains(Object obj) {
/*  84 */     return service().contains(obj);
/*     */   }
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean remove(Object obj) {
/*  89 */     return service().remove(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public int indexOf(Object obj) {
/*  95 */     return service().indexOf(obj);
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
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean addIfAbsent(E element) {
/* 110 */     return service().addIfAbsent(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public int positionOf(E element) {
/* 119 */     return service().positionOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedTable<E> addAll(E... elements) {
/* 124 */     return (FastSortedTable<E>)super.addAll(elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedTable<E> addAll(FastCollection<? extends E> that) {
/* 129 */     return (FastSortedTable<E>)super.addAll(that);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedTableService<E> service() {
/* 134 */     return (SortedTableService<E>)super.service();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/FastSortedTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */