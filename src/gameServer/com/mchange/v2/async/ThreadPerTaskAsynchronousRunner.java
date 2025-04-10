package com.mchange.v2.async;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.util.ResourceClosedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadPerTaskAsynchronousRunner
implements AsynchronousRunner
{
static final int PRESUME_DEADLOCKED_MULTIPLE = 3;
static final MLogger logger = MLog.getLogger(ThreadPerTaskAsynchronousRunner.class);

final int max_task_threads;

final long interrupt_task_delay;

LinkedList queue = new LinkedList();
ArrayList running = new ArrayList();
ArrayList deadlockSnapshot = null;

boolean still_open = true;

Thread dispatchThread = new DispatchThread();
Timer interruptAndDeadlockTimer;

public ThreadPerTaskAsynchronousRunner(int paramInt) {
this(paramInt, 0L);
}

public ThreadPerTaskAsynchronousRunner(int paramInt, long paramLong) {
this.max_task_threads = paramInt;
this.interrupt_task_delay = paramLong;
if (hasIdTimer()) {

this.interruptAndDeadlockTimer = new Timer(true);
TimerTask timerTask = new TimerTask()
{
public void run() {
ThreadPerTaskAsynchronousRunner.this.checkForDeadlock(); }
};
long l = paramLong * 3L;
this.interruptAndDeadlockTimer.schedule(timerTask, l, l);
} 

this.dispatchThread.start();
}

private boolean hasIdTimer() {
return (this.interrupt_task_delay > 0L);
}

public synchronized void postRunnable(Runnable paramRunnable) {
if (this.still_open) {

this.queue.add(paramRunnable);
notifyAll();
} else {

throw new ResourceClosedException("Attempted to use a ThreadPerTaskAsynchronousRunner in a closed or broken state.");
} 
}

public void close() {
close(true);
}

public synchronized void close(boolean paramBoolean) {
if (this.still_open) {

this.still_open = false;
if (paramBoolean) {

this.queue.clear();
for (Iterator<Thread> iterator = this.running.iterator(); iterator.hasNext();)
((Thread)iterator.next()).interrupt(); 
closeThreadResources();
} 
} 
}

public synchronized int getRunningCount() {
return this.running.size();
}
public synchronized Collection getRunningTasks() {
return (Collection)this.running.clone();
}
public synchronized int getWaitingCount() {
return this.queue.size();
}
public synchronized Collection getWaitingTasks() {
return (Collection)this.queue.clone();
}
public synchronized boolean isClosed() {
return !this.still_open;
}
public synchronized boolean isDoneAndGone() {
return (!this.dispatchThread.isAlive() && this.running.isEmpty() && this.interruptAndDeadlockTimer == null);
}

private synchronized void acknowledgeComplete(TaskThread paramTaskThread) {
if (!paramTaskThread.isCompleted()) {

this.running.remove(paramTaskThread);
paramTaskThread.markCompleted();
notifyAll();

if (!this.still_open && this.queue.isEmpty() && this.running.isEmpty()) {
closeThreadResources();
}
} 
}

private synchronized void checkForDeadlock() {
if (this.deadlockSnapshot == null) {

if (this.running.size() == this.max_task_threads) {
this.deadlockSnapshot = (ArrayList)this.running.clone();
}
} else if (this.running.size() < this.max_task_threads) {
this.deadlockSnapshot = null;
} else if (this.deadlockSnapshot.equals(this.running)) {

if (logger.isLoggable(MLevel.WARNING)) {

StringBuffer stringBuffer = new StringBuffer(1024);
stringBuffer.append("APPARENT DEADLOCK! (");
stringBuffer.append(this);
stringBuffer.append(") Deadlocked threads (unresponsive to interrupt()) are being set aside as hopeless and up to ");
stringBuffer.append(this.max_task_threads);
stringBuffer.append(" may now be spawned for new tasks. If tasks continue to deadlock, you may run out of memory. Deadlocked task list: "); byte b1; int j;
for (b1 = 0, j = this.deadlockSnapshot.size(); b1 < j; b1++) {

if (b1 != 0) stringBuffer.append(", "); 
stringBuffer.append(((TaskThread)this.deadlockSnapshot.get(b1)).getTask());
} 

logger.log(MLevel.WARNING, stringBuffer.toString());
} 

byte b;
int i;
for (b = 0, i = this.deadlockSnapshot.size(); b < i; b++)
acknowledgeComplete(this.deadlockSnapshot.get(b)); 
this.deadlockSnapshot = null;
} else {

this.deadlockSnapshot = (ArrayList)this.running.clone();
} 
}

private void closeThreadResources() {
if (this.interruptAndDeadlockTimer != null) {

this.interruptAndDeadlockTimer.cancel();
this.interruptAndDeadlockTimer = null;
} 
this.dispatchThread.interrupt();
}

class DispatchThread
extends Thread {
DispatchThread() {
super("Dispatch-Thread-for-" + ThreadPerTaskAsynchronousRunner.this);
}

public void run() {
synchronized (ThreadPerTaskAsynchronousRunner.this) {

while (true) {

try { if (ThreadPerTaskAsynchronousRunner.this.queue.isEmpty() || ThreadPerTaskAsynchronousRunner.this.running.size() == ThreadPerTaskAsynchronousRunner.this.max_task_threads) {
ThreadPerTaskAsynchronousRunner.this.wait(); continue;
} 
Runnable runnable = ThreadPerTaskAsynchronousRunner.this.queue.remove(0);
ThreadPerTaskAsynchronousRunner.TaskThread taskThread = new ThreadPerTaskAsynchronousRunner.TaskThread(runnable);
taskThread.start();
ThreadPerTaskAsynchronousRunner.this.running.add(taskThread);
}

catch (InterruptedException interruptedException) { break; }

}  if (ThreadPerTaskAsynchronousRunner.this.still_open) {

if (ThreadPerTaskAsynchronousRunner.logger.isLoggable(MLevel.WARNING))
ThreadPerTaskAsynchronousRunner.logger.log(MLevel.WARNING, getName() + " unexpectedly interrupted! Shutting down!"); 
ThreadPerTaskAsynchronousRunner.this.close(false);
} 
} 
}
}

class TaskThread
extends Thread
{
Runnable r;

boolean completed = false;

TaskThread(Runnable param1Runnable) {
super("Task-Thread-for-" + ThreadPerTaskAsynchronousRunner.this);
this.r = param1Runnable;
}

Runnable getTask() {
return this.r;
}
synchronized void markCompleted() {
this.completed = true;
}
synchronized boolean isCompleted() {
return this.completed;
}

public void run() {
try {
if (ThreadPerTaskAsynchronousRunner.this.hasIdTimer()) {

TimerTask timerTask = new TimerTask()
{
public void run() {
ThreadPerTaskAsynchronousRunner.TaskThread.this.interrupt(); }
};
ThreadPerTaskAsynchronousRunner.this.interruptAndDeadlockTimer.schedule(timerTask, ThreadPerTaskAsynchronousRunner.this.interrupt_task_delay);
} 
this.r.run();
} finally {

ThreadPerTaskAsynchronousRunner.this.acknowledgeComplete(this);
} 
}
}
}

