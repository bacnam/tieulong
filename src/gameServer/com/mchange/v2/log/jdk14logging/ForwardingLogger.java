/*    */ package com.mchange.v2.log.jdk14logging;
/*    */ 
/*    */ import com.mchange.v2.log.LogUtils;
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.LogRecord;
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
/*    */ public final class ForwardingLogger
/*    */   extends Logger
/*    */ {
/*    */   MLogger forwardTo;
/*    */   
/*    */   public ForwardingLogger(MLogger paramMLogger, String paramString) {
/* 48 */     super(paramMLogger.getName(), paramString);
/* 49 */     this.forwardTo = paramMLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void log(LogRecord paramLogRecord) {
/* 54 */     Level level = paramLogRecord.getLevel();
/* 55 */     MLevel mLevel = Jdk14LoggingUtils.mlevelFromLevel(level);
/*    */     
/* 57 */     String str1 = paramLogRecord.getResourceBundleName();
/* 58 */     String str2 = paramLogRecord.getMessage();
/* 59 */     Object[] arrayOfObject = paramLogRecord.getParameters();
/*    */     
/* 61 */     String str3 = LogUtils.formatMessage(str1, str2, arrayOfObject);
/*    */     
/* 63 */     Throwable throwable = paramLogRecord.getThrown();
/*    */     
/* 65 */     String str4 = paramLogRecord.getSourceClassName();
/* 66 */     String str5 = paramLogRecord.getSourceMethodName();
/*    */     
/* 68 */     int i = ((str4 != null) ? 1 : 0) & ((str5 != null) ? 1 : 0);
/*    */     
/* 70 */     if (i == 0) {
/* 71 */       this.forwardTo.log(mLevel, str3, throwable);
/*    */     } else {
/* 73 */       this.forwardTo.logp(mLevel, str4, str5, str3, throwable);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/jdk14logging/ForwardingLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */