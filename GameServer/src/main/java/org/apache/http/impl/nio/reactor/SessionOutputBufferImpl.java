package org.apache.http.impl.nio.reactor;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.nio.reactor.SessionOutputBuffer;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.ExpandableBuffer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.CharsetUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.*;

@NotThreadSafe
public class SessionOutputBufferImpl
        extends ExpandableBuffer
        implements SessionOutputBuffer {
    private static final byte[] CRLF = new byte[]{13, 10};

    private final CharsetEncoder charencoder;

    private final int lineBuffersize;

    private CharBuffer charbuffer;

    public SessionOutputBufferImpl(int buffersize, int lineBuffersize, CharsetEncoder charencoder, ByteBufferAllocator allocator) {
        super(buffersize, (allocator != null) ? allocator : (ByteBufferAllocator) HeapByteBufferAllocator.INSTANCE);
        this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
        this.charencoder = charencoder;
    }

    @Deprecated
    public SessionOutputBufferImpl(int buffersize, int lineBuffersize, ByteBufferAllocator allocator, HttpParams params) {
        super(buffersize, allocator);
        this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
        String charsetName = (String) params.getParameter("http.protocol.element-charset");
        Charset charset = CharsetUtils.lookup(charsetName);
        if (charset != null) {
            this.charencoder = charset.newEncoder();
            CodingErrorAction a1 = (CodingErrorAction) params.getParameter("http.malformed.input.action");

            this.charencoder.onMalformedInput((a1 != null) ? a1 : CodingErrorAction.REPORT);
            CodingErrorAction a2 = (CodingErrorAction) params.getParameter("http.unmappable.input.action");

            this.charencoder.onUnmappableCharacter((a2 != null) ? a2 : CodingErrorAction.REPORT);
        } else {
            this.charencoder = null;
        }
    }

    @Deprecated
    public SessionOutputBufferImpl(int buffersize, int linebuffersize, HttpParams params) {
        this(buffersize, linebuffersize, (ByteBufferAllocator) HeapByteBufferAllocator.INSTANCE, params);
    }

    public SessionOutputBufferImpl(int buffersize) {
        this(buffersize, 256, (CharsetEncoder) null, (ByteBufferAllocator) HeapByteBufferAllocator.INSTANCE);
    }

    public SessionOutputBufferImpl(int buffersize, int linebuffersize, Charset charset) {
        this(buffersize, linebuffersize, (charset != null) ? charset.newEncoder() : null, (ByteBufferAllocator) HeapByteBufferAllocator.INSTANCE);
    }

    public SessionOutputBufferImpl(int buffersize, int linebuffersize) {
        this(buffersize, linebuffersize, (CharsetEncoder) null, (ByteBufferAllocator) HeapByteBufferAllocator.INSTANCE);
    }

    public void reset(HttpParams params) {
        clear();
    }

    public int flush(WritableByteChannel channel) throws IOException {
        Args.notNull(channel, "Channel");
        setOutputMode();
        return channel.write(this.buffer);
    }

    public void write(ByteBuffer src) {
        if (src == null) {
            return;
        }
        setInputMode();
        int requiredCapacity = this.buffer.position() + src.remaining();
        ensureCapacity(requiredCapacity);
        this.buffer.put(src);
    }

    public void write(ReadableByteChannel src) throws IOException {
        if (src == null) {
            return;
        }
        setInputMode();
        src.read(this.buffer);
    }

    private void write(byte[] b) {
        if (b == null) {
            return;
        }
        setInputMode();
        int off = 0;
        int len = b.length;
        int requiredCapacity = this.buffer.position() + len;
        ensureCapacity(requiredCapacity);
        this.buffer.put(b, 0, len);
    }

    private void writeCRLF() {
        write(CRLF);
    }

    public void writeLine(CharArrayBuffer linebuffer) throws CharacterCodingException {
        if (linebuffer == null) {
            return;
        }
        setInputMode();

        if (linebuffer.length() > 0) {
            if (this.charencoder == null) {
                int requiredCapacity = this.buffer.position() + linebuffer.length();
                ensureCapacity(requiredCapacity);
                if (this.buffer.hasArray()) {
                    byte[] b = this.buffer.array();
                    int len = linebuffer.length();
                    int off = this.buffer.position();
                    for (int i = 0; i < len; i++) {
                        b[off + i] = (byte) linebuffer.charAt(i);
                    }
                    this.buffer.position(off + len);
                } else {
                    for (int i = 0; i < linebuffer.length(); i++) {
                        this.buffer.put((byte) linebuffer.charAt(i));
                    }
                }
            } else {
                if (this.charbuffer == null) {
                    this.charbuffer = CharBuffer.allocate(this.lineBuffersize);
                }
                this.charencoder.reset();

                int remaining = linebuffer.length();
                int offset = 0;
                while (remaining > 0) {
                    int l = this.charbuffer.remaining();
                    boolean eol = false;
                    if (remaining <= l) {
                        l = remaining;

                        eol = true;
                    }
                    this.charbuffer.put(linebuffer.buffer(), offset, l);
                    this.charbuffer.flip();

                    boolean bool1 = true;
                    while (bool1) {
                        CoderResult result = this.charencoder.encode(this.charbuffer, this.buffer, eol);
                        if (result.isError()) {
                            result.throwException();
                        }
                        if (result.isOverflow()) {
                            expand();
                        }
                        bool1 = !result.isUnderflow();
                    }
                    this.charbuffer.compact();
                    offset += l;
                    remaining -= l;
                }

                boolean retry = true;
                while (retry) {
                    CoderResult result = this.charencoder.flush(this.buffer);
                    if (result.isError()) {
                        result.throwException();
                    }
                    if (result.isOverflow()) {
                        expand();
                    }
                    retry = !result.isUnderflow();
                }
            }
        }
        writeCRLF();
    }

    public void writeLine(String s) throws IOException {
        if (s == null) {
            return;
        }
        if (s.length() > 0) {
            CharArrayBuffer tmp = new CharArrayBuffer(s.length());
            tmp.append(s);
            writeLine(tmp);
        } else {
            write(CRLF);
        }
    }
}

