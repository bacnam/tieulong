package org.apache.http.nio;

import java.io.IOException;
import java.nio.channels.FileChannel;

public interface FileContentDecoder extends ContentDecoder {
  long transfer(FileChannel paramFileChannel, long paramLong1, long paramLong2) throws IOException;
}

