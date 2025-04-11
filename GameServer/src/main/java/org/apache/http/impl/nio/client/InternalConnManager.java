package org.apache.http.impl.nio.client;

import org.apache.http.nio.NHttpClientConnection;

interface InternalConnManager {
    void releaseConnection();

    void abortConnection();

    NHttpClientConnection getConnection();
}

