package org.apache.http.nio;

import org.apache.http.HttpException;

import java.io.IOException;

public interface NHttpMessageWriter<T extends org.apache.http.HttpMessage> {
    void reset();

    void write(T paramT) throws IOException, HttpException;
}

