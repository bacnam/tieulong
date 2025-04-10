package org.apache.http.nio.reactor.ssl;

import java.nio.ByteBuffer;

public interface SSLBuffer {
  ByteBuffer acquire();

  void release();

  boolean isAcquired();

  boolean hasData();
}

