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
/*    */ public final class APNS
/*    */ {
/*    */   private APNS() {
/* 42 */     throw new AssertionError("Uninstantiable class");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static PayloadBuilder newPayload() {
/* 48 */     return new PayloadBuilder();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ApnsServiceBuilder newService() {
/* 55 */     return new ApnsServiceBuilder();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/APNS.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */