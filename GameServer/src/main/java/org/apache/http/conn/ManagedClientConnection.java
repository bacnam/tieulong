package org.apache.http.conn;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Deprecated
public interface ManagedClientConnection extends HttpClientConnection, HttpRoutedConnection, ManagedHttpClientConnection, ConnectionReleaseTrigger {
    boolean isSecure();

    HttpRoute getRoute();

    SSLSession getSSLSession();

    void open(HttpRoute paramHttpRoute, HttpContext paramHttpContext, HttpParams paramHttpParams) throws IOException;

    void tunnelTarget(boolean paramBoolean, HttpParams paramHttpParams) throws IOException;

    void tunnelProxy(HttpHost paramHttpHost, boolean paramBoolean, HttpParams paramHttpParams) throws IOException;

    void layerProtocol(HttpContext paramHttpContext, HttpParams paramHttpParams) throws IOException;

    void markReusable();

    void unmarkReusable();

    boolean isMarkedReusable();

    Object getState();

    void setState(Object paramObject);

    void setIdleDuration(long paramLong, TimeUnit paramTimeUnit);
}

