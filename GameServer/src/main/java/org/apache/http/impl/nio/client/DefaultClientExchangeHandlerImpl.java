package org.apache.http.impl.nio.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.logging.Log;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.nio.protocol.HttpAsyncClientExchangeHandler;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

class DefaultClientExchangeHandlerImpl<T>
implements HttpAsyncClientExchangeHandler, InternalConnManager, Cancellable
{
private final Log log;
private final HttpAsyncRequestProducer requestProducer;
private final HttpAsyncResponseConsumer<T> responseConsumer;
private final HttpClientContext localContext;
private final BasicFuture<T> resultFuture;
private final NHttpClientConnectionManager connmgr;
private final InternalClientExec exec;
private final InternalState state;
private final AtomicReference<NHttpClientConnection> managedConn;
private final AtomicBoolean closed;
private final AtomicBoolean completed;

public DefaultClientExchangeHandlerImpl(Log log, HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpClientContext localContext, BasicFuture<T> resultFuture, NHttpClientConnectionManager connmgr, InternalClientExec exec) {
this.log = log;
this.requestProducer = requestProducer;
this.responseConsumer = responseConsumer;
this.localContext = localContext;
this.resultFuture = resultFuture;
this.connmgr = connmgr;
this.exec = exec;
this.state = new InternalState(requestProducer, responseConsumer, localContext);
this.closed = new AtomicBoolean(false);
this.completed = new AtomicBoolean(false);
this.managedConn = new AtomicReference<NHttpClientConnection>(null);
}

public void close() {
if (this.closed.getAndSet(true)) {
return;
}
abortConnection();
try {
this.requestProducer.close();
} catch (IOException ex) {
this.log.debug("I/O error closing request producer", ex);
} 
try {
this.responseConsumer.close();
} catch (IOException ex) {
this.log.debug("I/O error closing response consumer", ex);
} 
}

public void start() throws HttpException, IOException {
HttpHost target = this.requestProducer.getTarget();
HttpRequest original = this.requestProducer.generateRequest();

if (original instanceof HttpExecutionAware) {
((HttpExecutionAware)original).setCancellable(this);
}
this.exec.prepare(this.state, target, original);
requestConnection();
}

public boolean isDone() {
return this.completed.get();
}

public HttpRequest generateRequest() throws IOException, HttpException {
return this.exec.generateRequest(this.state, this);
}

public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
this.exec.produceContent(this.state, encoder, ioctrl);
}

public void requestCompleted() {
this.exec.requestCompleted(this.state);
}

public void responseReceived(HttpResponse response) throws IOException, HttpException {
this.exec.responseReceived(this.state, response);
}

public void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
this.exec.consumeContent(this.state, decoder, ioctrl);
if (!decoder.isCompleted() && this.responseConsumer.isDone()) {
if (this.completed.compareAndSet(false, true)) {
this.resultFuture.cancel();
}
this.state.setNonReusable();
releaseConnection();
} 
}

public void responseCompleted() throws IOException, HttpException {
this.exec.responseCompleted(this.state, this);

if (this.state.getFinalResponse() != null || this.resultFuture.isDone()) {
try {
this.completed.set(true);
releaseConnection();
T result = (T)this.responseConsumer.getResult();
Exception ex = this.responseConsumer.getException();
if (ex == null) {
this.resultFuture.completed(result);
} else {
this.resultFuture.failed(ex);
} 
} finally {
close();
} 
} else {
NHttpClientConnection localConn = this.managedConn.get();
if (localConn != null && !localConn.isOpen()) {
releaseConnection();
localConn = null;
} 
if (localConn != null) {
localConn.requestOutput();
} else {
requestConnection();
} 
} 
}

public void inputTerminated() {
if (!this.completed.get()) {
requestConnection();
} else {
close();
} 
}

