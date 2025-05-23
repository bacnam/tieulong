package org.apache.http.impl.nio.codecs;

import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class DefaultHttpResponseParser
extends AbstractMessageParser<HttpResponse>
{
private final HttpResponseFactory responseFactory;

@Deprecated
public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
super(buffer, parser, params);
Args.notNull(responseFactory, "Response factory");
this.responseFactory = responseFactory;
}

public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, MessageConstraints constraints) {
super(buffer, parser, constraints);
this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
}

public DefaultHttpResponseParser(SessionInputBuffer buffer, MessageConstraints constraints) {
this(buffer, (LineParser)null, (HttpResponseFactory)null, constraints);
}

public DefaultHttpResponseParser(SessionInputBuffer buffer) {
this(buffer, null);
}

protected HttpResponse createMessage(CharArrayBuffer buffer) throws HttpException, ParseException {
ParserCursor cursor = new ParserCursor(0, buffer.length());
StatusLine statusline = this.lineParser.parseStatusLine(buffer, cursor);
return this.responseFactory.newHttpResponse(statusline, null);
}
}

