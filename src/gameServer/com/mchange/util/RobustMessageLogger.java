package com.mchange.util;

public interface RobustMessageLogger extends MessageLogger {
  void log(String paramString);
  
  void log(Throwable paramThrowable, String paramString);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/RobustMessageLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */