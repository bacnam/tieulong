package org.apache.mina.filter.codec.demux;

public interface MessageEncoderFactory<T> {
  MessageEncoder<T> getEncoder() throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/demux/MessageEncoderFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */