package org.apache.http.impl.nio.codecs;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.nio.NHttpMessageWriter;
import org.apache.http.nio.reactor.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;

@NotThreadSafe
public abstract class AbstractMessageWriter<T extends HttpMessage>
        implements NHttpMessageWriter<T> {
    protected final SessionOutputBuffer sessionBuffer;
    protected final CharArrayBuffer lineBuf;
    protected final LineFormatter lineFormatter;

    @Deprecated
    public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
        Args.notNull(buffer, "Session input buffer");
        this.sessionBuffer = buffer;
        this.lineBuf = new CharArrayBuffer(64);
        this.lineFormatter = (formatter != null) ? formatter : (LineFormatter) BasicLineFormatter.INSTANCE;
    }

    public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
        this.sessionBuffer = (SessionOutputBuffer) Args.notNull(buffer, "Session input buffer");
        this.lineFormatter = (formatter != null) ? formatter : (LineFormatter) BasicLineFormatter.INSTANCE;
        this.lineBuf = new CharArrayBuffer(64);
    }

    public void reset() {
    }

    protected abstract void writeHeadLine(T paramT) throws IOException;

    public void write(T message) throws IOException, HttpException {
        Args.notNull(message, "HTTP message");
        writeHeadLine(message);
        for (HeaderIterator<Header> headerIterator = message.headerIterator(); headerIterator.hasNext(); ) {
            Header header = headerIterator.next();
            this.sessionBuffer.writeLine(this.lineFormatter.formatHeader(this.lineBuf, header));
        }

        this.lineBuf.clear();
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}

