package com.mchange.v2.c3p0;

import java.sql.Connection;

public interface ConnectionCustomizer {
  void onAcquire(Connection paramConnection, String paramString) throws Exception;
  
  void onDestroy(Connection paramConnection, String paramString) throws Exception;
  
  void onCheckOut(Connection paramConnection, String paramString) throws Exception;
  
  void onCheckIn(Connection paramConnection, String paramString) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/ConnectionCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */