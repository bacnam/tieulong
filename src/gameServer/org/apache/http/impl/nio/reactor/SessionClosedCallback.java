package org.apache.http.impl.nio.reactor;

import org.apache.http.nio.reactor.IOSession;

public interface SessionClosedCallback {
  void sessionClosed(IOSession paramIOSession);
}

