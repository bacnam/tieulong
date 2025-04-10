package org.apache.mina.core.session;

public interface IoSessionInitializer<T extends org.apache.mina.core.future.IoFuture> {
  void initializeSession(IoSession paramIoSession, T paramT);
}

