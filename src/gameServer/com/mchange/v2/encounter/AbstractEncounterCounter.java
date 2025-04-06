/*    */ package com.mchange.v2.encounter;
/*    */ 
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
/*    */ class AbstractEncounterCounter
/*    */   implements EncounterCounter
/*    */ {
/* 42 */   static final Long ONE = new Long(1L);
/*    */   Map m;
/*    */   
/*    */   AbstractEncounterCounter(Map paramMap) {
/* 46 */     this.m = paramMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public long encounter(Object paramObject) {
/*    */     Long long_2;
/*    */     long l;
/* 53 */     Long long_1 = (Long)this.m.get(paramObject);
/*    */ 
/*    */     
/* 56 */     if (long_1 == null) {
/*    */       
/* 58 */       l = 0L;
/* 59 */       long_2 = ONE;
/*    */     }
/*    */     else {
/*    */       
/* 63 */       l = long_1.longValue();
/* 64 */       long_2 = new Long(l + 1L);
/*    */     } 
/* 66 */     this.m.put(paramObject, long_2);
/* 67 */     return l;
/*    */   }
/*    */ 
/*    */   
/*    */   public long reset(Object paramObject) {
/* 72 */     long l = encounter(paramObject);
/* 73 */     this.m.remove(paramObject);
/* 74 */     return l;
/*    */   }
/*    */   
/*    */   public void resetAll() {
/* 78 */     this.m.clear();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/encounter/AbstractEncounterCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */