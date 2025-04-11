package org.apache.http.impl.nio.codecs;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.nio.FileContentDecoder;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.util.Args;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@NotThreadSafe
public class IdentityDecoder
        extends AbstractContentDecoder
        implements FileContentDecoder {
    public IdentityDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, HttpTransportMetricsImpl metrics) {
        super(channel, buffer, metrics);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int read(ByteBuffer dst) throws IOException {
        int bytesRead;
        Args.notNull(dst, "Byte buffer");
        if (this.completed) {
            return -1;
        }

        if (this.buffer.hasData()) {
            bytesRead = this.buffer.read(dst);
        } else {
            bytesRead = readFromChannel(dst);
        }
        if (bytesRead == -1) {
            this.completed = true;
        }
        return bytesRead;
    }

    public long transfer(FileChannel dst, long position, long count) throws IOException {
        long bytesRead;
        if (dst == null) {
            return 0L;
        }
        if (this.completed) {
            return 0L;
        }

        if (this.buffer.hasData()) {
            dst.position(position);
            bytesRead = this.buffer.read(dst);
        } else {
            if (this.channel.isOpen()) {
                if (position > dst.size()) {
                    throw new IOException("Position past end of file [" + position + " > " + dst.size() + "]");
                }

                bytesRead = dst.transferFrom(this.channel, position, count);
                if (count > 0L && bytesRead == 0L) {
                    bytesRead = this.buffer.fill(this.channel);
                }
            } else {
                bytesRead = -1L;
            }
            if (bytesRead > 0L) {
                this.metrics.incrementBytesTransferred(bytesRead);
            }
        }
        if (bytesRead == -1L) {
            this.completed = true;
        }
        return bytesRead;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[identity; completed: ");
        sb.append(this.completed);
        sb.append("]");
        return sb.toString();
    }
}

