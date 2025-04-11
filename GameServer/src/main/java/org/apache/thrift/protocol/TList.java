package org.apache.thrift.protocol;

public final class TList {
    public final byte elemType;
    public final int size;

    public TList() {
        this(TType.STOP, 0);
    }
    public TList(byte t, int s) {
        elemType = t;
        size = s;
    }
}
