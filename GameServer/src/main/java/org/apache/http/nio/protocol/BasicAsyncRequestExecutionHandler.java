package org.apache.http.nio.protocol;

import java.io.IOException;
import java.util.concurrent.Future;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

@Deprecated
public class BasicAsyncRequestExecutionHandler<T>
implements HttpAsyncRequestExecutionHandler<T>
{
private final HttpAsyncRequestProducer requestProducer;
private final HttpAsyncResponseConsumer<T> responseConsumer;
private final BasicFuture<T> future;
private final HttpContext localContext;
private final HttpProcessor httppocessor;
private final ConnectionReuseStrategy reuseStrategy;
private volatile boolean requestSent;

public BasicAsyncRequestExecutionHandler(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback, HttpContext localContext, HttpProcessor httppocessor, ConnectionReuseStrategy reuseStrategy, HttpParams params) {
Args.notNull(requestProducer, "Request producer");
Args.notNull(responseConsumer, "Response consumer");
Args.notNull(localContext, "HTTP context");
Args.notNull(httppocessor, "HTTP processor");
Args.notNull(reuseStrategy, "Connection reuse strategy");
Args.notNull(params, "HTTP parameters");
this.requestProducer = requestProducer;
this.responseConsumer = responseConsumer;
this.future = new BasicFuture(callback);
this.localContext = localContext;
this.httppocessor = httppocessor;
this.reuseStrategy = reuseStrategy;
}

public BasicAsyncRequestExecutionHandler(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext localContext, HttpProcessor httppocessor, ConnectionReuseStrategy reuseStrategy, HttpParams params) {
this(requestProducer, responseConsumer, null, localContext, httppocessor, reuseStrategy, params);
}

public Future<T> getFuture() {
return (Future<T>)this.future;
}

private void releaseResources() {
try {
this.responseConsumer.close();
} catch (IOException ex) {}

try {
this.requestProducer.close();
} catch (IOException ex) {}
}

public void close() throws IOException {
releaseResources();
if (!this.future.isDone()) {
this.future.cancel();
}
}

public HttpHost getTarget() {
return this.requestProducer.getTarget();
}

public HttpRequest generateRequest() throws IOException, HttpException {
return this.requestProducer.generateRequest();
}

public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
this.requestProducer.produceContent(encoder, ioctrl);
}

public void requestCompleted(HttpContext context) {
this.requestProducer.requestCompleted(context);
this.requestSent = true;
}

public boolean isRepeatable() {
return false;
}

public void resetRequest() {}

public void responseReceived(HttpResponse response) throws IOException, HttpException {
this.responseConsumer.responseReceived(response);
}

public void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
this.responseConsumer.consumeContent(decoder, ioctrl);
}

public void failed(Exception ex) {
try {
if (!this.requestSent) {
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

public boolean cancel() {
try {
boolean cancelled = this.responseConsumer.cancel();
this.future.cancel();
releaseResources();
return cancelled;
} catch (RuntimeException ex) {
failed(ex);
throw ex;
} 
}

public void responseCompleted(HttpContext context) {
try {
this.responseConsumer.responseCompleted(context);
T result = this.responseConsumer.getResult();
Exception ex = this.responseConsumer.getException();
if (ex == null) {
this.future.completed(result);
} else {
this.future.failed(ex);
} 
releaseResources();
} catch (RuntimeException ex) {
failed(ex);
throw ex;
} 
}

public T getResult() {
return this.responseConsumer.getResult();
}

public Exception getException() {
return this.responseConsumer.getException();
}

public HttpContext getContext() {
return this.localContext;
}

public HttpProcessor getHttpProcessor() {
return this.httppocessor;
}

public ConnectionReuseStrategy getConnectionReuseStrategy() {
return this.reuseStrategy;
}

public boolean isDone() {
return this.responseConsumer.isDone();
}
}

