/*    */ package com.mchange.v2.c3p0;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface C3P0ProxyStatement
/*    */   extends Statement
/*    */ {
/* 58 */   public static final Object RAW_STATEMENT = new Object();
/*    */   
/*    */   Object rawStatementOperation(Method paramMethod, Object paramObject, Object[] paramArrayOfObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/C3P0ProxyStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */