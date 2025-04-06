/*    */ package com.notnoop.exceptions;
/*    */ 
/*    */ import com.notnoop.apns.DeliveryError;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ApnsDeliveryErrorException
/*    */   extends ApnsException
/*    */ {
/*    */   private final DeliveryError deliveryError;
/*    */   
/*    */   public ApnsDeliveryErrorException(DeliveryError error) {
/* 18 */     this.deliveryError = error;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 23 */     return "Failed to deliver notification with error code " + this.deliveryError.code();
/*    */   }
/*    */   
/*    */   public DeliveryError getDeliveryError() {
/* 27 */     return this.deliveryError;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/exceptions/ApnsDeliveryErrorException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */