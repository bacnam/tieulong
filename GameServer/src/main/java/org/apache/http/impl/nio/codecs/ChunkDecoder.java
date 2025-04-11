package org.apache.http.impl.nio.codecs;

import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.message.BufferedHeader;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

@NotThreadSafe
public class ChunkDecoder
        extends AbstractContentDecoder {
    private static final int READ_CONTENT = 0;
    private static final int READ_FOOTERS = 1;
    private static final int COMPLETED = 2;
    private final MessageConstraints constraints;
    private final List<CharArrayBuffer> trailerBufs;
    private int state;
    private boolean endOfChunk;
    private boolean endOfStream;
    private CharArrayBuffer lineBuf;
    private int chunkSize;
    private int pos;
    private Header[] footers;

    public ChunkDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, MessageConstraints constraints, HttpTransportMetricsImpl metrics) {
        super(channel, buffer, metrics);
        this.state = 0;
        this.chunkSize = -1;
        this.pos = 0;
        this.endOfChunk = false;
        this.endOfStream = false;
        this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
        this.trailerBufs = new ArrayList<CharArrayBuffer>();
    }

    public ChunkDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, HttpTransportMetricsImpl metrics) {
        this(channel, buffer, (MessageConstraints) null, metrics);
    }

    private void readChunkHead() throws IOException {
        if (this.lineBuf == null) {
            this.lineBuf = new CharArrayBuffer(32);
        } else {
            this.lineBuf.clear();
        }
        if (this.endOfChunk) {
            if (this.buffer.readLine(this.lineBuf, this.endOfStream)) {
                if (!this.lineBuf.isEmpty()) {
                    throw new MalformedChunkCodingException("CRLF expected at end of chunk");
                }
            } else {
                if (this.buffer.length() > 2 || this.endOfStream) {
                    throw new MalformedChunkCodingException("CRLF expected at end of chunk");
                }
                return;
            }
            this.endOfChunk = false;
        }
        boolean lineComplete = this.buffer.readLine(this.lineBuf, this.endOfStream);
        int maxLineLen = this.constraints.getMaxLineLength();
        if (maxLineLen > 0 && (this.lineBuf.length() > maxLineLen || (!lineComplete && this.buffer.length() > maxLineLen))) {

            throw new MessageConstraintException("Maximum line length limit exceeded");
        }
        if (lineComplete) {
            int separator = this.lineBuf.indexOf(59);
            if (separator < 0) {
                separator = this.lineBuf.length();
            }
            try {
                String s = this.lineBuf.substringTrimmed(0, separator);
                this.chunkSize = Integer.parseInt(s, 16);
            } catch (NumberFormatException e) {
                throw new MalformedChunkCodingException("Bad chunk header");
            }
            this.pos = 0;
        } else if (this.endOfStream) {
            throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
        }
    }

    private void parseHeader() throws IOException {
        CharArrayBuffer current = this.lineBuf;
        int count = this.trailerBufs.size();
        if ((this.lineBuf.charAt(0) == ' ' || this.lineBuf.charAt(0) == '\t') && count > 0) {

            CharArrayBuffer previous = this.trailerBufs.get(count - 1);
            int i = 0;
            while (i < current.length()) {
                char ch = current.charAt(i);
                if (ch != ' ' && ch != '\t') {
                    break;
                }
                i++;
            }
            int maxLineLen = this.constraints.getMaxLineLength();
            if (maxLineLen > 0 && previous.length() + 1 + current.length() - i > maxLineLen) {
                throw new MessageConstraintException("Maximum line length limit exceeded");
            }
            previous.append(' ');
            previous.append(current, i, current.length() - i);
        } else {
            this.trailerBufs.add(current);
            this.lineBuf = null;
        }
    }

    private void processFooters() throws IOException {
        int count = this.trailerBufs.size();
        if (count > 0) {
            this.footers = new Header[this.trailerBufs.size()];
            for (int i = 0; i < this.trailerBufs.size(); i++) {
                try {
                    this.footers[i] = (Header) new BufferedHeader(this.trailerBufs.get(i));
                } catch (ParseException ex) {
                    throw new IOException(ex.getMessage());
                }
            }
        }
        this.trailerBufs.clear();
    }

    public int read(ByteBuffer dst) throws IOException {
        Args.notNull(dst, "Byte buffer");
        if (this.state == 2) {
            return -1;
        }

        int totalRead = 0;
        while (this.state != 2) {
            int maxLen;
            int len;
            if (!this.buffer.hasData() || this.chunkSize == -1) {
                int bytesRead = fillBufferFromChannel();
                if (bytesRead == -1) {
                    this.endOfStream = true;
                }
            }

            switch (this.state) {

                case 0:
                    if (this.chunkSize == -1) {
                        readChunkHead();
                        if (this.chunkSize == -1) {
                            return totalRead;
                        }
                        if (this.chunkSize == 0) {

                            this.chunkSize = -1;
                            this.state = 1;
                            continue;
                        }
                    }
                    maxLen = this.chunkSize - this.pos;
                    len = this.buffer.read(dst, maxLen);
                    if (len > 0) {
                        this.pos += len;
                        totalRead += len;
                    } else if (!this.buffer.hasData() && this.endOfStream) {
                        this.state = 2;
                        this.completed = true;
                        throw new TruncatedChunkException("Truncated chunk ( expected size: " + this.chunkSize + "; actual size: " + this.pos + ")");
                    }

                    if (this.pos == this.chunkSize) {

                        this.chunkSize = -1;
                        this.pos = 0;
                        this.endOfChunk = true;
                        continue;
                    }
                    return totalRead;
                case 1:
                    if (this.lineBuf == null) {
                        this.lineBuf = new CharArrayBuffer(32);
                    } else {
                        this.lineBuf.clear();
                    }
                    if (!this.buffer.readLine(this.lineBuf, this.endOfStream)) {

                        if (this.endOfStream) {
                            this.state = 2;
                            this.completed = true;
                        }
                        return totalRead;
                    }
                    if (this.lineBuf.length() > 0) {
                        int maxHeaderCount = this.constraints.getMaxHeaderCount();
                        if (maxHeaderCount > 0 && this.trailerBufs.size() >= maxHeaderCount) {
                            throw new MessageConstraintException("Maximum header count exceeded");
                        }
                        parseHeader();
                        continue;
                    }
                    this.state = 2;
                    this.completed = true;
                    processFooters();
            }

        }
        return totalRead;
    }

    public Header[] getFooters() {
        if (this.footers != null) {
            return (Header[]) this.footers.clone();
        }
        return new Header[0];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[chunk-coded; completed: ");
        sb.append(this.completed);
        sb.append("]");
        return sb.toString();
    }
}

