package org.apache.http.nio;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ContentDecoder {
    int read(ByteBuffer paramByteBuffer) throws IOException;

    boolean isCompleted();
}

