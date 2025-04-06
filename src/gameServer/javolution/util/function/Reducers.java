/*     */ package javolution.util.function;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javolution.lang.Parallelizable;
/*     */ import javolution.lang.Realtime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Reducers
/*     */ {
/*     */   @Parallelizable
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public static <E> Reducer<E> any(Class<? extends E> type) {
/*  43 */     return new AnyReducer<E>(type);
/*     */   }
/*     */   
/*     */   private static class AnyReducer<E> implements Reducer<E> {
/*     */     private final Class<? extends E> type;
/*     */     private volatile E found;
/*     */     
/*     */     public AnyReducer(Class<? extends E> type) {
/*  51 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Collection<E> param) {
/*  56 */       Iterator<E> it = param.iterator();
/*  57 */       while (it.hasNext() && this.found == null) {
/*  58 */         E e = it.next();
/*  59 */         if (this.type.isInstance(e)) {
/*  60 */           this.found = e;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public E get() {
/*  68 */       return this.found;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Parallelizable(mutexFree = true, comment = "Internal use of AtomicReference")
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public static <E> Reducer<E> max(Comparator<? super E> comparator) {
/*  81 */     return new MaxReducer<E>(comparator);
/*     */   }
/*     */   
/*     */   private static class MaxReducer<E> implements Reducer<E> {
/*     */     private final Comparator<? super E> cmp;
/*  86 */     private final AtomicReference<E> max = new AtomicReference<E>(null);
/*     */     
/*     */     public MaxReducer(Comparator<? super E> cmp) {
/*  89 */       this.cmp = cmp;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Collection<E> param) {
/*  94 */       Iterator<E> it = param.iterator();
/*  95 */       label15: while (it.hasNext()) {
/*  96 */         E e = it.next();
/*  97 */         E read = this.max.get(); while (true) {
/*  98 */           if (read == null || this.cmp.compare(e, read) > 0) {
/*  99 */             if (this.max.compareAndSet(read, e))
/* 100 */               continue label15;  read = this.max.get();
/*     */             continue;
/*     */           } 
/*     */           continue label15;
/*     */         } 
/*     */       } 
/*     */     } public E get() {
/* 107 */       return this.max.get();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Parallelizable(mutexFree = true, comment = "Internal use of AtomicReference")
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public static <E> Reducer<E> min(Comparator<? super E> comparator) {
/* 118 */     return new MinReducer<E>(comparator);
/*     */   }
/*     */   
/*     */   private static class MinReducer<E> implements Reducer<E> {
/*     */     private final Comparator<? super E> cmp;
/* 123 */     private final AtomicReference<E> min = new AtomicReference<E>(null);
/*     */     
/*     */     public MinReducer(Comparator<? super E> cmp) {
/* 126 */       this.cmp = cmp;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Collection<E> param) {
/* 131 */       Iterator<E> it = param.iterator();
/* 132 */       label15: while (it.hasNext()) {
/* 133 */         E e = it.next();
/* 134 */         E read = this.min.get(); while (true) {
/* 135 */           if (read == null || this.cmp.compare(e, read) < 0) {
/* 136 */             if (this.min.compareAndSet(read, e))
/* 137 */               continue label15;  read = this.min.get();
/*     */             continue;
/*     */           } 
/*     */           continue label15;
/*     */         } 
/*     */       } 
/*     */     } public E get() {
/* 144 */       return this.min.get();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Parallelizable
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public static Reducer<Boolean> and() {
/* 156 */     return new AndReducer();
/*     */   }
/*     */   
/*     */   private static class AndReducer
/*     */     implements Reducer<Boolean> {
/*     */     volatile boolean result = true;
/*     */     
/*     */     public void accept(Collection<Boolean> param) {
/* 164 */       Iterator<Boolean> it = param.iterator();
/* 165 */       while (this.result && it.hasNext()) {
/* 166 */         if (!((Boolean)it.next()).booleanValue()) this.result = false;
/*     */       
/*     */       } 
/*     */     }
/*     */     
/*     */     public Boolean get() {
/* 172 */       return Boolean.valueOf(this.result);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private AndReducer() {}
/*     */   }
/*     */ 
/*     */   
/*     */   @Parallelizable
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public static Reducer<Boolean> or() {
/* 184 */     return new OrReducer();
/*     */   }
/*     */   
/*     */   private static class OrReducer
/*     */     implements Reducer<Boolean> {
/*     */     volatile boolean result = false;
/*     */     
/*     */     public void accept(Collection<Boolean> param) {
/* 192 */       Iterator<Boolean> it = param.iterator();
/* 193 */       while (!this.result && it.hasNext()) {
/* 194 */         if (!((Boolean)it.next()).booleanValue()) this.result = true;
/*     */       
/*     */       } 
/*     */     }
/*     */     
/*     */     public Boolean get() {
/* 200 */       return Boolean.valueOf(this.result);
/*     */     }
/*     */ 
/*     */     
/*     */     private OrReducer() {}
/*     */   }
/*     */ 
/*     */   
/*     */   @Parallelizable(comment = "Internal use of AtomicInteger")
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public static Reducer<Integer> sum() {
/* 211 */     return new SumReducer();
/*     */   }
/*     */   
/*     */   private static class SumReducer implements Reducer<Integer> {
/* 215 */     private final AtomicInteger sum = new AtomicInteger(0);
/*     */ 
/*     */     
/*     */     public void accept(Collection<Integer> param) {
/* 219 */       Iterator<Integer> it = param.iterator();
/* 220 */       while (it.hasNext()) {
/* 221 */         this.sum.getAndAdd(((Integer)it.next()).intValue());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer get() {
/* 227 */       return Integer.valueOf(this.sum.get());
/*     */     }
/*     */     
/*     */     private SumReducer() {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/function/Reducers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */