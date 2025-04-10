package org.apache.http.nio.protocol;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import org.apache.http.ConnectionClosedException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.ConnPool;
import org.apache.http.pool.PoolEntry;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

@Immutable
public class HttpAsyncRequester
{
private final HttpProcessor httpprocessor;
private final ConnectionReuseStrategy connReuseStrategy;
private final ExceptionLogger exceptionLogger;

@Deprecated
public HttpAsyncRequester(HttpProcessor httpprocessor, ConnectionReuseStrategy reuseStrategy, HttpParams params) {
this(httpprocessor, reuseStrategy);
}

public HttpAsyncRequester(HttpProcessor httpprocessor, ConnectionReuseStrategy connReuseStrategy, ExceptionLogger exceptionLogger) {
this.httpprocessor = (HttpProcessor)Args.notNull(httpprocessor, "HTTP processor");
this.connReuseStrategy = (connReuseStrategy != null) ? connReuseStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;

this.exceptionLogger = (exceptionLogger != null) ? exceptionLogger : ExceptionLogger.NO_OP;
}

public HttpAsyncRequester(HttpProcessor httpprocessor, ConnectionReuseStrategy connReuseStrategy) {
this(httpprocessor, connReuseStrategy, (ExceptionLogger)null);
}

public HttpAsyncRequester(HttpProcessor httpprocessor) {
this(httpprocessor, null);
}

public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, NHttpClientConnection conn, HttpContext context, FutureCallback<T> callback) {
Args.notNull(requestProducer, "HTTP request producer");
Args.notNull(responseConsumer, "HTTP response consumer");
Args.notNull(conn, "HTTP connection");
Args.notNull(context, "HTTP context");
BasicAsyncClientExchangeHandler<T> handler = new BasicAsyncClientExchangeHandler<T>(requestProducer, responseConsumer, callback, context, conn, this.httpprocessor, this.connReuseStrategy);

initExection(handler, conn);
return handler.getFuture();
}

private void initExection(HttpAsyncClientExchangeHandler handler, NHttpClientConnection conn) {
conn.getContext().setAttribute("http.nio.exchange-handler", handler);
if (!conn.isOpen()) {
handler.failed((Exception)new ConnectionClosedException("Connection closed"));
try {
handler.close();
} catch (IOException ex) {
log(ex);
} 
} else {
conn.requestOutput();
} 
}

public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, NHttpClientConnection conn, HttpContext context) {
return execute(requestProducer, responseConsumer, conn, context, (FutureCallback<T>)null);
}

public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, NHttpClientConnection conn) {
return execute(requestProducer, responseConsumer, conn, (HttpContext)new BasicHttpContext());
}

public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, ConnPool<HttpHost, E> connPool, HttpContext context, FutureCallback<T> callback) {
Args.notNull(requestProducer, "HTTP request producer");
Args.notNull(responseConsumer, "HTTP response consumer");
Args.notNull(connPool, "HTTP connection pool");
Args.notNull(context, "HTTP context");
BasicFuture<T> future = new BasicFuture(callback);
HttpHost target = requestProducer.getTarget();
connPool.lease(target, null, new ConnRequestCallback<T, E>(future, requestProducer, responseConsumer, connPool, context));

return (Future<T>)future;
}

public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<List<T>> executePipelined(HttpHost target, List<? extends HttpAsyncRequestProducer> requestProducers, List<? extends HttpAsyncResponseConsumer<T>> responseConsumers, ConnPool<HttpHost, E> connPool, HttpContext context, FutureCallback<List<T>> callback) {
Args.notNull(target, "HTTP target");
Args.notEmpty(requestProducers, "Request producer list");
Args.notEmpty(responseConsumers, "Response consumer list");
Args.notNull(connPool, "HTTP connection pool");
Args.notNull(context, "HTTP context");
BasicFuture<List<T>> future = new BasicFuture(callback);
connPool.lease(target, null, new ConnPipelinedRequestCallback<T, E>(future, requestProducers, responseConsumers, connPool, context));

return (Future<List<T>>)future;
}

public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, E poolEntry, ConnPool<HttpHost, E> connPool, HttpContext context, FutureCallback<T> callback) {
Args.notNull(requestProducer, "HTTP request producer");
Args.notNull(responseConsumer, "HTTP response consumer");
Args.notNull(connPool, "HTTP connection pool");
Args.notNull(poolEntry, "Pool entry");
Args.notNull(context, "HTTP context");
BasicFuture<T> future = new BasicFuture(callback);
NHttpClientConnection conn = (NHttpClientConnection)poolEntry.getConnection();
BasicAsyncClientExchangeHandler<T> handler = new BasicAsyncClientExchangeHandler<T>(requestProducer, responseConsumer, new RequestExecutionCallback<T, E>(future, poolEntry, connPool), context, conn, this.httpprocessor, this.connReuseStrategy);

initExection(handler, conn);
return (Future<T>)future;
}

public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<List<T>> executePipelined(List<HttpAsyncRequestProducer> requestProducers, List<HttpAsyncResponseConsumer<T>> responseConsumers, E poolEntry, ConnPool<HttpHost, E> connPool, HttpContext context, FutureCallback<List<T>> callback) {
Args.notEmpty(requestProducers, "Request producer list");
Args.notEmpty(responseConsumers, "Response consumer list");
Args.notNull(connPool, "HTTP connection pool");
Args.notNull(poolEntry, "Pool entry");
Args.notNull(context, "HTTP context");
BasicFuture<List<T>> future = new BasicFuture(callback);
NHttpClientConnection conn = (NHttpClientConnection)poolEntry.getConnection();
PipeliningClientExchangeHandler<T> handler = new PipeliningClientExchangeHandler<T>(requestProducers, responseConsumers, new RequestExecutionCallback<List<T>, E>(future, poolEntry, connPool), context, conn, this.httpprocessor, this.connReuseStrategy);

initExection(handler, conn);
return (Future<List<T>>)future;
}

public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, ConnPool<HttpHost, E> connPool, HttpContext context) {
return execute(requestProducer, responseConsumer, connPool, context, (FutureCallback<T>)null);
}

public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, ConnPool<HttpHost, E> connPool) {
return execute(requestProducer, responseConsumer, connPool, (HttpContext)new BasicHttpContext());
}

class ConnRequestCallback<T, E extends PoolEntry<HttpHost, NHttpClientConnection>>
implements FutureCallback<E>
{
private final BasicFuture<T> requestFuture;

private final HttpAsyncRequestProducer requestProducer;

private final HttpAsyncResponseConsumer<T> responseConsumer;

private final ConnPool<HttpHost, E> connPool;

private final HttpContext context;

ConnRequestCallback(BasicFuture<T> requestFuture, HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, ConnPool<HttpHost, E> connPool, HttpContext context) {
this.requestFuture = requestFuture;
this.requestProducer = requestProducer;
this.responseConsumer = responseConsumer;
this.connPool = connPool;
this.context = context;
}

public void completed(E result) {
if (this.requestFuture.isDone()) {
this.connPool.release(result, true);
return;
} 
NHttpClientConnection conn = (NHttpClientConnection)result.getConnection();
BasicAsyncClientExchangeHandler<T> handler = new BasicAsyncClientExchangeHandler<T>(this.requestProducer, this.responseConsumer, new HttpAsyncRequester.RequestExecutionCallback<T, E>(this.requestFuture, result, this.connPool), this.context, conn, HttpAsyncRequester.this.httpprocessor, HttpAsyncRequester.this.connReuseStrategy);

HttpAsyncRequester.this.initExection(handler, conn);
}

public void failed(Exception ex) {
try {
try {
this.responseConsumer.failed(ex);
} finally {
releaseResources();
} 
} finally {
this.requestFuture.failed(ex);
} 
}

public void cancelled() {
try {
try {
this.responseConsumer.cancel();
} finally {
releaseResources();
} 
} finally {
this.requestFuture.cancel(true);
} 
}

public void releaseResources() {
HttpAsyncRequester.this.close(this.requestProducer);
HttpAsyncRequester.this.close(this.responseConsumer);
}
}

class ConnPipelinedRequestCallback<T, E extends PoolEntry<HttpHost, NHttpClientConnection>>
implements FutureCallback<E>
{
private final BasicFuture<List<T>> requestFuture;

private final List<? extends HttpAsyncRequestProducer> requestProducers;

private final List<? extends HttpAsyncResponseConsumer<T>> responseConsumers;

private final ConnPool<HttpHost, E> connPool;

private final HttpContext context;

ConnPipelinedRequestCallback(BasicFuture<List<T>> requestFuture, List<? extends HttpAsyncRequestProducer> requestProducers, List<? extends HttpAsyncResponseConsumer<T>> responseConsumers, ConnPool<HttpHost, E> connPool, HttpContext context) {
this.requestFuture = requestFuture;
this.requestProducers = requestProducers;
this.responseConsumers = responseConsumers;
this.connPool = connPool;
this.context = context;
}

public void completed(E result) {
if (this.requestFuture.isDone()) {
this.connPool.release(result, true);
return;
} 
NHttpClientConnection conn = (NHttpClientConnection)result.getConnection();
PipeliningClientExchangeHandler<T> handler = new PipeliningClientExchangeHandler<T>(this.requestProducers, this.responseConsumers, new HttpAsyncRequester.RequestExecutionCallback<List<T>, E>(this.requestFuture, result, this.connPool), this.context, conn, HttpAsyncRequester.this.httpprocessor, HttpAsyncRequester.this.connReuseStrategy);

HttpAsyncRequester.this.initExection(handler, conn);
}

public void failed(Exception ex) {
try {
try {
for (HttpAsyncResponseConsumer<T> responseConsumer : this.responseConsumers) {
responseConsumer.failed(ex);
}
} finally {
releaseResources();
} 
} finally {
this.requestFuture.failed(ex);
} 
}

public void cancelled() {
try {
try {
for (HttpAsyncResponseConsumer<T> responseConsumer : this.responseConsumers) {
responseConsumer.cancel();
}
} finally {
releaseResources();
} 
} finally {
this.requestFuture.cancel(true);
} 
}

public void releaseResources() {
for (HttpAsyncRequestProducer requestProducer : this.requestProducers) {
HttpAsyncRequester.this.close(requestProducer);
}
for (HttpAsyncResponseConsumer<T> responseConsumer : this.responseConsumers) {
HttpAsyncRequester.this.close(responseConsumer);
}
}
}

class RequestExecutionCallback<T, E extends PoolEntry<HttpHost, NHttpClientConnection>>
implements FutureCallback<T>
{
private final BasicFuture<T> future;

private final E poolEntry;

private final ConnPool<HttpHost, E> connPool;

RequestExecutionCallback(BasicFuture<T> future, E poolEntry, ConnPool<HttpHost, E> connPool) {
this.future = future;
this.poolEntry = poolEntry;
this.connPool = connPool;
}

public void completed(T result) {
try {
this.connPool.release(this.poolEntry, true);
} finally {
this.future.completed(result);
} 
}

public void failed(Exception ex) {
try {
this.connPool.release(this.poolEntry, false);
} finally {
this.future.failed(ex);
} 
}

public void cancelled() {
try {
this.connPool.release(this.poolEntry, false);
} finally {
this.future.cancel(true);
} 
}
}

protected void log(Exception ex) {
this.exceptionLogger.log(ex);
}

private void close(Closeable closeable) {
try {
closeable.close();
} catch (IOException ex) {
log(ex);
} 
}
}

