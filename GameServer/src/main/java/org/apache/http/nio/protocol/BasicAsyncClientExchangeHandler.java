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

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class BasicAsyncClientExchangeHandler<T>
        implements HttpAsyncClientExchangeHandler {
    private final HttpAsyncRequestProducer requestProducer;
    private final HttpAsyncResponseConsumer<T> responseConsumer;
    private final BasicFuture<T> future;
    private final HttpContext localContext;
    private final NHttpClientConnection conn;
    private final HttpProcessor httppocessor;
    private final ConnectionReuseStrategy connReuseStrategy;
    private final AtomicBoolean requestSent;
    private final AtomicBoolean keepAlive;
    private final AtomicBoolean closed;

    public BasicAsyncClientExchangeHandler(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback, HttpContext localContext, NHttpClientConnection conn, HttpProcessor httppocessor, ConnectionReuseStrategy connReuseStrategy) {
        this.requestProducer = (HttpAsyncRequestProducer) Args.notNull(requestProducer, "Request producer");
        this.responseConsumer = (HttpAsyncResponseConsumer<T>) Args.notNull(responseConsumer, "Response consumer");
        this.future = new BasicFuture(callback);
        this.localContext = (HttpContext) Args.notNull(localContext, "HTTP context");
        this.conn = (NHttpClientConnection) Args.notNull(conn, "HTTP connection");
        this.httppocessor = (HttpProcessor) Args.notNull(httppocessor, "HTTP processor");
        this.connReuseStrategy = (connReuseStrategy != null) ? connReuseStrategy : (ConnectionReuseStrategy) DefaultConnectionReuseStrategy.INSTANCE;

        this.requestSent = new AtomicBoolean(false);
        this.keepAlive = new AtomicBoolean(false);
        this.closed = new AtomicBoolean(false);
    }

    public BasicAsyncClientExchangeHandler(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext localContext, NHttpClientConnection conn, HttpProcessor httppocessor) {
        this(requestProducer, responseConsumer, null, localContext, conn, httppocessor, null);
    }

    public Future<T> getFuture() {
        return (Future<T>) this.future;
    }

    private void releaseResources() {
        try {
            this.responseConsumer.close();
        } catch (IOException ex) {
        }

        try {
            this.requestProducer.close();
        } catch (IOException ex) {
        }
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
        if (isDone()) {
            return null;
        }
        HttpRequest request = this.requestProducer.generateRequest();
        this.localContext.setAttribute("http.request", request);
        this.localContext.setAttribute("http.connection", this.conn);
        this.httppocessor.process(request, this.localContext);
        return request;
    }

    public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
        this.requestProducer.produceContent(encoder, ioctrl);
    }

    public void requestCompleted() {
        this.requestProducer.requestCompleted(this.localContext);
        this.requestSent.set(true);
    }

    public void responseReceived(HttpResponse response) throws IOException, HttpException {
        this.localContext.setAttribute("http.response", response);
        this.httppocessor.process(response, this.localContext);
        this.responseConsumer.responseReceived(response);
        this.keepAlive.set(this.connReuseStrategy.keepAlive(response, this.localContext));
    }

    public void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
        this.responseConsumer.consumeContent(decoder, ioctrl);
    }

    public void responseCompleted() throws IOException {
        try {
            if (!this.keepAlive.get()) {
                this.conn.close();
            }
            this.responseConsumer.responseCompleted(this.localContext);
            T result = this.responseConsumer.getResult();
            Exception ex = this.responseConsumer.getException();
            if (result != null) {
                this.future.completed(result);
            } else {
                this.future.failed(ex);
            }
            if (this.closed.compareAndSet(false, true)) {
                releaseResources();
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
                if (!this.requestSent.get()) {
                    this.requestProducer.failed(ex);
                }
                this.responseConsumer.failed(ex);
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
                    return this.responseConsumer.cancel();
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
        return this.responseConsumer.isDone();
    }
}

