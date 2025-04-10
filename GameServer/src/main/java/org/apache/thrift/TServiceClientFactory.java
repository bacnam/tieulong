

package org.apache.thrift;

import org.apache.thrift.protocol.TProtocol;

public interface TServiceClientFactory<T extends TServiceClient> {

  public T getClient(TProtocol prot);

  public T getClient(TProtocol iprot, TProtocol oprot);
}
