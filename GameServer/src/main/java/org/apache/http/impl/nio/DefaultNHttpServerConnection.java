package org.apache.http.impl.nio;

import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestParser;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestParserFactory;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseWriter;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseWriterFactory;
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
public class DefaultNHttpServerConnection
        extends NHttpConnectionBase
        implements NHttpServerIOTarget {
    protected final NHttpMessageParser<HttpRequest> requestParser;
    protected final NHttpMessageWriter<HttpResponse> responseWriter;

    @Deprecated
    public DefaultNHttpServerConnection(IOSession session, HttpRequestFactory requestFactory, ByteBufferAllocator allocator, HttpParams params) {
        super(session, allocator, params);
        Args.notNull(requestFactory, "Request factory");
        this.requestParser = createRequestParser((SessionInputBuffer) this.inbuf, requestFactory, params);
        this.responseWriter = createResponseWriter((SessionOutputBuffer) this.outbuf, params);
    }

    public DefaultNHttpServerConnection(IOSession session, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory) {
        super(session, buffersize, fragmentSizeHint, allocator, chardecoder, charencoder, constraints, (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy) DisallowIdentityContentLengthStrategy.INSTANCE, (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy) StrictContentLengthStrategy.INSTANCE);

        this.requestParser = ((requestParserFactory != null) ? requestParserFactory : DefaultHttpRequestParserFactory.INSTANCE).create((SessionInputBuffer) this.inbuf, constraints);

        this.responseWriter = ((responseWriterFactory != null) ? responseWriterFactory : DefaultHttpResponseWriterFactory.INSTANCE).create((SessionOutputBuffer) this.outbuf);
    }

    public DefaultNHttpServerConnection(IOSession session, int buffersize, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints) {
        this(session, buffersize, buffersize, (ByteBufferAllocator) null, chardecoder, charencoder, constraints, (ContentLengthStrategy) null, (ContentLengthStrategy) null, (NHttpMessageParserFactory<HttpRequest>) null, (NHttpMessageWriterFactory<HttpResponse>) null);
    }

    public DefaultNHttpServerConnection(IOSession session, int buffersize) {
        this(session, buffersize, buffersize, (ByteBufferAllocator) null, (CharsetDecoder) null, (CharsetEncoder) null, (MessageConstraints) null, (ContentLengthStrategy) null, (ContentLengthStrategy) null, (NHttpMessageParserFactory<HttpRequest>) null, (NHttpMessageWriterFactory<HttpResponse>) null);
    }

    @Deprecated
    protected ContentLengthStrategy createIncomingContentStrategy() {
        return (ContentLengthStrategy) new DisallowIdentityContentLengthStrategy((ContentLengthStrategy) new LaxContentLengthStrategy(0));
    }

    @Deprecated
    protected NHttpMessageParser<HttpRequest> createRequestParser(SessionInputBuffer buffer, HttpRequestFactory requestFactory, HttpParams params) {
        MessageConstraints constraints = HttpParamConfig.getMessageConstraints(params);
        return (NHttpMessageParser<HttpRequest>) new DefaultHttpRequestParser(buffer, null, requestFactory, constraints);
    }

    @Deprecated
    protected NHttpMessageWriter<HttpResponse> createResponseWriter(SessionOutputBuffer buffer, HttpParams params) {
        return (NHttpMessageWriter<HttpResponse>) new DefaultHttpResponseWriter(buffer, null);
    }

    protected void onRequestReceived(HttpRequest request) {
    }

    protected void onResponseSubmitted(HttpResponse response) {
    }

    public void resetInput() {
        this.request = null;
        this.contentDecoder = null;
        this.requestParser.reset();
    }

    public void resetOutput() {
        this.response = null;
        this.contentEncoder = null;
        this.responseWriter.reset();
    }

    public void consumeInput(NHttpServerEventHandler handler) {
        if (this.status != 0) {
            this.session.clearEvent(1);
            return;
        }
        try {
            if (this.request == null) {
                int bytesRead;
                do {
                    bytesRead = this.requestParser.fillBuffer(this.session.channel());
                    if (bytesRead > 0) {
                        this.inTransportMetrics.incrementBytesTransferred(bytesRead);
                    }
                    this.request = (HttpRequest) this.requestParser.parse();
                } while (bytesRead > 0 && this.request == null);
                if (this.request != null) {
                    if (this.request instanceof HttpEntityEnclosingRequest) {

                        HttpEntity entity = prepareDecoder((HttpMessage) this.request);
                        ((HttpEntityEnclosingRequest) this.request).setEntity(entity);
                    }
                    this.connMetrics.incrementRequestCount();
                    this.hasBufferedInput = this.inbuf.hasData();
                    onRequestReceived(this.request);
                    handler.requestReceived((NHttpServerConnection) this);
                    if (this.contentDecoder == null) {

                        resetInput();
                    }
                }
                if (bytesRead == -1 && !this.inbuf.hasData()) {
                    handler.endOfInput((NHttpServerConnection) this);
                }
            }
            if (this.contentDecoder != null && (this.session.getEventMask() & 0x1) > 0) {
                handler.inputReady((NHttpServerConnection) this, this.contentDecoder);
                if (this.contentDecoder.isCompleted()) {

                    resetInput();
                }
            }
        } catch (HttpException ex) {
            resetInput();
            handler.exception((NHttpServerConnection) this, (Exception) ex);
        } catch (Exception ex) {
            handler.exception((NHttpServerConnection) this, ex);
        } finally {

            this.hasBufferedInput = this.inbuf.hasData();
        }
    }

    public void produceOutput(NHttpServerEventHandler handler) {
        try {
            if (this.status == 0) {
                if (this.contentEncoder == null) {
                    handler.responseReady((NHttpServerConnection) this);
                }
                if (this.contentEncoder != null) {
                    handler.outputReady((NHttpServerConnection) this, this.contentEncoder);
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
            if (!this.outbuf.hasData() &&
                    this.status == 1) {
                this.session.close();
                this.status = 2;
                resetOutput();
            }

        } catch (Exception ex) {
            handler.exception((NHttpServerConnection) this, ex);
        } finally {

            this.hasBufferedOutput = this.outbuf.hasData();
        }
    }

    public void submitResponse(HttpResponse response) throws IOException, HttpException {
        Args.notNull(response, "HTTP response");
        assertNotClosed();
        if (this.response != null) {
            throw new HttpException("Response already submitted");
        }
        onResponseSubmitted(response);
        this.responseWriter.write((HttpMessage) response);
        this.hasBufferedOutput = this.outbuf.hasData();

        if (response.getStatusLine().getStatusCode() >= 200) {
            this.connMetrics.incrementResponseCount();
            if (response.getEntity() != null) {
                this.response = response;
                prepareEncoder((HttpMessage) response);
            }
        }

        this.session.setEvent(4);
    }

    public boolean isResponseSubmitted() {
        return (this.response != null);
    }

    public void consumeInput(NHttpServiceHandler handler) {
        consumeInput(new NHttpServerEventHandlerAdaptor(handler));
    }

    public void produceOutput(NHttpServiceHandler handler) {
        produceOutput(new NHttpServerEventHandlerAdaptor(handler));
    }
}

