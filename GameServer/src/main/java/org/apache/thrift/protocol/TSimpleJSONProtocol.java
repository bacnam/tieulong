package org.apache.thrift.protocol;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Stack;

public class TSimpleJSONProtocol extends TProtocol {

    public static final byte[] COMMA = new byte[]{','};
    public static final byte[] COLON = new byte[]{':'};
    public static final byte[] LBRACE = new byte[]{'{'};
    public static final byte[] RBRACE = new byte[]{'}'};
    public static final byte[] LBRACKET = new byte[]{'['};
    public static final byte[] RBRACKET = new byte[]{']'};
    public static final char QUOTE = '"';
    private static final TStruct ANONYMOUS_STRUCT = new TStruct();
    private static final TField ANONYMOUS_FIELD = new TField();
    private static final TMessage EMPTY_MESSAGE = new TMessage();
    private static final TSet EMPTY_SET = new TSet();
    private static final TList EMPTY_LIST = new TList();
    private static final TMap EMPTY_MAP = new TMap();
    protected final Context BASE_CONTEXT = new Context();
    protected Stack<Context> writeContextStack_ = new Stack<Context>();
    protected Context writeContext_ = BASE_CONTEXT;

    public TSimpleJSONProtocol(TTransport trans) {
        super(trans);
    }

    protected void pushWriteContext(Context c) {
        writeContextStack_.push(writeContext_);
        writeContext_ = c;
    }

    protected void popWriteContext() {
        writeContext_ = writeContextStack_.pop();
    }

    public void writeMessageBegin(TMessage message) throws TException {
        trans_.write(LBRACKET);
        pushWriteContext(new ListContext());
        writeString(message.name);
        writeByte(message.type);
        writeI32(message.seqid);
    }

    public void writeMessageEnd() throws TException {
        popWriteContext();
        trans_.write(RBRACKET);
    }

    public void writeStructBegin(TStruct struct) throws TException {
        writeContext_.write();
        trans_.write(LBRACE);
        pushWriteContext(new StructContext());
    }

    public void writeStructEnd() throws TException {
        popWriteContext();
        trans_.write(RBRACE);
    }

    public void writeFieldBegin(TField field) throws TException {

        writeString(field.name);
    }

    public void writeFieldEnd() {
    }

    public void writeFieldStop() {
    }

    public void writeMapBegin(TMap map) throws TException {
        writeContext_.write();
        trans_.write(LBRACE);
        pushWriteContext(new StructContext());

    }

    public void writeMapEnd() throws TException {
        popWriteContext();
        trans_.write(RBRACE);
    }

    public void writeListBegin(TList list) throws TException {
        writeContext_.write();
        trans_.write(LBRACKET);
        pushWriteContext(new ListContext());

    }

    public void writeListEnd() throws TException {
        popWriteContext();
        trans_.write(RBRACKET);
    }

    public void writeSetBegin(TSet set) throws TException {
        writeContext_.write();
        trans_.write(LBRACKET);
        pushWriteContext(new ListContext());

    }

    public void writeSetEnd() throws TException {
        popWriteContext();
        trans_.write(RBRACKET);
    }

    public void writeBool(boolean b) throws TException {
        writeByte(b ? (byte) 1 : (byte) 0);
    }

    public void writeByte(byte b) throws TException {
        writeI32(b);
    }

    public void writeI16(short i16) throws TException {
        writeI32(i16);
    }

    public void writeI32(int i32) throws TException {
        writeContext_.write();
        _writeStringData(Integer.toString(i32));
    }

    public void _writeStringData(String s) throws TException {
        try {
            byte[] b = s.getBytes("UTF-8");
            trans_.write(b);
        } catch (UnsupportedEncodingException uex) {
            throw new TException("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    public void writeI64(long i64) throws TException {
        writeContext_.write();
        _writeStringData(Long.toString(i64));
    }

    public void writeDouble(double dub) throws TException {
        writeContext_.write();
        _writeStringData(Double.toString(dub));
    }

    public void writeString(String str) throws TException {
        writeContext_.write();
        int length = str.length();
        StringBuffer escape = new StringBuffer(length + 16);
        escape.append(QUOTE);
        for (int i = 0; i < length; ++i) {
            char c = str.charAt(i);
            switch (c) {
                case '"':
                case '\\':
                    escape.append('\\');
                    escape.append(c);
                    break;
                case '\b':
                    escape.append('\\');
                    escape.append('b');
                    break;
                case '\f':
                    escape.append('\\');
                    escape.append('f');
                    break;
                case '\n':
                    escape.append('\\');
                    escape.append('n');
                    break;
                case '\r':
                    escape.append('\\');
                    escape.append('r');
                    break;
                case '\t':
                    escape.append('\\');
                    escape.append('t');
                    break;
                default:

                    if (c < ' ') {
                        String hex = Integer.toHexString(c);
                        escape.append('\\');
                        escape.append('u');
                        for (int j = 4; j > hex.length(); --j) {
                            escape.append('0');
                        }
                        escape.append(hex);
                    } else {
                        escape.append(c);
                    }
                    break;
            }
        }
        escape.append(QUOTE);
        _writeStringData(escape.toString());
    }

    public void writeBinary(ByteBuffer bin) throws TException {
        try {

            writeString(new String(bin.array(), bin.position() + bin.arrayOffset(), bin.limit() - bin.position() - bin.arrayOffset(), "UTF-8"));
        } catch (UnsupportedEncodingException uex) {
            throw new TException("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    public TMessage readMessageBegin() throws TException {

        return EMPTY_MESSAGE;
    }

    public void readMessageEnd() {
    }

    public TStruct readStructBegin() {

        return ANONYMOUS_STRUCT;
    }

    public void readStructEnd() {
    }

    public TField readFieldBegin() throws TException {

        return ANONYMOUS_FIELD;
    }

    public void readFieldEnd() {
    }

    public TMap readMapBegin() throws TException {

        return EMPTY_MAP;
    }

    public void readMapEnd() {
    }

    public TList readListBegin() throws TException {

        return EMPTY_LIST;
    }

    public void readListEnd() {
    }

    public TSet readSetBegin() throws TException {

        return EMPTY_SET;
    }

    public void readSetEnd() {
    }

    public boolean readBool() throws TException {
        return (readByte() == 1);
    }

    public byte readByte() throws TException {

        return 0;
    }

    public short readI16() throws TException {

        return 0;
    }

    public int readI32() throws TException {

        return 0;
    }

    public long readI64() throws TException {

        return 0;
    }

    public double readDouble() throws TException {

        return 0;
    }

    public String readString() throws TException {

        return "";
    }

    public String readStringBody(int size) throws TException {

        return "";
    }

    public ByteBuffer readBinary() throws TException {

        return ByteBuffer.wrap(new byte[0]);
    }

    public static class Factory implements TProtocolFactory {
        public TProtocol getProtocol(TTransport trans) {
            return new TSimpleJSONProtocol(trans);
        }
    }

    protected class Context {
        protected void write() throws TException {
        }
    }

    protected class ListContext extends Context {
        protected boolean first_ = true;

        protected void write() throws TException {
            if (first_) {
                first_ = false;
            } else {
                trans_.write(COMMA);
            }
        }
    }

    protected class StructContext extends Context {
        protected boolean first_ = true;
        protected boolean colon_ = true;

        protected void write() throws TException {
            if (first_) {
                first_ = false;
                colon_ = true;
            } else {
                trans_.write(colon_ ? COLON : COMMA);
                colon_ = !colon_;
            }
        }
    }

}
