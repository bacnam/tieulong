package org.apache.thrift;

import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.TMemoryInputTransport;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class TDeserializer {
    private final TProtocol protocol_;
    private final TMemoryInputTransport trans_;

    public TDeserializer() {
        this(new TBinaryProtocol.Factory());
    }

    public TDeserializer(TProtocolFactory protocolFactory) {
        trans_ = new TMemoryInputTransport();
        protocol_ = protocolFactory.getProtocol(trans_);
    }

    public void deserialize(TBase base, byte[] bytes) throws TException {
        try {
            trans_.reset(bytes);
            base.read(protocol_);
        } finally {
            protocol_.reset();
        }
    }

    public void deserialize(TBase base, String data, String charset) throws TException {
        try {
            deserialize(base, data.getBytes(charset));
        } catch (UnsupportedEncodingException uex) {
            throw new TException("JVM DOES NOT SUPPORT ENCODING: " + charset);
        } finally {
            protocol_.reset();
        }
    }

    public void partialDeserialize(TBase tb, byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        try {
            if (locateField(bytes, fieldIdPathFirst, fieldIdPathRest) != null) {

                tb.read(protocol_);
            }
        } catch (Exception e) {
            throw new TException(e);
        } finally {
            protocol_.reset();
        }
    }

    public Boolean partialDeserializeBool(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        return (Boolean) partialDeserializeField(TType.BOOL, bytes, fieldIdPathFirst, fieldIdPathRest);
    }

    public Byte partialDeserializeByte(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        return (Byte) partialDeserializeField(TType.BYTE, bytes, fieldIdPathFirst, fieldIdPathRest);
    }

    public Double partialDeserializeDouble(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        return (Double) partialDeserializeField(TType.DOUBLE, bytes, fieldIdPathFirst, fieldIdPathRest);
    }

    public Short partialDeserializeI16(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        return (Short) partialDeserializeField(TType.I16, bytes, fieldIdPathFirst, fieldIdPathRest);
    }

    public Integer partialDeserializeI32(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        return (Integer) partialDeserializeField(TType.I32, bytes, fieldIdPathFirst, fieldIdPathRest);
    }

    public Long partialDeserializeI64(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        return (Long) partialDeserializeField(TType.I64, bytes, fieldIdPathFirst, fieldIdPathRest);
    }

    public String partialDeserializeString(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        return (String) partialDeserializeField(TType.STRING, bytes, fieldIdPathFirst, fieldIdPathRest);
    }

    public ByteBuffer partialDeserializeByteArray(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {

        return (ByteBuffer) partialDeserializeField((byte) 100, bytes, fieldIdPathFirst, fieldIdPathRest);
    }

    public Short partialDeserializeSetFieldIdInUnion(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        try {
            TField field = locateField(bytes, fieldIdPathFirst, fieldIdPathRest);
            if (field != null) {
                protocol_.readStructBegin();
                return protocol_.readFieldBegin().id;
            }
            return null;
        } catch (Exception e) {
            throw new TException(e);
        } finally {
            protocol_.reset();
        }
    }

    private Object partialDeserializeField(byte ttype, byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        try {
            TField field = locateField(bytes, fieldIdPathFirst, fieldIdPathRest);
            if (field != null) {

                switch (ttype) {
                    case TType.BOOL:
                        if (field.type == TType.BOOL) {
                            return protocol_.readBool();
                        }
                        break;
                    case TType.BYTE:
                        if (field.type == TType.BYTE) {
                            return protocol_.readByte();
                        }
                        break;
                    case TType.DOUBLE:
                        if (field.type == TType.DOUBLE) {
                            return protocol_.readDouble();
                        }
                        break;
                    case TType.I16:
                        if (field.type == TType.I16) {
                            return protocol_.readI16();
                        }
                        break;
                    case TType.I32:
                        if (field.type == TType.I32) {
                            return protocol_.readI32();
                        }
                        break;
                    case TType.I64:
                        if (field.type == TType.I64) {
                            return protocol_.readI64();
                        }
                        break;
                    case TType.STRING:
                        if (field.type == TType.STRING) {
                            return protocol_.readString();
                        }
                        break;
                    case 100:
                        if (field.type == TType.STRING) {
                            return protocol_.readBinary();
                        }
                        break;
                }
            }
            return null;
        } catch (Exception e) {
            throw new TException(e);
        } finally {
            protocol_.reset();
        }
    }

    private TField locateField(byte[] bytes, TFieldIdEnum fieldIdPathFirst, TFieldIdEnum... fieldIdPathRest) throws TException {
        trans_.reset(bytes);

        TFieldIdEnum[] fieldIdPath = new TFieldIdEnum[fieldIdPathRest.length + 1];
        fieldIdPath[0] = fieldIdPathFirst;
        for (int i = 0; i < fieldIdPathRest.length; i++) {
            fieldIdPath[i + 1] = fieldIdPathRest[i];
        }

        int curPathIndex = 0;

        TField field = null;

        protocol_.readStructBegin();

        while (curPathIndex < fieldIdPath.length) {
            field = protocol_.readFieldBegin();

            if (field.type == TType.STOP || field.id > fieldIdPath[curPathIndex].getThriftFieldId()) {
                return null;
            }

            if (field.id != fieldIdPath[curPathIndex].getThriftFieldId()) {

                TProtocolUtil.skip(protocol_, field.type);
                protocol_.readFieldEnd();
            } else {

                curPathIndex++;
                if (curPathIndex < fieldIdPath.length) {
                    protocol_.readStructBegin();
                }
            }
        }
        return field;
    }

    public void fromString(TBase base, String data) throws TException {
        deserialize(base, data.getBytes());
    }
}
