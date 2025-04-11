package org.apache.http.nio.util;

import org.apache.http.nio.ContentDecoder;

import java.io.IOException;

public interface ContentInputBuffer {
    @Deprecated
    int consumeContent(ContentDecoder paramContentDecoder) throws IOException;

    void reset();

    int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;

    int read() throws IOException;
}

