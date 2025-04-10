

package org.apache.thrift.transport;

import java.nio.channels.Selector;

public abstract class TNonblockingServerTransport extends TServerTransport {

  public abstract void registerSelector(Selector selector);
}
