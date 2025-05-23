package org.apache.http.impl.io;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;

@Immutable
public class DefaultHttpResponseParserFactory
implements HttpMessageParserFactory<HttpResponse>
{
public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();

private final LineParser lineParser;

private final HttpResponseFactory responseFactory;

public DefaultHttpResponseParserFactory(LineParser lineParser, HttpResponseFactory responseFactory) {
this.lineParser = (lineParser != null) ? lineParser : (LineParser)BasicLineParser.INSTANCE;
this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
}

public DefaultHttpResponseParserFactory() {
this(null, null);
}

public HttpMessageParser<HttpResponse> create(SessionInputBuffer buffer, MessageConstraints constraints) {
return new DefaultHttpResponseParser(buffer, this.lineParser, this.responseFactory, constraints);
}
}

