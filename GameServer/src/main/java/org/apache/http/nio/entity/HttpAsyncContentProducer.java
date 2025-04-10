package org.apache.http.nio.entity;

import java.io.Closeable;
import java.io.IOException;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;

public interface HttpAsyncContentProducer extends Closeable {
  void produceContent(ContentEncoder paramContentEncoder, IOControl paramIOControl) throws IOException;

  boolean isRepeatable();
}

