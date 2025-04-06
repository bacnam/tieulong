package org.apache.mina.handler.chain;

import org.apache.mina.core.session.IoSession;

public interface IoHandlerCommand {
  void execute(NextCommand paramNextCommand, IoSession paramIoSession, Object paramObject) throws Exception;
  
  public static interface NextCommand {
    void execute(IoSession param1IoSession, Object param1Object) throws Exception;
  }
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/chain/IoHandlerCommand.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */