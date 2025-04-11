package org.apache.http.impl.nio.conn;

import org.apache.commons.logging.Log;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.nio.DefaultNHttpClientConnection;
import org.apache.http.nio.NHttpMessageParserFactory;
import org.apache.http.nio.NHttpMessageWriterFactory;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLIOSession;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

import javax.net.ssl.SSLSession;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

class ManagedNHttpClientConnectionImpl
        extends DefaultNHttpClientConnection
        implements ManagedNHttpClientConnection {
    private final Log headerlog;
    private final Log wirelog;
    private final Log log;
    private final String id;
    private IOSession original;

    public ManagedNHttpClientConnectionImpl(String id, Log log, Log headerlog, Log wirelog, IOSession iosession, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, NHttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super(iosession, buffersize, fragmentSizeHint, allocator, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);

        this.id = id;
        this.log = log;
        this.headerlog = headerlog;
        this.wirelog = wirelog;
        this.original = iosession;
        if (this.log.isDebugEnabled() || this.wirelog.isDebugEnabled()) {
            super.bind(new LoggingIOSession(iosession, this.id, this.log, this.wirelog));
        }
    }

    public void bind(IOSession iosession) {
        Args.notNull(iosession, "I/O session");
        Asserts.check(!iosession.isClosed(), "I/O session is closed");
        this.status = 0;
        this.original = iosession;
        if (this.log.isDebugEnabled() || this.wirelog.isDebugEnabled()) {
            this.log.debug(this.id + " Upgrade session " + iosession);
            super.bind(new LoggingIOSession(iosession, this.id, this.log, this.wirelog));
        } else {
            super.bind(iosession);
        }
    }

    public IOSession getIOSession() {
        return this.original;
    }

    public SSLSession getSSLSession() {
        if (this.original instanceof SSLIOSession) {
            return ((SSLIOSession) this.original).getSSLSession();
        }
        return null;
    }

    public String getId() {
        return this.id;
    }

    protected void onResponseReceived(HttpResponse response) {
        if (response != null && this.headerlog.isDebugEnabled()) {
            this.headerlog.debug(this.id + " << " + response.getStatusLine().toString());
            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                this.headerlog.debug(this.id + " << " + header.toString());
            }
        }
    }

    protected void onRequestSubmitted(HttpRequest request) {
        if (request != null && this.headerlog.isDebugEnabled()) {
            this.headerlog.debug(this.id + " >> " + request.getRequestLine().toString());
            Header[] headers = request.getAllHeaders();
            for (Header header : headers) {
                this.headerlog.debug(this.id + " >> " + header.toString());
            }
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.id);
        buf.append(" [");
        switch (this.status) {
            case 0:
                buf.append("ACTIVE");
                if (this.inbuf.hasData()) {
                    buf.append("(").append(this.inbuf.length()).append(")");
                }
                break;
            case 1:
                buf.append("CLOSING");
                break;
            case 2:
                buf.append("CLOSED");
                break;
        }
        buf.append("]");
        return buf.toString();
    }
}

