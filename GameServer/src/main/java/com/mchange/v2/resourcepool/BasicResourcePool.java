package com.mchange.v2.resourcepool;

import com.mchange.v2.async.AsynchronousRunner;
import com.mchange.v2.async.RunnableQueue;
import com.mchange.v2.cfg.MConfig;
import com.mchange.v2.lang.ThreadUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.util.ResourceClosedException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

class BasicResourcePool
implements ResourcePool
{
private static final MLogger logger = MLog.getLogger(BasicResourcePool.class);

static final int AUTO_CULL_FREQUENCY_DIVISOR = 4;
static final int AUTO_MAX_CULL_FREQUENCY = 900000;
static final int AUTO_MIN_CULL_FREQUENCY = 1000;
static final String USE_SCATTERED_ACQUIRE_TASK_KEY = "com.mchange.v2.resourcepool.experimental.useScatteredAcquireTask";
static final boolean USE_SCATTERED_ACQUIRE_TASK;
final ResourcePool.Manager mgr;
final int start;
final int min;
final int max;
final int inc;

static {
String checkScattered = MConfig.readVmConfig().getProperty("com.mchange.v2.resourcepool.experimental.useScatteredAcquireTask");
if (checkScattered != null && checkScattered.trim().toLowerCase().equals("false")) {

USE_SCATTERED_ACQUIRE_TASK = false;
if (logger.isLoggable(MLevel.INFO)) {
logger.info(BasicResourcePool.class.getName() + " using traditional, Thread-blocking AcquireTask. Yuk. Why?");
}
} else {
USE_SCATTERED_ACQUIRE_TASK = true;
} 
}

final int num_acq_attempts;

final int acq_attempt_delay;

final long check_idle_resources_delay;

final long max_resource_age;

final long max_idle_time;

final long excess_max_idle_time;

final long destroy_unreturned_resc_time;

final long expiration_enforcement_delay;

final boolean break_on_acquisition_failure;

final boolean debug_store_checkout_exceptions;

final long pool_start_time = System.currentTimeMillis();

final BasicResourcePoolFactory factory;

final AsynchronousRunner taskRunner;

final RunnableQueue asyncEventQueue;

final ResourcePoolEventSupport rpes;
Timer cullAndIdleRefurbishTimer;
TimerTask cullTask;
TimerTask idleRefurbishTask;
HashSet acquireWaiters = new HashSet();
HashSet otherWaiters = new HashSet();

int pending_acquires;

int pending_removes;

int target_pool_size;

HashMap managed = new HashMap<Object, Object>();

LinkedList unused = new LinkedList();

HashSet excluded = new HashSet();

Map formerResources = new WeakHashMap<Object, Object>();

Set idleCheckResources = new HashSet();

boolean force_kill_acquires = false;

boolean broken = false;

long failed_checkins = 0L;
long failed_checkouts = 0L;
long failed_idle_tests = 0L;

Throwable lastCheckinFailure = null;
Throwable lastCheckoutFailure = null;
Throwable lastIdleTestFailure = null;
Throwable lastResourceTestFailure = null;

Throwable lastAcquisitionFailiure = null; Object exampleResource;
private static final int NO_DECREMENT = 0;
private static final int DECREMENT_ON_SUCCESS = 1;
private static final int DECREMENT_WITH_CERTAINTY = 2;

public long getStartTime() {
return this.pool_start_time;
}
public long getUpTime() {
return System.currentTimeMillis() - this.pool_start_time;
}
public synchronized long getNumFailedCheckins() {
return this.failed_checkins;
}
public synchronized long getNumFailedCheckouts() {
return this.failed_checkouts;
}
public synchronized long getNumFailedIdleTests() {
return this.failed_idle_tests;
}
public synchronized Throwable getLastCheckinFailure() {
return this.lastCheckinFailure;
}

private void setLastCheckinFailure(Throwable t) {
assert Thread.holdsLock(this);

this.lastCheckinFailure = t;
this.lastResourceTestFailure = t;
}

public synchronized Throwable getLastCheckoutFailure() {
return this.lastCheckoutFailure;
}

private void setLastCheckoutFailure(Throwable t) {
assert Thread.holdsLock(this);

this.lastCheckoutFailure = t;
this.lastResourceTestFailure = t;
}

public synchronized Throwable getLastIdleCheckFailure() {
return this.lastIdleTestFailure;
}

private void setLastIdleCheckFailure(Throwable t) {
assert Thread.holdsLock(this);

this.lastIdleTestFailure = t;
this.lastResourceTestFailure = t;
}

public synchronized Throwable getLastResourceTestFailure() {
return this.lastResourceTestFailure;
}
public synchronized Throwable getLastAcquisitionFailure() {
return this.lastAcquisitionFailiure;
}

private synchronized void setLastAcquisitionFailure(Throwable t) {
this.lastAcquisitionFailiure = t;
}
public synchronized int getNumCheckoutWaiters() {
return this.acquireWaiters.size();
}
public synchronized int getNumPendingAcquireTasks() {
return this.pending_acquires;
}
public synchronized int getNumPendingRemoveTasks() {
return this.pending_removes;
}
public synchronized int getNumThreadsWaitingForResources() {
return this.acquireWaiters.size();
}

public synchronized String[] getThreadNamesWaitingForResources() {
int len = this.acquireWaiters.size();
String[] out = new String[len];
int i = 0;
for (Iterator<Thread> ii = this.acquireWaiters.iterator(); ii.hasNext();)
out[i++] = ((Thread)ii.next()).getName(); 
Arrays.sort((Object[])out);
return out;
}

public synchronized int getNumThreadsWaitingForAdministrativeTasks() {
return this.otherWaiters.size();
}

public synchronized String[] getThreadNamesWaitingForAdministrativeTasks() {
int len = this.otherWaiters.size();
String[] out = new String[len];
int i = 0;
for (Iterator<Thread> ii = this.otherWaiters.iterator(); ii.hasNext();)
out[i++] = ((Thread)ii.next()).getName(); 
Arrays.sort((Object[])out);
return out;
}

private void addToFormerResources(Object resc) {
this.formerResources.put(resc, null);
}
private boolean isFormerResource(Object resc) {
return this.formerResources.keySet().contains(resc);
}

public BasicResourcePool(ResourcePool.Manager mgr, int start, int min, int max, int inc, int num_acq_attempts, int acq_attempt_delay, long check_idle_resources_delay, long max_resource_age, long max_idle_time, long excess_max_idle_time, long destroy_unreturned_resc_time, long expiration_enforcement_delay, boolean break_on_acquisition_failure, boolean debug_store_checkout_exceptions, AsynchronousRunner taskRunner, RunnableQueue asyncEventQueue, Timer cullAndIdleRefurbishTimer, BasicResourcePoolFactory factory) throws ResourcePoolException {
try {
this.mgr = mgr;
this.start = start;
this.min = min;
this.max = max;
this.inc = inc;
this.num_acq_attempts = num_acq_attempts;
this.acq_attempt_delay = acq_attempt_delay;
this.check_idle_resources_delay = check_idle_resources_delay;
this.max_resource_age = max_resource_age;
this.max_idle_time = max_idle_time;
this.excess_max_idle_time = excess_max_idle_time;
this.destroy_unreturned_resc_time = destroy_unreturned_resc_time;

this.break_on_acquisition_failure = break_on_acquisition_failure;
this.debug_store_checkout_exceptions = (debug_store_checkout_exceptions && destroy_unreturned_resc_time > 0L);
this.taskRunner = taskRunner;
this.asyncEventQueue = asyncEventQueue;
this.cullAndIdleRefurbishTimer = cullAndIdleRefurbishTimer;
this.factory = factory;

this.pending_acquires = 0;
this.pending_removes = 0;

this.target_pool_size = Math.max(start, min);

if (asyncEventQueue != null) {
this.rpes = new ResourcePoolEventSupport(this);
} else {
this.rpes = null;
} 

ensureStartResources();

if (mustEnforceExpiration()) {

if (expiration_enforcement_delay <= 0L) {
this.expiration_enforcement_delay = automaticExpirationEnforcementDelay();
} else {
this.expiration_enforcement_delay = expiration_enforcement_delay;
} 
this.cullTask = new CullTask();

cullAndIdleRefurbishTimer.schedule(this.cullTask, minExpirationTime(), this.expiration_enforcement_delay);
} else {

this.expiration_enforcement_delay = expiration_enforcement_delay;
} 

if (check_idle_resources_delay > 0L) {

this.idleRefurbishTask = new CheckIdleResourcesTask();
cullAndIdleRefurbishTimer.schedule(this.idleRefurbishTask, check_idle_resources_delay, check_idle_resources_delay);
} 

if (logger.isLoggable(MLevel.FINER)) {
logger.finer(this + " config: [start -> " + this.start + "; min -> " + this.min + "; max -> " + this.max + "; inc -> " + this.inc + "; num_acq_attempts -> " + this.num_acq_attempts + "; acq_attempt_delay -> " + this.acq_attempt_delay + "; check_idle_resources_delay -> " + this.check_idle_resources_delay + "; mox_resource_age -> " + this.max_resource_age + "; max_idle_time -> " + this.max_idle_time + "; excess_max_idle_time -> " + this.excess_max_idle_time + "; destroy_unreturned_resc_time -> " + this.destroy_unreturned_resc_time + "; expiration_enforcement_delay -> " + this.expiration_enforcement_delay + "; break_on_acquisition_failure -> " + this.break_on_acquisition_failure + "; debug_store_checkout_exceptions -> " + this.debug_store_checkout_exceptions + "]");

}

}
catch (Exception e) {

throw ResourcePoolUtils.convertThrowable(e);
} 
}

private boolean mustTestIdleResources() {
return (this.check_idle_resources_delay > 0L);
}

private boolean mustEnforceExpiration() {
return (this.max_resource_age > 0L || this.max_idle_time > 0L || this.excess_max_idle_time > 0L || this.destroy_unreturned_resc_time > 0L);
}

private long minExpirationTime() {
long out = Long.MAX_VALUE;
if (this.max_resource_age > 0L)
out = Math.min(out, this.max_resource_age); 
if (this.max_idle_time > 0L)
out = Math.min(out, this.max_idle_time); 
if (this.excess_max_idle_time > 0L)
out = Math.min(out, this.excess_max_idle_time); 
if (this.destroy_unreturned_resc_time > 0L)
out = Math.min(out, this.destroy_unreturned_resc_time); 
return out;
}

private long automaticExpirationEnforcementDelay() {
long out = minExpirationTime();
out /= 4L;
out = Math.min(out, 900000L);
out = Math.max(out, 1000L);
return out;
}

public long getEffectiveExpirationEnforcementDelay() {
return this.expiration_enforcement_delay;
}
private synchronized boolean isBroken() {
return this.broken;
}

private boolean supportsEvents() {
return (this.asyncEventQueue != null);
}

public Object checkoutResource() throws ResourcePoolException, InterruptedException {
try {
return checkoutResource(0L);
} catch (TimeoutException e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Huh??? TimeoutException with no timeout set!!!", (Throwable)e);
}
throw new ResourcePoolException("Huh??? TimeoutException with no timeout set!!!", e);
} 
}

private void _recheckResizePool() {
assert Thread.holdsLock(this);

if (!this.broken) {

int msz = this.managed.size();

int shrink_count;

if ((shrink_count = msz - this.pending_removes - this.target_pool_size) > 0)
{ shrinkPool(shrink_count); }
else { int expand_count; if ((expand_count = this.target_pool_size - msz + this.pending_acquires) > 0)
expandPool(expand_count);  }

} 
}

private synchronized void incrementPendingAcquires() {
this.pending_acquires++;

if (logger.isLoggable(MLevel.FINEST)) {
logger.finest("incremented pending_acquires: " + this.pending_acquires);
}
}

private synchronized void incrementPendingRemoves() {
this.pending_removes++;

if (logger.isLoggable(MLevel.FINEST)) {
logger.finest("incremented pending_removes: " + this.pending_removes);
}
}

private synchronized void decrementPendingAcquires() {
_decrementPendingAcquires();
}

private void _decrementPendingAcquires() {
this.pending_acquires--;

if (logger.isLoggable(MLevel.FINEST)) {
logger.finest("decremented pending_acquires: " + this.pending_acquires);
}
}

private synchronized void decrementPendingRemoves() {
this.pending_removes--;

if (logger.isLoggable(MLevel.FINEST)) {
logger.finest("decremented pending_removes: " + this.pending_removes);
}
}

private synchronized void recheckResizePool() {
_recheckResizePool();
}

private void expandPool(int count) {
assert Thread.holdsLock(this);

if (USE_SCATTERED_ACQUIRE_TASK) {

for (int i = 0; i < count; i++) {
this.taskRunner.postRunnable(new ScatteredAcquireTask());
}
} else {

for (int i = 0; i < count; i++) {
this.taskRunner.postRunnable(new AcquireTask());
}
} 
}

private void shrinkPool(int count) {
assert Thread.holdsLock(this);

for (int i = 0; i < count; i++) {
this.taskRunner.postRunnable(new RemoveTask());
}
}

public Object checkoutResource(long timeout) throws TimeoutException, ResourcePoolException, InterruptedException {
Object resc = prelimCheckoutResource(timeout);

boolean refurb = attemptRefurbishResourceOnCheckout(resc);

synchronized (this) {

if (!refurb) {

removeResource(resc);
ensureMinResources();
resc = null;
}
else {

asyncFireResourceCheckedOut(resc, this.managed.size(), this.unused.size(), this.excluded.size());
trace();

PunchCard card = (PunchCard)this.managed.get(resc);
if (card == null) {

if (logger.isLoggable(MLevel.FINE)) {
logger.fine("Resource " + resc + " was removed from the pool while it was being checked out " + " or refurbished for checkout.");
}
resc = null;
}
else {

card.checkout_time = System.currentTimeMillis();
if (this.debug_store_checkout_exceptions) {
card.checkoutStackTraceException = new Exception("DEBUG STACK TRACE: Overdue resource check-out stack trace.");
}
} 
} 
} 

if (resc == null) {
return checkoutResource(timeout);
}
return resc;
}

private synchronized Object prelimCheckoutResource(long timeout) throws TimeoutException, ResourcePoolException, InterruptedException {
try {
ensureNotBroken();

int available = this.unused.size();
if (available == 0) {

int msz = this.managed.size();

if (msz < this.max) {

int desired_target = msz + this.acquireWaiters.size() + 1;

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "acquire test -- pool size: " + msz + "; target_pool_size: " + this.target_pool_size + "; desired target? " + desired_target);
}
if (desired_target >= this.target_pool_size)
{

desired_target = Math.max(desired_target, this.target_pool_size + this.inc);

this.target_pool_size = Math.max(Math.min(this.max, desired_target), this.min);

_recheckResizePool();

}

}
else if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "acquire test -- pool is already maxed out. [managed: " + msz + "; max: " + this.max + "]");
} 

awaitAvailable(timeout);
} 

Object resc = this.unused.get(0);

if (this.idleCheckResources.contains(resc)) {

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "Resource we want to check out is in idleCheck! (waiting until idle-check completes.) [" + this + "]");
}

Thread t = Thread.currentThread();

try {
this.otherWaiters.add(t);
wait(timeout);
ensureNotBroken();
} finally {

this.otherWaiters.remove(t);
}  return prelimCheckoutResource(timeout);
} 
if (shouldExpire(resc)) {

removeResource(resc);
ensureMinResources();
return prelimCheckoutResource(timeout);
} 

this.unused.remove(0);
return resc;

}
catch (ResourceClosedException e) {

if (logger.isLoggable(MLevel.SEVERE)) {
logger.log(MLevel.SEVERE, this + " -- the pool was found to be closed or broken during an attempt to check out a resource.", (Throwable)e);
}
unexpectedBreak();
throw e;
}
catch (InterruptedException e) {

if (this.broken) {

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, this + " -- an attempt to checkout a resource was interrupted, because the pool is now closed. " + "[Thread: " + Thread.currentThread().getName() + ']', e);

}
else if (logger.isLoggable(MLevel.INFO)) {
logger.log(MLevel.INFO, this + " -- an attempt to checkout a resource was interrupted, because the pool is now closed. " + "[Thread: " + Thread.currentThread().getName() + ']');

}

}
else if (logger.isLoggable(MLevel.WARNING)) {

logger.log(MLevel.WARNING, this + " -- an attempt to checkout a resource was interrupted, and the pool is still live: some other thread " + "must have either interrupted the Thread attempting checkout!", e);
} 

throw e;
} 
}

public synchronized void checkinResource(Object resc) throws ResourcePoolException {
try {
if (this.managed.keySet().contains(resc)) {
doCheckinManaged(resc);
} else if (this.excluded.contains(resc)) {
doCheckinExcluded(resc);
} else if (isFormerResource(resc)) {

if (logger.isLoggable(MLevel.FINER)) {
logger.finer("Resource " + resc + " checked-in after having been checked-in already, or checked-in after " + " having being destroyed for being checked-out too long.");
}
} else {

throw new ResourcePoolException("ResourcePool" + (this.broken ? " [BROKEN!]" : "") + ": Tried to check-in a foreign resource!");
}  trace();
}
catch (ResourceClosedException e) {

if (logger.isLoggable(MLevel.SEVERE)) {
logger.log(MLevel.SEVERE, this + " - checkinResource( ... ) -- even broken pools should allow checkins without exception. probable resource pool bug.", (Throwable)e);
}

unexpectedBreak();
throw e;
} 
}

public synchronized void checkinAll() throws ResourcePoolException {
try {
Set checkedOutNotExcluded = new HashSet(this.managed.keySet());
checkedOutNotExcluded.removeAll(this.unused); Iterator ii;
for (ii = checkedOutNotExcluded.iterator(); ii.hasNext();)
doCheckinManaged(ii.next()); 
for (ii = this.excluded.iterator(); ii.hasNext();) {
doCheckinExcluded(ii.next());
}
} catch (ResourceClosedException e) {

if (logger.isLoggable(MLevel.SEVERE)) {
logger.log(MLevel.SEVERE, this + " - checkinAll() -- even broken pools should allow checkins without exception. probable resource pool bug.", (Throwable)e);
}

unexpectedBreak();
throw e;
} 
}

public synchronized int statusInPool(Object resc) throws ResourcePoolException {
try {
if (this.unused.contains(resc))
return 0; 
if (this.managed.keySet().contains(resc) || this.excluded.contains(resc)) {
return 1;
}
return -1;
}
catch (ResourceClosedException e) {

if (logger.isLoggable(MLevel.SEVERE))
logger.log(MLevel.SEVERE, "Apparent pool break.", (Throwable)e); 
unexpectedBreak();
throw e;
} 
}

public synchronized void markBroken(Object resc) {
try {
if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "Resource " + resc + " marked broken by pool (" + this + ").");
}
_markBroken(resc);
ensureMinResources();
}
catch (ResourceClosedException e) {

if (logger.isLoggable(MLevel.SEVERE))
logger.log(MLevel.SEVERE, "Apparent pool break.", (Throwable)e); 
unexpectedBreak();
} 
}

public int getMinPoolSize() {
return this.min;
}

public int getMaxPoolSize() {
return this.max;
}

public synchronized int getPoolSize() throws ResourcePoolException {
return this.managed.size();
}

public synchronized int getAvailableCount() {
return this.unused.size();
}
public synchronized int getExcludedCount() {
return this.excluded.size();
}
public synchronized int getAwaitingCheckinCount() {
return this.managed.size() - this.unused.size() + this.excluded.size();
}

public synchronized void resetPool() {
try {
for (Iterator ii = cloneOfManaged().keySet().iterator(); ii.hasNext();)
markBrokenNoEnsureMinResources(ii.next()); 
ensureMinResources();
}
catch (ResourceClosedException e) {

if (logger.isLoggable(MLevel.SEVERE))
logger.log(MLevel.SEVERE, "Apparent pool break.", (Throwable)e); 
unexpectedBreak();
} 
}

public synchronized void close() throws ResourcePoolException {
close(true);
}

public void finalize() throws Throwable {
if (!this.broken) {
close();
}
}

public void addResourcePoolListener(ResourcePoolListener rpl) {
if (!supportsEvents()) {
throw new RuntimeException(this + " does not support ResourcePoolEvents. " + "Probably it was constructed by a BasicResourceFactory configured not to support such events.");
}

this.rpes.addResourcePoolListener(rpl);
}

public void removeResourcePoolListener(ResourcePoolListener rpl) {
if (!supportsEvents()) {
throw new RuntimeException(this + " does not support ResourcePoolEvents. " + "Probably it was constructed by a BasicResourceFactory configured not to support such events.");
}

this.rpes.removeResourcePoolListener(rpl);
}

private synchronized boolean isForceKillAcquiresPending() {
return this.force_kill_acquires;
}

private synchronized void forceKillAcquires() throws InterruptedException {
if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Having failed to acquire a resource, " + this + " is interrupting all Threads waiting on a resource to check out. " + "Will try again in response to new client requests.");
}

Thread t = Thread.currentThread();

try {
this.force_kill_acquires = true;
notifyAll();
while (this.acquireWaiters.size() > 0) {

this.otherWaiters.add(t);
wait();
} 
this.force_kill_acquires = false;
} finally {

this.otherWaiters.remove(t);
} 
}

private synchronized void unexpectedBreak() {
if (logger.isLoggable(MLevel.SEVERE))
logger.log(MLevel.SEVERE, this + " -- Unexpectedly broken!!!", (Throwable)new ResourcePoolException("Unexpected Break Stack Trace!")); 
close(false);
}

private boolean canFireEvents() {
return (this.asyncEventQueue != null && !isBroken());
}

private void asyncFireResourceAcquired(final Object resc, final int pool_size, final int available_size, final int removed_but_unreturned_size) {
if (canFireEvents()) {

Runnable r = new Runnable()
{
public void run() {
BasicResourcePool.this.rpes.fireResourceAcquired(resc, pool_size, available_size, removed_but_unreturned_size); }
};
this.asyncEventQueue.postRunnable(r);
} 
}

private void asyncFireResourceCheckedIn(final Object resc, final int pool_size, final int available_size, final int removed_but_unreturned_size) {
if (canFireEvents()) {

Runnable r = new Runnable()
{
public void run() {
BasicResourcePool.this.rpes.fireResourceCheckedIn(resc, pool_size, available_size, removed_but_unreturned_size); }
};
this.asyncEventQueue.postRunnable(r);
} 
}

private void asyncFireResourceCheckedOut(final Object resc, final int pool_size, final int available_size, final int removed_but_unreturned_size) {
if (canFireEvents()) {

Runnable r = new Runnable()
{
public void run() {
BasicResourcePool.this.rpes.fireResourceCheckedOut(resc, pool_size, available_size, removed_but_unreturned_size); }
};
this.asyncEventQueue.postRunnable(r);
} 
}

private void asyncFireResourceRemoved(final Object resc, final boolean checked_out_resource, final int pool_size, final int available_size, final int removed_but_unreturned_size) {
if (canFireEvents()) {

Runnable r = new Runnable()
{
public void run()
{
BasicResourcePool.this.rpes.fireResourceRemoved(resc, checked_out_resource, pool_size, available_size, removed_but_unreturned_size);
}
};

this.asyncEventQueue.postRunnable(r);
} 
}

private void destroyResource(Object resc) {
destroyResource(resc, false);
}

private void destroyResource(Object resc, boolean synchronous) {
destroyResource(resc, synchronous, false);
}

private void destroyResource(final Object resc, boolean synchronous, final boolean checked_out) {
class DestroyResourceTask
implements Runnable
{
public void run() {
try {
if (BasicResourcePool.logger.isLoggable(MLevel.FINER)) {
BasicResourcePool.logger.log(MLevel.FINER, "Preparing to destroy resource: " + resc);
}
BasicResourcePool.this.mgr.destroyResource(resc, checked_out);

if (BasicResourcePool.logger.isLoggable(MLevel.FINER)) {
BasicResourcePool.logger.log(MLevel.FINER, "Successfully destroyed resource: " + resc);
}
} catch (Exception e) {

if (BasicResourcePool.logger.isLoggable(MLevel.WARNING)) {
BasicResourcePool.logger.log(MLevel.WARNING, "Failed to destroy resource: " + resc, e);
}
} 
}
};

Runnable r = new DestroyResourceTask();
if (synchronous || this.broken) {

if (logger.isLoggable(MLevel.FINEST) && !this.broken && Boolean.TRUE.equals(ThreadUtils.reflectiveHoldsLock(this))) {
logger.log(MLevel.FINEST, this + ": Destroyiong a resource on an active pool, synchronousy while holding pool's lock! " + "(not a bug, but a potential bottleneck... is there a good reason for this?)", new Exception("DEBUG STACK TRACE: resource destruction while holding lock."));
}

r.run();
} else {

try {
this.taskRunner.postRunnable(r);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "AsynchronousRunner refused to accept task to destroy resource. It is probably shared, and has probably been closed underneath us. Reverting to synchronous destruction. This is not usually a problem.", e);
}

destroyResource(resc, true);
} 
} 
}

private void doAcquire() throws Exception {
doAcquire(0);
}
private void doAcquireAndDecrementPendingAcquiresWithinLockOnSuccess() throws Exception {
doAcquire(1);
}
private void doAcquireAndDecrementPendingAcquiresWithinLockAlways() throws Exception {
doAcquire(2);
}

private void doAcquire(int decrement_policy) throws Exception {
assert !Thread.holdsLock(this);

Object resc = this.mgr.acquireResource();

boolean destroy = false;

synchronized (this) {

try {
int msz = this.managed.size();
if (!this.broken && msz < this.target_pool_size) {
assimilateResource(resc);
} else {
destroy = true;
} 
if (decrement_policy == 1) {
_decrementPendingAcquires();
}
} finally {

if (decrement_policy == 2) {
_decrementPendingAcquires();
}
} 
} 
if (destroy) {

try {

this.mgr.destroyResource(resc, false);
if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "destroying overacquired resource: " + resc);
}
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "An exception occurred while trying to destroy an overacquired resource: " + resc, e);
}
} 
}
}

public synchronized void setPoolSize(int sz) throws ResourcePoolException {
try {
setTargetPoolSize(sz);
while (this.managed.size() != sz) {
wait();
}
} catch (Exception e) {

String msg = "An exception occurred while trying to set the pool size!";
if (logger.isLoggable(MLevel.FINER))
logger.log(MLevel.FINER, msg, e); 
throw ResourcePoolUtils.convertThrowable(msg, e);
} 
}

public synchronized void setTargetPoolSize(int sz) {
if (sz > this.max)
{
throw new IllegalArgumentException("Requested size [" + sz + "] is greater than max [" + this.max + "].");
}

if (sz < this.min)
{
throw new IllegalArgumentException("Requested size [" + sz + "] is less than min [" + this.min + "].");
}

this.target_pool_size = sz;

_recheckResizePool();
}

private void markBrokenNoEnsureMinResources(Object resc) {
assert Thread.holdsLock(this);

try {
_markBroken(resc);
}
catch (ResourceClosedException e) {

if (logger.isLoggable(MLevel.SEVERE))
logger.log(MLevel.SEVERE, "Apparent pool break.", (Throwable)e); 
unexpectedBreak();
} 
}

private void _markBroken(Object resc) {
assert Thread.holdsLock(this);

if (this.unused.contains(resc)) {
removeResource(resc);
} else {
excludeResource(resc);
} 
}

public synchronized void close(boolean close_checked_out_resources) {
if (!this.broken) {

this.broken = true;
final Collection<?> cleanupResources = close_checked_out_resources ? cloneOfManaged().keySet() : cloneOfUnused();
if (this.cullTask != null)
this.cullTask.cancel(); 
if (this.idleRefurbishTask != null) {
this.idleRefurbishTask.cancel();
}
for (Iterator ii = cleanupResources.iterator(); ii.hasNext();) {
addToFormerResources(ii.next());
}
this.managed.keySet().removeAll(cleanupResources);
this.unused.removeAll(cleanupResources);

Thread resourceDestroyer = new Thread("Resource Destroyer in BasicResourcePool.close()")
{
public void run()
{
for (Iterator ii = cleanupResources.iterator(); ii.hasNext();) {

try {
Object resc = ii.next();

BasicResourcePool.this.destroyResource(resc, true);
}
catch (Exception e) {

if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
BasicResourcePool.logger.log(MLevel.FINE, "BasicResourcePool -- A resource couldn't be cleaned up on close()", e);
}
} 
} 
}
};
resourceDestroyer.start();

for (Iterator<Thread> iterator2 = this.acquireWaiters.iterator(); iterator2.hasNext();)
((Thread)iterator2.next()).interrupt(); 
for (Iterator<Thread> iterator1 = this.otherWaiters.iterator(); iterator1.hasNext();)
((Thread)iterator1.next()).interrupt(); 
if (this.factory != null) {
this.factory.markBroken(this);

}

}
else if (logger.isLoggable(MLevel.WARNING)) {
logger.warning(this + " -- close() called multiple times.");
} 
}

private void doCheckinManaged(final Object resc) throws ResourcePoolException {
assert Thread.holdsLock(this);

if (this.unused.contains(resc))
{

throw new ResourcePoolException("Tried to check-in an already checked-in resource: " + resc);
}

if (this.broken) {
removeResource(resc, true);

}
else {

Runnable doMe = new RefurbishCheckinResourceTask();
this.taskRunner.postRunnable(doMe);
} 
}

private void doCheckinExcluded(Object resc) {
assert Thread.holdsLock(this);

this.excluded.remove(resc);
destroyResource(resc);
}

private void awaitAvailable(long timeout) throws InterruptedException, TimeoutException, ResourcePoolException {
assert Thread.holdsLock(this);

if (this.force_kill_acquires) {
throw new ResourcePoolException("A ResourcePool cannot acquire a new resource -- the factory or source appears to be down.");
}
Thread t = Thread.currentThread();

try {
this.acquireWaiters.add(t);

long start = (timeout > 0L) ? System.currentTimeMillis() : -1L;

if (logger.isLoggable(MLevel.FINE)) {
logger.fine("awaitAvailable(): " + ((this.exampleResource != null) ? (String)this.exampleResource : "[unknown]"));
}

trace();
int avail;
while ((avail = this.unused.size()) == 0)
{

if (this.pending_acquires == 0 && this.managed.size() < this.max) {
_recheckResizePool();
}
wait(timeout);
if (timeout > 0L && System.currentTimeMillis() - start > timeout)
throw new TimeoutException("A client timed out while waiting to acquire a resource from " + this + " -- timeout at awaitAvailable()"); 
if (this.force_kill_acquires)
throw new CannotAcquireResourceException("A ResourcePool could not acquire a resource from its primary factory or source."); 
ensureNotBroken();
}

} finally {

this.acquireWaiters.remove(t);
if (this.acquireWaiters.size() == 0) {
notifyAll();
}
} 
}

private void assimilateResource(Object resc) throws Exception {
assert Thread.holdsLock(this);

this.managed.put(resc, new PunchCard());
this.unused.add(0, resc);

asyncFireResourceAcquired(resc, this.managed.size(), this.unused.size(), this.excluded.size());
notifyAll();
trace();
if (this.exampleResource == null) {
this.exampleResource = resc;
}
}

private void synchronousRemoveArbitraryResource() {
assert !Thread.holdsLock(this);

Object removeMe = null;

synchronized (this) {

if (this.unused.size() > 0) {

removeMe = this.unused.get(0);
this.managed.remove(removeMe);
this.unused.remove(removeMe);
}
else {

Set checkedOut = cloneOfManaged().keySet();
if (checkedOut.isEmpty()) {

unexpectedBreak();
logger.severe("A pool from which a resource is requested to be removed appears to have no managed resources?!");
} else {

excludeResource(checkedOut.iterator().next());
} 
} 
} 
if (removeMe != null)
destroyResource(removeMe, true); 
}

private void removeResource(Object resc) {
removeResource(resc, false);
}

private void removeResource(Object resc, boolean synchronous) {
assert Thread.holdsLock(this);

PunchCard pc = (PunchCard)this.managed.remove(resc);

boolean checked_out = false;
if (pc != null) {

checked_out = (pc.checkout_time > 0L);
if (checked_out && !this.broken)
{
if (logger.isLoggable(MLevel.INFO))
{
logger.info("A checked-out resource is overdue, and will be destroyed: " + resc);
if (pc.checkoutStackTraceException != null)
{
logger.log(MLevel.INFO, "Logging the stack trace by which the overdue resource was checked-out.", pc.checkoutStackTraceException);

}
}

}
}
else if (logger.isLoggable(MLevel.FINE)) {
logger.fine("Resource " + resc + " was removed twice. (Lotsa reasons a resource can be removed, sometimes simultaneously. It's okay)");
} 
this.unused.remove(resc);
destroyResource(resc, synchronous, checked_out);
addToFormerResources(resc);
asyncFireResourceRemoved(resc, false, this.managed.size(), this.unused.size(), this.excluded.size());

trace();
}

private void excludeResource(Object resc) {
assert Thread.holdsLock(this);

this.managed.remove(resc);
this.excluded.add(resc);
if (this.unused.contains(resc))
throw new InternalError("We should only \"exclude\" checked-out resources!"); 
if (logger.isLoggable(MLevel.FINEST))
logger.log(MLevel.FINEST, "Excluded resource " + resc, new Exception("DEBUG STACK TRACE: Excluded resource stack trace")); 
asyncFireResourceRemoved(resc, true, this.managed.size(), this.unused.size(), this.excluded.size());
}

private void removeTowards(int new_sz) {
assert Thread.holdsLock(this);

int num_to_remove = this.managed.size() - new_sz;
int count = 0;
Iterator ii = cloneOfUnused().iterator();
for (; ii.hasNext() && count < num_to_remove; 
count++) {

Object resc = ii.next();
removeResource(resc);
} 
}

private void cullExpired() {
assert Thread.holdsLock(this);

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "BEGIN check for expired resources.  [" + this + "]");
}

Collection checkMe = (this.destroy_unreturned_resc_time > 0L) ? cloneOfManaged().keySet() : cloneOfUnused();

for (Iterator ii = checkMe.iterator(); ii.hasNext(); ) {

Object resc = ii.next();
if (shouldExpire(resc)) {

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "Removing expired resource: " + resc + " [" + this + "]");
}
this.target_pool_size = Math.max(this.min, this.target_pool_size - 1);

removeResource(resc);

trace();
} 
} 
if (logger.isLoggable(MLevel.FINER))
logger.log(MLevel.FINER, "FINISHED check for expired resources.  [" + this + "]"); 
ensureMinResources();
}

private void checkIdleResources() {
assert Thread.holdsLock(this);

List u = cloneOfUnused();
for (Iterator ii = u.iterator(); ii.hasNext(); ) {

Object resc = ii.next();
if (this.idleCheckResources.add(resc)) {
this.taskRunner.postRunnable(new AsyncTestIdleResourceTask(resc));
}
} 
trace();
}

private boolean shouldExpire(Object resc) {
assert Thread.holdsLock(this);

boolean expired = false;

PunchCard pc = (PunchCard)this.managed.get(resc);

if (pc == null) {

if (logger.isLoggable(MLevel.FINE))
logger.fine("Resource " + resc + " was being tested for expiration, but has already been removed from the pool."); 
return true;
} 

long now = System.currentTimeMillis();

if (pc.checkout_time < 0L) {

long idle_age = now - pc.last_checkin_time;
if (this.excess_max_idle_time > 0L) {

int msz = this.managed.size();
expired = (msz > this.min && idle_age > this.excess_max_idle_time);
if (expired && logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "EXPIRED excess idle resource: " + resc + " ---> idle_time: " + idle_age + "; excess_max_idle_time: " + this.excess_max_idle_time + "; pool_size: " + msz + "; min_pool_size: " + this.min + " [" + this + "]");
}
} 

if (!expired && this.max_idle_time > 0L) {

expired = (idle_age > this.max_idle_time);
if (expired && logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "EXPIRED idle resource: " + resc + " ---> idle_time: " + idle_age + "; max_idle_time: " + this.max_idle_time + " [" + this + "]");
}
} 

if (!expired && this.max_resource_age > 0L)
{
long abs_age = now - pc.acquisition_time;
expired = (abs_age > this.max_resource_age);

if (expired && logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "EXPIRED old resource: " + resc + " ---> absolute_age: " + abs_age + "; max_absolute_age: " + this.max_resource_age + " [" + this + "]");

}

}

}
else {

long checkout_age = now - pc.checkout_time;
expired = (checkout_age > this.destroy_unreturned_resc_time);
} 

return expired;
}

private void ensureStartResources() {
recheckResizePool();
}

private void ensureMinResources() {
recheckResizePool();
}

private boolean attemptRefurbishResourceOnCheckout(Object resc) {
assert !Thread.holdsLock(this);

try {
this.mgr.refurbishResourceOnCheckout(resc);
return true;
}
catch (Exception e) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "A resource could not be refurbished for checkout. [" + resc + ']', e);
}
synchronized (this) {

this.failed_checkouts++;
setLastCheckoutFailure(e);
} 
return false;
} 
}

private boolean attemptRefurbishResourceOnCheckin(Object resc) {
assert !Thread.holdsLock(this);

try {
this.mgr.refurbishResourceOnCheckin(resc);
return true;
}
catch (Exception e) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "A resource could not be refurbished on checkin. [" + resc + ']', e);
}
synchronized (this) {

this.failed_checkins++;
setLastCheckinFailure(e);
} 
return false;
} 
}

private void ensureNotBroken() throws ResourcePoolException {
assert Thread.holdsLock(this);

if (this.broken) {
throw new ResourcePoolException("Attempted to use a closed or broken resource pool");
}
}

private void trace() {
assert Thread.holdsLock(this);

if (logger.isLoggable(MLevel.FINEST)) {

String exampleResStr = (this.exampleResource == null) ? "" : (" (e.g. " + this.exampleResource + ")");

logger.finest("trace " + this + " [managed: " + this.managed.size() + ", " + "unused: " + this.unused.size() + ", excluded: " + this.excluded.size() + ']' + exampleResStr);
} 
}

private final HashMap cloneOfManaged() {
assert Thread.holdsLock(this);

return (HashMap)this.managed.clone();
}

private final LinkedList cloneOfUnused() {
assert Thread.holdsLock(this);

return (LinkedList)this.unused.clone();
}

private final HashSet cloneOfExcluded() {
assert Thread.holdsLock(this);

return (HashSet)this.excluded.clone();
}

class ScatteredAcquireTask
implements Runnable {
int attempts_remaining;

ScatteredAcquireTask() {
this((BasicResourcePool.this.num_acq_attempts >= 0) ? BasicResourcePool.this.num_acq_attempts : -1, true);
}

private ScatteredAcquireTask(int attempts_remaining, boolean first_attempt) {
this.attempts_remaining = attempts_remaining;
if (first_attempt) {

BasicResourcePool.this.incrementPendingAcquires();
if (BasicResourcePool.logger.isLoggable(MLevel.FINEST)) {
BasicResourcePool.logger.finest("Starting acquisition series. Incremented pending_acquires [" + BasicResourcePool.this.pending_acquires + "], " + " attempts_remaining: " + attempts_remaining);

}

}
else if (BasicResourcePool.logger.isLoggable(MLevel.FINEST)) {
BasicResourcePool.logger.finest("Continuing acquisition series. pending_acquires [" + BasicResourcePool.this.pending_acquires + "], " + " attempts_remaining: " + attempts_remaining);
} 
}

public void run() {
boolean recheck = false;

try {
boolean fkap = BasicResourcePool.this.isForceKillAcquiresPending();
if (!fkap) {

BasicResourcePool.this.doAcquireAndDecrementPendingAcquiresWithinLockOnSuccess();
}
else {

BasicResourcePool.this.decrementPendingAcquires();
recheck = true;
} 

try {
if (BasicResourcePool.logger.isLoggable(MLevel.FINEST)) {
BasicResourcePool.logger.finest("Acquisition series terminated " + (fkap ? "because force-kill-acquires is pending" : "successfully") + ". Decremented pending_acquires [" + BasicResourcePool.this.pending_acquires + "], " + " attempts_remaining: " + this.attempts_remaining);

}

}
catch (Exception e) {

System.err.println("Exception during logging:");
e.printStackTrace();
}

} catch (Exception e) {

BasicResourcePool.this.setLastAcquisitionFailure(e);

if (this.attempts_remaining == 0)
{
BasicResourcePool.this.decrementPendingAcquires();
if (BasicResourcePool.logger.isLoggable(MLevel.WARNING))
{
BasicResourcePool.logger.log(MLevel.WARNING, this + " -- Acquisition Attempt Failed!!! Clearing pending acquires. " + "While trying to acquire a needed new resource, we failed " + "to succeed more than the maximum number of allowed " + "acquisition attempts (" + BasicResourcePool.this.num_acq_attempts + "). " + "Last acquisition attempt exception: ", e);
}

if (BasicResourcePool.this.break_on_acquisition_failure) {

if (BasicResourcePool.logger.isLoggable(MLevel.SEVERE)) {
BasicResourcePool.logger.severe("A RESOURCE POOL IS PERMANENTLY BROKEN! [" + this + "] " + "(because a series of " + BasicResourcePool.this.num_acq_attempts + " acquisition attempts " + "failed.)");
}

BasicResourcePool.this.unexpectedBreak();
} else {

try {
BasicResourcePool.this.forceKillAcquires();
} catch (InterruptedException ie) {

if (BasicResourcePool.logger.isLoggable(MLevel.WARNING)) {
BasicResourcePool.logger.log(MLevel.WARNING, "Failed to force-kill pending acquisition attempts after acquisition failue,  due to an InterruptedException!", ie);
}

recheck = true;
} 
} 
if (BasicResourcePool.logger.isLoggable(MLevel.FINEST)) {
BasicResourcePool.logger.finest("Acquisition series terminated unsuccessfully. Decremented pending_acquires [" + BasicResourcePool.this.pending_acquires + "], " + " attempts_remaining: " + this.attempts_remaining);

}

}
else
{

MLevel logLevel = (this.attempts_remaining > 0) ? MLevel.FINE : MLevel.INFO;
if (BasicResourcePool.logger.isLoggable(logLevel)) {
BasicResourcePool.logger.log(logLevel, "An exception occurred while acquiring a poolable resource. Will retry.", e);
}
TimerTask doNextAcquire = new TimerTask()
{
public void run() {
BasicResourcePool.this.taskRunner.postRunnable(new BasicResourcePool.ScatteredAcquireTask(BasicResourcePool.ScatteredAcquireTask.this.attempts_remaining - 1, false)); }
};
BasicResourcePool.this.cullAndIdleRefurbishTimer.schedule(doNextAcquire, BasicResourcePool.this.acq_attempt_delay);
}

} finally {

if (recheck) {
BasicResourcePool.this.recheckResizePool();
}
} 
}
}

class AcquireTask
implements Runnable
{
boolean success = false;

public AcquireTask() {
BasicResourcePool.this.incrementPendingAcquires();
}

public void run() {
boolean decremented = false;
boolean recheck = false;

try {
Exception lastException = null;
for (int i = 0; shouldTry(i); i++) {

try {
if (i > 0) {
Thread.sleep(BasicResourcePool.this.acq_attempt_delay);
}

if (goodAttemptNumber(i + 1)) {

BasicResourcePool.this.doAcquireAndDecrementPendingAcquiresWithinLockOnSuccess();
decremented = true;
}
else {

decremented = true;
recheck = true;
BasicResourcePool.this.doAcquireAndDecrementPendingAcquiresWithinLockAlways();
} 

this.success = true;
}
catch (InterruptedException e) {

throw e;
}
catch (Exception e) {

MLevel logLevel = (BasicResourcePool.this.num_acq_attempts > 0) ? MLevel.FINE : MLevel.INFO;
if (BasicResourcePool.logger.isLoggable(logLevel)) {
BasicResourcePool.logger.log(logLevel, "An exception occurred while acquiring a poolable resource. Will retry.", e);
}
lastException = e;
BasicResourcePool.this.setLastAcquisitionFailure(e);
} 
} 
if (!this.success) {

if (BasicResourcePool.logger.isLoggable(MLevel.WARNING))
{
BasicResourcePool.logger.log(MLevel.WARNING, this + " -- Acquisition Attempt Failed!!! Clearing pending acquires. " + "While trying to acquire a needed new resource, we failed " + "to succeed more than the maximum number of allowed " + "acquisition attempts (" + BasicResourcePool.this.num_acq_attempts + "). " + ((lastException == null) ? "" : "Last acquisition attempt exception: "), lastException);
}

if (BasicResourcePool.this.break_on_acquisition_failure) {

if (BasicResourcePool.logger.isLoggable(MLevel.SEVERE))
BasicResourcePool.logger.severe("A RESOURCE POOL IS PERMANENTLY BROKEN! [" + this + "]"); 
BasicResourcePool.this.unexpectedBreak();
} else {

BasicResourcePool.this.forceKillAcquires();
} 
} else {
BasicResourcePool.this.recheckResizePool();
} 
} catch (ResourceClosedException e) {

if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
BasicResourcePool.logger.log(MLevel.FINE, "a resource pool async thread died.", (Throwable)e);
}
BasicResourcePool.this.unexpectedBreak();
}
catch (InterruptedException e) {

if (BasicResourcePool.logger.isLoggable(MLevel.WARNING))
{
BasicResourcePool.logger.log(MLevel.WARNING, BasicResourcePool.this + " -- Thread unexpectedly interrupted while performing an acquisition attempt.", e);
}

BasicResourcePool.this.recheckResizePool();
}
finally {

if (!decremented)
BasicResourcePool.this.decrementPendingAcquires(); 
if (recheck) {
BasicResourcePool.this.recheckResizePool();
}
} 
}

private boolean shouldTry(int attempt_num) {
return (!this.success && !BasicResourcePool.this.isForceKillAcquiresPending() && goodAttemptNumber(attempt_num));
}

private boolean goodAttemptNumber(int attempt_num) {
return (BasicResourcePool.this.num_acq_attempts <= 0 || attempt_num < BasicResourcePool.this.num_acq_attempts);
}
}

class RemoveTask
implements Runnable
{
public RemoveTask() {
BasicResourcePool.this.incrementPendingRemoves();
}

public void run() {
try {
BasicResourcePool.this.synchronousRemoveArbitraryResource();
BasicResourcePool.this.recheckResizePool();
} finally {

BasicResourcePool.this.decrementPendingRemoves();
} 
}
}

class CullTask
extends TimerTask
{
public void run() {
try {
if (BasicResourcePool.logger.isLoggable(MLevel.FINER))
BasicResourcePool.logger.log(MLevel.FINER, "Checking for expired resources - " + new Date() + " [" + BasicResourcePool.this + "]"); 
synchronized (BasicResourcePool.this) {
BasicResourcePool.this.cullExpired();
} 
} catch (ResourceClosedException e) {

if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
BasicResourcePool.logger.log(MLevel.FINE, "a resource pool async thread died.", (Throwable)e);
}
BasicResourcePool.this.unexpectedBreak();
} 
}
}

class CheckIdleResourcesTask
extends TimerTask
{
public void run() {
try {
if (BasicResourcePool.logger.isLoggable(MLevel.FINER))
BasicResourcePool.logger.log(MLevel.FINER, "Refurbishing idle resources - " + new Date() + " [" + BasicResourcePool.this + "]"); 
synchronized (BasicResourcePool.this) {
BasicResourcePool.this.checkIdleResources();
} 
} catch (ResourceClosedException e) {

if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
BasicResourcePool.logger.log(MLevel.FINE, "a resource pool async thread died.", (Throwable)e);
}
BasicResourcePool.this.unexpectedBreak();
} 
}
}

class AsyncTestIdleResourceTask
implements Runnable
{
Object resc;

boolean pending = true;
boolean failed;

AsyncTestIdleResourceTask(Object resc) {
this.resc = resc;
}

public void run() {
assert !Thread.holdsLock(BasicResourcePool.this);

try {
try {
BasicResourcePool.this.mgr.refurbishIdleResource(this.resc);
}
catch (Exception e) {

if (BasicResourcePool.logger.isLoggable(MLevel.FINE)) {
BasicResourcePool.logger.log(MLevel.FINE, "BasicResourcePool: An idle resource is broken and will be purged. [" + this.resc + ']', e);
}
synchronized (BasicResourcePool.this)
{
if (BasicResourcePool.this.managed.keySet().contains(this.resc)) {

BasicResourcePool.this.removeResource(this.resc);
BasicResourcePool.this.ensureMinResources();
} 

BasicResourcePool.this.failed_idle_tests++;
BasicResourcePool.this.setLastIdleCheckFailure(e);
}

} 
} finally {

synchronized (BasicResourcePool.this) {

BasicResourcePool.this.idleCheckResources.remove(this.resc);
BasicResourcePool.this.notifyAll();
} 
} 
}
}

static final class PunchCard
{
long acquisition_time = System.currentTimeMillis();
long last_checkin_time = this.acquisition_time;
long checkout_time = -1L;
Exception checkoutStackTraceException = null;
}
}

