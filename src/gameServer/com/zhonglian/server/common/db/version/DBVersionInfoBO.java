/*    */ package com.zhonglian.server.common.db.version;
/*    */ 
/*    */ import com.zhonglian.server.common.db.IBaseBO;
/*    */ import com.zhonglian.server.common.db.IDBConnectionFactory;
/*    */ import java.sql.ResultSet;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DBVersionInfoBO
/*    */   implements IBaseBO
/*    */ {
/*    */   private long m_id;
/*    */   private String m_version;
/*    */   private String m_info;
/*    */   
/*    */   public DBVersionInfoBO() {
/* 16 */     this.m_id = 0L;
/* 17 */     this.m_version = "0.0.0";
/* 18 */     this.m_info = "";
/*    */   }
/*    */   
/*    */   public DBVersionInfoBO(DBVersionInfoBO _bo) {
/* 22 */     this.m_id = _bo.m_id;
/* 23 */     this.m_version = _bo.m_version;
/* 24 */     this.m_info = _bo.m_info;
/*    */   }
/*    */   
/*    */   public DBVersionInfoBO(int id, String version, String info) {
/* 28 */     this.m_id = id;
/* 29 */     this.m_version = version;
/* 30 */     this.m_info = info;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void getFromResultSet(ResultSet rs, List<DBVersionInfoBO> list) throws Exception {
/* 36 */     DBVersionInfoBO bo = new DBVersionInfoBO();
/*    */     
/* 38 */     bo.m_id = rs.getInt(1);
/* 39 */     bo.m_version = rs.getString(2);
/* 40 */     bo.m_info = rs.getString(3);
/*    */     
/* 42 */     list.add(bo);
/*    */   }
/*    */   
/*    */   public String createTableSql() {
/* 46 */     return String.format("CREATE TABLE %s (%s int(11) NOT NULL, %s varchar(64) NOT NULL, %s varchar(64), PRIMARY KEY (%s));", new Object[] { getTableName(), "ID", 
/* 47 */           "Version", "Info", "ID" });
/*    */   }
/*    */ 
/*    */   
/*    */   public String getItemsName() {
/* 52 */     return "`ID`, `Version`, `Info`";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTableName() {
/* 57 */     return "`TableVersionInfo`";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getItemsValue() {
/* 62 */     StringBuilder strBuf = new StringBuilder();
/*    */     
/* 64 */     strBuf.append(this.m_id).append(",'");
/* 65 */     strBuf.append(this.m_version).append("','");
/* 66 */     strBuf.append(this.m_info).append("'");
/*    */     
/* 68 */     return strBuf.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setId(long iID) {
/* 73 */     this.m_id = iID;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getId() {
/* 78 */     return this.m_id;
/*    */   }
/*    */   
/*    */   public void setVersion(String version) {
/* 82 */     this.m_version = version;
/*    */   }
/*    */   
/*    */   public String getVersion() {
/* 86 */     return this.m_version;
/*    */   }
/*    */   
/*    */   public void setInfo(String info) {
/* 90 */     this.m_info = info;
/*    */   }
/*    */   
/*    */   public String getInfo() {
/* 94 */     return this.m_info;
/*    */   }
/*    */ 
/*    */   
/*    */   public IDBConnectionFactory getConnectionFactory() {
/* 99 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/version/DBVersionInfoBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */