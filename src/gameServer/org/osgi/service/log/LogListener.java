package org.osgi.service.log;

import java.util.EventListener;

public interface LogListener extends EventListener {
  void logged(LogEntry paramLogEntry);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/osgi/service/log/LogListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */