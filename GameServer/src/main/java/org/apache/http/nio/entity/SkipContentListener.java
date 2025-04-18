package org.apache.http.nio.entity;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.util.Args;

@Deprecated
public class SkipContentListener
implements ContentListener
{
private final ByteBuffer buffer;

public SkipContentListener(ByteBufferAllocator allocator) {
Args.notNull(allocator, "ByteBuffer allocator");
this.buffer = allocator.allocate(2048);
}

public void contentAvailable(ContentDecoder decoder, IOControl ioctrl) throws IOException {
int lastRead;
do {
this.buffer.clear();
lastRead = decoder.read(this.buffer);
} while (lastRead > 0);
}

public void finished() {}
}

