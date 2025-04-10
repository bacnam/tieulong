package org.apache.http.nio;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;

public interface NHttpClientConnection extends NHttpConnection {
  void submitRequest(HttpRequest paramHttpRequest) throws IOException, HttpException;

  boolean isRequestSubmitted();

  void resetOutput();

  void resetInput();
}

