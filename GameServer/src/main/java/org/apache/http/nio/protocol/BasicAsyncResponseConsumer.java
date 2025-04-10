package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.ContentTooLongException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

public class BasicAsyncResponseConsumer
extends AbstractAsyncResponseConsumer<HttpResponse>
{
private volatile HttpResponse response;
private volatile SimpleInputBuffer buf;

protected void onResponseReceived(HttpResponse response) throws IOException {
this.response = response;
}

protected void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
long len = entity.getContentLength();
if (len > 2147483647L) {
throw new ContentTooLongException("Entity content is too long: " + len);
}
if (len < 0L) {
len = 4096L;
}
this.buf = new SimpleInputBuffer((int)len, (ByteBufferAllocator)new HeapByteBufferAllocator());
this.response.setEntity((HttpEntity)new ContentBufferEntity(entity, (ContentInputBuffer)this.buf));
}

protected void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
Asserts.notNull(this.buf, "Content buffer");
this.buf.consumeContent(decoder);
}

protected void releaseResources() {
this.response = null;
this.buf = null;
}

protected HttpResponse buildResult(HttpContext context) {
return this.response;
}
}

