package org.apache.http.nio.conn;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
import org.apache.http.nio.reactor.IOReactor;

@Deprecated
public interface ClientAsyncConnectionManager extends IOReactor {
  AsyncSchemeRegistry getSchemeRegistry();

  Future<ManagedClientAsyncConnection> leaseConnection(HttpRoute paramHttpRoute, Object paramObject, long paramLong, TimeUnit paramTimeUnit, FutureCallback<ManagedClientAsyncConnection> paramFutureCallback);

  void releaseConnection(ManagedClientAsyncConnection paramManagedClientAsyncConnection, long paramLong, TimeUnit paramTimeUnit);
}

