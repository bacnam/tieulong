package com.zhonglian.server.websocket;

import java.nio.ByteBuffer;

public interface IMessageDispatcher<Session extends BaseSession> {
  void init();
  
  int handleRawMessage(Session paramSession, ByteBuffer paramByteBuffer);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/IMessageDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */