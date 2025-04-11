package org.apache.http.nio;

@Deprecated
public interface NHttpServerIOTarget extends NHttpServerConnection {
    void consumeInput(NHttpServiceHandler paramNHttpServiceHandler);

    void produceOutput(NHttpServiceHandler paramNHttpServiceHandler);
}

