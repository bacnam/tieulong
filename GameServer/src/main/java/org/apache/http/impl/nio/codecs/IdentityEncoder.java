package org.apache.http.impl.nio.codecs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.nio.FileContentEncoder;
import org.apache.http.nio.reactor.SessionOutputBuffer;

@NotThreadSafe
public class IdentityEncoder
extends AbstractContentEncoder
implements FileContentEncoder
{
private final int fragHint;

public IdentityEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics, int fragementSizeHint) {
super(channel, buffer, metrics);
this.fragHint = (fragementSizeHint > 0) ? fragementSizeHint : 0;
}

public IdentityEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics) {
this(channel, buffer, metrics, 0);
}

public int write(ByteBuffer src) throws IOException {
if (src == null) {
return 0;
}
assertNotCompleted();

int total = 0;
while (src.hasRemaining()) {
if ((this.buffer.hasData() || this.fragHint > 0) && 
src.remaining() <= this.fragHint) {
int capacity = this.fragHint - this.buffer.length();
if (capacity > 0) {
int limit = Math.min(capacity, src.remaining());
int bytesWritten = writeToBuffer(src, limit);
total += bytesWritten;
} 
} 

if (this.buffer.hasData() && (
this.buffer.length() >= this.fragHint || src.hasRemaining())) {
int bytesWritten = flushToChannel();
if (bytesWritten == 0) {
break;
}
} 

if (!this.buffer.hasData() && src.remaining() > this.fragHint) {
int bytesWritten = writeToChannel(src);
total += bytesWritten;
if (bytesWritten == 0) {
break;
}
} 
} 
return total;
}

public long transfer(FileChannel src, long position, long count) throws IOException {
if (src == null) {
return 0L;
}
assertNotCompleted();

flushToChannel();
if (this.buffer.hasData()) {
return 0L;
}

long bytesWritten = src.transferTo(position, count, this.channel);
if (bytesWritten > 0L) {
this.metrics.incrementBytesTransferred(bytesWritten);
}
return bytesWritten;
}

public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("[identity; completed: ");
sb.append(isCompleted());
sb.append("]");
return sb.toString();
}
}

