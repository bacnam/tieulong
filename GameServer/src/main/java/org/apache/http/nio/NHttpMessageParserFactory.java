package org.apache.http.nio;

import org.apache.http.config.MessageConstraints;
import org.apache.http.nio.reactor.SessionInputBuffer;

public interface NHttpMessageParserFactory<T extends org.apache.http.HttpMessage> {
  NHttpMessageParser<T> create(SessionInputBuffer paramSessionInputBuffer, MessageConstraints paramMessageConstraints);
}

