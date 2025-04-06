/*     */ package javolution.util.internal.map;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javolution.context.ConcurrentContext;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.service.MapService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParallelMapImpl<K, V>
/*     */   extends MapView<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public ParallelMapImpl(MapService<K, V> target) {
/*  26 */     super(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  31 */     return target().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  36 */     return (V)target().get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<K, V>> iterator() {
/*  41 */     return target().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super K> keyComparator() {
/*  46 */     return target().keyComparator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void perform(final Consumer<MapService<K, V>> action, MapService<K, V> view) {
/*  52 */     ConcurrentContext ctx = ConcurrentContext.enter();
/*     */     try {
/*  54 */       int concurrency = ctx.getConcurrency();
/*  55 */       MapService[] arrayOfMapService = (MapService[])view.split(concurrency + 1);
/*  56 */       for (int i = 1; i < arrayOfMapService.length; i++) {
/*  57 */         final MapService<K, V> subView = arrayOfMapService[i];
/*  58 */         ctx.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/*  61 */                 ParallelMapImpl.this.target().perform(action, subView);
/*     */               }
/*     */             });
/*     */       } 
/*  65 */       target().perform(action, arrayOfMapService[0]);
/*     */     } finally {
/*     */       
/*  68 */       ctx.exit();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/*  74 */     return (V)target().put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/*  79 */     return (V)target().remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(final Consumer<MapService<K, V>> action, MapService<K, V> view) {
/*  85 */     ConcurrentContext ctx = ConcurrentContext.enter();
/*     */     try {
/*  87 */       int concurrency = ctx.getConcurrency();
/*  88 */       MapService[] arrayOfMapService = (MapService[])view.threadSafe().split(concurrency + 1);
/*     */       
/*  90 */       for (int i = 1; i < arrayOfMapService.length; i++) {
/*  91 */         final MapService<K, V> subView = arrayOfMapService[i];
/*  92 */         ctx.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/*  95 */                 ParallelMapImpl.this.target().update(action, subView);
/*     */               }
/*     */             });
/*     */       } 
/*  99 */       target().perform(action, arrayOfMapService[0]);
/*     */     } finally {
/*     */       
/* 102 */       ctx.exit();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super V> valueComparator() {
/* 108 */     return target().valueComparator();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/ParallelMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */