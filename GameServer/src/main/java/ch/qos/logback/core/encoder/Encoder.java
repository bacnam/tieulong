package ch.qos.logback.core.encoder;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

import java.io.IOException;
import java.io.OutputStream;

public interface Encoder<E> extends ContextAware, LifeCycle {
    void init(OutputStream paramOutputStream) throws IOException;

    void doEncode(E paramE) throws IOException;

    void close() throws IOException;
}

