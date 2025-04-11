package org.apache.http.nio.protocol;

import org.apache.http.*;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.*;
import org.apache.http.nio.entity.ContentBufferEntity;
import org.apache.http.nio.entity.ContentOutputStream;
import org.apache.http.nio.util.*;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;

@Deprecated
@ThreadSafe
public class ThrottlingHttpClientHandler
        extends NHttpHandlerBase
        implements NHttpClientHandler {
    protected final Executor executor;
    private final int bufsize;
    protected HttpRequestExecutionHandler execHandler;

    public ThrottlingHttpClientHandler(HttpProcessor httpProcessor, HttpRequestExecutionHandler execHandler, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, Executor executor, HttpParams params) {
        super(httpProcessor, connStrategy, allocator, params);
        Args.notNull(execHandler, "HTTP request execution handler");
        Args.notNull(executor, "Executor");
        this.execHandler = execHandler;
        this.executor = executor;
        this.bufsize = this.params.getIntParameter("http.nio.content-buffer-size", 20480);
    }

    public ThrottlingHttpClientHandler(HttpProcessor httpProcessor, HttpRequestExecutionHandler execHandler, ConnectionReuseStrategy connStrategy, Executor executor, HttpParams params) {
        this(httpProcessor, execHandler, connStrategy, (ByteBufferAllocator) DirectByteBufferAllocator.INSTANCE, executor, params);
    }

    public void connected(NHttpClientConnection conn, Object attachment) {
        HttpContext context = conn.getContext();

        initialize(conn, attachment);

        ClientConnState connState = new ClientConnState(this.bufsize, (IOControl) conn, this.allocator);
        context.setAttribute("http.nio.conn-state", connState);

        if (this.eventListener != null) {
            this.eventListener.connectionOpen((NHttpConnection) conn);
        }

        requestReady(conn);
    }

    public void closed(NHttpClientConnection conn) {
        HttpContext context = conn.getContext();
        ClientConnState connState = (ClientConnState) context.getAttribute("http.nio.conn-state");

        if (connState != null) {
            synchronized (connState) {
                connState.close();
                connState.notifyAll();
            }
        }
        this.execHandler.finalizeContext(context);

        if (this.eventListener != null) {
            this.eventListener.connectionClosed((NHttpConnection) conn);
        }
    }

    public void exception(NHttpClientConnection conn, HttpException ex) {
        closeConnection((NHttpConnection) conn, (Throwable) ex);
        if (this.eventListener != null) {
            this.eventListener.fatalProtocolException(ex, (NHttpConnection) conn);
        }
    }

    public void exception(NHttpClientConnection conn, IOException ex) {
        shutdownConnection((NHttpConnection) conn, ex);
        if (this.eventListener != null) {
            this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
        }
    }

    public void requestReady(NHttpClientConnection conn) {
        HttpContext context = conn.getContext();

        ClientConnState connState = (ClientConnState) context.getAttribute("http.nio.conn-state");

        try {
            synchronized (connState) {
                if (connState.getOutputState() != 0) {
                    return;
                }

                HttpRequest request = this.execHandler.submitRequest(context);
                if (request == null) {
                    return;
                }

                request.setParams((HttpParams) new DefaultedHttpParams(request.getParams(), this.params));

                context.setAttribute("http.request", request);
                this.httpProcessor.process(request, context);
                connState.setRequest(request);
                conn.submitRequest(request);
                connState.setOutputState(1);

                conn.requestInput();

                if (request instanceof HttpEntityEnclosingRequest) {
                    if (((HttpEntityEnclosingRequest) request).expectContinue()) {
                        int timeout = conn.getSocketTimeout();
                        connState.setTimeout(timeout);
                        timeout = this.params.getIntParameter("http.protocol.wait-for-continue", 3000);

                        conn.setSocketTimeout(timeout);
                        connState.setOutputState(2);
                    } else {
                        sendRequestBody((HttpEntityEnclosingRequest) request, connState, conn);
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

    public void outputReady(NHttpClientConnection conn, ContentEncoder encoder) {
        HttpContext context = conn.getContext();

        ClientConnState connState = (ClientConnState) context.getAttribute("http.nio.conn-state");

        try {
            synchronized (connState) {
                if (connState.getOutputState() == 2) {
                    conn.suspendOutput();
                    return;
                }
                ContentOutputBuffer buffer = connState.getOutbuffer();
                buffer.produceContent(encoder);
                if (encoder.isCompleted()) {
                    connState.setInputState(8);
                } else {
                    connState.setInputState(4);
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

    public void responseReceived(NHttpClientConnection conn) {
        HttpContext context = conn.getContext();
        ClientConnState connState = (ClientConnState) context.getAttribute("http.nio.conn-state");

        try {
            synchronized (connState) {
                HttpResponse response = conn.getHttpResponse();
                response.setParams((HttpParams) new DefaultedHttpParams(response.getParams(), this.params));

                HttpRequest request = connState.getRequest();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode < 200) {

                    if (statusCode == 100 && connState.getOutputState() == 2) {

                        connState.setOutputState(1);
                        continueRequest(conn, connState);
                    }
                    return;
                }
                connState.setResponse(response);
                connState.setInputState(16);

                if (connState.getOutputState() == 2) {
                    int timeout = connState.getTimeout();
                    conn.setSocketTimeout(timeout);
                    conn.resetOutput();
                }

                if (!canResponseHaveBody(request, response)) {
                    conn.resetInput();
                    response.setEntity(null);
                    connState.setInputState(64);

                    if (!this.connStrategy.keepAlive(response, context)) {
                        conn.close();
                    }
                }

                if (response.getEntity() != null) {
                    response.setEntity((HttpEntity) new ContentBufferEntity(response.getEntity(), connState.getInbuffer()));
                }

                context.setAttribute("http.response", response);

                this.httpProcessor.process(response, context);

                handleResponse(response, connState, conn);

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

    public void inputReady(NHttpClientConnection conn, ContentDecoder decoder) {
        HttpContext context = conn.getContext();

        ClientConnState connState = (ClientConnState) context.getAttribute("http.nio.conn-state");

        try {
            synchronized (connState) {
                HttpResponse response = connState.getResponse();
                ContentInputBuffer buffer = connState.getInbuffer();

                buffer.consumeContent(decoder);
                if (decoder.isCompleted()) {
                    connState.setInputState(64);

                    if (!this.connStrategy.keepAlive(response, context)) {
                        conn.close();
                    }
                } else {
                    connState.setInputState(32);
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

    public void timeout(NHttpClientConnection conn) {
        HttpContext context = conn.getContext();
        ClientConnState connState = (ClientConnState) context.getAttribute("http.nio.conn-state");

        try {
            synchronized (connState) {
                if (connState.getOutputState() == 2) {
                    connState.setOutputState(1);
                    continueRequest(conn, connState);

                    connState.notifyAll();

                    return;
                }
            }
        } catch (IOException ex) {
            shutdownConnection((NHttpConnection) conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
            }
        }

        handleTimeout((NHttpConnection) conn);
    }

    private void initialize(NHttpClientConnection conn, Object attachment) {
        HttpContext context = conn.getContext();

        context.setAttribute("http.connection", conn);
        this.execHandler.initalizeContext(context, attachment);
    }

    private void continueRequest(NHttpClientConnection conn, ClientConnState connState) throws IOException {
        HttpRequest request = connState.getRequest();

        int timeout = connState.getTimeout();
        conn.setSocketTimeout(timeout);

        sendRequestBody((HttpEntityEnclosingRequest) request, connState, conn);
    }

    private void sendRequestBody(final HttpEntityEnclosingRequest request, final ClientConnState connState, final NHttpClientConnection conn) throws IOException {
        HttpEntity entity = request.getEntity();
        if (entity != null) {
            this.executor.execute(new Runnable() {

                public void run() {
                    try {
                        synchronized (connState) {
                            while (true) {
                                try {
                                    int currentState = connState.getOutputState();
                                    if (!connState.isWorkerRunning()) {
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
                            connState.setWorkerRunning(true);
                        }

                        ContentOutputStream contentOutputStream = new ContentOutputStream(connState.getOutbuffer());

                        request.getEntity().writeTo((OutputStream) contentOutputStream);
                        contentOutputStream.flush();
                        contentOutputStream.close();

                        synchronized (connState) {
                            connState.setWorkerRunning(false);
                            connState.notifyAll();
                        }

                    } catch (IOException ex) {
                        ThrottlingHttpClientHandler.this.shutdownConnection((NHttpConnection) conn, ex);
                        if (ThrottlingHttpClientHandler.this.eventListener != null) {
                            ThrottlingHttpClientHandler.this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
                        }
                    }
                }
            });
        }
    }

    private void handleResponse(final HttpResponse response, final ClientConnState connState, final NHttpClientConnection conn) {
        final HttpContext context = conn.getContext();

        this.executor.execute(new Runnable() {

            public void run() {
                try {
                    synchronized (connState) {
                        while (true) {
                            try {
                                int currentState = connState.getOutputState();
                                if (!connState.isWorkerRunning()) {
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
                        connState.setWorkerRunning(true);
                    }

                    ThrottlingHttpClientHandler.this.execHandler.handleResponse(response, context);

                    synchronized (connState) {
                        while (true) {
                            try {
                                int currentState = connState.getInputState();
                                if (currentState == 64) {
                                    break;
                                }
                                if (currentState == -1) {
                                    return;
                                }
                                connState.wait();
                            } catch (InterruptedException ex) {
                                connState.shutdown();
                                break;
                            }
                        }
                        connState.resetInput();
                        connState.resetOutput();
                        if (conn.isOpen()) {
                            conn.requestOutput();
                        }
                        connState.setWorkerRunning(false);
                        connState.notifyAll();
                    }

                } catch (IOException ex) {
                    ThrottlingHttpClientHandler.this.shutdownConnection((NHttpConnection) conn, ex);
                    if (ThrottlingHttpClientHandler.this.eventListener != null) {
                        ThrottlingHttpClientHandler.this.eventListener.fatalIOException(ex, (NHttpConnection) conn);
                    }
                }
            }
        });
    }

    static class ClientConnState {
        public static final int SHUTDOWN = -1;

        public static final int READY = 0;

        public static final int REQUEST_SENT = 1;

        public static final int EXPECT_CONTINUE = 2;

        public static final int REQUEST_BODY_STREAM = 4;

        public static final int REQUEST_BODY_DONE = 8;

        public static final int RESPONSE_RECEIVED = 16;

        public static final int RESPONSE_BODY_STREAM = 32;

        public static final int RESPONSE_BODY_DONE = 64;

        public static final int RESPONSE_DONE = 64;

        private final SharedInputBuffer inbuffer;
        private final SharedOutputBuffer outbuffer;
        private volatile int inputState;
        private volatile int outputState;
        private volatile HttpRequest request;
        private volatile HttpResponse response;
        private volatile int timeout;
        private volatile boolean workerRunning;

        public ClientConnState(int bufsize, IOControl ioControl, ByteBufferAllocator allocator) {
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

        public int getTimeout() {
            return this.timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public boolean isWorkerRunning() {
            return this.workerRunning;
        }

        public void setWorkerRunning(boolean b) {
            this.workerRunning = b;
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
        }
    }
}

