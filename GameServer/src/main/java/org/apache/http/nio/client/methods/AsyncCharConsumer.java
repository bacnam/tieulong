package org.apache.http.nio.client.methods;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Asserts;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public abstract class AsyncCharConsumer<T>
        extends AbstractAsyncResponseConsumer<T> {
    private final ByteBuffer bbuf;
    private final CharBuffer cbuf;
    private CharsetDecoder chardecoder;
    private ContentType contentType;

    public AsyncCharConsumer(int bufSize) {
        this.bbuf = ByteBuffer.allocate(bufSize);
        this.cbuf = CharBuffer.allocate(bufSize);
    }

    public AsyncCharConsumer() {
        this(8192);
    }

    protected abstract void onCharReceived(CharBuffer paramCharBuffer, IOControl paramIOControl) throws IOException;

    protected final void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
        this.contentType = (contentType != null) ? contentType : ContentType.DEFAULT_TEXT;
        Charset charset = this.contentType.getCharset();
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET;
        }
        this.chardecoder = charset.newDecoder();
    }

    protected final void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
        IOSession iosession;
        Asserts.notNull(this.bbuf, "Byte buffer");

        if (ioctrl instanceof ManagedNHttpClientConnection) {
            ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection) ioctrl;
            iosession = (conn != null) ? conn.getIOSession() : null;
        } else {
            iosession = null;
        }
        while (!isDone()) {
            int bytesRead = decoder.read(this.bbuf);
            if (bytesRead <= 0) {
                break;
            }
            this.bbuf.flip();
            boolean completed = decoder.isCompleted();
            CoderResult result = this.chardecoder.decode(this.bbuf, this.cbuf, completed);
            handleDecodingResult(result, ioctrl);
            this.bbuf.compact();
            if (completed) {
                result = this.chardecoder.flush(this.cbuf);
                handleDecodingResult(result, ioctrl);
                break;
            }
            if (iosession != null && (iosession.isClosed() || (iosession.getEventMask() & 0x1) == 0)) {
                break;
            }
        }
    }

    private void handleDecodingResult(CoderResult result, IOControl ioctrl) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.cbuf.flip();
        if (this.cbuf.hasRemaining()) {
            onCharReceived(this.cbuf, ioctrl);
        }
        this.cbuf.clear();
    }

    protected void releaseResources() {
    }
}

