package org.apache.http.nio.conn.scheme;

import org.apache.http.nio.reactor.IOSession;

@Deprecated
public interface LayeringStrategy {
  boolean isSecure();
  
  IOSession layer(IOSession paramIOSession);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/scheme/LayeringStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */