package org.apache.mina.core.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CachedBufferAllocator
implements IoBufferAllocator
{
private static final int DEFAULT_MAX_POOL_SIZE = 8;
private static final int DEFAULT_MAX_CACHED_BUFFER_SIZE = 262144;
private final int maxPoolSize;
private final int maxCachedBufferSize;
private final ThreadLocal<Map<Integer, Queue<CachedBuffer>>> heapBuffers;
private final ThreadLocal<Map<Integer, Queue<CachedBuffer>>> directBuffers;

public CachedBufferAllocator() {
this(8, 262144);
}

public CachedBufferAllocator(int maxPoolSize, int maxCachedBufferSize) {
if (maxPoolSize < 0) {
throw new IllegalArgumentException("maxPoolSize: " + maxPoolSize);
}

if (maxCachedBufferSize < 0) {
throw new IllegalArgumentException("maxCachedBufferSize: " + maxCachedBufferSize);
}

this.maxPoolSize = maxPoolSize;
this.maxCachedBufferSize = maxCachedBufferSize;

this.heapBuffers = new ThreadLocal<Map<Integer, Queue<CachedBuffer>>>()
{
protected Map<Integer, Queue<CachedBufferAllocator.CachedBuffer>> initialValue() {
return CachedBufferAllocator.this.newPoolMap();
}
};

this.directBuffers = new ThreadLocal<Map<Integer, Queue<CachedBuffer>>>()
{
protected Map<Integer, Queue<CachedBufferAllocator.CachedBuffer>> initialValue() {
return CachedBufferAllocator.this.newPoolMap();
}
};
}

public int getMaxPoolSize() {
return this.maxPoolSize;
}

public int getMaxCachedBufferSize() {
return this.maxCachedBufferSize;
}

Map<Integer, Queue<CachedBuffer>> newPoolMap() {
Map<Integer, Queue<CachedBuffer>> poolMap = new HashMap<Integer, Queue<CachedBuffer>>();

for (int i = 0; i < 31; i++) {
poolMap.put(Integer.valueOf(1 << i), new ConcurrentLinkedQueue<CachedBuffer>());
}

poolMap.put(Integer.valueOf(0), new ConcurrentLinkedQueue<CachedBuffer>());
poolMap.put(Integer.valueOf(2147483647), new ConcurrentLinkedQueue<CachedBuffer>());

return poolMap;
}
public IoBuffer allocate(int requestedCapacity, boolean direct) {
IoBuffer buf;
int actualCapacity = IoBuffer.normalizeCapacity(requestedCapacity);

if (this.maxCachedBufferSize != 0 && actualCapacity > this.maxCachedBufferSize) {
if (direct) {
buf = wrap(ByteBuffer.allocateDirect(actualCapacity));
} else {
buf = wrap(ByteBuffer.allocate(actualCapacity));
} 
} else {
Queue<CachedBuffer> pool;

if (direct) {
pool = (Queue<CachedBuffer>)((Map)this.directBuffers.get()).get(Integer.valueOf(actualCapacity));
} else {
pool = (Queue<CachedBuffer>)((Map)this.heapBuffers.get()).get(Integer.valueOf(actualCapacity));
} 

buf = pool.poll();

if (buf != null) {
buf.clear();
buf.setAutoExpand(false);
buf.order(ByteOrder.BIG_ENDIAN);
}
else if (direct) {
buf = wrap(ByteBuffer.allocateDirect(actualCapacity));
} else {
buf = wrap(ByteBuffer.allocate(actualCapacity));
} 
} 

buf.limit(requestedCapacity);
return buf;
}

public ByteBuffer allocateNioBuffer(int capacity, boolean direct) {
return allocate(capacity, direct).buf();
}

public IoBuffer wrap(ByteBuffer nioBuffer) {
return new CachedBuffer(nioBuffer);
}

public void dispose() {}

private class CachedBuffer
extends AbstractIoBuffer
{
private final Thread ownerThread;
private ByteBuffer buf;

protected CachedBuffer(ByteBuffer buf) {
super(CachedBufferAllocator.this, buf.capacity());
this.ownerThread = Thread.currentThread();
this.buf = buf;
buf.order(ByteOrder.BIG_ENDIAN);
}

protected CachedBuffer(CachedBuffer parent, ByteBuffer buf) {
super(parent);
this.ownerThread = Thread.currentThread();
this.buf = buf;
}

public ByteBuffer buf() {
if (this.buf == null) {
throw new IllegalStateException("Buffer has been freed already.");
}
return this.buf;
}

protected void buf(ByteBuffer buf) {
ByteBuffer oldBuf = this.buf;
this.buf = buf;
free(oldBuf);
}

protected IoBuffer duplicate0() {
return new CachedBuffer(this, buf().duplicate());
}

protected IoBuffer slice0() {
return new CachedBuffer(this, buf().slice());
}

protected IoBuffer asReadOnlyBuffer0() {
return new CachedBuffer(this, buf().asReadOnlyBuffer());
}

public byte[] array() {
return buf().array();
}

public int arrayOffset() {
return buf().arrayOffset();
}

public boolean hasArray() {
return buf().hasArray();
}

public void free() {
free(this.buf);
this.buf = null;
}
private void free(ByteBuffer oldBuf) {
Queue<CachedBuffer> pool;
if (oldBuf == null || (CachedBufferAllocator.this.maxCachedBufferSize != 0 && oldBuf.capacity() > CachedBufferAllocator.this.maxCachedBufferSize) || oldBuf.isReadOnly() || isDerived() || Thread.currentThread() != this.ownerThread) {
return;
}

if (oldBuf.isDirect()) {
pool = (Queue<CachedBuffer>)((Map)CachedBufferAllocator.this.directBuffers.get()).get(Integer.valueOf(oldBuf.capacity()));
} else {
pool = (Queue<CachedBuffer>)((Map)CachedBufferAllocator.this.heapBuffers.get()).get(Integer.valueOf(oldBuf.capacity()));
} 

if (pool == null) {
return;
}

if (CachedBufferAllocator.this.maxPoolSize == 0 || pool.size() < CachedBufferAllocator.this.maxPoolSize)
pool.offer(new CachedBuffer(oldBuf)); 
}
}
}

