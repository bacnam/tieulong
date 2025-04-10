package org.apache.http.nio;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;

public interface NHttpServerConnection extends NHttpConnection {
  void submitResponse(HttpResponse paramHttpResponse) throws IOException, HttpException;

  boolean isResponseSubmitted();

  void resetInput();

  void resetOutput();
}

