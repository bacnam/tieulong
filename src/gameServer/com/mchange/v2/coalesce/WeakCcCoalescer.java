/*    */ package com.mchange.v2.coalesce;
/*    */ 
/*    */ import com.mchange.v1.identicator.IdWeakHashMap;
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
/*    */ final class WeakCcCoalescer
/*    */   extends AbstractWeakCoalescer
/*    */   implements Coalescer
/*    */ {
/*    */   WeakCcCoalescer(CoalesceChecker paramCoalesceChecker) {
/* 45 */     super((Map)new IdWeakHashMap(new CoalesceIdenticator(paramCoalesceChecker)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/WeakCcCoalescer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */