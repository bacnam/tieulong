/*    */ package com.notnoop.apns.internal;
/*    */ 
/*    */ import com.notnoop.apns.ApnsNotification;
/*    */ import com.notnoop.apns.EnhancedApnsNotification;
/*    */ import com.notnoop.exceptions.NetworkIOException;
/*    */ import java.util.Collection;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
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
/*    */ public class ApnsServiceImpl
/*    */   extends AbstractApnsService
/*    */ {
/*    */   private ApnsConnection connection;
/*    */   
/*    */   public ApnsServiceImpl(ApnsConnection connection, ApnsFeedbackConnection feedback) {
/* 40 */     super(feedback);
/* 41 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public void push(ApnsNotification msg) throws NetworkIOException {
/* 46 */     this.connection.sendMessage(msg);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {}
/*    */   
/*    */   public void stop() {
/* 53 */     Utilities.close(this.connection);
/*    */   }
/*    */   
/*    */   public void testConnection() {
/* 57 */     this.connection.testConnection();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/ApnsServiceImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */