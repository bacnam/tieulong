package org.apache.http.nio.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface ManagedClientAsyncConnection extends HttpRoutedConnection, NHttpClientConnection, ConnectionReleaseTrigger {
  Object getState();

  void setState(Object paramObject);

  void markReusable();

  void unmarkReusable();

  boolean isMarkedReusable();

  void open(HttpRoute paramHttpRoute, HttpContext paramHttpContext, HttpParams paramHttpParams) throws IOException;

  void tunnelTarget(HttpParams paramHttpParams) throws IOException;

  void tunnelProxy(HttpHost paramHttpHost, HttpParams paramHttpParams) throws IOException;

  void layerProtocol(HttpContext paramHttpContext, HttpParams paramHttpParams) throws IOException;

  void setIdleDuration(long paramLong, TimeUnit paramTimeUnit);
}

