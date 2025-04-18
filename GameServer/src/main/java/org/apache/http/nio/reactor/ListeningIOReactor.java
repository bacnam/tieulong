package org.apache.http.nio.reactor;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Set;

public interface ListeningIOReactor extends IOReactor {
  ListenerEndpoint listen(SocketAddress paramSocketAddress);

  void pause() throws IOException;

  void resume() throws IOException;

  Set<ListenerEndpoint> getEndpoints();
}

