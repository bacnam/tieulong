

package org.apache.thrift.protocol;

public final class TMap {
  public TMap() {
    this(TType.STOP, TType.STOP, 0);
  }

  public TMap(byte k, byte v, int s) {
    keyType = k;
    valueType = v;
    size = s;
  }

  public final byte  keyType;
  public final byte  valueType;
  public final int   size;
}
