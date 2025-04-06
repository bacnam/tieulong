/*    */ package com.mchange.v2.coalesce;
/*    */ 
/*    */ import com.mchange.v1.identicator.IdHashMap;
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
/*    */ final class StrongCcCoalescer
/*    */   extends AbstractStrongCoalescer
/*    */   implements Coalescer
/*    */ {
/*    */   StrongCcCoalescer(CoalesceChecker paramCoalesceChecker) {
/* 45 */     super((Map)new IdHashMap(new CoalesceIdenticator(paramCoalesceChecker)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/StrongCcCoalescer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */