package org.osgi.service.log;

import java.util.Enumeration;

public interface LogReaderService {
  void addLogListener(LogListener paramLogListener);
  
  void removeLogListener(LogListener paramLogListener);
  
  Enumeration getLog();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/osgi/service/log/LogReaderService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */