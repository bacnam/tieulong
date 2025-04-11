package javolution.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public final class UTF8StreamReader
        extends Reader {
    private final byte[] _bytes;
    private InputStream _inputStream;
    private int _start;
    private int _end;
    private int _code;
    private int _moreBytes;

    public UTF8StreamReader() {
        this._bytes = new byte[2048];
    }

    public UTF8StreamReader(int capacity) {
        this._bytes = new byte[capacity];
    }

    public UTF8StreamReader setInput(InputStream inStream) {
        if (this._inputStream != null)
            throw new IllegalStateException("Reader not closed or reset");
        this._inputStream = inStream;
        return this;
    }

    public boolean ready() throws IOException {
        if (this._inputStream == null)
            throw new IOException("Stream closed");
        return (this._end - this._start > 0 || this._inputStream.available() != 0);
    }

    public void close() throws IOException {
        if (this._inputStream != null) {
            this._inputStream.close();
            reset();
        }
    }

    public int read() throws IOException {
        byte b = this._bytes[this._start];
        return (b >= 0 && this._start++ < this._end) ? b : read2();
    }

    private int read2() throws IOException {
        if (this._start < this._end) {
            byte b = this._bytes[this._start++];

            if (b >= 0 && this._moreBytes == 0) {
                return b;
            }
            if ((b & 0xC0) == 128 && this._moreBytes != 0) {

                this._code = this._code << 6 | b & 0x3F;
                if (--this._moreBytes == 0) {
                    return this._code;
                }
                return read2();
            }
            if ((b & 0xE0) == 192 && this._moreBytes == 0) {

                this._code = b & 0x1F;
                this._moreBytes = 1;
                return read2();
            }
            if ((b & 0xF0) == 224 && this._moreBytes == 0) {

                this._code = b & 0xF;
                this._moreBytes = 2;
                return read2();
            }
            if ((b & 0xF8) == 240 && this._moreBytes == 0) {

                this._code = b & 0x7;
                this._moreBytes = 3;
                return read2();
            }
            if ((b & 0xFC) == 248 && this._moreBytes == 0) {

                this._code = b & 0x3;
                this._moreBytes = 4;
                return read2();
            }
            if ((b & 0xFE) == 252 && this._moreBytes == 0) {

                this._code = b & 0x1;
                this._moreBytes = 5;
                return read2();
            }
            throw new CharConversionException("Invalid UTF-8 Encoding");
        }

        if (this._inputStream == null)
            throw new IOException("No input stream or stream closed");
        this._start = 0;
        this._end = this._inputStream.read(this._bytes, 0, this._bytes.length);
        if (this._end > 0) {
            return read2();
        }
        if (this._moreBytes == 0) {
            return -1;
        }
        throw new CharConversionException("Unexpected end of stream");
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        if (this._inputStream == null)
            throw new IOException("No input stream or stream closed");
        if (this._start >= this._end) {
            this._start = 0;
            this._end = this._inputStream.read(this._bytes, 0, this._bytes.length);
            if (this._end <= 0) {
                return this._end;
            }
        }
        int off_plus_len = off + len;
        for (int i = off; i < off_plus_len; ) {

            byte b = this._bytes[this._start];
            if (b >= 0 && ++this._start < this._end) {
                cbuf[i++] = (char) b;
                continue;
            }
            if (b < 0) {
                if (i < off_plus_len - 1) {
                    int code = read2();
                    if (code < 65536) {
                        cbuf[i++] = (char) code;
                    } else if (code <= 1114111) {
                        cbuf[i++] = (char) ((code - 65536 >> 10) + 55296);
                        cbuf[i++] = (char) ((code - 65536 & 0x3FF) + 56320);
                    } else {
                        throw new CharConversionException("Cannot convert U+" + Integer.toHexString(code) + " to char (code greater than U+10FFFF)");
                    }

                    if (this._start < this._end) {
                        continue;
                    }
                }
                return i - off;
            }
            cbuf[i++] = (char) b;
            return i - off;
        }

        return len;
    }

    public void read(Appendable dest) throws IOException {
        if (this._inputStream == null)
            throw new IOException("No input stream or stream closed");
        while (true) {
            if (this._start >= this._end) {
                this._start = 0;
                this._end = this._inputStream.read(this._bytes, 0, this._bytes.length);
                if (this._end <= 0) {
                    break;
                }
            }
            byte b = this._bytes[this._start];
            if (b >= 0) {
                dest.append((char) b);
                this._start++;
                continue;
            }
            int code = read2();
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
        this._code = 0;
        this._end = 0;
        this._inputStream = null;
        this._moreBytes = 0;
        this._start = 0;
    }

    public UTF8StreamReader setInputStream(InputStream inStream) {
        return setInput(inStream);
    }
}

