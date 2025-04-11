package org.apache.mina.util.byteaccess;

import org.apache.mina.core.buffer.IoBuffer;

import java.nio.ByteOrder;

public interface ByteArray extends IoAbsoluteReader, IoAbsoluteWriter {
    int first();

    int last();

    ByteOrder order();

    void order(ByteOrder paramByteOrder);

    void free();

    Iterable<IoBuffer> getIoBuffers();

    IoBuffer getSingleIoBuffer();

    boolean equals(Object paramObject);

    byte get(int paramInt);

    void get(int paramInt, IoBuffer paramIoBuffer);

    int getInt(int paramInt);

    Cursor cursor();

    Cursor cursor(int paramInt);

    public static interface Cursor extends IoRelativeReader, IoRelativeWriter {
        int getIndex();

        void setIndex(int param1Int);

        int getRemaining();

        boolean hasRemaining();

        byte get();

        void get(IoBuffer param1IoBuffer);

        int getInt();
    }
}

