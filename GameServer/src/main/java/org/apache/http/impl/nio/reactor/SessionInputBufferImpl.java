package org.apache.http.impl.nio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.MessageConstraintException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.ExpandableBuffer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.CharsetUtils;

@NotThreadSafe
public class SessionInputBufferImpl
extends ExpandableBuffer
implements SessionInputBuffer
{
private final CharsetDecoder chardecoder;
private final MessageConstraints constraints;
private final int lineBuffersize;
private CharBuffer charbuffer;

public SessionInputBufferImpl(int buffersize, int lineBuffersize, MessageConstraints constraints, CharsetDecoder chardecoder, ByteBufferAllocator allocator) {
super(buffersize, (allocator != null) ? allocator : (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
this.chardecoder = chardecoder;
}

public SessionInputBufferImpl(int buffersize, int lineBuffersize, CharsetDecoder chardecoder, ByteBufferAllocator allocator) {
this(buffersize, lineBuffersize, (MessageConstraints)null, chardecoder, allocator);
}

@Deprecated
public SessionInputBufferImpl(int buffersize, int lineBuffersize, ByteBufferAllocator allocator, HttpParams params) {
super(buffersize, allocator);
this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
String charsetName = (String)params.getParameter("http.protocol.element-charset");
Charset charset = CharsetUtils.lookup(charsetName);
if (charset != null) {
this.chardecoder = charset.newDecoder();
CodingErrorAction a1 = (CodingErrorAction)params.getParameter("http.malformed.input.action");

this.chardecoder.onMalformedInput((a1 != null) ? a1 : CodingErrorAction.REPORT);
CodingErrorAction a2 = (CodingErrorAction)params.getParameter("http.unmappable.input.action");

this.chardecoder.onUnmappableCharacter((a2 != null) ? a2 : CodingErrorAction.REPORT);
} else {
this.chardecoder = null;
} 
this.constraints = MessageConstraints.DEFAULT;
}

@Deprecated
public SessionInputBufferImpl(int buffersize, int linebuffersize, HttpParams params) {
this(buffersize, linebuffersize, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
}

public SessionInputBufferImpl(int buffersize, int lineBuffersize, Charset charset) {
this(buffersize, lineBuffersize, (MessageConstraints)null, (charset != null) ? charset.newDecoder() : null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
}

public SessionInputBufferImpl(int buffersize, int lineBuffersize, MessageConstraints constraints, Charset charset) {
this(buffersize, lineBuffersize, constraints, (charset != null) ? charset.newDecoder() : null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
}

public SessionInputBufferImpl(int buffersize, int lineBuffersize) {
this(buffersize, lineBuffersize, (MessageConstraints)null, (CharsetDecoder)null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
}

public SessionInputBufferImpl(int buffersize) {
this(buffersize, 256, (MessageConstraints)null, (CharsetDecoder)null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
}

public int fill(ReadableByteChannel channel) throws IOException {
Args.notNull(channel, "Channel");
setInputMode();
if (!this.buffer.hasRemaining()) {
expand();
}
return channel.read(this.buffer);
}

public int read() {
setOutputMode();
return this.buffer.get() & 0xFF;
}

public int read(ByteBuffer dst, int maxLen) {
if (dst == null) {
return 0;
}
setOutputMode();
int len = Math.min(dst.remaining(), maxLen);
int chunk = Math.min(this.buffer.remaining(), len);
if (this.buffer.remaining() > chunk) {
int oldLimit = this.buffer.limit();
int newLimit = this.buffer.position() + chunk;
this.buffer.limit(newLimit);
dst.put(this.buffer);
this.buffer.limit(oldLimit);
return len;
} 
dst.put(this.buffer);

return chunk;
}

public int read(ByteBuffer dst) {
if (dst == null) {
return 0;
}
return read(dst, dst.remaining());
}

public int read(WritableByteChannel dst, int maxLen) throws IOException {
int bytesRead;
if (dst == null) {
return 0;
}
setOutputMode();

if (this.buffer.remaining() > maxLen) {
int oldLimit = this.buffer.limit();
int newLimit = oldLimit - this.buffer.remaining() - maxLen;
this.buffer.limit(newLimit);
bytesRead = dst.write(this.buffer);
this.buffer.limit(oldLimit);
} else {
bytesRead = dst.write(this.buffer);
} 
return bytesRead;
}

public int read(WritableByteChannel dst) throws IOException {
if (dst == null) {
return 0;
}
setOutputMode();
return dst.write(this.buffer);
}

public boolean readLine(CharArrayBuffer linebuffer, boolean endOfStream) throws CharacterCodingException {
setOutputMode();

int pos = -1;
for (int i = this.buffer.position(); i < this.buffer.limit(); i++) {
int b = this.buffer.get(i);
if (b == 10) {
pos = i + 1;

break;
} 
} 
int maxLineLen = this.constraints.getMaxLineLength();
if (maxLineLen > 0) {
int currentLen = ((pos > 0) ? pos : this.buffer.limit()) - this.buffer.position();
if (currentLen >= maxLineLen) {
throw new MessageConstraintException("Maximum line length limit exceeded");
}
} 

if (pos == -1) {
if (endOfStream && this.buffer.hasRemaining()) {

pos = this.buffer.limit();
}
else {

return false;
} 
}
int origLimit = this.buffer.limit();
this.buffer.limit(pos);

int requiredCapacity = this.buffer.limit() - this.buffer.position();

linebuffer.ensureCapacity(requiredCapacity);

if (this.chardecoder == null) {
if (this.buffer.hasArray()) {
byte[] b = this.buffer.array();
int off = this.buffer.position();
int len = this.buffer.remaining();
linebuffer.append(b, off, len);
this.buffer.position(off + len);
} else {
while (this.buffer.hasRemaining())
linebuffer.append((char)(this.buffer.get() & 0xFF)); 
} 
} else {
CoderResult result;
if (this.charbuffer == null) {
this.charbuffer = CharBuffer.allocate(this.lineBuffersize);
}
this.chardecoder.reset();

do {
result = this.chardecoder.decode(this.buffer, this.charbuffer, true);

if (result.isError()) {
result.throwException();
}
if (!result.isOverflow())
continue;  this.charbuffer.flip();
linebuffer.append(this.charbuffer.array(), this.charbuffer.position(), this.charbuffer.remaining());

this.charbuffer.clear();
}
while (!result.isUnderflow());

this.chardecoder.flush(this.charbuffer);
this.charbuffer.flip();

if (this.charbuffer.hasRemaining()) {
linebuffer.append(this.charbuffer.array(), this.charbuffer.position(), this.charbuffer.remaining());
}
} 

this.buffer.limit(origLimit);

int l = linebuffer.length();
if (l > 0) {
if (linebuffer.charAt(l - 1) == '\n') {
l--;
linebuffer.setLength(l);
} 

if (l > 0 && 
linebuffer.charAt(l - 1) == '\r') {
l--;
linebuffer.setLength(l);
} 
} 

return true;
}

public String readLine(boolean endOfStream) throws CharacterCodingException {
CharArrayBuffer buffer = new CharArrayBuffer(64);
boolean found = readLine(buffer, endOfStream);
if (found) {
return buffer.toString();
}
return null;
}
}

