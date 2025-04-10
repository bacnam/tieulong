

package org.apache.thrift;

import org.apache.thrift.protocol.TProtocol;

public interface TProcessor {
  public boolean process(TProtocol in, TProtocol out)
    throws TException;
}
