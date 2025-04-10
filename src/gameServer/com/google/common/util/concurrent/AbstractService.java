package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

@Beta
public abstract class AbstractService
implements Service
{
private final ReentrantLock lock = new ReentrantLock();

private final Transition startup = new Transition();
private final Transition shutdown = new Transition();

private Service.State state = Service.State.NEW;

private boolean shutdownWhenStartupFinishes = false;

protected abstract void doStart();

protected abstract void doStop();

public final ListenableFuture<Service.State> start() {
this.lock.lock();
try {
if (this.state == Service.State.NEW) {
this.state = Service.State.STARTING;
doStart();
} 
} catch (Throwable startupFailure) {

notifyFailed(startupFailure);
} finally {
this.lock.unlock();
} 

return this.startup;
}

public final ListenableFuture<Service.State> stop() {
this.lock.lock();
try {
if (this.state == Service.State.NEW) {
this.state = Service.State.TERMINATED;
this.startup.set(Service.State.TERMINATED);
this.shutdown.set(Service.State.TERMINATED);
} else if (this.state == Service.State.STARTING) {
this.shutdownWhenStartupFinishes = true;
this.startup.set(Service.State.STOPPING);
} else if (this.state == Service.State.RUNNING) {
this.state = Service.State.STOPPING;
doStop();
} 
} catch (Throwable shutdownFailure) {

notifyFailed(shutdownFailure);
} finally {
this.lock.unlock();
} 

return this.shutdown;
}

public Service.State startAndWait() {
return Futures.<Service.State>getUnchecked(start());
}

public Service.State stopAndWait() {
return Futures.<Service.State>getUnchecked(stop());
}

protected final void notifyStarted() {
this.lock.lock();
try {
if (this.state != Service.State.STARTING) {
IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + this.state);

notifyFailed(failure);
throw failure;
} 

this.state = Service.State.RUNNING;
if (this.shutdownWhenStartupFinishes) {
stop();
} else {
this.startup.set(Service.State.RUNNING);
} 
} finally {
this.lock.unlock();
} 
}

protected final void notifyStopped() {
this.lock.lock();
try {
if (this.state != Service.State.STOPPING && this.state != Service.State.RUNNING) {
IllegalStateException failure = new IllegalStateException("Cannot notifyStopped() when the service is " + this.state);

notifyFailed(failure);
throw failure;
} 

this.state = Service.State.TERMINATED;
this.shutdown.set(Service.State.TERMINATED);
} finally {
this.lock.unlock();
} 
}

protected final void notifyFailed(Throwable cause) {
Preconditions.checkNotNull(cause);

this.lock.lock();
try {
if (this.state == Service.State.STARTING) {
this.startup.setException(cause);
this.shutdown.setException(new Exception("Service failed to start.", cause));
}
else if (this.state == Service.State.STOPPING) {
this.shutdown.setException(cause);
} 

this.state = Service.State.FAILED;
} finally {
this.lock.unlock();
} 
}

public final boolean isRunning() {
return (state() == Service.State.RUNNING);
}

public final Service.State state() {
this.lock.lock();
try {
if (this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING) {
return Service.State.STOPPING;
}
return this.state;
} finally {

this.lock.unlock();
} 
}

public String toString() {
return getClass().getSimpleName() + " [" + state() + "]";
}

private class Transition
extends AbstractFuture<Service.State>
{
private Transition() {}

public Service.State get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
try {
return super.get(timeout, unit);
} catch (TimeoutException e) {
throw new TimeoutException(AbstractService.this.toString());
} 
}
}
}

