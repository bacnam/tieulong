package com.notnoop.apns.internal;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.exceptions.NetworkIOException;
import java.io.Closeable;

public interface ApnsConnection extends Closeable {
  public static final int DEFAULT_CACHE_LENGTH = 100;
  
  void sendMessage(ApnsNotification paramApnsNotification) throws NetworkIOException;
  
  void testConnection() throws NetworkIOException;
  
  ApnsConnection copy();
  
  void setCacheLength(int paramInt);
  
  int getCacheLength();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/ApnsConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */