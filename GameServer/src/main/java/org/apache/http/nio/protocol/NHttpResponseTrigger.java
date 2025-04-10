package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;

@Deprecated
public interface NHttpResponseTrigger {
  void submitResponse(HttpResponse paramHttpResponse);

  void handleException(HttpException paramHttpException);

  void handleException(IOException paramIOException);
}

