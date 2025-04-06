package org.apache.mina.handler.multiton;

import org.apache.mina.core.session.IoSession;

@Deprecated
public interface SingleSessionIoHandlerFactory {
  SingleSessionIoHandler getHandler(IoSession paramIoSession) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/multiton/SingleSessionIoHandlerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */