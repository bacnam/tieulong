package org.apache.http.nio.protocol;

import org.apache.http.ContentTooLongException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.entity.ContentBufferEntity;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.ContentInputBuffer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.nio.util.SimpleInputBuffer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

import java.io.IOException;

public class BasicAsyncRequestConsumer
        extends AbstractAsyncRequestConsumer<HttpRequest> {
    private volatile HttpRequest request;
    private volatile SimpleInputBuffer buf;

    protected void onRequestReceived(HttpRequest request) throws IOException {
        this.request = request;
    }

    protected void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
        long len = entity.getContentLength();
        if (len > 2147483647L) {
            throw new ContentTooLongException("Entity content is too long: " + len);
        }
        if (len < 0L) {
            len = 4096L;
        }
        this.buf = new SimpleInputBuffer((int) len, (ByteBufferAllocator) new HeapByteBufferAllocator());
        ((HttpEntityEnclosingRequest) this.request).setEntity((HttpEntity) new ContentBufferEntity(entity, (ContentInputBuffer) this.buf));
    }

    protected void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
        Asserts.notNull(this.buf, "Content buffer");
        this.buf.consumeContent(decoder);
    }

    protected void releaseResources() {
        this.request = null;
        this.buf = null;
    }

    protected HttpRequest buildResult(HttpContext context) {
        return this.request;
    }
}

