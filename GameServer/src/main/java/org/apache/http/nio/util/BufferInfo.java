package org.apache.http.nio.util;

@Deprecated
public interface BufferInfo {
    int length();

    int capacity();

    int available();
}

