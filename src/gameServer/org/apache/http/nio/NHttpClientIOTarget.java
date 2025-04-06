package org.apache.http.nio;

@Deprecated
public interface NHttpClientIOTarget extends NHttpClientConnection {
  void consumeInput(NHttpClientHandler paramNHttpClientHandler);
  
  void produceOutput(NHttpClientHandler paramNHttpClientHandler);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/NHttpClientIOTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */