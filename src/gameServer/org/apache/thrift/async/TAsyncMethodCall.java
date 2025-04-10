
package org.apache.thrift.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TTransportException;

public abstract class TAsyncMethodCall<T extends TAsyncMethodCall> {

  private static final int INITIAL_MEMORY_BUFFER_SIZE = 128;

  public static enum State {
    CONNECTING,
    WRITING_REQUEST_SIZE,
    WRITING_REQUEST_BODY,
    READING_RESPONSE_SIZE,
    READING_RESPONSE_BODY,
    RESPONSE_READ,
    ERROR;
  }

  private State state = null;

  protected final TNonblockingTransport transport;
  private final TProtocolFactory protocolFactory;
  protected final TAsyncClient client;
  private final AsyncMethodCallback<T> callback;
  private final boolean isOneway;

  private long lastTransitionTime;

  private ByteBuffer sizeBuffer;
  private final byte[] sizeBufferArray = new byte[4];
  private ByteBuffer frameBuffer;

  protected TAsyncMethodCall(TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport, AsyncMethodCallback<T> callback, boolean isOneway) {
    this.transport = transport;
    this.callback = callback;
    this.protocolFactory = protocolFactory;
    this.client = client;
    this.isOneway = isOneway;
    this.lastTransitionTime = System.currentTimeMillis();
  }

  protected State getState() {
    return state;
  }

  protected boolean isFinished() {
    return state == State.RESPONSE_READ;
  }

  protected long getLastTransitionTime() {
    return lastTransitionTime;
  }

  public TAsyncClient getClient() {
    return client;
  }

  protected abstract void write_args(TProtocol protocol) throws TException;

  protected void prepareMethodCall() throws TException {
    TMemoryBuffer memoryBuffer = new TMemoryBuffer(INITIAL_MEMORY_BUFFER_SIZE);
    TProtocol protocol = protocolFactory.getProtocol(memoryBuffer);
    write_args(protocol);

    int length = memoryBuffer.length();
    frameBuffer = ByteBuffer.wrap(memoryBuffer.getArray(), 0, length);

    TFramedTransport.encodeFrameSize(length, sizeBufferArray);
    sizeBuffer = ByteBuffer.wrap(sizeBufferArray);
  }

  void start(Selector sel) throws IOException {
    SelectionKey key;
    if (transport.isOpen()) {
      state = State.WRITING_REQUEST_SIZE;
      key = transport.registerSelector(sel, SelectionKey.OP_WRITE);
    } else {
      state = State.CONNECTING;
      key = transport.registerSelector(sel, SelectionKey.OP_CONNECT);

      if (transport.startConnect()) {
        registerForFirstWrite(key);
      }
    }

    key.attach(this);
  }

  void registerForFirstWrite(SelectionKey key) throws IOException {
    state = State.WRITING_REQUEST_SIZE;
    key.interestOps(SelectionKey.OP_WRITE);
  }

  protected ByteBuffer getFrameBuffer() {
    return frameBuffer;
  }

  protected void transition(SelectionKey key) {

    if (!key.isValid()) {
      key.cancel();
      Exception e = new TTransportException("Selection key not valid!");
      onError(e);
      return;
    }

    try {
      switch (state) {
        case CONNECTING:
          doConnecting(key);
          break;
        case WRITING_REQUEST_SIZE:
          doWritingRequestSize();
          break;
        case WRITING_REQUEST_BODY:
          doWritingRequestBody(key);
          break;
        case READING_RESPONSE_SIZE:
          doReadingResponseSize();
          break;
        case READING_RESPONSE_BODY:
          doReadingResponseBody(key);
          break;
        default: 
          throw new IllegalStateException("Method call in state " + state
              + " but selector called transition method. Seems like a bug...");
      }
      lastTransitionTime = System.currentTimeMillis();
    } catch (Throwable e) {
      key.cancel();
      key.attach(null);
      onError(e);
    }
  }

  protected void onError(Throwable e) {
    client.onError(e);
    callback.onError(e);
    state = State.ERROR;
  }

  private void doReadingResponseBody(SelectionKey key) throws IOException {
    if (transport.read(frameBuffer) < 0) {
      throw new IOException("Read call frame failed");
    }
    if (frameBuffer.remaining() == 0) {
      cleanUpAndFireCallback(key);
    }
  }

  private void cleanUpAndFireCallback(SelectionKey key) {
    state = State.RESPONSE_READ;
    key.interestOps(0);

    key.attach(null);
    client.onComplete();
    callback.onComplete((T)this);
  }

  private void doReadingResponseSize() throws IOException {
    if (transport.read(sizeBuffer) < 0) {
      throw new IOException("Read call frame size failed");
    }
    if (sizeBuffer.remaining() == 0) {
      state = State.READING_RESPONSE_BODY;
      frameBuffer = ByteBuffer.allocate(TFramedTransport.decodeFrameSize(sizeBufferArray));
    }
  }

  private void doWritingRequestBody(SelectionKey key) throws IOException {
    if (transport.write(frameBuffer) < 0) {
      throw new IOException("Write call frame failed");
    }
    if (frameBuffer.remaining() == 0) {
      if (isOneway) {
        cleanUpAndFireCallback(key);
      } else {
        state = State.READING_RESPONSE_SIZE;
        sizeBuffer.rewind();  
        key.interestOps(SelectionKey.OP_READ);
      }
    }
  }

  private void doWritingRequestSize() throws IOException {
    if (transport.write(sizeBuffer) < 0) {
      throw new IOException("Write call frame size failed");
    }
    if (sizeBuffer.remaining() == 0) {
      state = State.WRITING_REQUEST_BODY;
    }
  }

  private void doConnecting(SelectionKey key) throws IOException {
    if (!key.isConnectable() || !transport.finishConnect()) {
      throw new IOException("not connectable or finishConnect returned false after we got an OP_CONNECT");
    }
    registerForFirstWrite(key);
  }
}
