package org.apache.http.io;

import org.apache.http.HttpException;

import java.io.IOException;

public interface HttpMessageParser<T extends org.apache.http.HttpMessage> {
    T parse() throws IOException, HttpException;
}

