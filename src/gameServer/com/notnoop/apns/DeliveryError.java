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
/*    */ public enum DeliveryError
/*    */ {
/* 43 */   NO_ERROR(0),
/* 44 */   PROCESSING_ERROR(1),
/* 45 */   MISSING_DEVICE_TOKEN(2),
/* 46 */   MISSING_TOPIC(3),
/* 47 */   MISSING_PAYLOAD(4),
/* 48 */   INVALID_TOKEN_SIZE(5),
/* 49 */   INVALID_TOPIC_SIZE(6),
/* 50 */   INVALID_PAYLOAD_SIZE(7),
/* 51 */   INVALID_TOKEN(8),
/*    */   
/* 53 */   NONE(255),
/* 54 */   UNKNOWN(254);
/*    */   private final byte code;
/*    */   
/*    */   DeliveryError(int code) {
/* 58 */     this.code = (byte)code;
/*    */   }
/*    */ 
/*    */   
/*    */   public int code() {
/* 63 */     return this.code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DeliveryError ofCode(int code) {
/* 74 */     for (DeliveryError e : values()) {
/* 75 */       if (e.code == code) {
/* 76 */         return e;
/*    */       }
/*    */     } 
/* 79 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/DeliveryError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */