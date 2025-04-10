package org.apache.http.nio.conn;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.protocol.HttpContext;

public interface NHttpClientConnectionManager {
  Future<NHttpClientConnection> requestConnection(HttpRoute paramHttpRoute, Object paramObject, long paramLong1, long paramLong2, TimeUnit paramTimeUnit, FutureCallback<NHttpClientConnection> paramFutureCallback);

  void releaseConnection(NHttpClientConnection paramNHttpClientConnection, Object paramObject, long paramLong, TimeUnit paramTimeUnit);

  void startRoute(NHttpClientConnection paramNHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext) throws IOException;

  void upgrade(NHttpClientConnection paramNHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext) throws IOException;

  void routeComplete(NHttpClientConnection paramNHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext);

  boolean isRouteComplete(NHttpClientConnection paramNHttpClientConnection);

  void closeIdleConnections(long paramLong, TimeUnit paramTimeUnit);

  void closeExpiredConnections();

  void execute(IOEventDispatch paramIOEventDispatch) throws IOException;

  void shutdown() throws IOException;
}

