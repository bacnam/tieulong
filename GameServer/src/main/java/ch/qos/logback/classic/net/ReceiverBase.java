package ch.qos.logback.classic.net;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

public abstract class ReceiverBase
extends ContextAwareBase
implements LifeCycle
{
private boolean started;

public final void start() {
if (isStarted())
return;  if (getContext() == null) {
throw new IllegalStateException("context not set");
}
if (shouldStart()) {
getContext().getExecutorService().execute(getRunnableTask());
this.started = true;
} 
}

public final void stop() {
if (!isStarted())
return;  try {
onStop();
}
catch (RuntimeException ex) {
addError("on stop: " + ex, ex);
} 
this.started = false;
}

public final boolean isStarted() {
return this.started;
}

protected abstract boolean shouldStart();

protected abstract void onStop();

protected abstract Runnable getRunnableTask();
}

