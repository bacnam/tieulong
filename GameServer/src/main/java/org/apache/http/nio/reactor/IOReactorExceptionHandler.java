package org.apache.http.nio.reactor;

import java.io.IOException;

public interface IOReactorExceptionHandler {
  boolean handle(IOException paramIOException);

  boolean handle(RuntimeException paramRuntimeException);
}

