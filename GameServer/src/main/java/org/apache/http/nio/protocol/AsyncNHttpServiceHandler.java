package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.NHttpServiceHandler;
import org.apache.http.nio.entity.ConsumingNHttpEntity;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.nio.entity.NHttpEntityWrapper;
import org.apache.http.nio.entity.ProducingNHttpEntity;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.EncodingUtils;

@Deprecated
@Immutable
public class AsyncNHttpServiceHandler
extends NHttpHandlerBase
implements NHttpServiceHandler
{
protected final HttpResponseFactory responseFactory;
protected NHttpRequestHandlerResolver handlerResolver;
protected HttpExpectationVerifier expectationVerifier;

public AsyncNHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, HttpParams params) {
super(httpProcessor, connStrategy, allocator, params);
Args.notNull(responseFactory, "Response factory");
this.responseFactory = responseFactory;
}

public AsyncNHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, HttpParams params) {
this(httpProcessor, responseFactory, connStrategy, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
}

public void setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
this.expectationVerifier = expectationVerifier;
}

public void setHandlerResolver(NHttpRequestHandlerResolver handlerResolver) {
this.handlerResolver = handlerResolver;
}

public void connected(NHttpServerConnection conn) {
HttpContext context = conn.getContext();

ServerConnState connState = new ServerConnState();
context.setAttribute("http.nio.conn-state", connState);
context.setAttribute("http.connection", conn);

if (this.eventListener != null)
this.eventListener.connectionOpen((NHttpConnection)conn); 
}

public void requestReceived(NHttpServerConnection conn) {
HttpVersion httpVersion;
HttpContext context = conn.getContext();

ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");

HttpRequest request = conn.getHttpRequest();
request.setParams((HttpParams)new DefaultedHttpParams(request.getParams(), this.params));

connState.setRequest(request);

NHttpRequestHandler requestHandler = getRequestHandler(request);
connState.setRequestHandler(requestHandler);

ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
if (!ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_1))
{
httpVersion = HttpVersion.HTTP_1_1;
}

try {
if (request instanceof HttpEntityEnclosingRequest) {
HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest)request;
if (entityRequest.expectContinue()) {
HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)httpVersion, 100, context);

response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));

if (this.expectationVerifier != null) {
try {
this.expectationVerifier.verify(request, response, context);
} catch (HttpException ex) {
response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);

response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));

handleException(ex, response);
} 
}

if (response.getStatusLine().getStatusCode() < 200) {

conn.submitResponse(response);
} else {
conn.resetInput();
sendResponse(conn, request, response);
} 
} 

ConsumingNHttpEntity consumingEntity = null;

if (requestHandler != null) {
consumingEntity = requestHandler.entityRequest(entityRequest, context);
}
if (consumingEntity == null) {
consumingEntity = new NullNHttpEntity(entityRequest.getEntity());
}
entityRequest.setEntity((HttpEntity)consumingEntity);
connState.setConsumingEntity(consumingEntity);

}
else {

conn.suspendInput();
processRequest(conn, request);
}

} catch (IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} catch (HttpException ex) {
closeConnection((NHttpConnection)conn, (Throwable)ex);
if (this.eventListener != null) {
this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
}
} 
}

public void closed(NHttpServerConnection conn) {
HttpContext context = conn.getContext();

ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
try {
connState.reset();
} catch (IOException ex) {
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} 
if (this.eventListener != null) {
this.eventListener.connectionClosed((NHttpConnection)conn);
}
}

public void exception(NHttpServerConnection conn, HttpException httpex) {
if (conn.isResponseSubmitted()) {

closeConnection((NHttpConnection)conn, (Throwable)httpex);
if (this.eventListener != null) {
this.eventListener.fatalProtocolException(httpex, (NHttpConnection)conn);
}

return;
} 
HttpContext context = conn.getContext();
try {
HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);

response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));

handleException(httpex, response);
response.setEntity(null);
sendResponse(conn, (HttpRequest)null, response);
}
catch (IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} catch (HttpException ex) {
closeConnection((NHttpConnection)conn, (Throwable)ex);
if (this.eventListener != null) {
this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
}
} 
}

public void exception(NHttpServerConnection conn, IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);

if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
}

public void timeout(NHttpServerConnection conn) {
handleTimeout((NHttpConnection)conn);
}

public void inputReady(NHttpServerConnection conn, ContentDecoder decoder) {
HttpContext context = conn.getContext();
ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");

HttpRequest request = connState.getRequest();
ConsumingNHttpEntity consumingEntity = connState.getConsumingEntity();

try {
consumingEntity.consumeContent(decoder, (IOControl)conn);
if (decoder.isCompleted()) {
conn.suspendInput();
processRequest(conn, request);
}

} catch (IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} catch (HttpException ex) {
closeConnection((NHttpConnection)conn, (Throwable)ex);
if (this.eventListener != null) {
this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
}
} 
}

public void responseReady(NHttpServerConnection conn) {
HttpContext context = conn.getContext();
ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");

if (connState.isHandled()) {
return;
}

HttpRequest request = connState.getRequest();

try {
IOException ioex = connState.getIOException();
if (ioex != null) {
throw ioex;
}

HttpException httpex = connState.getHttpException();
if (httpex != null) {
HttpResponse httpResponse = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);

httpResponse.setParams((HttpParams)new DefaultedHttpParams(httpResponse.getParams(), this.params));

handleException(httpex, httpResponse);
connState.setResponse(httpResponse);
} 

HttpResponse response = connState.getResponse();
if (response != null) {
connState.setHandled(true);
sendResponse(conn, request, response);
}

} catch (IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} catch (HttpException ex) {
closeConnection((NHttpConnection)conn, (Throwable)ex);
if (this.eventListener != null) {
this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
}
} 
}

public void outputReady(NHttpServerConnection conn, ContentEncoder encoder) {
HttpContext context = conn.getContext();
ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");

HttpResponse response = conn.getHttpResponse();

try {
ProducingNHttpEntity entity = connState.getProducingEntity();
entity.produceContent(encoder, (IOControl)conn);

if (encoder.isCompleted()) {
connState.finishOutput();
if (!this.connStrategy.keepAlive(response, context)) {
conn.close();
} else {

connState.reset();
conn.requestInput();
} 
responseComplete(response, context);
}

} catch (IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} 
}

private void handleException(HttpException ex, HttpResponse response) {
int code = 500;
if (ex instanceof org.apache.http.MethodNotSupportedException) {
code = 501;
} else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
code = 505;
} else if (ex instanceof org.apache.http.ProtocolException) {
code = 400;
} 
response.setStatusCode(code);

byte[] msg = EncodingUtils.getAsciiBytes(ex.getMessage());
NByteArrayEntity entity = new NByteArrayEntity(msg);
entity.setContentType("text/plain; charset=US-ASCII");
response.setEntity((HttpEntity)entity);
}

private void processRequest(NHttpServerConnection conn, HttpRequest request) throws IOException, HttpException {
HttpVersion httpVersion;
HttpContext context = conn.getContext();
ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");

ProtocolVersion ver = request.getRequestLine().getProtocolVersion();

if (!ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_1))
{
httpVersion = HttpVersion.HTTP_1_1;
}

NHttpResponseTrigger trigger = new ResponseTriggerImpl(connState, (IOControl)conn);
try {
this.httpProcessor.process(request, context);

NHttpRequestHandler handler = connState.getRequestHandler();
if (handler != null) {
HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)httpVersion, 200, context);

response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));

handler.handle(request, response, trigger, context);

}
else {

HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)httpVersion, 501, context);

response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));

trigger.submitResponse(response);
}

} catch (HttpException ex) {
trigger.handleException(ex);
} 
}

private void sendResponse(NHttpServerConnection conn, HttpRequest request, HttpResponse response) throws IOException, HttpException {
HttpContext context = conn.getContext();
ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");

connState.finishInput();

context.setAttribute("http.request", request);
this.httpProcessor.process(response, context);
context.setAttribute("http.request", null);

if (response.getEntity() != null && !canResponseHaveBody(request, response)) {
response.setEntity(null);
}

HttpEntity entity = response.getEntity();
if (entity != null) {
if (entity instanceof ProducingNHttpEntity) {
connState.setProducingEntity((ProducingNHttpEntity)entity);
} else {
connState.setProducingEntity((ProducingNHttpEntity)new NHttpEntityWrapper(entity));
} 
}

conn.submitResponse(response);

if (entity == null) {
if (!this.connStrategy.keepAlive(response, context)) {
conn.close();
} else {

connState.reset();
conn.requestInput();
} 
responseComplete(response, context);
} 
}

protected void responseComplete(HttpResponse response, HttpContext context) {}

private NHttpRequestHandler getRequestHandler(HttpRequest request) {
NHttpRequestHandler handler = null;
if (this.handlerResolver != null) {
String requestURI = request.getRequestLine().getUri();
handler = this.handlerResolver.lookup(requestURI);
} 

return handler;
}

protected static class ServerConnState
{
private volatile NHttpRequestHandler requestHandler;
private volatile HttpRequest request;
private volatile ConsumingNHttpEntity consumingEntity;
private volatile HttpResponse response;
private volatile ProducingNHttpEntity producingEntity;
private volatile IOException ioex;
private volatile HttpException httpex;
private volatile boolean handled;

public void finishInput() throws IOException {
if (this.consumingEntity != null) {
this.consumingEntity.finish();
this.consumingEntity = null;
} 
}

public void finishOutput() throws IOException {
if (this.producingEntity != null) {
this.producingEntity.finish();
this.producingEntity = null;
} 
}

public void reset() throws IOException {
finishInput();
this.request = null;
finishOutput();
this.handled = false;
this.response = null;
this.ioex = null;
this.httpex = null;
this.requestHandler = null;
}

public NHttpRequestHandler getRequestHandler() {
return this.requestHandler;
}

public void setRequestHandler(NHttpRequestHandler requestHandler) {
this.requestHandler = requestHandler;
}

public HttpRequest getRequest() {
return this.request;
}

public void setRequest(HttpRequest request) {
this.request = request;
}

public ConsumingNHttpEntity getConsumingEntity() {
return this.consumingEntity;
}

public void setConsumingEntity(ConsumingNHttpEntity consumingEntity) {
this.consumingEntity = consumingEntity;
}

public HttpResponse getResponse() {
return this.response;
}

public void setResponse(HttpResponse response) {
this.response = response;
}

public ProducingNHttpEntity getProducingEntity() {
return this.producingEntity;
}

public void setProducingEntity(ProducingNHttpEntity producingEntity) {
this.producingEntity = producingEntity;
}

public IOException getIOException() {
return this.ioex;
}

public IOException getIOExepction() {
return this.ioex;
}

public void setIOException(IOException ex) {
this.ioex = ex;
}

public void setIOExepction(IOException ex) {
this.ioex = ex;
}

public HttpException getHttpException() {
return this.httpex;
}

public HttpException getHttpExepction() {
return this.httpex;
}

public void setHttpException(HttpException ex) {
this.httpex = ex;
}

public void setHttpExepction(HttpException ex) {
this.httpex = ex;
}

public boolean isHandled() {
return this.handled;
}

public void setHandled(boolean handled) {
this.handled = handled;
}
}

private static class ResponseTriggerImpl
implements NHttpResponseTrigger
{
private final AsyncNHttpServiceHandler.ServerConnState connState;

private final IOControl iocontrol;
private volatile boolean triggered;

public ResponseTriggerImpl(AsyncNHttpServiceHandler.ServerConnState connState, IOControl iocontrol) {
this.connState = connState;
this.iocontrol = iocontrol;
}

public void submitResponse(HttpResponse response) {
Args.notNull(response, "Response");
Asserts.check(!this.triggered, "Response already triggered");
this.triggered = true;
this.connState.setResponse(response);
this.iocontrol.requestOutput();
}

public void handleException(HttpException ex) {
Asserts.check(!this.triggered, "Response already triggered");
this.triggered = true;
this.connState.setHttpException(ex);
this.iocontrol.requestOutput();
}

public void handleException(IOException ex) {
Asserts.check(!this.triggered, "Response already triggered");
this.triggered = true;
this.connState.setIOException(ex);
this.iocontrol.requestOutput();
}
}
}

