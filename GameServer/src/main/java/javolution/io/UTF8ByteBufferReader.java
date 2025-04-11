package javolution.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.Reader;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class UTF8ByteBufferReader
        extends Reader {
    private ByteBuffer _byteBuffer;
    private int _code;
    private int _moreBytes;

    public UTF8ByteBufferReader setInput(ByteBuffer byteBuffer) {
        if (this._byteBuffer != null)
            throw new IllegalStateException("Reader not closed or reset");
        this._byteBuffer = byteBuffer;
        return this;
    }

    public boolean ready() throws IOException {
        if (this._byteBuffer != null) {
            return this._byteBuffer.hasRemaining();
        }
        throw new IOException("Reader closed");
    }

    public void close() throws IOException {
        if (this._byteBuffer != null) {
            reset();
        }
    }

    public int read() throws IOException {
        if (this._byteBuffer != null) {
            if (this._byteBuffer.hasRemaining()) {
                byte b = this._byteBuffer.get();
                return (b >= 0) ? b : read2(b);
            }
            return -1;
        }

        throw new IOException("Reader closed");
    }

    private int read2(byte b) throws IOException {
        try {
            if (b >= 0 && this._moreBytes == 0) {
                return b;
            }
            if ((b & 0xC0) == 128 && this._moreBytes != 0) {

                this._code = this._code << 6 | b & 0x3F;
                if (--this._moreBytes == 0) {
                    return this._code;
                }
                return read2(this._byteBuffer.get());
            }
            if ((b & 0xE0) == 192 && this._moreBytes == 0) {

                this._code = b & 0x1F;
                this._moreBytes = 1;
                return read2(this._byteBuffer.get());
            }
            if ((b & 0xF0) == 224 && this._moreBytes == 0) {

                this._code = b & 0xF;
                this._moreBytes = 2;
                return read2(this._byteBuffer.get());
            }
            if ((b & 0xF8) == 240 && this._moreBytes == 0) {

                this._code = b & 0x7;
                this._moreBytes = 3;
                return read2(this._byteBuffer.get());
            }
            if ((b & 0xFC) == 248 && this._moreBytes == 0) {

                this._code = b & 0x3;
                this._moreBytes = 4;
                return read2(this._byteBuffer.get());
            }
            if ((b & 0xFE) == 252 && this._moreBytes == 0) {

                this._code = b & 0x1;
                this._moreBytes = 5;
                return read2(this._byteBuffer.get());
            }
            throw new CharConversionException("Invalid UTF-8 Encoding");
        } catch (BufferUnderflowException e) {
            throw new CharConversionException("Incomplete Sequence");
        }
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        if (this._byteBuffer == null)
            throw new IOException("Reader closed");
        int off_plus_len = off + len;
        int remaining = this._byteBuffer.remaining();
        if (remaining <= 0)
            return -1;
        for (int i = off; i < off_plus_len; ) {
            if (remaining-- > 0) {
                byte b = this._byteBuffer.get();
                if (b >= 0) {
                    cbuf[i++] = (char) b;
                    continue;
                }
                if (i < off_plus_len - 1) {
                    int code = read2(b);
                    remaining = this._byteBuffer.remaining();
                    if (code < 65536) {
                        cbuf[i++] = (char) code;
                        continue;
                    }
                    if (code <= 1114111) {
                        cbuf[i++] = (char) ((code - 65536 >> 10) + 55296);
                        cbuf[i++] = (char) ((code - 65536 & 0x3FF) + 56320);
                        continue;
                    }
                    throw new CharConversionException("Cannot convert U+" + Integer.toHexString(code) + " to char (code greater than U+10FFFF)");
                }

                this._byteBuffer.position(this._byteBuffer.position() - 1);
                remaining++;
                return i - off;
            }

            return i - off;
        }

        return len;
    }

    public void read(Appendable dest) throws IOException {
        if (this._byteBuffer == null)
            throw new IOException("Reader closed");
        while (this._byteBuffer.hasRemaining()) {
            byte b = this._byteBuffer.get();
            if (b >= 0) {
                dest.append((char) b);
                continue;
            }
            int code = read2(b);
            if (code < 65536) {
                dest.append((char) code);
                continue;
            }
            if (code <= 1114111) {
                dest.append((char) ((code - 65536 >> 10) + 55296));
                dest.append((char) ((code - 65536 & 0x3FF) + 56320));
                continue;
            }
            throw new CharConversionException("Cannot convert U+" + Integer.toHexString(code) + " to char (code greater than U+10FFFF)");
        }
    }

    public void reset() {
        this._byteBuffer = null;
        this._code = 0;
        this._moreBytes = 0;
    }

    public UTF8ByteBufferReader setByteBuffer(ByteBuffer byteBuffer) {
        return setInput(byteBuffer);
    }
}

