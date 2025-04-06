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
/*     */ public class LongnvWarResultBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "公会ID")
/*     */   private long guildId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "龙女等级")
/*     */   private int level;
/*     */   @DataBaseField(type = "int(11)", fieldname = "result", comment = "结果")
/*     */   private int result;
/*     */   @DataBaseField(type = "int(11)", fieldname = "challengeTime", comment = "宣战时间")
/*     */   private int challengeTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "fightTime", comment = "战斗时间")
/*     */   private int fightTime;
/*     */   
/*     */   public LongnvWarResultBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.guildId = 0L;
/*  30 */     this.level = 0;
/*  31 */     this.result = 0;
/*  32 */     this.challengeTime = 0;
/*  33 */     this.fightTime = 0;
/*     */   }
/*     */   
/*     */   public LongnvWarResultBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.guildId = rs.getLong(2);
/*  39 */     this.level = rs.getInt(3);
/*  40 */     this.result = rs.getInt(4);
/*  41 */     this.challengeTime = rs.getInt(5);
/*  42 */     this.fightTime = rs.getInt(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<LongnvWarResultBO> list) throws Exception {
/*  48 */     list.add(new LongnvWarResultBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `guildId`, `level`, `result`, `challengeTime`, `fightTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`longnvWarResult`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.guildId).append("', ");
/*  71 */     strBuf.append("'").append(this.level).append("', ");
/*  72 */     strBuf.append("'").append(this.result).append("', ");
/*  73 */     strBuf.append("'").append(this.challengeTime).append("', ");
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
/*     */   public long getGuildId() {
/*  96 */     return this.guildId;
/*     */   } public void setGuildId(long guildId) {
/*  98 */     if (guildId == this.guildId)
/*     */       return; 
/* 100 */     this.guildId = guildId;
/*     */   }
/*     */   public void saveGuildId(long guildId) {
/* 103 */     if (guildId == this.guildId)
/*     */       return; 
/* 105 */     this.guildId = guildId;
/* 106 */     saveField("guildId", Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 110 */     return this.level;
/*     */   } public void setLevel(int level) {
/* 112 */     if (level == this.level)
/*     */       return; 
/* 114 */     this.level = level;
/*     */   }
/*     */   public void saveLevel(int level) {
/* 117 */     if (level == this.level)
/*     */       return; 
/* 119 */     this.level = level;
/* 120 */     saveField("level", Integer.valueOf(level));
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
/*     */   public int getChallengeTime() {
/* 138 */     return this.challengeTime;
/*     */   } public void setChallengeTime(int challengeTime) {
/* 140 */     if (challengeTime == this.challengeTime)
/*     */       return; 
/* 142 */     this.challengeTime = challengeTime;
/*     */   }
/*     */   public void saveChallengeTime(int challengeTime) {
/* 145 */     if (challengeTime == this.challengeTime)
/*     */       return; 
/* 147 */     this.challengeTime = challengeTime;
/* 148 */     saveField("challengeTime", Integer.valueOf(challengeTime));
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
/* 170 */     sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
/* 171 */     sBuilder.append(" `level` = '").append(this.level).append("',");
/* 172 */     sBuilder.append(" `result` = '").append(this.result).append("',");
/* 173 */     sBuilder.append(" `challengeTime` = '").append(this.challengeTime).append("',");
/* 174 */     sBuilder.append(" `fightTime` = '").append(this.fightTime).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `longnvWarResult` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`level` int(11) NOT NULL DEFAULT '0' COMMENT '龙女等级',`result` int(11) NOT NULL DEFAULT '0' COMMENT '结果',`challengeTime` int(11) NOT NULL DEFAULT '0' COMMENT '宣战时间',`fightTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗时间',PRIMARY KEY (`id`)) COMMENT='公会战'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/LongnvWarResultBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */