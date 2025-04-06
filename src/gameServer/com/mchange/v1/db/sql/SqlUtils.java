/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import com.mchange.lang.ThrowableUtils;
/*    */ import java.sql.SQLException;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
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
/*    */ public final class SqlUtils
/*    */ {
/* 49 */   static final DateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
/*    */ 
/*    */ 
/*    */   
/*    */   public static String escapeBadSqlPatternChars(String paramString) {
/* 54 */     StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
/* 55 */     for (b = 0, i = stringBuffer.length(); b < i; b++) {
/* 56 */       if (stringBuffer.charAt(b) == '\'') {
/*    */         
/* 58 */         stringBuffer.insert(b, '\'');
/* 59 */         i++;
/* 60 */         b += 2;
/*    */       } 
/* 62 */     }  return stringBuffer.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String escapeAsTimestamp(Date paramDate) {
/* 70 */     return "{ts '" + tsdf.format(paramDate) + "'}";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static SQLException toSQLException(Throwable paramThrowable) {
/* 76 */     if (paramThrowable instanceof SQLException) {
/* 77 */       return (SQLException)paramThrowable;
/*    */     }
/*    */     
/* 80 */     paramThrowable.printStackTrace();
/* 81 */     return new SQLException(ThrowableUtils.extractStackTrace(paramThrowable));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/SqlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */