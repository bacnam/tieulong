package org.apache.http.impl.nio.codecs;

import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class DefaultHttpRequestParser
        extends AbstractMessageParser<HttpRequest> {
    private final HttpRequestFactory requestFactory;

    @Deprecated
    public DefaultHttpRequestParser(SessionInputBuffer buffer, LineParser parser, HttpRequestFactory requestFactory, HttpParams params) {
        super(buffer, parser, params);
        Args.notNull(requestFactory, "Request factory");
        this.requestFactory = requestFactory;
    }

    public DefaultHttpRequestParser(SessionInputBuffer buffer, LineParser parser, HttpRequestFactory requestFactory, MessageConstraints constraints) {
        super(buffer, parser, constraints);
        this.requestFactory = (requestFactory != null) ? requestFactory : (HttpRequestFactory) DefaultHttpRequestFactory.INSTANCE;
    }

    public DefaultHttpRequestParser(SessionInputBuffer buffer, MessageConstraints constraints) {
        this(buffer, (LineParser) null, (HttpRequestFactory) null, constraints);
    }

    public DefaultHttpRequestParser(SessionInputBuffer buffer) {
        this(buffer, null);
    }

    protected HttpRequest createMessage(CharArrayBuffer buffer) throws HttpException, ParseException {
        ParserCursor cursor = new ParserCursor(0, buffer.length());
        RequestLine requestLine = this.lineParser.parseRequestLine(buffer, cursor);
        return this.requestFactory.newHttpRequest(requestLine);
    }
}

