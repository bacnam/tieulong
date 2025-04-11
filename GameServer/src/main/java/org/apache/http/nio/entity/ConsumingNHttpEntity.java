package org.apache.http.nio.entity;

import org.apache.http.HttpEntity;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;

import java.io.IOException;

@Deprecated
public interface ConsumingNHttpEntity extends HttpEntity {
    void consumeContent(ContentDecoder paramContentDecoder, IOControl paramIOControl) throws IOException;

    void finish() throws IOException;
}

