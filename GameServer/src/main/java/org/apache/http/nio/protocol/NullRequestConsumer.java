package org.apache.http.nio.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.http.HttpRequest;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.protocol.HttpContext;

class NullRequestConsumer
implements HttpAsyncRequestConsumer<Object>
{
private final ByteBuffer buffer = ByteBuffer.allocate(2048);

private volatile boolean completed;

public void requestReceived(HttpRequest request) {}

public void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
int lastRead;
do {
this.buffer.clear();
lastRead = decoder.read(this.buffer);
} while (lastRead > 0);
}

public void requestCompleted(HttpContext context) {
this.completed = true;
}

public void failed(Exception ex) {
this.completed = true;
}

public Object getResult() {
return Boolean.valueOf(this.completed);
}

public Exception getException() {
return null;
}

public void close() throws IOException {
this.completed = true;
}

public boolean isDone() {
return this.completed;
}
}

