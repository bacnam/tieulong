

package org.apache.thrift.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TServerSocket extends TServerTransport {

  private static final Logger LOGGER = LoggerFactory.getLogger(TServerSocket.class.getName());

  private ServerSocket serverSocket_ = null;

  private int port_ = 0;

  private int clientTimeout_ = 0;

  public TServerSocket(ServerSocket serverSocket) {
    this(serverSocket, 0);
  }

  public TServerSocket(ServerSocket serverSocket, int clientTimeout) {
    serverSocket_ = serverSocket;
    clientTimeout_ = clientTimeout;
  }

  public TServerSocket(int port) throws TTransportException {
    this(port, 0);
  }

  public TServerSocket(int port, int clientTimeout) throws TTransportException {
    this(new InetSocketAddress(port), clientTimeout);
    port_ = port;
  }

  public TServerSocket(InetSocketAddress bindAddr) throws TTransportException {
    this(bindAddr, 0);
  }

  public TServerSocket(InetSocketAddress bindAddr, int clientTimeout) throws TTransportException {
    clientTimeout_ = clientTimeout;
    try {

      serverSocket_ = new ServerSocket();

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
        LOGGER.error("Could not set socket timeout.", sx);
      }
    }
  }

  protected TSocket acceptImpl() throws TTransportException {
    if (serverSocket_ == null) {
      throw new TTransportException(TTransportException.NOT_OPEN, "No underlying server socket.");
    }
    try {
      Socket result = serverSocket_.accept();
      TSocket result2 = new TSocket(result);
      result2.setTimeout(clientTimeout_);
      return result2;
    } catch (IOException iox) {
      throw new TTransportException(iox);
    }
  }

  public void close() {
    if (serverSocket_ != null) {
      try {
        serverSocket_.close();
      } catch (IOException iox) {
        LOGGER.warn("Could not close server socket.", iox);
      }
      serverSocket_ = null;
    }
  }

  public void interrupt() {

    close();
  }

}
