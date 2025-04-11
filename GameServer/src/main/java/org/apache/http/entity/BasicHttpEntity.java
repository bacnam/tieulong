package org.apache.http.entity;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.impl.io.EmptyInputStream;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@NotThreadSafe
public class BasicHttpEntity
        extends AbstractHttpEntity {
    private InputStream content;
    private long length = -1L;

    public long getContentLength() {
        return this.length;
    }

    public void setContentLength(long len) {
        this.length = len;
    }

    public InputStream getContent() throws IllegalStateException {
        Asserts.check((this.content != null), "Content has not been provided");
        return this.content;
    }

    public void setContent(InputStream instream) {
        this.content = instream;
    }

    public boolean isRepeatable() {
        return false;
    }

    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        InputStream instream = getContent();

        try {
            byte[] tmp = new byte[4096];
            int l;
            while ((l = instream.read(tmp)) != -1) {
                outstream.write(tmp, 0, l);
            }
        } finally {
            instream.close();
        }
    }

    public boolean isStreaming() {
        return (this.content != null && this.content != EmptyInputStream.INSTANCE);
    }
}

