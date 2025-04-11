package org.apache.http.nio.protocol;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.protocol.HttpContext;

import java.io.Closeable;
import java.io.IOException;

public interface HttpAsyncRequestProducer extends Closeable {
    HttpHost getTarget();

    HttpRequest generateRequest() throws IOException, HttpException;

    void produceContent(ContentEncoder paramContentEncoder, IOControl paramIOControl) throws IOException;

    void requestCompleted(HttpContext paramHttpContext);

    void failed(Exception paramException);

    boolean isRepeatable();

    void resetRequest() throws IOException;
}

