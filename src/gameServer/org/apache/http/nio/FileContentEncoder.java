package org.apache.http.nio;

import java.io.IOException;
import java.nio.channels.FileChannel;

public interface FileContentEncoder extends ContentEncoder {
  long transfer(FileChannel paramFileChannel, long paramLong1, long paramLong2) throws IOException;
}

