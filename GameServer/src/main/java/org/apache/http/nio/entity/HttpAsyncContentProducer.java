package org.apache.http.nio.entity;

import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;

import java.io.Closeable;
import java.io.IOException;

public interface HttpAsyncContentProducer extends Closeable {
    void produceContent(ContentEncoder paramContentEncoder, IOControl paramIOControl) throws IOException;

    boolean isRepeatable();
}

