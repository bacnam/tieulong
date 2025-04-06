/*     */ package com.notnoop.apns;
/*     */ 
/*     */ import com.notnoop.apns.internal.ReconnectPolicies;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ReconnectPolicy
/*     */ {
/*     */   boolean shouldReconnect();
/*     */   
/*     */   void reconnected();
/*     */   
/*     */   ReconnectPolicy copy();
/*     */   
/*     */   public enum Provided
/*     */   {
/*  84 */     NEVER
/*     */     {
/*     */       public ReconnectPolicy newObject() {
/*  87 */         return (ReconnectPolicy)new ReconnectPolicies.Never();
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     EVERY_HALF_HOUR
/*     */     {
/*     */       public ReconnectPolicy newObject() {
/* 101 */         return (ReconnectPolicy)new ReconnectPolicies.EveryHalfHour();
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     EVERY_NOTIFICATION
/*     */     {
/*     */       public ReconnectPolicy newObject() {
/* 117 */         return (ReconnectPolicy)new ReconnectPolicies.Always();
/*     */       }
/*     */     };
/*     */     
/*     */     abstract ReconnectPolicy newObject();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/ReconnectPolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */