/*    */ package com.mchange.v2.util;
/*    */ 
/*    */ import com.mchange.v1.identicator.IdWeakHashMap;
/*    */ import com.mchange.v1.identicator.Identicator;
/*    */ import com.mchange.v1.identicator.StrongIdentityIdenticator;
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
/*    */ public final class WeakIdentityHashMapFactory
/*    */ {
/*    */   public static Map create() {
/* 45 */     StrongIdentityIdenticator strongIdentityIdenticator = new StrongIdentityIdenticator();
/* 46 */     return (Map)new IdWeakHashMap((Identicator)strongIdentityIdenticator);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/util/WeakIdentityHashMapFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */