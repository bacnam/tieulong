package javolution.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;

public final class UTF8ByteBufferWriter
extends Writer
{
private ByteBuffer _byteBuffer;
private char _highSurrogate;

public UTF8ByteBufferWriter setOutput(ByteBuffer byteBuffer) {
if (this._byteBuffer != null)
throw new IllegalStateException("Writer not closed or reset"); 
this._byteBuffer = byteBuffer;
return this;
}

public void write(char c) throws IOException {
if (c < '?' || c > '?') {
write(c);
} else if (c < '?') {
this._highSurrogate = c;
} else {
int code = (this._highSurrogate - 55296 << 10) + c - 56320 + 65536;

write(code);
} 
}

public void write(int code) throws IOException {
if ((code & 0xFFFFFF80) == 0) {
this._byteBuffer.put((byte)code);
} else {
write2(code);
} 
}

private void write2(int c) throws IOException {
if ((c & 0xFFFFF800) == 0) {
this._byteBuffer.put((byte)(0xC0 | c >> 6));
this._byteBuffer.put((byte)(0x80 | c & 0x3F));
} else if ((c & 0xFFFF0000) == 0) {
this._byteBuffer.put((byte)(0xE0 | c >> 12));
this._byteBuffer.put((byte)(0x80 | c >> 6 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c & 0x3F));
} else if ((c & 0xFF200000) == 0) {
this._byteBuffer.put((byte)(0xF0 | c >> 18));
this._byteBuffer.put((byte)(0x80 | c >> 12 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c >> 6 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c & 0x3F));
} else if ((c & 0xF4000000) == 0) {
this._byteBuffer.put((byte)(0xF8 | c >> 24));
this._byteBuffer.put((byte)(0x80 | c >> 18 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c >> 12 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c >> 6 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c & 0x3F));
} else if ((c & Integer.MIN_VALUE) == 0) {
this._byteBuffer.put((byte)(0xFC | c >> 30));
this._byteBuffer.put((byte)(0x80 | c >> 24 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c >> 18 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c >> 12 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c >> 6 & 0x3F));
this._byteBuffer.put((byte)(0x80 | c & 0x3F));
} else {
throw new CharConversionException("Illegal character U+" + Integer.toHexString(c));
} 
}

public void write(char[] cbuf, int off, int len) throws IOException {
int off_plus_len = off + len;
for (int i = off; i < off_plus_len; ) {
char c = cbuf[i++];
if (c < '') {
this._byteBuffer.put((byte)c); continue;
} 
write(c);
} 
}

public void write(String str, int off, int len) throws IOException {
int off_plus_len = off + len;
for (int i = off; i < off_plus_len; ) {
char c = str.charAt(i++);
if (c < '') {
this._byteBuffer.put((byte)c); continue;
} 
write(c);
} 
}

public void write(CharSequence csq) throws IOException {
int length = csq.length();
for (int i = 0; i < length; ) {
char c = csq.charAt(i++);
if (c < '') {
this._byteBuffer.put((byte)c); continue;
} 
write(c);
} 
}

public void flush() throws IOException {
if (this._byteBuffer == null) throw new IOException("Writer closed");

}

public void close() throws IOException {
if (this._byteBuffer != null) {
reset();
}
}

public void reset() {
this._byteBuffer = null;
this._highSurrogate = Character.MIN_VALUE;
}

public UTF8ByteBufferWriter setByteBuffer(ByteBuffer byteBuffer) {
return setOutput(byteBuffer);
}
}

