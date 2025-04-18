package com.mchange.v2.async;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.util.ResourceClosedException;

public class RoundRobinAsynchronousRunner
implements AsynchronousRunner, Queuable
{
private static final MLogger logger = MLog.getLogger(RoundRobinAsynchronousRunner.class);

final RunnableQueue[] rqs;

int task_turn = 0;

int view_turn = 0;

public RoundRobinAsynchronousRunner(int paramInt, boolean paramBoolean) {
this.rqs = new RunnableQueue[paramInt];
for (byte b = 0; b < paramInt; b++) {
this.rqs[b] = new CarefulRunnableQueue(paramBoolean, false);
}
}

public synchronized void postRunnable(Runnable paramRunnable) {
try {
int i = this.task_turn;
this.task_turn = (this.task_turn + 1) % this.rqs.length;
this.rqs[i].postRunnable(paramRunnable);

}
catch (NullPointerException nullPointerException) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "NullPointerException while posting Runnable -- Probably we're closed.", nullPointerException);
}
close(true);
throw new ResourceClosedException("Attempted to use a RoundRobinAsynchronousRunner in a closed or broken state.");
} 
}

public synchronized RunnableQueue asRunnableQueue() {
try {
int i = this.view_turn;
this.view_turn = (this.view_turn + 1) % this.rqs.length;
return new RunnableQueueView(i);

}
catch (NullPointerException nullPointerException) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "NullPointerException in asRunnableQueue() -- Probably we're closed.", nullPointerException);
}
close(true);
throw new ResourceClosedException("Attempted to use a RoundRobinAsynchronousRunner in a closed or broken state.");
} 
}
public synchronized void close(boolean paramBoolean) {
byte b;
int i;
for (b = 0, i = this.rqs.length; b < i; b++) {

attemptClose(this.rqs[b], paramBoolean);
this.rqs[b] = null;
} 
}

public void close() {
close(true);
}
static void attemptClose(RunnableQueue paramRunnableQueue, boolean paramBoolean) {
try {
paramRunnableQueue.close(paramBoolean);
} catch (Exception exception) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "RunnableQueue close FAILED.", exception); 
} 
}

class RunnableQueueView
implements RunnableQueue {
final int rq_num;

RunnableQueueView(int param1Int) {
this.rq_num = param1Int;
}
public void postRunnable(Runnable param1Runnable) {
RoundRobinAsynchronousRunner.this.rqs[this.rq_num].postRunnable(param1Runnable);
}

public void close(boolean param1Boolean) {}

public void close() {}
}
}

