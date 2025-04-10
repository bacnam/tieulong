

package org.apache.thrift.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TNonblockingSocket extends TNonblockingTransport {

  private static final Logger LOGGER = LoggerFactory.getLogger(TNonblockingSocket.class.getName());

  private final SocketAddress socketAddress_;

  private final SocketChannel socketChannel_;

  public TNonblockingSocket(String host, int port) throws IOException {
    this(host, port, 0);
  }

  public TNonblockingSocket(String host, int port, int timeout) throws IOException {
    this(SocketChannel.open(), timeout, new InetSocketAddress(host, port));
  }

  public TNonblockingSocket(SocketChannel socketChannel) throws IOException {
    this(socketChannel, 0, null);
    if (!socketChannel.isConnected()) throw new IOException("Socket must already be connected");
  }

  private TNonblockingSocket(SocketChannel socketChannel, int timeout, SocketAddress socketAddress)
      throws IOException {
    socketChannel_ = socketChannel;
    socketAddress_ = socketAddress;

    socketChannel.configureBlocking(false);

    Socket socket = socketChannel.socket();
    socket.setSoLinger(false, 0);
    socket.setTcpNoDelay(true);
    setTimeout(timeout);
  }

  public SelectionKey registerSelector(Selector selector, int interests) throws IOException {
    return socketChannel_.register(selector, interests);
  }

  public void setTimeout(int timeout) {
    try {
      socketChannel_.socket().setSoTimeout(timeout);
    } catch (SocketException sx) {
      LOGGER.warn("Could not set socket timeout.", sx);
    }
  }

  public SocketChannel getSocketChannel() {
    return socketChannel_;
  }

  public boolean isOpen() {

    return socketChannel_.isOpen() && socketChannel_.isConnected();
  }

  public void open() throws TTransportException {
    throw new RuntimeException("open() is not implemented for TNonblockingSocket");
  }

  public int read(ByteBuffer buffer) throws IOException {
    return socketChannel_.read(buffer);
  }

  public int read(byte[] buf, int off, int len) throws TTransportException {
    if ((socketChannel_.validOps() & SelectionKey.OP_READ) != SelectionKey.OP_READ) {
      throw new TTransportException(TTransportException.NOT_OPEN,
        "Cannot read from write-only socket channel");
    }
    try {
      return socketChannel_.read(ByteBuffer.wrap(buf, off, len));
    } catch (IOException iox) {
      throw new TTransportException(TTransportException.UNKNOWN, iox);
    }
  }

  public int write(ByteBuffer buffer) throws IOException {
    return socketChannel_.write(buffer);
  }

  public void write(byte[] buf, int off, int len) throws TTransportException {
    if ((socketChannel_.validOps() & SelectionKey.OP_WRITE) != SelectionKey.OP_WRITE) {
      throw new TTransportException(TTransportException.NOT_OPEN,
        "Cannot write to write-only socket channel");
    }
    try {
      socketChannel_.write(ByteBuffer.wrap(buf, off, len));
    } catch (IOException iox) {
      throw new TTransportException(TTransportException.UNKNOWN, iox);
    }
  }

  public void flush() throws TTransportException {

  }

  public void close() {
    try {
      socketChannel_.close();
    } catch (IOException iox) {
      LOGGER.warn("Could not close socket.", iox);
    }
  }

  public boolean startConnect() throws IOException {
    return socketChannel_.connect(socketAddress_);
  }

  public boolean finishConnect() throws IOException {
    return socketChannel_.finishConnect();
  }

}
