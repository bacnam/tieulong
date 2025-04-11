package org.apache.http.nio.entity;

import org.apache.http.HttpEntity;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;

import java.io.IOException;

@Deprecated
public interface ProducingNHttpEntity extends HttpEntity {
    void produceContent(ContentEncoder paramContentEncoder, IOControl paramIOControl) throws IOException;

    void finish() throws IOException;
}

