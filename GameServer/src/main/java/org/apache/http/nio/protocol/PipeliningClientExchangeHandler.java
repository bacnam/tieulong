package org.apache.http.nio.protocol;

import org.apache.http.*;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Pipelined
public class PipeliningClientExchangeHandler<T>
        implements HttpAsyncClientExchangeHandler {
    private final Queue<HttpAsyncRequestProducer> requestProducerQueue;
    private final Queue<HttpAsyncResponseConsumer<T>> responseConsumerQueue;
    private final Queue<HttpRequest> requestQueue;
    private final Queue<T> resultQueue;
    private final BasicFuture<List<T>> future;
    private final HttpContext localContext;
    private final NHttpClientConnection conn;
    private final HttpProcessor httppocessor;
    private final ConnectionReuseStrategy connReuseStrategy;
    private final AtomicReference<HttpAsyncRequestProducer> requestProducerRef;
    private final AtomicReference<HttpAsyncResponseConsumer<T>> responseConsumerRef;
    private final AtomicBoolean keepAlive;
    private final AtomicBoolean closed;

    public PipeliningClientExchangeHandler(List<? extends HttpAsyncRequestProducer> requestProducers, List<? extends HttpAsyncResponseConsumer<T>> responseConsumers, FutureCallback<List<T>> callback, HttpContext localContext, NHttpClientConnection conn, HttpProcessor httppocessor, ConnectionReuseStrategy connReuseStrategy) {
        Args.notEmpty(requestProducers, "Request producer list");
        Args.notEmpty(responseConsumers, "Response consumer list");
        Args.check((requestProducers.size() == responseConsumers.size()), "Number of request producers does not match that of response consumers");

        this.requestProducerQueue = new ConcurrentLinkedQueue<HttpAsyncRequestProducer>(requestProducers);
        this.responseConsumerQueue = new ConcurrentLinkedQueue<HttpAsyncResponseConsumer<T>>(responseConsumers);
        this.requestQueue = new ConcurrentLinkedQueue<HttpRequest>();
        this.resultQueue = new ConcurrentLinkedQueue<T>();
        this.future = new BasicFuture(callback);
        this.localContext = (HttpContext) Args.notNull(localContext, "HTTP context");
        this.conn = (NHttpClientConnection) Args.notNull(conn, "HTTP connection");
        this.httppocessor = (HttpProcessor) Args.notNull(httppocessor, "HTTP processor");
        this.connReuseStrategy = (connReuseStrategy != null) ? connReuseStrategy : (ConnectionReuseStrategy) DefaultConnectionReuseStrategy.INSTANCE;

        this.localContext.setAttribute("http.connection", this.conn);
        this.requestProducerRef = new AtomicReference<HttpAsyncRequestProducer>(null);
        this.responseConsumerRef = new AtomicReference<HttpAsyncResponseConsumer<T>>(null);
        this.keepAlive = new AtomicBoolean(false);
        this.closed = new AtomicBoolean(false);
    }

    public PipeliningClientExchangeHandler(List<? extends HttpAsyncRequestProducer> requestProducers, List<? extends HttpAsyncResponseConsumer<T>> responseConsumers, HttpContext localContext, NHttpClientConnection conn, HttpProcessor httppocessor) {
        this(requestProducers, responseConsumers, null, localContext, conn, httppocessor, null);
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
            }
        }
    }

    public Future<List<T>> getFuture() {
        return (Future<List<T>>) this.future;
    }

    private void releaseResources() {
        closeQuietly(this.requestProducerRef.getAndSet(null));
        closeQuietly(this.responseConsumerRef.getAndSet(null));
        while (!this.requestProducerQueue.isEmpty()) {
            closeQuietly(this.requestProducerQueue.remove());
        }
        while (!this.responseConsumerQueue.isEmpty()) {
            closeQuietly(this.responseConsumerQueue.remove());
        }
        this.requestQueue.clear();
        this.resultQueue.clear();
    }

    public void close() throws IOException {
        if (this.closed.compareAndSet(false, true)) {
            releaseResources();
            if (!this.future.isDone()) {
                this.future.cancel();
            }
        }
    }

    public HttpRequest generateRequest() throws IOException, HttpException {
        Asserts.check((this.requestProducerRef.get() == null), "Inconsistent state: request producer is not null");
        HttpAsyncRequestProducer requestProducer = this.requestProducerQueue.poll();
        if (requestProducer == null) {
            return null;
        }
        this.requestProducerRef.set(requestProducer);
        HttpRequest request = requestProducer.generateRequest();
        this.httppocessor.process(request, this.localContext);
        this.requestQueue.add(request);
        return request;
    }

    public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
        HttpAsyncRequestProducer requestProducer = this.requestProducerRef.get();
        Asserts.check((requestProducer != null), "Inconsistent state: request producer is null");
        requestProducer.produceContent(encoder, ioctrl);
    }

    public void requestCompleted() {
        HttpAsyncRequestProducer requestProducer = this.requestProducerRef.getAndSet(null);
        Asserts.check((requestProducer != null), "Inconsistent state: request producer is null");
        requestProducer.requestCompleted(this.localContext);
    }

    public void responseReceived(HttpResponse response) throws IOException, HttpException {
        Asserts.check((this.responseConsumerRef.get() == null), "Inconsistent state: response consumer is not null");

        HttpAsyncResponseConsumer<T> responseConsumer = this.responseConsumerQueue.poll();
        Asserts.check((responseConsumer != null), "Inconsistent state: response consumer queue is empty");
        this.responseConsumerRef.set(responseConsumer);

        HttpRequest request = this.requestQueue.poll();
        Asserts.check((request != null), "Inconsistent state: request queue is empty");

        this.localContext.setAttribute("http.request", request);
        this.localContext.setAttribute("http.response", response);
        this.httppocessor.process(response, this.localContext);

        responseConsumer.responseReceived(response);
        this.keepAlive.set(this.connReuseStrategy.keepAlive(response, this.localContext));
    }

    public void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
        HttpAsyncResponseConsumer<T> responseConsumer = this.responseConsumerRef.get();
        Asserts.check((responseConsumer != null), "Inconsistent state: response consumer is null");
        responseConsumer.consumeContent(decoder, ioctrl);
    }

    public void responseCompleted() throws IOException {
        HttpAsyncResponseConsumer<T> responseConsumer = this.responseConsumerRef.getAndSet(null);
        Asserts.check((responseConsumer != null), "Inconsistent state: response consumer is null");
        try {
            if (!this.keepAlive.get()) {
                this.conn.close();
            }
            responseConsumer.responseCompleted(this.localContext);
            T result = responseConsumer.getResult();
            Exception ex = responseConsumer.getException();
            if (result != null) {
                this.resultQueue.add(result);
            } else {
                this.future.failed(ex);
                this.conn.shutdown();
            }
            if (!this.conn.isOpen() &&
                    this.closed.compareAndSet(false, true)) {
                releaseResources();
            }

            if (!this.future.isDone() && this.responseConsumerQueue.isEmpty()) {
                this.future.completed(new ArrayList<T>(this.resultQueue));
                this.resultQueue.clear();
            }
        } catch (RuntimeException ex) {
            failed(ex);
            throw ex;
        }
    }

    public void inputTerminated() {
        failed((Exception) new ConnectionClosedException("Connection closed"));
    }

    public void failed(Exception ex) {
        if (this.closed.compareAndSet(false, true)) {
            try {
                HttpAsyncRequestProducer requestProducer = this.requestProducerRef.get();
                if (requestProducer != null) {
                    requestProducer.failed(ex);
                }
                HttpAsyncResponseConsumer<T> responseConsumer = this.responseConsumerRef.get();
                if (responseConsumer != null) {
                    responseConsumer.failed(ex);
                }
            } finally {
                try {
                    this.future.failed(ex);
                } finally {
                    releaseResources();
                }
            }
        }
    }

    public boolean cancel() {
        if (this.closed.compareAndSet(false, true)) {
            try {
                try {
                    HttpAsyncResponseConsumer<T> responseConsumer = this.responseConsumerRef.get();
                    return (responseConsumer != null && responseConsumer.cancel());
                } finally {
                    this.future.cancel();
                }
            } finally {
                releaseResources();
            }
        }
        return false;
    }

    public boolean isDone() {
        return this.future.isDone();
    }
}

