package org.apache.http.nio;

import org.apache.http.HttpException;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

public interface NHttpMessageParser<T extends org.apache.http.HttpMessage> {
    void reset();

    int fillBuffer(ReadableByteChannel paramReadableByteChannel) throws IOException;

    T parse() throws IOException, HttpException;
}

