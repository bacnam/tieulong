package org.apache.mina.util.byteaccess;

import org.apache.mina.core.buffer.IoBuffer;

import java.nio.ByteOrder;

public interface IoRelativeWriter {
    int getRemaining();

    boolean hasRemaining();

    void skip(int paramInt);

    ByteOrder order();

    void put(byte paramByte);

    void put(IoBuffer paramIoBuffer);

    void putShort(short paramShort);

    void putInt(int paramInt);

    void putLong(long paramLong);

    void putFloat(float paramFloat);

    void putDouble(double paramDouble);

    void putChar(char paramChar);
}

