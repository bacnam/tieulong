package org.apache.thrift;

import org.apache.thrift.protocol.TProtocol;

public interface TServiceClient {

    public TProtocol getInputProtocol();

    public TProtocol getOutputProtocol();
}
