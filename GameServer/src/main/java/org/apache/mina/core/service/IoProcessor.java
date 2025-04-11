package org.apache.mina.core.service;

import org.apache.mina.core.write.WriteRequest;

public interface IoProcessor<S extends org.apache.mina.core.session.IoSession> {
    boolean isDisposing();

    boolean isDisposed();

    void dispose();

    void add(S paramS);

    void flush(S paramS);

    void write(S paramS, WriteRequest paramWriteRequest);

    void updateTrafficControl(S paramS);

    void remove(S paramS);
}

