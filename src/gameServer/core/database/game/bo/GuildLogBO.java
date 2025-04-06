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
/*     */ public class GuildLogBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "帮会id")
/*     */   private long guildId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "type", comment = "日志类型")
/*     */   private int type;
/*     */   @DataBaseField(type = "varchar(50)", size = 5, fieldname = "param", comment = "参数")
/*     */   private List<String> param;
/*     */   @DataBaseField(type = "int(11)", fieldname = "time", comment = "日志时间")
/*     */   private int time;
/*     */   
/*     */   public GuildLogBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.guildId = 0L;
/*  28 */     this.type = 0;
/*  29 */     this.param = new ArrayList<>(5);
/*  30 */     for (int i = 0; i < 5; i++) {
/*  31 */       this.param.add("");
/*     */     }
/*  33 */     this.time = 0;
/*     */   }
/*     */   
/*     */   public GuildLogBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.guildId = rs.getLong(2);
/*  39 */     this.type = rs.getInt(3);
/*  40 */     this.param = new ArrayList<>(5);
/*  41 */     for (int i = 0; i < 5; i++) {
/*  42 */       this.param.add(rs.getString(i + 4));
/*     */     }
/*  44 */     this.time = rs.getInt(9);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildLogBO> list) throws Exception {
/*  50 */     list.add(new GuildLogBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  55 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  60 */     return "`id`, `guildId`, `type`, `param_0`, `param_1`, `param_2`, `param_3`, `param_4`, `time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  65 */     return "`guildLog`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  70 */     StringBuilder strBuf = new StringBuilder();
/*  71 */     strBuf.append("'").append(this.id).append("', ");
/*  72 */     strBuf.append("'").append(this.guildId).append("', ");
/*  73 */     strBuf.append("'").append(this.type).append("', ");
/*  74 */     for (int i = 0; i < this.param.size(); i++) {
/*  75 */       strBuf.append("'").append((this.param.get(i) == null) ? null : ((String)this.param.get(i)).replace("'", "''")).append("', ");
/*     */     }
/*  77 */     strBuf.append("'").append(this.time).append("', ");
/*  78 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  79 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  84 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  85 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  90 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  95 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getGuildId() {
/*  99 */     return this.guildId;
/*     */   } public void setGuildId(long guildId) {
/* 101 */     if (guildId == this.guildId)
/*     */       return; 
/* 103 */     this.guildId = guildId;
/*     */   }
/*     */   public void saveGuildId(long guildId) {
/* 106 */     if (guildId == this.guildId)
/*     */       return; 
/* 108 */     this.guildId = guildId;
/* 109 */     saveField("guildId", Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public int getType() {
/* 113 */     return this.type;
/*     */   } public void setType(int type) {
/* 115 */     if (type == this.type)
/*     */       return; 
/* 117 */     this.type = type;
/*     */   }
/*     */   public void saveType(int type) {
/* 120 */     if (type == this.type)
/*     */       return; 
/* 122 */     this.type = type;
/* 123 */     saveField("type", Integer.valueOf(type));
/*     */   }
/*     */   
/*     */   public int getParamSize() {
/* 127 */     return this.param.size();
/* 128 */   } public List<String> getParamAll() { return new ArrayList<>(this.param); }
/* 129 */   public void setParamAll(String value) { for (int i = 0; i < this.param.size(); ) { this.param.set(i, value); i++; }
/* 130 */      } public void saveParamAll(String value) { setParamAll(value); saveAll(); } public String getParam(int index) {
/* 131 */     return this.param.get(index);
/*     */   } public void setParam(int index, String value) {
/* 133 */     if (value.equals(this.param.get(index)))
/*     */       return; 
/* 135 */     this.param.set(index, value);
/*     */   }
/*     */   public void saveParam(int index, String value) {
/* 138 */     if (value.equals(this.param.get(index)))
/*     */       return; 
/* 140 */     this.param.set(index, value);
/* 141 */     saveField("param_" + index, this.param.get(index));
/*     */   }
/*     */   
/*     */   public int getTime() {
/* 145 */     return this.time;
/*     */   } public void setTime(int time) {
/* 147 */     if (time == this.time)
/*     */       return; 
/* 149 */     this.time = time;
/*     */   }
/*     */   public void saveTime(int time) {
/* 152 */     if (time == this.time)
/*     */       return; 
/* 154 */     this.time = time;
/* 155 */     saveField("time", Integer.valueOf(time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 162 */     StringBuilder sBuilder = new StringBuilder();
/* 163 */     sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
/* 164 */     sBuilder.append(" `type` = '").append(this.type).append("',");
/* 165 */     for (int i = 0; i < this.param.size(); i++) {
/* 166 */       sBuilder.append(" `param_").append(i).append("` = '").append((this.param == null) ? null : ((String)this.param.get(i)).replace("'", "''")).append("',");
/*     */     }
/* 168 */     sBuilder.append(" `time` = '").append(this.time).append("',");
/* 169 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 170 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 174 */     String sql = "CREATE TABLE IF NOT EXISTS `guildLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮会id',`type` int(11) NOT NULL DEFAULT '0' COMMENT '日志类型',`param_0` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`param_1` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`param_2` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`param_3` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`param_4` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`time` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间',PRIMARY KEY (`id`)) COMMENT='竞技场信息表,记录每个玩家竞技场相关信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 185 */       ServerConfig.getInitialID() + 1L);
/* 186 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildLogBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */