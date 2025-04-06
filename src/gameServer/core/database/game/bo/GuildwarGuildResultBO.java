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
/*     */ public class GuildwarGuildResultBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "atkGuildId", comment = "公会ID")
/*     */   private long atkGuildId;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "defGuildId", comment = "公会ID")
/*     */   private long defGuildId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "result", comment = "结果")
/*     */   private int result;
/*     */   @DataBaseField(type = "int(11)", fieldname = "centerId", comment = "据点id")
/*     */   private int centerId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "fightTime", comment = "战斗时间")
/*     */   private int fightTime;
/*     */   
/*     */   public GuildwarGuildResultBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.atkGuildId = 0L;
/*  30 */     this.defGuildId = 0L;
/*  31 */     this.result = 0;
/*  32 */     this.centerId = 0;
/*  33 */     this.fightTime = 0;
/*     */   }
/*     */   
/*     */   public GuildwarGuildResultBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.atkGuildId = rs.getLong(2);
/*  39 */     this.defGuildId = rs.getLong(3);
/*  40 */     this.result = rs.getInt(4);
/*  41 */     this.centerId = rs.getInt(5);
/*  42 */     this.fightTime = rs.getInt(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildwarGuildResultBO> list) throws Exception {
/*  48 */     list.add(new GuildwarGuildResultBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `atkGuildId`, `defGuildId`, `result`, `centerId`, `fightTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`guildwarGuildResult`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.atkGuildId).append("', ");
/*  71 */     strBuf.append("'").append(this.defGuildId).append("', ");
/*  72 */     strBuf.append("'").append(this.result).append("', ");
/*  73 */     strBuf.append("'").append(this.centerId).append("', ");
/*  74 */     strBuf.append("'").append(this.fightTime).append("', ");
/*  75 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  76 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  81 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  82 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  87 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  92 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getAtkGuildId() {
/*  96 */     return this.atkGuildId;
/*     */   } public void setAtkGuildId(long atkGuildId) {
/*  98 */     if (atkGuildId == this.atkGuildId)
/*     */       return; 
/* 100 */     this.atkGuildId = atkGuildId;
/*     */   }
/*     */   public void saveAtkGuildId(long atkGuildId) {
/* 103 */     if (atkGuildId == this.atkGuildId)
/*     */       return; 
/* 105 */     this.atkGuildId = atkGuildId;
/* 106 */     saveField("atkGuildId", Long.valueOf(atkGuildId));
/*     */   }
/*     */   
/*     */   public long getDefGuildId() {
/* 110 */     return this.defGuildId;
/*     */   } public void setDefGuildId(long defGuildId) {
/* 112 */     if (defGuildId == this.defGuildId)
/*     */       return; 
/* 114 */     this.defGuildId = defGuildId;
/*     */   }
/*     */   public void saveDefGuildId(long defGuildId) {
/* 117 */     if (defGuildId == this.defGuildId)
/*     */       return; 
/* 119 */     this.defGuildId = defGuildId;
/* 120 */     saveField("defGuildId", Long.valueOf(defGuildId));
/*     */   }
/*     */   
/*     */   public int getResult() {
/* 124 */     return this.result;
/*     */   } public void setResult(int result) {
/* 126 */     if (result == this.result)
/*     */       return; 
/* 128 */     this.result = result;
/*     */   }
/*     */   public void saveResult(int result) {
/* 131 */     if (result == this.result)
/*     */       return; 
/* 133 */     this.result = result;
/* 134 */     saveField("result", Integer.valueOf(result));
/*     */   }
/*     */   
/*     */   public int getCenterId() {
/* 138 */     return this.centerId;
/*     */   } public void setCenterId(int centerId) {
/* 140 */     if (centerId == this.centerId)
/*     */       return; 
/* 142 */     this.centerId = centerId;
/*     */   }
/*     */   public void saveCenterId(int centerId) {
/* 145 */     if (centerId == this.centerId)
/*     */       return; 
/* 147 */     this.centerId = centerId;
/* 148 */     saveField("centerId", Integer.valueOf(centerId));
/*     */   }
/*     */   
/*     */   public int getFightTime() {
/* 152 */     return this.fightTime;
/*     */   } public void setFightTime(int fightTime) {
/* 154 */     if (fightTime == this.fightTime)
/*     */       return; 
/* 156 */     this.fightTime = fightTime;
/*     */   }
/*     */   public void saveFightTime(int fightTime) {
/* 159 */     if (fightTime == this.fightTime)
/*     */       return; 
/* 161 */     this.fightTime = fightTime;
/* 162 */     saveField("fightTime", Integer.valueOf(fightTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `atkGuildId` = '").append(this.atkGuildId).append("',");
/* 171 */     sBuilder.append(" `defGuildId` = '").append(this.defGuildId).append("',");
/* 172 */     sBuilder.append(" `result` = '").append(this.result).append("',");
/* 173 */     sBuilder.append(" `centerId` = '").append(this.centerId).append("',");
/* 174 */     sBuilder.append(" `fightTime` = '").append(this.fightTime).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `guildwarGuildResult` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`atkGuildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`defGuildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`result` int(11) NOT NULL DEFAULT '0' COMMENT '结果',`centerId` int(11) NOT NULL DEFAULT '0' COMMENT '据点id',`fightTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗时间',PRIMARY KEY (`id`)) COMMENT='公会战'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       ServerConfig.getInitialID() + 1L);
/* 189 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildwarGuildResultBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */