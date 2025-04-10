
package org.apache.thrift.transport;

import org.apache.commons.lang.NotImplementedException;

public final class AutoExpandingBufferWriteTransport extends TTransport {

  private final AutoExpandingBuffer buf;
  private int pos;

  public AutoExpandingBufferWriteTransport(int initialCapacity, double growthCoefficient) {
    this.buf = new AutoExpandingBuffer(initialCapacity, growthCoefficient);
    this.pos = 0;
  }

  @Override
  public void close() {}

  @Override
  public boolean isOpen() {return true;}

  @Override
  public void open() throws TTransportException {}

  @Override
  public int read(byte[] buf, int off, int len) throws TTransportException {
    throw new NotImplementedException();
  }

  @Override
  public void write(byte[] toWrite, int off, int len) throws TTransportException {
    buf.resizeIfNecessary(pos + len);
    System.arraycopy(toWrite, off, buf.array(), pos, len);
    pos += len;
  }

  public AutoExpandingBuffer getBuf() {
    return buf;
  }

  public int getPos() {
    return pos;
  }

  public void reset() {
    pos = 0;
  }
}
