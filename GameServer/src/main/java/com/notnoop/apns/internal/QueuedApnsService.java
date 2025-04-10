package com.notnoop.apns.internal;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.EnhancedApnsNotification;
import com.notnoop.exceptions.NetworkIOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueuedApnsService
extends AbstractApnsService
{
private static final Logger logger = LoggerFactory.getLogger(QueuedApnsService.class);

private ApnsService service;
private BlockingQueue<ApnsNotification> queue;
private AtomicBoolean started = new AtomicBoolean(false); private Thread thread;

public QueuedApnsService(ApnsService service) {
super(null);
this.service = service;
this.queue = new LinkedBlockingQueue<ApnsNotification>();
this.thread = null;
}
private volatile boolean shouldContinue;

public void push(ApnsNotification msg) {
if (!this.started.get()) {
throw new IllegalStateException("service hasn't be started or was closed");
}
this.queue.add(msg);
}

public void start() {
if (this.started.getAndSet(true)) {
return;
}

this.service.start();
this.shouldContinue = true;
this.thread = new Thread() {
public void run() {
while (QueuedApnsService.this.shouldContinue) {
try {
ApnsNotification msg = QueuedApnsService.this.queue.take();
QueuedApnsService.this.service.push(msg);
} catch (InterruptedException e) {

} catch (NetworkIOException e) {

} catch (Exception e) {

QueuedApnsService.logger.warn("Unexpected message caught... Shouldn't be here", e);
} 
} 
}
};
this.thread.start();
}

public void stop() {
this.started.set(false);
this.shouldContinue = false;
this.thread.interrupt();
this.service.stop();
}

public Map<String, Date> getInactiveDevices() throws NetworkIOException {
return this.service.getInactiveDevices();
}

public void testConnection() throws NetworkIOException {
this.service.testConnection();
}
}

