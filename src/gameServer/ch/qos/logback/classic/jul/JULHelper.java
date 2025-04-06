/*    */ package ch.qos.logback.classic.jul;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JULHelper
/*    */ {
/*    */   public static final boolean isRegularNonRootLogger(Logger julLogger) {
/* 23 */     if (julLogger == null)
/* 24 */       return false; 
/* 25 */     return !julLogger.getName().equals("");
/*    */   }
/*    */   
/*    */   public static final boolean isRoot(Logger julLogger) {
/* 29 */     if (julLogger == null)
/* 30 */       return false; 
/* 31 */     return julLogger.getName().equals("");
/*    */   }
/*    */   
/*    */   public static Level asJULLevel(Level lbLevel) {
/* 35 */     if (lbLevel == null) {
/* 36 */       throw new IllegalArgumentException("Unexpected level [null]");
/*    */     }
/* 38 */     switch (lbLevel.levelInt) {
/*    */       case -2147483648:
/* 40 */         return Level.ALL;
/*    */       case 5000:
/* 42 */         return Level.FINEST;
/*    */       case 10000:
/* 44 */         return Level.FINE;
/*    */       case 20000:
/* 46 */         return Level.INFO;
/*    */       case 30000:
/* 48 */         return Level.WARNING;
/*    */       case 40000:
/* 50 */         return Level.SEVERE;
/*    */       case 2147483647:
/* 52 */         return Level.OFF;
/*    */     } 
/* 54 */     throw new IllegalArgumentException("Unexpected level [" + lbLevel + "]");
/*    */   }
/*    */ 
/*    */   
/*    */   public static String asJULLoggerName(String loggerName) {
/* 59 */     if ("ROOT".equals(loggerName)) {
/* 60 */       return "";
/*    */     }
/* 62 */     return loggerName;
/*    */   }
/*    */   
/*    */   public static Logger asJULLogger(String loggerName) {
/* 66 */     String julLoggerName = asJULLoggerName(loggerName);
/* 67 */     return Logger.getLogger(julLoggerName);
/*    */   }
/*    */   
/*    */   public static Logger asJULLogger(Logger logger) {
/* 71 */     return asJULLogger(logger.getName());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/jul/JULHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */