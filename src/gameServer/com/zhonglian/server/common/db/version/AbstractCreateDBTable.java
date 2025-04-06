/*    */ package com.zhonglian.server.common.db.version;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.db.IDBConnectionFactory;
/*    */ import com.zhonglian.server.common.db.SQLExecutor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCreateDBTable
/*    */   implements ICreateDBTable
/*    */ {
/* 16 */   private String m_dbName = "";
/*    */   
/* 18 */   private String m_tableName = "";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AbstractCreateDBTable(String dbName, String tableName) {
/* 27 */     this.m_dbName = dbName;
/* 28 */     this.m_tableName = tableName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDBName() {
/* 37 */     return this.m_dbName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTableName() {
/* 46 */     return this.m_tableName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract String getCreateSql();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean run(IDBConnectionFactory _conInfo) {
/* 63 */     boolean isOk = false;
/*    */ 
/*    */     
/*    */     try {
/* 67 */       isOk = SQLExecutor.execute(getCreateSql(), _conInfo);
/* 68 */     } catch (Throwable e) {
/* 69 */       return false;
/*    */     } finally {
/* 71 */       if (isOk) {
/* 72 */         CommLog.info("process db=[{}] table=[{}]: succeed!", this.m_dbName, this.m_tableName);
/*    */       } else {
/* 74 */         CommLog.error("process db=[{}] table=[{}]: failed!", this.m_dbName, this.m_tableName);
/*    */       } 
/*    */     } 
/* 77 */     return isOk;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/version/AbstractCreateDBTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */