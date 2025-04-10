package org.apache.mina.core.future;

import java.util.concurrent.atomic.AtomicInteger;

public class CompositeIoFuture<E extends IoFuture>
extends DefaultIoFuture
{
private final NotifyingListener listener = new NotifyingListener();

private final AtomicInteger unnotified = new AtomicInteger();

private volatile boolean constructionFinished;

public CompositeIoFuture(Iterable<E> children) {
super(null);

for (IoFuture ioFuture : children) {
ioFuture.addListener(this.listener);
this.unnotified.incrementAndGet();
} 

this.constructionFinished = true;
if (this.unnotified.get() == 0)
setValue(Boolean.valueOf(true)); 
}

private class NotifyingListener
implements IoFutureListener<IoFuture> {
public void operationComplete(IoFuture future) {
if (CompositeIoFuture.this.unnotified.decrementAndGet() == 0 && CompositeIoFuture.this.constructionFinished)
CompositeIoFuture.this.setValue(Boolean.valueOf(true)); 
}

private NotifyingListener() {}
}
}

