package org.apache.mina.filter.codec;

import org.apache.mina.core.future.WriteFuture;

public interface ProtocolEncoderOutput {
  void write(Object paramObject);
  
  void mergeAll();
  
  WriteFuture flush();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolEncoderOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */