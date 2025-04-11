package org.apache.http.impl.io;

import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;

@Deprecated
@NotThreadSafe
public class HttpResponseParser
        extends AbstractMessageParser<HttpMessage> {
    private final HttpResponseFactory responseFactory;
    private final CharArrayBuffer lineBuf;

    public HttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
        super(buffer, parser, params);
        this.responseFactory = (HttpResponseFactory) Args.notNull(responseFactory, "Response factory");
        this.lineBuf = new CharArrayBuffer(128);
    }

    protected HttpMessage parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
        this.lineBuf.clear();
        int i = sessionBuffer.readLine(this.lineBuf);
        if (i == -1) {
            throw new NoHttpResponseException("The target server failed to respond");
        }

        ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
        StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
        return (HttpMessage) this.responseFactory.newHttpResponse(statusline, null);
    }
}

