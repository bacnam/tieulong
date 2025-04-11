package org.apache.http.nio.reactor;

import java.net.Socket;

public interface SocketAccessor {
    Socket getSocket();
}

