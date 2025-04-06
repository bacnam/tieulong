/*    */ package com.zhonglian.server.common.db.mgr;
/*    */ 
/*    */ import com.zhonglian.server.common.db.DBCons;
/*    */ import com.zhonglian.server.common.db.IDBConnectionFactory;
/*    */ import com.zhonglian.server.common.db.version.DBVersionManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Log_DBVersionManager
/*    */   extends DBVersionManager
/*    */ {
/* 13 */   private static Log_DBVersionManager instance = null;
/*    */   
/*    */   public static Log_DBVersionManager getInstance() {
/* 16 */     if (instance == null) {
/* 17 */       instance = new Log_DBVersionManager();
/*    */     }
/* 19 */     return instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public IDBConnectionFactory getConn() {
/* 24 */     return DBCons.getLogDBFactory();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/mgr/Log_DBVersionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */