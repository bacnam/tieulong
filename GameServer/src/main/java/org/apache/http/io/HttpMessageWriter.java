package org.apache.http.io;

import org.apache.http.HttpException;

import java.io.IOException;

public interface HttpMessageWriter<T extends org.apache.http.HttpMessage> {
    void write(T paramT) throws IOException, HttpException;
}

