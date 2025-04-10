package org.apache.mina.filter.stream;

import java.io.IOException;
import java.io.InputStream;
import org.apache.mina.core.buffer.IoBuffer;

public class StreamWriteFilter
extends AbstractStreamWriteFilter<InputStream>
{
protected IoBuffer getNextBuffer(InputStream is) throws IOException {
byte[] bytes = new byte[getWriteBufferSize()];

int off = 0;
int n = 0;
while (off < bytes.length && (n = is.read(bytes, off, bytes.length - off)) != -1) {
off += n;
}

if (n == -1 && off == 0) {
return null;
}

IoBuffer buffer = IoBuffer.wrap(bytes, 0, off);

return buffer;
}

protected Class<InputStream> getMessageClass() {
return InputStream.class;
}
}

