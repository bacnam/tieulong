package org.apache.http.nio.protocol;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.NHttpServerEventHandler;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.reactor.SessionBufferStatus;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Immutable
public class HttpAsyncService
implements NHttpServerEventHandler
{
static final String HTTP_EXCHANGE_STATE = "http.nio.http-exchange-state";
private final HttpProcessor httpProcessor;
private final ConnectionReuseStrategy connStrategy;
private final HttpResponseFactory responseFactory;
private final HttpAsyncRequestHandlerMapper handlerMapper;
private final HttpAsyncExpectationVerifier expectationVerifier;
private final ExceptionLogger exceptionLogger;

@Deprecated
public HttpAsyncService(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpAsyncRequestHandlerResolver handlerResolver, HttpAsyncExpectationVerifier expectationVerifier, HttpParams params) {
this(httpProcessor, connStrategy, responseFactory, new HttpAsyncRequestHandlerResolverAdapter(handlerResolver), expectationVerifier);
}

@Deprecated
public HttpAsyncService(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, HttpAsyncRequestHandlerResolver handlerResolver, HttpParams params) {
this(httpProcessor, connStrategy, (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE, new HttpAsyncRequestHandlerResolverAdapter(handlerResolver), null);
}

public HttpAsyncService(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpAsyncRequestHandlerMapper handlerMapper, HttpAsyncExpectationVerifier expectationVerifier) {
this(httpProcessor, connStrategy, responseFactory, handlerMapper, expectationVerifier, (ExceptionLogger)null);
}

public HttpAsyncService(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpAsyncRequestHandlerMapper handlerMapper, HttpAsyncExpectationVerifier expectationVerifier, ExceptionLogger exceptionLogger) {
this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
this.connStrategy = (connStrategy != null) ? connStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;

this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;

this.handlerMapper = handlerMapper;
this.expectationVerifier = expectationVerifier;
this.exceptionLogger = (exceptionLogger != null) ? exceptionLogger : ExceptionLogger.NO_OP;
}

public HttpAsyncService(HttpProcessor httpProcessor, HttpAsyncRequestHandlerMapper handlerMapper) {
this(httpProcessor, null, null, handlerMapper, null);
}

public HttpAsyncService(HttpProcessor httpProcessor, HttpAsyncRequestHandlerMapper handlerMapper, ExceptionLogger exceptionLogger) {
this(httpProcessor, (ConnectionReuseStrategy)null, (HttpResponseFactory)null, handlerMapper, (HttpAsyncExpectationVerifier)null, exceptionLogger);
}

public void connected(NHttpServerConnection conn) {
State state = new State();
conn.getContext().setAttribute("http.nio.http-exchange-state", state);
}

public void closed(NHttpServerConnection conn) {
State state = (State)conn.getContext().removeAttribute("http.nio.http-exchange-state");
if (state != null) {
state.setTerminated();
closeHandlers(state);
Cancellable cancellable = state.getCancellable();
if (cancellable != null) {
cancellable.cancel();
}
} 
}

public void exception(NHttpServerConnection conn, Exception cause) {
State state = getState((NHttpConnection)conn);
if (state == null) {
shutdownConnection((NHttpConnection)conn);
log(cause);
return;
} 
state.setTerminated();
closeHandlers(state, cause);
Cancellable cancellable = state.getCancellable();
if (cancellable != null) {
cancellable.cancel();
}
Queue<PipelineEntry> pipeline = state.getPipeline();
if (!pipeline.isEmpty() || conn.isResponseSubmitted() || state.getResponseState().compareTo(MessageState.INIT) > 0) {

shutdownConnection((NHttpConnection)conn);
} else {
try {
Incoming incoming = state.getIncoming();
HttpRequest request = (incoming != null) ? incoming.getRequest() : null;
HttpContext context = (incoming != null) ? incoming.getContext() : (HttpContext)new BasicHttpContext();
HttpAsyncResponseProducer responseProducer = handleException(cause, context);
HttpResponse response = responseProducer.generateResponse();
Outgoing outgoing = new Outgoing(request, response, responseProducer, context);
state.setResponseState(MessageState.INIT);
state.setOutgoing(outgoing);
commitFinalResponse(conn, state);
} catch (Exception ex) {
shutdownConnection((NHttpConnection)conn);
closeHandlers(state);
if (ex instanceof RuntimeException) {
throw (RuntimeException)ex;
}
log(ex);
} 
} 
}

public void requestReceived(NHttpServerConnection conn) throws IOException, HttpException {
State state = getState((NHttpConnection)conn);
Asserts.notNull(state, "Connection state");
Asserts.check((state.getRequestState() == MessageState.READY), "Unexpected request state %s", state.getRequestState());

HttpRequest request = conn.getHttpRequest();
BasicHttpContext basicHttpContext = new BasicHttpContext();

basicHttpContext.setAttribute("http.request", request);
basicHttpContext.setAttribute("http.connection", conn);
this.httpProcessor.process(request, (HttpContext)basicHttpContext);

HttpAsyncRequestHandler<Object> requestHandler = getRequestHandler(request);
HttpAsyncRequestConsumer<Object> consumer = requestHandler.processRequest(request, (HttpContext)basicHttpContext);
consumer.requestReceived(request);

Incoming incoming = new Incoming(request, requestHandler, consumer, (HttpContext)basicHttpContext);
state.setIncoming(incoming);

if (request instanceof HttpEntityEnclosingRequest) {

if (((HttpEntityEnclosingRequest)request).expectContinue() && state.getResponseState() == MessageState.READY && state.getPipeline().isEmpty() && (!(conn instanceof SessionBufferStatus) || !((SessionBufferStatus)conn).hasBufferedInput())) {

state.setRequestState(MessageState.ACK_EXPECTED);
HttpResponse ack = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 100, (HttpContext)basicHttpContext);

if (this.expectationVerifier != null) {
conn.suspendInput();
conn.suspendOutput();
HttpAsyncExchange httpAsyncExchange = new HttpAsyncExchangeImpl(request, ack, state, conn, (HttpContext)basicHttpContext);

this.expectationVerifier.verify(httpAsyncExchange, (HttpContext)basicHttpContext);
} else {
conn.submitResponse(ack);
state.setRequestState(MessageState.BODY_STREAM);
} 
} else {
state.setRequestState(MessageState.BODY_STREAM);
} 
} else {

completeRequest(incoming, conn, state);
} 
}

public void inputReady(NHttpServerConnection conn, ContentDecoder decoder) throws IOException, HttpException {
State state = getState((NHttpConnection)conn);
Asserts.notNull(state, "Connection state");
Asserts.check((state.getRequestState() == MessageState.BODY_STREAM), "Unexpected request state %s", state.getRequestState());

Incoming incoming = state.getIncoming();
Asserts.notNull(incoming, "Incoming request");
HttpAsyncRequestConsumer<?> consumer = incoming.getConsumer();
consumer.consumeContent(decoder, (IOControl)conn);
if (decoder.isCompleted()) {
completeRequest(incoming, conn, state);
}
}

public void responseReady(NHttpServerConnection conn) throws IOException, HttpException {
State state = getState((NHttpConnection)conn);
Asserts.notNull(state, "Connection state");
Asserts.check((state.getResponseState() == MessageState.READY || state.getResponseState() == MessageState.INIT), "Unexpected response state %s", state.getResponseState());

if (state.getRequestState() == MessageState.ACK_EXPECTED) {
Outgoing outgoing = state.getOutgoing();
Asserts.notNull(outgoing, "Outgoing response");

HttpResponse response = outgoing.getResponse();
int status = response.getStatusLine().getStatusCode();
if (status == 100) {
HttpContext context = outgoing.getContext();
HttpAsyncResponseProducer responseProducer = outgoing.getProducer();

try {
response.setEntity(null);
conn.requestInput();
state.setRequestState(MessageState.BODY_STREAM);
state.setOutgoing(null);
conn.submitResponse(response);
responseProducer.responseCompleted(context);
} finally {
responseProducer.close();
} 
} else if (status >= 400) {
conn.resetInput();
state.setRequestState(MessageState.READY);
commitFinalResponse(conn, state);
} else {
throw new HttpException("Invalid response: " + response.getStatusLine());
} 
} else {
if (state.getResponseState() == MessageState.READY) {
Queue<PipelineEntry> pipeline = state.getPipeline();
PipelineEntry pipelineEntry = pipeline.poll();
if (pipelineEntry == null) {
conn.suspendOutput();
return;
} 
state.setResponseState(MessageState.INIT);
Object result = pipelineEntry.getResult();
HttpRequest request = pipelineEntry.getRequest();
HttpContext context = pipelineEntry.getContext();
if (result != null) {
HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 200, context);

HttpAsyncExchangeImpl httpExchange = new HttpAsyncExchangeImpl(request, response, state, conn, context);

HttpAsyncRequestHandler<Object> handler = pipelineEntry.getHandler();
conn.suspendOutput();
try {
handler.handle(result, httpExchange, context);
} catch (RuntimeException ex) {
throw ex;
} catch (Exception ex) {
pipeline.add(new PipelineEntry(request, null, ex, handler, context));

state.setResponseState(MessageState.READY);
responseReady(conn);
return;
} 
} else {
Exception exception = pipelineEntry.getException();
HttpAsyncResponseProducer responseProducer = handleException((exception != null) ? exception : (Exception)new HttpException("Internal error processing request"), context);

HttpResponse error = responseProducer.generateResponse();
state.setOutgoing(new Outgoing(request, error, responseProducer, context));
} 
} 
if (state.getResponseState() == MessageState.INIT) {
Outgoing outgoing;
synchronized (state) {
outgoing = state.getOutgoing();
if (outgoing == null) {
conn.suspendOutput();
return;
} 
} 
HttpResponse response = outgoing.getResponse();
int status = response.getStatusLine().getStatusCode();
if (status >= 200) {
commitFinalResponse(conn, state);
} else {
throw new HttpException("Invalid response: " + response.getStatusLine());
} 
} 
} 
}

public void outputReady(NHttpServerConnection conn, ContentEncoder encoder) throws HttpException, IOException {
State state = getState((NHttpConnection)conn);
Asserts.notNull(state, "Connection state");
Asserts.check((state.getResponseState() == MessageState.BODY_STREAM), "Unexpected response state %s", state.getResponseState());

Outgoing outgoing = state.getOutgoing();
Asserts.notNull(outgoing, "Outgoing response");
HttpAsyncResponseProducer responseProducer = outgoing.getProducer();

responseProducer.produceContent(encoder, (IOControl)conn);

if (encoder.isCompleted()) {
completeResponse(outgoing, conn, state);
}
}

public void endOfInput(NHttpServerConnection conn) throws IOException {
if (conn.getSocketTimeout() <= 0) {
conn.setSocketTimeout(1000);
}
conn.close();
}

public void timeout(NHttpServerConnection conn) throws IOException {
State state = getState((NHttpConnection)conn);
if (state != null) {
closeHandlers(state, new SocketTimeoutException());
}
if (conn.getStatus() == 0) {
conn.close();
if (conn.getStatus() == 1)
{

conn.setSocketTimeout(250);
}
} else {
conn.shutdown();
} 
}

private State getState(NHttpConnection conn) {
return (State)conn.getContext().getAttribute("http.nio.http-exchange-state");
}

