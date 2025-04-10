package ch.qos.logback.core;

import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AsyncAppenderBase<E>
extends UnsynchronizedAppenderBase<E>
implements AppenderAttachable<E>
{
AppenderAttachableImpl<E> aai = new AppenderAttachableImpl();

BlockingQueue<E> blockingQueue;

public static final int DEFAULT_QUEUE_SIZE = 256;

int queueSize = 256;

int appenderCount = 0;

static final int UNDEFINED = -1;
int discardingThreshold = -1;

Worker worker = new Worker();

public static final int DEFAULT_MAX_FLUSH_TIME = 1000;

int maxFlushTime = 1000;

protected boolean isDiscardable(E eventObject) {
return false;
}

protected void preprocess(E eventObject) {}

public void start() {
if (this.appenderCount == 0) {
addError("No attached appenders found.");
return;
} 
if (this.queueSize < 1) {
addError("Invalid queue size [" + this.queueSize + "]");
return;
} 
this.blockingQueue = new ArrayBlockingQueue<E>(this.queueSize);

if (this.discardingThreshold == -1)
this.discardingThreshold = this.queueSize / 5; 
addInfo("Setting discardingThreshold to " + this.discardingThreshold);
this.worker.setDaemon(true);
this.worker.setName("AsyncAppender-Worker-" + getName());

super.start();
this.worker.start();
}

public void stop() {
if (!isStarted()) {
return;
}

super.stop();

this.worker.interrupt();
try {
this.worker.join(this.maxFlushTime);

if (this.worker.isAlive()) {
addWarn("Max queue flush timeout (" + this.maxFlushTime + " ms) exceeded. Approximately " + this.blockingQueue.size() + " queued events were possibly discarded.");
} else {

addInfo("Queue flush finished successfully within timeout.");
}

} catch (InterruptedException e) {
addError("Failed to join worker thread. " + this.blockingQueue.size() + " queued events may be discarded.", e);
} 
}

protected void append(E eventObject) {
if (isQueueBelowDiscardingThreshold() && isDiscardable(eventObject)) {
return;
}
preprocess(eventObject);
put(eventObject);
}

private boolean isQueueBelowDiscardingThreshold() {
return (this.blockingQueue.remainingCapacity() < this.discardingThreshold);
}

private void put(E eventObject) {
try {
this.blockingQueue.put(eventObject);
} catch (InterruptedException e) {}
}

public int getQueueSize() {
return this.queueSize;
}

public void setQueueSize(int queueSize) {
this.queueSize = queueSize;
}

public int getDiscardingThreshold() {
return this.discardingThreshold;
}

public void setDiscardingThreshold(int discardingThreshold) {
this.discardingThreshold = discardingThreshold;
}

public int getMaxFlushTime() {
return this.maxFlushTime;
}

public void setMaxFlushTime(int maxFlushTime) {
this.maxFlushTime = maxFlushTime;
}

public int getNumberOfElementsInQueue() {
return this.blockingQueue.size();
}

public int getRemainingCapacity() {
return this.blockingQueue.remainingCapacity();
}

public void addAppender(Appender<E> newAppender) {
if (this.appenderCount == 0) {
this.appenderCount++;
addInfo("Attaching appender named [" + newAppender.getName() + "] to AsyncAppender.");
this.aai.addAppender(newAppender);
} else {
addWarn("One and only one appender may be attached to AsyncAppender.");
addWarn("Ignoring additional appender named [" + newAppender.getName() + "]");
} 
}

public Iterator<Appender<E>> iteratorForAppenders() {
return this.aai.iteratorForAppenders();
}

public Appender<E> getAppender(String name) {
return this.aai.getAppender(name);
}

public boolean isAttached(Appender<E> eAppender) {
return this.aai.isAttached(eAppender);
}

public void detachAndStopAllAppenders() {
this.aai.detachAndStopAllAppenders();
}

public boolean detachAppender(Appender<E> eAppender) {
return this.aai.detachAppender(eAppender);
}

public boolean detachAppender(String name) {
return this.aai.detachAppender(name);
}

class Worker
extends Thread {
public void run() {
AsyncAppenderBase<E> parent = AsyncAppenderBase.this;
AppenderAttachableImpl<E> aai = parent.aai;

while (parent.isStarted()) {
try {
E e = parent.blockingQueue.take();
aai.appendLoopOnAppenders(e);
} catch (InterruptedException ie) {
break;
} 
} 

AsyncAppenderBase.this.addInfo("Worker thread will flush remaining events before exiting. ");

for (E e : parent.blockingQueue) {
aai.appendLoopOnAppenders(e);
parent.blockingQueue.remove(e);
} 

aai.detachAndStopAllAppenders();
}
}
}

