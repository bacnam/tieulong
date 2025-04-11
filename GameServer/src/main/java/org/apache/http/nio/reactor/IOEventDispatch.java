package org.apache.http.nio.reactor;

public interface IOEventDispatch {
    public static final String CONNECTION_KEY = "http.connection";

    void connected(IOSession paramIOSession);

    void inputReady(IOSession paramIOSession);

    void outputReady(IOSession paramIOSession);

    void timeout(IOSession paramIOSession);

    void disconnected(IOSession paramIOSession);
}

