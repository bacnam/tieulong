package javolution.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javolution.context.LocalContext;
import javolution.lang.MathLib;
import javolution.lang.Realtime;
import javolution.text.TextBuilder;
import sun.nio.ch.DirectBuffer;

@Realtime
public class Struct
{
public static final LocalContext.Parameter<Integer> MAXIMUM_ALIGNMENT = new LocalContext.Parameter<Integer>()
{
protected Integer getDefault() {
return Integer.valueOf(4);
}
};

Struct _outer;

ByteBuffer _byteBuffer;

int _outerOffset;

int _alignment = 1;

int _length;

int _index;

int _wordSize;

int _bitsUsed;

boolean _resetIndex;

byte[] _bytes;

public Struct() {
this._resetIndex = isUnion();
}

public final int size() {
return (this._alignment <= 1) ? this._length : ((this._length + this._alignment - 1) / this._alignment * this._alignment);
}

public Struct outer() {
return this._outer;
}

public final ByteBuffer getByteBuffer() {
if (this._outer != null) return this._outer.getByteBuffer(); 
return (this._byteBuffer != null) ? this._byteBuffer : newBuffer();
}

private synchronized ByteBuffer newBuffer() {
if (this._byteBuffer != null) return this._byteBuffer; 
ByteBuffer bf = ByteBuffer.allocateDirect(size());
bf.order(byteOrder());
setByteBuffer(bf, 0);
return this._byteBuffer;
}

public final Struct setByteBuffer(ByteBuffer byteBuffer, int position) {
if (byteBuffer.order() != byteOrder()) throw new IllegalArgumentException("The byte order of the specified byte buffer is different from this struct byte order");

if (this._outer != null) throw new UnsupportedOperationException("Inner struct byte buffer is inherited from outer");

this._byteBuffer = byteBuffer;
this._outerOffset = position;
return this;
}

public final Struct setByteBufferPosition(int position) {
return setByteBuffer(getByteBuffer(), position);
}

public final int getByteBufferPosition() {
return (this._outer != null) ? (this._outer.getByteBufferPosition() + this._outerOffset) : this._outerOffset;
}

public int read(InputStream in) throws IOException {
ByteBuffer buffer = getByteBuffer();
int size = size();
int remaining = size - buffer.position();
if (remaining == 0) remaining = size; 
int alreadyRead = size - remaining;
if (buffer.hasArray()) {
int offset = buffer.arrayOffset() + getByteBufferPosition();
int bytesRead = in.read(buffer.array(), offset + alreadyRead, remaining);

buffer.position(getByteBufferPosition() + alreadyRead + bytesRead - offset);

return bytesRead;
} 
synchronized (buffer) {
if (this._bytes == null) {
this._bytes = new byte[size()];
}
int bytesRead = in.read(this._bytes, 0, remaining);
buffer.position(getByteBufferPosition() + alreadyRead);
buffer.put(this._bytes, 0, bytesRead);
return bytesRead;
} 
}

public void write(OutputStream out) throws IOException {
ByteBuffer buffer = getByteBuffer();
if (buffer.hasArray()) {
int offset = buffer.arrayOffset() + getByteBufferPosition();
out.write(buffer.array(), offset, size());
} else {
synchronized (buffer) {
if (this._bytes == null) {
this._bytes = new byte[size()];
}
buffer.position(getByteBufferPosition());
buffer.get(this._bytes);
out.write(this._bytes);
} 
} 
}

public final long address() {
ByteBuffer thisBuffer = getByteBuffer();
if (thisBuffer instanceof DirectBuffer)
return ((DirectBuffer)thisBuffer).address(); 
throw new UnsupportedOperationException();
}

public String toString() {
TextBuilder tmp = new TextBuilder();
int size = size();
ByteBuffer buffer = getByteBuffer();
int start = getByteBufferPosition();
for (int i = 0; i < size; i++) {
int b = buffer.get(start + i) & 0xFF;
tmp.append(HEXA[b >> 4]);
tmp.append(HEXA[b & 0xF]);
tmp.append(((i & 0xF) == 15) ? 10 : 32);
} 
return tmp.toString();
}

private static final char[] HEXA = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

public boolean isUnion() {
return false;
}

public ByteOrder byteOrder() {
return (this._outer != null) ? this._outer.byteOrder() : ByteOrder.BIG_ENDIAN;
}

public boolean isPacked() {
return false;
}

protected <S extends Struct> S inner(S struct) {
if (((Struct)struct)._outer != null) throw new IllegalArgumentException("struct: Already an inner struct");

Member inner = new Member(struct.size() << 3, ((Struct)struct)._alignment);
((Struct)struct)._outer = this;
((Struct)struct)._outerOffset = inner.offset();
return struct;
}

protected <S extends Struct> S[] array(S[] structs) {
Class<?> structClass = null;
boolean resetIndexSaved = this._resetIndex;
if (this._resetIndex) {
this._index = 0;
this._resetIndex = false;
} 
for (int i = 0; i < structs.length; ) {
Struct struct1; S struct = structs[i];
if (struct == null) {
try {
if (structClass == null) {
String arrayName = structs.getClass().getName();
String structName = arrayName.substring(2, arrayName.length() - 1);

structClass = Class.forName(structName);
if (structClass == null) throw new IllegalArgumentException("Struct class: " + structName + " not found");

} 
struct1 = (Struct)structClass.newInstance();
} catch (Exception e) {
throw new RuntimeException(e.getMessage());
} 
}
structs[i++] = inner((S)struct1);
} 
this._resetIndex = resetIndexSaved;
return structs;
}

protected <S extends Struct> S[][] array(S[][] structs) {
boolean resetIndexSaved = this._resetIndex;
if (this._resetIndex) {
this._index = 0;
this._resetIndex = false;
} 
for (int i = 0; i < structs.length; i++) {
array(structs[i]);
}
this._resetIndex = resetIndexSaved;
return structs;
}

protected <S extends Struct> S[][][] array(S[][][] structs) {
boolean resetIndexSaved = this._resetIndex;
if (this._resetIndex) {
this._index = 0;
this._resetIndex = false;
} 
for (int i = 0; i < structs.length; i++) {
array(structs[i]);
}
this._resetIndex = resetIndexSaved;
return structs;
}

protected <M extends Member> M[] array(M[] arrayMember) {
boolean resetIndexSaved = this._resetIndex;
if (this._resetIndex) {
this._index = 0;
this._resetIndex = false;
} 
if (BOOL.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Bool();
}
} else if (SIGNED_8.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Signed8();
}
} else if (UNSIGNED_8.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Unsigned8();
}
} else if (SIGNED_16.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Signed16();
}
} else if (UNSIGNED_16.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Unsigned16();
}
} else if (SIGNED_32.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Signed32();
}
} else if (UNSIGNED_32.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Unsigned32();
}
} else if (SIGNED_64.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Signed64();
}
} else if (FLOAT_32.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Float32();
}
} else if (FLOAT_64.isInstance(arrayMember)) {
for (int i = 0; i < arrayMember.length;) {
arrayMember[i++] = (M)new Float64();
}
} else {
throw new UnsupportedOperationException("Cannot create member elements, the arrayMember should contain the member instances instead of null");
} 

this._resetIndex = resetIndexSaved;
return arrayMember;
}

private static final Class<? extends Bool[]> BOOL = (Class)(new Bool[0]).getClass();
private static final Class<? extends Signed8[]> SIGNED_8 = (Class)(new Signed8[0]).getClass();

private static final Class<? extends Unsigned8[]> UNSIGNED_8 = (Class)(new Unsigned8[0]).getClass();

private static final Class<? extends Signed16[]> SIGNED_16 = (Class)(new Signed16[0]).getClass();

private static final Class<? extends Unsigned16[]> UNSIGNED_16 = (Class)(new Unsigned16[0]).getClass();

private static final Class<? extends Signed32[]> SIGNED_32 = (Class)(new Signed32[0]).getClass();

private static final Class<? extends Unsigned32[]> UNSIGNED_32 = (Class)(new Unsigned32[0]).getClass();

private static final Class<? extends Signed64[]> SIGNED_64 = (Class)(new Signed64[0]).getClass();

private static final Class<? extends Float32[]> FLOAT_32 = (Class)(new Float32[0]).getClass();

private static final Class<? extends Float64[]> FLOAT_64 = (Class)(new Float64[0]).getClass();

protected <M extends Member> M[][] array(M[][] arrayMember) {
boolean resetIndexSaved = this._resetIndex;
if (this._resetIndex) {
this._index = 0;
this._resetIndex = false;
} 
for (int i = 0; i < arrayMember.length; i++) {
array(arrayMember[i]);
}
this._resetIndex = resetIndexSaved;
return arrayMember;
}

protected <M extends Member> M[][][] array(M[][][] arrayMember) {
boolean resetIndexSaved = this._resetIndex;
if (this._resetIndex) {
this._index = 0;
this._resetIndex = false;
} 
for (int i = 0; i < arrayMember.length; i++) {
array(arrayMember[i]);
}
this._resetIndex = resetIndexSaved;
return arrayMember;
}

protected UTF8String[] array(UTF8String[] array, int stringLength) {
boolean resetIndexSaved = this._resetIndex;
if (this._resetIndex) {
this._index = 0;
this._resetIndex = false;
} 
for (int i = 0; i < array.length; i++) {
array[i] = new UTF8String(stringLength);
}
this._resetIndex = resetIndexSaved;
return array;
}

public long readBits(int bitOffset, int bitSize) {
if (bitOffset + bitSize - 1 >> 3 >= size()) throw new IllegalArgumentException("Attempt to read outside the Struct");

int offset = bitOffset >> 3;
int bitStart = bitOffset - (offset << 3);
bitStart = (byteOrder() == ByteOrder.BIG_ENDIAN) ? bitStart : (64 - bitSize - bitStart);

int index = getByteBufferPosition() + offset;
long value = readByteBufferLong(index);
value <<= bitStart;
value >>= 64 - bitSize;
return value;
}

private long readByteBufferLong(int index) {
ByteBuffer byteBuffer = getByteBuffer();
if (index + 8 < byteBuffer.limit()) return byteBuffer.getLong(index);

if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
return ((readByte(index, byteBuffer) & 0xFF) + ((readByte(++index, byteBuffer) & 0xFF) << 8) + ((readByte(++index, byteBuffer) & 0xFF) << 16)) + ((readByte(++index, byteBuffer) & 0xFFL) << 24L) + ((readByte(++index, byteBuffer) & 0xFFL) << 32L) + ((readByte(++index, byteBuffer) & 0xFFL) << 40L) + ((readByte(++index, byteBuffer) & 0xFFL) << 48L) + ((readByte(++index, byteBuffer) & 0xFFL) << 56L);
}

return (readByte(index, byteBuffer) << 56L) + ((readByte(++index, byteBuffer) & 0xFFL) << 48L) + ((readByte(++index, byteBuffer) & 0xFFL) << 40L) + ((readByte(++index, byteBuffer) & 0xFFL) << 32L) + ((readByte(++index, byteBuffer) & 0xFFL) << 24L) + ((readByte(++index, byteBuffer) & 0xFF) << 16) + ((readByte(++index, byteBuffer) & 0xFF) << 8) + (readByte(++index, byteBuffer) & 0xFFL);
}

private static byte readByte(int index, ByteBuffer byteBuffer) {
return (index < byteBuffer.limit()) ? byteBuffer.get(index) : 0;
}

public void writeBits(long value, int bitOffset, int bitSize) {
if (bitOffset + bitSize - 1 >> 3 >= size()) throw new IllegalArgumentException("Attempt to write outside the Struct");

int offset = bitOffset >> 3;
int bitStart = (byteOrder() == ByteOrder.BIG_ENDIAN) ? (bitOffset - (offset << 3)) : (64 - bitSize - bitOffset - (offset << 3));

long mask = -1L;
mask <<= bitStart;
mask >>>= 64 - bitSize;
mask <<= 64 - bitSize - bitStart;
value <<= 64 - bitSize - bitStart;
value &= mask;
int index = getByteBufferPosition() + offset;
long oldValue = readByteBufferLong(index);
long resetValue = oldValue & (mask ^ 0xFFFFFFFFFFFFFFFFL);
long newValue = resetValue | value;
writeByteBufferLong(index, newValue);
}

private void writeByteBufferLong(int index, long value) {
ByteBuffer byteBuffer = getByteBuffer();
if (index + 8 < byteBuffer.limit()) {
byteBuffer.putLong(index, value);

return;
} 
if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
writeByte(index, byteBuffer, (byte)(int)value);
writeByte(++index, byteBuffer, (byte)(int)(value >> 8L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 16L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 24L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 32L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 40L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 48L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 56L));
} else {
writeByte(index, byteBuffer, (byte)(int)(value >> 56L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 48L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 40L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 32L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 24L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 16L));
writeByte(++index, byteBuffer, (byte)(int)(value >> 8L));
writeByte(++index, byteBuffer, (byte)(int)value);
} 
}

private static void writeByte(int index, ByteBuffer byteBuffer, byte value) {
if (index < byteBuffer.limit()) {
byteBuffer.put(index, value);
}
}

protected class Member
{
private final int _offset;

private final int _bitIndex;

private final int _bitLength;

protected Member(int bitLength, int wordSize) {
this._bitLength = bitLength;

if (Struct.this._resetIndex) {
Struct.this._index = 0;
}

if (wordSize == 0 || (bitLength != 0 && wordSize == Struct.this._wordSize && Struct.this._bitsUsed + bitLength <= wordSize << 3)) {

this._offset = Struct.this._index - Struct.this._wordSize;
this._bitIndex = Struct.this._bitsUsed;
Struct.this._bitsUsed += bitLength;

while (Struct.this._bitsUsed > Struct.this._wordSize << 3) {
Struct.this._index++;
Struct.this._wordSize++;
Struct.this._length = MathLib.max(Struct.this._length, Struct.this._index);
} 

return;
} 

if (!Struct.this.isPacked()) {

if (Struct.this._alignment < wordSize) {
Struct.this._alignment = wordSize;
}

int misaligned = Struct.this._index % wordSize;
if (misaligned != 0) {
Struct.this._index += wordSize - misaligned;
}
} 

this._offset = Struct.this._index;
this._bitIndex = 0;

Struct.this._index += MathLib.max(wordSize, bitLength + 7 >> 3);
Struct.this._wordSize = wordSize;
Struct.this._bitsUsed = bitLength;
Struct.this._length = MathLib.max(Struct.this._length, Struct.this._index);
}

public final Struct struct() {
return Struct.this;
}

public final int offset() {
return this._offset;
}

public final int bitIndex() {
return this._bitIndex;
}

public final int bitLength() {
return this._bitLength;
}

final int get(int wordSize, int word) {
int shift = (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) ? ((wordSize << 3) - bitIndex() - bitLength()) : bitIndex();

word >>= shift;
int mask = -1 >>> 32 - bitLength();
return word & mask;
}

final int set(int value, int wordSize, int word) {
int shift = (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) ? ((wordSize << 3) - bitIndex() - bitLength()) : bitIndex();

int mask = -1 >>> 32 - bitLength();
mask <<= shift;
value <<= shift;
return word & (mask ^ 0xFFFFFFFF) | value & mask;
}

final long get(int wordSize, long word) {
int shift = (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) ? ((wordSize << 3) - bitIndex() - bitLength()) : bitIndex();

word >>= shift;
long mask = -1L >>> 64 - bitLength();
return word & mask;
}

final long set(long value, int wordSize, long word) {
int shift = (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) ? ((wordSize << 3) - bitIndex() - bitLength()) : bitIndex();

long mask = -1L >>> 64 - bitLength();
mask <<= shift;
value <<= shift;
return word & (mask ^ 0xFFFFFFFFFFFFFFFFL) | value & mask;
}
}

public class UTF8String
extends Member
{
private final UTF8ByteBufferWriter _writer = new UTF8ByteBufferWriter();
private final UTF8ByteBufferReader _reader = new UTF8ByteBufferReader();
private final int _length;

public UTF8String(int length) {
super(length << 3, 1);
this._length = length;
}

public void set(String string) {
ByteBuffer buffer = Struct.this.getByteBuffer();
synchronized (buffer) {
try {
int index = Struct.this.getByteBufferPosition() + offset();
buffer.position(index);
this._writer.setOutput(buffer);
if (string.length() < this._length) {
this._writer.write(string);
this._writer.write(0);
} else if (string.length() > this._length) {
this._writer.write(string.substring(0, this._length));
} else {
this._writer.write(string);
} 
} catch (IOException e) {
throw new Error(e.getMessage());
} finally {
this._writer.reset();
} 
} 
}

public String get() {
ByteBuffer buffer = Struct.this.getByteBuffer();
synchronized (buffer) {
TextBuilder tmp = new TextBuilder();
try {
int index = Struct.this.getByteBufferPosition() + offset();
buffer.position(index);
this._reader.setInput(buffer);
for (int i = 0; i < this._length; i++) {
char c = (char)this._reader.read();
if (c == '\000') {
return tmp.toString();
}
tmp.append(c);
} 

return tmp.toString();
} catch (IOException e) {
throw new Error(e.getMessage());
} finally {
this._reader.reset();
} 
} 
}

public String toString() {
return get();
}
}

public class Bool
extends Member
{
public Bool() {
super(8, 1);
}

public Bool(int nbrOfBits) {
super(nbrOfBits, 1);
}

public boolean get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().get(index);
word = (bitLength() == 8) ? word : get(1, word);
return (word != 0);
}

public void set(boolean value) {
int index = Struct.this.getByteBufferPosition() + offset();
if (bitLength() == 8) {
Struct.this.getByteBuffer().put(index, (byte)(value ? -1 : 0));
} else {
Struct.this.getByteBuffer().put(index, (byte)set(value ? -1 : 0, 1, Struct.this.getByteBuffer().get(index)));
} 
}

public String toString() {
return String.valueOf(get());
}
}

public class Signed8
extends Member
{
public Signed8() {
super(8, 1);
}

public Signed8(int nbrOfBits) {
super(nbrOfBits, 1);
}

public byte get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().get(index);
return (byte)((bitLength() == 8) ? word : get(1, word));
}

public void set(byte value) {
int index = Struct.this.getByteBufferPosition() + offset();
if (bitLength() == 8) {
Struct.this.getByteBuffer().put(index, value);
} else {
Struct.this.getByteBuffer().put(index, (byte)set(value, 1, Struct.this.getByteBuffer().get(index)));
} 
}

public String toString() {
return String.valueOf(get());
}
}

public class Unsigned8
extends Member
{
public Unsigned8() {
super(8, 1);
}

public Unsigned8(int nbrOfBits) {
super(nbrOfBits, 1);
}

public short get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().get(index);
return (short)(0xFF & ((bitLength() == 8) ? word : get(1, word)));
}

public void set(short value) {
int index = Struct.this.getByteBufferPosition() + offset();
if (bitLength() == 8) {
Struct.this.getByteBuffer().put(index, (byte)value);
} else {
Struct.this.getByteBuffer().put(index, (byte)set(value, 1, Struct.this.getByteBuffer().get(index)));
} 
}

public String toString() {
return String.valueOf(get());
}
}

public class Signed16
extends Member
{
public Signed16() {
super(16, 2);
}

public Signed16(int nbrOfBits) {
super(nbrOfBits, 2);
}

public short get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().getShort(index);
return (short)((bitLength() == 16) ? word : get(2, word));
}

public void set(short value) {
int index = Struct.this.getByteBufferPosition() + offset();
if (bitLength() == 16) {
Struct.this.getByteBuffer().putShort(index, value);
} else {
Struct.this.getByteBuffer().putShort(index, (short)set(value, 2, Struct.this.getByteBuffer().getShort(index)));
} 
}

public String toString() {
return String.valueOf(get());
}
}

public class Unsigned16
extends Member
{
public Unsigned16() {
super(16, 2);
}

public Unsigned16(int nbrOfBits) {
super(nbrOfBits, 2);
}

public int get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().getShort(index);
return 0xFFFF & ((bitLength() == 16) ? word : get(2, word));
}

public void set(int value) {
int index = Struct.this.getByteBufferPosition() + offset();
if (bitLength() == 16) {
Struct.this.getByteBuffer().putShort(index, (short)value);
} else {
Struct.this.getByteBuffer().putShort(index, (short)set(value, 2, Struct.this.getByteBuffer().getShort(index)));
} 
}

public String toString() {
return String.valueOf(get());
}
}

public class Signed32
extends Member
{
public Signed32() {
super(32, 4);
}

public Signed32(int nbrOfBits) {
super(nbrOfBits, 4);
}

public int get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().getInt(index);
return (bitLength() == 32) ? word : get(4, word);
}

public void set(int value) {
int index = Struct.this.getByteBufferPosition() + offset();
if (bitLength() == 32) {
Struct.this.getByteBuffer().putInt(index, value);
} else {
Struct.this.getByteBuffer().putInt(index, set(value, 4, Struct.this.getByteBuffer().getInt(index)));
} 
}

public String toString() {
return String.valueOf(get());
}
}

public class Unsigned32
extends Member
{
public Unsigned32() {
super(32, 4);
}

public Unsigned32(int nbrOfBits) {
super(nbrOfBits, 4);
}

public long get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().getInt(index);
return 0xFFFFFFFFL & ((bitLength() == 32) ? word : get(4, word));
}

public void set(long value) {
int index = Struct.this.getByteBufferPosition() + offset();
if (bitLength() == 32) {
Struct.this.getByteBuffer().putInt(index, (int)value);
} else {
Struct.this.getByteBuffer().putInt(index, set((int)value, 4, Struct.this.getByteBuffer().getInt(index)));
} 
}

public String toString() {
return String.valueOf(get());
}
}

public class Signed64
extends Member
{
public Signed64() {
super(64, 8);
}

public Signed64(int nbrOfBits) {
super(nbrOfBits, 8);
}

public long get() {
int index = Struct.this.getByteBufferPosition() + offset();
long word = Struct.this.getByteBuffer().getLong(index);
return (bitLength() == 64) ? word : get(8, word);
}

public void set(long value) {
int index = Struct.this.getByteBufferPosition() + offset();
if (bitLength() == 64) {
Struct.this.getByteBuffer().putLong(index, value);
} else {
Struct.this.getByteBuffer().putLong(index, set(value, 8, Struct.this.getByteBuffer().getLong(index)));
} 
}

public String toString() {
return String.valueOf(get());
}
}

public class BitField
extends Member
{
public BitField(int nbrOfBits) {
super(nbrOfBits, 0);
}

public long longValue() {
long signedValue = Struct.this.readBits(bitIndex() + (offset() << 3), bitLength());

return (-1L << bitLength() ^ 0xFFFFFFFFFFFFFFFFL) & signedValue;
}

public int intValue() {
return (int)longValue();
}

public short shortValue() {
return (short)(int)longValue();
}

public byte byteValue() {
return (byte)(int)longValue();
}

public void set(long value) {
Struct.this.writeBits(value, bitIndex() + (offset() << 3), bitLength());
}

public String toString() {
return String.valueOf(longValue());
}
}

public class Float32
extends Member
{
public Float32() {
super(32, 4);
}

public float get() {
int index = Struct.this.getByteBufferPosition() + offset();
return Struct.this.getByteBuffer().getFloat(index);
}

public void set(float value) {
int index = Struct.this.getByteBufferPosition() + offset();
Struct.this.getByteBuffer().putFloat(index, value);
}

public String toString() {
return String.valueOf(get());
}
}

public class Float64
extends Member
{
public Float64() {
super(64, 8);
}

public double get() {
int index = Struct.this.getByteBufferPosition() + offset();
return Struct.this.getByteBuffer().getDouble(index);
}

public void set(double value) {
int index = Struct.this.getByteBufferPosition() + offset();
Struct.this.getByteBuffer().putDouble(index, value);
}

public String toString() {
return String.valueOf(get());
}
}

public class Reference32<S extends Struct>
extends Member
{
private S _struct;

public Reference32() {
super(32, 4);
}

public void set(S struct) {
int index = Struct.this.getByteBufferPosition() + offset();
if (struct != null) {
Struct.this.getByteBuffer().putInt(index, (int)struct.address());
} else {
Struct.this.getByteBuffer().putInt(index, 0);
} 
this._struct = struct;
}

public S get() {
return this._struct;
}

public int value() {
int index = Struct.this.getByteBufferPosition() + offset();
return Struct.this.getByteBuffer().getInt(index);
}

public boolean isUpToDate() {
int index = Struct.this.getByteBufferPosition() + offset();
if (this._struct != null) {
return (Struct.this.getByteBuffer().getInt(index) == (int)this._struct.address());
}
return (Struct.this.getByteBuffer().getInt(index) == 0);
}
}

public class Reference64<S extends Struct>
extends Member
{
private S _struct;

public Reference64() {
super(64, 8);
}

public void set(S struct) {
int index = Struct.this.getByteBufferPosition() + offset();
if (struct != null) {
Struct.this.getByteBuffer().putLong(index, struct.address());
} else if (struct == null) {
Struct.this.getByteBuffer().putLong(index, 0L);
} 
this._struct = struct;
}

public S get() {
return this._struct;
}

public long value() {
int index = Struct.this.getByteBufferPosition() + offset();
return Struct.this.getByteBuffer().getLong(index);
}

public boolean isUpToDate() {
int index = Struct.this.getByteBufferPosition() + offset();
if (this._struct != null) {
return (Struct.this.getByteBuffer().getLong(index) == this._struct.address());
}
return (Struct.this.getByteBuffer().getLong(index) == 0L);
}
}

public class Enum8<T extends Enum<T>>
extends Member
{
private final T[] _values;

public Enum8(T[] values) {
super(8, 1);
this._values = values;
}

public Enum8(T[] values, int nbrOfBits) {
super(nbrOfBits, 1);
this._values = values;
}

public T get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().get(index);
return this._values[0xFF & get(1, word)];
}

public void set(T e) {
int value = e.ordinal();
if (this._values[value] != e) throw new IllegalArgumentException("enum: " + e + ", ordinal value does not reflect enum values position");

int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().get(index);
Struct.this.getByteBuffer().put(index, (byte)set(value, 1, word));
}

public String toString() {
return String.valueOf(get());
}
}

public class Enum16<T extends Enum<T>>
extends Member
{
private final T[] _values;

public Enum16(T[] values) {
super(16, 2);
this._values = values;
}

public Enum16(T[] values, int nbrOfBits) {
super(nbrOfBits, 2);
this._values = values;
}

public T get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().getShort(index);
return this._values[0xFFFF & get(2, word)];
}

public void set(T e) {
int value = e.ordinal();
if (this._values[value] != e) throw new IllegalArgumentException("enum: " + e + ", ordinal value does not reflect enum values position");

int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().getShort(index);
Struct.this.getByteBuffer().putShort(index, (short)set(value, 2, word));
}

public String toString() {
return String.valueOf(get());
}
}

public class Enum32<T extends Enum<T>>
extends Member
{
private final T[] _values;

public Enum32(T[] values) {
super(32, 4);
this._values = values;
}

public Enum32(T[] values, int nbrOfBits) {
super(nbrOfBits, 4);
this._values = values;
}

public T get() {
int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().getInt(index);
return this._values[get(4, word)];
}

public void set(T e) {
int value = e.ordinal();
if (this._values[value] != e) throw new IllegalArgumentException("enum: " + e + ", ordinal value does not reflect enum values position");

int index = Struct.this.getByteBufferPosition() + offset();
int word = Struct.this.getByteBuffer().getInt(index);
Struct.this.getByteBuffer().putInt(index, set(value, 4, word));
}

public String toString() {
return String.valueOf(get());
}
}

public class Enum64<T extends Enum<T>>
extends Member
{
private final T[] _values;

public Enum64(T[] values) {
super(64, 8);
this._values = values;
}

public Enum64(T[] values, int nbrOfBits) {
super(nbrOfBits, 8);
this._values = values;
}

public T get() {
int index = Struct.this.getByteBufferPosition() + offset();
long word = Struct.this.getByteBuffer().getLong(index);
return this._values[(int)get(8, word)];
}

public void set(T e) {
long value = e.ordinal();
if (this._values[(int)value] != e) throw new IllegalArgumentException("enum: " + e + ", ordinal value does not reflect enum values position");

int index = Struct.this.getByteBufferPosition() + offset();
long word = Struct.this.getByteBuffer().getLong(index);
Struct.this.getByteBuffer().putLong(index, set(value, 8, word));
}

public String toString() {
return String.valueOf(get());
}
}
}

