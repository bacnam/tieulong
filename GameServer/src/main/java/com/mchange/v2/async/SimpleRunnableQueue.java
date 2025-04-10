package com.mchange.v2.async;

import java.util.LinkedList;
import java.util.List;

public class SimpleRunnableQueue
implements RunnableQueue, Queuable
{
private List taskList = new LinkedList();
private Thread t = new TaskThread();

boolean gentle_close_requested = false;

public SimpleRunnableQueue(boolean paramBoolean) {
this.t.setDaemon(paramBoolean);
this.t.start();
}

public SimpleRunnableQueue() {
this(true);
}
public RunnableQueue asRunnableQueue() {
return this;
}

public synchronized void postRunnable(Runnable paramRunnable) {
if (this.gentle_close_requested) {
throw new IllegalStateException("Attempted to post a task to a closed AsynchronousRunner.");
}

this.taskList.add(paramRunnable);
notifyAll();
}

public synchronized void close(boolean paramBoolean) {
if (paramBoolean) {
this.t.interrupt();
} else {
this.gentle_close_requested = true;
} 
}
public synchronized void close() {
close(true);
}

private synchronized Runnable dequeueRunnable() {
Runnable runnable = this.taskList.get(0);
this.taskList.remove(0);
return runnable;
}

private synchronized void awaitTask() throws InterruptedException {
while (this.taskList.size() == 0) {

if (this.gentle_close_requested)
this.t.interrupt(); 
wait();
} 
}

class TaskThread
extends Thread {
TaskThread() {
super("SimpleRunnableQueue.TaskThread");
}

public void run() {
try {
while (!isInterrupted()) {

SimpleRunnableQueue.this.awaitTask();
Runnable runnable = SimpleRunnableQueue.this.dequeueRunnable();
try {
runnable.run();
} catch (Exception exception) {

System.err.println(getClass().getName() + " -- Unexpected exception in task!");

exception.printStackTrace();
}

} 
} catch (InterruptedException interruptedException) {

}
finally {

SimpleRunnableQueue.this.taskList = null;
SimpleRunnableQueue.this.t = null;
} 
}
}
}

