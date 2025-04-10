

package org.apache.thrift.server;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

public abstract class TServer {

  protected TProcessorFactory processorFactory_;

  protected TServerTransport serverTransport_;

  protected TTransportFactory inputTransportFactory_;

  protected TTransportFactory outputTransportFactory_;

  protected TProtocolFactory inputProtocolFactory_;

  protected TProtocolFactory outputProtocolFactory_;

  protected TServer(TProcessorFactory processorFactory,
                    TServerTransport serverTransport) {
    this(processorFactory,
         serverTransport,
         new TTransportFactory(),
         new TTransportFactory(),
         new TBinaryProtocol.Factory(),
         new TBinaryProtocol.Factory());
  }

  protected TServer(TProcessorFactory processorFactory,
                    TServerTransport serverTransport,
                    TTransportFactory transportFactory) {
    this(processorFactory,
         serverTransport,
         transportFactory,
         transportFactory,
         new TBinaryProtocol.Factory(),
         new TBinaryProtocol.Factory());
  }

  protected TServer(TProcessorFactory processorFactory,
                    TServerTransport serverTransport,
                    TTransportFactory transportFactory,
                    TProtocolFactory protocolFactory) {
    this(processorFactory,
         serverTransport,
         transportFactory,
         transportFactory,
         protocolFactory,
         protocolFactory);
  }

  protected TServer(TProcessorFactory processorFactory,
                    TServerTransport serverTransport,
                    TTransportFactory inputTransportFactory,
                    TTransportFactory outputTransportFactory,
                    TProtocolFactory inputProtocolFactory,
                    TProtocolFactory outputProtocolFactory) {
    processorFactory_ = processorFactory;
    serverTransport_ = serverTransport;
    inputTransportFactory_ = inputTransportFactory;
    outputTransportFactory_ = outputTransportFactory;
    inputProtocolFactory_ = inputProtocolFactory;
    outputProtocolFactory_ = outputProtocolFactory;
  }

  public abstract void serve();

  public void stop() {}

}
