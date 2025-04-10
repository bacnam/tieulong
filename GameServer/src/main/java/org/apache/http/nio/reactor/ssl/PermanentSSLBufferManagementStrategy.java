package org.apache.http.nio.reactor.ssl;

import java.nio.ByteBuffer;
import org.apache.http.util.Args;

public class PermanentSSLBufferManagementStrategy
implements SSLBufferManagementStrategy
{
public SSLBuffer constructBuffer(int size) {
return new InternalBuffer(size);
}

private static final class InternalBuffer
implements SSLBuffer
{
private final ByteBuffer buffer;

public InternalBuffer(int size) {
Args.positive(size, "size");
this.buffer = ByteBuffer.allocate(size);
}

public ByteBuffer acquire() {
return this.buffer;
}

public void release() {}

public boolean isAcquired() {
return true;
}

public boolean hasData() {
return (this.buffer.position() > 0);
}
}
}

