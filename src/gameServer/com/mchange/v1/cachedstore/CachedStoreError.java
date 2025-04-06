/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import com.mchange.lang.PotentiallySecondaryError;
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
/*    */ public class CachedStoreError
/*    */   extends PotentiallySecondaryError
/*    */ {
/*    */   public CachedStoreError(String paramString, Throwable paramThrowable) {
/* 43 */     super(paramString, paramThrowable);
/*    */   }
/*    */   public CachedStoreError(Throwable paramThrowable) {
/* 46 */     super(paramThrowable);
/*    */   }
/*    */   public CachedStoreError(String paramString) {
/* 49 */     super(paramString);
/*    */   }
/*    */   
/*    */   public CachedStoreError() {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/CachedStoreError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */