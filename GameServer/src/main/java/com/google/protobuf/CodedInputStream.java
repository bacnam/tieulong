

package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class CodedInputStream {

  public static CodedInputStream newInstance(final InputStream input) {
    return new CodedInputStream(input);
  }

  public static CodedInputStream newInstance(final byte[] buf) {
    return newInstance(buf, 0, buf.length);
  }

  public static CodedInputStream newInstance(final byte[] buf, final int off,
                                             final int len) {
    CodedInputStream result = new CodedInputStream(buf, off, len);
    try {

      result.pushLimit(len);
    } catch (InvalidProtocolBufferException ex) {

      throw new IllegalArgumentException(ex);
    }
    return result;
  }

  public int readTag() throws IOException {
    if (isAtEnd()) {
      lastTag = 0;
      return 0;
    }

    lastTag = readRawVarint32();
    if (WireFormat.getTagFieldNumber(lastTag) == 0) {

      throw InvalidProtocolBufferException.invalidTag();
    }
    return lastTag;
  }

  public void checkLastTagWas(final int value)
                              throws InvalidProtocolBufferException {
    if (lastTag != value) {
      throw InvalidProtocolBufferException.invalidEndTag();
    }
  }

  public boolean skipField(final int tag) throws IOException {
    switch (WireFormat.getTagWireType(tag)) {
      case WireFormat.WIRETYPE_VARINT:
        readInt32();
        return true;
      case WireFormat.WIRETYPE_FIXED64:
        readRawLittleEndian64();
        return true;
      case WireFormat.WIRETYPE_LENGTH_DELIMITED:
        skipRawBytes(readRawVarint32());
        return true;
      case WireFormat.WIRETYPE_START_GROUP:
        skipMessage();
        checkLastTagWas(
          WireFormat.makeTag(WireFormat.getTagFieldNumber(tag),
                             WireFormat.WIRETYPE_END_GROUP));
        return true;
      case WireFormat.WIRETYPE_END_GROUP:
        return false;
      case WireFormat.WIRETYPE_FIXED32:
        readRawLittleEndian32();
        return true;
      default:
        throw InvalidProtocolBufferException.invalidWireType();
    }
  }

  public void skipMessage() throws IOException {
    while (true) {
      final int tag = readTag();
      if (tag == 0 || !skipField(tag)) {
        return;
      }
    }
  }

  public double readDouble() throws IOException {
    return Double.longBitsToDouble(readRawLittleEndian64());
  }

  public float readFloat() throws IOException {
    return Float.intBitsToFloat(readRawLittleEndian32());
  }

  public long readUInt64() throws IOException {
    return readRawVarint64();
  }

  public long readInt64() throws IOException {
    return readRawVarint64();
  }

  public int readInt32() throws IOException {
    return readRawVarint32();
  }

  public long readFixed64() throws IOException {
    return readRawLittleEndian64();
  }

  public int readFixed32() throws IOException {
    return readRawLittleEndian32();
  }

  public boolean readBool() throws IOException {
    return readRawVarint32() != 0;
  }

  public String readString() throws IOException {
    final int size = readRawVarint32();
    if (size <= (bufferSize - bufferPos) && size > 0) {

      final String result = new String(buffer, bufferPos, size, "UTF-8");
      bufferPos += size;
      return result;
    } else {

      return new String(readRawBytes(size), "UTF-8");
    }
  }

  public void readGroup(final int fieldNumber,
                        final MessageLite.Builder builder,
                        final ExtensionRegistryLite extensionRegistry)
      throws IOException {
    if (recursionDepth >= recursionLimit) {
      throw InvalidProtocolBufferException.recursionLimitExceeded();
    }
    ++recursionDepth;
    builder.mergeFrom(this, extensionRegistry);
    checkLastTagWas(
      WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_END_GROUP));
    --recursionDepth;
  }

  public <T extends MessageLite> T readGroup(
      final int fieldNumber,
      final Parser<T> parser,
      final ExtensionRegistryLite extensionRegistry)
      throws IOException {
    if (recursionDepth >= recursionLimit) {
      throw InvalidProtocolBufferException.recursionLimitExceeded();
    }
    ++recursionDepth;
    T result = parser.parsePartialFrom(this, extensionRegistry);
    checkLastTagWas(
      WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_END_GROUP));
    --recursionDepth;
    return result;
  }

  @Deprecated
  public void readUnknownGroup(final int fieldNumber,
                               final MessageLite.Builder builder)
      throws IOException {

    readGroup(fieldNumber, builder, null);
  }

  public void readMessage(final MessageLite.Builder builder,
                          final ExtensionRegistryLite extensionRegistry)
      throws IOException {
    final int length = readRawVarint32();
    if (recursionDepth >= recursionLimit) {
      throw InvalidProtocolBufferException.recursionLimitExceeded();
    }
    final int oldLimit = pushLimit(length);
    ++recursionDepth;
    builder.mergeFrom(this, extensionRegistry);
    checkLastTagWas(0);
    --recursionDepth;
    popLimit(oldLimit);
  }

  public <T extends MessageLite> T readMessage(
      final Parser<T> parser,
      final ExtensionRegistryLite extensionRegistry)
      throws IOException {
    int length = readRawVarint32();
    if (recursionDepth >= recursionLimit) {
      throw InvalidProtocolBufferException.recursionLimitExceeded();
    }
    final int oldLimit = pushLimit(length);
    ++recursionDepth;
    T result = parser.parsePartialFrom(this, extensionRegistry);
    checkLastTagWas(0);
    --recursionDepth;
    popLimit(oldLimit);
    return result;
  }

  public ByteString readBytes() throws IOException {
    final int size = readRawVarint32();
    if (size == 0) {
      return ByteString.EMPTY;
    } else if (size <= (bufferSize - bufferPos) && size > 0) {

      final ByteString result = ByteString.copyFrom(buffer, bufferPos, size);
      bufferPos += size;
      return result;
    } else {

      return ByteString.copyFrom(readRawBytes(size));
    }
  }

  public int readUInt32() throws IOException {
    return readRawVarint32();
  }

  public int readEnum() throws IOException {
    return readRawVarint32();
  }

  public int readSFixed32() throws IOException {
    return readRawLittleEndian32();
  }

  public long readSFixed64() throws IOException {
    return readRawLittleEndian64();
  }

  public int readSInt32() throws IOException {
    return decodeZigZag32(readRawVarint32());
  }

  public long readSInt64() throws IOException {
    return decodeZigZag64(readRawVarint64());
  }

  public int readRawVarint32() throws IOException {
    byte tmp = readRawByte();
    if (tmp >= 0) {
      return tmp;
    }
    int result = tmp & 0x7f;
    if ((tmp = readRawByte()) >= 0) {
      result |= tmp << 7;
    } else {
      result |= (tmp & 0x7f) << 7;
      if ((tmp = readRawByte()) >= 0) {
        result |= tmp << 14;
      } else {
        result |= (tmp & 0x7f) << 14;
        if ((tmp = readRawByte()) >= 0) {
          result |= tmp << 21;
        } else {
          result |= (tmp & 0x7f) << 21;
          result |= (tmp = readRawByte()) << 28;
          if (tmp < 0) {

            for (int i = 0; i < 5; i++) {
              if (readRawByte() >= 0) {
                return result;
              }
            }
            throw InvalidProtocolBufferException.malformedVarint();
          }
        }
      }
    }
    return result;
  }

  static int readRawVarint32(final InputStream input) throws IOException {
    final int firstByte = input.read();
    if (firstByte == -1) {
      throw InvalidProtocolBufferException.truncatedMessage();
    }
    return readRawVarint32(firstByte, input);
  }

  public static int readRawVarint32(
      final int firstByte, final InputStream input) throws IOException {
    if ((firstByte & 0x80) == 0) {
      return firstByte;
    }

    int result = firstByte & 0x7f;
    int offset = 7;
    for (; offset < 32; offset += 7) {
      final int b = input.read();
      if (b == -1) {
        throw InvalidProtocolBufferException.truncatedMessage();
      }
      result |= (b & 0x7f) << offset;
      if ((b & 0x80) == 0) {
        return result;
      }
    }

    for (; offset < 64; offset += 7) {
      final int b = input.read();
      if (b == -1) {
        throw InvalidProtocolBufferException.truncatedMessage();
      }
      if ((b & 0x80) == 0) {
        return result;
      }
    }
    throw InvalidProtocolBufferException.malformedVarint();
  }

  public long readRawVarint64() throws IOException {
    int shift = 0;
    long result = 0;
    while (shift < 64) {
      final byte b = readRawByte();
      result |= (long)(b & 0x7F) << shift;
      if ((b & 0x80) == 0) {
        return result;
      }
      shift += 7;
    }
    throw InvalidProtocolBufferException.malformedVarint();
  }

  public int readRawLittleEndian32() throws IOException {
    final byte b1 = readRawByte();
    final byte b2 = readRawByte();
    final byte b3 = readRawByte();
    final byte b4 = readRawByte();
    return (((int)b1 & 0xff)      ) |
           (((int)b2 & 0xff) <<  8) |
           (((int)b3 & 0xff) << 16) |
           (((int)b4 & 0xff) << 24);
  }

  public long readRawLittleEndian64() throws IOException {
    final byte b1 = readRawByte();
    final byte b2 = readRawByte();
    final byte b3 = readRawByte();
    final byte b4 = readRawByte();
    final byte b5 = readRawByte();
    final byte b6 = readRawByte();
    final byte b7 = readRawByte();
    final byte b8 = readRawByte();
    return (((long)b1 & 0xff)      ) |
           (((long)b2 & 0xff) <<  8) |
           (((long)b3 & 0xff) << 16) |
           (((long)b4 & 0xff) << 24) |
           (((long)b5 & 0xff) << 32) |
           (((long)b6 & 0xff) << 40) |
           (((long)b7 & 0xff) << 48) |
           (((long)b8 & 0xff) << 56);
  }

  public static int decodeZigZag32(final int n) {
    return (n >>> 1) ^ -(n & 1);
  }

  public static long decodeZigZag64(final long n) {
    return (n >>> 1) ^ -(n & 1);
  }

  private final byte[] buffer;
  private int bufferSize;
  private int bufferSizeAfterLimit;
  private int bufferPos;
  private final InputStream input;
  private int lastTag;

  private int totalBytesRetired;

  private int currentLimit = Integer.MAX_VALUE;

  private int recursionDepth;
  private int recursionLimit = DEFAULT_RECURSION_LIMIT;

  private int sizeLimit = DEFAULT_SIZE_LIMIT;

  private static final int DEFAULT_RECURSION_LIMIT = 64;
  private static final int DEFAULT_SIZE_LIMIT = 64 << 20;  
  private static final int BUFFER_SIZE = 4096;

  private CodedInputStream(final byte[] buffer, final int off, final int len) {
    this.buffer = buffer;
    bufferSize = off + len;
    bufferPos = off;
    totalBytesRetired = -off;
    input = null;
  }

  private CodedInputStream(final InputStream input) {
    buffer = new byte[BUFFER_SIZE];
    bufferSize = 0;
    bufferPos = 0;
    totalBytesRetired = 0;
    this.input = input;
  }

  public int setRecursionLimit(final int limit) {
    if (limit < 0) {
      throw new IllegalArgumentException(
        "Recursion limit cannot be negative: " + limit);
    }
    final int oldLimit = recursionLimit;
    recursionLimit = limit;
    return oldLimit;
  }

  public int setSizeLimit(final int limit) {
    if (limit < 0) {
      throw new IllegalArgumentException(
        "Size limit cannot be negative: " + limit);
    }
    final int oldLimit = sizeLimit;
    sizeLimit = limit;
    return oldLimit;
  }

  public void resetSizeCounter() {
    totalBytesRetired = -bufferPos;
  }

  public int pushLimit(int byteLimit) throws InvalidProtocolBufferException {
    if (byteLimit < 0) {
      throw InvalidProtocolBufferException.negativeSize();
    }
    byteLimit += totalBytesRetired + bufferPos;
    final int oldLimit = currentLimit;
    if (byteLimit > oldLimit) {
      throw InvalidProtocolBufferException.truncatedMessage();
    }
    currentLimit = byteLimit;

    recomputeBufferSizeAfterLimit();

    return oldLimit;
  }

  private void recomputeBufferSizeAfterLimit() {
    bufferSize += bufferSizeAfterLimit;
    final int bufferEnd = totalBytesRetired + bufferSize;
    if (bufferEnd > currentLimit) {

      bufferSizeAfterLimit = bufferEnd - currentLimit;
      bufferSize -= bufferSizeAfterLimit;
    } else {
      bufferSizeAfterLimit = 0;
    }
  }

  public void popLimit(final int oldLimit) {
    currentLimit = oldLimit;
    recomputeBufferSizeAfterLimit();
  }

  public int getBytesUntilLimit() {
    if (currentLimit == Integer.MAX_VALUE) {
      return -1;
    }

    final int currentAbsolutePosition = totalBytesRetired + bufferPos;
    return currentLimit - currentAbsolutePosition;
  }

  public boolean isAtEnd() throws IOException {
    return bufferPos == bufferSize && !refillBuffer(false);
  }

  public int getTotalBytesRead() {
      return totalBytesRetired + bufferPos;
  }

  private boolean refillBuffer(final boolean mustSucceed) throws IOException {
    if (bufferPos < bufferSize) {
      throw new IllegalStateException(
        "refillBuffer() called when buffer wasn't empty.");
    }

    if (totalBytesRetired + bufferSize == currentLimit) {

      if (mustSucceed) {
        throw InvalidProtocolBufferException.truncatedMessage();
      } else {
        return false;
      }
    }

    totalBytesRetired += bufferSize;

    bufferPos = 0;
    bufferSize = (input == null) ? -1 : input.read(buffer);
    if (bufferSize == 0 || bufferSize < -1) {
      throw new IllegalStateException(
          "InputStream#read(byte[]) returned invalid result: " + bufferSize +
          "\nThe InputStream implementation is buggy.");
    }
    if (bufferSize == -1) {
      bufferSize = 0;
      if (mustSucceed) {
        throw InvalidProtocolBufferException.truncatedMessage();
      } else {
        return false;
      }
    } else {
      recomputeBufferSizeAfterLimit();
      final int totalBytesRead =
        totalBytesRetired + bufferSize + bufferSizeAfterLimit;
      if (totalBytesRead > sizeLimit || totalBytesRead < 0) {
        throw InvalidProtocolBufferException.sizeLimitExceeded();
      }
      return true;
    }
  }

  public byte readRawByte() throws IOException {
    if (bufferPos == bufferSize) {
      refillBuffer(true);
    }
    return buffer[bufferPos++];
  }

  public byte[] readRawBytes(final int size) throws IOException {
    if (size < 0) {
      throw InvalidProtocolBufferException.negativeSize();
    }

    if (totalBytesRetired + bufferPos + size > currentLimit) {

      skipRawBytes(currentLimit - totalBytesRetired - bufferPos);

      throw InvalidProtocolBufferException.truncatedMessage();
    }

    if (size <= bufferSize - bufferPos) {

      final byte[] bytes = new byte[size];
      System.arraycopy(buffer, bufferPos, bytes, 0, size);
      bufferPos += size;
      return bytes;
    } else if (size < BUFFER_SIZE) {

      final byte[] bytes = new byte[size];
      int pos = bufferSize - bufferPos;
      System.arraycopy(buffer, bufferPos, bytes, 0, pos);
      bufferPos = bufferSize;

      refillBuffer(true);

      while (size - pos > bufferSize) {
        System.arraycopy(buffer, 0, bytes, pos, bufferSize);
        pos += bufferSize;
        bufferPos = bufferSize;
        refillBuffer(true);
      }

      System.arraycopy(buffer, 0, bytes, pos, size - pos);
      bufferPos = size - pos;

      return bytes;
    } else {

      final int originalBufferPos = bufferPos;
      final int originalBufferSize = bufferSize;

      totalBytesRetired += bufferSize;
      bufferPos = 0;
      bufferSize = 0;

      int sizeLeft = size - (originalBufferSize - originalBufferPos);
      final List<byte[]> chunks = new ArrayList<byte[]>();

      while (sizeLeft > 0) {
        final byte[] chunk = new byte[Math.min(sizeLeft, BUFFER_SIZE)];
        int pos = 0;
        while (pos < chunk.length) {
          final int n = (input == null) ? -1 :
            input.read(chunk, pos, chunk.length - pos);
          if (n == -1) {
            throw InvalidProtocolBufferException.truncatedMessage();
          }
          totalBytesRetired += n;
          pos += n;
        }
        sizeLeft -= chunk.length;
        chunks.add(chunk);
      }

      final byte[] bytes = new byte[size];

      int pos = originalBufferSize - originalBufferPos;
      System.arraycopy(buffer, originalBufferPos, bytes, 0, pos);

      for (final byte[] chunk : chunks) {
        System.arraycopy(chunk, 0, bytes, pos, chunk.length);
        pos += chunk.length;
      }

      return bytes;
    }
  }

  public void skipRawBytes(final int size) throws IOException {
    if (size < 0) {
      throw InvalidProtocolBufferException.negativeSize();
    }

    if (totalBytesRetired + bufferPos + size > currentLimit) {

      skipRawBytes(currentLimit - totalBytesRetired - bufferPos);

      throw InvalidProtocolBufferException.truncatedMessage();
    }

    if (size <= bufferSize - bufferPos) {

      bufferPos += size;
    } else {

      int pos = bufferSize - bufferPos;
      bufferPos = bufferSize;

      refillBuffer(true);
      while (size - pos > bufferSize) {
        pos += bufferSize;
        bufferPos = bufferSize;
        refillBuffer(true);
      }

      bufferPos = size - pos;
    }
  }
}
