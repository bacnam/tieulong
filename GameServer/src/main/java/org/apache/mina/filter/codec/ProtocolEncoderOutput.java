package org.apache.mina.filter.codec;

import org.apache.mina.core.future.WriteFuture;

public interface ProtocolEncoderOutput {
    void write(Object paramObject);

    void mergeAll();

    WriteFuture flush();
}

