package org.apache.http.entity;

import org.apache.http.util.Args;

import java.io.*;

public class EntityTemplate
        extends AbstractHttpEntity {
    private final ContentProducer contentproducer;

    public EntityTemplate(ContentProducer contentproducer) {
        this.contentproducer = (ContentProducer) Args.notNull(contentproducer, "Content producer");
    }

    public long getContentLength() {
        return -1L;
    }

    public InputStream getContent() throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        writeTo(buf);
        return new ByteArrayInputStream(buf.toByteArray());
    }

    public boolean isRepeatable() {
        return true;
    }

    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        this.contentproducer.writeTo(outstream);
    }

    public boolean isStreaming() {
        return false;
    }
}

