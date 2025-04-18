

package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class CodedOutputStream {
  private final byte[] buffer;
  private final int limit;
  private int position;

  private final OutputStream output;

  public static final int DEFAULT_BUFFER_SIZE = 4096;

  static int computePreferredBufferSize(int dataLength) {
    if (dataLength > DEFAULT_BUFFER_SIZE) return DEFAULT_BUFFER_SIZE;
    return dataLength;
  }

  private CodedOutputStream(final byte[] buffer, final int offset,
                            final int length) {
    output = null;
    this.buffer = buffer;
    position = offset;
    limit = offset + length;
  }

  private CodedOutputStream(final OutputStream output, final byte[] buffer) {
    this.output = output;
    this.buffer = buffer;
    position = 0;
    limit = buffer.length;
  }

  public static CodedOutputStream newInstance(final OutputStream output) {
    return newInstance(output, DEFAULT_BUFFER_SIZE);
  }

  public static CodedOutputStream newInstance(final OutputStream output,
      final int bufferSize) {
    return new CodedOutputStream(output, new byte[bufferSize]);
  }

  public static CodedOutputStream newInstance(final byte[] flatArray) {
    return newInstance(flatArray, 0, flatArray.length);
  }

  public static CodedOutputStream newInstance(final byte[] flatArray,
                                              final int offset,
                                              final int length) {
    return new CodedOutputStream(flatArray, offset, length);
  }

  public void writeDouble(final int fieldNumber, final double value)
                          throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
    writeDoubleNoTag(value);
  }

  public void writeFloat(final int fieldNumber, final float value)
                         throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
    writeFloatNoTag(value);
  }

  public void writeUInt64(final int fieldNumber, final long value)
                          throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
    writeUInt64NoTag(value);
  }

  public void writeInt64(final int fieldNumber, final long value)
                         throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
    writeInt64NoTag(value);
  }

  public void writeInt32(final int fieldNumber, final int value)
                         throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
    writeInt32NoTag(value);
  }

  public void writeFixed64(final int fieldNumber, final long value)
                           throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
    writeFixed64NoTag(value);
  }

  public void writeFixed32(final int fieldNumber, final int value)
                           throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
    writeFixed32NoTag(value);
  }

  public void writeBool(final int fieldNumber, final boolean value)
                        throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
    writeBoolNoTag(value);
  }

  public void writeString(final int fieldNumber, final String value)
                          throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
    writeStringNoTag(value);
  }

  public void writeGroup(final int fieldNumber, final MessageLite value)
                         throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_START_GROUP);
    writeGroupNoTag(value);
    writeTag(fieldNumber, WireFormat.WIRETYPE_END_GROUP);
  }

  @Deprecated
  public void writeUnknownGroup(final int fieldNumber,
                                final MessageLite value)
                                throws IOException {
    writeGroup(fieldNumber, value);
  }

  public void writeMessage(final int fieldNumber, final MessageLite value)
                           throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
    writeMessageNoTag(value);
  }

  public void writeBytes(final int fieldNumber, final ByteString value)
                         throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
    writeBytesNoTag(value);
  }

  public void writeUInt32(final int fieldNumber, final int value)
                          throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
    writeUInt32NoTag(value);
  }

  public void writeEnum(final int fieldNumber, final int value)
                        throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
    writeEnumNoTag(value);
  }

  public void writeSFixed32(final int fieldNumber, final int value)
                            throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
    writeSFixed32NoTag(value);
  }

  public void writeSFixed64(final int fieldNumber, final long value)
                            throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
    writeSFixed64NoTag(value);
  }

  public void writeSInt32(final int fieldNumber, final int value)
                          throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
    writeSInt32NoTag(value);
  }

  public void writeSInt64(final int fieldNumber, final long value)
                          throws IOException {
    writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
    writeSInt64NoTag(value);
  }

  public void writeMessageSetExtension(final int fieldNumber,
                                       final MessageLite value)
                                       throws IOException {
    writeTag(WireFormat.MESSAGE_SET_ITEM, WireFormat.WIRETYPE_START_GROUP);
    writeUInt32(WireFormat.MESSAGE_SET_TYPE_ID, fieldNumber);
    writeMessage(WireFormat.MESSAGE_SET_MESSAGE, value);
    writeTag(WireFormat.MESSAGE_SET_ITEM, WireFormat.WIRETYPE_END_GROUP);
  }

  public void writeRawMessageSetExtension(final int fieldNumber,
                                          final ByteString value)
                                          throws IOException {
    writeTag(WireFormat.MESSAGE_SET_ITEM, WireFormat.WIRETYPE_START_GROUP);
    writeUInt32(WireFormat.MESSAGE_SET_TYPE_ID, fieldNumber);
    writeBytes(WireFormat.MESSAGE_SET_MESSAGE, value);
    writeTag(WireFormat.MESSAGE_SET_ITEM, WireFormat.WIRETYPE_END_GROUP);
  }

  public void writeDoubleNoTag(final double value) throws IOException {
    writeRawLittleEndian64(Double.doubleToRawLongBits(value));
  }

  public void writeFloatNoTag(final float value) throws IOException {
    writeRawLittleEndian32(Float.floatToRawIntBits(value));
  }

  public void writeUInt64NoTag(final long value) throws IOException {
    writeRawVarint64(value);
  }

  public void writeInt64NoTag(final long value) throws IOException {
    writeRawVarint64(value);
  }

  public void writeInt32NoTag(final int value) throws IOException {
    if (value >= 0) {
      writeRawVarint32(value);
    } else {

      writeRawVarint64(value);
    }
  }

  public void writeFixed64NoTag(final long value) throws IOException {
    writeRawLittleEndian64(value);
  }

  public void writeFixed32NoTag(final int value) throws IOException {
    writeRawLittleEndian32(value);
  }

  public void writeBoolNoTag(final boolean value) throws IOException {
    writeRawByte(value ? 1 : 0);
  }

  public void writeStringNoTag(final String value) throws IOException {

    final byte[] bytes = value.getBytes("UTF-8");
    writeRawVarint32(bytes.length);
    writeRawBytes(bytes);
  }

  public void writeGroupNoTag(final MessageLite value) throws IOException {
    value.writeTo(this);
  }

  @Deprecated
  public void writeUnknownGroupNoTag(final MessageLite value)
      throws IOException {
    writeGroupNoTag(value);
  }

  public void writeMessageNoTag(final MessageLite value) throws IOException {
    writeRawVarint32(value.getSerializedSize());
    value.writeTo(this);
  }

  public void writeBytesNoTag(final ByteString value) throws IOException {
    writeRawVarint32(value.size());
    writeRawBytes(value);
  }

  public void writeUInt32NoTag(final int value) throws IOException {
    writeRawVarint32(value);
  }

  public void writeEnumNoTag(final int value) throws IOException {
    writeInt32NoTag(value);
  }

  public void writeSFixed32NoTag(final int value) throws IOException {
    writeRawLittleEndian32(value);
  }

  public void writeSFixed64NoTag(final long value) throws IOException {
    writeRawLittleEndian64(value);
  }

  public void writeSInt32NoTag(final int value) throws IOException {
    writeRawVarint32(encodeZigZag32(value));
  }

  public void writeSInt64NoTag(final long value) throws IOException {
    writeRawVarint64(encodeZigZag64(value));
  }

  public static int computeDoubleSize(final int fieldNumber,
                                      final double value) {
    return computeTagSize(fieldNumber) + computeDoubleSizeNoTag(value);
  }

  public static int computeFloatSize(final int fieldNumber, final float value) {
    return computeTagSize(fieldNumber) + computeFloatSizeNoTag(value);
  }

  public static int computeUInt64Size(final int fieldNumber, final long value) {
    return computeTagSize(fieldNumber) + computeUInt64SizeNoTag(value);
  }

  public static int computeInt64Size(final int fieldNumber, final long value) {
    return computeTagSize(fieldNumber) + computeInt64SizeNoTag(value);
  }

  public static int computeInt32Size(final int fieldNumber, final int value) {
    return computeTagSize(fieldNumber) + computeInt32SizeNoTag(value);
  }

  public static int computeFixed64Size(final int fieldNumber,
                                       final long value) {
    return computeTagSize(fieldNumber) + computeFixed64SizeNoTag(value);
  }

  public static int computeFixed32Size(final int fieldNumber,
                                       final int value) {
    return computeTagSize(fieldNumber) + computeFixed32SizeNoTag(value);
  }

  public static int computeBoolSize(final int fieldNumber,
                                    final boolean value) {
    return computeTagSize(fieldNumber) + computeBoolSizeNoTag(value);
  }

  public static int computeStringSize(final int fieldNumber,
                                      final String value) {
    return computeTagSize(fieldNumber) + computeStringSizeNoTag(value);
  }

  public static int computeGroupSize(final int fieldNumber,
                                     final MessageLite value) {
    return computeTagSize(fieldNumber) * 2 + computeGroupSizeNoTag(value);
  }

  @Deprecated
  public static int computeUnknownGroupSize(final int fieldNumber,
                                            final MessageLite value) {
    return computeGroupSize(fieldNumber, value);
  }

  public static int computeMessageSize(final int fieldNumber,
                                       final MessageLite value) {
    return computeTagSize(fieldNumber) + computeMessageSizeNoTag(value);
  }

  public static int computeBytesSize(final int fieldNumber,
                                     final ByteString value) {
    return computeTagSize(fieldNumber) + computeBytesSizeNoTag(value);
  }

  public static int computeLazyFieldSize(final int fieldNumber,
                                         final LazyField value) {
    return computeTagSize(fieldNumber) + computeLazyFieldSizeNoTag(value);
  }

  public static int computeUInt32Size(final int fieldNumber, final int value) {
    return computeTagSize(fieldNumber) + computeUInt32SizeNoTag(value);
  }

  public static int computeEnumSize(final int fieldNumber, final int value) {
    return computeTagSize(fieldNumber) + computeEnumSizeNoTag(value);
  }

  public static int computeSFixed32Size(final int fieldNumber,
                                        final int value) {
    return computeTagSize(fieldNumber) + computeSFixed32SizeNoTag(value);
  }

  public static int computeSFixed64Size(final int fieldNumber,
                                        final long value) {
    return computeTagSize(fieldNumber) + computeSFixed64SizeNoTag(value);
  }

  public static int computeSInt32Size(final int fieldNumber, final int value) {
    return computeTagSize(fieldNumber) + computeSInt32SizeNoTag(value);
  }

  public static int computeSInt64Size(final int fieldNumber, final long value) {
    return computeTagSize(fieldNumber) + computeSInt64SizeNoTag(value);
  }

  public static int computeMessageSetExtensionSize(
      final int fieldNumber, final MessageLite value) {
    return computeTagSize(WireFormat.MESSAGE_SET_ITEM) * 2 +
           computeUInt32Size(WireFormat.MESSAGE_SET_TYPE_ID, fieldNumber) +
           computeMessageSize(WireFormat.MESSAGE_SET_MESSAGE, value);
  }

  public static int computeRawMessageSetExtensionSize(
      final int fieldNumber, final ByteString value) {
    return computeTagSize(WireFormat.MESSAGE_SET_ITEM) * 2 +
           computeUInt32Size(WireFormat.MESSAGE_SET_TYPE_ID, fieldNumber) +
           computeBytesSize(WireFormat.MESSAGE_SET_MESSAGE, value);
  }

  public static int computeLazyFieldMessageSetExtensionSize(
      final int fieldNumber, final LazyField value) {
    return computeTagSize(WireFormat.MESSAGE_SET_ITEM) * 2 +
           computeUInt32Size(WireFormat.MESSAGE_SET_TYPE_ID, fieldNumber) +
           computeLazyFieldSize(WireFormat.MESSAGE_SET_MESSAGE, value);
  }

  public static int computeDoubleSizeNoTag(final double value) {
    return LITTLE_ENDIAN_64_SIZE;
  }

  public static int computeFloatSizeNoTag(final float value) {
    return LITTLE_ENDIAN_32_SIZE;
  }

  public static int computeUInt64SizeNoTag(final long value) {
    return computeRawVarint64Size(value);
  }

  public static int computeInt64SizeNoTag(final long value) {
    return computeRawVarint64Size(value);
  }

  public static int computeInt32SizeNoTag(final int value) {
    if (value >= 0) {
      return computeRawVarint32Size(value);
    } else {

      return 10;
    }
  }

  public static int computeFixed64SizeNoTag(final long value) {
    return LITTLE_ENDIAN_64_SIZE;
  }

  public static int computeFixed32SizeNoTag(final int value) {
    return LITTLE_ENDIAN_32_SIZE;
  }

  public static int computeBoolSizeNoTag(final boolean value) {
    return 1;
  }

  public static int computeStringSizeNoTag(final String value) {
    try {
      final byte[] bytes = value.getBytes("UTF-8");
      return computeRawVarint32Size(bytes.length) +
             bytes.length;
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("UTF-8 not supported.", e);
    }
  }

  public static int computeGroupSizeNoTag(final MessageLite value) {
    return value.getSerializedSize();
  }

  @Deprecated
  public static int computeUnknownGroupSizeNoTag(final MessageLite value) {
    return computeGroupSizeNoTag(value);
  }

  public static int computeMessageSizeNoTag(final MessageLite value) {
    final int size = value.getSerializedSize();
    return computeRawVarint32Size(size) + size;
  }

  public static int computeLazyFieldSizeNoTag(final LazyField value) {
    final int size = value.getSerializedSize();
    return computeRawVarint32Size(size) + size;
  }

  public static int computeBytesSizeNoTag(final ByteString value) {
    return computeRawVarint32Size(value.size()) +
           value.size();
  }

  public static int computeUInt32SizeNoTag(final int value) {
    return computeRawVarint32Size(value);
  }

  public static int computeEnumSizeNoTag(final int value) {
    return computeInt32SizeNoTag(value);
  }

  public static int computeSFixed32SizeNoTag(final int value) {
    return LITTLE_ENDIAN_32_SIZE;
  }

  public static int computeSFixed64SizeNoTag(final long value) {
    return LITTLE_ENDIAN_64_SIZE;
  }

  public static int computeSInt32SizeNoTag(final int value) {
    return computeRawVarint32Size(encodeZigZag32(value));
  }

  public static int computeSInt64SizeNoTag(final long value) {
    return computeRawVarint64Size(encodeZigZag64(value));
  }

  private void refreshBuffer() throws IOException {
    if (output == null) {

      throw new OutOfSpaceException();
    }

    output.write(buffer, 0, position);
    position = 0;
  }

  public void flush() throws IOException {
    if (output != null) {
      refreshBuffer();
    }
  }

  public int spaceLeft() {
    if (output == null) {
      return limit - position;
    } else {
      throw new UnsupportedOperationException(
        "spaceLeft() can only be called on CodedOutputStreams that are " +
        "writing to a flat array.");
    }
  }

  public void checkNoSpaceLeft() {
    if (spaceLeft() != 0) {
      throw new IllegalStateException(
        "Did not write as much data as expected.");
    }
  }

  public static class OutOfSpaceException extends IOException {
    private static final long serialVersionUID = -6947486886997889499L;

    OutOfSpaceException() {
      super("CodedOutputStream was writing to a flat byte array and ran " +
            "out of space.");
    }
  }

  public void writeRawByte(final byte value) throws IOException {
    if (position == limit) {
      refreshBuffer();
    }

    buffer[position++] = value;
  }

  public void writeRawByte(final int value) throws IOException {
    writeRawByte((byte) value);
  }

  public void writeRawBytes(final ByteString value) throws IOException {
    writeRawBytes(value, 0, value.size());
  }

  public void writeRawBytes(final byte[] value) throws IOException {
    writeRawBytes(value, 0, value.length);
  }

  public void writeRawBytes(final byte[] value, int offset, int length)
                            throws IOException {
    if (limit - position >= length) {

      System.arraycopy(value, offset, buffer, position, length);
      position += length;
    } else {

      final int bytesWritten = limit - position;
      System.arraycopy(value, offset, buffer, position, bytesWritten);
      offset += bytesWritten;
      length -= bytesWritten;
      position = limit;
      refreshBuffer();

      if (length <= limit) {

        System.arraycopy(value, offset, buffer, 0, length);
        position = length;
      } else {

        output.write(value, offset, length);
      }
    }
  }

  public void writeRawBytes(final ByteString value, int offset, int length)
                            throws IOException {
    if (limit - position >= length) {

      value.copyTo(buffer, offset, position, length);
      position += length;
    } else {

      final int bytesWritten = limit - position;
      value.copyTo(buffer, offset, position, bytesWritten);
      offset += bytesWritten;
      length -= bytesWritten;
      position = limit;
      refreshBuffer();

      if (length <= limit) {

        value.copyTo(buffer, offset, 0, length);
        position = length;
      } else {

        InputStream inputStreamFrom = value.newInput();
        if (offset != inputStreamFrom.skip(offset)) {
          throw new IllegalStateException("Skip failed? Should never happen.");
        }

        while (length > 0) {
          int bytesToRead = Math.min(length, limit);
          int bytesRead = inputStreamFrom.read(buffer, 0, bytesToRead);
          if (bytesRead != bytesToRead) {
            throw new IllegalStateException("Read failed? Should never happen");
          }
          output.write(buffer, 0, bytesRead);
          length -= bytesRead;
        }
      }
    }
  }

  public void writeTag(final int fieldNumber, final int wireType)
                       throws IOException {
    writeRawVarint32(WireFormat.makeTag(fieldNumber, wireType));
  }

  public static int computeTagSize(final int fieldNumber) {
    return computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 0));
  }

  public void writeRawVarint32(int value) throws IOException {
    while (true) {
      if ((value & ~0x7F) == 0) {
        writeRawByte(value);
        return;
      } else {
        writeRawByte((value & 0x7F) | 0x80);
        value >>>= 7;
      }
    }
  }

  public static int computeRawVarint32Size(final int value) {
    if ((value & (0xffffffff <<  7)) == 0) return 1;
    if ((value & (0xffffffff << 14)) == 0) return 2;
    if ((value & (0xffffffff << 21)) == 0) return 3;
    if ((value & (0xffffffff << 28)) == 0) return 4;
    return 5;
  }

  public void writeRawVarint64(long value) throws IOException {
    while (true) {
      if ((value & ~0x7FL) == 0) {
        writeRawByte((int)value);
        return;
      } else {
        writeRawByte(((int)value & 0x7F) | 0x80);
        value >>>= 7;
      }
    }
  }

  public static int computeRawVarint64Size(final long value) {
    if ((value & (0xffffffffffffffffL <<  7)) == 0) return 1;
    if ((value & (0xffffffffffffffffL << 14)) == 0) return 2;
    if ((value & (0xffffffffffffffffL << 21)) == 0) return 3;
    if ((value & (0xffffffffffffffffL << 28)) == 0) return 4;
    if ((value & (0xffffffffffffffffL << 35)) == 0) return 5;
    if ((value & (0xffffffffffffffffL << 42)) == 0) return 6;
    if ((value & (0xffffffffffffffffL << 49)) == 0) return 7;
    if ((value & (0xffffffffffffffffL << 56)) == 0) return 8;
    if ((value & (0xffffffffffffffffL << 63)) == 0) return 9;
    return 10;
  }

  public void writeRawLittleEndian32(final int value) throws IOException {
    writeRawByte((value      ) & 0xFF);
    writeRawByte((value >>  8) & 0xFF);
    writeRawByte((value >> 16) & 0xFF);
    writeRawByte((value >> 24) & 0xFF);
  }

  public static final int LITTLE_ENDIAN_32_SIZE = 4;

  public void writeRawLittleEndian64(final long value) throws IOException {
    writeRawByte((int)(value      ) & 0xFF);
    writeRawByte((int)(value >>  8) & 0xFF);
    writeRawByte((int)(value >> 16) & 0xFF);
    writeRawByte((int)(value >> 24) & 0xFF);
    writeRawByte((int)(value >> 32) & 0xFF);
    writeRawByte((int)(value >> 40) & 0xFF);
    writeRawByte((int)(value >> 48) & 0xFF);
    writeRawByte((int)(value >> 56) & 0xFF);
  }

  public static final int LITTLE_ENDIAN_64_SIZE = 8;

  public static int encodeZigZag32(final int n) {

    return (n << 1) ^ (n >> 31);
  }

  public static long encodeZigZag64(final long n) {

    return (n << 1) ^ (n >> 63);
  }
}
