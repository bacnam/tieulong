/*     */ package javolution.util.internal.collection;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javolution.context.ConcurrentContext;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equality;
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
/*     */ public class ParallelCollectionImpl<E>
/*     */   extends CollectionView<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public ParallelCollectionImpl(CollectionService<E> target) {
/*  26 */     super(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E e) {
/*  31 */     return target().add(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  36 */     target().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super E> comparator() {
/*  41 */     return target().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object obj) {
/*  46 */     return target().contains(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  51 */     return target().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/*  56 */     return target().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void perform(final Consumer<CollectionService<E>> action, CollectionService<E> view) {
/*  62 */     ConcurrentContext ctx = ConcurrentContext.enter();
/*     */     try {
/*  64 */       int concurrency = ctx.getConcurrency();
/*  65 */       CollectionService[] arrayOfCollectionService = (CollectionService[])view.split(concurrency + 1);
/*  66 */       for (int i = 1; i < arrayOfCollectionService.length; i++) {
/*  67 */         final CollectionService<E> subView = arrayOfCollectionService[i];
/*  68 */         ctx.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/*  71 */                 ParallelCollectionImpl.this.target().perform(action, subView);
/*     */               }
/*     */             });
/*     */       } 
/*  75 */       target().perform(action, arrayOfCollectionService[0]);
/*     */     } finally {
/*     */       
/*  78 */       ctx.exit();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object obj) {
/*  84 */     return target().remove(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  89 */     return target().size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(final Consumer<CollectionService<E>> action, CollectionService<E> view) {
/*  95 */     ConcurrentContext ctx = ConcurrentContext.enter();
/*     */     try {
/*  97 */       int concurrency = ctx.getConcurrency();
/*  98 */       CollectionService[] arrayOfCollectionService = (CollectionService[])view.threadSafe().split(concurrency + 1);
/*     */       
/* 100 */       for (int i = 1; i < arrayOfCollectionService.length; i++) {
/* 101 */         final CollectionService<E> subView = arrayOfCollectionService[i];
/* 102 */         ctx.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 105 */                 ParallelCollectionImpl.this.target().update(action, subView);
/*     */               }
/*     */             });
/*     */       } 
/* 109 */       target().perform(action, arrayOfCollectionService[0]);
/*     */     } finally {
/*     */       
/* 112 */       ctx.exit();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/ParallelCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */