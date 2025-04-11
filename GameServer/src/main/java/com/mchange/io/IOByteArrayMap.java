package com.mchange.io;

import java.io.IOException;

public interface IOByteArrayMap {
    byte[] get(byte[] paramArrayOfbyte) throws IOException;

    void put(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IOException;

    boolean putNoReplace(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IOException;

    boolean remove(byte[] paramArrayOfbyte) throws IOException;

    boolean containsKey(byte[] paramArrayOfbyte) throws IOException;

    IOByteArrayEnumeration keys() throws IOException;
}

