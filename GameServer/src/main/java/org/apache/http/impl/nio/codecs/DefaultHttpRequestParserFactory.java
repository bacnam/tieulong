package org.apache.http.impl.nio.codecs;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.nio.NHttpMessageParser;
import org.apache.http.nio.NHttpMessageParserFactory;
import org.apache.http.nio.reactor.SessionInputBuffer;

@Immutable
public class DefaultHttpRequestParserFactory
implements NHttpMessageParserFactory<HttpRequest>
{
public static final DefaultHttpRequestParserFactory INSTANCE = new DefaultHttpRequestParserFactory();

private final LineParser lineParser;

private final HttpRequestFactory requestFactory;

public DefaultHttpRequestParserFactory(LineParser lineParser, HttpRequestFactory requestFactory) {
this.lineParser = (lineParser != null) ? lineParser : (LineParser)BasicLineParser.INSTANCE;
this.requestFactory = (requestFactory != null) ? requestFactory : (HttpRequestFactory)DefaultHttpRequestFactory.INSTANCE;
}

public DefaultHttpRequestParserFactory() {
this(null, null);
}

public NHttpMessageParser<HttpRequest> create(SessionInputBuffer buffer, MessageConstraints constraints) {
return new DefaultHttpRequestParser(buffer, this.lineParser, this.requestFactory, constraints);
}
}

