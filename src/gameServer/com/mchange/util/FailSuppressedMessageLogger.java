package com.mchange.util;

import java.util.Iterator;

public interface FailSuppressedMessageLogger extends RobustMessageLogger {
  Iterator getFailures();
  
  void clearFailures();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/FailSuppressedMessageLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */