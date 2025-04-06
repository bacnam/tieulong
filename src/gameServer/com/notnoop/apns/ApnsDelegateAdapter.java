package com.notnoop.apns;

public class ApnsDelegateAdapter implements ApnsDelegate {
  public void messageSent(ApnsNotification message, boolean resent) {}
  
  public void messageSendFailed(ApnsNotification message, Throwable e) {}
  
  public void connectionClosed(DeliveryError e, int messageIdentifier) {}
  
  public void cacheLengthExceeded(int newCacheLength) {}
  
  public void notificationsResent(int resendCount) {}
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/ApnsDelegateAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */