package org.apache.mina.core.file;

import java.nio.channels.FileChannel;

public interface FileRegion {
    FileChannel getFileChannel();

    long getPosition();

    void update(long paramLong);

    long getRemainingBytes();

    long getWrittenBytes();

    String getFilename();
}

