/*    */ package com.notnoop.apns.internal;
/*    */ 
/*    */ import com.notnoop.apns.ReconnectPolicy;
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
/*    */ public final class ReconnectPolicies
/*    */ {
/*    */   public static class Never
/*    */     implements ReconnectPolicy
/*    */   {
/*    */     public void reconnected() {}
/*    */     
/*    */     public boolean shouldReconnect() {
/* 39 */       return false;
/*    */     } public Never copy() {
/* 41 */       return this;
/*    */     } }
/*    */   
/*    */   public static class Always implements ReconnectPolicy { public boolean shouldReconnect() {
/* 45 */       return true;
/*    */     } public Always copy() {
/* 47 */       return this;
/*    */     }
/*    */     
/*    */     public void reconnected() {} }
/*    */   
/*    */   public static class EveryHalfHour implements ReconnectPolicy { private static final long PERIOD = 1800000L;
/* 53 */     private long lastRunning = 0L;
/*    */     
/*    */     public boolean shouldReconnect() {
/* 56 */       return (System.currentTimeMillis() - this.lastRunning > 1800000L);
/*    */     }
/*    */     
/*    */     public void reconnected() {
/* 60 */       this.lastRunning = System.currentTimeMillis();
/*    */     }
/*    */     
/*    */     public EveryHalfHour copy() {
/* 64 */       return new EveryHalfHour();
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/ReconnectPolicies.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */