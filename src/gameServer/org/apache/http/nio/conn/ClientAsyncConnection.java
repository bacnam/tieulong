package org.apache.http.nio.conn;

import org.apache.http.HttpInetConnection;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.reactor.IOSession;

@Deprecated
public interface ClientAsyncConnection extends NHttpClientConnection, HttpInetConnection {
  void upgrade(IOSession paramIOSession);
  
  IOSession getIOSession();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/ClientAsyncConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */