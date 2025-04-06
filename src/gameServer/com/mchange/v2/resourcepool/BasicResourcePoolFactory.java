/*     */ package com.mchange.v2.resourcepool;
/*     */ 
/*     */ import com.mchange.v2.async.AsynchronousRunner;
/*     */ import com.mchange.v2.async.CarefulRunnableQueue;
/*     */ import com.mchange.v2.async.RunnableQueue;
/*     */ import com.mchange.v2.async.ThreadPoolAsynchronousRunner;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Timer;
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
/*     */ 
/*     */ 
/*     */ public class BasicResourcePoolFactory
/*     */   extends ResourcePoolFactory
/*     */ {
/*     */   public static BasicResourcePoolFactory createNoEventSupportInstance(int num_task_threads) {
/*  44 */     return createNoEventSupportInstance(null, null, num_task_threads);
/*     */   }
/*     */   
/*     */   public static BasicResourcePoolFactory createNoEventSupportInstance(AsynchronousRunner taskRunner, Timer timer) {
/*  48 */     return createNoEventSupportInstance(taskRunner, timer, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static BasicResourcePoolFactory createNoEventSupportInstance(AsynchronousRunner taskRunner, Timer timer, int default_num_task_threads) {
/*  55 */     return new BasicResourcePoolFactory(taskRunner, timer, default_num_task_threads, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   int start = -1;
/*  62 */   int min = 1;
/*  63 */   int max = 12;
/*  64 */   int inc = 3;
/*  65 */   int retry_attempts = -1;
/*  66 */   int retry_delay = 1000;
/*  67 */   long idle_resource_test_period = -1L;
/*  68 */   long max_age = -1L;
/*  69 */   long max_idle_time = -1L;
/*  70 */   long excess_max_idle_time = -1L;
/*  71 */   long destroy_overdue_resc_time = -1L;
/*  72 */   long expiration_enforcement_delay = -1L;
/*     */ 
/*     */   
/*     */   boolean break_on_acquisition_failure = true;
/*     */ 
/*     */   
/*     */   boolean debug_store_checkout_stacktrace = false;
/*     */ 
/*     */   
/*     */   AsynchronousRunner taskRunner;
/*     */ 
/*     */   
/*     */   boolean taskRunner_is_external;
/*     */ 
/*     */   
/*     */   RunnableQueue asyncEventQueue;
/*     */   
/*     */   boolean asyncEventQueue_is_external;
/*     */   
/*     */   Timer timer;
/*     */   
/*     */   boolean timer_is_external;
/*     */   
/*     */   int default_num_task_threads;
/*     */   
/*     */   Set liveChildren;
/*     */ 
/*     */   
/*     */   BasicResourcePoolFactory() {
/* 101 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   BasicResourcePoolFactory(AsynchronousRunner taskRunner, RunnableQueue asyncEventQueue, Timer timer) {
/* 106 */     this(taskRunner, asyncEventQueue, timer, 3);
/*     */   }
/*     */   BasicResourcePoolFactory(int num_task_threads) {
/* 109 */     this((AsynchronousRunner)null, (RunnableQueue)null, (Timer)null, num_task_threads);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BasicResourcePoolFactory(AsynchronousRunner taskRunner, Timer timer, int default_num_task_threads, boolean no_event_support) {
/* 116 */     this(taskRunner, (RunnableQueue)null, timer, default_num_task_threads);
/* 117 */     if (no_event_support) {
/* 118 */       this.asyncEventQueue_is_external = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BasicResourcePoolFactory(AsynchronousRunner taskRunner, RunnableQueue asyncEventQueue, Timer timer, int default_num_task_threads) {
/* 126 */     this.taskRunner = taskRunner;
/* 127 */     this.taskRunner_is_external = (taskRunner != null);
/*     */     
/* 129 */     this.asyncEventQueue = asyncEventQueue;
/* 130 */     this.asyncEventQueue_is_external = (asyncEventQueue != null);
/*     */     
/* 132 */     this.timer = timer;
/* 133 */     this.timer_is_external = (timer != null);
/*     */     
/* 135 */     this.default_num_task_threads = default_num_task_threads;
/*     */   }
/*     */ 
/*     */   
/*     */   private void createThreadResources() {
/* 140 */     if (!this.taskRunner_is_external)
/* 141 */       this.taskRunner = (AsynchronousRunner)new ThreadPoolAsynchronousRunner(this.default_num_task_threads, true); 
/* 142 */     if (!this.asyncEventQueue_is_external)
/* 143 */       this.asyncEventQueue = (RunnableQueue)new CarefulRunnableQueue(true, false); 
/* 144 */     if (!this.timer_is_external) {
/* 145 */       this.timer = new Timer(true);
/*     */     }
/* 147 */     this.liveChildren = new HashSet();
/*     */   }
/*     */ 
/*     */   
/*     */   private void destroyThreadResources() {
/* 152 */     if (!this.taskRunner_is_external) {
/*     */       
/* 154 */       this.taskRunner.close();
/* 155 */       this.taskRunner = null;
/*     */     } 
/* 157 */     if (!this.asyncEventQueue_is_external) {
/*     */       
/* 159 */       this.asyncEventQueue.close();
/* 160 */       this.asyncEventQueue = null;
/*     */     } 
/* 162 */     if (!this.timer_is_external) {
/*     */       
/* 164 */       this.timer.cancel();
/* 165 */       this.timer = null;
/*     */     } 
/*     */     
/* 168 */     this.liveChildren = null;
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
/*     */   synchronized void markBroken(BasicResourcePool pool) {
/* 196 */     if (this.liveChildren != null) {
/*     */       
/* 198 */       this.liveChildren.remove(pool);
/* 199 */       if (this.liveChildren.isEmpty()) {
/* 200 */         destroyThreadResources();
/*     */       }
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
/*     */   public synchronized void setStart(int start) throws ResourcePoolException {
/* 226 */     this.start = start;
/*     */   }
/*     */   
/*     */   public synchronized int getStart() throws ResourcePoolException {
/* 230 */     return this.start;
/*     */   }
/*     */   
/*     */   public synchronized void setMin(int min) throws ResourcePoolException {
/* 234 */     this.min = min;
/*     */   }
/*     */   
/*     */   public synchronized int getMin() throws ResourcePoolException {
/* 238 */     return this.min;
/*     */   }
/*     */   
/*     */   public synchronized void setMax(int max) throws ResourcePoolException {
/* 242 */     this.max = max;
/*     */   }
/*     */   
/*     */   public synchronized int getMax() throws ResourcePoolException {
/* 246 */     return this.max;
/*     */   }
/*     */   
/*     */   public synchronized void setIncrement(int inc) throws ResourcePoolException {
/* 250 */     this.inc = inc;
/*     */   }
/*     */   
/*     */   public synchronized int getIncrement() throws ResourcePoolException {
/* 254 */     return this.inc;
/*     */   }
/*     */   
/*     */   public synchronized void setAcquisitionRetryAttempts(int retry_attempts) throws ResourcePoolException {
/* 258 */     this.retry_attempts = retry_attempts;
/*     */   }
/*     */   
/*     */   public synchronized int getAcquisitionRetryAttempts() throws ResourcePoolException {
/* 262 */     return this.retry_attempts;
/*     */   }
/*     */   
/*     */   public synchronized void setAcquisitionRetryDelay(int retry_delay) throws ResourcePoolException {
/* 266 */     this.retry_delay = retry_delay;
/*     */   }
/*     */   
/*     */   public synchronized int getAcquisitionRetryDelay() throws ResourcePoolException {
/* 270 */     return this.retry_delay;
/*     */   }
/*     */   public synchronized void setIdleResourceTestPeriod(long test_period) {
/* 273 */     this.idle_resource_test_period = test_period;
/*     */   }
/*     */   public synchronized long getIdleResourceTestPeriod() {
/* 276 */     return this.idle_resource_test_period;
/*     */   }
/*     */   
/*     */   public synchronized void setResourceMaxAge(long max_age) throws ResourcePoolException {
/* 280 */     this.max_age = max_age;
/*     */   }
/*     */   
/*     */   public synchronized long getResourceMaxAge() throws ResourcePoolException {
/* 284 */     return this.max_age;
/*     */   }
/*     */   
/*     */   public synchronized void setResourceMaxIdleTime(long millis) throws ResourcePoolException {
/* 288 */     this.max_idle_time = millis;
/*     */   }
/*     */   
/*     */   public synchronized long getResourceMaxIdleTime() throws ResourcePoolException {
/* 292 */     return this.max_idle_time;
/*     */   }
/*     */   
/*     */   public synchronized void setExcessResourceMaxIdleTime(long millis) throws ResourcePoolException {
/* 296 */     this.excess_max_idle_time = millis;
/*     */   }
/*     */   
/*     */   public synchronized long getExcessResourceMaxIdleTime() throws ResourcePoolException {
/* 300 */     return this.excess_max_idle_time;
/*     */   }
/*     */   
/*     */   public synchronized long getDestroyOverdueResourceTime() throws ResourcePoolException {
/* 304 */     return this.destroy_overdue_resc_time;
/*     */   }
/*     */   
/*     */   public synchronized void setDestroyOverdueResourceTime(long millis) throws ResourcePoolException {
/* 308 */     this.destroy_overdue_resc_time = millis;
/*     */   }
/*     */   
/*     */   public synchronized void setExpirationEnforcementDelay(long expiration_enforcement_delay) throws ResourcePoolException {
/* 312 */     this.expiration_enforcement_delay = expiration_enforcement_delay;
/*     */   }
/*     */   
/*     */   public synchronized long getExpirationEnforcementDelay() throws ResourcePoolException {
/* 316 */     return this.expiration_enforcement_delay;
/*     */   }
/*     */   
/*     */   public synchronized void setBreakOnAcquisitionFailure(boolean break_on_acquisition_failure) throws ResourcePoolException {
/* 320 */     this.break_on_acquisition_failure = break_on_acquisition_failure;
/*     */   }
/*     */   
/*     */   public synchronized boolean getBreakOnAcquisitionFailure() throws ResourcePoolException {
/* 324 */     return this.break_on_acquisition_failure;
/*     */   }
/*     */   
/*     */   public synchronized void setDebugStoreCheckoutStackTrace(boolean debug_store_checkout_stacktrace) throws ResourcePoolException {
/* 328 */     this.debug_store_checkout_stacktrace = debug_store_checkout_stacktrace;
/*     */   }
/*     */   
/*     */   public synchronized boolean getDebugStoreCheckoutStackTrace() throws ResourcePoolException {
/* 332 */     return this.debug_store_checkout_stacktrace;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResourcePool createPool(ResourcePool.Manager mgr) throws ResourcePoolException {
/* 337 */     if (this.liveChildren == null) {
/* 338 */       createThreadResources();
/*     */     }
/* 340 */     ResourcePool child = new BasicResourcePool(mgr, this.start, this.min, this.max, this.inc, this.retry_attempts, this.retry_delay, this.idle_resource_test_period, this.max_age, this.max_idle_time, this.excess_max_idle_time, this.destroy_overdue_resc_time, this.expiration_enforcement_delay, this.break_on_acquisition_failure, this.debug_store_checkout_stacktrace, this.taskRunner, this.asyncEventQueue, this.timer, this);
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
/* 359 */     this.liveChildren.add(child);
/* 360 */     return child;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/BasicResourcePoolFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */