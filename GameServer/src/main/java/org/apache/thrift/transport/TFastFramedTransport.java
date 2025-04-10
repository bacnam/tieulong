
package org.apache.thrift.transport;

public class TFastFramedTransport extends TTransport {

  public static class Factory extends TTransportFactory {
    private final int initialCapacity;
    private final int maxLength;

    public Factory() {
      this(DEFAULT_BUF_CAPACITY, DEFAULT_MAX_LENGTH);
    }

    public Factory(int initialCapacity) {
      this(initialCapacity, DEFAULT_MAX_LENGTH);
    }

    public Factory(int initialCapacity, int maxLength) {
      this.initialCapacity = initialCapacity;
      this.maxLength = maxLength;
    }

    @Override
    public TTransport getTransport(TTransport trans) {
      return new TFastFramedTransport(trans,
          initialCapacity,
          maxLength);
    }
  }

  public static final int DEFAULT_BUF_CAPACITY = 1024;

  public static final int DEFAULT_MAX_LENGTH = Integer.MAX_VALUE;

  private final TTransport underlying;
  private final AutoExpandingBufferWriteTransport writeBuffer;
  private final AutoExpandingBufferReadTransport readBuffer;
  private final byte[] i32buf = new byte[4];
  private final int maxLength;

  public TFastFramedTransport(TTransport underlying) {
    this(underlying, DEFAULT_BUF_CAPACITY, DEFAULT_MAX_LENGTH);
  }

  public TFastFramedTransport(TTransport underlying, int initialBufferCapacity) {
    this(underlying, initialBufferCapacity, DEFAULT_MAX_LENGTH);
  }

  public TFastFramedTransport(TTransport underlying, int initialBufferCapacity, int maxLength) {
    this.underlying = underlying;
    this.maxLength = maxLength;
    writeBuffer = new AutoExpandingBufferWriteTransport(initialBufferCapacity, 1.5);
    readBuffer = new AutoExpandingBufferReadTransport(initialBufferCapacity, 1.5);
  }

  @Override
  public void close() {
    underlying.close();
  }

  @Override
  public boolean isOpen() {
    return underlying.isOpen();
  }

  @Override
  public void open() throws TTransportException {
    underlying.open();
  }

  @Override
  public int read(byte[] buf, int off, int len) throws TTransportException {
    int got = readBuffer.read(buf, off, len);
    if (got > 0) {
      return got;
    }

    readFrame();

    return readBuffer.read(buf, off, len);
  }

  private void readFrame() throws TTransportException {
    underlying.readAll(i32buf , 0, 4);
    int size = TFramedTransport.decodeFrameSize(i32buf);

    if (size < 0) {
      throw new TTransportException("Read a negative frame size (" + size + ")!");
    }

    if (size > maxLength) {
      throw new TTransportException("Frame size (" + size + ") larger than max length (" + maxLength + ")!");
    }

    readBuffer.fill(underlying, size);
  }

  @Override
  public void write(byte[] buf, int off, int len) throws TTransportException {
    writeBuffer.write(buf, off, len);
  }

  @Override
  public void consumeBuffer(int len) {
    readBuffer.consumeBuffer(len);
  }

  @Override
  public void flush() throws TTransportException {
    int length = writeBuffer.getPos();
    TFramedTransport.encodeFrameSize(length, i32buf);
    underlying.write(i32buf, 0, 4);
    underlying.write(writeBuffer.getBuf().array(), 0, length);
    writeBuffer.reset();
    underlying.flush();
  }

  @Override
  public byte[] getBuffer() {
    return readBuffer.getBuffer();
  }

  @Override
  public int getBufferPosition() {
    return readBuffer.getBufferPosition();
  }

  @Override
  public int getBytesRemainingInBuffer() {
    return readBuffer.getBytesRemainingInBuffer();
  }
}
