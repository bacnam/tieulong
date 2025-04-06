package org.apache.http.nio.conn;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.nio.reactor.IOSession;

public interface SchemeIOSessionStrategy {
  boolean isLayeringRequired();
  
  IOSession upgrade(HttpHost paramHttpHost, IOSession paramIOSession) throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/SchemeIOSessionStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */