package org.apache.http.nio;

import org.apache.http.nio.reactor.SessionOutputBuffer;

public interface NHttpMessageWriterFactory<T extends org.apache.http.HttpMessage> {
    NHttpMessageWriter<T> create(SessionOutputBuffer paramSessionOutputBuffer);
}

