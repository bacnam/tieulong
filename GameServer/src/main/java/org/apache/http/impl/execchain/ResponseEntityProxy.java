package org.apache.http.impl.execchain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

@NotThreadSafe
class ResponseEntityProxy
        extends HttpEntityWrapper
        implements EofSensorWatcher {
    private final ConnectionHolder connHolder;

    ResponseEntityProxy(HttpEntity entity, ConnectionHolder connHolder) {
        super(entity);
        this.connHolder = connHolder;
    }

    public static void enchance(HttpResponse response, ConnectionHolder connHolder) {
        HttpEntity entity = response.getEntity();
        if (entity != null && entity.isStreaming() && connHolder != null) {
            response.setEntity((HttpEntity) new ResponseEntityProxy(entity, connHolder));
        }
    }

    private void cleanup() {
        if (this.connHolder != null) {
            this.connHolder.abortConnection();
        }
    }

    public void releaseConnection() throws IOException {
        if (this.connHolder != null) {
            try {
                if (this.connHolder.isReusable()) {
                    this.connHolder.releaseConnection();
                }
            } finally {
                cleanup();
            }
        }
    }

    public boolean isRepeatable() {
        return false;
    }

    public InputStream getContent() throws IOException {
        return (InputStream) new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }

    @Deprecated
    public void consumeContent() throws IOException {
        releaseConnection();
    }

    public void writeTo(OutputStream outstream) throws IOException {
        try {
            this.wrappedEntity.writeTo(outstream);
            releaseConnection();
        } finally {
            cleanup();
        }
    }

    public boolean eofDetected(InputStream wrapped) throws IOException {
        try {
            wrapped.close();
            releaseConnection();
        } finally {
            cleanup();
        }
        return false;
    }

    public boolean streamClosed(InputStream wrapped) throws IOException {
        try {
            boolean open = (this.connHolder != null && !this.connHolder.isReleased());

            try {
                wrapped.close();
                releaseConnection();
            } catch (SocketException ex) {
                if (open) {
                    throw ex;
                }
            }
        } finally {
            cleanup();
        }
        return false;
    }

    public boolean streamAbort(InputStream wrapped) throws IOException {
        cleanup();
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ResponseEntityProxy{");
        sb.append(this.wrappedEntity);
        sb.append('}');
        return sb.toString();
    }
}

