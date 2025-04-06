package com.mchange.util;

import java.io.IOException;

public interface MessageLogger {
  void log(String paramString) throws IOException;
  
  void log(Throwable paramThrowable, String paramString) throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/MessageLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */