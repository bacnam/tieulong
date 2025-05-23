package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class SessionOutputBufferImpl
implements SessionOutputBuffer, BufferInfo
{
private static final byte[] CRLF = new byte[] { 13, 10 };

private final HttpTransportMetricsImpl metrics;

private final ByteArrayBuffer buffer;

private final int fragementSizeHint;

private final CharsetEncoder encoder;

private OutputStream outstream;

private ByteBuffer bbuf;

public SessionOutputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize, int fragementSizeHint, CharsetEncoder charencoder) {
Args.positive(buffersize, "Buffer size");
Args.notNull(metrics, "HTTP transport metrcis");
this.metrics = metrics;
this.buffer = new ByteArrayBuffer(buffersize);
this.fragementSizeHint = (fragementSizeHint >= 0) ? fragementSizeHint : 0;
this.encoder = charencoder;
}

public SessionOutputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize) {
this(metrics, buffersize, buffersize, null);
}

public void bind(OutputStream outstream) {
this.outstream = outstream;
}

public boolean isBound() {
return (this.outstream != null);
}

public int capacity() {
return this.buffer.capacity();
}

public int length() {
return this.buffer.length();
}

public int available() {
return capacity() - length();
}

private void streamWrite(byte[] b, int off, int len) throws IOException {
Asserts.notNull(this.outstream, "Output stream");
this.outstream.write(b, off, len);
}

private void flushStream() throws IOException {
if (this.outstream != null) {
this.outstream.flush();
}
}

private void flushBuffer() throws IOException {
int len = this.buffer.length();
if (len > 0) {
streamWrite(this.buffer.buffer(), 0, len);
this.buffer.clear();
this.metrics.incrementBytesTransferred(len);
} 
}

public void flush() throws IOException {
flushBuffer();
flushStream();
}

public void write(byte[] b, int off, int len) throws IOException {
if (b == null) {
return;
}

if (len > this.fragementSizeHint || len > this.buffer.capacity()) {

flushBuffer();

streamWrite(b, off, len);
this.metrics.incrementBytesTransferred(len);
} else {

int freecapacity = this.buffer.capacity() - this.buffer.length();
if (len > freecapacity)
{
flushBuffer();
}

this.buffer.append(b, off, len);
} 
}

public void write(byte[] b) throws IOException {
if (b == null) {
return;
}
write(b, 0, b.length);
}

public void write(int b) throws IOException {
if (this.fragementSizeHint > 0) {
if (this.buffer.isFull()) {
flushBuffer();
}
this.buffer.append(b);
} else {
flushBuffer();
this.outstream.write(b);
} 
}

public void writeLine(String s) throws IOException {
if (s == null) {
return;
}
if (s.length() > 0) {
if (this.encoder == null) {
for (int i = 0; i < s.length(); i++) {
write(s.charAt(i));
}
} else {
CharBuffer cbuf = CharBuffer.wrap(s);
writeEncoded(cbuf);
} 
}
write(CRLF);
}

public void writeLine(CharArrayBuffer charbuffer) throws IOException {
if (charbuffer == null) {
return;
}
if (this.encoder == null) {
int off = 0;
int remaining = charbuffer.length();
while (remaining > 0) {
int chunk = this.buffer.capacity() - this.buffer.length();
chunk = Math.min(chunk, remaining);
if (chunk > 0) {
this.buffer.append(charbuffer, off, chunk);
}
if (this.buffer.isFull()) {
flushBuffer();
}
off += chunk;
remaining -= chunk;
} 
} else {
CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
writeEncoded(cbuf);
} 
write(CRLF);
}

private void writeEncoded(CharBuffer cbuf) throws IOException {
if (!cbuf.hasRemaining()) {
return;
}
if (this.bbuf == null) {
this.bbuf = ByteBuffer.allocate(1024);
}
this.encoder.reset();
while (cbuf.hasRemaining()) {
CoderResult coderResult = this.encoder.encode(cbuf, this.bbuf, true);
handleEncodingResult(coderResult);
} 
CoderResult result = this.encoder.flush(this.bbuf);
handleEncodingResult(result);
this.bbuf.clear();
}

private void handleEncodingResult(CoderResult result) throws IOException {
if (result.isError()) {
result.throwException();
}
this.bbuf.flip();
while (this.bbuf.hasRemaining()) {
write(this.bbuf.get());
}
this.bbuf.compact();
}

public HttpTransportMetrics getMetrics() {
return this.metrics;
}
}

