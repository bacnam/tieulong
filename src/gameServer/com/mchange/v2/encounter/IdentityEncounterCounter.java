/*    */ package com.mchange.v2.encounter;
/*    */ 
/*    */ import com.mchange.v2.util.WeakIdentityHashMapFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IdentityEncounterCounter
/*    */   extends AbstractEncounterCounter
/*    */ {
/*    */   public IdentityEncounterCounter() {
/* 49 */     super(WeakIdentityHashMapFactory.create());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/encounter/IdentityEncounterCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */