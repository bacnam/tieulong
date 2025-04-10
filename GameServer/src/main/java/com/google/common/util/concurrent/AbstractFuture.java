package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import javax.annotation.Nullable;

public abstract class AbstractFuture<V>
implements ListenableFuture<V>
{
private final Sync<V> sync = new Sync<V>();

private final ExecutionList executionList = new ExecutionList();

public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
return this.sync.get(unit.toNanos(timeout));
}

public V get() throws InterruptedException, ExecutionException {
return this.sync.get();
}

public boolean isDone() {
return this.sync.isDone();
}

public boolean isCancelled() {
return this.sync.isCancelled();
}

public boolean cancel(boolean mayInterruptIfRunning) {
if (!this.sync.cancel()) {
return false;
}
done();
if (mayInterruptIfRunning) {
interruptTask();
}
return true;
}

protected void interruptTask() {}

public void addListener(Runnable listener, Executor exec) {
this.executionList.add(listener, exec);
}

protected boolean set(@Nullable V value) {
boolean result = this.sync.set(value);
if (result) {
done();
}
return result;
}

protected boolean setException(Throwable throwable) {
boolean result = this.sync.setException((Throwable)Preconditions.checkNotNull(throwable));
if (result) {
done();
}

if (throwable instanceof Error) {
throw (Error)throwable;
}
return result;
}

@Deprecated
@Beta
protected final boolean cancel() {
boolean result = this.sync.cancel();
if (result) {
done();
}
return result;
}

@Deprecated
@Beta
protected void done() {
this.executionList.execute();
}

static final class Sync<V>
extends AbstractQueuedSynchronizer
{
private static final long serialVersionUID = 0L;

static final int RUNNING = 0;

static final int COMPLETING = 1;

static final int COMPLETED = 2;

static final int CANCELLED = 4;

private V value;

private Throwable exception;

protected int tryAcquireShared(int ignored) {
if (isDone()) {
return 1;
}
return -1;
}

protected boolean tryReleaseShared(int finalState) {
setState(finalState);
return true;
}

V get(long nanos) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
if (!tryAcquireSharedNanos(-1, nanos)) {
throw new TimeoutException("Timeout waiting for task.");
}

return getValue();
}

V get() throws CancellationException, ExecutionException, InterruptedException {
acquireSharedInterruptibly(-1);
return getValue();
}

private V getValue() throws CancellationException, ExecutionException {
int state = getState();
switch (state) {
case 2:
if (this.exception != null) {
throw new ExecutionException(this.exception);
}
return this.value;

case 4:
throw new CancellationException("Task was cancelled.");
} 

throw new IllegalStateException("Error, synchronizer in invalid state: " + state);
}

boolean isDone() {
return ((getState() & 0x6) != 0);
}

boolean isCancelled() {
return (getState() == 4);
}

boolean set(@Nullable V v) {
return complete(v, (Throwable)null, 2);
}

boolean setException(Throwable t) {
return complete((V)null, t, 2);
}

boolean cancel() {
return complete((V)null, (Throwable)null, 4);
}

private boolean complete(@Nullable V v, Throwable t, int finalState) {
if (compareAndSetState(0, 1)) {
this.value = v;
this.exception = t;
releaseShared(finalState);
return true;
} 

return false;
}
}
}

