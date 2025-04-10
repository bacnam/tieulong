

package org.apache.thrift.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TNonblockingServerSocket extends TNonblockingServerTransport {

  private ServerSocketChannel serverSocketChannel = null;

  private ServerSocket serverSocket_ = null;

  private int port_ = 0;

  private int clientTimeout_ = 0;

  public TNonblockingServerSocket(int port) throws TTransportException {
    this(port, 0);
  }

  public TNonblockingServerSocket(int port, int clientTimeout) throws TTransportException {
    this(new InetSocketAddress(port), clientTimeout);
    port_ = port;
  }  

  public TNonblockingServerSocket(InetSocketAddress bindAddr) throws TTransportException {
    this(bindAddr, 0);
  }

  public TNonblockingServerSocket(InetSocketAddress bindAddr, int clientTimeout) throws TTransportException {
    clientTimeout_ = clientTimeout;
    try {
      serverSocketChannel = ServerSocketChannel.open();
      serverSocketChannel.configureBlocking(false);

      serverSocket_ = serverSocketChannel.socket();

      serverSocket_.setReuseAddress(true);

      serverSocket_.bind(bindAddr);
    } catch (IOException ioe) {
      serverSocket_ = null;
      throw new TTransportException("Could not create ServerSocket on address " + bindAddr.toString() + ".");
    }
  }

  public void listen() throws TTransportException {

    if (serverSocket_ != null) {
      try {
        serverSocket_.setSoTimeout(0);
      } catch (SocketException sx) {
        sx.printStackTrace();
      }
    }
  }

  protected TNonblockingSocket acceptImpl() throws TTransportException {
    if (serverSocket_ == null) {
      throw new TTransportException(TTransportException.NOT_OPEN, "No underlying server socket.");
    }
    try {
      SocketChannel socketChannel = serverSocketChannel.accept();
      if (socketChannel == null) {
        return null;
      }

      TNonblockingSocket tsocket = new TNonblockingSocket(socketChannel);
      tsocket.setTimeout(clientTimeout_);
      return tsocket;
    } catch (IOException iox) {
      throw new TTransportException(iox);
    }
  }

  public void registerSelector(Selector selector) {
    try {

      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    } catch (ClosedChannelException e) {

    }
  }

  public void close() {
    if (serverSocket_ != null) {
      try {
        serverSocket_.close();
      } catch (IOException iox) {
        System.err.println("WARNING: Could not close server socket: " +
                           iox.getMessage());
      }
      serverSocket_ = null;
    }
  }

  public void interrupt() {

    close();
  }

}
