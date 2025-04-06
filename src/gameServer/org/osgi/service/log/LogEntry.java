package org.osgi.service.log;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public interface LogEntry {
  Bundle getBundle();
  
  ServiceReference getServiceReference();
  
  int getLevel();
  
  String getMessage();
  
  Throwable getException();
  
  long getTime();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/osgi/service/log/LogEntry.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */