package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;

public interface ProtocolCodecFactory {
  ProtocolEncoder getEncoder(IoSession paramIoSession) throws Exception;
  
  ProtocolDecoder getDecoder(IoSession paramIoSession) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolCodecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */