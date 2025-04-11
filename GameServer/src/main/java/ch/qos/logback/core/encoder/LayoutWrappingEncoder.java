package ch.qos.logback.core.encoder;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class LayoutWrappingEncoder<E>
        extends EncoderBase<E> {
    protected Layout<E> layout;
    private Charset charset;
    private boolean immediateFlush = true;

    public boolean isImmediateFlush() {
        return this.immediateFlush;
    }

    public void setImmediateFlush(boolean immediateFlush) {
        this.immediateFlush = immediateFlush;
    }

    public Layout<E> getLayout() {
        return this.layout;
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void init(OutputStream os) throws IOException {
        super.init(os);
        writeHeader();
    }

    void writeHeader() throws IOException {
        if (this.layout != null && this.outputStream != null) {
            StringBuilder sb = new StringBuilder();
            appendIfNotNull(sb, this.layout.getFileHeader());
            appendIfNotNull(sb, this.layout.getPresentationHeader());
            if (sb.length() > 0) {
                sb.append(CoreConstants.LINE_SEPARATOR);

                this.outputStream.write(convertToBytes(sb.toString()));
                this.outputStream.flush();
            }
        }
    }

    public void close() throws IOException {
        writeFooter();
    }

    void writeFooter() throws IOException {
        if (this.layout != null && this.outputStream != null) {
            StringBuilder sb = new StringBuilder();
            appendIfNotNull(sb, this.layout.getPresentationFooter());
            appendIfNotNull(sb, this.layout.getFileFooter());
            if (sb.length() > 0) {
                this.outputStream.write(convertToBytes(sb.toString()));
                this.outputStream.flush();
            }
        }
    }

    private byte[] convertToBytes(String s) {
        if (this.charset == null) {
            return s.getBytes();
        }
        try {
            return s.getBytes(this.charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("An existing charset cannot possibly be unsupported.");
        }
    }

    public void doEncode(E event) throws IOException {
        String txt = this.layout.doLayout(event);
        this.outputStream.write(convertToBytes(txt));
        if (this.immediateFlush)
            this.outputStream.flush();
    }

    public boolean isStarted() {
        return false;
    }

    public void start() {
        this.started = true;
    }

    public void stop() {
        this.started = false;
        if (this.outputStream != null) {
            try {
                this.outputStream.flush();
            } catch (IOException e) {
            }
        }
    }

    private void appendIfNotNull(StringBuilder sb, String s) {
        if (s != null)
            sb.append(s);
    }
}

