package org.apache.http.impl.nio.codecs;

import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.nio.NHttpMessageWriter;
import org.apache.http.nio.NHttpMessageWriterFactory;
import org.apache.http.nio.reactor.SessionOutputBuffer;

@Immutable
public class DefaultHttpRequestWriterFactory
implements NHttpMessageWriterFactory<HttpRequest>
{
public static final DefaultHttpRequestWriterFactory INSTANCE = new DefaultHttpRequestWriterFactory();

private final LineFormatter lineFormatter;

public DefaultHttpRequestWriterFactory(LineFormatter lineFormatter) {
this.lineFormatter = (lineFormatter != null) ? lineFormatter : (LineFormatter)BasicLineFormatter.INSTANCE;
}

public DefaultHttpRequestWriterFactory() {
this(null);
}

public NHttpMessageWriter<HttpRequest> create(SessionOutputBuffer buffer) {
return new DefaultHttpRequestWriter(buffer, this.lineFormatter);
}
}

