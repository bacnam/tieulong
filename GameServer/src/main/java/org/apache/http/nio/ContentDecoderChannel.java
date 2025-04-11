package org.apache.http.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class ContentDecoderChannel
        implements ReadableByteChannel {
    private final ContentDecoder decoder;

    public ContentDecoderChannel(ContentDecoder decoder) {
        this.decoder = decoder;
    }

    public int read(ByteBuffer dst) throws IOException {
        return this.decoder.read(dst);
    }

    public void close() {
    }

    public boolean isOpen() {
        return true;
    }
}

