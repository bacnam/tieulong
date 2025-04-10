package org.apache.http.nio;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ContentEncoder {
  int write(ByteBuffer paramByteBuffer) throws IOException;

  void complete() throws IOException;

  boolean isCompleted();
}

