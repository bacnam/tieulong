package ch.qos.logback.core.net;

import java.io.IOException;

public interface ObjectWriter {
    void write(Object paramObject) throws IOException;
}

