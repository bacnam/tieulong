package org.apache.http.nio;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import org.apache.http.HttpException;

public interface NHttpMessageParser<T extends org.apache.http.HttpMessage> {
  void reset();

  int fillBuffer(ReadableByteChannel paramReadableByteChannel) throws IOException;

  T parse() throws IOException, HttpException;
}

