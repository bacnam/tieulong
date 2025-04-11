package javolution.io;

import javolution.text.Text;

import java.io.IOException;
import java.io.Writer;

public final class AppendableWriter
        extends Writer {
    private Appendable _output;
    private char[] _tmpBuffer;
    private final CharSequence _tmpBufferAsCharSequence = new CharSequence() {
        public int length() {
            return AppendableWriter.this._tmpBuffer.length;
        }

        public char charAt(int index) {
            return AppendableWriter.this._tmpBuffer[index];
        }

        public CharSequence subSequence(int start, int end) {
            throw new UnsupportedOperationException();
        }
    };

    public AppendableWriter setOutput(Appendable output) {
        if (this._output != null)
            throw new IllegalStateException("Writer not closed or reset");
        this._output = output;
        return this;
    }

    public void write(char c) throws IOException {
        if (this._output == null)
            throw new IOException("Writer closed");
        this._output.append(c);
    }

    public void write(int c) throws IOException {
        if (this._output == null)
            throw new IOException("Writer closed");
        this._output.append((char) c);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        if (this._output == null)
            throw new IOException("Writer closed");
        this._tmpBuffer = cbuf;
        this._output.append(this._tmpBufferAsCharSequence, off, off + len);
        this._tmpBuffer = null;
    }

    public void write(String str, int off, int len) throws IOException {
        if (this._output == null)
            throw new IOException("Writer closed");
        Object obj = str;
        if (obj instanceof CharSequence) {
            this._output.append((CharSequence) obj);
        } else {
            this._output.append((CharSequence) Text.valueOf(str));
        }
    }

    public void write(CharSequence csq) throws IOException {
        if (this._output == null)
            throw new IOException("Writer closed");
        this._output.append(csq);
    }

    public void flush() {
    }

    public void close() {
        if (this._output != null) {
            reset();
        }
    }

    public void reset() {
        this._output = null;
        this._tmpBuffer = null;
    }
}

