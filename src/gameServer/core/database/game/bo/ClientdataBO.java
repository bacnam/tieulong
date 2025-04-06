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
/*     */ public class ClientdataBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "text(20000)", size = 8, fieldname = "vstr", comment = "自定义字段")
/*     */   private List<String> vstr;
/*     */   
/*     */   public ClientdataBO() {
/*  22 */     this.id = 0L;
/*  23 */     this.pid = 0L;
/*  24 */     this.vstr = new ArrayList<>(8);
/*  25 */     for (int i = 0; i < 8; i++) {
/*  26 */       this.vstr.add("");
/*     */     }
/*     */   }
/*     */   
/*     */   public ClientdataBO(ResultSet rs) throws Exception {
/*  31 */     this.id = rs.getLong(1);
/*  32 */     this.pid = rs.getLong(2);
/*  33 */     this.vstr = new ArrayList<>(8);
/*  34 */     for (int i = 0; i < 8; i++) {
/*  35 */       this.vstr.add(rs.getString(i + 3));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ClientdataBO> list) throws Exception {
/*  42 */     list.add(new ClientdataBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  47 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  52 */     return "`id`, `pid`, `vstr_0`, `vstr_1`, `vstr_2`, `vstr_3`, `vstr_4`, `vstr_5`, `vstr_6`, `vstr_7`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  57 */     return "`clientdata`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  62 */     StringBuilder strBuf = new StringBuilder();
/*  63 */     strBuf.append("'").append(this.id).append("', ");
/*  64 */     strBuf.append("'").append(this.pid).append("', ");
/*  65 */     for (int i = 0; i < this.vstr.size(); i++) {
/*  66 */       strBuf.append("'").append((this.vstr.get(i) == null) ? null : ((String)this.vstr.get(i)).replace("'", "''")).append("', ");
/*     */     }
/*  68 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  69 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  74 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  75 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  80 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  85 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  89 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  91 */     if (pid == this.pid)
/*     */       return; 
/*  93 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/*  96 */     if (pid == this.pid)
/*     */       return; 
/*  98 */     this.pid = pid;
/*  99 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getVstrSize() {
/* 103 */     return this.vstr.size();
/* 104 */   } public List<String> getVstrAll() { return new ArrayList<>(this.vstr); }
/* 105 */   public void setVstrAll(String value) { for (int i = 0; i < this.vstr.size(); ) { this.vstr.set(i, value); i++; }
/* 106 */      } public void saveVstrAll(String value) { setVstrAll(value); saveAll(); } public String getVstr(int index) {
/* 107 */     return this.vstr.get(index);
/*     */   } public void setVstr(int index, String value) {
/* 109 */     if (value.equals(this.vstr.get(index)))
/*     */       return; 
/* 111 */     this.vstr.set(index, value);
/*     */   }
/*     */   public void saveVstr(int index, String value) {
/* 114 */     if (value.equals(this.vstr.get(index)))
/*     */       return; 
/* 116 */     this.vstr.set(index, value);
/* 117 */     saveField("vstr_" + index, this.vstr.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 124 */     StringBuilder sBuilder = new StringBuilder();
/* 125 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 126 */     for (int i = 0; i < this.vstr.size(); i++) {
/* 127 */       sBuilder.append(" `vstr_").append(i).append("` = '").append((this.vstr == null) ? null : ((String)this.vstr.get(i)).replace("'", "''")).append("',");
/*     */     }
/* 129 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 130 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 134 */     String sql = "CREATE TABLE IF NOT EXISTS `clientdata` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`vstr_0` text NULL COMMENT '自定义字段',`vstr_1` text NULL COMMENT '自定义字段',`vstr_2` text NULL COMMENT '自定义字段',`vstr_3` text NULL COMMENT '自定义字段',`vstr_4` text NULL COMMENT '自定义字段',`vstr_5` text NULL COMMENT '自定义字段',`vstr_6` text NULL COMMENT '自定义字段',`vstr_7` text NULL COMMENT '自定义字段',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='用户自定义数段表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 147 */       ServerConfig.getInitialID() + 1L);
/* 148 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ClientdataBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */