package org.apache.http.nio.util;

import java.io.IOException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.nio.ContentDecoder;

@NotThreadSafe
public class SimpleInputBuffer
extends ExpandableBuffer
implements ContentInputBuffer
{
private boolean endOfStream = false;

public SimpleInputBuffer(int buffersize, ByteBufferAllocator allocator) {
super(buffersize, allocator);
}

public SimpleInputBuffer(int buffersize) {
this(buffersize, HeapByteBufferAllocator.INSTANCE);
}

public void reset() {
this.endOfStream = false;
clear();
}

public int consumeContent(ContentDecoder decoder) throws IOException {
setInputMode();
int totalRead = 0;
int bytesRead;
while ((bytesRead = decoder.read(this.buffer)) != -1) {
if (bytesRead == 0) {
if (!this.buffer.hasRemaining()) {
expand();
continue;
} 
break;
} 
totalRead += bytesRead;
} 

if (bytesRead == -1 || decoder.isCompleted()) {
this.endOfStream = true;
}
return totalRead;
}

public boolean isEndOfStream() {
return (!hasData() && this.endOfStream);
}

public int read() throws IOException {
if (isEndOfStream()) {
return -1;
}
setOutputMode();
return this.buffer.get() & 0xFF;
}

public int read(byte[] b, int off, int len) throws IOException {
if (isEndOfStream()) {
return -1;
}
if (b == null) {
return 0;
}
setOutputMode();
int chunk = len;
if (chunk > this.buffer.remaining()) {
chunk = this.buffer.remaining();
}
this.buffer.get(b, off, chunk);
return chunk;
}

public int read(byte[] b) throws IOException {
if (isEndOfStream()) {
return -1;
}
if (b == null) {
return 0;
}
return read(b, 0, b.length);
}

public void shutdown() {
this.endOfStream = true;
}
}

