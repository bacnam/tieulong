package org.apache.http.impl.nio.codecs;

import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.message.LineFormatter;
import org.apache.http.nio.reactor.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;

@NotThreadSafe
public class DefaultHttpResponseWriter
        extends AbstractMessageWriter<HttpResponse> {
    @Deprecated
    public DefaultHttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
        super(buffer, formatter, params);
    }

    public DefaultHttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
        super(buffer, formatter);
    }

    public DefaultHttpResponseWriter(SessionOutputBuffer buffer) {
        super(buffer, null);
    }

    protected void writeHeadLine(HttpResponse message) throws IOException {
        CharArrayBuffer buffer = this.lineFormatter.formatStatusLine(this.lineBuf, message.getStatusLine());

        this.sessionBuffer.writeLine(buffer);
    }
}

