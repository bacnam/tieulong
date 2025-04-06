/*     */ package javolution.osgi.internal;
/*     */ 
/*     */ import javolution.util.FastTable;
/*     */ import org.osgi.framework.ServiceReference;
/*     */ import org.osgi.service.log.LogService;
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
/*     */ public final class LogServiceImpl
/*     */   extends Thread
/*     */   implements LogService
/*     */ {
/*     */   private static class LogEvent
/*     */   {
/*     */     Throwable exception;
/*     */     int level;
/*     */     String message;
/*     */     
/*     */     private LogEvent() {}
/*     */   }
/*  28 */   private final FastTable<LogEvent> eventQueue = new FastTable();
/*     */   
/*     */   public LogServiceImpl() {
/*  31 */     super("Logging-Thread");
/*  32 */     setDaemon(true);
/*  33 */     start();
/*  34 */     Thread hook = new Thread(new Runnable()
/*     */         {
/*     */           public void run() {
/*  37 */             synchronized (LogServiceImpl.this.eventQueue) { while (true) {
/*     */                 try {
/*  39 */                   if (!LogServiceImpl.this.eventQueue.isEmpty())
/*  40 */                   { LogServiceImpl.this.eventQueue.wait(); continue; } 
/*  41 */                 } catch (InterruptedException e) {}
/*     */                 break;
/*     */               }  }
/*     */              } });
/*  45 */     Runtime.getRuntime().addShutdownHook(hook);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(int level, String message) {
/*  50 */     log(level, message, (Throwable)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(int level, String message, Throwable exception) {
/*  55 */     LogEvent event = new LogEvent();
/*  56 */     event.level = level;
/*  57 */     event.message = message;
/*  58 */     event.exception = exception;
/*  59 */     synchronized (this.eventQueue) {
/*  60 */       this.eventQueue.addFirst(event);
/*  61 */       this.eventQueue.notify();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(ServiceReference sr, int level, String message) {
/*  68 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(ServiceReference sr, int level, String message, Throwable exception) {
/*  75 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*     */       try {
/*     */         LogEvent event;
/*  83 */         synchronized (this.eventQueue) {
/*  84 */           while (this.eventQueue.isEmpty())
/*  85 */             this.eventQueue.wait(); 
/*  86 */           event = (LogEvent)this.eventQueue.pollLast();
/*  87 */           this.eventQueue.notify();
/*     */         } 
/*  89 */         switch (event.level) {
/*     */           case 4:
/*  91 */             System.out.println("[DEBUG] " + event.message);
/*     */             break;
/*     */           case 3:
/*  94 */             System.out.println("[INFO] " + event.message);
/*     */             break;
/*     */           case 2:
/*  97 */             System.out.println("[WARNING] " + event.message);
/*     */             break;
/*     */           case 1:
/* 100 */             System.out.println("[ERROR] " + event.message);
/*     */             break;
/*     */           default:
/* 103 */             System.out.println("[UNKNOWN] " + event.message);
/*     */             break;
/*     */         } 
/* 106 */         if (event.exception != null) {
/* 107 */           event.exception.printStackTrace(System.out);
/*     */         }
/* 109 */       } catch (InterruptedException error) {
/* 110 */         error.printStackTrace(System.err);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/osgi/internal/LogServiceImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */