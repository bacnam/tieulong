package org.apache.http.nio;

import java.io.IOException;

public interface IOControl {
  void requestInput();

  void suspendInput();

  void requestOutput();

  void suspendOutput();

  void shutdown() throws IOException;
}

