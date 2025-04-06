package org.apache.http.nio.conn;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.nio.reactor.IOSession;

public interface NHttpConnectionFactory<T extends org.apache.http.nio.NHttpConnection> {
  T create(IOSession paramIOSession, ConnectionConfig paramConnectionConfig);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/NHttpConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */