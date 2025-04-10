
package org.apache.thrift.async;

import org.apache.thrift.transport.TNonblockingTransport;

public interface TAsyncClientFactory<T extends TAsyncClient> {
  public T getAsyncClient(TNonblockingTransport transport);
}
