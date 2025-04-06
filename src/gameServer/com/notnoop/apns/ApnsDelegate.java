/*    */ package com.notnoop.apns;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ApnsDelegate
/*    */ {
/* 73 */   public static final ApnsDelegate EMPTY = new ApnsDelegateAdapter();
/*    */   
/*    */   void messageSent(ApnsNotification paramApnsNotification, boolean paramBoolean);
/*    */   
/*    */   void messageSendFailed(ApnsNotification paramApnsNotification, Throwable paramThrowable);
/*    */   
/*    */   void connectionClosed(DeliveryError paramDeliveryError, int paramInt);
/*    */   
/*    */   void cacheLengthExceeded(int paramInt);
/*    */   
/*    */   void notificationsResent(int paramInt);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/ApnsDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */