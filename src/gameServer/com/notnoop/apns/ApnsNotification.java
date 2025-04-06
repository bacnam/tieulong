package com.notnoop.apns;

public interface ApnsNotification {
  byte[] getDeviceToken();
  
  byte[] getPayload();
  
  int getIdentifier();
  
  int getExpiry();
  
  byte[] marshall();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/ApnsNotification.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */