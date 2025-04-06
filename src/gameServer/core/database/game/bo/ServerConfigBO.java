/*     */ package core.database.game.bo;
/*     */ 
/*     */ import com.zhonglian.server.common.db.BaseBO;
/*     */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*     */ import core.server.ServerConfig;
/*     */ import java.sql.ResultSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class ServerConfigBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "key", comment = "键")
/*     */   private String key;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "value", comment = "值")
/*     */   private String value;
/*     */   
/*     */   public ServerConfigBO() {
/*  22 */     this.id = 0L;
/*  23 */     this.key = "";
/*  24 */     this.value = "";
/*     */   }
/*     */   
/*     */   public ServerConfigBO(ResultSet rs) throws Exception {
/*  28 */     this.id = rs.getLong(1);
/*  29 */     this.key = rs.getString(2);
/*  30 */     this.value = rs.getString(3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ServerConfigBO> list) throws Exception {
/*  36 */     list.add(new ServerConfigBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  41 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  46 */     return "`id`, `key`, `value`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  51 */     return "`serverConfig`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  56 */     StringBuilder strBuf = new StringBuilder();
/*  57 */     strBuf.append("'").append(this.id).append("', ");
/*  58 */     strBuf.append("'").append((this.key == null) ? null : this.key.replace("'", "''")).append("', ");
/*  59 */     strBuf.append("'").append((this.value == null) ? null : this.value.replace("'", "''")).append("', ");
/*  60 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  61 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  66 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  67 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  72 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  77 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  81 */     return this.key;
/*     */   } public void setKey(String key) {
/*  83 */     if (key.equals(this.key))
/*     */       return; 
/*  85 */     this.key = key;
/*     */   }
/*     */   public void saveKey(String key) {
/*  88 */     if (key.equals(this.key))
/*     */       return; 
/*  90 */     this.key = key;
/*  91 */     saveField("key", key);
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  95 */     return this.value;
/*     */   } public void setValue(String value) {
/*  97 */     if (value.equals(this.value))
/*     */       return; 
/*  99 */     this.value = value;
/*     */   }
/*     */   public void saveValue(String value) {
/* 102 */     if (value.equals(this.value))
/*     */       return; 
/* 104 */     this.value = value;
/* 105 */     saveField("value", value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 112 */     StringBuilder sBuilder = new StringBuilder();
/* 113 */     sBuilder.append(" `key` = '").append((this.key == null) ? null : this.key.replace("'", "''")).append("',");
/* 114 */     sBuilder.append(" `value` = '").append((this.value == null) ? null : this.value.replace("'", "''")).append("',");
/* 115 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 116 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 120 */     String sql = "CREATE TABLE IF NOT EXISTS `serverConfig` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`key` varchar(500) NOT NULL DEFAULT '' COMMENT '键',`value` varchar(500) NOT NULL DEFAULT '' COMMENT '值',PRIMARY KEY (`id`)) COMMENT='服务器动态配置'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       ServerConfig.getInitialID() + 1L);
/* 126 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ServerConfigBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */