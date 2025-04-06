/*    */ package com.mchange.v2.encounter;
/*    */ 
/*    */ import com.mchange.v1.identicator.IdHashMap;
/*    */ import com.mchange.v1.identicator.IdWeakHashMap;
/*    */ import com.mchange.v1.identicator.Identicator;
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
/*    */ public final class EncounterUtils
/*    */ {
/*    */   public static EncounterCounter createStrong(Identicator paramIdenticator) {
/* 43 */     return new GenericEncounterCounter((Map)new IdHashMap(paramIdenticator));
/*    */   }
/*    */   public static EncounterCounter createWeak(Identicator paramIdenticator) {
/* 46 */     return new GenericEncounterCounter((Map)new IdWeakHashMap(paramIdenticator));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static EncounterCounter syncWrap(final EncounterCounter inner) {
/* 54 */     return new EncounterCounter() {
/*    */         public synchronized long encounter(Object param1Object) {
/* 56 */           return inner.encounter(param1Object);
/* 57 */         } public synchronized long reset(Object param1Object) { return inner.reset(param1Object); } public synchronized void resetAll() {
/* 58 */           inner.resetAll();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/encounter/EncounterUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */