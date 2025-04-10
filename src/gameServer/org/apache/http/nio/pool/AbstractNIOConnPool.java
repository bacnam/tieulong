package org.apache.http.nio.pool;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.SessionRequest;
import org.apache.http.nio.reactor.SessionRequestCallback;
import org.apache.http.pool.ConnPool;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolEntry;
import org.apache.http.pool.PoolEntryCallback;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public abstract class AbstractNIOConnPool<T, C, E extends PoolEntry<T, C>>
implements ConnPool<T, E>, ConnPoolControl<T>
{
private final ConnectingIOReactor ioreactor;
private final NIOConnFactory<T, C> connFactory;
private final SocketAddressResolver<T> addressResolver;
private final SessionRequestCallback sessionRequestCallback;
private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
private final LinkedList<LeaseRequest<T, C, E>> leasingRequests;
private final Set<SessionRequest> pending;
private final Set<E> leased;
private final LinkedList<E> available;
private final ConcurrentLinkedQueue<LeaseRequest<T, C, E>> completedRequests;
private final Map<T, Integer> maxPerRoute;
private final Lock lock;
private final AtomicBoolean isShutDown;
private volatile int defaultMaxPerRoute;
private volatile int maxTotal;

@Deprecated
public AbstractNIOConnPool(ConnectingIOReactor ioreactor, NIOConnFactory<T, C> connFactory, int defaultMaxPerRoute, int maxTotal) {
Args.notNull(ioreactor, "I/O reactor");
Args.notNull(connFactory, "Connection factory");
Args.positive(defaultMaxPerRoute, "Max per route value");
Args.positive(maxTotal, "Max total value");
this.ioreactor = ioreactor;
this.connFactory = connFactory;
this.addressResolver = new SocketAddressResolver<T>()
{
public SocketAddress resolveLocalAddress(T route) throws IOException
{
return AbstractNIOConnPool.this.resolveLocalAddress(route);
}

public SocketAddress resolveRemoteAddress(T route) throws IOException {
return AbstractNIOConnPool.this.resolveRemoteAddress(route);
}
};

this.sessionRequestCallback = new InternalSessionRequestCallback();
this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
this.leasingRequests = new LinkedList<LeaseRequest<T, C, E>>();
this.pending = new HashSet<SessionRequest>();
this.leased = new HashSet<E>();
this.available = new LinkedList<E>();
this.maxPerRoute = new HashMap<T, Integer>();
this.completedRequests = new ConcurrentLinkedQueue<LeaseRequest<T, C, E>>();
this.lock = new ReentrantLock();
this.isShutDown = new AtomicBoolean(false);
this.defaultMaxPerRoute = defaultMaxPerRoute;
this.maxTotal = maxTotal;
}

public AbstractNIOConnPool(ConnectingIOReactor ioreactor, NIOConnFactory<T, C> connFactory, SocketAddressResolver<T> addressResolver, int defaultMaxPerRoute, int maxTotal) {
Args.notNull(ioreactor, "I/O reactor");
Args.notNull(connFactory, "Connection factory");
Args.notNull(addressResolver, "Address resolver");
Args.positive(defaultMaxPerRoute, "Max per route value");
Args.positive(maxTotal, "Max total value");
this.ioreactor = ioreactor;
this.connFactory = connFactory;
this.addressResolver = addressResolver;
this.sessionRequestCallback = new InternalSessionRequestCallback();
this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
this.leasingRequests = new LinkedList<LeaseRequest<T, C, E>>();
this.pending = new HashSet<SessionRequest>();
this.leased = new HashSet<E>();
this.available = new LinkedList<E>();
this.completedRequests = new ConcurrentLinkedQueue<LeaseRequest<T, C, E>>();
this.maxPerRoute = new HashMap<T, Integer>();
this.lock = new ReentrantLock();
this.isShutDown = new AtomicBoolean(false);
this.defaultMaxPerRoute = defaultMaxPerRoute;
this.maxTotal = maxTotal;
}

@Deprecated
protected SocketAddress resolveRemoteAddress(T route) {
return null;
}

@Deprecated
protected SocketAddress resolveLocalAddress(T route) {
return null;
}

protected void onLease(E entry) {}

protected void onRelease(E entry) {}

protected void onReuse(E entry) {}

public boolean isShutdown() {
return this.isShutDown.get();
}

public void shutdown(long waitMs) throws IOException {
if (this.isShutDown.compareAndSet(false, true)) {
fireCallbacks();
this.lock.lock();
try {
for (SessionRequest sessionRequest : this.pending) {
sessionRequest.cancel();
}
for (PoolEntry poolEntry : this.available) {
poolEntry.close();
}
for (PoolEntry poolEntry : this.leased) {
poolEntry.close();
}
for (RouteSpecificPool<T, C, E> pool : this.routeToPool.values()) {
pool.shutdown();
}
this.routeToPool.clear();
this.leased.clear();
this.pending.clear();
this.available.clear();
this.leasingRequests.clear();
this.ioreactor.shutdown(waitMs);
} finally {
this.lock.unlock();
} 
} 
}

private RouteSpecificPool<T, C, E> getPool(T route) {
RouteSpecificPool<T, C, E> pool = this.routeToPool.get(route);
if (pool == null) {
pool = new RouteSpecificPool<T, C, E>(route)
{
protected E createEntry(T route, C conn)
{
return AbstractNIOConnPool.this.createEntry(route, conn);
}
};

this.routeToPool.put(route, pool);
} 
return pool;
}

public Future<E> lease(T route, Object state, long connectTimeout, TimeUnit tunit, FutureCallback<E> callback) {
return lease(route, state, connectTimeout, connectTimeout, tunit, callback);
}

public Future<E> lease(T route, Object state, long connectTimeout, long leaseTimeout, TimeUnit tunit, FutureCallback<E> callback) {
Args.notNull(route, "Route");
Args.notNull(tunit, "Time unit");
Asserts.check(!this.isShutDown.get(), "Connection pool shut down");
BasicFuture<E> future = new BasicFuture(callback);
this.lock.lock();
try {
long timeout = (connectTimeout > 0L) ? tunit.toMillis(connectTimeout) : 0L;
LeaseRequest<T, C, E> request = new LeaseRequest<T, C, E>(route, state, timeout, leaseTimeout, future);
boolean completed = processPendingRequest(request);
if (!request.isDone() && !completed) {
this.leasingRequests.add(request);
}
if (request.isDone()) {
this.completedRequests.add(request);
}
} finally {
this.lock.unlock();
} 
fireCallbacks();
return (Future<E>)future;
}

public Future<E> lease(T route, Object state, FutureCallback<E> callback) {
return lease(route, state, -1L, TimeUnit.MICROSECONDS, callback);
}

public Future<E> lease(T route, Object state) {
return lease(route, state, -1L, TimeUnit.MICROSECONDS, null);
}

public void release(E entry, boolean reusable) {
if (entry == null) {
return;
}
if (this.isShutDown.get()) {
return;
}
this.lock.lock();
try {
if (this.leased.remove(entry)) {
RouteSpecificPool<T, C, E> pool = getPool((T)entry.getRoute());
pool.free(entry, reusable);
if (reusable) {
this.available.addFirst(entry);
onRelease(entry);
} else {
entry.close();
} 
processNextPendingRequest();
} 
} finally {
this.lock.unlock();
} 
fireCallbacks();
}

private void processPendingRequests() {
ListIterator<LeaseRequest<T, C, E>> it = this.leasingRequests.listIterator();
while (it.hasNext()) {
LeaseRequest<T, C, E> request = it.next();
boolean completed = processPendingRequest(request);
if (request.isDone() || completed) {
it.remove();
}
if (request.isDone()) {
this.completedRequests.add(request);
}
} 
}

private void processNextPendingRequest() {
ListIterator<LeaseRequest<T, C, E>> it = this.leasingRequests.listIterator();
while (it.hasNext()) {
LeaseRequest<T, C, E> request = it.next();
boolean completed = processPendingRequest(request);
if (request.isDone() || completed) {
it.remove();
}
if (request.isDone()) {
this.completedRequests.add(request);
}
if (completed)
return; 
} 
}

private boolean processPendingRequest(LeaseRequest<T, C, E> request) {
E entry;
T route = request.getRoute();
Object state = request.getState();
long deadline = request.getDeadline();

long now = System.currentTimeMillis();
if (now > deadline) {
request.failed(new TimeoutException());
return false;
} 

RouteSpecificPool<T, C, E> pool = getPool(route);

while (true) {
entry = pool.getFree(state);
if (entry == null) {
break;
}
if (entry.isClosed() || entry.isExpired(System.currentTimeMillis())) {
entry.close();
this.available.remove(entry);
pool.free(entry, false);
continue;
} 
break;
} 
if (entry != null) {
this.available.remove(entry);
this.leased.add(entry);
request.completed(entry);
onReuse(entry);
onLease(entry);
return true;
} 

int maxPerRoute = getMax(route);

int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
if (excess > 0) {
for (int i = 0; i < excess; i++) {
E lastUsed = pool.getLastUsed();
if (lastUsed == null) {
break;
}
lastUsed.close();
this.available.remove(lastUsed);
pool.remove(lastUsed);
} 
}

if (pool.getAllocatedCount() < maxPerRoute) {
SocketAddress localAddress, remoteAddress; int totalUsed = this.pending.size() + this.leased.size();
int freeCapacity = Math.max(this.maxTotal - totalUsed, 0);
if (freeCapacity == 0) {
return false;
}
int totalAvailable = this.available.size();
if (totalAvailable > freeCapacity - 1 && 
!this.available.isEmpty()) {
PoolEntry poolEntry = (PoolEntry)this.available.removeLast();
poolEntry.close();
RouteSpecificPool<T, C, E> otherpool = getPool((T)poolEntry.getRoute());
otherpool.remove((E)poolEntry);
} 

try {
remoteAddress = this.addressResolver.resolveRemoteAddress(route);
localAddress = this.addressResolver.resolveLocalAddress(route);
} catch (IOException ex) {
request.failed(ex);
return false;
} 

SessionRequest sessionRequest = this.ioreactor.connect(remoteAddress, localAddress, route, this.sessionRequestCallback);

int timout = (request.getConnectTimeout() < 2147483647L) ? (int)request.getConnectTimeout() : Integer.MAX_VALUE;

sessionRequest.setConnectTimeout(timout);
this.pending.add(sessionRequest);
pool.addPending(sessionRequest, request.getFuture());
return true;
} 
return false;
}

private void fireCallbacks() {
LeaseRequest<T, C, E> request;
while ((request = (LeaseRequest<T, C, E>)this.completedRequests.poll()) != null) {
BasicFuture<E> future = request.getFuture();
Exception ex = request.getException();
E result = request.getResult();
if (ex != null) {
future.failed(ex); continue;
}  if (result != null) {
future.completed(result); continue;
} 
future.cancel();
} 
}

public void validatePendingRequests() {
this.lock.lock();
try {
long now = System.currentTimeMillis();
ListIterator<LeaseRequest<T, C, E>> it = this.leasingRequests.listIterator();
while (it.hasNext()) {
LeaseRequest<T, C, E> request = it.next();
long deadline = request.getDeadline();
if (now > deadline) {
it.remove();
request.failed(new TimeoutException());
this.completedRequests.add(request);
} 
} 
} finally {
this.lock.unlock();
} 
fireCallbacks();
}

protected void requestCompleted(SessionRequest request) {
if (this.isShutDown.get()) {
return;
}

T route = (T)request.getAttachment();
this.lock.lock();
try {
this.pending.remove(request);
RouteSpecificPool<T, C, E> pool = getPool(route);
IOSession session = request.getSession();
try {
C conn = this.connFactory.create(route, session);
E entry = pool.createEntry(request, conn);
this.leased.add(entry);
pool.completed(request, entry);
onLease(entry);
} catch (IOException ex) {
pool.failed(request, ex);
} 
} finally {
this.lock.unlock();
} 
fireCallbacks();
}

protected void requestCancelled(SessionRequest request) {
if (this.isShutDown.get()) {
return;
}

T route = (T)request.getAttachment();
this.lock.lock();
try {
this.pending.remove(request);
RouteSpecificPool<T, C, E> pool = getPool(route);
pool.cancelled(request);
if (this.ioreactor.getStatus().compareTo((Enum)IOReactorStatus.ACTIVE) <= 0) {
processNextPendingRequest();
}
} finally {
this.lock.unlock();
} 
fireCallbacks();
}

protected void requestFailed(SessionRequest request) {
if (this.isShutDown.get()) {
return;
}

T route = (T)request.getAttachment();
this.lock.lock();
try {
this.pending.remove(request);
RouteSpecificPool<T, C, E> pool = getPool(route);
pool.failed(request, request.getException());
processNextPendingRequest();
} finally {
this.lock.unlock();
} 
fireCallbacks();
}

protected void requestTimeout(SessionRequest request) {
if (this.isShutDown.get()) {
return;
}

T route = (T)request.getAttachment();
this.lock.lock();
try {
this.pending.remove(request);
RouteSpecificPool<T, C, E> pool = getPool(route);
pool.timeout(request);
processNextPendingRequest();
} finally {
this.lock.unlock();
} 
fireCallbacks();
}

private int getMax(T route) {
Integer v = this.maxPerRoute.get(route);
if (v != null) {
return v.intValue();
}
return this.defaultMaxPerRoute;
}

public void setMaxTotal(int max) {
Args.positive(max, "Max value");
this.lock.lock();
try {
this.maxTotal = max;
} finally {
this.lock.unlock();
} 
}

public int getMaxTotal() {
this.lock.lock();
try {
return this.maxTotal;
} finally {
this.lock.unlock();
} 
}

public void setDefaultMaxPerRoute(int max) {
Args.positive(max, "Max value");
this.lock.lock();
try {
this.defaultMaxPerRoute = max;
} finally {
this.lock.unlock();
} 
}

public int getDefaultMaxPerRoute() {
this.lock.lock();
try {
return this.defaultMaxPerRoute;
} finally {
this.lock.unlock();
} 
}

public void setMaxPerRoute(T route, int max) {
Args.notNull(route, "Route");
Args.positive(max, "Max value");
this.lock.lock();
try {
this.maxPerRoute.put(route, Integer.valueOf(max));
} finally {
this.lock.unlock();
} 
}

public int getMaxPerRoute(T route) {
Args.notNull(route, "Route");
this.lock.lock();
try {
return getMax(route);
} finally {
this.lock.unlock();
} 
}

public PoolStats getTotalStats() {
this.lock.lock();
try {
return new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);

}
finally {

this.lock.unlock();
} 
}

public PoolStats getStats(T route) {
Args.notNull(route, "Route");
this.lock.lock();
try {
RouteSpecificPool<T, C, E> pool = getPool(route);
return new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), getMax(route));

}
finally {

this.lock.unlock();
} 
}

public Set<T> getRoutes() {
this.lock.lock();
try {
return new HashSet(this.routeToPool.keySet());
} finally {
this.lock.unlock();
} 
}

protected void enumAvailable(PoolEntryCallback<T, C> callback) {
this.lock.lock();
try {
Iterator<E> it = this.available.iterator();
while (it.hasNext()) {
PoolEntry poolEntry = (PoolEntry)it.next();
callback.process(poolEntry);
if (poolEntry.isClosed()) {
RouteSpecificPool<T, C, E> pool = getPool((T)poolEntry.getRoute());
pool.remove((E)poolEntry);
it.remove();
} 
} 
processPendingRequests();
purgePoolMap();
} finally {
this.lock.unlock();
} 
}

protected void enumLeased(PoolEntryCallback<T, C> callback) {
this.lock.lock();
try {
Iterator<E> it = this.leased.iterator();
while (it.hasNext()) {
PoolEntry poolEntry = (PoolEntry)it.next();
callback.process(poolEntry);
} 
processPendingRequests();
} finally {
this.lock.unlock();
} 
}

@Deprecated
protected void enumEntries(Iterator<E> it, PoolEntryCallback<T, C> callback) {
while (it.hasNext()) {
PoolEntry poolEntry = (PoolEntry)it.next();
callback.process(poolEntry);
} 
processPendingRequests();
}

private void purgePoolMap() {
Iterator<Map.Entry<T, RouteSpecificPool<T, C, E>>> it = this.routeToPool.entrySet().iterator();
while (it.hasNext()) {
Map.Entry<T, RouteSpecificPool<T, C, E>> entry = it.next();
RouteSpecificPool<T, C, E> pool = entry.getValue();
if (pool.getAllocatedCount() == 0) {
it.remove();
}
} 
}

public void closeIdle(long idletime, TimeUnit tunit) {
Args.notNull(tunit, "Time unit");
long time = tunit.toMillis(idletime);
if (time < 0L) {
time = 0L;
}
final long deadline = System.currentTimeMillis() - time;
enumAvailable(new PoolEntryCallback<T, C>()
{
public void process(PoolEntry<T, C> entry)
{
if (entry.getUpdated() <= deadline) {
entry.close();
}
}
});
}

public void closeExpired() {
final long now = System.currentTimeMillis();
enumAvailable(new PoolEntryCallback<T, C>()
{
public void process(PoolEntry<T, C> entry)
{
if (entry.isExpired(now)) {
entry.close();
}
}
});
}

public String toString() {
StringBuilder buffer = new StringBuilder();
buffer.append("[leased: ");
buffer.append(this.leased);
buffer.append("][available: ");
buffer.append(this.available);
buffer.append("][pending: ");
buffer.append(this.pending);
buffer.append("]");
return buffer.toString();
}

protected abstract E createEntry(T paramT, C paramC);

class InternalSessionRequestCallback implements SessionRequestCallback {
public void completed(SessionRequest request) {
AbstractNIOConnPool.this.requestCompleted(request);
}

public void cancelled(SessionRequest request) {
AbstractNIOConnPool.this.requestCancelled(request);
}

public void failed(SessionRequest request) {
AbstractNIOConnPool.this.requestFailed(request);
}

public void timeout(SessionRequest request) {
AbstractNIOConnPool.this.requestTimeout(request);
}
}
}

