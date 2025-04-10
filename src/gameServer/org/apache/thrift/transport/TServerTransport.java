

package org.apache.thrift.transport;

public abstract class TServerTransport {

  public abstract void listen() throws TTransportException;

  public final TTransport accept() throws TTransportException {
    TTransport transport = acceptImpl();
    if (transport == null) {
      throw new TTransportException("accept() may not return NULL");
    }
    return transport;
  }

  public abstract void close();

  protected abstract TTransport acceptImpl() throws TTransportException;

  public void interrupt() {}

}
