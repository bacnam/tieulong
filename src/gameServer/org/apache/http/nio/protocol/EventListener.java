package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.nio.NHttpConnection;

@Deprecated
public interface EventListener {
  void fatalIOException(IOException paramIOException, NHttpConnection paramNHttpConnection);

  void fatalProtocolException(HttpException paramHttpException, NHttpConnection paramNHttpConnection);

  void connectionOpen(NHttpConnection paramNHttpConnection);

  void connectionClosed(NHttpConnection paramNHttpConnection);

  void connectionTimeout(NHttpConnection paramNHttpConnection);
}

