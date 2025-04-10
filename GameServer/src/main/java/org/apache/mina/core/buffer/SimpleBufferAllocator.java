package org.apache.mina.core.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SimpleBufferAllocator
implements IoBufferAllocator
{
public IoBuffer allocate(int capacity, boolean direct) {
return wrap(allocateNioBuffer(capacity, direct));
}

public ByteBuffer allocateNioBuffer(int capacity, boolean direct) {
ByteBuffer nioBuffer;
if (direct) {
nioBuffer = ByteBuffer.allocateDirect(capacity);
} else {
nioBuffer = ByteBuffer.allocate(capacity);
} 
return nioBuffer;
}

public IoBuffer wrap(ByteBuffer nioBuffer) {
return new SimpleBuffer(nioBuffer);
}

public void dispose() {}

private class SimpleBuffer
extends AbstractIoBuffer
{
private ByteBuffer buf;

protected SimpleBuffer(ByteBuffer buf) {
super(SimpleBufferAllocator.this, buf.capacity());
this.buf = buf;
buf.order(ByteOrder.BIG_ENDIAN);
}

protected SimpleBuffer(SimpleBuffer parent, ByteBuffer buf) {
super(parent);
this.buf = buf;
}

public ByteBuffer buf() {
return this.buf;
}

protected void buf(ByteBuffer buf) {
this.buf = buf;
}

protected IoBuffer duplicate0() {
return new SimpleBuffer(this, this.buf.duplicate());
}

protected IoBuffer slice0() {
return new SimpleBuffer(this, this.buf.slice());
}

protected IoBuffer asReadOnlyBuffer0() {
return new SimpleBuffer(this, this.buf.asReadOnlyBuffer());
}

public byte[] array() {
return this.buf.array();
}

public int arrayOffset() {
return this.buf.arrayOffset();
}

public boolean hasArray() {
return this.buf.hasArray();
}

public void free() {}
}
}