protected void log(Exception ex) {
this.exceptionLogger.log(ex);
}

private void shutdownConnection(NHttpConnection conn) {
try {
conn.shutdown();
} catch (IOException ex) {
log(ex);
} 
}

private void closeHandlers(State state, Exception ex) {
HttpAsyncRequestConsumer<Object> consumer = (state.getIncoming() != null) ? state.getIncoming().getConsumer() : null;

if (consumer != null) {
try {
consumer.failed(ex);
} finally {
try {
consumer.close();
} catch (IOException ioex) {
log(ioex);
} 
} 
}
HttpAsyncResponseProducer producer = (state.getOutgoing() != null) ? state.getOutgoing().getProducer() : null;

if (producer != null) {
try {
producer.failed(ex);
} finally {
try {
producer.close();
} catch (IOException ioex) {
log(ioex);
} 
} 
}
}

private void closeHandlers(State state) {
HttpAsyncRequestConsumer<Object> consumer = (state.getIncoming() != null) ? state.getIncoming().getConsumer() : null;

if (consumer != null) {
try {
consumer.close();
} catch (IOException ioex) {
log(ioex);
} 
}
HttpAsyncResponseProducer producer = (state.getOutgoing() != null) ? state.getOutgoing().getProducer() : null;

if (producer != null) {
try {
producer.close();
} catch (IOException ioex) {
log(ioex);
} 
}
}

protected HttpAsyncResponseProducer handleException(Exception ex, HttpContext context) {
int code;
if (ex instanceof org.apache.http.MethodNotSupportedException) {
code = 501;
} else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
code = 505;
} else if (ex instanceof org.apache.http.ProtocolException) {
code = 400;
} else {
code = 500;
} 
String message = ex.getMessage();
if (message == null) {
message = ex.toString();
}
HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, code, context);

return new ErrorResponseProducer(response, (HttpEntity)new NStringEntity(message, ContentType.DEFAULT_TEXT), false);
}

protected void handleAlreadySubmittedResponse(Cancellable cancellable, HttpContext context) {
throw new IllegalStateException("Response already submitted");
}

protected void handleAlreadySubmittedResponse(HttpAsyncResponseProducer responseProducer, HttpContext context) {
throw new IllegalStateException("Response already submitted");
}

private boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
if (request != null && "HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
return false;
}
int status = response.getStatusLine().getStatusCode();
return (status >= 200 && status != 204 && status != 304 && status != 205);
}

private void completeRequest(Incoming incoming, NHttpServerConnection conn, State state) throws IOException, HttpException {
PipelineEntry pipelineEntry;
state.setRequestState(MessageState.READY);
state.setIncoming(null);

HttpAsyncRequestConsumer<?> consumer = incoming.getConsumer();
try {
HttpContext context = incoming.getContext();
consumer.requestCompleted(context);
pipelineEntry = new PipelineEntry(incoming.getRequest(), consumer.getResult(), consumer.getException(), incoming.getHandler(), context);

}
finally {

consumer.close();
} 
Queue<PipelineEntry> pipeline = state.getPipeline();
pipeline.add(pipelineEntry);
if (state.getResponseState() == MessageState.READY) {
conn.requestOutput();
}
}

private void commitFinalResponse(NHttpServerConnection conn, State state) throws IOException, HttpException {
Outgoing outgoing = state.getOutgoing();
Asserts.notNull(outgoing, "Outgoing response");
HttpRequest request = outgoing.getRequest();
HttpResponse response = outgoing.getResponse();
HttpContext context = outgoing.getContext();

context.setAttribute("http.response", response);
this.httpProcessor.process(response, context);

HttpEntity entity = response.getEntity();
if (entity != null && !canResponseHaveBody(request, response)) {
response.setEntity(null);
entity = null;
} 

conn.submitResponse(response);

if (entity == null) {
completeResponse(outgoing, conn, state);
} else {
state.setResponseState(MessageState.BODY_STREAM);
} 
}

private void completeResponse(Outgoing outgoing, NHttpServerConnection conn, State state) throws IOException, HttpException {
HttpContext context = outgoing.getContext();
HttpResponse response = outgoing.getResponse();
HttpAsyncResponseProducer responseProducer = outgoing.getProducer();
try {
responseProducer.responseCompleted(context);
state.setOutgoing(null);
state.setCancellable(null);
state.setResponseState(MessageState.READY);
} finally {
responseProducer.close();
} 
if (!this.connStrategy.keepAlive(response, context)) {
conn.close();
} else {

Queue<PipelineEntry> pipeline = state.getPipeline();
if (pipeline.isEmpty()) {
conn.suspendOutput();
}
conn.requestInput();
} 
}

private HttpAsyncRequestHandler<Object> getRequestHandler(HttpRequest request) {
HttpAsyncRequestHandler<Object> handler = null;
if (this.handlerMapper != null) {
handler = (HttpAsyncRequestHandler)this.handlerMapper.lookup(request);
}
if (handler == null) {
handler = new NullRequestHandler();
}
return handler;
}

static class Incoming
{
private final HttpRequest request;

private final HttpAsyncRequestHandler<Object> handler;

private final HttpAsyncRequestConsumer<Object> consumer;

private final HttpContext context;

Incoming(HttpRequest request, HttpAsyncRequestHandler<Object> handler, HttpAsyncRequestConsumer<Object> consumer, HttpContext context) {
this.request = request;
this.handler = handler;
this.consumer = consumer;
this.context = context;
}

public HttpRequest getRequest() {
return this.request;
}

public HttpAsyncRequestHandler<Object> getHandler() {
return this.handler;
}

public HttpAsyncRequestConsumer<Object> getConsumer() {
return this.consumer;
}

public HttpContext getContext() {
return this.context;
}
}

static class Outgoing
{
private final HttpRequest request;

private final HttpResponse response;

private final HttpAsyncResponseProducer producer;

private final HttpContext context;

Outgoing(HttpRequest request, HttpResponse response, HttpAsyncResponseProducer producer, HttpContext context) {
this.request = request;
this.response = response;
this.producer = producer;
this.context = context;
}

public HttpRequest getRequest() {
return this.request;
}

public HttpResponse getResponse() {
return this.response;
}

public HttpAsyncResponseProducer getProducer() {
return this.producer;
}

public HttpContext getContext() {
return this.context;
}
}

static class PipelineEntry
{
private final HttpRequest request;

private final Object result;

private final Exception exception;

private final HttpAsyncRequestHandler<Object> handler;

private final HttpContext context;

PipelineEntry(HttpRequest request, Object result, Exception exception, HttpAsyncRequestHandler<Object> handler, HttpContext context) {
this.request = request;
this.result = result;
this.exception = exception;
this.handler = handler;
this.context = context;
}

public HttpRequest getRequest() {
return this.request;
}

public Object getResult() {
return this.result;
}

public Exception getException() {
return this.exception;
}

public HttpAsyncRequestHandler<Object> getHandler() {
return this.handler;
}

public HttpContext getContext() {
return this.context;
}
}

static class State
{
private final Queue<HttpAsyncService.PipelineEntry> pipeline = new ConcurrentLinkedQueue<HttpAsyncService.PipelineEntry>(); private volatile boolean terminated;
private volatile MessageState requestState = MessageState.READY;
private volatile MessageState responseState = MessageState.READY;
private volatile HttpAsyncService.Incoming incoming;

public boolean isTerminated() {
return this.terminated;
}
private volatile HttpAsyncService.Outgoing outgoing; private volatile Cancellable cancellable;
public void setTerminated() {
this.terminated = true;
}

public MessageState getRequestState() {
return this.requestState;
}

public void setRequestState(MessageState state) {
this.requestState = state;
}

public MessageState getResponseState() {
return this.responseState;
}

public void setResponseState(MessageState state) {
this.responseState = state;
}

public HttpAsyncService.Incoming getIncoming() {
return this.incoming;
}

public void setIncoming(HttpAsyncService.Incoming incoming) {
this.incoming = incoming;
}

public HttpAsyncService.Outgoing getOutgoing() {
return this.outgoing;
}

public void setOutgoing(HttpAsyncService.Outgoing outgoing) {
this.outgoing = outgoing;
}

public Cancellable getCancellable() {
return this.cancellable;
}

public void setCancellable(Cancellable cancellable) {
this.cancellable = cancellable;
}

public Queue<HttpAsyncService.PipelineEntry> getPipeline() {
return this.pipeline;
}

public String toString() {
StringBuilder buf = new StringBuilder();
buf.append("[incoming ");
buf.append(this.requestState);
if (this.incoming != null) {
buf.append(" ");
buf.append(this.incoming.getRequest().getRequestLine());
} 
buf.append("; outgoing ");
buf.append(this.responseState);
if (this.outgoing != null) {
buf.append(" ");
buf.append(this.outgoing.getResponse().getStatusLine());
} 
buf.append("]");
return buf.toString();
}
}

class HttpAsyncExchangeImpl
implements HttpAsyncExchange
{
private final AtomicBoolean completed = new AtomicBoolean();

private final HttpRequest request;

private final HttpResponse response;

private final HttpAsyncService.State state;

private final NHttpServerConnection conn;

private final HttpContext context;

public HttpAsyncExchangeImpl(HttpRequest request, HttpResponse response, HttpAsyncService.State state, NHttpServerConnection conn, HttpContext context) {
this.request = request;
this.response = response;
this.state = state;
this.conn = conn;
this.context = context;
}

public HttpRequest getRequest() {
return this.request;
}

public HttpResponse getResponse() {
return this.response;
}

public void setCallback(Cancellable cancellable) {
if (this.completed.get()) {
HttpAsyncService.this.handleAlreadySubmittedResponse(cancellable, this.context);
} else if (this.state.isTerminated() && cancellable != null) {
cancellable.cancel();
} else {
this.state.setCancellable(cancellable);
} 
}

public void submitResponse(HttpAsyncResponseProducer responseProducer) {
Args.notNull(responseProducer, "Response producer");
if (this.completed.getAndSet(true)) {
HttpAsyncService.this.handleAlreadySubmittedResponse(responseProducer, this.context);
} else if (!this.state.isTerminated()) {
HttpResponse response = responseProducer.generateResponse();
HttpAsyncService.Outgoing outgoing = new HttpAsyncService.Outgoing(this.request, response, responseProducer, this.context);

synchronized (this.state) {
this.state.setOutgoing(outgoing);
this.state.setCancellable(null);
this.conn.requestOutput();
} 
} else {

try {
responseProducer.close();
} catch (IOException ex) {
HttpAsyncService.this.log(ex);
} 
} 
}

public void submitResponse() {
submitResponse(new BasicAsyncResponseProducer(this.response));
}

public boolean isCompleted() {
return this.completed.get();
}

public void setTimeout(int timeout) {
this.conn.setSocketTimeout(timeout);
}

public int getTimeout() {
return this.conn.getSocketTimeout();
}
}

@Deprecated
private static class HttpAsyncRequestHandlerResolverAdapter
implements HttpAsyncRequestHandlerMapper
{
private final HttpAsyncRequestHandlerResolver resolver;

public HttpAsyncRequestHandlerResolverAdapter(HttpAsyncRequestHandlerResolver resolver) {
this.resolver = resolver;
}

public HttpAsyncRequestHandler<?> lookup(HttpRequest request) {
return this.resolver.lookup(request.getRequestLine().getUri());
}
}
}

