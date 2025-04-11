package org.apache.http.impl.nio.conn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
import org.apache.http.nio.NHttpMessageParserFactory;
import org.apache.http.nio.NHttpMessageWriterFactory;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.conn.NHttpConnectionFactory;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.HeapByteBufferAllocator;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;

public class ManagedNHttpClientConnectionFactory
        implements NHttpConnectionFactory<ManagedNHttpClientConnection> {
    public static final ManagedNHttpClientConnectionFactory INSTANCE = new ManagedNHttpClientConnectionFactory();
    private static final AtomicLong COUNTER = new AtomicLong();
    private final Log headerlog = LogFactory.getLog("org.apache.http.headers");
    private final Log wirelog = LogFactory.getLog("org.apache.http.wire");
    private final Log log = LogFactory.getLog(ManagedNHttpClientConnectionImpl.class);
    private final ByteBufferAllocator allocator;

    private final NHttpMessageWriterFactory<HttpRequest> requestWriterFactory;

    private final NHttpMessageParserFactory<HttpResponse> responseParserFactory;

    public ManagedNHttpClientConnectionFactory(NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, NHttpMessageParserFactory<HttpResponse> responseParserFactory, ByteBufferAllocator allocator) {
        this.requestWriterFactory = (requestWriterFactory != null) ? requestWriterFactory : (NHttpMessageWriterFactory<HttpRequest>) DefaultHttpRequestWriterFactory.INSTANCE;

        this.responseParserFactory = (responseParserFactory != null) ? responseParserFactory : (NHttpMessageParserFactory<HttpResponse>) DefaultHttpResponseParserFactory.INSTANCE;

        this.allocator = (allocator != null) ? allocator : (ByteBufferAllocator) HeapByteBufferAllocator.INSTANCE;
    }

    public ManagedNHttpClientConnectionFactory() {
        this(null, null, null);
    }

    public ManagedNHttpClientConnection create(IOSession iosession, ConnectionConfig config) {
        String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
        CharsetDecoder chardecoder = null;
        CharsetEncoder charencoder = null;
        Charset charset = config.getCharset();
        CodingErrorAction malformedInputAction = (config.getMalformedInputAction() != null) ? config.getMalformedInputAction() : CodingErrorAction.REPORT;

        CodingErrorAction unmappableInputAction = (config.getUnmappableInputAction() != null) ? config.getUnmappableInputAction() : CodingErrorAction.REPORT;

        if (charset != null) {
            chardecoder = charset.newDecoder();
            chardecoder.onMalformedInput(malformedInputAction);
            chardecoder.onUnmappableCharacter(unmappableInputAction);
            charencoder = charset.newEncoder();
            charencoder.onMalformedInput(malformedInputAction);
            charencoder.onUnmappableCharacter(unmappableInputAction);
        }
        ManagedNHttpClientConnection conn = new ManagedNHttpClientConnectionImpl(id, this.log, this.headerlog, this.wirelog, iosession, config.getBufferSize(), config.getFragmentSizeHint(), this.allocator, chardecoder, charencoder, config.getMessageConstraints(), null, null, this.requestWriterFactory, this.responseParserFactory);

        iosession.setAttribute("http.connection", conn);
        return conn;
    }
}

