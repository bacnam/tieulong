package org.apache.http.nio.reactor.ssl;

import java.nio.ByteBuffer;
import org.apache.http.util.Args;

public class ReleasableSSLBufferManagementStrategy
implements SSLBufferManagementStrategy
{
public SSLBuffer constructBuffer(int size) {
return new InternalBuffer(size);
}

private static final class InternalBuffer
implements SSLBuffer
{
private ByteBuffer wrapped;
private final int length;

public InternalBuffer(int size) {
Args.positive(size, "size");
this.length = size;
}

public ByteBuffer acquire() {
if (this.wrapped != null) {
return this.wrapped;
}
this.wrapped = ByteBuffer.allocate(this.length);
return this.wrapped;
}

public void release() {
this.wrapped = null;
}

public boolean isAcquired() {
return (this.wrapped != null);
}

public boolean hasData() {
return (this.wrapped != null && this.wrapped.position() > 0);
}
}
}