public void releaseConnection() {
NHttpClientConnection localConn = this.managedConn.getAndSet(null);
if (localConn != null) {
if (this.log.isDebugEnabled()) {
this.log.debug("[exchange: " + this.state.getId() + "] releasing connection");
}
localConn.getContext().removeAttribute("http.nio.exchange-handler");
if (this.state.isReusable()) {
this.connmgr.releaseConnection(localConn, this.localContext.getUserToken(), this.state.getValidDuration(), TimeUnit.MILLISECONDS);
} else {

try {
localConn.close();
if (this.log.isDebugEnabled()) {
this.log.debug("[exchange: " + this.state.getId() + "] connection discarded");
}
} catch (IOException ex) {
if (this.log.isDebugEnabled()) {
this.log.debug(ex.getMessage(), ex);
}
} finally {
this.connmgr.releaseConnection(localConn, null, 0L, TimeUnit.MILLISECONDS);
} 
} 
} 
}

public void abortConnection() {
discardConnection();
}

private void discardConnection() {
NHttpClientConnection localConn = this.managedConn.getAndSet(null);
if (localConn != null) {
try {
localConn.shutdown();
if (this.log.isDebugEnabled()) {
this.log.debug("[exchange: " + this.state.getId() + "] connection aborted");
}
} catch (IOException ex) {
if (this.log.isDebugEnabled()) {
this.log.debug(ex.getMessage(), ex);
}
} finally {
this.connmgr.releaseConnection(localConn, null, 0L, TimeUnit.MILLISECONDS);
} 
}
}

public void failed(Exception ex) {
try {
this.requestProducer.failed(ex);
this.responseConsumer.failed(ex);
} finally {
try {
this.resultFuture.failed(ex);
} finally {
close();
} 
} 
}

public boolean cancel() {
if (this.log.isDebugEnabled()) {
this.log.debug("[exchange: " + this.state.getId() + "] Cancelled");
}
try {
boolean cancelled = this.responseConsumer.cancel();

T result = (T)this.responseConsumer.getResult();
Exception ex = this.responseConsumer.getException();
if (ex != null) {
this.resultFuture.failed(ex);
} else if (result != null) {
this.resultFuture.completed(result);
} else {
this.resultFuture.cancel();
} 
return cancelled;
} catch (RuntimeException runex) {
this.resultFuture.failed(runex);
throw runex;
} finally {
close();
} 
}

private void connectionAllocated(NHttpClientConnection managedConn) {
try {
if (this.log.isDebugEnabled()) {
this.log.debug("[exchange: " + this.state.getId() + "] Connection allocated: " + managedConn);
}
this.managedConn.set(managedConn);

if (this.closed.get()) {
releaseConnection();

return;
} 
managedConn.getContext().setAttribute("http.nio.exchange-handler", this);
managedConn.requestOutput();
if (!managedConn.isOpen()) {
failed((Exception)new ConnectionClosedException("Connection closed"));
}
} catch (RuntimeException runex) {
failed(runex);
throw runex;
} 
}

private void connectionRequestFailed(Exception ex) {
if (this.log.isDebugEnabled()) {
this.log.debug("[exchange: " + this.state.getId() + "] connection request failed");
}
try {
this.resultFuture.failed(ex);
} finally {
close();
} 
}

private void connectionRequestCancelled() {
if (this.log.isDebugEnabled()) {
this.log.debug("[exchange: " + this.state.getId() + "] Connection request cancelled");
}
try {
this.resultFuture.cancel();
} finally {
close();
} 
}

private void requestConnection() {
if (this.log.isDebugEnabled()) {
this.log.debug("[exchange: " + this.state.getId() + "] Request connection for " + this.state.getRoute());
}

discardConnection();

this.state.setValidDuration(0L);
this.state.setNonReusable();
this.state.setRouteEstablished(false);
this.state.setRouteTracker(null);

HttpRoute route = this.state.getRoute();
Object userToken = this.localContext.getUserToken();
RequestConfig config = this.localContext.getRequestConfig();
this.connmgr.requestConnection(route, userToken, config.getConnectTimeout(), config.getConnectionRequestTimeout(), TimeUnit.MILLISECONDS, new FutureCallback<NHttpClientConnection>()
{

public void completed(NHttpClientConnection managedConn)
{
DefaultClientExchangeHandlerImpl.this.connectionAllocated(managedConn);
}

public void failed(Exception ex) {
DefaultClientExchangeHandlerImpl.this.connectionRequestFailed(ex);
}

public void cancelled() {
DefaultClientExchangeHandlerImpl.this.connectionRequestCancelled();
}
});
}

public NHttpClientConnection getConnection() {
return this.managedConn.get();
}
}

