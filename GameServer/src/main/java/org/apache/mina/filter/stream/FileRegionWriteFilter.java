package org.apache.mina.filter.stream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.file.FileRegion;

import java.io.IOException;

public class FileRegionWriteFilter
        extends AbstractStreamWriteFilter<FileRegion> {
    protected Class<FileRegion> getMessageClass() {
        return FileRegion.class;
    }

    protected IoBuffer getNextBuffer(FileRegion fileRegion) throws IOException {
        if (fileRegion.getRemainingBytes() <= 0L) {
            return null;
        }

        int bufferSize = (int) Math.min(getWriteBufferSize(), fileRegion.getRemainingBytes());
        IoBuffer buffer = IoBuffer.allocate(bufferSize);

        int bytesRead = fileRegion.getFileChannel().read(buffer.buf(), fileRegion.getPosition());
        fileRegion.update(bytesRead);

        buffer.flip();
        return buffer;
    }
}

