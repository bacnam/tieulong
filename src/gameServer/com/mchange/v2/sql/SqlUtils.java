/*     */ package com.mchange.v2.sql;
/*     */ 
/*     */ import com.mchange.lang.ThrowableUtils;
/*     */ import com.mchange.v2.lang.VersionUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.sql.SQLClientInfoException;
/*     */ import java.sql.SQLException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SqlUtils
/*     */ {
/*  49 */   static final MLogger logger = MLog.getLogger(SqlUtils.class);
/*     */ 
/*     */   
/*  52 */   static final DateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
/*     */   
/*     */   public static final String DRIVER_MANAGER_USER_PROPERTY = "user";
/*     */   
/*     */   public static final String DRIVER_MANAGER_PASSWORD_PROPERTY = "password";
/*     */   
/*     */   public static String escapeBadSqlPatternChars(String paramString) {
/*  59 */     StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
/*  60 */     for (b = 0, i = stringBuffer.length(); b < i; b++) {
/*  61 */       if (stringBuffer.charAt(b) == '\'') {
/*     */         
/*  63 */         stringBuffer.insert(b, '\'');
/*  64 */         i++;
/*  65 */         b += 2;
/*     */       } 
/*  67 */     }  return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   public static synchronized String escapeAsTimestamp(Date paramDate) {
/*  71 */     return "{ts '" + tsdf.format(paramDate) + "'}";
/*     */   }
/*     */   public static SQLException toSQLException(Throwable paramThrowable) {
/*  74 */     return toSQLException(null, paramThrowable);
/*     */   }
/*     */   public static SQLException toSQLException(String paramString, Throwable paramThrowable) {
/*  77 */     return toSQLException(paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public static SQLException toSQLException(String paramString1, String paramString2, Throwable paramThrowable) {
/*  81 */     if (paramThrowable instanceof SQLException) {
/*     */       
/*  83 */       if (logger.isLoggable(MLevel.FINER)) {
/*     */ 
/*     */ 
/*     */         
/*  87 */         SQLException sQLException1 = (SQLException)paramThrowable;
/*  88 */         StringBuffer stringBuffer = new StringBuffer(255);
/*  89 */         stringBuffer.append("Attempted to convert SQLException to SQLException. Leaving it alone.");
/*  90 */         stringBuffer.append(" [SQLState: ");
/*  91 */         stringBuffer.append(sQLException1.getSQLState());
/*  92 */         stringBuffer.append("; errorCode: ");
/*  93 */         stringBuffer.append(sQLException1.getErrorCode());
/*  94 */         stringBuffer.append(']');
/*  95 */         if (paramString1 != null)
/*  96 */           stringBuffer.append(" Ignoring suggested message: '" + paramString1 + "'."); 
/*  97 */         logger.log(MLevel.FINER, stringBuffer.toString(), paramThrowable);
/*     */         
/*  99 */         SQLException sQLException2 = sQLException1;
/* 100 */         while ((sQLException2 = sQLException2.getNextException()) != null)
/* 101 */           logger.log(MLevel.FINER, "Nested SQLException or SQLWarning: ", sQLException2); 
/*     */       } 
/* 103 */       return (SQLException)paramThrowable;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (logger.isLoggable(MLevel.FINE)) {
/* 111 */       logger.log(MLevel.FINE, "Converting Throwable to SQLException...", paramThrowable);
/*     */     }
/*     */     
/* 114 */     if (paramString1 == null)
/* 115 */       paramString1 = "An SQLException was provoked by the following failure: " + paramThrowable.toString(); 
/* 116 */     if (VersionUtils.isAtLeastJavaVersion14()) {
/*     */       
/* 118 */       SQLException sQLException = new SQLException(paramString1);
/* 119 */       sQLException.initCause(paramThrowable);
/* 120 */       return sQLException;
/*     */     } 
/*     */     
/* 123 */     return new SQLException(paramString1 + System.getProperty("line.separator") + "[Cause: " + ThrowableUtils.extractStackTrace(paramThrowable) + ']', paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SQLClientInfoException toSQLClientInfoException(Throwable paramThrowable) {
/* 130 */     if (paramThrowable instanceof SQLClientInfoException)
/* 131 */       return (SQLClientInfoException)paramThrowable; 
/* 132 */     if (paramThrowable.getCause() instanceof SQLClientInfoException)
/* 133 */       return (SQLClientInfoException)paramThrowable.getCause(); 
/* 134 */     if (paramThrowable instanceof SQLException) {
/*     */       
/* 136 */       SQLException sQLException = (SQLException)paramThrowable;
/* 137 */       return new SQLClientInfoException(sQLException.getMessage(), sQLException.getSQLState(), sQLException.getErrorCode(), null, paramThrowable);
/*     */     } 
/*     */     
/* 140 */     return new SQLClientInfoException(paramThrowable.getMessage(), null, paramThrowable);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/SqlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */