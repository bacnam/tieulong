package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.zip.Checksum;

@Beta
public final class ByteStreams
{
private static final int BUF_SIZE = 4096;

public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] b) {
return newInputStreamSupplier(b, 0, b.length);
}

public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(final byte[] b, final int off, final int len) {
return new InputSupplier<ByteArrayInputStream>()
{
public ByteArrayInputStream getInput() {
return new ByteArrayInputStream(b, off, len);
}
};
}

public static void write(byte[] from, OutputSupplier<? extends OutputStream> to) throws IOException {
Preconditions.checkNotNull(from);
boolean threw = true;
OutputStream out = to.getOutput();
try {
out.write(from);
threw = false;
} finally {
Closeables.close(out, threw);
} 
}

public static long copy(InputSupplier<? extends InputStream> from, OutputSupplier<? extends OutputStream> to) throws IOException {
boolean threw = true;
InputStream in = from.getInput();
try {
OutputStream out = to.getOutput();

}
finally {

Closeables.close(in, threw);
} 
}

public static long copy(InputSupplier<? extends InputStream> from, OutputStream to) throws IOException {
boolean threw = true;
InputStream in = from.getInput();
try {
long count = copy(in, to);
threw = false;
return count;
} finally {
Closeables.close(in, threw);
} 
}

public static long copy(InputStream from, OutputSupplier<? extends OutputStream> to) throws IOException {
boolean threw = true;
OutputStream out = to.getOutput();
try {
long count = copy(from, out);
threw = false;
return count;
} finally {
Closeables.close(out, threw);
} 
}

public static long copy(InputStream from, OutputStream to) throws IOException {
byte[] buf = new byte[4096];
long total = 0L;
while (true) {
int r = from.read(buf);
if (r == -1) {
break;
}
to.write(buf, 0, r);
total += r;
} 
return total;
}

public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
ByteBuffer buf = ByteBuffer.allocate(4096);
long total = 0L;
while (from.read(buf) != -1) {
buf.flip();
while (buf.hasRemaining()) {
total += to.write(buf);
}
buf.clear();
} 
return total;
}

public static byte[] toByteArray(InputStream in) throws IOException {
ByteArrayOutputStream out = new ByteArrayOutputStream();
copy(in, out);
return out.toByteArray();
}

public static byte[] toByteArray(InputSupplier<? extends InputStream> supplier) throws IOException {
boolean threw = true;
InputStream in = supplier.getInput();
try {
byte[] result = toByteArray(in);
threw = false;
return result;
} finally {
Closeables.close(in, threw);
} 
}

public static ByteArrayDataInput newDataInput(byte[] bytes) {
return new ByteArrayDataInputStream(bytes);
}

public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
Preconditions.checkPositionIndex(start, bytes.length);
return new ByteArrayDataInputStream(bytes, start);
}

private static class ByteArrayDataInputStream implements ByteArrayDataInput {
final DataInput input;

ByteArrayDataInputStream(byte[] bytes) {
this.input = new DataInputStream(new ByteArrayInputStream(bytes));
}

ByteArrayDataInputStream(byte[] bytes, int start) {
this.input = new DataInputStream(new ByteArrayInputStream(bytes, start, bytes.length - start));
}

public void readFully(byte[] b) {
try {
this.input.readFully(b);
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public void readFully(byte[] b, int off, int len) {
try {
this.input.readFully(b, off, len);
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public int skipBytes(int n) {
try {
return this.input.skipBytes(n);
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public boolean readBoolean() {
try {
return this.input.readBoolean();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public byte readByte() {
try {
return this.input.readByte();
} catch (EOFException e) {
throw new IllegalStateException(e);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public int readUnsignedByte() {
try {
return this.input.readUnsignedByte();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public short readShort() {
try {
return this.input.readShort();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public int readUnsignedShort() {
try {
return this.input.readUnsignedShort();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public char readChar() {
try {
return this.input.readChar();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public int readInt() {
try {
return this.input.readInt();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public long readLong() {
try {
return this.input.readLong();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public float readFloat() {
try {
return this.input.readFloat();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public double readDouble() {
try {
return this.input.readDouble();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public String readLine() {
try {
return this.input.readLine();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}

public String readUTF() {
try {
return this.input.readUTF();
} catch (IOException e) {
throw new IllegalStateException(e);
} 
}
}

public static ByteArrayDataOutput newDataOutput() {
return new ByteArrayDataOutputStream();
}

public static ByteArrayDataOutput newDataOutput(int size) {
Preconditions.checkArgument((size >= 0), "Invalid size: %s", new Object[] { Integer.valueOf(size) });
return new ByteArrayDataOutputStream(size);
}

private static class ByteArrayDataOutputStream
implements ByteArrayDataOutput
{
final DataOutput output;
final ByteArrayOutputStream byteArrayOutputSteam;

ByteArrayDataOutputStream() {
this(new ByteArrayOutputStream());
}

ByteArrayDataOutputStream(int size) {
this(new ByteArrayOutputStream(size));
}

ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputSteam) {
this.byteArrayOutputSteam = byteArrayOutputSteam;
this.output = new DataOutputStream(byteArrayOutputSteam);
}

public void write(int b) {
try {
this.output.write(b);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void write(byte[] b) {
try {
this.output.write(b);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void write(byte[] b, int off, int len) {
try {
this.output.write(b, off, len);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeBoolean(boolean v) {
try {
this.output.writeBoolean(v);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeByte(int v) {
try {
this.output.writeByte(v);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeBytes(String s) {
try {
this.output.writeBytes(s);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeChar(int v) {
try {
this.output.writeChar(v);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeChars(String s) {
try {
this.output.writeChars(s);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeDouble(double v) {
try {
this.output.writeDouble(v);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeFloat(float v) {
try {
this.output.writeFloat(v);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeInt(int v) {
try {
this.output.writeInt(v);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeLong(long v) {
try {
this.output.writeLong(v);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeShort(int v) {
try {
this.output.writeShort(v);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public void writeUTF(String s) {
try {
this.output.writeUTF(s);
} catch (IOException impossible) {
throw new AssertionError(impossible);
} 
}

public byte[] toByteArray() {
return this.byteArrayOutputSteam.toByteArray();
}
}

public static long length(InputSupplier<? extends InputStream> supplier) throws IOException {
long count = 0L;
boolean threw = true;
InputStream in = supplier.getInput();

try {
while (true) {
long amt = in.skip(2147483647L);
if (amt == 0L) {
if (in.read() == -1) {
threw = false;
return count;
} 
count++; continue;
} 
count += amt;
} 
} finally {

Closeables.close(in, threw);
} 
}

public static boolean equal(InputSupplier<? extends InputStream> supplier1, InputSupplier<? extends InputStream> supplier2) throws IOException {
byte[] buf1 = new byte[4096];
byte[] buf2 = new byte[4096];

boolean threw = true;
InputStream in1 = supplier1.getInput();
try {
InputStream in2 = supplier2.getInput();

}
finally {

Closeables.close(in1, threw);
} 
}

public static void readFully(InputStream in, byte[] b) throws IOException {
readFully(in, b, 0, b.length);
}

public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
if (read(in, b, off, len) != len) {
throw new EOFException();
}
}

public static void skipFully(InputStream in, long n) throws IOException {
while (n > 0L) {
long amt = in.skip(n);
if (amt == 0L) {

if (in.read() == -1) {
throw new EOFException();
}
n--; continue;
} 
n -= amt;
} 
}

public static <T> T readBytes(InputSupplier<? extends InputStream> supplier, ByteProcessor<T> processor) throws IOException {
byte[] buf = new byte[4096];
boolean threw = true;
InputStream in = supplier.getInput();
try {
int amt;
do {
amt = in.read(buf);
if (amt == -1) {
threw = false;
break;
} 
} while (processor.processBytes(buf, 0, amt));
return processor.getResult();
} finally {
Closeables.close(in, threw);
} 
}

public static long getChecksum(InputSupplier<? extends InputStream> supplier, final Checksum checksum) throws IOException {
return ((Long)readBytes(supplier, new ByteProcessor<Long>()
{
public boolean processBytes(byte[] buf, int off, int len) {
checksum.update(buf, off, len);
return true;
}

public Long getResult() {
long result = checksum.getValue();
checksum.reset();
return Long.valueOf(result);
}
})).longValue();
}

public static byte[] getDigest(InputSupplier<? extends InputStream> supplier, final MessageDigest md) throws IOException {
return readBytes(supplier, (ByteProcessor)new ByteProcessor<byte[]>()
{
public boolean processBytes(byte[] buf, int off, int len) {
md.update(buf, off, len);
return true;
}

public byte[] getResult() {
return md.digest();
}
});
}

public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
if (len < 0) {
throw new IndexOutOfBoundsException("len is negative");
}
int total = 0;
while (total < len) {
int result = in.read(b, off + total, len - total);
if (result == -1) {
break;
}
total += result;
} 
return total;
}

public static InputSupplier<InputStream> slice(final InputSupplier<? extends InputStream> supplier, final long offset, final long length) {
Preconditions.checkNotNull(supplier);
Preconditions.checkArgument((offset >= 0L), "offset is negative");
Preconditions.checkArgument((length >= 0L), "length is negative");
return new InputSupplier<InputStream>() {
public InputStream getInput() throws IOException {
InputStream in = supplier.getInput();
if (offset > 0L) {
try {
ByteStreams.skipFully(in, offset);
} catch (IOException e) {
Closeables.closeQuietly(in);
throw e;
} 
}
return new LimitInputStream(in, length);
}
};
}

public static InputSupplier<InputStream> join(final Iterable<? extends InputSupplier<? extends InputStream>> suppliers) {
return new InputSupplier<InputStream>() {
public InputStream getInput() throws IOException {
return new MultiInputStream(suppliers.iterator());
}
};
}

public static InputSupplier<InputStream> join(InputSupplier<? extends InputStream>... suppliers) {
return join(Arrays.asList(suppliers));
}
}

