

package org.apache.thrift.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.thrift.ShortStack;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;

public final class TCompactProtocol extends TProtocol {

  private final static TStruct ANONYMOUS_STRUCT = new TStruct("");
  private final static TField TSTOP = new TField("", TType.STOP, (short)0);

  private final static byte[] ttypeToCompactType = new byte[16];

  static {
    ttypeToCompactType[TType.STOP] = TType.STOP;
    ttypeToCompactType[TType.BOOL] = Types.BOOLEAN_TRUE;
    ttypeToCompactType[TType.BYTE] = Types.BYTE;
    ttypeToCompactType[TType.I16] = Types.I16;
    ttypeToCompactType[TType.I32] = Types.I32;
    ttypeToCompactType[TType.I64] = Types.I64;
    ttypeToCompactType[TType.DOUBLE] = Types.DOUBLE;
    ttypeToCompactType[TType.STRING] = Types.BINARY;
    ttypeToCompactType[TType.LIST] = Types.LIST;
    ttypeToCompactType[TType.SET] = Types.SET;
    ttypeToCompactType[TType.MAP] = Types.MAP;
    ttypeToCompactType[TType.STRUCT] = Types.STRUCT;
  }

  public static class Factory implements TProtocolFactory {
    public Factory() {}

    public TProtocol getProtocol(TTransport trans) {
      return new TCompactProtocol(trans);
    }
  }

  private static final byte PROTOCOL_ID = (byte)0x82;
  private static final byte VERSION = 1;
  private static final byte VERSION_MASK = 0x1f; 
  private static final byte TYPE_MASK = (byte)0xE0; 
  private static final int  TYPE_SHIFT_AMOUNT = 5;

  private static class Types {
    public static final byte BOOLEAN_TRUE   = 0x01;
    public static final byte BOOLEAN_FALSE  = 0x02;
    public static final byte BYTE           = 0x03;
    public static final byte I16            = 0x04;
    public static final byte I32            = 0x05;
    public static final byte I64            = 0x06;
    public static final byte DOUBLE         = 0x07;
    public static final byte BINARY         = 0x08;
    public static final byte LIST           = 0x09;
    public static final byte SET            = 0x0A;
    public static final byte MAP            = 0x0B;
    public static final byte STRUCT         = 0x0C;
  }

  private ShortStack lastField_ = new ShortStack(15);

  private short lastFieldId_ = 0;

  private TField booleanField_ = null;

  private Boolean boolValue_ = null;

  public TCompactProtocol(TTransport transport) {
    super(transport);
  }

  @Override
  public void reset() {
    lastField_.clear();
    lastFieldId_ = 0;
  }

  public void writeMessageBegin(TMessage message) throws TException {
    writeByteDirect(PROTOCOL_ID);
    writeByteDirect((VERSION & VERSION_MASK) | ((message.type << TYPE_SHIFT_AMOUNT) & TYPE_MASK));
    writeVarint32(message.seqid);
    writeString(message.name);
  }

  public void writeStructBegin(TStruct struct) throws TException {
    lastField_.push(lastFieldId_);
    lastFieldId_ = 0;
  }

  public void writeStructEnd() throws TException {
    lastFieldId_ = lastField_.pop();
  }

  public void writeFieldBegin(TField field) throws TException {
    if (field.type == TType.BOOL) {

      booleanField_ = field;
    } else {
      writeFieldBeginInternal(field, (byte)-1);
    }
  }

  private void writeFieldBeginInternal(TField field, byte typeOverride) throws TException {

    byte typeToWrite = typeOverride == -1 ? getCompactType(field.type) : typeOverride;

    if (field.id > lastFieldId_ && field.id - lastFieldId_ <= 15) {

      writeByteDirect((field.id - lastFieldId_) << 4 | typeToWrite);
    } else {

      writeByteDirect(typeToWrite);
      writeI16(field.id);
    }

    lastFieldId_ = field.id;

  }

  public void writeFieldStop() throws TException {
    writeByteDirect(TType.STOP);
  }

  public void writeMapBegin(TMap map) throws TException {
    if (map.size == 0) {
      writeByteDirect(0);
    } else {
      writeVarint32(map.size);
      writeByteDirect(getCompactType(map.keyType) << 4 | getCompactType(map.valueType));
    }
  }

  public void writeListBegin(TList list) throws TException {
    writeCollectionBegin(list.elemType, list.size);
  }

  public void writeSetBegin(TSet set) throws TException {
    writeCollectionBegin(set.elemType, set.size);
  }

  public void writeBool(boolean b) throws TException {
    if (booleanField_ != null) {

      writeFieldBeginInternal(booleanField_, b ? Types.BOOLEAN_TRUE : Types.BOOLEAN_FALSE);
      booleanField_ = null;
    } else {

      writeByteDirect(b ? Types.BOOLEAN_TRUE : Types.BOOLEAN_FALSE);
    }
  }

  public void writeByte(byte b) throws TException {
    writeByteDirect(b);
  }

  public void writeI16(short i16) throws TException {
    writeVarint32(intToZigZag(i16));
  }

  public void writeI32(int i32) throws TException {
    writeVarint32(intToZigZag(i32));
  }

  public void writeI64(long i64) throws TException {
    writeVarint64(longToZigzag(i64));
  }

  public void writeDouble(double dub) throws TException {
    byte[] data = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
    fixedLongToBytes(Double.doubleToLongBits(dub), data, 0);
    trans_.write(data);
  }

  public void writeString(String str) throws TException {
    try {
      byte[] bytes = str.getBytes("UTF-8");
      writeBinary(bytes, 0, bytes.length);
    } catch (UnsupportedEncodingException e) {
      throw new TException("UTF-8 not supported!");
    }
  }

  public void writeBinary(ByteBuffer bin) throws TException {
    int length = bin.limit() - bin.position() - bin.arrayOffset();
    writeBinary(bin.array(), bin.position() + bin.arrayOffset(), length);
  }

  private void writeBinary(byte[] buf, int offset, int length) throws TException {
    writeVarint32(length);
    trans_.write(buf, offset, length);
  }

  public void writeMessageEnd() throws TException {}
  public void writeMapEnd() throws TException {}
  public void writeListEnd() throws TException {}
  public void writeSetEnd() throws TException {}
  public void writeFieldEnd() throws TException {}

  protected void writeCollectionBegin(byte elemType, int size) throws TException {
    if (size <= 14) {
      writeByteDirect(size << 4 | getCompactType(elemType));
    } else {
      writeByteDirect(0xf0 | getCompactType(elemType));
      writeVarint32(size);
    }
  }

  byte[] i32buf = new byte[5];
  private void writeVarint32(int n) throws TException {
    int idx = 0;
    while (true) {
      if ((n & ~0x7F) == 0) {
        i32buf[idx++] = (byte)n;

        break;

      } else {
        i32buf[idx++] = (byte)((n & 0x7F) | 0x80);

        n >>>= 7;
      }
    }
    trans_.write(i32buf, 0, idx);
  }

  byte[] varint64out = new byte[10];
  private void writeVarint64(long n) throws TException {
    int idx = 0;
    while (true) {
      if ((n & ~0x7FL) == 0) {
        varint64out[idx++] = (byte)n;
        break;
      } else {
        varint64out[idx++] = ((byte)((n & 0x7F) | 0x80));
        n >>>= 7;
      }
    }
    trans_.write(varint64out, 0, idx);
  }

  private long longToZigzag(long l) {
    return (l << 1) ^ (l >> 63);
  }

  private int intToZigZag(int n) {
    return (n << 1) ^ (n >> 31);
  }

  private void fixedLongToBytes(long n, byte[] buf, int off) {
    buf[off+0] = (byte)( n        & 0xff);
    buf[off+1] = (byte)((n >> 8 ) & 0xff);
    buf[off+2] = (byte)((n >> 16) & 0xff);
    buf[off+3] = (byte)((n >> 24) & 0xff);
    buf[off+4] = (byte)((n >> 32) & 0xff);
    buf[off+5] = (byte)((n >> 40) & 0xff);
    buf[off+6] = (byte)((n >> 48) & 0xff);
    buf[off+7] = (byte)((n >> 56) & 0xff);
  }

  private byte[] byteDirectBuffer = new byte[1];
  private void writeByteDirect(byte b) throws TException {
    byteDirectBuffer[0] = b;
    trans_.write(byteDirectBuffer);
  }

  private void writeByteDirect(int n) throws TException {
    writeByteDirect((byte)n);
  }

  public TMessage readMessageBegin() throws TException {
    byte protocolId = readByte();
    if (protocolId != PROTOCOL_ID) {
      throw new TProtocolException("Expected protocol id " + Integer.toHexString(PROTOCOL_ID) + " but got " + Integer.toHexString(protocolId));
    }
    byte versionAndType = readByte();
    byte version = (byte)(versionAndType & VERSION_MASK);
    if (version != VERSION) {
      throw new TProtocolException("Expected version " + VERSION + " but got " + version);
    }
    byte type = (byte)((versionAndType >> TYPE_SHIFT_AMOUNT) & 0x03);
    int seqid = readVarint32();
    String messageName = readString();
    return new TMessage(messageName, type, seqid);
  }

  public TStruct readStructBegin() throws TException {
    lastField_.push(lastFieldId_);
    lastFieldId_ = 0;
    return ANONYMOUS_STRUCT;
  }

  public void readStructEnd() throws TException {

    lastFieldId_ = lastField_.pop();
  }

  public TField readFieldBegin() throws TException {
    byte type = readByte();

    if (type == TType.STOP) {
      return TSTOP;
    }

    short fieldId;

    short modifier = (short)((type & 0xf0) >> 4);
    if (modifier == 0) {

      fieldId = readI16();
    } else {

      fieldId = (short)(lastFieldId_ + modifier);
    }

    TField field = new TField("", getTType((byte)(type & 0x0f)), fieldId);

    if (isBoolType(type)) {

      boolValue_ = (byte)(type & 0x0f) == Types.BOOLEAN_TRUE ? Boolean.TRUE : Boolean.FALSE;
    } 

    lastFieldId_ = field.id;
    return field;
  }

  public TMap readMapBegin() throws TException {
    int size = readVarint32();
    byte keyAndValueType = size == 0 ? 0 : readByte();
    return new TMap(getTType((byte)(keyAndValueType >> 4)), getTType((byte)(keyAndValueType & 0xf)), size);
  }

  public TList readListBegin() throws TException {
    byte size_and_type = readByte();
    int size = (size_and_type >> 4) & 0x0f;
    if (size == 15) {
      size = readVarint32();
    }
    byte type = getTType(size_and_type);
    return new TList(type, size);
  }

  public TSet readSetBegin() throws TException {
    return new TSet(readListBegin());
  }

  public boolean readBool() throws TException {
    if (boolValue_ != null) {
      boolean result = boolValue_.booleanValue();
      boolValue_ = null;
      return result;
    }
    return readByte() == Types.BOOLEAN_TRUE;
  }

  byte[] byteRawBuf = new byte[1];

  public byte readByte() throws TException {
    byte b;
    if (trans_.getBytesRemainingInBuffer() > 0) {
      b = trans_.getBuffer()[trans_.getBufferPosition()];
      trans_.consumeBuffer(1);
    } else {
      trans_.readAll(byteRawBuf, 0, 1);
      b = byteRawBuf[0];
    }
    return b;
  }

  public short readI16() throws TException {
    return (short)zigzagToInt(readVarint32());
  }

  public int readI32() throws TException {
    return zigzagToInt(readVarint32());
  }

  public long readI64() throws TException {
    return zigzagToLong(readVarint64());
  }

  public double readDouble() throws TException {
    byte[] longBits = new byte[8];
    trans_.readAll(longBits, 0, 8);
    return Double.longBitsToDouble(bytesToLong(longBits));
  }

  public String readString() throws TException {
    int length = readVarint32();

    if (length == 0) {
      return "";
    }

    try {
      if (trans_.getBytesRemainingInBuffer() >= length) {
        String str = new String(trans_.getBuffer(), trans_.getBufferPosition(), length, "UTF-8");
        trans_.consumeBuffer(length);
        return str;
      } else {
        return new String(readBinary(length), "UTF-8");
      }
    } catch (UnsupportedEncodingException e) {
      throw new TException("UTF-8 not supported!");
    }
  }

  public ByteBuffer readBinary() throws TException {
    int length = readVarint32();
    if (length == 0) return ByteBuffer.wrap(new byte[0]);

    byte[] buf = new byte[length];
    trans_.readAll(buf, 0, length);
    return ByteBuffer.wrap(buf);
  }

  private byte[] readBinary(int length) throws TException {
    if (length == 0) return new byte[0];

    byte[] buf = new byte[length];
    trans_.readAll(buf, 0, length);
    return buf;
  }

  public void readMessageEnd() throws TException {}
  public void readFieldEnd() throws TException {}
  public void readMapEnd() throws TException {}
  public void readListEnd() throws TException {}
  public void readSetEnd() throws TException {}

  private int readVarint32() throws TException {
    int result = 0;
    int shift = 0;
    if (trans_.getBytesRemainingInBuffer() >= 5) {
      byte[] buf = trans_.getBuffer();
      int pos = trans_.getBufferPosition();
      int off = 0;
      while (true) {
        byte b = buf[pos+off];
        result |= (int) (b & 0x7f) << shift;
        if ((b & 0x80) != 0x80) break;
        shift += 7;
        off++;
      }
      trans_.consumeBuffer(off+1);
    } else {
      while (true) {
        byte b = readByte();
        result |= (int) (b & 0x7f) << shift;
        if ((b & 0x80) != 0x80) break;
        shift += 7;
      }
    }
    return result;
  }

  private long readVarint64() throws TException {
    int shift = 0;
    long result = 0;
    if (trans_.getBytesRemainingInBuffer() >= 10) {
      byte[] buf = trans_.getBuffer();
      int pos = trans_.getBufferPosition();
      int off = 0;
      while (true) {
        byte b = buf[pos+off];
        result |= (long) (b & 0x7f) << shift;
        if ((b & 0x80) != 0x80) break;
        shift += 7;
        off++;
      }
      trans_.consumeBuffer(off+1);
    } else {
      while (true) {
        byte b = readByte();
        result |= (long) (b & 0x7f) << shift;
        if ((b & 0x80) != 0x80) break;
        shift +=7;
      }
    }
    return result;
  }

  private int zigzagToInt(int n) {
    return (n >>> 1) ^ -(n & 1);
  }

  private long zigzagToLong(long n) {
    return (n >>> 1) ^ -(n & 1);
  }

  private long bytesToLong(byte[] bytes) {
    return
      ((bytes[7] & 0xffL) << 56) |
      ((bytes[6] & 0xffL) << 48) |
      ((bytes[5] & 0xffL) << 40) |
      ((bytes[4] & 0xffL) << 32) |
      ((bytes[3] & 0xffL) << 24) |
      ((bytes[2] & 0xffL) << 16) |
      ((bytes[1] & 0xffL) <<  8) |
      ((bytes[0] & 0xffL));
  }

  private boolean isBoolType(byte b) {
    int lowerNibble = b & 0x0f;
    return lowerNibble == Types.BOOLEAN_TRUE || lowerNibble == Types.BOOLEAN_FALSE;
  }

  private byte getTType(byte type) throws TProtocolException {
    switch ((byte)(type & 0x0f)) {
      case TType.STOP:
        return TType.STOP;
      case Types.BOOLEAN_FALSE:
      case Types.BOOLEAN_TRUE:
        return TType.BOOL;
      case Types.BYTE:
        return TType.BYTE;
      case Types.I16:
        return TType.I16;
      case Types.I32:
        return TType.I32;
      case Types.I64:
        return TType.I64;
      case Types.DOUBLE:
        return TType.DOUBLE;
      case Types.BINARY:
        return TType.STRING;
      case Types.LIST:
        return TType.LIST;
      case Types.SET:
        return TType.SET;
      case Types.MAP:
        return TType.MAP;
      case Types.STRUCT:
        return TType.STRUCT;
      default:
        throw new TProtocolException("don't know what type: " + (byte)(type & 0x0f));
    }
  }

  private byte getCompactType(byte ttype) {
    return ttypeToCompactType[ttype];
  }
}
