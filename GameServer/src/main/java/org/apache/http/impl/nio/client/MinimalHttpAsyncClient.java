package org.apache.http.impl.nio.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.*;
import org.apache.http.util.Asserts;
import org.apache.http.util.VersionInfo;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

class MinimalHttpAsyncClient
        extends CloseableHttpAsyncClientBase {
    private final Log log = LogFactory.getLog(getClass());

    private final NHttpClientConnectionManager connmgr;

    private final InternalClientExec execChain;

    public MinimalHttpAsyncClient(NHttpClientConnectionManager connmgr, ThreadFactory threadFactory) {
        super(connmgr, threadFactory);
        this.connmgr = connmgr;
        ImmutableHttpProcessor immutableHttpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{(HttpRequestInterceptor) new RequestContent(), (HttpRequestInterceptor) new RequestTargetHost(), (HttpRequestInterceptor) new RequestClientConnControl(), (HttpRequestInterceptor) new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpAsyncClient", "org.apache.http.nio.client", getClass()))});

        this.execChain = new MinimalClientExec(connmgr, (HttpProcessor) immutableHttpProcessor, (ConnectionReuseStrategy) DefaultConnectionReuseStrategy.INSTANCE, (ConnectionKeepAliveStrategy) DefaultConnectionKeepAliveStrategy.INSTANCE);
    }

    public MinimalHttpAsyncClient(NHttpClientConnectionManager connmgr) {
        this(connmgr, Executors.defaultThreadFactory());
    }

    public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
        CloseableHttpAsyncClientBase.Status status = getStatus();
        Asserts.check((status == CloseableHttpAsyncClientBase.Status.ACTIVE), "Request cannot be executed; I/O reactor status: %s", new Object[]{status});

        BasicFuture<T> future = new BasicFuture(callback);
        HttpClientContext localcontext = HttpClientContext.adapt((context != null) ? context : (HttpContext) new BasicHttpContext());

        DefaultClientExchangeHandlerImpl<T> handler = new DefaultClientExchangeHandlerImpl<T>(this.log, requestProducer, responseConsumer, localcontext, future, this.connmgr, this.execChain);

        try {
            handler.start();
        } catch (Exception ex) {
            handler.failed(ex);
        }
        return (Future<T>) future;
    }
}

