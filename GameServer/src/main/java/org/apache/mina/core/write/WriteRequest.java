package org.apache.mina.core.write;

import org.apache.mina.core.future.WriteFuture;

import java.net.SocketAddress;

public interface WriteRequest {
    WriteRequest getOriginalRequest();

    WriteFuture getFuture();

    Object getMessage();

    SocketAddress getDestination();

    boolean isEncoded();
}

