package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.NHttpClientHandler;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.entity.ConsumingNHttpEntity;
import org.apache.http.nio.entity.NHttpEntityWrapper;
import org.apache.http.nio.entity.ProducingNHttpEntity;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class AsyncNHttpClientHandler
extends NHttpHandlerBase
implements NHttpClientHandler
{
protected NHttpRequestExecutionHandler execHandler;

public AsyncNHttpClientHandler(HttpProcessor httpProcessor, NHttpRequestExecutionHandler execHandler, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, HttpParams params) {
super(httpProcessor, connStrategy, allocator, params);
this.execHandler = (NHttpRequestExecutionHandler)Args.notNull(execHandler, "HTTP request execution handler");
}

public AsyncNHttpClientHandler(HttpProcessor httpProcessor, NHttpRequestExecutionHandler execHandler, ConnectionReuseStrategy connStrategy, HttpParams params) {
this(httpProcessor, execHandler, connStrategy, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
}

public void connected(NHttpClientConnection conn, Object attachment) {
HttpContext context = conn.getContext();

initialize(conn, attachment);

ClientConnState connState = new ClientConnState();
context.setAttribute("http.nio.conn-state", connState);

if (this.eventListener != null) {
this.eventListener.connectionOpen((NHttpConnection)conn);
}

requestReady(conn);
}

public void closed(NHttpClientConnection conn) {
HttpContext context = conn.getContext();

ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
try {
connState.reset();
} catch (IOException ex) {
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} 

this.execHandler.finalizeContext(context);

if (this.eventListener != null) {
this.eventListener.connectionClosed((NHttpConnection)conn);
}
}

public void exception(NHttpClientConnection conn, HttpException ex) {
closeConnection((NHttpConnection)conn, (Throwable)ex);
if (this.eventListener != null) {
this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
}
}

public void exception(NHttpClientConnection conn, IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
}

public void requestReady(NHttpClientConnection conn) {
HttpContext context = conn.getContext();

ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
if (connState.getOutputState() != 0) {
return;
}

try {
HttpRequest request = this.execHandler.submitRequest(context);
if (request == null) {
return;
}

request.setParams((HttpParams)new DefaultedHttpParams(request.getParams(), this.params));

context.setAttribute("http.request", request);
this.httpProcessor.process(request, context);

HttpEntityEnclosingRequest entityReq = null;
HttpEntity entity = null;

if (request instanceof HttpEntityEnclosingRequest) {
entityReq = (HttpEntityEnclosingRequest)request;
entity = entityReq.getEntity();
} 

if (entity instanceof ProducingNHttpEntity) {
connState.setProducingEntity((ProducingNHttpEntity)entity);
} else if (entity != null) {
connState.setProducingEntity((ProducingNHttpEntity)new NHttpEntityWrapper(entity));
} 

connState.setRequest(request);
conn.submitRequest(request);
connState.setOutputState(1);

if (entityReq != null && entityReq.expectContinue()) {
int timeout = conn.getSocketTimeout();
connState.setTimeout(timeout);
timeout = this.params.getIntParameter("http.protocol.wait-for-continue", 3000);

conn.setSocketTimeout(timeout);
connState.setOutputState(2);
} else if (connState.getProducingEntity() != null) {
connState.setOutputState(4);
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

public void inputReady(NHttpClientConnection conn, ContentDecoder decoder) {
HttpContext context = conn.getContext();

ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");

ConsumingNHttpEntity consumingEntity = connState.getConsumingEntity();

try {
consumingEntity.consumeContent(decoder, (IOControl)conn);
if (decoder.isCompleted()) {
processResponse(conn, connState);
}
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

public void outputReady(NHttpClientConnection conn, ContentEncoder encoder) {
HttpContext context = conn.getContext();
ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");

try {
if (connState.getOutputState() == 2) {
conn.suspendOutput();

return;
} 
ProducingNHttpEntity entity = connState.getProducingEntity();

entity.produceContent(encoder, (IOControl)conn);
if (encoder.isCompleted()) {
connState.setOutputState(8);
}
} catch (IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} 
}

public void responseReceived(NHttpClientConnection conn) {
HttpContext context = conn.getContext();
ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");

HttpResponse response = conn.getHttpResponse();
response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));

HttpRequest request = connState.getRequest();

try {
int statusCode = response.getStatusLine().getStatusCode();
if (statusCode < 200) {

if (statusCode == 100 && connState.getOutputState() == 2)
{
continueRequest(conn, connState);
}
return;
} 
connState.setResponse(response);
if (connState.getOutputState() == 2) {
cancelRequest(conn, connState);
} else if (connState.getOutputState() == 4) {

cancelRequest(conn, connState);
connState.invalidate();
conn.suspendOutput();
} 

context.setAttribute("http.response", response);

if (!canResponseHaveBody(request, response)) {
conn.resetInput();
response.setEntity(null);
this.httpProcessor.process(response, context);
processResponse(conn, connState);
} else {
HttpEntity entity = response.getEntity();
if (entity != null) {
ConsumingNHttpEntity consumingEntity = this.execHandler.responseEntity(response, context);

if (consumingEntity == null) {
consumingEntity = new NullNHttpEntity(entity);
}
response.setEntity((HttpEntity)consumingEntity);
connState.setConsumingEntity(consumingEntity);
this.httpProcessor.process(response, context);
}

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

public void timeout(NHttpClientConnection conn) {
HttpContext context = conn.getContext();
ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");

try {
if (connState.getOutputState() == 2) {
continueRequest(conn, connState);

return;
} 
} catch (IOException ex) {
shutdownConnection((NHttpConnection)conn, ex);
if (this.eventListener != null) {
this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
}
} 

handleTimeout((NHttpConnection)conn);
}

private void initialize(NHttpClientConnection conn, Object attachment) {
HttpContext context = conn.getContext();

context.setAttribute("http.connection", conn);
this.execHandler.initalizeContext(context, attachment);
}

private void continueRequest(NHttpClientConnection conn, ClientConnState connState) throws IOException {
int timeout = connState.getTimeout();
conn.setSocketTimeout(timeout);

conn.requestOutput();
connState.setOutputState(4);
}

private void cancelRequest(NHttpClientConnection conn, ClientConnState connState) throws IOException {
int timeout = connState.getTimeout();
conn.setSocketTimeout(timeout);

conn.resetOutput();
connState.resetOutput();
}

private void processResponse(NHttpClientConnection conn, ClientConnState connState) throws IOException, HttpException {
if (!connState.isValid()) {
conn.close();
}

HttpContext context = conn.getContext();
HttpResponse response = connState.getResponse();
this.execHandler.handleResponse(response, context);
if (!this.connStrategy.keepAlive(response, context)) {
conn.close();
}

if (conn.isOpen()) {

connState.resetInput();
connState.resetOutput();
conn.requestOutput();
} 
}

protected static class ClientConnState
{
public static final int READY = 0;

public static final int REQUEST_SENT = 1;

public static final int EXPECT_CONTINUE = 2;

public static final int REQUEST_BODY_STREAM = 4;

public static final int REQUEST_BODY_DONE = 8;

public static final int RESPONSE_RECEIVED = 16;

public static final int RESPONSE_BODY_STREAM = 32;
public static final int RESPONSE_BODY_DONE = 64;
private int outputState;
private HttpRequest request;
private HttpResponse response;
private ConsumingNHttpEntity consumingEntity;
private ProducingNHttpEntity producingEntity;
private boolean valid = true;
private int timeout;

public void setConsumingEntity(ConsumingNHttpEntity consumingEntity) {
this.consumingEntity = consumingEntity;
}

public void setProducingEntity(ProducingNHttpEntity producingEntity) {
this.producingEntity = producingEntity;
}

public ProducingNHttpEntity getProducingEntity() {
return this.producingEntity;
}

public ConsumingNHttpEntity getConsumingEntity() {
return this.consumingEntity;
}

public int getOutputState() {
return this.outputState;
}

public void setOutputState(int outputState) {
this.outputState = outputState;
}

public HttpRequest getRequest() {
return this.request;
}

public void setRequest(HttpRequest request) {
this.request = request;
}

public HttpResponse getResponse() {
return this.response;
}

public void setResponse(HttpResponse response) {
this.response = response;
}

public int getTimeout() {
return this.timeout;
}

public void setTimeout(int timeout) {
this.timeout = timeout;
}

public void resetInput() throws IOException {
this.response = null;
if (this.consumingEntity != null) {
this.consumingEntity.finish();
this.consumingEntity = null;
} 
}

public void resetOutput() throws IOException {
this.request = null;
if (this.producingEntity != null) {
this.producingEntity.finish();
this.producingEntity = null;
} 
this.outputState = 0;
}

public void reset() throws IOException {
resetInput();
resetOutput();
}

public boolean isValid() {
return this.valid;
}

public void invalidate() {
this.valid = false;
}
}
}

