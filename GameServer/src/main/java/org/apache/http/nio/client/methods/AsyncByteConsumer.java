package org.apache.http.nio.client.methods;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.util.Asserts;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class AsyncByteConsumer<T>
        extends AbstractAsyncResponseConsumer<T> {
    private final ByteBuffer bbuf;

    public AsyncByteConsumer(int bufSize) {
        this.bbuf = ByteBuffer.allocate(bufSize);
    }

    public AsyncByteConsumer() {
        this(8192);
    }

    protected abstract void onByteReceived(ByteBuffer paramByteBuffer, IOControl paramIOControl) throws IOException;

    protected final void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
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
            onByteReceived(this.bbuf, ioctrl);
            this.bbuf.clear();
            if (decoder.isCompleted()) {
                break;
            }
            if (iosession != null && (iosession.isClosed() || (iosession.getEventMask() & 0x1) == 0))
                break;
        }
    }

    protected void releaseResources() {
    }
}

