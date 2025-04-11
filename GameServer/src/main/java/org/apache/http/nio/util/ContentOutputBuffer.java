package org.apache.http.nio.util;

import org.apache.http.nio.ContentEncoder;

import java.io.IOException;

public interface ContentOutputBuffer {
    @Deprecated
    int produceContent(ContentEncoder paramContentEncoder) throws IOException;

    void reset();

    @Deprecated
    void flush() throws IOException;

    void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;

    void write(int paramInt) throws IOException;

    void writeCompleted() throws IOException;
}

