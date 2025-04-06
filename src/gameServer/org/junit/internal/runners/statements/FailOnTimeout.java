/*     */ package org.junit.internal.runners.statements;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.junit.runners.model.MultipleFailureException;
/*     */ import org.junit.runners.model.Statement;
/*     */ import org.junit.runners.model.TestTimedOutException;
/*     */ 
/*     */ public class FailOnTimeout
/*     */   extends Statement {
/*     */   private final Statement originalStatement;
/*     */   private final TimeUnit timeUnit;
/*     */   private final long timeout;
/*     */   private final boolean lookForStuckThread;
/*  22 */   private volatile ThreadGroup threadGroup = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/*  30 */     return new Builder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public FailOnTimeout(Statement statement, long timeoutMillis) {
/*  42 */     this(builder().withTimeout(timeoutMillis, TimeUnit.MILLISECONDS), statement);
/*     */   }
/*     */   
/*     */   private FailOnTimeout(Builder builder, Statement statement) {
/*  46 */     this.originalStatement = statement;
/*  47 */     this.timeout = builder.timeout;
/*  48 */     this.timeUnit = builder.unit;
/*  49 */     this.lookForStuckThread = builder.lookForStuckThread;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private boolean lookForStuckThread = false;
/*     */ 
/*     */     
/*  59 */     private long timeout = 0L;
/*  60 */     private TimeUnit unit = TimeUnit.SECONDS;
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
/*     */     public Builder withTimeout(long timeout, TimeUnit unit) {
/*  79 */       if (timeout < 0L) {
/*  80 */         throw new IllegalArgumentException("timeout must be non-negative");
/*     */       }
/*  82 */       if (unit == null) {
/*  83 */         throw new NullPointerException("TimeUnit cannot be null");
/*     */       }
/*  85 */       this.timeout = timeout;
/*  86 */       this.unit = unit;
/*  87 */       return this;
/*     */     }
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
/*     */     public Builder withLookingForStuckThread(boolean enable) {
/* 100 */       this.lookForStuckThread = enable;
/* 101 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FailOnTimeout build(Statement statement) {
/* 111 */       if (statement == null) {
/* 112 */         throw new NullPointerException("statement cannot be null");
/*     */       }
/* 114 */       return new FailOnTimeout(this, statement);
/*     */     }
/*     */     
/*     */     private Builder() {} }
/*     */   
/*     */   public void evaluate() throws Throwable {
/* 120 */     CallableStatement callable = new CallableStatement();
/* 121 */     FutureTask<Throwable> task = new FutureTask<Throwable>(callable);
/* 122 */     this.threadGroup = new ThreadGroup("FailOnTimeoutGroup");
/* 123 */     Thread thread = new Thread(this.threadGroup, task, "Time-limited test");
/* 124 */     thread.setDaemon(true);
/* 125 */     thread.start();
/* 126 */     callable.awaitStarted();
/* 127 */     Throwable throwable = getResult(task, thread);
/* 128 */     if (throwable != null) {
/* 129 */       throw throwable;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Throwable getResult(FutureTask<Throwable> task, Thread thread) {
/*     */     try {
/* 140 */       if (this.timeout > 0L) {
/* 141 */         return task.get(this.timeout, this.timeUnit);
/*     */       }
/* 143 */       return task.get();
/*     */     }
/* 145 */     catch (InterruptedException e) {
/* 146 */       return e;
/* 147 */     } catch (ExecutionException e) {
/*     */       
/* 149 */       return e.getCause();
/* 150 */     } catch (TimeoutException e) {
/* 151 */       return createTimeoutException(thread);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Exception createTimeoutException(Thread thread) {
/* 156 */     StackTraceElement[] stackTrace = thread.getStackTrace();
/* 157 */     Thread stuckThread = this.lookForStuckThread ? getStuckThread(thread) : null;
/* 158 */     TestTimedOutException testTimedOutException = new TestTimedOutException(this.timeout, this.timeUnit);
/* 159 */     if (stackTrace != null) {
/* 160 */       testTimedOutException.setStackTrace(stackTrace);
/* 161 */       thread.interrupt();
/*     */     } 
/* 163 */     if (stuckThread != null) {
/* 164 */       Exception stuckThreadException = new Exception("Appears to be stuck in thread " + stuckThread.getName());
/*     */ 
/*     */       
/* 167 */       stuckThreadException.setStackTrace(getStackTrace(stuckThread));
/* 168 */       return (Exception)new MultipleFailureException(Arrays.asList(new Throwable[] { (Throwable)testTimedOutException, stuckThreadException }));
/*     */     } 
/*     */     
/* 171 */     return (Exception)testTimedOutException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StackTraceElement[] getStackTrace(Thread thread) {
/*     */     try {
/* 183 */       return thread.getStackTrace();
/* 184 */     } catch (SecurityException e) {
/* 185 */       return new StackTraceElement[0];
/*     */     } 
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
/*     */   private Thread getStuckThread(Thread mainThread) {
/* 200 */     if (this.threadGroup == null) {
/* 201 */       return null;
/*     */     }
/* 203 */     Thread[] threadsInGroup = getThreadArray(this.threadGroup);
/* 204 */     if (threadsInGroup == null) {
/* 205 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     Thread stuckThread = null;
/* 214 */     long maxCpuTime = 0L;
/* 215 */     for (Thread thread : threadsInGroup) {
/* 216 */       if (thread.getState() == Thread.State.RUNNABLE) {
/* 217 */         long threadCpuTime = cpuTime(thread);
/* 218 */         if (stuckThread == null || threadCpuTime > maxCpuTime) {
/* 219 */           stuckThread = thread;
/* 220 */           maxCpuTime = threadCpuTime;
/*     */         } 
/*     */       } 
/*     */     } 
/* 224 */     return (stuckThread == mainThread) ? null : stuckThread;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Thread[] getThreadArray(ThreadGroup group) {
/*     */     int enumCount;
/*     */     Thread[] threads;
/* 236 */     int count = group.activeCount();
/* 237 */     int enumSize = Math.max(count * 2, 100);
/*     */ 
/*     */     
/* 240 */     int loopCount = 0;
/*     */     while (true) {
/* 242 */       threads = new Thread[enumSize];
/* 243 */       enumCount = group.enumerate(threads);
/* 244 */       if (enumCount < enumSize) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 250 */       enumSize += 100;
/* 251 */       if (++loopCount >= 5) {
/* 252 */         return null;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 257 */     return copyThreads(threads, enumCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Thread[] copyThreads(Thread[] threads, int count) {
/* 268 */     int length = Math.min(count, threads.length);
/* 269 */     Thread[] result = new Thread[length];
/* 270 */     for (int i = 0; i < length; i++) {
/* 271 */       result[i] = threads[i];
/*     */     }
/* 273 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long cpuTime(Thread thr) {
/* 282 */     ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
/* 283 */     if (mxBean.isThreadCpuTimeSupported()) {
/*     */       try {
/* 285 */         return mxBean.getThreadCpuTime(thr.getId());
/* 286 */       } catch (UnsupportedOperationException e) {}
/*     */     }
/*     */     
/* 289 */     return 0L;
/*     */   }
/*     */   
/*     */   private class CallableStatement implements Callable<Throwable> {
/* 293 */     private final CountDownLatch startLatch = new CountDownLatch(1);
/*     */     
/*     */     public Throwable call() throws Exception {
/*     */       try {
/* 297 */         this.startLatch.countDown();
/* 298 */         FailOnTimeout.this.originalStatement.evaluate();
/* 299 */       } catch (Exception e) {
/* 300 */         throw e;
/* 301 */       } catch (Throwable e) {
/* 302 */         return e;
/*     */       } 
/* 304 */       return null;
/*     */     }
/*     */     
/*     */     public void awaitStarted() throws InterruptedException {
/* 308 */       this.startLatch.await();
/*     */     }
/*     */     
/*     */     private CallableStatement() {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/runners/statements/FailOnTimeout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */