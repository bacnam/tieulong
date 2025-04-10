package org.apache.http.nio.entity;

import java.io.IOException;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;

@Deprecated
public interface ContentListener {
  void contentAvailable(ContentDecoder paramContentDecoder, IOControl paramIOControl) throws IOException;

  void finished();
}

