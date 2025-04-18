package org.apache.http.nio.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.entity.ConsumingNHttpEntity;

@Deprecated
class NullNHttpEntity
extends HttpEntityWrapper
implements ConsumingNHttpEntity
{
private final ByteBuffer buffer;

public NullNHttpEntity(HttpEntity httpEntity) {
super(httpEntity);
this.buffer = ByteBuffer.allocate(2048);
}

public InputStream getContent() throws IOException, UnsupportedOperationException {
throw new UnsupportedOperationException("Does not support blocking methods");
}

public boolean isStreaming() {
return true;
}

public void writeTo(OutputStream out) throws IOException, UnsupportedOperationException {
throw new UnsupportedOperationException("Does not support blocking methods");
}

public void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
int lastRead;
do {
this.buffer.clear();
lastRead = decoder.read(this.buffer);
} while (lastRead > 0);
}

public void finish() {}
}

