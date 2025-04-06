/*      */ package com.mchange.v2.resourcepool;
/*      */ 
/*      */ import com.mchange.v2.async.AsynchronousRunner;
/*      */ import com.mchange.v2.async.RunnableQueue;
/*      */ import com.mchange.v2.cfg.MConfig;
/*      */ import com.mchange.v2.lang.ThreadUtils;
/*      */ import com.mchange.v2.log.MLevel;
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.util.ResourceClosedException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Timer;
/*      */ import java.util.TimerTask;
/*      */ import java.util.WeakHashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class BasicResourcePool
/*      */   implements ResourcePool
/*      */ {
/*   46 */   private static final MLogger logger = MLog.getLogger(BasicResourcePool.class);
/*      */   
/*      */   static final int AUTO_CULL_FREQUENCY_DIVISOR = 4;
/*      */   static final int AUTO_MAX_CULL_FREQUENCY = 900000;
/*      */   static final int AUTO_MIN_CULL_FREQUENCY = 1000;
/*      */   static final String USE_SCATTERED_ACQUIRE_TASK_KEY = "com.mchange.v2.resourcepool.experimental.useScatteredAcquireTask";
/*      */   static final boolean USE_SCATTERED_ACQUIRE_TASK;
/*      */   final ResourcePool.Manager mgr;
/*      */   final int start;
/*      */   final int min;
/*      */   final int max;
/*      */   final int inc;
/*      */   
/*      */   static {
/*   60 */     String checkScattered = MConfig.readVmConfig().getProperty("com.mchange.v2.resourcepool.experimental.useScatteredAcquireTask");
/*   61 */     if (checkScattered != null && checkScattered.trim().toLowerCase().equals("false")) {
/*      */       
/*   63 */       USE_SCATTERED_ACQUIRE_TASK = false;
/*   64 */       if (logger.isLoggable(MLevel.INFO)) {
/*   65 */         logger.info(BasicResourcePool.class.getName() + " using traditional, Thread-blocking AcquireTask. Yuk. Why?");
/*      */       }
/*      */     } else {
/*   68 */       USE_SCATTERED_ACQUIRE_TASK = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final int num_acq_attempts;
/*      */   
/*      */   final int acq_attempt_delay;
/*      */   
/*      */   final long check_idle_resources_delay;
/*      */   
/*      */   final long max_resource_age;
/*      */   
/*      */   final long max_idle_time;
/*      */   
/*      */   final long excess_max_idle_time;
/*      */   
/*      */   final long destroy_unreturned_resc_time;
/*      */   
/*      */   final long expiration_enforcement_delay;
/*      */   
/*      */   final boolean break_on_acquisition_failure;
/*      */   
/*      */   final boolean debug_store_checkout_exceptions;
/*      */   
/*   93 */   final long pool_start_time = System.currentTimeMillis();
/*      */   
/*      */   final BasicResourcePoolFactory factory;
/*      */   
/*      */   final AsynchronousRunner taskRunner;
/*      */   
/*      */   final RunnableQueue asyncEventQueue;
/*      */   
/*      */   final ResourcePoolEventSupport rpes;
/*      */   Timer cullAndIdleRefurbishTimer;
/*      */   TimerTask cullTask;
/*      */   TimerTask idleRefurbishTask;
/*  105 */   HashSet acquireWaiters = new HashSet();
/*  106 */   HashSet otherWaiters = new HashSet();
/*      */   
/*      */   int pending_acquires;
/*      */   
/*      */   int pending_removes;
/*      */   
/*      */   int target_pool_size;
/*      */   
/*  114 */   HashMap managed = new HashMap<Object, Object>();
/*      */ 
/*      */   
/*  117 */   LinkedList unused = new LinkedList();
/*      */ 
/*      */ 
/*      */   
/*  121 */   HashSet excluded = new HashSet();
/*      */   
/*  123 */   Map formerResources = new WeakHashMap<Object, Object>();
/*      */   
/*  125 */   Set idleCheckResources = new HashSet();
/*      */ 
/*      */   
/*      */   boolean force_kill_acquires = false;
/*      */ 
/*      */   
/*      */   boolean broken = false;
/*      */   
/*  133 */   long failed_checkins = 0L;
/*  134 */   long failed_checkouts = 0L;
/*  135 */   long failed_idle_tests = 0L;
/*      */   
/*  137 */   Throwable lastCheckinFailure = null;
/*  138 */   Throwable lastCheckoutFailure = null;
/*  139 */   Throwable lastIdleTestFailure = null;
/*  140 */   Throwable lastResourceTestFailure = null;
/*      */   
/*  142 */   Throwable lastAcquisitionFailiure = null; Object exampleResource;
/*      */   private static final int NO_DECREMENT = 0;
/*      */   private static final int DECREMENT_ON_SUCCESS = 1;
/*      */   private static final int DECREMENT_WITH_CERTAINTY = 2;
/*      */   
/*      */   public long getStartTime() {
/*  148 */     return this.pool_start_time;
/*      */   }
/*      */   public long getUpTime() {
/*  151 */     return System.currentTimeMillis() - this.pool_start_time;
/*      */   }
/*      */   public synchronized long getNumFailedCheckins() {
/*  154 */     return this.failed_checkins;
/*      */   }
/*      */   public synchronized long getNumFailedCheckouts() {
/*  157 */     return this.failed_checkouts;
/*      */   }
/*      */   public synchronized long getNumFailedIdleTests() {
/*  160 */     return this.failed_idle_tests;
/*      */   }
/*      */   public synchronized Throwable getLastCheckinFailure() {
/*  163 */     return this.lastCheckinFailure;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setLastCheckinFailure(Throwable t) {
/*  168 */     assert Thread.holdsLock(this);
/*      */     
/*  170 */     this.lastCheckinFailure = t;
/*  171 */     this.lastResourceTestFailure = t;
/*      */   }
/*      */   
/*      */   public synchronized Throwable getLastCheckoutFailure() {
/*  175 */     return this.lastCheckoutFailure;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setLastCheckoutFailure(Throwable t) {
/*  180 */     assert Thread.holdsLock(this);
/*      */     
/*  182 */     this.lastCheckoutFailure = t;
/*  183 */     this.lastResourceTestFailure = t;
/*      */   }
/*      */   
/*      */   public synchronized Throwable getLastIdleCheckFailure() {
/*  187 */     return this.lastIdleTestFailure;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setLastIdleCheckFailure(Throwable t) {
/*  192 */     assert Thread.holdsLock(this);
/*      */     
/*  194 */     this.lastIdleTestFailure = t;
/*  195 */     this.lastResourceTestFailure = t;
/*      */   }
/*      */   
/*      */   public synchronized Throwable getLastResourceTestFailure() {
/*  199 */     return this.lastResourceTestFailure;
/*      */   }
/*      */   public synchronized Throwable getLastAcquisitionFailure() {
/*  202 */     return this.lastAcquisitionFailiure;
/*      */   }
/*      */   
/*      */   private synchronized void setLastAcquisitionFailure(Throwable t) {
/*  206 */     this.lastAcquisitionFailiure = t;
/*      */   }
/*      */   public synchronized int getNumCheckoutWaiters() {
/*  209 */     return this.acquireWaiters.size();
/*      */   }
/*      */   public synchronized int getNumPendingAcquireTasks() {
/*  212 */     return this.pending_acquires;
/*      */   }
/*      */   public synchronized int getNumPendingRemoveTasks() {
/*  215 */     return this.pending_removes;
/*      */   }
/*      */   public synchronized int getNumThreadsWaitingForResources() {
/*  218 */     return this.acquireWaiters.size();
/*      */   }
/*      */   
/*      */   public synchronized String[] getThreadNamesWaitingForResources() {
/*  222 */     int len = this.acquireWaiters.size();
/*  223 */     String[] out = new String[len];
/*  224 */     int i = 0;
/*  225 */     for (Iterator<Thread> ii = this.acquireWaiters.iterator(); ii.hasNext();)
/*  226 */       out[i++] = ((Thread)ii.next()).getName(); 
/*  227 */     Arrays.sort((Object[])out);
/*  228 */     return out;
/*      */   }
/*      */   
/*      */   public synchronized int getNumThreadsWaitingForAdministrativeTasks() {
/*  232 */     return this.otherWaiters.size();
/*      */   }
/*      */   
/*      */   public synchronized String[] getThreadNamesWaitingForAdministrativeTasks() {
/*  236 */     int len = this.otherWaiters.size();
/*  237 */     String[] out = new String[len];
/*  238 */     int i = 0;
/*  239 */     for (Iterator<Thread> ii = this.otherWaiters.iterator(); ii.hasNext();)
/*  240 */       out[i++] = ((Thread)ii.next()).getName(); 
/*  241 */     Arrays.sort((Object[])out);
/*  242 */     return out;
/*      */   }
/*      */   
/*      */   private void addToFormerResources(Object resc) {
/*  246 */     this.formerResources.put(resc, null);
/*      */   }
/*      */   private boolean isFormerResource(Object resc) {
/*  249 */     return this.formerResources.keySet().contains(resc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BasicResourcePool(ResourcePool.Manager mgr, int start, int min, int max, int inc, int num_acq_attempts, int acq_attempt_delay, long check_idle_resources_delay, long max_resource_age, long max_idle_time, long excess_max_idle_time, long destroy_unreturned_resc_time, long expiration_enforcement_delay, boolean break_on_acquisition_failure, boolean debug_store_checkout_exceptions, AsynchronousRunner taskRunner, RunnableQueue asyncEventQueue, Timer cullAndIdleRefurbishTimer, BasicResourcePoolFactory factory) throws ResourcePoolException {
/*      */     try {
/*  277 */       this.mgr = mgr;
/*  278 */       this.start = start;
/*  279 */       this.min = min;
/*  280 */       this.max = max;
/*  281 */       this.inc = inc;
/*  282 */       this.num_acq_attempts = num_acq_attempts;
/*  283 */       this.acq_attempt_delay = acq_attempt_delay;
/*  284 */       this.check_idle_resources_delay = check_idle_resources_delay;
/*  285 */       this.max_resource_age = max_resource_age;
/*  286 */       this.max_idle_time = max_idle_time;
/*  287 */       this.excess_max_idle_time = excess_max_idle_time;
/*  288 */       this.destroy_unreturned_resc_time = destroy_unreturned_resc_time;
/*      */       
/*  290 */       this.break_on_acquisition_failure = break_on_acquisition_failure;
/*  291 */       this.debug_store_checkout_exceptions = (debug_store_checkout_exceptions && destroy_unreturned_resc_time > 0L);
/*  292 */       this.taskRunner = taskRunner;
/*  293 */       this.asyncEventQueue = asyncEventQueue;
/*  294 */       this.cullAndIdleRefurbishTimer = cullAndIdleRefurbishTimer;
/*  295 */       this.factory = factory;
/*      */       
/*  297 */       this.pending_acquires = 0;
/*  298 */       this.pending_removes = 0;
/*      */       
/*  300 */       this.target_pool_size = Math.max(start, min);
/*      */       
/*  302 */       if (asyncEventQueue != null) {
/*  303 */         this.rpes = new ResourcePoolEventSupport(this);
/*      */       } else {
/*  305 */         this.rpes = null;
/*      */       } 
/*      */       
/*  308 */       ensureStartResources();
/*      */       
/*  310 */       if (mustEnforceExpiration()) {
/*      */         
/*  312 */         if (expiration_enforcement_delay <= 0L) {
/*  313 */           this.expiration_enforcement_delay = automaticExpirationEnforcementDelay();
/*      */         } else {
/*  315 */           this.expiration_enforcement_delay = expiration_enforcement_delay;
/*      */         } 
/*  317 */         this.cullTask = new CullTask();
/*      */ 
/*      */         
/*  320 */         cullAndIdleRefurbishTimer.schedule(this.cullTask, minExpirationTime(), this.expiration_enforcement_delay);
/*      */       } else {
/*      */         
/*  323 */         this.expiration_enforcement_delay = expiration_enforcement_delay;
/*      */       } 
/*      */       
/*  326 */       if (check_idle_resources_delay > 0L) {
/*      */         
/*  328 */         this.idleRefurbishTask = new CheckIdleResourcesTask();
/*  329 */         cullAndIdleRefurbishTimer.schedule(this.idleRefurbishTask, check_idle_resources_delay, check_idle_resources_delay);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  334 */       if (logger.isLoggable(MLevel.FINER)) {
/*  335 */         logger.finer(this + " config: [start -> " + this.start + "; min -> " + this.min + "; max -> " + this.max + "; inc -> " + this.inc + "; num_acq_attempts -> " + this.num_acq_attempts + "; acq_attempt_delay -> " + this.acq_attempt_delay + "; check_idle_resources_delay -> " + this.check_idle_resources_delay + "; mox_resource_age -> " + this.max_resource_age + "; max_idle_time -> " + this.max_idle_time + "; excess_max_idle_time -> " + this.excess_max_idle_time + "; destroy_unreturned_resc_time -> " + this.destroy_unreturned_resc_time + "; expiration_enforcement_delay -> " + this.expiration_enforcement_delay + "; break_on_acquisition_failure -> " + this.break_on_acquisition_failure + "; debug_store_checkout_exceptions -> " + this.debug_store_checkout_exceptions + "]");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  346 */     catch (Exception e) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  351 */       throw ResourcePoolUtils.convertThrowable(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean mustTestIdleResources() {
/*  360 */     return (this.check_idle_resources_delay > 0L);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean mustEnforceExpiration() {
/*  365 */     return (this.max_resource_age > 0L || this.max_idle_time > 0L || this.excess_max_idle_time > 0L || this.destroy_unreturned_resc_time > 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long minExpirationTime() {
/*  375 */     long out = Long.MAX_VALUE;
/*  376 */     if (this.max_resource_age > 0L)
/*  377 */       out = Math.min(out, this.max_resource_age); 
/*  378 */     if (this.max_idle_time > 0L)
/*  379 */       out = Math.min(out, this.max_idle_time); 
/*  380 */     if (this.excess_max_idle_time > 0L)
/*  381 */       out = Math.min(out, this.excess_max_idle_time); 
/*  382 */     if (this.destroy_unreturned_resc_time > 0L)
/*  383 */       out = Math.min(out, this.destroy_unreturned_resc_time); 
/*  384 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   private long automaticExpirationEnforcementDelay() {
/*  389 */     long out = minExpirationTime();
/*  390 */     out /= 4L;
/*  391 */     out = Math.min(out, 900000L);
/*  392 */     out = Math.max(out, 1000L);
/*  393 */     return out;
/*      */   }
/*      */   
/*      */   public long getEffectiveExpirationEnforcementDelay() {
/*  397 */     return this.expiration_enforcement_delay;
/*      */   }
/*      */   private synchronized boolean isBroken() {
/*  400 */     return this.broken;
/*      */   }
/*      */   
/*      */   private boolean supportsEvents() {
/*  404 */     return (this.asyncEventQueue != null);
/*      */   }
/*      */   
/*      */   public Object checkoutResource() throws ResourcePoolException, InterruptedException {
/*      */     try {
/*  409 */       return checkoutResource(0L);
/*  410 */     } catch (TimeoutException e) {
/*      */ 
/*      */ 
/*      */       
/*  414 */       if (logger.isLoggable(MLevel.WARNING)) {
/*  415 */         logger.log(MLevel.WARNING, "Huh??? TimeoutException with no timeout set!!!", (Throwable)e);
/*      */       }
/*  417 */       throw new ResourcePoolException("Huh??? TimeoutException with no timeout set!!!", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void _recheckResizePool() {
/*  424 */     assert Thread.holdsLock(this);
/*      */     
/*  426 */     if (!this.broken) {
/*      */       
/*  428 */       int msz = this.managed.size();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       int shrink_count;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  438 */       if ((shrink_count = msz - this.pending_removes - this.target_pool_size) > 0)
/*  439 */       { shrinkPool(shrink_count); }
/*  440 */       else { int expand_count; if ((expand_count = this.target_pool_size - msz + this.pending_acquires) > 0)
/*  441 */           expandPool(expand_count);  }
/*      */     
/*      */     } 
/*      */   }
/*      */   
/*      */   private synchronized void incrementPendingAcquires() {
/*  447 */     this.pending_acquires++;
/*      */     
/*  449 */     if (logger.isLoggable(MLevel.FINEST)) {
/*  450 */       logger.finest("incremented pending_acquires: " + this.pending_acquires);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized void incrementPendingRemoves() {
/*  456 */     this.pending_removes++;
/*      */     
/*  458 */     if (logger.isLoggable(MLevel.FINEST)) {
/*  459 */       logger.finest("incremented pending_removes: " + this.pending_removes);
/*      */     }
/*      */   }
/*      */   
/*      */   private synchronized void decrementPendingAcquires() {
/*  464 */     _decrementPendingAcquires();
/*      */   }
/*      */   
/*      */   private void _decrementPendingAcquires() {
/*  468 */     this.pending_acquires--;
/*      */     
/*  470 */     if (logger.isLoggable(MLevel.FINEST)) {
/*  471 */       logger.finest("decremented pending_acquires: " + this.pending_acquires);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized void decrementPendingRemoves() {
/*  477 */     this.pending_removes--;
/*      */     
/*  479 */     if (logger.isLoggable(MLevel.FINEST)) {
/*  480 */       logger.finest("decremented pending_removes: " + this.pending_removes);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized void recheckResizePool() {
/*  486 */     _recheckResizePool();
/*      */   }
/*      */ 
/*      */   
/*      */   private void expandPool(int count) {
/*  491 */     assert Thread.holdsLock(this);
/*      */ 
/*      */ 
/*      */     
/*  495 */     if (USE_SCATTERED_ACQUIRE_TASK) {
/*      */       
/*  497 */       for (int i = 0; i < count; i++) {
/*  498 */         this.taskRunner.postRunnable(new ScatteredAcquireTask());
/*      */       }
/*      */     } else {
/*      */       
/*  502 */       for (int i = 0; i < count; i++) {
/*  503 */         this.taskRunner.postRunnable(new AcquireTask());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void shrinkPool(int count) {
/*  510 */     assert Thread.holdsLock(this);
/*      */     
/*  512 */     for (int i = 0; i < count; i++) {
/*  513 */       this.taskRunner.postRunnable(new RemoveTask());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object checkoutResource(long timeout) throws TimeoutException, ResourcePoolException, InterruptedException {
/*  526 */     Object resc = prelimCheckoutResource(timeout);
/*      */     
/*  528 */     boolean refurb = attemptRefurbishResourceOnCheckout(resc);
/*      */     
/*  530 */     synchronized (this) {
/*      */       
/*  532 */       if (!refurb) {
/*      */         
/*  534 */         removeResource(resc);
/*  535 */         ensureMinResources();
/*  536 */         resc = null;
/*      */       }
/*      */       else {
/*      */         
/*  540 */         asyncFireResourceCheckedOut(resc, this.managed.size(), this.unused.size(), this.excluded.size());
/*  541 */         trace();
/*      */         
/*  543 */         PunchCard card = (PunchCard)this.managed.get(resc);
/*  544 */         if (card == null) {
/*      */           
/*  546 */           if (logger.isLoggable(MLevel.FINE)) {
/*  547 */             logger.fine("Resource " + resc + " was removed from the pool while it was being checked out " + " or refurbished for checkout.");
/*      */           }
/*  549 */           resc = null;
/*      */         }
/*      */         else {
/*      */           
/*  553 */           card.checkout_time = System.currentTimeMillis();
/*  554 */           if (this.debug_store_checkout_exceptions) {
/*  555 */             card.checkoutStackTraceException = new Exception("DEBUG STACK TRACE: Overdue resource check-out stack trace.");
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  562 */     if (resc == null) {
/*  563 */       return checkoutResource(timeout);
/*      */     }
/*  565 */     return resc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized Object prelimCheckoutResource(long timeout) throws TimeoutException, ResourcePoolException, InterruptedException {
/*      */     try {
/*  573 */       ensureNotBroken();
/*      */       
/*  575 */       int available = this.unused.size();
/*  576 */       if (available == 0) {
/*      */         
/*  578 */         int msz = this.managed.size();
/*      */         
/*  580 */         if (msz < this.max) {
/*      */ 
/*      */ 
/*      */           
/*  584 */           int desired_target = msz + this.acquireWaiters.size() + 1;
/*      */           
/*  586 */           if (logger.isLoggable(MLevel.FINER)) {
/*  587 */             logger.log(MLevel.FINER, "acquire test -- pool size: " + msz + "; target_pool_size: " + this.target_pool_size + "; desired target? " + desired_target);
/*      */           }
/*  589 */           if (desired_target >= this.target_pool_size)
/*      */           {
/*      */             
/*  592 */             desired_target = Math.max(desired_target, this.target_pool_size + this.inc);
/*      */ 
/*      */             
/*  595 */             this.target_pool_size = Math.max(Math.min(this.max, desired_target), this.min);
/*      */             
/*  597 */             _recheckResizePool();
/*      */           
/*      */           }
/*      */         
/*      */         }
/*  602 */         else if (logger.isLoggable(MLevel.FINER)) {
/*  603 */           logger.log(MLevel.FINER, "acquire test -- pool is already maxed out. [managed: " + msz + "; max: " + this.max + "]");
/*      */         } 
/*      */         
/*  606 */         awaitAvailable(timeout);
/*      */       } 
/*      */       
/*  609 */       Object resc = this.unused.get(0);
/*      */ 
/*      */ 
/*      */       
/*  613 */       if (this.idleCheckResources.contains(resc)) {
/*      */         
/*  615 */         if (logger.isLoggable(MLevel.FINER)) {
/*  616 */           logger.log(MLevel.FINER, "Resource we want to check out is in idleCheck! (waiting until idle-check completes.) [" + this + "]");
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  624 */         Thread t = Thread.currentThread();
/*      */         
/*      */         try {
/*  627 */           this.otherWaiters.add(t);
/*  628 */           wait(timeout);
/*  629 */           ensureNotBroken();
/*      */         } finally {
/*      */           
/*  632 */           this.otherWaiters.remove(t);
/*  633 */         }  return prelimCheckoutResource(timeout);
/*      */       } 
/*  635 */       if (shouldExpire(resc)) {
/*      */         
/*  637 */         removeResource(resc);
/*  638 */         ensureMinResources();
/*  639 */         return prelimCheckoutResource(timeout);
/*      */       } 
/*      */ 
/*      */       
/*  643 */       this.unused.remove(0);
/*  644 */       return resc;
/*      */     
/*      */     }
/*  647 */     catch (ResourceClosedException e) {
/*      */ 
/*      */ 
/*      */       
/*  651 */       if (logger.isLoggable(MLevel.SEVERE)) {
/*  652 */         logger.log(MLevel.SEVERE, this + " -- the pool was found to be closed or broken during an attempt to check out a resource.", (Throwable)e);
/*      */       }
/*  654 */       unexpectedBreak();
/*  655 */       throw e;
/*      */     }
/*  657 */     catch (InterruptedException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  662 */       if (this.broken) {
/*      */         
/*  664 */         if (logger.isLoggable(MLevel.FINER)) {
/*  665 */           logger.log(MLevel.FINER, this + " -- an attempt to checkout a resource was interrupted, because the pool is now closed. " + "[Thread: " + Thread.currentThread().getName() + ']', e);
/*      */ 
/*      */         
/*      */         }
/*  669 */         else if (logger.isLoggable(MLevel.INFO)) {
/*  670 */           logger.log(MLevel.INFO, this + " -- an attempt to checkout a resource was interrupted, because the pool is now closed. " + "[Thread: " + Thread.currentThread().getName() + ']');
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*  676 */       else if (logger.isLoggable(MLevel.WARNING)) {
/*      */         
/*  678 */         logger.log(MLevel.WARNING, this + " -- an attempt to checkout a resource was interrupted, and the pool is still live: some other thread " + "must have either interrupted the Thread attempting checkout!", e);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  684 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void checkinResource(Object resc) throws ResourcePoolException {
/*      */     try {
/*  695 */       if (this.managed.keySet().contains(resc)) {
/*  696 */         doCheckinManaged(resc);
/*  697 */       } else if (this.excluded.contains(resc)) {
/*  698 */         doCheckinExcluded(resc);
/*  699 */       } else if (isFormerResource(resc)) {
/*      */         
/*  701 */         if (logger.isLoggable(MLevel.FINER)) {
/*  702 */           logger.finer("Resource " + resc + " checked-in after having been checked-in already, or checked-in after " + " having being destroyed for being checked-out too long.");
/*      */         }
/*      */       } else {
/*      */         
/*  706 */         throw new ResourcePoolException("ResourcePool" + (this.broken ? " [BROKEN!]" : "") + ": Tried to check-in a foreign resource!");
/*  707 */       }  trace();
/*      */     }
/*  709 */     catch (ResourceClosedException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  715 */       if (logger.isLoggable(MLevel.SEVERE)) {
/*  716 */         logger.log(MLevel.SEVERE, this + " - checkinResource( ... ) -- even broken pools should allow checkins without exception. probable resource pool bug.", (Throwable)e);
/*      */       }
/*      */ 
/*      */       
/*  720 */       unexpectedBreak();
/*  721 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void checkinAll() throws ResourcePoolException {
/*      */     try {
/*  730 */       Set checkedOutNotExcluded = new HashSet(this.managed.keySet());
/*  731 */       checkedOutNotExcluded.removeAll(this.unused); Iterator ii;
/*  732 */       for (ii = checkedOutNotExcluded.iterator(); ii.hasNext();)
/*  733 */         doCheckinManaged(ii.next()); 
/*  734 */       for (ii = this.excluded.iterator(); ii.hasNext();) {
/*  735 */         doCheckinExcluded(ii.next());
/*      */       }
/*  737 */     } catch (ResourceClosedException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  743 */       if (logger.isLoggable(MLevel.SEVERE)) {
/*  744 */         logger.log(MLevel.SEVERE, this + " - checkinAll() -- even broken pools should allow checkins without exception. probable resource pool bug.", (Throwable)e);
/*      */       }
/*      */ 
/*      */       
/*  748 */       unexpectedBreak();
/*  749 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int statusInPool(Object resc) throws ResourcePoolException {
/*      */     try {
/*  758 */       if (this.unused.contains(resc))
/*  759 */         return 0; 
/*  760 */       if (this.managed.keySet().contains(resc) || this.excluded.contains(resc)) {
/*  761 */         return 1;
/*      */       }
/*  763 */       return -1;
/*      */     }
/*  765 */     catch (ResourceClosedException e) {
/*      */ 
/*      */       
/*  768 */       if (logger.isLoggable(MLevel.SEVERE))
/*  769 */         logger.log(MLevel.SEVERE, "Apparent pool break.", (Throwable)e); 
/*  770 */       unexpectedBreak();
/*  771 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void markBroken(Object resc) {
/*      */     try {
/*  779 */       if (logger.isLoggable(MLevel.FINER)) {
/*  780 */         logger.log(MLevel.FINER, "Resource " + resc + " marked broken by pool (" + this + ").");
/*      */       }
/*  782 */       _markBroken(resc);
/*  783 */       ensureMinResources();
/*      */     }
/*  785 */     catch (ResourceClosedException e) {
/*      */ 
/*      */       
/*  788 */       if (logger.isLoggable(MLevel.SEVERE))
/*  789 */         logger.log(MLevel.SEVERE, "Apparent pool break.", (Throwable)e); 
/*  790 */       unexpectedBreak();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMinPoolSize() {
/*  796 */     return this.min;
/*      */   }
/*      */   
/*      */   public int getMaxPoolSize() {
/*  800 */     return this.max;
/*      */   }
/*      */   
/*      */   public synchronized int getPoolSize() throws ResourcePoolException {
/*  804 */     return this.managed.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getAvailableCount() {
/*  821 */     return this.unused.size();
/*      */   }
/*      */   public synchronized int getExcludedCount() {
/*  824 */     return this.excluded.size();
/*      */   }
/*      */   public synchronized int getAwaitingCheckinCount() {
/*  827 */     return this.managed.size() - this.unused.size() + this.excluded.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void resetPool() {
/*      */     try {
/*  833 */       for (Iterator ii = cloneOfManaged().keySet().iterator(); ii.hasNext();)
/*  834 */         markBrokenNoEnsureMinResources(ii.next()); 
/*  835 */       ensureMinResources();
/*      */     }
/*  837 */     catch (ResourceClosedException e) {
/*      */ 
/*      */       
/*  840 */       if (logger.isLoggable(MLevel.SEVERE))
/*  841 */         logger.log(MLevel.SEVERE, "Apparent pool break.", (Throwable)e); 
/*  842 */       unexpectedBreak();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void close() throws ResourcePoolException {
/*  852 */     close(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void finalize() throws Throwable {
/*  861 */     if (!this.broken) {
/*  862 */       close();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addResourcePoolListener(ResourcePoolListener rpl) {
/*  868 */     if (!supportsEvents()) {
/*  869 */       throw new RuntimeException(this + " does not support ResourcePoolEvents. " + "Probably it was constructed by a BasicResourceFactory configured not to support such events.");
/*      */     }
/*      */     
/*  872 */     this.rpes.addResourcePoolListener(rpl);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeResourcePoolListener(ResourcePoolListener rpl) {
/*  878 */     if (!supportsEvents()) {
/*  879 */       throw new RuntimeException(this + " does not support ResourcePoolEvents. " + "Probably it was constructed by a BasicResourceFactory configured not to support such events.");
/*      */     }
/*      */     
/*  882 */     this.rpes.removeResourcePoolListener(rpl);
/*      */   }
/*      */   
/*      */   private synchronized boolean isForceKillAcquiresPending() {
/*  886 */     return this.force_kill_acquires;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void forceKillAcquires() throws InterruptedException {
/*  893 */     if (logger.isLoggable(MLevel.WARNING)) {
/*  894 */       logger.log(MLevel.WARNING, "Having failed to acquire a resource, " + this + " is interrupting all Threads waiting on a resource to check out. " + "Will try again in response to new client requests.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  900 */     Thread t = Thread.currentThread();
/*      */ 
/*      */     
/*      */     try {
/*  904 */       this.force_kill_acquires = true;
/*  905 */       notifyAll();
/*  906 */       while (this.acquireWaiters.size() > 0) {
/*      */         
/*  908 */         this.otherWaiters.add(t);
/*  909 */         wait();
/*      */       } 
/*  911 */       this.force_kill_acquires = false;
/*      */     } finally {
/*      */       
/*  914 */       this.otherWaiters.remove(t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void unexpectedBreak() {
/*  921 */     if (logger.isLoggable(MLevel.SEVERE))
/*  922 */       logger.log(MLevel.SEVERE, this + " -- Unexpectedly broken!!!", (Throwable)new ResourcePoolException("Unexpected Break Stack Trace!")); 
/*  923 */     close(false);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean canFireEvents() {
/*  928 */     return (this.asyncEventQueue != null && !isBroken());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void asyncFireResourceAcquired(final Object resc, final int pool_size, final int available_size, final int removed_but_unreturned_size) {
/*  936 */     if (canFireEvents()) {
/*      */       
/*  938 */       Runnable r = new Runnable()
/*      */         {
/*      */           public void run() {
/*  941 */             BasicResourcePool.this.rpes.fireResourceAcquired(resc, pool_size, available_size, removed_but_unreturned_size); }
/*      */         };
/*  943 */       this.asyncEventQueue.postRunnable(r);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void asyncFireResourceCheckedIn(final Object resc, final int pool_size, final int available_size, final int removed_but_unreturned_size) {
/*  953 */     if (canFireEvents()) {
/*      */       
/*  955 */       Runnable r = new Runnable()
/*      */         {
/*      */           public void run() {
/*  958 */             BasicResourcePool.this.rpes.fireResourceCheckedIn(resc, pool_size, available_size, removed_but_unreturned_size); }
/*      */         };
/*  960 */       this.asyncEventQueue.postRunnable(r);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void asyncFireResourceCheckedOut(final Object resc, final int pool_size, final int available_size, final int removed_but_unreturned_size) {
/*  970 */     if (canFireEvents()) {
/*      */       
/*  972 */       Runnable r = new Runnable()
/*      */         {
/*      */           public void run() {
/*  975 */             BasicResourcePool.this.rpes.fireResourceCheckedOut(resc, pool_size, available_size, removed_but_unreturned_size); }
/*      */         };
/*  977 */       this.asyncEventQueue.postRunnable(r);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void asyncFireResourceRemoved(final Object resc, final boolean checked_out_resource, final int pool_size, final int available_size, final int removed_but_unreturned_size) {
/*  988 */     if (canFireEvents()) {
/*      */ 
/*      */ 
/*      */       
/*  992 */       Runnable r = new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  996 */             BasicResourcePool.this.rpes.fireResourceRemoved(resc, checked_out_resource, pool_size, available_size, removed_but_unreturned_size);
/*      */           }
/*      */         };
/*      */       
/* 1000 */       this.asyncEventQueue.postRunnable(r);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void destroyResource(Object resc) {
/* 1006 */     destroyResource(resc, false);
/*      */   }
/*      */   
/*      */   private void destroyResource(Object resc, boolean synchronous) {
/* 1010 */     destroyResource(resc, synchronous, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void destroyResource(final Object resc, boolean synchronous, final boolean checked_out) {
/*      */     class DestroyResourceTask
/*      */       implements Runnable
/*      */     {
/*      */       public void run() {
/*      */         try {
/* 1021 */           if (BasicResourcePool.logger.isLoggable(MLevel.FINER)) {
/* 1022 */             BasicResourcePool.logger.log(MLevel.FINER, "Preparing to destroy resource: " + resc);
/*      */           }
/* 1024 */           BasicResourcePool.this.mgr.destroyResource(resc, checked_out);
/*      */           
/* 1026 */           if (BasicResourcePool.logger.isLoggable(MLevel.FINER)) {
/* 1027 */             BasicResourcePool.logger.log(MLevel.FINER, "Successfully destroyed resource: " + resc);
/*      */           }
/* 1029 */         } catch (Exception e) {
/*      */           
/* 1031 */           if (BasicResourcePool.logger.isLoggable(MLevel.WARNING)) {
/* 1032 */             BasicResourcePool.logger.log(MLevel.WARNING, "Failed to destroy resource: " + resc, e);
/*      */           }
/*      */         } 
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */     
/* 1040 */     Runnable r = new DestroyResourceTask();
/* 1041 */     if (synchronous || this.broken) {
/*      */       
/* 1043 */       if (logger.isLoggable(MLevel.FINEST) && !this.broken && Boolean.TRUE.equals(ThreadUtils.reflectiveHoldsLock(this))) {
/* 1044 */         logger.log(MLevel.FINEST, this + ": Destroyiong a resource on an active pool, synchronousy while holding pool's lock! " + "(not a bug, but a potential bottleneck... is there a good reason for this?)", new Exception("DEBUG STACK TRACE: resource destruction while holding lock."));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1049 */       r.run();
/*      */     } else {
/*      */       
/*      */       try {
/* 1053 */         this.taskRunner.postRunnable(r);
/* 1054 */       } catch (Exception e) {
/*      */         
/* 1056 */         if (logger.isLoggable(MLevel.FINER)) {
/* 1057 */           logger.log(MLevel.FINER, "AsynchronousRunner refused to accept task to destroy resource. It is probably shared, and has probably been closed underneath us. Reverting to synchronous destruction. This is not usually a problem.", e);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1062 */         destroyResource(resc, true);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void doAcquire() throws Exception {
/* 1070 */     doAcquire(0);
/*      */   }
/*      */   private void doAcquireAndDecrementPendingAcquiresWithinLockOnSuccess() throws Exception {
/* 1073 */     doAcquire(1);
/*      */   }
/*      */   private void doAcquireAndDecrementPendingAcquiresWithinLockAlways() throws Exception {
/* 1076 */     doAcquire(2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doAcquire(int decrement_policy) throws Exception {
/* 1084 */     assert !Thread.holdsLock(this);
/*      */     
/* 1086 */     Object resc = this.mgr.acquireResource();
/*      */     
/* 1088 */     boolean destroy = false;
/*      */ 
/*      */     
/* 1091 */     synchronized (this) {
/*      */ 
/*      */       
/*      */       try {
/* 1095 */         int msz = this.managed.size();
/* 1096 */         if (!this.broken && msz < this.target_pool_size) {
/* 1097 */           assimilateResource(resc);
/*      */         } else {
/* 1099 */           destroy = true;
/*      */         } 
/* 1101 */         if (decrement_policy == 1) {
/* 1102 */           _decrementPendingAcquires();
/*      */         }
/*      */       } finally {
/*      */         
/* 1106 */         if (decrement_policy == 2) {
/* 1107 */           _decrementPendingAcquires();
/*      */         }
/*      */       } 
/*      */     } 
/* 1111 */     if (destroy) {
/*      */       
/*      */       try {
/*      */         
/* 1115 */         this.mgr.destroyResource(resc, false);
/* 1116 */         if (logger.isLoggable(MLevel.FINER)) {
/* 1117 */           logger.log(MLevel.FINER, "destroying overacquired resource: " + resc);
/*      */         }
/* 1119 */       } catch (Exception e) {
/*      */         
/* 1121 */         if (logger.isLoggable(MLevel.FINE)) {
/* 1122 */           logger.log(MLevel.FINE, "An exception occurred while trying to destroy an overacquired resource: " + resc, e);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setPoolSize(int sz) throws ResourcePoolException {
/*      */     try {
/* 1132 */       setTargetPoolSize(sz);
/* 1133 */       while (this.managed.size() != sz) {
/* 1134 */         wait();
/*      */       }
/* 1136 */     } catch (Exception e) {
/*      */       
/* 1138 */       String msg = "An exception occurred while trying to set the pool size!";
/* 1139 */       if (logger.isLoggable(MLevel.FINER))
/* 1140 */         logger.log(MLevel.FINER, msg, e); 
/* 1141 */       throw ResourcePoolUtils.convertThrowable(msg, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTargetPoolSize(int sz) {
/* 1147 */     if (sz > this.max)
/*      */     {
/* 1149 */       throw new IllegalArgumentException("Requested size [" + sz + "] is greater than max [" + this.max + "].");
/*      */     }
/*      */ 
/*      */     
/* 1153 */     if (sz < this.min)
/*      */     {
/* 1155 */       throw new IllegalArgumentException("Requested size [" + sz + "] is less than min [" + this.min + "].");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1160 */     this.target_pool_size = sz;
/*      */     
/* 1162 */     _recheckResizePool();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void markBrokenNoEnsureMinResources(Object resc) {
/* 1196 */     assert Thread.holdsLock(this);
/*      */ 
/*      */     
/*      */     try {
/* 1200 */       _markBroken(resc);
/*      */     }
/* 1202 */     catch (ResourceClosedException e) {
/*      */ 
/*      */       
/* 1205 */       if (logger.isLoggable(MLevel.SEVERE))
/* 1206 */         logger.log(MLevel.SEVERE, "Apparent pool break.", (Throwable)e); 
/* 1207 */       unexpectedBreak();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void _markBroken(Object resc) {
/* 1214 */     assert Thread.holdsLock(this);
/*      */     
/* 1216 */     if (this.unused.contains(resc)) {
/* 1217 */       removeResource(resc);
/*      */     } else {
/* 1219 */       excludeResource(resc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void close(boolean close_checked_out_resources) {
/* 1227 */     if (!this.broken) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1233 */       this.broken = true;
/* 1234 */       final Collection<?> cleanupResources = close_checked_out_resources ? cloneOfManaged().keySet() : cloneOfUnused();
/* 1235 */       if (this.cullTask != null)
/* 1236 */         this.cullTask.cancel(); 
/* 1237 */       if (this.idleRefurbishTask != null) {
/* 1238 */         this.idleRefurbishTask.cancel();
/*      */       }
/* 1240 */       for (Iterator ii = cleanupResources.iterator(); ii.hasNext();) {
/* 1241 */         addToFormerResources(ii.next());
/*      */       }
/* 1243 */       this.managed.keySet().removeAll(cleanupResources);
/* 1244 */       this.unused.removeAll(cleanupResources);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1253 */       Thread resourceDestroyer = new Thread("Resource Destroyer in BasicResourcePool.close()")
/*      */         {
/*      */           public void run()
/*      */           {
/* 1257 */             for (Iterator ii = cleanupResources.iterator(); ii.hasNext();) {
/*      */ 
/*      */               
/*      */               try {
/* 1261 */                 Object resc = ii.next();
/*      */ 
/*      */                 
/* 1264 */                 BasicResourcePool.this.destroyResource(resc, true);
/*      */               }
/* 1266 */               catch (Exception e) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1271 */                 if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
/* 1272 */                   BasicResourcePool.logger.log(MLevel.FINE, "BasicResourcePool -- A resource couldn't be cleaned up on close()", e);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*      */         };
/* 1278 */       resourceDestroyer.start();
/*      */       
/* 1280 */       for (Iterator<Thread> iterator2 = this.acquireWaiters.iterator(); iterator2.hasNext();)
/* 1281 */         ((Thread)iterator2.next()).interrupt(); 
/* 1282 */       for (Iterator<Thread> iterator1 = this.otherWaiters.iterator(); iterator1.hasNext();)
/* 1283 */         ((Thread)iterator1.next()).interrupt(); 
/* 1284 */       if (this.factory != null) {
/* 1285 */         this.factory.markBroken(this);
/*      */ 
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1291 */     else if (logger.isLoggable(MLevel.WARNING)) {
/* 1292 */       logger.warning(this + " -- close() called multiple times.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doCheckinManaged(final Object resc) throws ResourcePoolException {
/* 1306 */     assert Thread.holdsLock(this);
/*      */ 
/*      */ 
/*      */     
/* 1310 */     if (this.unused.contains(resc))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1318 */       throw new ResourcePoolException("Tried to check-in an already checked-in resource: " + resc);
/*      */     }
/*      */     
/* 1321 */     if (this.broken) {
/* 1322 */       removeResource(resc, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1359 */       Runnable doMe = new RefurbishCheckinResourceTask();
/* 1360 */       this.taskRunner.postRunnable(doMe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doCheckinExcluded(Object resc) {
/* 1368 */     assert Thread.holdsLock(this);
/*      */     
/* 1370 */     this.excluded.remove(resc);
/* 1371 */     destroyResource(resc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void awaitAvailable(long timeout) throws InterruptedException, TimeoutException, ResourcePoolException {
/* 1379 */     assert Thread.holdsLock(this);
/*      */     
/* 1381 */     if (this.force_kill_acquires) {
/* 1382 */       throw new ResourcePoolException("A ResourcePool cannot acquire a new resource -- the factory or source appears to be down.");
/*      */     }
/* 1384 */     Thread t = Thread.currentThread();
/*      */     
/*      */     try {
/* 1387 */       this.acquireWaiters.add(t);
/*      */ 
/*      */       
/* 1390 */       long start = (timeout > 0L) ? System.currentTimeMillis() : -1L;
/*      */ 
/*      */       
/* 1393 */       if (logger.isLoggable(MLevel.FINE)) {
/* 1394 */         logger.fine("awaitAvailable(): " + ((this.exampleResource != null) ? (String)this.exampleResource : "[unknown]"));
/*      */       }
/*      */ 
/*      */       
/* 1398 */       trace();
/*      */       int avail;
/* 1400 */       while ((avail = this.unused.size()) == 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1415 */         if (this.pending_acquires == 0 && this.managed.size() < this.max) {
/* 1416 */           _recheckResizePool();
/*      */         }
/* 1418 */         wait(timeout);
/* 1419 */         if (timeout > 0L && System.currentTimeMillis() - start > timeout)
/* 1420 */           throw new TimeoutException("A client timed out while waiting to acquire a resource from " + this + " -- timeout at awaitAvailable()"); 
/* 1421 */         if (this.force_kill_acquires)
/* 1422 */           throw new CannotAcquireResourceException("A ResourcePool could not acquire a resource from its primary factory or source."); 
/* 1423 */         ensureNotBroken();
/*      */       }
/*      */     
/*      */     } finally {
/*      */       
/* 1428 */       this.acquireWaiters.remove(t);
/* 1429 */       if (this.acquireWaiters.size() == 0) {
/* 1430 */         notifyAll();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void assimilateResource(Object resc) throws Exception {
/* 1436 */     assert Thread.holdsLock(this);
/*      */     
/* 1438 */     this.managed.put(resc, new PunchCard());
/* 1439 */     this.unused.add(0, resc);
/*      */     
/* 1441 */     asyncFireResourceAcquired(resc, this.managed.size(), this.unused.size(), this.excluded.size());
/* 1442 */     notifyAll();
/* 1443 */     trace();
/* 1444 */     if (this.exampleResource == null) {
/* 1445 */       this.exampleResource = resc;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void synchronousRemoveArbitraryResource() {
/* 1451 */     assert !Thread.holdsLock(this);
/*      */     
/* 1453 */     Object removeMe = null;
/*      */     
/* 1455 */     synchronized (this) {
/*      */       
/* 1457 */       if (this.unused.size() > 0) {
/*      */         
/* 1459 */         removeMe = this.unused.get(0);
/* 1460 */         this.managed.remove(removeMe);
/* 1461 */         this.unused.remove(removeMe);
/*      */       }
/*      */       else {
/*      */         
/* 1465 */         Set checkedOut = cloneOfManaged().keySet();
/* 1466 */         if (checkedOut.isEmpty()) {
/*      */           
/* 1468 */           unexpectedBreak();
/* 1469 */           logger.severe("A pool from which a resource is requested to be removed appears to have no managed resources?!");
/*      */         } else {
/*      */           
/* 1472 */           excludeResource(checkedOut.iterator().next());
/*      */         } 
/*      */       } 
/*      */     } 
/* 1476 */     if (removeMe != null)
/* 1477 */       destroyResource(removeMe, true); 
/*      */   }
/*      */   
/*      */   private void removeResource(Object resc) {
/* 1481 */     removeResource(resc, false);
/*      */   }
/*      */   
/*      */   private void removeResource(Object resc, boolean synchronous) {
/* 1485 */     assert Thread.holdsLock(this);
/*      */     
/* 1487 */     PunchCard pc = (PunchCard)this.managed.remove(resc);
/*      */     
/* 1489 */     boolean checked_out = false;
/* 1490 */     if (pc != null) {
/*      */       
/* 1492 */       checked_out = (pc.checkout_time > 0L);
/* 1493 */       if (checked_out && !this.broken)
/*      */       {
/* 1495 */         if (logger.isLoggable(MLevel.INFO))
/*      */         {
/* 1497 */           logger.info("A checked-out resource is overdue, and will be destroyed: " + resc);
/* 1498 */           if (pc.checkoutStackTraceException != null)
/*      */           {
/* 1500 */             logger.log(MLevel.INFO, "Logging the stack trace by which the overdue resource was checked-out.", pc.checkoutStackTraceException);
/*      */           
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */     }
/* 1507 */     else if (logger.isLoggable(MLevel.FINE)) {
/* 1508 */       logger.fine("Resource " + resc + " was removed twice. (Lotsa reasons a resource can be removed, sometimes simultaneously. It's okay)");
/*      */     } 
/* 1510 */     this.unused.remove(resc);
/* 1511 */     destroyResource(resc, synchronous, checked_out);
/* 1512 */     addToFormerResources(resc);
/* 1513 */     asyncFireResourceRemoved(resc, false, this.managed.size(), this.unused.size(), this.excluded.size());
/*      */     
/* 1515 */     trace();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void excludeResource(Object resc) {
/* 1523 */     assert Thread.holdsLock(this);
/*      */     
/* 1525 */     this.managed.remove(resc);
/* 1526 */     this.excluded.add(resc);
/* 1527 */     if (this.unused.contains(resc))
/* 1528 */       throw new InternalError("We should only \"exclude\" checked-out resources!"); 
/* 1529 */     if (logger.isLoggable(MLevel.FINEST))
/* 1530 */       logger.log(MLevel.FINEST, "Excluded resource " + resc, new Exception("DEBUG STACK TRACE: Excluded resource stack trace")); 
/* 1531 */     asyncFireResourceRemoved(resc, true, this.managed.size(), this.unused.size(), this.excluded.size());
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeTowards(int new_sz) {
/* 1536 */     assert Thread.holdsLock(this);
/*      */     
/* 1538 */     int num_to_remove = this.managed.size() - new_sz;
/* 1539 */     int count = 0;
/* 1540 */     Iterator ii = cloneOfUnused().iterator();
/* 1541 */     for (; ii.hasNext() && count < num_to_remove; 
/* 1542 */       count++) {
/*      */       
/* 1544 */       Object resc = ii.next();
/* 1545 */       removeResource(resc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void cullExpired() {
/* 1551 */     assert Thread.holdsLock(this);
/*      */     
/* 1553 */     if (logger.isLoggable(MLevel.FINER)) {
/* 1554 */       logger.log(MLevel.FINER, "BEGIN check for expired resources.  [" + this + "]");
/*      */     }
/*      */     
/* 1557 */     Collection checkMe = (this.destroy_unreturned_resc_time > 0L) ? cloneOfManaged().keySet() : cloneOfUnused();
/*      */     
/* 1559 */     for (Iterator ii = checkMe.iterator(); ii.hasNext(); ) {
/*      */       
/* 1561 */       Object resc = ii.next();
/* 1562 */       if (shouldExpire(resc)) {
/*      */         
/* 1564 */         if (logger.isLoggable(MLevel.FINER)) {
/* 1565 */           logger.log(MLevel.FINER, "Removing expired resource: " + resc + " [" + this + "]");
/*      */         }
/* 1567 */         this.target_pool_size = Math.max(this.min, this.target_pool_size - 1);
/*      */         
/* 1569 */         removeResource(resc);
/*      */         
/* 1571 */         trace();
/*      */       } 
/*      */     } 
/* 1574 */     if (logger.isLoggable(MLevel.FINER))
/* 1575 */       logger.log(MLevel.FINER, "FINISHED check for expired resources.  [" + this + "]"); 
/* 1576 */     ensureMinResources();
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIdleResources() {
/* 1581 */     assert Thread.holdsLock(this);
/*      */     
/* 1583 */     List u = cloneOfUnused();
/* 1584 */     for (Iterator ii = u.iterator(); ii.hasNext(); ) {
/*      */       
/* 1586 */       Object resc = ii.next();
/* 1587 */       if (this.idleCheckResources.add(resc)) {
/* 1588 */         this.taskRunner.postRunnable(new AsyncTestIdleResourceTask(resc));
/*      */       }
/*      */     } 
/* 1591 */     trace();
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean shouldExpire(Object resc) {
/* 1596 */     assert Thread.holdsLock(this);
/*      */     
/* 1598 */     boolean expired = false;
/*      */     
/* 1600 */     PunchCard pc = (PunchCard)this.managed.get(resc);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1607 */     if (pc == null) {
/*      */       
/* 1609 */       if (logger.isLoggable(MLevel.FINE))
/* 1610 */         logger.fine("Resource " + resc + " was being tested for expiration, but has already been removed from the pool."); 
/* 1611 */       return true;
/*      */     } 
/*      */     
/* 1614 */     long now = System.currentTimeMillis();
/*      */     
/* 1616 */     if (pc.checkout_time < 0L) {
/*      */       
/* 1618 */       long idle_age = now - pc.last_checkin_time;
/* 1619 */       if (this.excess_max_idle_time > 0L) {
/*      */         
/* 1621 */         int msz = this.managed.size();
/* 1622 */         expired = (msz > this.min && idle_age > this.excess_max_idle_time);
/* 1623 */         if (expired && logger.isLoggable(MLevel.FINER)) {
/* 1624 */           logger.log(MLevel.FINER, "EXPIRED excess idle resource: " + resc + " ---> idle_time: " + idle_age + "; excess_max_idle_time: " + this.excess_max_idle_time + "; pool_size: " + msz + "; min_pool_size: " + this.min + " [" + this + "]");
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1632 */       if (!expired && this.max_idle_time > 0L) {
/*      */         
/* 1634 */         expired = (idle_age > this.max_idle_time);
/* 1635 */         if (expired && logger.isLoggable(MLevel.FINER)) {
/* 1636 */           logger.log(MLevel.FINER, "EXPIRED idle resource: " + resc + " ---> idle_time: " + idle_age + "; max_idle_time: " + this.max_idle_time + " [" + this + "]");
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1642 */       if (!expired && this.max_resource_age > 0L)
/*      */       {
/* 1644 */         long abs_age = now - pc.acquisition_time;
/* 1645 */         expired = (abs_age > this.max_resource_age);
/*      */         
/* 1647 */         if (expired && logger.isLoggable(MLevel.FINER)) {
/* 1648 */           logger.log(MLevel.FINER, "EXPIRED old resource: " + resc + " ---> absolute_age: " + abs_age + "; max_absolute_age: " + this.max_resource_age + " [" + this + "]");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1657 */       long checkout_age = now - pc.checkout_time;
/* 1658 */       expired = (checkout_age > this.destroy_unreturned_resc_time);
/*      */     } 
/*      */     
/* 1661 */     return expired;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void ensureStartResources() {
/* 1674 */     recheckResizePool();
/*      */   }
/*      */   
/*      */   private void ensureMinResources() {
/* 1678 */     recheckResizePool();
/*      */   }
/*      */   
/*      */   private boolean attemptRefurbishResourceOnCheckout(Object resc) {
/* 1682 */     assert !Thread.holdsLock(this);
/*      */ 
/*      */     
/*      */     try {
/* 1686 */       this.mgr.refurbishResourceOnCheckout(resc);
/* 1687 */       return true;
/*      */     }
/* 1689 */     catch (Exception e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1695 */       if (logger.isLoggable(MLevel.FINE)) {
/* 1696 */         logger.log(MLevel.FINE, "A resource could not be refurbished for checkout. [" + resc + ']', e);
/*      */       }
/* 1698 */       synchronized (this) {
/*      */         
/* 1700 */         this.failed_checkouts++;
/* 1701 */         setLastCheckoutFailure(e);
/*      */       } 
/* 1703 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean attemptRefurbishResourceOnCheckin(Object resc) {
/* 1709 */     assert !Thread.holdsLock(this);
/*      */ 
/*      */     
/*      */     try {
/* 1713 */       this.mgr.refurbishResourceOnCheckin(resc);
/* 1714 */       return true;
/*      */     }
/* 1716 */     catch (Exception e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1722 */       if (logger.isLoggable(MLevel.FINE)) {
/* 1723 */         logger.log(MLevel.FINE, "A resource could not be refurbished on checkin. [" + resc + ']', e);
/*      */       }
/* 1725 */       synchronized (this) {
/*      */         
/* 1727 */         this.failed_checkins++;
/* 1728 */         setLastCheckinFailure(e);
/*      */       } 
/* 1730 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void ensureNotBroken() throws ResourcePoolException {
/* 1736 */     assert Thread.holdsLock(this);
/*      */     
/* 1738 */     if (this.broken) {
/* 1739 */       throw new ResourcePoolException("Attempted to use a closed or broken resource pool");
/*      */     }
/*      */   }
/*      */   
/*      */   private void trace() {
/* 1744 */     assert Thread.holdsLock(this);
/*      */     
/* 1746 */     if (logger.isLoggable(MLevel.FINEST)) {
/*      */       
/* 1748 */       String exampleResStr = (this.exampleResource == null) ? "" : (" (e.g. " + this.exampleResource + ")");
/*      */ 
/*      */       
/* 1751 */       logger.finest("trace " + this + " [managed: " + this.managed.size() + ", " + "unused: " + this.unused.size() + ", excluded: " + this.excluded.size() + ']' + exampleResStr);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final HashMap cloneOfManaged() {
/* 1759 */     assert Thread.holdsLock(this);
/*      */     
/* 1761 */     return (HashMap)this.managed.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   private final LinkedList cloneOfUnused() {
/* 1766 */     assert Thread.holdsLock(this);
/*      */     
/* 1768 */     return (LinkedList)this.unused.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   private final HashSet cloneOfExcluded() {
/* 1773 */     assert Thread.holdsLock(this);
/*      */     
/* 1775 */     return (HashSet)this.excluded.clone();
/*      */   }
/*      */   
/*      */   class ScatteredAcquireTask
/*      */     implements Runnable {
/*      */     int attempts_remaining;
/*      */     
/*      */     ScatteredAcquireTask() {
/* 1783 */       this((BasicResourcePool.this.num_acq_attempts >= 0) ? BasicResourcePool.this.num_acq_attempts : -1, true);
/*      */     }
/*      */     
/*      */     private ScatteredAcquireTask(int attempts_remaining, boolean first_attempt) {
/* 1787 */       this.attempts_remaining = attempts_remaining;
/* 1788 */       if (first_attempt) {
/*      */         
/* 1790 */         BasicResourcePool.this.incrementPendingAcquires();
/* 1791 */         if (BasicResourcePool.logger.isLoggable(MLevel.FINEST)) {
/* 1792 */           BasicResourcePool.logger.finest("Starting acquisition series. Incremented pending_acquires [" + BasicResourcePool.this.pending_acquires + "], " + " attempts_remaining: " + attempts_remaining);
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1797 */       else if (BasicResourcePool.logger.isLoggable(MLevel.FINEST)) {
/* 1798 */         BasicResourcePool.logger.finest("Continuing acquisition series. pending_acquires [" + BasicResourcePool.this.pending_acquires + "], " + " attempts_remaining: " + attempts_remaining);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/* 1805 */       boolean recheck = false;
/*      */       
/*      */       try {
/* 1808 */         boolean fkap = BasicResourcePool.this.isForceKillAcquiresPending();
/* 1809 */         if (!fkap) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1814 */           BasicResourcePool.this.doAcquireAndDecrementPendingAcquiresWithinLockOnSuccess();
/*      */         }
/*      */         else {
/*      */           
/* 1818 */           BasicResourcePool.this.decrementPendingAcquires();
/* 1819 */           recheck = true;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1828 */           if (BasicResourcePool.logger.isLoggable(MLevel.FINEST)) {
/* 1829 */             BasicResourcePool.logger.finest("Acquisition series terminated " + (fkap ? "because force-kill-acquires is pending" : "successfully") + ". Decremented pending_acquires [" + BasicResourcePool.this.pending_acquires + "], " + " attempts_remaining: " + this.attempts_remaining);
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 1834 */         catch (Exception e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1842 */           System.err.println("Exception during logging:");
/* 1843 */           e.printStackTrace();
/*      */         }
/*      */       
/* 1846 */       } catch (Exception e) {
/*      */         
/* 1848 */         BasicResourcePool.this.setLastAcquisitionFailure(e);
/*      */         
/* 1850 */         if (this.attempts_remaining == 0)
/*      */         {
/* 1852 */           BasicResourcePool.this.decrementPendingAcquires();
/* 1853 */           if (BasicResourcePool.logger.isLoggable(MLevel.WARNING))
/*      */           {
/* 1855 */             BasicResourcePool.logger.log(MLevel.WARNING, this + " -- Acquisition Attempt Failed!!! Clearing pending acquires. " + "While trying to acquire a needed new resource, we failed " + "to succeed more than the maximum number of allowed " + "acquisition attempts (" + BasicResourcePool.this.num_acq_attempts + "). " + "Last acquisition attempt exception: ", e);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1864 */           if (BasicResourcePool.this.break_on_acquisition_failure) {
/*      */ 
/*      */             
/* 1867 */             if (BasicResourcePool.logger.isLoggable(MLevel.SEVERE)) {
/* 1868 */               BasicResourcePool.logger.severe("A RESOURCE POOL IS PERMANENTLY BROKEN! [" + this + "] " + "(because a series of " + BasicResourcePool.this.num_acq_attempts + " acquisition attempts " + "failed.)");
/*      */             }
/*      */             
/* 1871 */             BasicResourcePool.this.unexpectedBreak();
/*      */           } else {
/*      */             
/*      */             try {
/* 1875 */               BasicResourcePool.this.forceKillAcquires();
/* 1876 */             } catch (InterruptedException ie) {
/*      */               
/* 1878 */               if (BasicResourcePool.logger.isLoggable(MLevel.WARNING)) {
/* 1879 */                 BasicResourcePool.logger.log(MLevel.WARNING, "Failed to force-kill pending acquisition attempts after acquisition failue,  due to an InterruptedException!", ie);
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1886 */               recheck = true;
/*      */             } 
/*      */           } 
/* 1889 */           if (BasicResourcePool.logger.isLoggable(MLevel.FINEST)) {
/* 1890 */             BasicResourcePool.logger.finest("Acquisition series terminated unsuccessfully. Decremented pending_acquires [" + BasicResourcePool.this.pending_acquires + "], " + " attempts_remaining: " + this.attempts_remaining);
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */         else
/*      */         {
/*      */           
/* 1900 */           MLevel logLevel = (this.attempts_remaining > 0) ? MLevel.FINE : MLevel.INFO;
/* 1901 */           if (BasicResourcePool.logger.isLoggable(logLevel)) {
/* 1902 */             BasicResourcePool.logger.log(logLevel, "An exception occurred while acquiring a poolable resource. Will retry.", e);
/*      */           }
/* 1904 */           TimerTask doNextAcquire = new TimerTask()
/*      */             {
/*      */               public void run() {
/* 1907 */                 BasicResourcePool.this.taskRunner.postRunnable(new BasicResourcePool.ScatteredAcquireTask(BasicResourcePool.ScatteredAcquireTask.this.attempts_remaining - 1, false)); }
/*      */             };
/* 1909 */           BasicResourcePool.this.cullAndIdleRefurbishTimer.schedule(doNextAcquire, BasicResourcePool.this.acq_attempt_delay);
/*      */         }
/*      */       
/*      */       } finally {
/*      */         
/* 1914 */         if (recheck) {
/* 1915 */           BasicResourcePool.this.recheckResizePool();
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class AcquireTask
/*      */     implements Runnable
/*      */   {
/*      */     boolean success = false;
/*      */ 
/*      */     
/*      */     public AcquireTask() {
/* 1929 */       BasicResourcePool.this.incrementPendingAcquires();
/*      */     }
/*      */     
/*      */     public void run() {
/* 1933 */       boolean decremented = false;
/* 1934 */       boolean recheck = false;
/*      */       
/*      */       try {
/* 1937 */         Exception lastException = null;
/* 1938 */         for (int i = 0; shouldTry(i); i++) {
/*      */ 
/*      */           
/*      */           try {
/* 1942 */             if (i > 0) {
/* 1943 */               Thread.sleep(BasicResourcePool.this.acq_attempt_delay);
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 1948 */             if (goodAttemptNumber(i + 1)) {
/*      */               
/* 1950 */               BasicResourcePool.this.doAcquireAndDecrementPendingAcquiresWithinLockOnSuccess();
/* 1951 */               decremented = true;
/*      */             }
/*      */             else {
/*      */               
/* 1955 */               decremented = true;
/* 1956 */               recheck = true;
/* 1957 */               BasicResourcePool.this.doAcquireAndDecrementPendingAcquiresWithinLockAlways();
/*      */             } 
/*      */             
/* 1960 */             this.success = true;
/*      */           }
/* 1962 */           catch (InterruptedException e) {
/*      */ 
/*      */ 
/*      */             
/* 1966 */             throw e;
/*      */           }
/* 1968 */           catch (Exception e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1977 */             MLevel logLevel = (BasicResourcePool.this.num_acq_attempts > 0) ? MLevel.FINE : MLevel.INFO;
/* 1978 */             if (BasicResourcePool.logger.isLoggable(logLevel)) {
/* 1979 */               BasicResourcePool.logger.log(logLevel, "An exception occurred while acquiring a poolable resource. Will retry.", e);
/*      */             }
/* 1981 */             lastException = e;
/* 1982 */             BasicResourcePool.this.setLastAcquisitionFailure(e);
/*      */           } 
/*      */         } 
/* 1985 */         if (!this.success) {
/*      */           
/* 1987 */           if (BasicResourcePool.logger.isLoggable(MLevel.WARNING))
/*      */           {
/* 1989 */             BasicResourcePool.logger.log(MLevel.WARNING, this + " -- Acquisition Attempt Failed!!! Clearing pending acquires. " + "While trying to acquire a needed new resource, we failed " + "to succeed more than the maximum number of allowed " + "acquisition attempts (" + BasicResourcePool.this.num_acq_attempts + "). " + ((lastException == null) ? "" : "Last acquisition attempt exception: "), lastException);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1997 */           if (BasicResourcePool.this.break_on_acquisition_failure) {
/*      */ 
/*      */             
/* 2000 */             if (BasicResourcePool.logger.isLoggable(MLevel.SEVERE))
/* 2001 */               BasicResourcePool.logger.severe("A RESOURCE POOL IS PERMANENTLY BROKEN! [" + this + "]"); 
/* 2002 */             BasicResourcePool.this.unexpectedBreak();
/*      */           } else {
/*      */             
/* 2005 */             BasicResourcePool.this.forceKillAcquires();
/*      */           } 
/*      */         } else {
/* 2008 */           BasicResourcePool.this.recheckResizePool();
/*      */         } 
/* 2010 */       } catch (ResourceClosedException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2015 */         if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
/* 2016 */           BasicResourcePool.logger.log(MLevel.FINE, "a resource pool async thread died.", (Throwable)e);
/*      */         }
/* 2018 */         BasicResourcePool.this.unexpectedBreak();
/*      */       }
/* 2020 */       catch (InterruptedException e) {
/*      */         
/* 2022 */         if (BasicResourcePool.logger.isLoggable(MLevel.WARNING))
/*      */         {
/* 2024 */           BasicResourcePool.logger.log(MLevel.WARNING, BasicResourcePool.this + " -- Thread unexpectedly interrupted while performing an acquisition attempt.", e);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2032 */         BasicResourcePool.this.recheckResizePool();
/*      */       }
/*      */       finally {
/*      */         
/* 2036 */         if (!decremented)
/* 2037 */           BasicResourcePool.this.decrementPendingAcquires(); 
/* 2038 */         if (recheck) {
/* 2039 */           BasicResourcePool.this.recheckResizePool();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean shouldTry(int attempt_num) {
/* 2049 */       return (!this.success && !BasicResourcePool.this.isForceKillAcquiresPending() && goodAttemptNumber(attempt_num));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean goodAttemptNumber(int attempt_num) {
/* 2056 */       return (BasicResourcePool.this.num_acq_attempts <= 0 || attempt_num < BasicResourcePool.this.num_acq_attempts);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class RemoveTask
/*      */     implements Runnable
/*      */   {
/*      */     public RemoveTask() {
/* 2070 */       BasicResourcePool.this.incrementPendingRemoves();
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*      */       try {
/* 2076 */         BasicResourcePool.this.synchronousRemoveArbitraryResource();
/* 2077 */         BasicResourcePool.this.recheckResizePool();
/*      */       } finally {
/*      */         
/* 2080 */         BasicResourcePool.this.decrementPendingRemoves();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   class CullTask
/*      */     extends TimerTask
/*      */   {
/*      */     public void run() {
/*      */       try {
/* 2090 */         if (BasicResourcePool.logger.isLoggable(MLevel.FINER))
/* 2091 */           BasicResourcePool.logger.log(MLevel.FINER, "Checking for expired resources - " + new Date() + " [" + BasicResourcePool.this + "]"); 
/* 2092 */         synchronized (BasicResourcePool.this) {
/* 2093 */           BasicResourcePool.this.cullExpired();
/*      */         } 
/* 2095 */       } catch (ResourceClosedException e) {
/*      */ 
/*      */ 
/*      */         
/* 2099 */         if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
/* 2100 */           BasicResourcePool.logger.log(MLevel.FINE, "a resource pool async thread died.", (Throwable)e);
/*      */         }
/* 2102 */         BasicResourcePool.this.unexpectedBreak();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class CheckIdleResourcesTask
/*      */     extends TimerTask
/*      */   {
/*      */     public void run() {
/*      */       try {
/* 2117 */         if (BasicResourcePool.logger.isLoggable(MLevel.FINER))
/* 2118 */           BasicResourcePool.logger.log(MLevel.FINER, "Refurbishing idle resources - " + new Date() + " [" + BasicResourcePool.this + "]"); 
/* 2119 */         synchronized (BasicResourcePool.this) {
/* 2120 */           BasicResourcePool.this.checkIdleResources();
/*      */         } 
/* 2122 */       } catch (ResourceClosedException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2127 */         if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
/* 2128 */           BasicResourcePool.logger.log(MLevel.FINE, "a resource pool async thread died.", (Throwable)e);
/*      */         }
/* 2130 */         BasicResourcePool.this.unexpectedBreak();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class AsyncTestIdleResourceTask
/*      */     implements Runnable
/*      */   {
/*      */     Object resc;
/*      */     
/*      */     boolean pending = true;
/*      */     boolean failed;
/*      */     
/*      */     AsyncTestIdleResourceTask(Object resc) {
/* 2145 */       this.resc = resc;
/*      */     }
/*      */     
/*      */     public void run() {
/* 2149 */       assert !Thread.holdsLock(BasicResourcePool.this);
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*      */         try {
/* 2155 */           BasicResourcePool.this.mgr.refurbishIdleResource(this.resc);
/*      */         }
/* 2157 */         catch (Exception e) {
/*      */           
/* 2159 */           if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
/* 2160 */             BasicResourcePool.logger.log(MLevel.FINE, "BasicResourcePool: An idle resource is broken and will be purged. [" + this.resc + ']', e);
/*      */           }
/* 2162 */           synchronized (BasicResourcePool.this)
/*      */           {
/* 2164 */             if (BasicResourcePool.this.managed.keySet().contains(this.resc)) {
/*      */               
/* 2166 */               BasicResourcePool.this.removeResource(this.resc);
/* 2167 */               BasicResourcePool.this.ensureMinResources();
/*      */             } 
/*      */             
/* 2170 */             BasicResourcePool.this.failed_idle_tests++;
/* 2171 */             BasicResourcePool.this.setLastIdleCheckFailure(e);
/*      */           }
/*      */         
/*      */         } 
/*      */       } finally {
/*      */         
/* 2177 */         synchronized (BasicResourcePool.this) {
/*      */           
/* 2179 */           BasicResourcePool.this.idleCheckResources.remove(this.resc);
/* 2180 */           BasicResourcePool.this.notifyAll();
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class PunchCard
/*      */   {
/* 2195 */     long acquisition_time = System.currentTimeMillis();
/* 2196 */     long last_checkin_time = this.acquisition_time;
/* 2197 */     long checkout_time = -1L;
/* 2198 */     Exception checkoutStackTraceException = null;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/BasicResourcePool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */