package org.osgi.service.log;

import org.osgi.framework.ServiceReference;

public interface LogService {
  public static final int LOG_ERROR = 1;
  
  public static final int LOG_WARNING = 2;
  
  public static final int LOG_INFO = 3;
  
  public static final int LOG_DEBUG = 4;
  
  void log(int paramInt, String paramString);
  
  void log(int paramInt, String paramString, Throwable paramThrowable);
  
  void log(ServiceReference paramServiceReference, int paramInt, String paramString);
  
  void log(ServiceReference paramServiceReference, int paramInt, String paramString, Throwable paramThrowable);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/osgi/service/log/LogService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */