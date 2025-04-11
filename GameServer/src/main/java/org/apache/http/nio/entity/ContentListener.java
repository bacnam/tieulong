package org.apache.http.nio.entity;

import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;

import java.io.IOException;

@Deprecated
public interface ContentListener {
    void contentAvailable(ContentDecoder paramContentDecoder, IOControl paramIOControl) throws IOException;

    void finished();
}

