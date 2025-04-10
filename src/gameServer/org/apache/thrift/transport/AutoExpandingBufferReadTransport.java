
package org.apache.thrift.transport;

import org.apache.commons.lang.NotImplementedException;

public class AutoExpandingBufferReadTransport extends TTransport {

  private final AutoExpandingBuffer buf;

  private int pos = 0;
  private int limit = 0;

  public AutoExpandingBufferReadTransport(int initialCapacity, double overgrowthCoefficient) {
    this.buf = new AutoExpandingBuffer(initialCapacity, overgrowthCoefficient);
  }

  public void fill(TTransport inTrans, int length) throws TTransportException {
    buf.resizeIfNecessary(length);
    inTrans.readAll(buf.array(), 0, length);
    pos = 0;
    limit = length;
  }

  @Override
  public void close() {}

  @Override
  public boolean isOpen() { return true; }

  @Override
  public void open() throws TTransportException {}

  @Override
  public final int read(byte[] target, int off, int len) throws TTransportException {
    int amtToRead = Math.min(len, getBytesRemainingInBuffer());
    System.arraycopy(buf.array(), pos, target, off, amtToRead);
    consumeBuffer(amtToRead);
    return amtToRead;
  }

  @Override
  public void write(byte[] buf, int off, int len) throws TTransportException {
    throw new NotImplementedException();
  }

  @Override
  public final void consumeBuffer(int len) {
    pos += len;
  }

  @Override
  public final byte[] getBuffer() {
    return buf.array();
  }

  @Override
  public final int getBufferPosition() {
    return pos;
  }

  @Override
  public final int getBytesRemainingInBuffer() {
    return limit - pos;
  }
}
  