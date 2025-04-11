package org.apache.http.impl.nio.client;

import org.apache.http.concurrent.BasicFuture;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutionHandler;

import java.util.Queue;

@Deprecated
class DefaultResultCallback<T>
        implements ResultCallback<T> {
    private final BasicFuture<T> future;
    private final Queue<HttpAsyncRequestExecutionHandler<?>> queue;

    DefaultResultCallback(BasicFuture<T> future, Queue<HttpAsyncRequestExecutionHandler<?>> queue) {
        this.future = future;
        this.queue = queue;
    }

    public void completed(T result, HttpAsyncRequestExecutionHandler<T> handler) {
        this.future.completed(result);
        this.queue.remove(handler);
    }

    public void failed(Exception ex, HttpAsyncRequestExecutionHandler<T> handler) {
        this.future.failed(ex);
        this.queue.remove(handler);
    }

    public void cancelled(HttpAsyncRequestExecutionHandler<T> handler) {
        this.future.cancel(true);
        this.queue.remove(handler);
    }

    public boolean isDone() {
        return this.future.isDone();
    }
}

