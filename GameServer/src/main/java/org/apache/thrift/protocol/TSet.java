

package org.apache.thrift.protocol;

public final class TSet {
  public TSet() {
    this(TType.STOP, 0);
  }

  public TSet(byte t, int s) {
    elemType = t;
    size = s;
  }

  public TSet(TList list) {
    this(list.elemType, list.size);
  }

  public final byte elemType;
  public final int  size;
}
