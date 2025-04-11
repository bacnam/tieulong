package org.apache.thrift.protocol;

import org.apache.thrift.transport.TTransport;

import java.io.Serializable;

public interface TProtocolFactory extends Serializable {
    public TProtocol getProtocol(TTransport trans);
}
