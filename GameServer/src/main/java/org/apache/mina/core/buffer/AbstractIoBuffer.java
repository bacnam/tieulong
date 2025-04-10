package org.apache.mina.core.buffer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.EnumSet;
import java.util.Set;

public abstract class AbstractIoBuffer
extends IoBuffer
{
private final boolean derived;
private boolean autoExpand;
private boolean autoShrink;
private boolean recapacityAllowed = true;
private int minimumCapacity;
private static final long BYTE_MASK = 255L;
private static final long SHORT_MASK = 65535L;
private static final long INT_MASK = 4294967295L;
private int mark = -1;

protected AbstractIoBuffer(IoBufferAllocator allocator, int initialCapacity) {
setAllocator(allocator);
this.recapacityAllowed = true;
this.derived = false;
this.minimumCapacity = initialCapacity;
}

protected AbstractIoBuffer(AbstractIoBuffer parent) {
setAllocator(getAllocator());
this.recapacityAllowed = false;
this.derived = true;
this.minimumCapacity = parent.minimumCapacity;
}

public final boolean isDirect() {
return buf().isDirect();
}

public final boolean isReadOnly() {
return buf().isReadOnly();
}

public final int minimumCapacity() {
return this.minimumCapacity;
}

public final IoBuffer minimumCapacity(int minimumCapacity) {
if (minimumCapacity < 0) {
throw new IllegalArgumentException("minimumCapacity: " + minimumCapacity);
}
this.minimumCapacity = minimumCapacity;
return this;
}

public final int capacity() {
return buf().capacity();
}

public final IoBuffer capacity(int newCapacity) {
if (!this.recapacityAllowed) {
throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
}

if (newCapacity > capacity()) {

int pos = position();
int limit = limit();
ByteOrder bo = order();

ByteBuffer oldBuf = buf();
ByteBuffer newBuf = getAllocator().allocateNioBuffer(newCapacity, isDirect());
oldBuf.clear();
newBuf.put(oldBuf);
buf(newBuf);

buf().limit(limit);
if (this.mark >= 0) {
buf().position(this.mark);
buf().mark();
} 
buf().position(pos);
buf().order(bo);
} 

return this;
}

public final boolean isAutoExpand() {
return (this.autoExpand && this.recapacityAllowed);
}

public final boolean isAutoShrink() {
return (this.autoShrink && this.recapacityAllowed);
}

public final boolean isDerived() {
return this.derived;
}

public final IoBuffer setAutoExpand(boolean autoExpand) {
if (!this.recapacityAllowed) {
throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
}
this.autoExpand = autoExpand;
return this;
}

public final IoBuffer setAutoShrink(boolean autoShrink) {
if (!this.recapacityAllowed) {
throw new IllegalStateException("Derived buffers and their parent can't be shrinked.");
}
this.autoShrink = autoShrink;
return this;
}

public final IoBuffer expand(int expectedRemaining) {
return expand(position(), expectedRemaining, false);
}

private IoBuffer expand(int expectedRemaining, boolean autoExpand) {
return expand(position(), expectedRemaining, autoExpand);
}

public final IoBuffer expand(int pos, int expectedRemaining) {
return expand(pos, expectedRemaining, false);
}
private IoBuffer expand(int pos, int expectedRemaining, boolean autoExpand) {
int newCapacity;
if (!this.recapacityAllowed) {
throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
}

int end = pos + expectedRemaining;

if (autoExpand) {
newCapacity = IoBuffer.normalizeCapacity(end);
} else {
newCapacity = end;
} 
if (newCapacity > capacity())
{
capacity(newCapacity);
}

if (end > limit())
{
buf().limit(end);
}
return this;
}

public final IoBuffer shrink() {
if (!this.recapacityAllowed) {
throw new IllegalStateException("Derived buffers and their parent can't be expanded.");
}

int position = position();
int capacity = capacity();
int limit = limit();

if (capacity == limit) {
return this;
}

int newCapacity = capacity;
int minCapacity = Math.max(this.minimumCapacity, limit);

while (newCapacity >>> 1 >= minCapacity) {

newCapacity >>>= 1;

if (minCapacity == 0) {
break;
}
} 

newCapacity = Math.max(minCapacity, newCapacity);

if (newCapacity == capacity) {
return this;
}

ByteOrder bo = order();

ByteBuffer oldBuf = buf();
ByteBuffer newBuf = getAllocator().allocateNioBuffer(newCapacity, isDirect());
oldBuf.position(0);
oldBuf.limit(limit);
newBuf.put(oldBuf);
buf(newBuf);

buf().position(position);
buf().limit(limit);
buf().order(bo);
this.mark = -1;

return this;
}

public final int position() {
return buf().position();
}

public final IoBuffer position(int newPosition) {
autoExpand(newPosition, 0);
buf().position(newPosition);
if (this.mark > newPosition) {
this.mark = -1;
}
return this;
}

public final int limit() {
return buf().limit();
}

public final IoBuffer limit(int newLimit) {
autoExpand(newLimit, 0);
buf().limit(newLimit);
if (this.mark > newLimit) {
this.mark = -1;
}
return this;
}

public final IoBuffer mark() {
ByteBuffer byteBuffer = buf();
byteBuffer.mark();
this.mark = byteBuffer.position();

return this;
}

public final int markValue() {
return this.mark;
}

public final IoBuffer reset() {
buf().reset();
return this;
}

public final IoBuffer clear() {
buf().clear();
this.mark = -1;
return this;
}

public final IoBuffer sweep() {
clear();
return fillAndReset(remaining());
}

public final IoBuffer sweep(byte value) {
clear();
return fillAndReset(value, remaining());
}

public final IoBuffer flip() {
buf().flip();
this.mark = -1;
return this;
}

public final IoBuffer rewind() {
buf().rewind();
this.mark = -1;
return this;
}

public final int remaining() {
ByteBuffer byteBuffer = buf();

return byteBuffer.limit() - byteBuffer.position();
}

public final boolean hasRemaining() {
ByteBuffer byteBuffer = buf();

return (byteBuffer.limit() > byteBuffer.position());
}

public final byte get() {
return buf().get();
}

public final short getUnsigned() {
return (short)(get() & 0xFF);
}

public final IoBuffer put(byte b) {
autoExpand(1);
buf().put(b);
return this;
}

public IoBuffer putUnsigned(byte value) {
autoExpand(1);
buf().put((byte)(value & 0xFF));
return this;
}

public IoBuffer putUnsigned(int index, byte value) {
autoExpand(index, 1);
buf().put(index, (byte)(value & 0xFF));
return this;
}

public IoBuffer putUnsigned(short value) {
autoExpand(1);
buf().put((byte)(value & 0xFF));
return this;
}

public IoBuffer putUnsigned(int index, short value) {
autoExpand(index, 1);
buf().put(index, (byte)(value & 0xFF));
return this;
}

public IoBuffer putUnsigned(int value) {
autoExpand(1);
buf().put((byte)(value & 0xFF));
return this;
}

public IoBuffer putUnsigned(int index, int value) {
autoExpand(index, 1);
buf().put(index, (byte)(value & 0xFF));
return this;
}

public IoBuffer putUnsigned(long value) {
autoExpand(1);
buf().put((byte)(int)(value & 0xFFL));
return this;
}

public IoBuffer putUnsigned(int index, long value) {
autoExpand(index, 1);
buf().put(index, (byte)(int)(value & 0xFFL));
return this;
}

public final byte get(int index) {
return buf().get(index);
}

public final short getUnsigned(int index) {
return (short)(get(index) & 0xFF);
}

public final IoBuffer put(int index, byte b) {
autoExpand(index, 1);
buf().put(index, b);
return this;
}

public final IoBuffer get(byte[] dst, int offset, int length) {
buf().get(dst, offset, length);
return this;
}

public final IoBuffer put(ByteBuffer src) {
autoExpand(src.remaining());
buf().put(src);
return this;
}

public final IoBuffer put(byte[] src, int offset, int length) {
autoExpand(length);
buf().put(src, offset, length);
return this;
}

public final IoBuffer compact() {
int remaining = remaining();
int capacity = capacity();

if (capacity == 0) {
return this;
}

if (isAutoShrink() && remaining <= capacity >>> 2 && capacity > this.minimumCapacity) {
int newCapacity = capacity;
int minCapacity = Math.max(this.minimumCapacity, remaining << 1);

while (newCapacity >>> 1 >= minCapacity)
{

newCapacity >>>= 1;
}

newCapacity = Math.max(minCapacity, newCapacity);

if (newCapacity == capacity) {
return this;
}

ByteOrder bo = order();

if (remaining > newCapacity) {
throw new IllegalStateException("The amount of the remaining bytes is greater than the new capacity.");
}

ByteBuffer oldBuf = buf();
ByteBuffer newBuf = getAllocator().allocateNioBuffer(newCapacity, isDirect());
newBuf.put(oldBuf);
buf(newBuf);

buf().order(bo);
} else {
buf().compact();
} 
this.mark = -1;
return this;
}

public final ByteOrder order() {
return buf().order();
}

public final IoBuffer order(ByteOrder bo) {
buf().order(bo);
return this;
}

public final char getChar() {
return buf().getChar();
}

public final IoBuffer putChar(char value) {
autoExpand(2);
buf().putChar(value);
return this;
}

public final char getChar(int index) {
return buf().getChar(index);
}

public final IoBuffer putChar(int index, char value) {
autoExpand(index, 2);
buf().putChar(index, value);
return this;
}

public final CharBuffer asCharBuffer() {
return buf().asCharBuffer();
}

public final short getShort() {
return buf().getShort();
}

public final IoBuffer putShort(short value) {
autoExpand(2);
buf().putShort(value);
return this;
}

public final short getShort(int index) {
return buf().getShort(index);
}

public final IoBuffer putShort(int index, short value) {
autoExpand(index, 2);
buf().putShort(index, value);
return this;
}

public final ShortBuffer asShortBuffer() {
return buf().asShortBuffer();
}

public final int getInt() {
return buf().getInt();
}

public final IoBuffer putInt(int value) {
autoExpand(4);
buf().putInt(value);
return this;
}

public final IoBuffer putUnsignedInt(byte value) {
autoExpand(4);
buf().putInt(value & 0xFF);
return this;
}

public final IoBuffer putUnsignedInt(int index, byte value) {
autoExpand(index, 4);
buf().putInt(index, value & 0xFF);
return this;
}

public final IoBuffer putUnsignedInt(short value) {
autoExpand(4);
buf().putInt(value & 0xFFFF);
return this;
}

public final IoBuffer putUnsignedInt(int index, short value) {
autoExpand(index, 4);
buf().putInt(index, value & 0xFFFF);
return this;
}

public final IoBuffer putUnsignedInt(int value) {
autoExpand(4);
buf().putInt(value);
return this;
}

public final IoBuffer putUnsignedInt(int index, int value) {
autoExpand(index, 4);
buf().putInt(index, value);
return this;
}

public final IoBuffer putUnsignedInt(long value) {
autoExpand(4);
buf().putInt((int)(value & 0xFFFFFFFFFFFFFFFFL));
return this;
}

public final IoBuffer putUnsignedInt(int index, long value) {
autoExpand(index, 4);
buf().putInt(index, (int)(value & 0xFFFFFFFFL));
return this;
}

public final IoBuffer putUnsignedShort(byte value) {
autoExpand(2);
buf().putShort((short)(value & 0xFF));
return this;
}

public final IoBuffer putUnsignedShort(int index, byte value) {
autoExpand(index, 2);
buf().putShort(index, (short)(value & 0xFF));
return this;
}

public final IoBuffer putUnsignedShort(short value) {
autoExpand(2);
buf().putShort(value);
return this;
}

public final IoBuffer putUnsignedShort(int index, short value) {
autoExpand(index, 2);
buf().putShort(index, value);
return this;
}

public final IoBuffer putUnsignedShort(int value) {
autoExpand(2);
buf().putShort((short)value);
return this;
}

public final IoBuffer putUnsignedShort(int index, int value) {
autoExpand(index, 2);
buf().putShort(index, (short)value);
return this;
}

public final IoBuffer putUnsignedShort(long value) {
autoExpand(2);
buf().putShort((short)(int)value);
return this;
}

public final IoBuffer putUnsignedShort(int index, long value) {
autoExpand(index, 2);
buf().putShort(index, (short)(int)value);
return this;
}

public final int getInt(int index) {
return buf().getInt(index);
}

public final IoBuffer putInt(int index, int value) {
autoExpand(index, 4);
buf().putInt(index, value);
return this;
}

public final IntBuffer asIntBuffer() {
return buf().asIntBuffer();
}

public final long getLong() {
return buf().getLong();
}

public final IoBuffer putLong(long value) {
autoExpand(8);
buf().putLong(value);
return this;
}

public final long getLong(int index) {
return buf().getLong(index);
}

public final IoBuffer putLong(int index, long value) {
autoExpand(index, 8);
buf().putLong(index, value);
return this;
}

public final LongBuffer asLongBuffer() {
return buf().asLongBuffer();
}

public final float getFloat() {
return buf().getFloat();
}

public final IoBuffer putFloat(float value) {
autoExpand(4);
buf().putFloat(value);
return this;
}

public final float getFloat(int index) {
return buf().getFloat(index);
}

public final IoBuffer putFloat(int index, float value) {
autoExpand(index, 4);
buf().putFloat(index, value);
return this;
}

public final FloatBuffer asFloatBuffer() {
return buf().asFloatBuffer();
}

public final double getDouble() {
return buf().getDouble();
}

public final IoBuffer putDouble(double value) {
autoExpand(8);
buf().putDouble(value);
return this;
}

public final double getDouble(int index) {
return buf().getDouble(index);
}

public final IoBuffer putDouble(int index, double value) {
autoExpand(index, 8);
buf().putDouble(index, value);
return this;
}

public final DoubleBuffer asDoubleBuffer() {
return buf().asDoubleBuffer();
}

public final IoBuffer asReadOnlyBuffer() {
this.recapacityAllowed = false;
return asReadOnlyBuffer0();
}

public final IoBuffer duplicate() {
this.recapacityAllowed = false;
return duplicate0();
}

public final IoBuffer slice() {
this.recapacityAllowed = false;
return slice0();
}

public final IoBuffer getSlice(int index, int length) {
if (length < 0) {
throw new IllegalArgumentException("length: " + length);
}

int pos = position();
int limit = limit();

if (index > limit) {
throw new IllegalArgumentException("index: " + index);
}

int endIndex = index + length;

if (endIndex > limit) {
throw new IndexOutOfBoundsException("index + length (" + endIndex + ") is greater " + "than limit (" + limit + ").");
}

clear();
limit(endIndex);
position(index);

IoBuffer slice = slice();
limit(limit);
position(pos);

return slice;
}

public final IoBuffer getSlice(int length) {
if (length < 0) {
throw new IllegalArgumentException("length: " + length);
}
int pos = position();
int limit = limit();
int nextPos = pos + length;
if (limit < nextPos) {
throw new IndexOutOfBoundsException("position + length (" + nextPos + ") is greater " + "than limit (" + limit + ").");
}

limit(pos + length);
IoBuffer slice = slice();
position(nextPos);
limit(limit);
return slice;
}

public int hashCode() {
int h = 1;
int p = position();
for (int i = limit() - 1; i >= p; i--) {
h = 31 * h + get(i);
}
return h;
}

public boolean equals(Object o) {
if (!(o instanceof IoBuffer)) {
return false;
}

IoBuffer that = (IoBuffer)o;
if (remaining() != that.remaining()) {
return false;
}

int p = position();
for (int i = limit() - 1, j = that.limit() - 1; i >= p; i--, j--) {
byte v1 = get(i);
byte v2 = that.get(j);
if (v1 != v2) {
return false;
}
} 
return true;
}

public int compareTo(IoBuffer that) {
int n = position() + Math.min(remaining(), that.remaining());
for (int i = position(), j = that.position(); i < n; ) {
byte v1 = get(i);
byte v2 = that.get(j);
if (v1 == v2) {
i++; j++; continue;
} 
if (v1 < v2) {
return -1;
}

return 1;
} 
return remaining() - that.remaining();
}

public String toString() {
StringBuilder buf = new StringBuilder();
if (isDirect()) {
buf.append("DirectBuffer");
} else {
buf.append("HeapBuffer");
} 
buf.append("[pos=");
buf.append(position());
buf.append(" lim=");
buf.append(limit());
buf.append(" cap=");
buf.append(capacity());
buf.append(": ");
buf.append(getHexDump(16));
buf.append(']');
return buf.toString();
}

public IoBuffer get(byte[] dst) {
return get(dst, 0, dst.length);
}

public IoBuffer put(IoBuffer src) {
return put(src.buf());
}

public IoBuffer put(byte[] src) {
return put(src, 0, src.length);
}

public int getUnsignedShort() {
return getShort() & 0xFFFF;
}

public int getUnsignedShort(int index) {
return getShort(index) & 0xFFFF;
}

public long getUnsignedInt() {
return getInt() & 0xFFFFFFFFL;
}

public int getMediumInt() {
byte b1 = get();
byte b2 = get();
byte b3 = get();
if (ByteOrder.BIG_ENDIAN.equals(order())) {
return getMediumInt(b1, b2, b3);
}

return getMediumInt(b3, b2, b1);
}

public int getUnsignedMediumInt() {
int b1 = getUnsigned();
int b2 = getUnsigned();
int b3 = getUnsigned();
if (ByteOrder.BIG_ENDIAN.equals(order())) {
return b1 << 16 | b2 << 8 | b3;
}

return b3 << 16 | b2 << 8 | b1;
}

public int getMediumInt(int index) {
byte b1 = get(index);
byte b2 = get(index + 1);
byte b3 = get(index + 2);
if (ByteOrder.BIG_ENDIAN.equals(order())) {
return getMediumInt(b1, b2, b3);
}

return getMediumInt(b3, b2, b1);
}

public int getUnsignedMediumInt(int index) {
int b1 = getUnsigned(index);
int b2 = getUnsigned(index + 1);
int b3 = getUnsigned(index + 2);
if (ByteOrder.BIG_ENDIAN.equals(order())) {
return b1 << 16 | b2 << 8 | b3;
}

return b3 << 16 | b2 << 8 | b1;
}

private int getMediumInt(byte b1, byte b2, byte b3) {
int ret = b1 << 16 & 0xFF0000 | b2 << 8 & 0xFF00 | b3 & 0xFF;

if ((b1 & 0x80) == 128)
{
ret |= 0xFF000000;
}
return ret;
}

public IoBuffer putMediumInt(int value) {
byte b1 = (byte)(value >> 16);
byte b2 = (byte)(value >> 8);
byte b3 = (byte)value;

if (ByteOrder.BIG_ENDIAN.equals(order())) {
put(b1).put(b2).put(b3);
} else {
put(b3).put(b2).put(b1);
} 

return this;
}

public IoBuffer putMediumInt(int index, int value) {
byte b1 = (byte)(value >> 16);
byte b2 = (byte)(value >> 8);
byte b3 = (byte)value;

if (ByteOrder.BIG_ENDIAN.equals(order())) {
put(index, b1).put(index + 1, b2).put(index + 2, b3);
} else {
put(index, b3).put(index + 1, b2).put(index + 2, b1);
} 

return this;
}

public long getUnsignedInt(int index) {
return getInt(index) & 0xFFFFFFFFL;
}

public InputStream asInputStream() {
return new InputStream()
{
public int available() {
return AbstractIoBuffer.this.remaining();
}

public synchronized void mark(int readlimit) {
AbstractIoBuffer.this.mark();
}

public boolean markSupported() {
return true;
}

public int read() {
if (AbstractIoBuffer.this.hasRemaining()) {
return AbstractIoBuffer.this.get() & 0xFF;
}

return -1;
}

public int read(byte[] b, int off, int len) {
int remaining = AbstractIoBuffer.this.remaining();
if (remaining > 0) {
int readBytes = Math.min(remaining, len);
AbstractIoBuffer.this.get(b, off, readBytes);
return readBytes;
} 

return -1;
}

public synchronized void reset() {
AbstractIoBuffer.this.reset();
}

public long skip(long n) {
int bytes;
if (n > 2147483647L) {
bytes = AbstractIoBuffer.this.remaining();
} else {
bytes = Math.min(AbstractIoBuffer.this.remaining(), (int)n);
} 
AbstractIoBuffer.this.skip(bytes);
return bytes;
}
};
}

public OutputStream asOutputStream() {
return new OutputStream()
{
public void write(byte[] b, int off, int len) {
AbstractIoBuffer.this.put(b, off, len);
}

public void write(int b) {
AbstractIoBuffer.this.put((byte)b);
}
};
}

public String getHexDump() {
return getHexDump(2147483647);
}

public String getHexDump(int lengthLimit) {
return IoBufferHexDumper.getHexdump(this, lengthLimit);
}

public String getString(CharsetDecoder decoder) throws CharacterCodingException {
int newPos;
if (!hasRemaining()) {
return "";
}

boolean utf16 = decoder.charset().name().startsWith("UTF-16");

int oldPos = position();
int oldLimit = limit();
int end = -1;

if (!utf16) {
end = indexOf((byte)0);
if (end < 0) {
newPos = end = oldLimit;
} else {
newPos = end + 1;
} 
} else {
int i = oldPos;
while (true) {
boolean wasZero = (get(i) == 0);
i++;

if (i >= oldLimit) {
break;
}

if (get(i) != 0) {
i++;
if (i >= oldLimit) {
break;
}

continue;
} 

if (wasZero) {
end = i - 1;

break;
} 
} 
if (end < 0) {
newPos = end = oldPos + (oldLimit - oldPos & 0xFFFFFFFE);
}
else if (end + 2 <= oldLimit) {
newPos = end + 2;
} else {
newPos = end;
} 
} 

if (oldPos == end) {
position(newPos);
return "";
} 

limit(end);
decoder.reset();

int expectedLength = (int)(remaining() * decoder.averageCharsPerByte()) + 1;
CharBuffer out = CharBuffer.allocate(expectedLength);
while (true) {
CoderResult cr;
if (hasRemaining()) {
cr = decoder.decode(buf(), out, true);
} else {
cr = decoder.flush(out);
} 

if (cr.isUnderflow()) {
break;
}

if (cr.isOverflow()) {
CharBuffer o = CharBuffer.allocate(out.capacity() + expectedLength);
out.flip();
o.put(out);
out = o;

continue;
} 
if (cr.isError()) {

limit(oldLimit);
position(oldPos);
cr.throwException();
} 
} 

limit(oldLimit);
position(newPos);
return out.flip().toString();
}

public String getString(int fieldSize, CharsetDecoder decoder) throws CharacterCodingException {
checkFieldSize(fieldSize);

if (fieldSize == 0) {
return "";
}

if (!hasRemaining()) {
return "";
}

boolean utf16 = decoder.charset().name().startsWith("UTF-16");

if (utf16 && (fieldSize & 0x1) != 0) {
throw new IllegalArgumentException("fieldSize is not even.");
}

int oldPos = position();
int oldLimit = limit();
int end = oldPos + fieldSize;

if (oldLimit < end) {
throw new BufferUnderflowException();
}

if (!utf16) {
int i; for (i = oldPos; i < end && 
get(i) != 0; i++);

if (i == end) {
limit(end);
} else {
limit(i);
} 
} else {
int i; for (i = oldPos; i < end && (
get(i) != 0 || get(i + 1) != 0); i += 2);

if (i == end) {
limit(end);
} else {
limit(i);
} 
} 

if (!hasRemaining()) {
limit(oldLimit);
position(end);
return "";
} 
decoder.reset();

int expectedLength = (int)(remaining() * decoder.averageCharsPerByte()) + 1;
CharBuffer out = CharBuffer.allocate(expectedLength);
while (true) {
CoderResult cr;
if (hasRemaining()) {
cr = decoder.decode(buf(), out, true);
} else {
cr = decoder.flush(out);
} 

if (cr.isUnderflow()) {
break;
}

if (cr.isOverflow()) {
CharBuffer o = CharBuffer.allocate(out.capacity() + expectedLength);
out.flip();
o.put(out);
out = o;

continue;
} 
if (cr.isError()) {

limit(oldLimit);
position(oldPos);
cr.throwException();
} 
} 

limit(oldLimit);
position(end);
return out.flip().toString();
}

public IoBuffer putString(CharSequence val, CharsetEncoder encoder) throws CharacterCodingException {
if (val.length() == 0) {
return this;
}

CharBuffer in = CharBuffer.wrap(val);
encoder.reset();

int expandedState = 0;

while (true) {
CoderResult cr;
if (in.hasRemaining()) {
cr = encoder.encode(in, buf(), true);
} else {
cr = encoder.flush(buf());
} 

if (cr.isUnderflow()) {
break;
}
if (cr.isOverflow()) {
if (isAutoExpand()) {
switch (expandedState) {
case 0:
autoExpand((int)Math.ceil((in.remaining() * encoder.averageBytesPerChar())));
expandedState++;
continue;
case 1:
autoExpand((int)Math.ceil((in.remaining() * encoder.maxBytesPerChar())));
expandedState++;
continue;
} 
throw new RuntimeException("Expanded by " + (int)Math.ceil((in.remaining() * encoder.maxBytesPerChar())) + " but that wasn't enough for '" + val + "'");

}

}
else {

expandedState = 0;
} 
cr.throwException();
} 
return this;
}

public IoBuffer putString(CharSequence val, int fieldSize, CharsetEncoder encoder) throws CharacterCodingException {
checkFieldSize(fieldSize);

if (fieldSize == 0) {
return this;
}

autoExpand(fieldSize);

boolean utf16 = encoder.charset().name().startsWith("UTF-16");

if (utf16 && (fieldSize & 0x1) != 0) {
throw new IllegalArgumentException("fieldSize is not even.");
}

int oldLimit = limit();
int end = position() + fieldSize;

if (oldLimit < end) {
throw new BufferOverflowException();
}

if (val.length() == 0) {
if (!utf16) {
put((byte)0);
} else {
put((byte)0);
put((byte)0);
} 
position(end);
return this;
} 

CharBuffer in = CharBuffer.wrap(val);
limit(end);
encoder.reset();

while (true) {
CoderResult cr;
if (in.hasRemaining()) {
cr = encoder.encode(in, buf(), true);
} else {
cr = encoder.flush(buf());
} 

if (cr.isUnderflow() || cr.isOverflow()) {
break;
}
cr.throwException();
} 

limit(oldLimit);

if (position() < end) {
if (!utf16) {
put((byte)0);
} else {
put((byte)0);
put((byte)0);
} 
}

position(end);
return this;
}

public String getPrefixedString(CharsetDecoder decoder) throws CharacterCodingException {
return getPrefixedString(2, decoder);
}

public String getPrefixedString(int prefixLength, CharsetDecoder decoder) throws CharacterCodingException {
if (!prefixedDataAvailable(prefixLength)) {
throw new BufferUnderflowException();
}

int fieldSize = 0;

switch (prefixLength) {
case 1:
fieldSize = getUnsigned();
break;
case 2:
fieldSize = getUnsignedShort();
break;
case 4:
fieldSize = getInt();
break;
} 

if (fieldSize == 0) {
return "";
}

boolean utf16 = decoder.charset().name().startsWith("UTF-16");

if (utf16 && (fieldSize & 0x1) != 0) {
throw new BufferDataException("fieldSize is not even for a UTF-16 string.");
}

int oldLimit = limit();
int end = position() + fieldSize;

if (oldLimit < end) {
throw new BufferUnderflowException();
}

limit(end);
decoder.reset();

int expectedLength = (int)(remaining() * decoder.averageCharsPerByte()) + 1;
CharBuffer out = CharBuffer.allocate(expectedLength);
while (true) {
CoderResult cr;
if (hasRemaining()) {
cr = decoder.decode(buf(), out, true);
} else {
cr = decoder.flush(out);
} 

if (cr.isUnderflow()) {
break;
}

if (cr.isOverflow()) {
CharBuffer o = CharBuffer.allocate(out.capacity() + expectedLength);
out.flip();
o.put(out);
out = o;

continue;
} 
cr.throwException();
} 

limit(oldLimit);
position(end);
return out.flip().toString();
}

public IoBuffer putPrefixedString(CharSequence in, CharsetEncoder encoder) throws CharacterCodingException {
return putPrefixedString(in, 2, 0, encoder);
}

public IoBuffer putPrefixedString(CharSequence in, int prefixLength, CharsetEncoder encoder) throws CharacterCodingException {
return putPrefixedString(in, prefixLength, 0, encoder);
}

public IoBuffer putPrefixedString(CharSequence in, int prefixLength, int padding, CharsetEncoder encoder) throws CharacterCodingException {
return putPrefixedString(in, prefixLength, padding, (byte)0, encoder);
}

public IoBuffer putPrefixedString(CharSequence val, int prefixLength, int padding, byte padValue, CharsetEncoder encoder) throws CharacterCodingException {
int maxLength, padMask;
switch (prefixLength) {
case 1:
maxLength = 255;
break;
case 2:
maxLength = 65535;
break;
case 4:
maxLength = Integer.MAX_VALUE;
break;
default:
throw new IllegalArgumentException("prefixLength: " + prefixLength);
} 

if (val.length() > maxLength) {
throw new IllegalArgumentException("The specified string is too long.");
}
if (val.length() == 0) {
switch (prefixLength) {
case 1:
put((byte)0);
break;
case 2:
putShort((short)0);
break;
case 4:
putInt(0);
break;
} 
return this;
} 

switch (padding) {
case 0:
case 1:
padMask = 0;
break;
case 2:
padMask = 1;
break;
case 4:
padMask = 3;
break;
default:
throw new IllegalArgumentException("padding: " + padding);
} 

CharBuffer in = CharBuffer.wrap(val);
skip(prefixLength);
int oldPos = position();
encoder.reset();

int expandedState = 0;

while (true) {
CoderResult cr;
if (in.hasRemaining()) {
cr = encoder.encode(in, buf(), true);
} else {
cr = encoder.flush(buf());
} 

if (position() - oldPos > maxLength) {
throw new IllegalArgumentException("The specified string is too long.");
}

if (cr.isUnderflow()) {
break;
}
if (cr.isOverflow()) {
if (isAutoExpand()) {
switch (expandedState) {
case 0:
autoExpand((int)Math.ceil((in.remaining() * encoder.averageBytesPerChar())));
expandedState++;
continue;
case 1:
autoExpand((int)Math.ceil((in.remaining() * encoder.maxBytesPerChar())));
expandedState++;
continue;
} 
throw new RuntimeException("Expanded by " + (int)Math.ceil((in.remaining() * encoder.maxBytesPerChar())) + " but that wasn't enough for '" + val + "'");

}

}
else {

expandedState = 0;
} 
cr.throwException();
} 

fill(padValue, padding - (position() - oldPos & padMask));
int length = position() - oldPos;
switch (prefixLength) {
case 1:
put(oldPos - 1, (byte)length);
break;
case 2:
putShort(oldPos - 2, (short)length);
break;
case 4:
putInt(oldPos - 4, length);
break;
} 
return this;
}

public Object getObject() throws ClassNotFoundException {
return getObject(Thread.currentThread().getContextClassLoader());
}

public Object getObject(final ClassLoader classLoader) throws ClassNotFoundException {
if (!prefixedDataAvailable(4)) {
throw new BufferUnderflowException();
}

int length = getInt();
if (length <= 4) {
throw new BufferDataException("Object length should be greater than 4: " + length);
}

int oldLimit = limit();
limit(position() + length);
try {
ObjectInputStream in = new ObjectInputStream(asInputStream()) { protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
String className;
Class<?> clazz;
int type = read();
if (type < 0) {
throw new EOFException();
}
switch (type) {
case 0:
return super.readClassDescriptor();
case 1:
className = readUTF();
clazz = Class.forName(className, true, classLoader);
return ObjectStreamClass.lookup(clazz);
} 
throw new StreamCorruptedException("Unexpected class descriptor type: " + type);
}

protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
Class<?> clazz = desc.forClass();

if (clazz == null) {
String name = desc.getName();
try {
return Class.forName(name, false, classLoader);
} catch (ClassNotFoundException ex) {
return super.resolveClass(desc);
} 
} 
return clazz;
} }
;

return in.readObject();
} catch (IOException e) {
throw new BufferDataException(e);
} finally {
limit(oldLimit);
} 
}

public IoBuffer putObject(Object o) {
int oldPos = position();
skip(4);
try {
ObjectOutputStream out = new ObjectOutputStream(asOutputStream())
{
protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
Class<?> clazz = desc.forClass();

if (clazz.isArray() || clazz.isPrimitive() || !Serializable.class.isAssignableFrom(clazz)) {
write(0);
super.writeClassDescriptor(desc);
} else {

write(1);
writeUTF(desc.getName());
} 
}
};
out.writeObject(o);
out.flush();
} catch (IOException e) {
throw new BufferDataException(e);
} 

int newPos = position();
position(oldPos);
putInt(newPos - oldPos - 4);
position(newPos);
return this;
}

public boolean prefixedDataAvailable(int prefixLength) {
return prefixedDataAvailable(prefixLength, 2147483647);
}

public boolean prefixedDataAvailable(int prefixLength, int maxDataLength) {
int dataLength;
if (remaining() < prefixLength) {
return false;
}

switch (prefixLength) {
case 1:
dataLength = getUnsigned(position());
break;
case 2:
dataLength = getUnsignedShort(position());
break;
case 4:
dataLength = getInt(position());
break;
default:
throw new IllegalArgumentException("prefixLength: " + prefixLength);
} 

if (dataLength < 0 || dataLength > maxDataLength) {
throw new BufferDataException("dataLength: " + dataLength);
}

return (remaining() - prefixLength >= dataLength);
}

public int indexOf(byte b) {
if (hasArray()) {
int arrayOffset = arrayOffset();
int beginPos = arrayOffset + position();
int limit = arrayOffset + limit();
byte[] array = array();

for (int i = beginPos; i < limit; i++) {
if (array[i] == b) {
return i - arrayOffset;
}
} 
} else {
int beginPos = position();
int limit = limit();

for (int i = beginPos; i < limit; i++) {
if (get(i) == b) {
return i;
}
} 
} 

return -1;
}

public IoBuffer skip(int size) {
autoExpand(size);
return position(position() + size);
}

public IoBuffer fill(byte value, int size) {
autoExpand(size);
int q = size >>> 3;
int r = size & 0x7;

if (q > 0) {
int intValue = value | value << 8 | value << 16 | value << 24;
long longValue = intValue;
longValue <<= 32L;
longValue |= intValue;

for (int i = q; i > 0; i--) {
putLong(longValue);
}
} 

q = r >>> 2;
r &= 0x3;

if (q > 0) {
int intValue = value | value << 8 | value << 16 | value << 24;
putInt(intValue);
} 

q = r >> 1;
r &= 0x1;

if (q > 0) {
short shortValue = (short)(value | value << 8);
putShort(shortValue);
} 

if (r > 0) {
put(value);
}

return this;
}

public IoBuffer fillAndReset(byte value, int size) {
autoExpand(size);
int pos = position();
try {
fill(value, size);
} finally {
position(pos);
} 
return this;
}

public IoBuffer fill(int size) {
autoExpand(size);
int q = size >>> 3;
int r = size & 0x7;

for (int i = q; i > 0; i--) {
putLong(0L);
}

q = r >>> 2;
r &= 0x3;

if (q > 0) {
putInt(0);
}

q = r >> 1;
r &= 0x1;

if (q > 0) {
putShort((short)0);
}

if (r > 0) {
put((byte)0);
}

return this;
}

public IoBuffer fillAndReset(int size) {
autoExpand(size);
int pos = position();
try {
fill(size);
} finally {
position(pos);
} 

return this;
}

public <E extends Enum<E>> E getEnum(Class<E> enumClass) {
return (E)toEnum(enumClass, getUnsigned());
}

public <E extends Enum<E>> E getEnum(int index, Class<E> enumClass) {
return (E)toEnum(enumClass, getUnsigned(index));
}

public <E extends Enum<E>> E getEnumShort(Class<E> enumClass) {
return (E)toEnum(enumClass, getUnsignedShort());
}

public <E extends Enum<E>> E getEnumShort(int index, Class<E> enumClass) {
return (E)toEnum(enumClass, getUnsignedShort(index));
}

public <E extends Enum<E>> E getEnumInt(Class<E> enumClass) {
return (E)toEnum(enumClass, getInt());
}

public <E extends Enum<E>> E getEnumInt(int index, Class<E> enumClass) {
return (E)toEnum(enumClass, getInt(index));
}

public IoBuffer putEnum(Enum<?> e) {
if (e.ordinal() > 255L) {
throw new IllegalArgumentException(enumConversionErrorMessage(e, "byte"));
}
return put((byte)e.ordinal());
}

public IoBuffer putEnum(int index, Enum<?> e) {
if (e.ordinal() > 255L) {
throw new IllegalArgumentException(enumConversionErrorMessage(e, "byte"));
}
return put(index, (byte)e.ordinal());
}

public IoBuffer putEnumShort(Enum<?> e) {
if (e.ordinal() > 65535L) {
throw new IllegalArgumentException(enumConversionErrorMessage(e, "short"));
}
return putShort((short)e.ordinal());
}

public IoBuffer putEnumShort(int index, Enum<?> e) {
if (e.ordinal() > 65535L) {
throw new IllegalArgumentException(enumConversionErrorMessage(e, "short"));
}
return putShort(index, (short)e.ordinal());
}

public IoBuffer putEnumInt(Enum<?> e) {
return putInt(e.ordinal());
}

public IoBuffer putEnumInt(int index, Enum<?> e) {
return putInt(index, e.ordinal());
}

private <E> E toEnum(Class<E> enumClass, int i) {
E[] enumConstants = enumClass.getEnumConstants();
if (i > enumConstants.length) {
throw new IndexOutOfBoundsException(String.format("%d is too large of an ordinal to convert to the enum %s", new Object[] { Integer.valueOf(i), enumClass.getName() }));
}

return enumConstants[i];
}

private String enumConversionErrorMessage(Enum<?> e, String type) {
return String.format("%s.%s has an ordinal value too large for a %s", new Object[] { e.getClass().getName(), e.name(), type });
}

public <E extends Enum<E>> EnumSet<E> getEnumSet(Class<E> enumClass) {
return toEnumSet(enumClass, get() & 0xFFL);
}

public <E extends Enum<E>> EnumSet<E> getEnumSet(int index, Class<E> enumClass) {
return toEnumSet(enumClass, get(index) & 0xFFL);
}

public <E extends Enum<E>> EnumSet<E> getEnumSetShort(Class<E> enumClass) {
return toEnumSet(enumClass, getShort() & 0xFFFFL);
}

public <E extends Enum<E>> EnumSet<E> getEnumSetShort(int index, Class<E> enumClass) {
return toEnumSet(enumClass, getShort(index) & 0xFFFFL);
}

public <E extends Enum<E>> EnumSet<E> getEnumSetInt(Class<E> enumClass) {
return toEnumSet(enumClass, getInt() & 0xFFFFFFFFL);
}

public <E extends Enum<E>> EnumSet<E> getEnumSetInt(int index, Class<E> enumClass) {
return toEnumSet(enumClass, getInt(index) & 0xFFFFFFFFL);
}

public <E extends Enum<E>> EnumSet<E> getEnumSetLong(Class<E> enumClass) {
return toEnumSet(enumClass, getLong());
}

public <E extends Enum<E>> EnumSet<E> getEnumSetLong(int index, Class<E> enumClass) {
return toEnumSet(enumClass, getLong(index));
}

private <E extends Enum<E>> EnumSet<E> toEnumSet(Class<E> clazz, long vector) {
EnumSet<E> set = EnumSet.noneOf(clazz);
long mask = 1L;
for (Enum enum_ : (Enum[])clazz.getEnumConstants()) {
if ((mask & vector) == mask) {
set.add((E)enum_);
}
mask <<= 1L;
} 
return set;
}

public <E extends Enum<E>> IoBuffer putEnumSet(Set<E> set) {
long vector = toLong(set);
if ((vector & 0xFFFFFFFFFFFFFF00L) != 0L) {
throw new IllegalArgumentException("The enum set is too large to fit in a byte: " + set);
}
return put((byte)(int)vector);
}

public <E extends Enum<E>> IoBuffer putEnumSet(int index, Set<E> set) {
long vector = toLong(set);
if ((vector & 0xFFFFFFFFFFFFFF00L) != 0L) {
throw new IllegalArgumentException("The enum set is too large to fit in a byte: " + set);
}
return put(index, (byte)(int)vector);
}

public <E extends Enum<E>> IoBuffer putEnumSetShort(Set<E> set) {
long vector = toLong(set);
if ((vector & 0xFFFFFFFFFFFF0000L) != 0L) {
throw new IllegalArgumentException("The enum set is too large to fit in a short: " + set);
}
return putShort((short)(int)vector);
}

public <E extends Enum<E>> IoBuffer putEnumSetShort(int index, Set<E> set) {
long vector = toLong(set);
if ((vector & 0xFFFFFFFFFFFF0000L) != 0L) {
throw new IllegalArgumentException("The enum set is too large to fit in a short: " + set);
}
return putShort(index, (short)(int)vector);
}

public <E extends Enum<E>> IoBuffer putEnumSetInt(Set<E> set) {
long vector = toLong(set);
if ((vector & 0xFFFFFFFF00000000L) != 0L) {
throw new IllegalArgumentException("The enum set is too large to fit in an int: " + set);
}
return putInt((int)vector);
}

public <E extends Enum<E>> IoBuffer putEnumSetInt(int index, Set<E> set) {
long vector = toLong(set);
if ((vector & 0xFFFFFFFF00000000L) != 0L) {
throw new IllegalArgumentException("The enum set is too large to fit in an int: " + set);
}
return putInt(index, (int)vector);
}

public <E extends Enum<E>> IoBuffer putEnumSetLong(Set<E> set) {
return putLong(toLong(set));
}

public <E extends Enum<E>> IoBuffer putEnumSetLong(int index, Set<E> set) {
return putLong(index, toLong(set));
}

private <E extends Enum<E>> long toLong(Set<E> set) {
long vector = 0L;
for (Enum enum_ : set) {
if (enum_.ordinal() >= 64) {
throw new IllegalArgumentException("The enum set is too large to fit in a bit vector: " + set);
}
vector |= 1L << enum_.ordinal();
} 
return vector;
}

private IoBuffer autoExpand(int expectedRemaining) {
if (isAutoExpand()) {
expand(expectedRemaining, true);
}
return this;
}

private IoBuffer autoExpand(int pos, int expectedRemaining) {
if (isAutoExpand()) {
expand(pos, expectedRemaining, true);
}
return this;
}

private static void checkFieldSize(int fieldSize) {
if (fieldSize < 0)
throw new IllegalArgumentException("fieldSize cannot be negative: " + fieldSize); 
}

protected abstract void buf(ByteBuffer paramByteBuffer);

protected abstract IoBuffer asReadOnlyBuffer0();

protected abstract IoBuffer duplicate0();

protected abstract IoBuffer slice0();
}

