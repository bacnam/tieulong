package org.apache.http.impl.nio;

import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriter;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParser;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
import org.apache.http.nio.*;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.nio.reactor.SessionOutputBuffer;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

@NotThreadSafe
public class DefaultNHttpClientConnection
        extends NHttpConnectionBase
        implements NHttpClientIOTarget {
    protected final NHttpMessageParser<HttpResponse> responseParser;
    protected final NHttpMessageWriter<HttpRequest> requestWriter;

    @Deprecated
    public DefaultNHttpClientConnection(IOSession session, HttpResponseFactory responseFactory, ByteBufferAllocator allocator, HttpParams params) {
        super(session, allocator, params);
        Args.notNull(responseFactory, "Response factory");
        this.responseParser = createResponseParser((SessionInputBuffer) this.inbuf, responseFactory, params);
        this.requestWriter = createRequestWriter((SessionOutputBuffer) this.outbuf, params);
        this.hasBufferedInput = false;
        this.hasBufferedOutput = false;
        this.session.setBufferStatus(this);
    }

    public DefaultNHttpClientConnection(IOSession session, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, NHttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super(session, buffersize, fragmentSizeHint, allocator, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy);

        this.requestWriter = ((requestWriterFactory != null) ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE).create((SessionOutputBuffer) this.outbuf);

        this.responseParser = ((responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE).create((SessionInputBuffer) this.inbuf, constraints);
    }

    public DefaultNHttpClientConnection(IOSession session, int buffersize, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints) {
        this(session, buffersize, buffersize, (ByteBufferAllocator) null, chardecoder, charencoder, constraints, (ContentLengthStrategy) null, (ContentLengthStrategy) null, (NHttpMessageWriterFactory<HttpRequest>) null, (NHttpMessageParserFactory<HttpResponse>) null);
    }

    public DefaultNHttpClientConnection(IOSession session, int buffersize) {
        this(session, buffersize, buffersize, (ByteBufferAllocator) null, (CharsetDecoder) null, (CharsetEncoder) null, (MessageConstraints) null, (ContentLengthStrategy) null, (ContentLengthStrategy) null, (NHttpMessageWriterFactory<HttpRequest>) null, (NHttpMessageParserFactory<HttpResponse>) null);
    }

    @Deprecated
    protected NHttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
        MessageConstraints constraints = HttpParamConfig.getMessageConstraints(params);
        return (NHttpMessageParser<HttpResponse>) new DefaultHttpResponseParser(buffer, null, responseFactory, constraints);
    }

    @Deprecated
    protected NHttpMessageWriter<HttpRequest> createRequestWriter(SessionOutputBuffer buffer, HttpParams params) {
        return (NHttpMessageWriter<HttpRequest>) new DefaultHttpRequestWriter(buffer, null);
    }

    protected void onResponseReceived(HttpResponse response) {
    }

    protected void onRequestSubmitted(HttpRequest request) {
    }

    public void resetInput() {
        this.response = null;
        this.contentDecoder = null;
        this.responseParser.reset();
    }

    public void resetOutput() {
        this.request = null;
        this.contentEncoder = null;
        this.requestWriter.reset();
    }

    public void consumeInput(NHttpClientEventHandler handler) {
        if (this.status != 0) {
            this.session.clearEvent(1);
            return;
        }
        try {
            if (this.response == null) {
                int bytesRead;
                do {
                    bytesRead = this.responseParser.fillBuffer(this.session.channel());
                    if (bytesRead > 0) {
                        this.inTransportMetrics.incrementBytesTransferred(bytesRead);
                    }
                    this.response = (HttpResponse) this.responseParser.parse();
                } while (bytesRead > 0 && this.response == null);
                if (this.response != null) {
                    if (this.response.getStatusLine().getStatusCode() >= 200) {
                        HttpEntity entity = prepareDecoder((HttpMessage) this.response);
                        this.response.setEntity(entity);
                        this.connMetrics.incrementResponseCount();
                    }
                    this.hasBufferedInput = this.inbuf.hasData();
                    onResponseReceived(this.response);
                    handler.responseReceived((NHttpClientConnection) this);
                    if (this.contentDecoder == null) {
                        resetInput();
                    }
                }
                if (bytesRead == -1 && !this.inbuf.hasData()) {
                    handler.endOfInput((NHttpClientConnection) this);
                }
            }
            if (this.contentDecoder != null && (this.session.getEventMask() & 0x1) > 0) {
                handler.inputReady((NHttpClientConnection) this, this.contentDecoder);
                if (this.contentDecoder.isCompleted()) {

                    resetInput();
                }
            }
        } catch (HttpException ex) {
            resetInput();
            handler.exception((NHttpClientConnection) this, (Exception) ex);
        } catch (Exception ex) {
            handler.exception((NHttpClientConnection) this, ex);
        } finally {

            this.hasBufferedInput = this.inbuf.hasData();
        }
    }

    public void produceOutput(NHttpClientEventHandler handler) {
        try {
            if (this.status == 0) {
                if (this.contentEncoder == null) {
                    handler.requestReady((NHttpClientConnection) this);
                }
                if (this.contentEncoder != null) {
                    handler.outputReady((NHttpClientConnection) this, this.contentEncoder);
                    if (this.contentEncoder.isCompleted()) {
                        resetOutput();
                    }
                }
            }
            if (this.outbuf.hasData()) {
                int bytesWritten = this.outbuf.flush(this.session.channel());
                if (bytesWritten > 0) {
                    this.outTransportMetrics.incrementBytesTransferred(bytesWritten);
                }
            }
            if (!this.outbuf.hasData()) {
                if (this.status == 1) {
                    this.session.close();
                    this.status = 2;
                    resetOutput();
                }
                if (this.contentEncoder == null && this.status != 2) {
                    this.session.clearEvent(4);
                }
            }
        } catch (Exception ex) {
            handler.exception((NHttpClientConnection) this, ex);
        } finally {

            this.hasBufferedOutput = this.outbuf.hasData();
        }
    }

    public void submitRequest(HttpRequest request) throws IOException, HttpException {
        Args.notNull(request, "HTTP request");
        assertNotClosed();
        if (this.request != null) {
            throw new HttpException("Request already submitted");
        }
        onRequestSubmitted(request);
        this.requestWriter.write((HttpMessage) request);
        this.hasBufferedOutput = this.outbuf.hasData();

        if (request instanceof HttpEntityEnclosingRequest && ((HttpEntityEnclosingRequest) request).getEntity() != null) {

            prepareEncoder((HttpMessage) request);
            this.request = request;
        }
        this.connMetrics.incrementRequestCount();
        this.session.setEvent(4);
    }

    public boolean isRequestSubmitted() {
        return (this.request != null);
    }

    public void consumeInput(NHttpClientHandler handler) {
        consumeInput(new NHttpClientEventHandlerAdaptor(handler));
    }

    public void produceOutput(NHttpClientHandler handler) {
        produceOutput(new NHttpClientEventHandlerAdaptor(handler));
    }
}

