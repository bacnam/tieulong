package com.notnoop.apns.internal;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.exceptions.NetworkIOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApnsPooledConnection
implements ApnsConnection
{
private static final Logger logger = LoggerFactory.getLogger(ApnsPooledConnection.class);

private final ApnsConnection prototype;
private final int max;
private final ExecutorService executors;
private final ConcurrentLinkedQueue<ApnsConnection> prototypes;
private final ThreadLocal<ApnsConnection> uniquePrototype;

public ApnsPooledConnection(ApnsConnection prototype, int max) {
this(prototype, max, Executors.newFixedThreadPool(max));
}

public ApnsPooledConnection(ApnsConnection prototype, int max, ExecutorService executors) {
this.uniquePrototype = new ThreadLocal<ApnsConnection>()
{
protected ApnsConnection initialValue() {
ApnsConnection newCopy = ApnsPooledConnection.this.prototype.copy();
ApnsPooledConnection.this.prototypes.add(newCopy);
return newCopy; }
};
this.prototype = prototype;
this.max = max;
this.executors = executors;
this.prototypes = new ConcurrentLinkedQueue<ApnsConnection>(); } public void sendMessage(final ApnsNotification m) throws NetworkIOException { this.executors.execute(new Runnable() {
public void run() {
((ApnsConnection)ApnsPooledConnection.this.uniquePrototype.get()).sendMessage(m);
}
}); }

public ApnsConnection copy() {
return new ApnsPooledConnection(this.prototype, this.max);
}

public void close() {
this.executors.shutdown();
try {
this.executors.awaitTermination(10L, TimeUnit.SECONDS);
} catch (InterruptedException e) {
logger.warn("pool termination interrupted", e);
} 
for (ApnsConnection conn : this.prototypes) {
Utilities.close(conn);
}
Utilities.close(this.prototype);
}

public void testConnection() {
this.prototype.testConnection();
}

public synchronized void setCacheLength(int cacheLength) {
for (ApnsConnection conn : this.prototypes) {
conn.setCacheLength(cacheLength);
}
}

public int getCacheLength() {
return ((ApnsConnection)this.prototypes.peek()).getCacheLength();
}
}

