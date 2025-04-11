package org.apache.http.impl.nio.reactor;

import org.apache.http.annotation.Immutable;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLIOSession;
import org.apache.http.util.Asserts;

import java.io.IOException;

@Immutable
public abstract class AbstractIODispatch<T>
        implements IOEventDispatch {
    protected abstract T createConnection(IOSession paramIOSession);

    protected abstract void onConnected(T paramT);

    protected abstract void onClosed(T paramT);

    protected abstract void onException(T paramT, IOException paramIOException);

    protected abstract void onInputReady(T paramT);

    protected abstract void onOutputReady(T paramT);

    protected abstract void onTimeout(T paramT);

    private void ensureNotNull(T conn) {
        Asserts.notNull(conn, "HTTP connection");
    }

    public void connected(IOSession session) {
        T conn = (T) session.getAttribute("http.connection");
        try {
            if (conn == null) {
                conn = createConnection(session);
                session.setAttribute("http.connection", conn);
            }
            onConnected(conn);
            SSLIOSession ssliosession = (SSLIOSession) session.getAttribute("http.session.ssl");

            if (ssliosession != null) {
                try {
                    synchronized (ssliosession) {
                        if (!ssliosession.isInitialized()) {
                            ssliosession.initialize();
                        }
                    }
                } catch (IOException ex) {
                    onException(conn, ex);
                    ssliosession.shutdown();
                }
            }
        } catch (RuntimeException ex) {
            session.shutdown();
            throw ex;
        }
    }

    public void disconnected(IOSession session) {
        T conn = (T) session.getAttribute("http.connection");
        if (conn != null) {
            onClosed(conn);
        }
    }

    public void inputReady(IOSession session) {
        T conn = (T) session.getAttribute("http.connection");
        try {
            ensureNotNull(conn);
            SSLIOSession ssliosession = (SSLIOSession) session.getAttribute("http.session.ssl");

            if (ssliosession == null) {
                onInputReady(conn);
            } else {
                try {
                    if (!ssliosession.isInitialized()) {
                        ssliosession.initialize();
                    }
                    if (ssliosession.isAppInputReady()) {
                        onInputReady(conn);
                    }
                    ssliosession.inboundTransport();
                } catch (IOException ex) {
                    onException(conn, ex);
                    ssliosession.shutdown();
                }
            }
        } catch (RuntimeException ex) {
            session.shutdown();
            throw ex;
        }
    }

    public void outputReady(IOSession session) {
        T conn = (T) session.getAttribute("http.connection");
        try {
            ensureNotNull(conn);
            SSLIOSession ssliosession = (SSLIOSession) session.getAttribute("http.session.ssl");

            if (ssliosession == null) {
                onOutputReady(conn);
            } else {
                try {
                    if (!ssliosession.isInitialized()) {
                        ssliosession.initialize();
                    }
                    if (ssliosession.isAppOutputReady()) {
                        onOutputReady(conn);
                    }
                    ssliosession.outboundTransport();
                } catch (IOException ex) {
                    onException(conn, ex);
                    ssliosession.shutdown();
                }
            }
        } catch (RuntimeException ex) {
            session.shutdown();
            throw ex;
        }
    }

    public void timeout(IOSession session) {
        T conn = (T) session.getAttribute("http.connection");
        try {
            SSLIOSession ssliosession = (SSLIOSession) session.getAttribute("http.session.ssl");

            ensureNotNull(conn);
            onTimeout(conn);
            if (ssliosession != null) {
                synchronized (ssliosession) {
                    if (ssliosession.isOutboundDone() && !ssliosession.isInboundDone()) {
                        ssliosession.shutdown();
                    }
                }
            }
        } catch (RuntimeException ex) {
            session.shutdown();
            throw ex;
        }
    }
}

