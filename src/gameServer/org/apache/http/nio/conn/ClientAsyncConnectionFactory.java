package org.apache.http.nio.conn;

import org.apache.http.nio.reactor.IOSession;
import org.apache.http.params.HttpParams;

@Deprecated
public interface ClientAsyncConnectionFactory {
  ClientAsyncConnection create(String paramString, IOSession paramIOSession, HttpParams paramHttpParams);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/ClientAsyncConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */