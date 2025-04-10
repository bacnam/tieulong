package org.apache.http.nio;

import java.io.IOException;
import org.apache.http.HttpException;

public interface NHttpMessageWriter<T extends org.apache.http.HttpMessage> {
  void reset();

  void write(T paramT) throws IOException, HttpException;
}

