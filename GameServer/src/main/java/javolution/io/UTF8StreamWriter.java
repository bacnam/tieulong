package javolution.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public final class UTF8StreamWriter
        extends Writer {
    private final byte[] _bytes;
    private OutputStream _outputStream;
    private int _index;
    private char _highSurrogate;

    public UTF8StreamWriter() {
        this._bytes = new byte[2048];
    }

    public UTF8StreamWriter(int capacity) {
        this._bytes = new byte[capacity];
    }

    public UTF8StreamWriter setOutput(OutputStream out) {
        if (this._outputStream != null)
            throw new IllegalStateException("Writer not closed or reset");
        this._outputStream = out;
        return this;
    }

    public void write(char c) throws IOException {
        if (c < '?' || c > '?') {
            write(c);
        } else if (c < '?') {
            this._highSurrogate = c;
        } else {
            int code = (this._highSurrogate - 55296 << 10) + c - 56320 + 65536;

            write(code);
        }
    }

    public void write(int code) throws IOException {
        if ((code & 0xFFFFFF80) == 0) {
            this._bytes[this._index] = (byte) code;
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
        } else {
            write2(code);
        }
    }

    private void write2(int c) throws IOException {
        if ((c & 0xFFFFF800) == 0) {
            this._bytes[this._index] = (byte) (0xC0 | c >> 6);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
        } else if ((c & 0xFFFF0000) == 0) {
            this._bytes[this._index] = (byte) (0xE0 | c >> 12);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 6 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
        } else if ((c & 0xFF200000) == 0) {
            this._bytes[this._index] = (byte) (0xF0 | c >> 18);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 12 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 6 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
        } else if ((c & 0xF4000000) == 0) {
            this._bytes[this._index] = (byte) (0xF8 | c >> 24);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 18 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 12 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 6 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
        } else if ((c & Integer.MIN_VALUE) == 0) {
            this._bytes[this._index] = (byte) (0xFC | c >> 30);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 24 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 18 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 12 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c >> 6 & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
            this._bytes[this._index] = (byte) (0x80 | c & 0x3F);
            if (++this._index >= this._bytes.length) {
                flushBuffer();
            }
        } else {
            throw new CharConversionException("Illegal character U+" + Integer.toHexString(c));
        }
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        int off_plus_len = off + len;
        for (int i = off; i < off_plus_len; ) {
            char c = cbuf[i++];
            if (c < '') {
                this._bytes[this._index] = (byte) c;
                if (++this._index >= this._bytes.length)
                    flushBuffer();
                continue;
            }
            write(c);
        }
    }

    public void write(String str, int off, int len) throws IOException {
        int off_plus_len = off + len;
        for (int i = off; i < off_plus_len; ) {
            char c = str.charAt(i++);
            if (c < '') {
                this._bytes[this._index] = (byte) c;
                if (++this._index >= this._bytes.length)
                    flushBuffer();
                continue;
            }
            write(c);
        }
    }

    public void write(CharSequence csq) throws IOException {
        int length = csq.length();
        for (int i = 0; i < length; ) {
            char c = csq.charAt(i++);
            if (c < '') {
                this._bytes[this._index] = (byte) c;
                if (++this._index >= this._bytes.length)
                    flushBuffer();
                continue;
            }
            write(c);
        }
    }

    public void flush() throws IOException {
        flushBuffer();
        this._outputStream.flush();
    }

    public void close() throws IOException {
        if (this._outputStream != null) {
            flushBuffer();
            this._outputStream.close();
            reset();
        }
    }

    private void flushBuffer() throws IOException {
        if (this._outputStream == null)
            throw new IOException("Stream closed");
        this._outputStream.write(this._bytes, 0, this._index);
        this._index = 0;
    }

    public void reset() {
        this._highSurrogate = Character.MIN_VALUE;
        this._index = 0;
        this._outputStream = null;
    }

    public UTF8StreamWriter setOutputStream(OutputStream out) {
        return setOutput(out);
    }
}

