package org.apache.http.nio.protocol;

import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.http.ConnectionClosedException;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.NHttpClientEventHandler;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Immutable
public class HttpAsyncRequestExecutor
implements NHttpClientEventHandler
{
public static final int DEFAULT_WAIT_FOR_CONTINUE = 3000;
public static final String HTTP_HANDLER = "http.nio.exchange-handler";
private final int waitForContinue;
private final ExceptionLogger exceptionLogger;
static final String HTTP_EXCHANGE_STATE = "http.nio.http-exchange-state";

public HttpAsyncRequestExecutor(int waitForContinue, ExceptionLogger exceptionLogger) {
this.waitForContinue = Args.positive(waitForContinue, "Wait for continue time");
this.exceptionLogger = (exceptionLogger != null) ? exceptionLogger : ExceptionLogger.NO_OP;
}

public HttpAsyncRequestExecutor(int waitForContinue) {
this(waitForContinue, null);
}

public HttpAsyncRequestExecutor() {
this(3000, null);
}

public void connected(NHttpClientConnection conn, Object attachment) throws IOException, HttpException {
State state = new State();
HttpContext context = conn.getContext();
context.setAttribute("http.nio.http-exchange-state", state);
requestReady(conn);
}

public void closed(NHttpClientConnection conn) {
State state = getState((NHttpConnection)conn);
HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
if (state != null && (
state.getRequestState() != MessageState.READY || state.getResponseState() != MessageState.READY) && 
handler != null) {
handler.failed((Exception)new ConnectionClosedException("Connection closed unexpectedly"));
}

if (state == null || (handler != null && handler.isDone())) {
closeHandler(handler);
}
}

public void exception(NHttpClientConnection conn, Exception cause) {
shutdownConnection((NHttpConnection)conn);
HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
if (handler != null) {
handler.failed(cause);
} else {
log(cause);
} 
}

public void requestReady(NHttpClientConnection conn) throws IOException, HttpException {
State state = getState((NHttpConnection)conn);
Asserts.notNull(state, "Connection state");
Asserts.check((state.getRequestState() == MessageState.READY || state.getRequestState() == MessageState.COMPLETED), "Unexpected request state %s", state.getRequestState());

if (state.getRequestState() != MessageState.READY) {
return;
}
HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
if (handler == null || handler.isDone()) {
return;
}
boolean pipelined = (handler.getClass().getAnnotation(Pipelined.class) != null);

HttpRequest request = handler.generateRequest();
if (request == null) {
return;
}
ProtocolVersion version = request.getRequestLine().getProtocolVersion();
if (pipelined && version.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
throw new ProtocolException(version + " cannot be used with request pipelining");
}
state.setRequest(request);

if (request instanceof HttpEntityEnclosingRequest) {
boolean expectContinue = ((HttpEntityEnclosingRequest)request).expectContinue();
if (expectContinue && pipelined) {
throw new ProtocolException("Expect-continue handshake cannot be used with request pipelining");
}
conn.submitRequest(request);
if (expectContinue) {
int timeout = conn.getSocketTimeout();
state.setTimeout(timeout);
conn.setSocketTimeout(this.waitForContinue);
state.setRequestState(MessageState.ACK_EXPECTED);
} else {
HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
if (entity != null) {
state.setRequestState(MessageState.BODY_STREAM);
} else {
handler.requestCompleted();
state.setRequestState(pipelined ? MessageState.READY : MessageState.COMPLETED);
} 
} 
} else {
conn.submitRequest(request);
handler.requestCompleted();
state.setRequestState(pipelined ? MessageState.READY : MessageState.COMPLETED);
} 
}

public void outputReady(NHttpClientConnection conn, ContentEncoder encoder) throws IOException, HttpException {
State state = getState((NHttpConnection)conn);
Asserts.notNull(state, "Connection state");
Asserts.check((state.getRequestState() == MessageState.BODY_STREAM || state.getRequestState() == MessageState.ACK_EXPECTED), "Unexpected request state %s", state.getRequestState());

HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
Asserts.notNull(handler, "Client exchange handler");
if (state.getRequestState() == MessageState.ACK_EXPECTED) {
conn.suspendOutput();
return;
} 
handler.produceContent(encoder, (IOControl)conn);
if (encoder.isCompleted()) {
handler.requestCompleted();
boolean pipelined = (handler.getClass().getAnnotation(Pipelined.class) != null);
state.setRequestState(pipelined ? MessageState.READY : MessageState.COMPLETED);
} 
}

public void responseReceived(NHttpClientConnection conn) throws HttpException, IOException {
State state = getState((NHttpConnection)conn);
Asserts.notNull(state, "Connection state");
Asserts.check((state.getResponseState() == MessageState.READY), "Unexpected request state %s", state.getResponseState());

HttpRequest request = state.getRequest();
if (request == null) {
throw new HttpException("Out of sequence response");
}
HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
Asserts.notNull(handler, "Client exchange handler");
HttpResponse response = conn.getHttpResponse();

int statusCode = response.getStatusLine().getStatusCode();
if (statusCode < 200) {

if (statusCode != 100) {
throw new ProtocolException("Unexpected response: " + response.getStatusLine());
}

if (state.getRequestState() == MessageState.ACK_EXPECTED) {
int timeout = state.getTimeout();
conn.setSocketTimeout(timeout);
conn.requestOutput();
state.setRequestState(MessageState.BODY_STREAM);
} 
return;
} 
state.setResponse(response);
if (state.getRequestState() == MessageState.ACK_EXPECTED) {
int timeout = state.getTimeout();
conn.setSocketTimeout(timeout);
conn.resetOutput();
state.setRequestState(MessageState.COMPLETED);
} else if (state.getRequestState() == MessageState.BODY_STREAM) {

conn.resetOutput();
conn.suspendOutput();
state.setRequestState(MessageState.COMPLETED);
state.invalidate();
} 

handler.responseReceived(response);

state.setResponseState(MessageState.BODY_STREAM);
if (!canResponseHaveBody(request, response)) {
response.setEntity(null);
conn.resetInput();
processResponse(conn, state, handler);
} 
}

public void inputReady(NHttpClientConnection conn, ContentDecoder decoder) throws IOException, HttpException {
State state = getState((NHttpConnection)conn);
Asserts.notNull(state, "Connection state");
Asserts.check((state.getResponseState() == MessageState.BODY_STREAM), "Unexpected request state %s", state.getResponseState());

HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
Asserts.notNull(handler, "Client exchange handler");
handler.consumeContent(decoder, (IOControl)conn);
if (decoder.isCompleted()) {
processResponse(conn, state, handler);
}
}

public void endOfInput(NHttpClientConnection conn) throws IOException {
State state = getState((NHttpConnection)conn);
if (state != null) {
if (state.getRequestState().compareTo(MessageState.READY) != 0) {
state.invalidate();
}
HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
if (handler != null) {
if (state.isValid()) {
handler.inputTerminated();
} else {
handler.failed((Exception)new ConnectionClosedException("Connection closed"));
} 
}
} 

if (conn.getSocketTimeout() <= 0) {
conn.setSocketTimeout(1000);
}
conn.close();
}

public void timeout(NHttpClientConnection conn) throws IOException {
State state = getState((NHttpConnection)conn);
if (state != null) {
if (state.getRequestState() == MessageState.ACK_EXPECTED) {
int timeout = state.getTimeout();
conn.setSocketTimeout(timeout);
conn.requestOutput();
state.setRequestState(MessageState.BODY_STREAM);
state.setTimeout(0);
return;
} 
state.invalidate();
HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
if (handler != null) {
handler.failed(new SocketTimeoutException());
handler.close();
} 
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

protected void log(Exception ex) {
this.exceptionLogger.log(ex);
}

private State getState(NHttpConnection conn) {
return (State)conn.getContext().getAttribute("http.nio.http-exchange-state");
}

private HttpAsyncClientExchangeHandler getHandler(NHttpConnection conn) {
return (HttpAsyncClientExchangeHandler)conn.getContext().getAttribute("http.nio.exchange-handler");
}

private void shutdownConnection(NHttpConnection conn) {
try {
conn.shutdown();
} catch (IOException ex) {
log(ex);
} 
}

private void closeHandler(HttpAsyncClientExchangeHandler handler) {
if (handler != null) {
try {
handler.close();
} catch (IOException ioex) {
log(ioex);
} 
}
}

private void processResponse(NHttpClientConnection conn, State state, HttpAsyncClientExchangeHandler handler) throws IOException, HttpException {
if (!state.isValid()) {
conn.close();
}
handler.responseCompleted();

boolean pipelined = (handler.getClass().getAnnotation(Pipelined.class) != null);
if (!pipelined) {
state.setRequestState(MessageState.READY);
state.setRequest(null);
} 
state.setResponseState(MessageState.READY);
state.setResponse(null);
if (!handler.isDone() && conn.isOpen()) {
conn.requestOutput();
}
}

private boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
String method = request.getRequestLine().getMethod();
int status = response.getStatusLine().getStatusCode();

if (method.equalsIgnoreCase("HEAD")) {
return false;
}
if (method.equalsIgnoreCase("CONNECT") && status < 300) {
return false;
}
return (status >= 200 && status != 204 && status != 304 && status != 205);
}

static class State
{
private volatile boolean valid = true;

private volatile MessageState requestState = MessageState.READY;
private volatile MessageState responseState = MessageState.READY;
private volatile HttpRequest request;

public MessageState getRequestState() {
return this.requestState;
}
private volatile HttpResponse response; private volatile int timeout;
public void setRequestState(MessageState state) {
this.requestState = state;
}

public MessageState getResponseState() {
return this.responseState;
}

public void setResponseState(MessageState state) {
this.responseState = state;
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

public boolean isValid() {
return this.valid;
}

public void invalidate() {
this.valid = false;
}

public String toString() {
StringBuilder buf = new StringBuilder();
buf.append("request state: ");
buf.append(this.requestState);
buf.append("; request: ");
if (this.request != null) {
buf.append(this.request.getRequestLine());
}
buf.append("; response state: ");
buf.append(this.responseState);
buf.append("; response: ");
if (this.response != null) {
buf.append(this.response.getStatusLine());
}
buf.append("; valid: ");
buf.append(this.valid);
buf.append(";");
return buf.toString();
}
}
}

