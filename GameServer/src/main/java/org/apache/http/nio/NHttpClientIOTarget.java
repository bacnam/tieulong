package org.apache.http.nio;

@Deprecated
public interface NHttpClientIOTarget extends NHttpClientConnection {
    void consumeInput(NHttpClientHandler paramNHttpClientHandler);

    void produceOutput(NHttpClientHandler paramNHttpClientHandler);
}

