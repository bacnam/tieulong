package org.apache.http.nio.reactor;

import java.net.SocketAddress;

public interface ConnectingIOReactor extends IOReactor {
    SessionRequest connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, Object paramObject, SessionRequestCallback paramSessionRequestCallback);
}

