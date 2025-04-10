package org.apache.http.nio.protocol;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.protocol.HttpContext;

public abstract class AbstractAsyncRequestConsumer<T>
implements HttpAsyncRequestConsumer<T>
{
private final AtomicBoolean completed = new AtomicBoolean(false);

private volatile T result;

private volatile Exception ex;

protected abstract void onRequestReceived(HttpRequest paramHttpRequest) throws HttpException, IOException;

protected abstract void onEntityEnclosed(HttpEntity paramHttpEntity, ContentType paramContentType) throws IOException;

protected abstract void onContentReceived(ContentDecoder paramContentDecoder, IOControl paramIOControl) throws IOException;

protected abstract T buildResult(HttpContext paramHttpContext) throws Exception;

protected abstract void releaseResources();

protected void onClose() throws IOException {}

public final void requestReceived(HttpRequest request) throws HttpException, IOException {
onRequestReceived(request);
if (request instanceof HttpEntityEnclosingRequest) {
HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
if (entity != null) {
ContentType contentType = ContentType.getOrDefault(entity);
onEntityEnclosed(entity, contentType);
} 
} 
}

public final void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
onContentReceived(decoder, ioctrl);
}

public final void requestCompleted(HttpContext context) {
if (this.completed.compareAndSet(false, true)) {
try {
this.result = buildResult(context);
} catch (Exception ex) {
this.ex = ex;
} finally {
releaseResources();
} 
}
}

public final void failed(Exception ex) {
if (this.completed.compareAndSet(false, true)) {
this.ex = ex;
releaseResources();
} 
}

public final void close() throws IOException {
if (this.completed.compareAndSet(false, true)) {
releaseResources();
onClose();
} 
}

public Exception getException() {
return this.ex;
}

public T getResult() {
return this.result;
}

public boolean isDone() {
return this.completed.get();
}
}

