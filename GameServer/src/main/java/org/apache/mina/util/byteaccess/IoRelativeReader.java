package org.apache.mina.util.byteaccess;

import org.apache.mina.core.buffer.IoBuffer;

import java.nio.ByteOrder;

public interface IoRelativeReader {
    int getRemaining();

    boolean hasRemaining();

    void skip(int paramInt);

    ByteArray slice(int paramInt);

    ByteOrder order();

    byte get();

    void get(IoBuffer paramIoBuffer);

    short getShort();

    int getInt();

    long getLong();

    float getFloat();

    double getDouble();

    char getChar();
}

