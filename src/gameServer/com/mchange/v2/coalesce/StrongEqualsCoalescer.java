/*    */ package com.mchange.v2.coalesce;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ final class StrongEqualsCoalescer
/*    */   extends AbstractStrongCoalescer
/*    */   implements Coalescer
/*    */ {
/*    */   StrongEqualsCoalescer() {
/* 45 */     super(new HashMap<Object, Object>());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/StrongEqualsCoalescer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */