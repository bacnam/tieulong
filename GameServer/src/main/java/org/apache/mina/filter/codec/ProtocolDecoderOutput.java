package org.apache.mina.filter.codec;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IoSession;

public interface ProtocolDecoderOutput {
  void write(Object paramObject);

  void flush(IoFilter.NextFilter paramNextFilter, IoSession paramIoSession);
}

