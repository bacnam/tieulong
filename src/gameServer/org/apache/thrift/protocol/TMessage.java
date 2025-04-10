

package org.apache.thrift.protocol;

public final class TMessage {
  public TMessage() {
    this("", TType.STOP, 0);
  }

  public TMessage(String n, byte t, int s) {
    name = n;
    type = t;
    seqid = s;
  }

  public final String name;
  public final byte type;
  public final int seqid;

  @Override
  public String toString() {
    return "<TMessage name:'" + name + "' type: " + type + " seqid:" + seqid + ">";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof TMessage) {
      return equals((TMessage) other);
    }
    return false;
  }

  public boolean equals(TMessage other) {
    return name.equals(other.name) && type == other.type && seqid == other.seqid;
  }
}
