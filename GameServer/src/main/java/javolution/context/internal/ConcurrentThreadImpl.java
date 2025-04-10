package javolution.context.internal;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.realtime.RealtimeThread;
import javolution.context.AbstractContext;

public class ConcurrentThreadImpl
extends RealtimeThread
{
private static int count;
private ConcurrentContextImpl context;
private AtomicBoolean isBusy = new AtomicBoolean();

private Runnable logic;

private int priority;

public ConcurrentThreadImpl() {
setName("ConcurrentThread-" + ++count);
setDaemon(true);
}

public boolean execute(Runnable logic, ConcurrentContextImpl inContext) {
if (!this.isBusy.compareAndSet(false, true))
return false; 
synchronized (this) {
this.priority = Thread.currentThread().getPriority();
this.context = inContext;
this.logic = logic;
notify();
} 
return true;
}

public void run() {
while (true) {
try {
synchronized (this) {
for (; this.logic == null; wait());
} 
setPriority(this.priority);
AbstractContext.inherit((AbstractContext)this.context);
this.logic.run();
this.context.completed(null);
} catch (Throwable error) {
this.context.completed(error);
} 

this.logic = null;
this.context = null;
AbstractContext.inherit(null);
this.isBusy.set(false);
} 
}
}

