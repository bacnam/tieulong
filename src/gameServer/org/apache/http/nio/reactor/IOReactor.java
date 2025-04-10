package org.apache.http.nio.reactor;

import java.io.IOException;

public interface IOReactor {
  IOReactorStatus getStatus();

  void execute(IOEventDispatch paramIOEventDispatch) throws IOException;

  void shutdown(long paramLong) throws IOException;

  void shutdown() throws IOException;
}

