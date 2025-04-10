
package org.apache.thrift.transport;

public class AutoExpandingBuffer {
  private byte[] array;

  private final double growthCoefficient;

  public AutoExpandingBuffer(int initialCapacity, double growthCoefficient) {
    if (growthCoefficient < 1.0) {
      throw new IllegalArgumentException("Growth coefficient must be >= 1.0");
    }
    array = new byte[initialCapacity];
    this.growthCoefficient = growthCoefficient;
  }

  public void resizeIfNecessary(int size) {
    if (array.length < size) {
      byte[] newBuf = new byte[(int)(size * growthCoefficient)];
      System.arraycopy(array, 0, newBuf, 0, array.length);
      array = newBuf;
    }
  }

  public byte[] array() {
    return array;
  }
}
