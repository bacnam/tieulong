

package org.apache.thrift.protocol;

public final class TStruct {
  public TStruct() {
    this("");
  }

  public TStruct(String n) {
    name = n;
  }

  public final String name;
}
