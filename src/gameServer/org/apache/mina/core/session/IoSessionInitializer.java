package org.apache.mina.core.session;

public interface IoSessionInitializer<T extends org.apache.mina.core.future.IoFuture> {
  void initializeSession(IoSession paramIoSession, T paramT);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IoSessionInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */