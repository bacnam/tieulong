/*    */ package com.mchange.v1.identicator;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ public final class IdHashMap
/*    */   extends IdMap
/*    */   implements Map
/*    */ {
/*    */   public IdHashMap(Identicator paramIdenticator) {
/* 43 */     super(new HashMap<Object, Object>(), paramIdenticator);
/*    */   }
/*    */   protected IdHashKey createIdKey(Object paramObject) {
/* 46 */     return new StrongIdHashKey(paramObject, this.id);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/IdHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */