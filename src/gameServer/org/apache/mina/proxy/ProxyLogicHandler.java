package org.apache.mina.proxy;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.proxy.session.ProxyIoSession;

public interface ProxyLogicHandler {
  boolean isHandshakeComplete();

  void messageReceived(IoFilter.NextFilter paramNextFilter, IoBuffer paramIoBuffer) throws ProxyAuthException;

  void doHandshake(IoFilter.NextFilter paramNextFilter) throws ProxyAuthException;

  ProxyIoSession getProxyIoSession();

  void enqueueWriteRequest(IoFilter.NextFilter paramNextFilter, WriteRequest paramWriteRequest);
}

