/*     */ package com.mchange.v2.coalesce;
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
/*     */ public final class CoalescerFactory
/*     */ {
/*     */   public static Coalescer createCoalescer() {
/*  50 */     return createCoalescer(true, true);
/*     */   }
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
/*     */   public static Coalescer createCoalescer(boolean paramBoolean1, boolean paramBoolean2) {
/*  67 */     return createCoalescer(null, paramBoolean1, paramBoolean2);
/*     */   }
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
/*     */   public static Coalescer createCoalescer(CoalesceChecker paramCoalesceChecker, boolean paramBoolean1, boolean paramBoolean2) {
/*     */     Coalescer coalescer;
/*  95 */     if (paramCoalesceChecker == null) {
/*     */       
/*  97 */       coalescer = (Coalescer)(paramBoolean1 ? new WeakEqualsCoalescer() : new StrongEqualsCoalescer());
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 103 */       coalescer = (Coalescer)(paramBoolean1 ? new WeakCcCoalescer(paramCoalesceChecker) : new StrongCcCoalescer(paramCoalesceChecker));
/*     */     } 
/*     */ 
/*     */     
/* 107 */     return paramBoolean2 ? new SyncedCoalescer(coalescer) : coalescer;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/CoalescerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */