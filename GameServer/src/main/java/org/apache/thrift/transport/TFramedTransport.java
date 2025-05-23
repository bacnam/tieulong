

package org.apache.thrift.transport;

import org.apache.thrift.TByteArrayOutputStream;

public class TFramedTransport extends TTransport {

  protected static final int DEFAULT_MAX_LENGTH = 0x7FFFFFFF;

  private int maxLength_;

  private TTransport transport_ = null;

  private final TByteArrayOutputStream writeBuffer_ =
    new TByteArrayOutputStream(1024);

  private TMemoryInputTransport readBuffer_ = new TMemoryInputTransport(new byte[0]);

  public static class Factory extends TTransportFactory {
    private int maxLength_;

    public Factory() {
      maxLength_ = TFramedTransport.DEFAULT_MAX_LENGTH;
    }

    public Factory(int maxLength) {
      maxLength_ = maxLength;
    }

    @Override
    public TTransport getTransport(TTransport base) {
      return new TFramedTransport(base, maxLength_);
    }
  }

  public TFramedTransport(TTransport transport, int maxLength) {
    transport_ = transport;
    maxLength_ = maxLength;
  }

  public TFramedTransport(TTransport transport) {
    transport_ = transport;
    maxLength_ = TFramedTransport.DEFAULT_MAX_LENGTH;
  }

  public void open() throws TTransportException {
    transport_.open();
  }

  public boolean isOpen() {
    return transport_.isOpen();
  }

  public void close() {
    transport_.close();
  }

  public int read(byte[] buf, int off, int len) throws TTransportException {
    if (readBuffer_ != null) {
      int got = readBuffer_.read(buf, off, len);
      if (got > 0) {
        return got;
      }
    }

    readFrame();

    return readBuffer_.read(buf, off, len);
  }

  @Override
  public byte[] getBuffer() {
    return readBuffer_.getBuffer();
  }

  @Override
  public int getBufferPosition() {
    return readBuffer_.getBufferPosition();
  }

  @Override
  public int getBytesRemainingInBuffer() {
    return readBuffer_.getBytesRemainingInBuffer();
  }

  @Override
  public void consumeBuffer(int len) {
    readBuffer_.consumeBuffer(len);
  }

  private final byte[] i32buf = new byte[4];

  private void readFrame() throws TTransportException {
    transport_.readAll(i32buf, 0, 4);
    int size = decodeFrameSize(i32buf);

    if (size < 0) {
      throw new TTransportException("Read a negative frame size (" + size + ")!");
    }

    if (size > maxLength_) {
      throw new TTransportException("Frame size (" + size + ") larger than max length (" + maxLength_ + ")!");
    }

    byte[] buff = new byte[size];
    transport_.readAll(buff, 0, size);
    readBuffer_.reset(buff);
  }

  public void write(byte[] buf, int off, int len) throws TTransportException {
    writeBuffer_.write(buf, off, len);
  }

  @Override
  public void flush() throws TTransportException {
    byte[] buf = writeBuffer_.get();
    int len = writeBuffer_.len();
    writeBuffer_.reset();

    encodeFrameSize(len, i32buf);
    transport_.write(i32buf, 0, 4);
    transport_.write(buf, 0, len);
    transport_.flush();
  }

  public static final void encodeFrameSize(final int frameSize, final byte[] buf) {
    buf[0] = (byte)(0xff & (frameSize >> 24));
    buf[1] = (byte)(0xff & (frameSize >> 16));
    buf[2] = (byte)(0xff & (frameSize >> 8));
    buf[3] = (byte)(0xff & (frameSize));
  }

  public static final int decodeFrameSize(final byte[] buf) {
    return 
      ((buf[0] & 0xff) << 24) |
      ((buf[1] & 0xff) << 16) |
      ((buf[2] & 0xff) <<  8) |
      ((buf[3] & 0xff));
  }
}
