package org.apache.http.impl.nio.conn;

import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.InetAddress;

@NotThreadSafe
class CPoolProxy
        implements ManagedNHttpClientConnection {
    private volatile CPoolEntry poolEntry;

    CPoolProxy(CPoolEntry entry) {
        this.poolEntry = entry;
    }

    public static NHttpClientConnection newProxy(CPoolEntry poolEntry) {
        return (NHttpClientConnection) new CPoolProxy(poolEntry);
    }

    private static CPoolProxy getProxy(NHttpClientConnection conn) {
        if (!CPoolProxy.class.isInstance(conn)) {
            throw new IllegalStateException("Unexpected connection proxy class: " + conn.getClass());
        }
        return CPoolProxy.class.cast(conn);
    }

    public static CPoolEntry getPoolEntry(NHttpClientConnection proxy) {
        CPoolEntry entry = getProxy(proxy).getPoolEntry();
        if (entry == null) {
            throw new ConnectionShutdownException();
        }
        return entry;
    }

    public static CPoolEntry detach(NHttpClientConnection proxy) {
        return getProxy(proxy).detach();
    }

    CPoolEntry getPoolEntry() {
        return this.poolEntry;
    }

    CPoolEntry detach() {
        CPoolEntry local = this.poolEntry;
        this.poolEntry = null;
        return local;
    }

    ManagedNHttpClientConnection getConnection() {
        CPoolEntry local = this.poolEntry;
        if (local == null) {
            return null;
        }
        return (ManagedNHttpClientConnection) local.getConnection();
    }

    ManagedNHttpClientConnection getValidConnection() {
        ManagedNHttpClientConnection conn = getConnection();
        if (conn == null) {
            throw new ConnectionShutdownException();
        }
        return conn;
    }

    public void close() throws IOException {
        CPoolEntry local = this.poolEntry;
        if (local != null) {
            local.closeConnection();
        }
    }

    public void shutdown() throws IOException {
        CPoolEntry local = this.poolEntry;
        if (local != null) {
            local.shutdownConnection();
        }
    }

    public HttpConnectionMetrics getMetrics() {
        return getValidConnection().getMetrics();
    }

    public void requestInput() {
        ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
        if (managedNHttpClientConnection != null) {
            managedNHttpClientConnection.requestInput();
        }
    }

    public void suspendInput() {
        ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
        if (managedNHttpClientConnection != null) {
            managedNHttpClientConnection.suspendInput();
        }
    }

    public void requestOutput() {
        ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
        if (managedNHttpClientConnection != null) {
            managedNHttpClientConnection.requestOutput();
        }
    }

    public void suspendOutput() {
        ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
        if (managedNHttpClientConnection != null) {
            managedNHttpClientConnection.suspendOutput();
        }
    }

    public InetAddress getLocalAddress() {
        return getValidConnection().getLocalAddress();
    }

    public int getLocalPort() {
        return getValidConnection().getLocalPort();
    }

    public InetAddress getRemoteAddress() {
        return getValidConnection().getRemoteAddress();
    }

    public int getRemotePort() {
        return getValidConnection().getRemotePort();
    }

    public boolean isOpen() {
        CPoolEntry local = this.poolEntry;
        if (local != null) {
            return !local.isClosed();
        }
        return false;
    }

    public boolean isStale() {
        ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
        if (managedNHttpClientConnection != null) {
            return (managedNHttpClientConnection.isStale() || !managedNHttpClientConnection.isOpen());
        }
        return true;
    }

    public int getSocketTimeout() {
        return getValidConnection().getSocketTimeout();
    }

    public void setSocketTimeout(int i) {
        getValidConnection().setSocketTimeout(i);
    }

    public void submitRequest(HttpRequest request) throws IOException, HttpException {
        getValidConnection().submitRequest(request);
    }

    public boolean isRequestSubmitted() {
        return getValidConnection().isRequestSubmitted();
    }

    public void resetOutput() {
        getValidConnection().resetOutput();
    }

    public void resetInput() {
        getValidConnection().resetInput();
    }

    public int getStatus() {
        return getValidConnection().getStatus();
    }

    public HttpRequest getHttpRequest() {
        return getValidConnection().getHttpRequest();
    }

    public HttpResponse getHttpResponse() {
        return getValidConnection().getHttpResponse();
    }

    public HttpContext getContext() {
        return getValidConnection().getContext();
    }

    public String getId() {
        return getValidConnection().getId();
    }

    public void bind(IOSession iosession) {
        getValidConnection().bind(iosession);
    }

    public IOSession getIOSession() {
        return getValidConnection().getIOSession();
    }

    public SSLSession getSSLSession() {
        return getValidConnection().getSSLSession();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CPoolProxy{");
        ManagedNHttpClientConnection conn = getConnection();
        if (conn != null) {
            sb.append(conn);
        } else {
            sb.append("detached");
        }
        sb.append('}');
        return sb.toString();
    }
}

