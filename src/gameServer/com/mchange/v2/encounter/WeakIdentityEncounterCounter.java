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
/*    */ public class WeakIdentityEncounterCounter
/*    */   extends AbstractEncounterCounter
/*    */ {
/*    */   public WeakIdentityEncounterCounter() {
/* 44 */     super(WeakIdentityHashMapFactory.create());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/encounter/WeakIdentityEncounterCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */