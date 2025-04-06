/*    */ package com.zhonglian.server.common.db.mgr;
/*    */ 
/*    */ import com.zhonglian.server.common.db.DBCons;
/*    */ import com.zhonglian.server.common.db.IDBConnectionFactory;
/*    */ import com.zhonglian.server.common.db.version.DBVersionManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Game_DBVersionManager
/*    */   extends DBVersionManager
/*    */ {
/* 13 */   private static Game_DBVersionManager instance = null;
/*    */   
/*    */   public static Game_DBVersionManager getInstance() {
/* 16 */     if (instance == null) {
/* 17 */       instance = new Game_DBVersionManager();
/*    */     }
/* 19 */     return instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public IDBConnectionFactory getConn() {
/* 24 */     return DBCons.getDBFactory();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/mgr/Game_DBVersionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */