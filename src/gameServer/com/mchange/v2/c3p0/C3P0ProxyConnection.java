/*    */ package com.mchange.v2.c3p0;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
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
/*    */ public interface C3P0ProxyConnection
/*    */   extends Connection
/*    */ {
/* 58 */   public static final Object RAW_CONNECTION = new Object();
/*    */   
/*    */   Object rawConnectionOperation(Method paramMethod, Object paramObject, Object[] paramArrayOfObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/C3P0ProxyConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */