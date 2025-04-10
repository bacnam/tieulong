package javolution.context.internal;

import javolution.context.AbstractContext;
import javolution.context.ConcurrentContext;
import javolution.lang.MathLib;

public final class ConcurrentContextImpl
extends ConcurrentContext
{
private int completedCount;
private Throwable error;
private int initiatedCount;
private final ConcurrentContextImpl parent;
private ConcurrentThreadImpl[] threads;

public ConcurrentContextImpl() {
this.parent = null;
int nbThreads = ((Integer)ConcurrentContext.CONCURRENCY.get()).intValue();
this.threads = new ConcurrentThreadImpl[nbThreads];
for (int i = 0; i < nbThreads; i++) {
this.threads[i] = new ConcurrentThreadImpl();
this.threads[i].start();
} 
}

public ConcurrentContextImpl(ConcurrentContextImpl parent) {
this.parent = parent;
this.threads = parent.threads;
}

public synchronized void completed(Throwable error) {
if (error != null) {
this.error = error;
}
this.completedCount++;
notify();
}

public void execute(Runnable logic) {
for (ConcurrentThreadImpl thread : this.threads) {
if (thread.execute(logic, this)) {
this.initiatedCount++;

return;
} 
} 
try {
logic.run();
} catch (Throwable e) {
this.error = e;
} 
}

public synchronized void exit() {
super.exit();
try {
while (this.initiatedCount != this.completedCount) {
wait();
}
} catch (InterruptedException ex) {
this.error = ex;
} 
if (this.error == null)
return; 
if (this.error instanceof RuntimeException)
throw (RuntimeException)this.error; 
if (this.error instanceof Error)
throw (Error)this.error; 
throw new RuntimeException(this.error);
}

public int getConcurrency() {
return this.threads.length;
}

public void setConcurrency(int concurrency) {
int nbThreads = MathLib.min(this.parent.threads.length, concurrency);
this.threads = new ConcurrentThreadImpl[nbThreads];
for (int i = 0; i < nbThreads; i++) {
this.threads[i] = this.parent.threads[i];
}
}

protected ConcurrentContext inner() {
return new ConcurrentContextImpl(this);
}
}

