/*     */ package javolution.context.internal;
/*     */ 
/*     */ import javolution.context.AbstractContext;
/*     */ import javolution.context.ConcurrentContext;
/*     */ import javolution.lang.MathLib;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConcurrentContextImpl
/*     */   extends ConcurrentContext
/*     */ {
/*     */   private int completedCount;
/*     */   private Throwable error;
/*     */   private int initiatedCount;
/*     */   private final ConcurrentContextImpl parent;
/*     */   private ConcurrentThreadImpl[] threads;
/*     */   
/*     */   public ConcurrentContextImpl() {
/*  30 */     this.parent = null;
/*  31 */     int nbThreads = ((Integer)ConcurrentContext.CONCURRENCY.get()).intValue();
/*  32 */     this.threads = new ConcurrentThreadImpl[nbThreads];
/*  33 */     for (int i = 0; i < nbThreads; i++) {
/*  34 */       this.threads[i] = new ConcurrentThreadImpl();
/*  35 */       this.threads[i].start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentContextImpl(ConcurrentContextImpl parent) {
/*  43 */     this.parent = parent;
/*  44 */     this.threads = parent.threads;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void completed(Throwable error) {
/*  49 */     if (error != null) {
/*  50 */       this.error = error;
/*     */     }
/*  52 */     this.completedCount++;
/*  53 */     notify();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable logic) {
/*  59 */     for (ConcurrentThreadImpl thread : this.threads) {
/*  60 */       if (thread.execute(logic, this)) {
/*  61 */         this.initiatedCount++;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     try {
/*  67 */       logic.run();
/*  68 */     } catch (Throwable e) {
/*  69 */       this.error = e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void exit() {
/*  75 */     super.exit();
/*     */     try {
/*  77 */       while (this.initiatedCount != this.completedCount) {
/*  78 */         wait();
/*     */       }
/*  80 */     } catch (InterruptedException ex) {
/*  81 */       this.error = ex;
/*     */     } 
/*  83 */     if (this.error == null)
/*     */       return; 
/*  85 */     if (this.error instanceof RuntimeException)
/*  86 */       throw (RuntimeException)this.error; 
/*  87 */     if (this.error instanceof Error)
/*  88 */       throw (Error)this.error; 
/*  89 */     throw new RuntimeException(this.error);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getConcurrency() {
/*  94 */     return this.threads.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConcurrency(int concurrency) {
/* 101 */     int nbThreads = MathLib.min(this.parent.threads.length, concurrency);
/* 102 */     this.threads = new ConcurrentThreadImpl[nbThreads];
/* 103 */     for (int i = 0; i < nbThreads; i++) {
/* 104 */       this.threads[i] = this.parent.threads[i];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected ConcurrentContext inner() {
/* 110 */     return new ConcurrentContextImpl(this);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/internal/ConcurrentContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */