/*     */ package javolution.util;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.util.function.Equalities;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.function.Predicate;
/*     */ import javolution.util.internal.map.FastMapImpl;
/*     */ import javolution.util.internal.set.AtomicSetImpl;
/*     */ import javolution.util.internal.set.FilteredSetImpl;
/*     */ import javolution.util.internal.set.SharedSetImpl;
/*     */ import javolution.util.internal.set.UnmodifiableSetImpl;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.SetService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastSet<E>
/*     */   extends FastCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private final SetService<E> service;
/*     */   
/*     */   public FastSet() {
/*  51 */     this(Equalities.STANDARD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSet(Equality<? super E> comparator) {
/*  59 */     this.service = (new FastMapImpl(comparator, Equalities.IDENTITY)).keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FastSet(SetService<E> service) {
/*  67 */     this.service = service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSet<E> atomic() {
/*  76 */     return new FastSet((SetService<E>)new AtomicSetImpl(service()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSet<E> filtered(Predicate<? super E> filter) {
/*  81 */     return new FastSet((SetService<E>)new FilteredSetImpl(service(), filter));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSet<E> shared() {
/*  86 */     return new FastSet((SetService<E>)new SharedSetImpl(service()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSet<E> unmodifiable() {
/*  91 */     return new FastSet((SetService<E>)new UnmodifiableSetImpl(service()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean isEmpty() {
/* 101 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public int size() {
/* 107 */     return this.service.size();
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public void clear() {
/* 113 */     this.service.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean contains(Object obj) {
/* 119 */     return this.service.contains(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean remove(Object obj) {
/* 125 */     return this.service.remove(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSet<E> addAll(E... elements) {
/* 134 */     return (FastSet<E>)super.addAll(elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSet<E> addAll(FastCollection<? extends E> that) {
/* 139 */     return (FastSet<E>)super.addAll(that);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SetService<E> service() {
/* 144 */     return this.service;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/FastSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */