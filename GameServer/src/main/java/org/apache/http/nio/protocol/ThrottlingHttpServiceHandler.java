package org.apache.http.nio.protocol;

import org.apache.http.*;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.nio.*;
import org.apache.http.nio.entity.ContentBufferEntity;
import org.apache.http.nio.entity.ContentOutputStream;
import org.apache.http.nio.util.*;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.*;
import org.apache.http.util.Args;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;

@Deprecated
@ThreadSafe
public class ThrottlingHttpServiceHandler
        extends NHttpHandlerBase
        implements NHttpServiceHandler {
    protected final HttpResponseFactory responseFactory;
    protected final Executor executor;
    private final int bufsize;
    protected HttpRequestHandlerResolver handlerResolver;
    protected HttpExpectationVerifier expectationVerifier;

    public ThrottlingHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, Executor executor, HttpParams params) {
        super(httpProcessor, connStrategy, allocator, params);
        Args.notNull(responseFactory, "Response factory");
        Args.notNull(executor, "Executor");
        this.responseFactory = responseFactory;
        this.executor = executor;
        this.bufsize = this.params.getIntParameter("http.nio.content-buffer-size", 20480);
    }

    public ThrottlingHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, Executor executor, HttpParams params) {
        this(httpProcessor, responseFactory, connStrategy, (ByteBufferAllocator) DirectByteBufferAllocator.INSTANCE, executor, params);
    }

    public void setHandlerResolver(HttpRequestHandlerResolver handlerResolver) {
        this.handlerResolver = handlerResolver;
    }

    public void setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
        this.expectationVerifier = expectationVerifier;
    }

    public void connected(NHttpServerConnection conn) {
        HttpContext context = conn.getContext();

        ServerConnState connState = new ServerConnState(this.bufsize, (IOControl) conn, this.allocator);
        context.setAttribute("http.nio.conn-state", connState);

        if (this.eventListener != null) {
            this.eventListener.connectionOpen((NHttpConnection) conn);
        }
    }

    public void closed(NHttpServerConnection conn) {
        HttpContext context = conn.getContext();
        ServerConnState connState = (ServerConnState) context.getAttribute("http.nio.conn-state");

        if (connState != null) {
            synchronized (connState) {
                connState.close();
                connState.notifyAll();
            }
        }

        if (this.eventListener != null) {
            this.eventListener.connectionClosed((NHttpConnection) conn);
        }
    }

    public void exception(NHttpServerConnection conn, HttpException httpex) {
        if (conn.isResponseSubmitted()) {
            if (this.eventListener != null) {
                this.eventListener.fatalProtocolException(httpex, (NHttpConnection) conn);
            }

            return;
        }
        HttpContext context = conn.getContext();

        ServerConnState connState = (ServerConnState) context.getAttribute("http.nio.conn-state");

        try {
            HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion) HttpVersion.HTTP_1_0, 500, context);

            response.setParams((HttpParams) new DefaultedHttpParams(response.getParams(), this.params));

            handleException(httpex, response);
            response.setEntity(null);

            this.httpProcessor.process(response, context);

            synchronized (connState) {
                connState.setResponse(response);

                conn.requestOutput();
            }

        } catch (IOException ex) {
            shutdownConnection((NHttpConnection) conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
            }
        } catch (HttpException ex) {
            closeConnection((NHttpConnection) conn, (Throwable) ex);
            if (this.eventListener != null) {
                this.eventListener.fatalProtocolException(ex, (NHttpConnection) conn);
            }
        }
    }

    public void exception(NHttpServerConnection conn, IOException ex) {
        shutdownConnection((NHttpConnection) conn, ex);

        if (this.eventListener != null) {
            this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
        }
    }

    public void timeout(NHttpServerConnection conn) {
        handleTimeout((NHttpConnection) conn);
    }

    public void requestReceived(final NHttpServerConnection conn) {
        HttpContext context = conn.getContext();

        final HttpRequest request = conn.getHttpRequest();
        final ServerConnState connState = (ServerConnState) context.getAttribute("http.nio.conn-state");

        synchronized (connState) {
            boolean contentExpected = false;
            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                if (entity != null) {
                    contentExpected = true;
                }
            }

            if (!contentExpected) {
                conn.suspendInput();
            }

            this.executor.execute(new Runnable() {
                public void run() {
                    try {
                        ThrottlingHttpServiceHandler.this.handleRequest(request, connState, conn);
                    } catch (IOException ex) {
                        ThrottlingHttpServiceHandler.this.shutdownConnection((NHttpConnection) conn, ex);
                        if (ThrottlingHttpServiceHandler.this.eventListener != null) {
                            ThrottlingHttpServiceHandler.this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
                        }
                    } catch (HttpException ex) {
                        ThrottlingHttpServiceHandler.this.shutdownConnection((NHttpConnection) conn, (Throwable) ex);
                        if (ThrottlingHttpServiceHandler.this.eventListener != null) {
                            ThrottlingHttpServiceHandler.this.eventListener.fatalProtocolException(ex, (NHttpConnection) conn);
                        }
                    }
                }
            });

            connState.notifyAll();
        }
    }

    public void inputReady(NHttpServerConnection conn, ContentDecoder decoder) {
        HttpContext context = conn.getContext();

        ServerConnState connState = (ServerConnState) context.getAttribute("http.nio.conn-state");

        try {
            synchronized (connState) {
                ContentInputBuffer buffer = connState.getInbuffer();

                buffer.consumeContent(decoder);
                if (decoder.isCompleted()) {
                    connState.setInputState(4);
                } else {
                    connState.setInputState(2);
                }

                connState.notifyAll();
            }

        } catch (IOException ex) {
            shutdownConnection((NHttpConnection) conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
            }
        }
    }

    public void responseReady(NHttpServerConnection conn) {
        HttpContext context = conn.getContext();

        ServerConnState connState = (ServerConnState) context.getAttribute("http.nio.conn-state");

        try {
            synchronized (connState) {
                if (connState.isExpectationFailed()) {

                    conn.resetInput();
                    connState.setExpectationFailed(false);
                }

                HttpResponse response = connState.getResponse();
                if (connState.getOutputState() == 0 && response != null && !conn.isResponseSubmitted()) {

                    conn.submitResponse(response);
                    int statusCode = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();

                    if (statusCode >= 200 && entity == null) {
                        connState.setOutputState(32);

                        if (!this.connStrategy.keepAlive(response, context)) {
                            conn.close();
                        }
                    } else {
                        connState.setOutputState(8);
                    }
                }

                connState.notifyAll();
            }

        } catch (IOException ex) {
            shutdownConnection((NHttpConnection) conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
            }
        } catch (HttpException ex) {
            closeConnection((NHttpConnection) conn, (Throwable) ex);
            if (this.eventListener != null) {
                this.eventListener.fatalProtocolException(ex, (NHttpConnection) conn);
            }
        }
    }

    public void outputReady(NHttpServerConnection conn, ContentEncoder encoder) {
        HttpContext context = conn.getContext();

        ServerConnState connState = (ServerConnState) context.getAttribute("http.nio.conn-state");

        try {
            synchronized (connState) {
                HttpResponse response = connState.getResponse();
                ContentOutputBuffer buffer = connState.getOutbuffer();

                buffer.produceContent(encoder);
                if (encoder.isCompleted()) {
                    connState.setOutputState(32);

                    if (!this.connStrategy.keepAlive(response, context)) {
                        conn.close();
                    }
                } else {
                    connState.setOutputState(16);
                }

                connState.notifyAll();
            }

        } catch (IOException ex) {
            shutdownConnection((NHttpConnection) conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
            }
        }
    }

    private void handleException(HttpException ex, HttpResponse response) {
        if (ex instanceof org.apache.http.MethodNotSupportedException) {
            response.setStatusCode(501);
        } else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
            response.setStatusCode(505);
        } else if (ex instanceof org.apache.http.ProtocolException) {
            response.setStatusCode(400);
        } else {
            response.setStatusCode(500);
        }
        byte[] msg = EncodingUtils.getAsciiBytes(ex.getMessage());
        ByteArrayEntity entity = new ByteArrayEntity(msg);
        entity.setContentType("text/plain; charset=US-ASCII");
        response.setEntity((HttpEntity) entity);
    }

    private void handleRequest(HttpRequest request, ServerConnState connState, NHttpServerConnection conn) throws HttpException, IOException {
        HttpVersion httpVersion;
        HttpContext context = conn.getContext();

        synchronized (connState) {
            while (true) {
                try {
                    int currentState = connState.getOutputState();
                    if (currentState == 0) {
                        break;
                    }
                    if (currentState == -1) {
                        return;
                    }
                    connState.wait();
                } catch (InterruptedException ex) {
                    connState.shutdown();
                    return;
                }
            }
            connState.setInputState(1);
            connState.setRequest(request);
        }

        request.setParams((HttpParams) new DefaultedHttpParams(request.getParams(), this.params));

        context.setAttribute("http.connection", conn);
        context.setAttribute("http.request", request);

        ProtocolVersion ver = request.getRequestLine().getProtocolVersion();

        if (!ver.lessEquals((ProtocolVersion) HttpVersion.HTTP_1_1)) {
            httpVersion = HttpVersion.HTTP_1_1;
        }

        HttpResponse response = null;

        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest eeRequest = (HttpEntityEnclosingRequest) request;

            if (eeRequest.expectContinue()) {
                response = this.responseFactory.newHttpResponse((ProtocolVersion) httpVersion, 100, context);

                response.setParams((HttpParams) new DefaultedHttpParams(response.getParams(), this.params));

                if (this.expectationVerifier != null) {
                    try {
                        this.expectationVerifier.verify(request, response, context);
                    } catch (HttpException ex) {
                        response = this.responseFactory.newHttpResponse((ProtocolVersion) HttpVersion.HTTP_1_0, 500, context);

                        response.setParams((HttpParams) new DefaultedHttpParams(response.getParams(), this.params));

                        handleException(ex, response);
                    }
                }

                synchronized (connState) {
                    if (response.getStatusLine().getStatusCode() < 200) {

                        connState.setResponse(response);
                        conn.requestOutput();

                        try {
                            while (true) {
                                int currentState = connState.getOutputState();
                                if (currentState == 8) {
                                    break;
                                }
                                if (currentState == -1) {
                                    return;
                                }
                                connState.wait();
                            }
                        } catch (InterruptedException ex) {
                            connState.shutdown();
                            return;
                        }
                        connState.resetOutput();
                        response = null;
                    } else {

                        eeRequest.setEntity(null);
                        conn.suspendInput();
                        connState.setExpectationFailed(true);
                    }
                }
            }

            if (eeRequest.getEntity() != null) {
                eeRequest.setEntity((HttpEntity) new ContentBufferEntity(eeRequest.getEntity(), connState.getInbuffer()));
            }
        }

        if (response == null) {
            response = this.responseFactory.newHttpResponse((ProtocolVersion) httpVersion, 200, context);

            response.setParams((HttpParams) new DefaultedHttpParams(response.getParams(), this.params));

            context.setAttribute("http.response", response);

            try {
                this.httpProcessor.process(request, context);

                HttpRequestHandler handler = null;
                if (this.handlerResolver != null) {
                    String requestURI = request.getRequestLine().getUri();
                    handler = this.handlerResolver.lookup(requestURI);
                }
                if (handler != null) {
                    handler.handle(request, response, context);
                } else {
                    response.setStatusCode(501);
                }

            } catch (HttpException ex) {
                response = this.responseFactory.newHttpResponse((ProtocolVersion) HttpVersion.HTTP_1_0, 500, context);

                response.setParams((HttpParams) new DefaultedHttpParams(response.getParams(), this.params));

                handleException(ex, response);
            }
        }

        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest eeRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity entity = eeRequest.getEntity();
            EntityUtils.consume(entity);
        }

        connState.resetInput();

        this.httpProcessor.process(response, context);

        if (!canResponseHaveBody(request, response)) {
            response.setEntity(null);
        }

        connState.setResponse(response);

        conn.requestOutput();

        if (response.getEntity() != null) {
            ContentOutputBuffer buffer = connState.getOutbuffer();
            ContentOutputStream contentOutputStream = new ContentOutputStream(buffer);

            HttpEntity entity = response.getEntity();
            entity.writeTo((OutputStream) contentOutputStream);
            contentOutputStream.flush();
            contentOutputStream.close();
        }

        synchronized (connState) {
            while (true) {
                try {
                    int currentState = connState.getOutputState();
                    if (currentState == 32) {
                        break;
                    }
                    if (currentState == -1) {
                        return;
                    }
                    connState.wait();
                } catch (InterruptedException ex) {
                    connState.shutdown();
                    return;
                }
            }
            connState.resetOutput();
            conn.requestInput();
            connState.notifyAll();
        }
    }

    protected void shutdownConnection(NHttpConnection conn, Throwable cause) {
        HttpContext context = conn.getContext();

        ServerConnState connState = (ServerConnState) context.getAttribute("http.nio.conn-state");

        super.shutdownConnection(conn, cause);

        if (connState != null) {
            connState.shutdown();
        }
    }

    static class ServerConnState {
        public static final int SHUTDOWN = -1;

        public static final int READY = 0;

        public static final int REQUEST_RECEIVED = 1;

        public static final int REQUEST_BODY_STREAM = 2;

        public static final int REQUEST_BODY_DONE = 4;

        public static final int RESPONSE_SENT = 8;

        public static final int RESPONSE_BODY_STREAM = 16;

        public static final int RESPONSE_BODY_DONE = 32;
        public static final int RESPONSE_DONE = 32;
        private final SharedInputBuffer inbuffer;
        private final SharedOutputBuffer outbuffer;
        private volatile int inputState;
        private volatile int outputState;
        private volatile HttpRequest request;
        private volatile HttpResponse response;
        private volatile boolean expectationFailure;

        public ServerConnState(int bufsize, IOControl ioControl, ByteBufferAllocator allocator) {
            this.inbuffer = new SharedInputBuffer(bufsize, ioControl, allocator);
            this.outbuffer = new SharedOutputBuffer(bufsize, ioControl, allocator);
            this.inputState = 0;
            this.outputState = 0;
        }

        public ContentInputBuffer getInbuffer() {
            return (ContentInputBuffer) this.inbuffer;
        }

        public ContentOutputBuffer getOutbuffer() {
            return (ContentOutputBuffer) this.outbuffer;
        }

        public int getInputState() {
            return this.inputState;
        }

        public void setInputState(int inputState) {
            this.inputState = inputState;
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

        public boolean isExpectationFailed() {
            return this.expectationFailure;
        }

        public void setExpectationFailed(boolean b) {
            this.expectationFailure = b;
        }

        public void close() {
            this.inbuffer.close();
            this.outbuffer.close();
            this.inputState = -1;
            this.outputState = -1;
        }

        public void shutdown() {
            this.inbuffer.shutdown();
            this.outbuffer.shutdown();
            this.inputState = -1;
            this.outputState = -1;
        }

        public void resetInput() {
            this.inbuffer.reset();
            this.request = null;
            this.inputState = 0;
        }

        public void resetOutput() {
            this.outbuffer.reset();
            this.response = null;
            this.outputState = 0;
            this.expectationFailure = false;
        }
    }
}

