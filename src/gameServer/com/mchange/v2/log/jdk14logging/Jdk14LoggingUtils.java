/*    */ package com.mchange.v2.log.jdk14logging;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import java.util.logging.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Jdk14LoggingUtils
/*    */ {
/*    */   public static MLevel mlevelFromLevel(Level paramLevel) {
/* 46 */     if (paramLevel == Level.ALL)
/* 47 */       return MLevel.ALL; 
/* 48 */     if (paramLevel == Level.CONFIG)
/* 49 */       return MLevel.CONFIG; 
/* 50 */     if (paramLevel == Level.FINE)
/* 51 */       return MLevel.FINE; 
/* 52 */     if (paramLevel == Level.FINER)
/* 53 */       return MLevel.FINER; 
/* 54 */     if (paramLevel == Level.FINEST)
/* 55 */       return MLevel.FINEST; 
/* 56 */     if (paramLevel == Level.INFO)
/* 57 */       return MLevel.INFO; 
/* 58 */     if (paramLevel == Level.OFF)
/* 59 */       return MLevel.OFF; 
/* 60 */     if (paramLevel == Level.SEVERE)
/* 61 */       return MLevel.SEVERE; 
/* 62 */     if (paramLevel == Level.WARNING) {
/* 63 */       return MLevel.WARNING;
/*    */     }
/* 65 */     throw new IllegalArgumentException("Unexpected Jdk14 logging level: " + paramLevel);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/jdk14logging/Jdk14LoggingUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */