/*    */ package javolution.context.internal;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import javax.realtime.RealtimeThread;
/*    */ import javolution.context.AbstractContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConcurrentThreadImpl
/*    */   extends RealtimeThread
/*    */ {
/*    */   private static int count;
/*    */   private ConcurrentContextImpl context;
/* 24 */   private AtomicBoolean isBusy = new AtomicBoolean();
/*    */   
/*    */   private Runnable logic;
/*    */   
/*    */   private int priority;
/*    */ 
/*    */   
/*    */   public ConcurrentThreadImpl() {
/* 32 */     setName("ConcurrentThread-" + ++count);
/* 33 */     setDaemon(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean execute(Runnable logic, ConcurrentContextImpl inContext) {
/* 41 */     if (!this.isBusy.compareAndSet(false, true))
/* 42 */       return false; 
/* 43 */     synchronized (this) {
/* 44 */       this.priority = Thread.currentThread().getPriority();
/* 45 */       this.context = inContext;
/* 46 */       this.logic = logic;
/* 47 */       notify();
/*    */     } 
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     while (true) {
/*    */       try {
/* 56 */         synchronized (this) {
/* 57 */           for (; this.logic == null; wait());
/*    */         } 
/* 59 */         setPriority(this.priority);
/* 60 */         AbstractContext.inherit((AbstractContext)this.context);
/* 61 */         this.logic.run();
/* 62 */         this.context.completed(null);
/* 63 */       } catch (Throwable error) {
/* 64 */         this.context.completed(error);
/*    */       } 
/*    */       
/* 67 */       this.logic = null;
/* 68 */       this.context = null;
/* 69 */       AbstractContext.inherit(null);
/* 70 */       this.isBusy.set(false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/internal/ConcurrentThreadImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */