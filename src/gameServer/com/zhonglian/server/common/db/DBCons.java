/*    */ package com.zhonglian.server.common.db;
/*    */ 
/*    */ public class DBCons
/*    */ {
/*  5 */   private static IDBConnectionFactory DBFactory = null;
/*  6 */   private static IDBConnectionFactory logDBFactory = null;
/*    */   
/*    */   public static IDBConnectionFactory getDBFactory() {
/*  9 */     return DBFactory;
/*    */   }
/*    */   
/*    */   public static void setDBFactory(IDBConnectionFactory aGameDBFactory) {
/* 13 */     DBFactory = aGameDBFactory;
/*    */   }
/*    */   
/*    */   public static IDBConnectionFactory getLogDBFactory() {
/* 17 */     return logDBFactory;
/*    */   }
/*    */   
/*    */   public static void setLogDBFactory(IDBConnectionFactory aLogDBFactory) {
/* 21 */     logDBFactory = aLogDBFactory;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/DBCons.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */