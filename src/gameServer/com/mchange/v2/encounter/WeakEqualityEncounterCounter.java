/*    */ package com.mchange.v2.encounter;
/*    */ 
/*    */ import java.util.WeakHashMap;
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
/*    */ public class WeakEqualityEncounterCounter
/*    */   extends AbstractEncounterCounter
/*    */ {
/*    */   public WeakEqualityEncounterCounter() {
/* 49 */     super(new WeakHashMap<Object, Object>());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/encounter/WeakEqualityEncounterCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */