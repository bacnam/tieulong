package org.apache.http.impl.nio.reactor;

import org.apache.http.nio.reactor.ListenerEndpoint;

public interface ListenerEndpointClosedCallback {
  void endpointClosed(ListenerEndpoint paramListenerEndpoint);
}

