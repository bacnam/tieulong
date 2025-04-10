

package org.apache.thrift.protocol;

import java.io.Serializable;

import org.apache.thrift.transport.TTransport;

public interface TProtocolFactory extends Serializable {
  public TProtocol getProtocol(TTransport trans);
}
