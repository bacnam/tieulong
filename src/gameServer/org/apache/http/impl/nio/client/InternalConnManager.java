package org.apache.http.impl.nio.client;

import org.apache.http.nio.NHttpClientConnection;

interface InternalConnManager {
  void releaseConnection();
  
  void abortConnection();
  
  NHttpClientConnection getConnection();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/InternalConnManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */