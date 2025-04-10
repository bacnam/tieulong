package org.apache.http.impl.nio.client;

import java.io.Closeable;
import java.net.URI;
import java.util.concurrent.Future;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@ThreadSafe
public abstract class CloseableHttpAsyncClient
implements HttpAsyncClient, Closeable
{
public abstract boolean isRunning();

public abstract void start();

public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
return execute(requestProducer, responseConsumer, (HttpContext)new BasicHttpContext(), callback);
}

public Future<HttpResponse> execute(HttpHost target, HttpRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
return execute(HttpAsyncMethods.create(target, request), HttpAsyncMethods.createConsumer(), context, callback);
}

public Future<HttpResponse> execute(HttpHost target, HttpRequest request, FutureCallback<HttpResponse> callback) {
return execute(target, request, (HttpContext)new BasicHttpContext(), callback);
}

public Future<HttpResponse> execute(HttpUriRequest request, FutureCallback<HttpResponse> callback) {
return execute(request, (HttpContext)new BasicHttpContext(), callback);
}

public Future<HttpResponse> execute(HttpUriRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
HttpHost target;
try {
target = determineTarget(request);
} catch (ClientProtocolException ex) {
BasicFuture<HttpResponse> future = new BasicFuture(callback);
future.failed((Exception)ex);
return (Future<HttpResponse>)future;
} 
return execute(target, (HttpRequest)request, context, callback);
}

private HttpHost determineTarget(HttpUriRequest request) throws ClientProtocolException {
Args.notNull(request, "HTTP request");

HttpHost target = null;

URI requestURI = request.getURI();
if (requestURI.isAbsolute()) {
target = URIUtils.extractHost(requestURI);
if (target == null) {
throw new ClientProtocolException("URI does not specify a valid host name: " + requestURI);
}
} 

return target;
}
}

