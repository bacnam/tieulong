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
/*     */ public class WorldBossKillRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "bossId", comment = "BossId[对应于RefWorldBoss表ID]")
/*     */   private long bossId;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "bossLevel", comment = "Boss等级")
/*     */   private long bossLevel;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "killerPid", comment = "击杀者pid")
/*     */   private long killerPid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "killerName", comment = "击杀者名称")
/*     */   private String killerName;
/*     */   @DataBaseField(type = "int(11)", fieldname = "killerLevel", comment = "击杀者等级")
/*     */   private int killerLevel;
/*     */   @DataBaseField(type = "int(11)", fieldname = "killerIcon", comment = "击杀者头像")
/*     */   private int killerIcon;
/*     */   @DataBaseField(type = "int(11)", fieldname = "deathTime", comment = "Boss死亡时间")
/*     */   private int deathTime;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "fightTime", comment = "战斗时间")
/*     */   private long fightTime;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "rewardItemId", comment = "奖励物品信息")
/*     */   private String rewardItemId;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "rewardItemCount", comment = "奖励物品数量")
/*     */   private String rewardItemCount;
/*     */   
/*     */   public WorldBossKillRecordBO() {
/*  38 */     this.id = 0L;
/*  39 */     this.bossId = 0L;
/*  40 */     this.bossLevel = 0L;
/*  41 */     this.killerPid = 0L;
/*  42 */     this.killerName = "";
/*  43 */     this.killerLevel = 0;
/*  44 */     this.killerIcon = 0;
/*  45 */     this.deathTime = 0;
/*  46 */     this.fightTime = 0L;
/*  47 */     this.rewardItemId = "";
/*  48 */     this.rewardItemCount = "";
/*     */   }
/*     */   
/*     */   public WorldBossKillRecordBO(ResultSet rs) throws Exception {
/*  52 */     this.id = rs.getLong(1);
/*  53 */     this.bossId = rs.getLong(2);
/*  54 */     this.bossLevel = rs.getLong(3);
/*  55 */     this.killerPid = rs.getLong(4);
/*  56 */     this.killerName = rs.getString(5);
/*  57 */     this.killerLevel = rs.getInt(6);
/*  58 */     this.killerIcon = rs.getInt(7);
/*  59 */     this.deathTime = rs.getInt(8);
/*  60 */     this.fightTime = rs.getLong(9);
/*  61 */     this.rewardItemId = rs.getString(10);
/*  62 */     this.rewardItemCount = rs.getString(11);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<WorldBossKillRecordBO> list) throws Exception {
/*  68 */     list.add(new WorldBossKillRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  73 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  78 */     return "`id`, `bossId`, `bossLevel`, `killerPid`, `killerName`, `killerLevel`, `killerIcon`, `deathTime`, `fightTime`, `rewardItemId`, `rewardItemCount`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  83 */     return "`worldBossKillRecord`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  88 */     StringBuilder strBuf = new StringBuilder();
/*  89 */     strBuf.append("'").append(this.id).append("', ");
/*  90 */     strBuf.append("'").append(this.bossId).append("', ");
/*  91 */     strBuf.append("'").append(this.bossLevel).append("', ");
/*  92 */     strBuf.append("'").append(this.killerPid).append("', ");
/*  93 */     strBuf.append("'").append((this.killerName == null) ? null : this.killerName.replace("'", "''")).append("', ");
/*  94 */     strBuf.append("'").append(this.killerLevel).append("', ");
/*  95 */     strBuf.append("'").append(this.killerIcon).append("', ");
/*  96 */     strBuf.append("'").append(this.deathTime).append("', ");
/*  97 */     strBuf.append("'").append(this.fightTime).append("', ");
/*  98 */     strBuf.append("'").append((this.rewardItemId == null) ? null : this.rewardItemId.replace("'", "''")).append("', ");
/*  99 */     strBuf.append("'").append((this.rewardItemCount == null) ? null : this.rewardItemCount.replace("'", "''")).append("', ");
/* 100 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 101 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 106 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 107 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 112 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 117 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getBossId() {
/* 121 */     return this.bossId;
/*     */   } public void setBossId(long bossId) {
/* 123 */     if (bossId == this.bossId)
/*     */       return; 
/* 125 */     this.bossId = bossId;
/*     */   }
/*     */   public void saveBossId(long bossId) {
/* 128 */     if (bossId == this.bossId)
/*     */       return; 
/* 130 */     this.bossId = bossId;
/* 131 */     saveField("bossId", Long.valueOf(bossId));
/*     */   }
/*     */   
/*     */   public long getBossLevel() {
/* 135 */     return this.bossLevel;
/*     */   } public void setBossLevel(long bossLevel) {
/* 137 */     if (bossLevel == this.bossLevel)
/*     */       return; 
/* 139 */     this.bossLevel = bossLevel;
/*     */   }
/*     */   public void saveBossLevel(long bossLevel) {
/* 142 */     if (bossLevel == this.bossLevel)
/*     */       return; 
/* 144 */     this.bossLevel = bossLevel;
/* 145 */     saveField("bossLevel", Long.valueOf(bossLevel));
/*     */   }
/*     */   
/*     */   public long getKillerPid() {
/* 149 */     return this.killerPid;
/*     */   } public void setKillerPid(long killerPid) {
/* 151 */     if (killerPid == this.killerPid)
/*     */       return; 
/* 153 */     this.killerPid = killerPid;
/*     */   }
/*     */   public void saveKillerPid(long killerPid) {
/* 156 */     if (killerPid == this.killerPid)
/*     */       return; 
/* 158 */     this.killerPid = killerPid;
/* 159 */     saveField("killerPid", Long.valueOf(killerPid));
/*     */   }
/*     */   
/*     */   public String getKillerName() {
/* 163 */     return this.killerName;
/*     */   } public void setKillerName(String killerName) {
/* 165 */     if (killerName.equals(this.killerName))
/*     */       return; 
/* 167 */     this.killerName = killerName;
/*     */   }
/*     */   public void saveKillerName(String killerName) {
/* 170 */     if (killerName.equals(this.killerName))
/*     */       return; 
/* 172 */     this.killerName = killerName;
/* 173 */     saveField("killerName", killerName);
/*     */   }
/*     */   
/*     */   public int getKillerLevel() {
/* 177 */     return this.killerLevel;
/*     */   } public void setKillerLevel(int killerLevel) {
/* 179 */     if (killerLevel == this.killerLevel)
/*     */       return; 
/* 181 */     this.killerLevel = killerLevel;
/*     */   }
/*     */   public void saveKillerLevel(int killerLevel) {
/* 184 */     if (killerLevel == this.killerLevel)
/*     */       return; 
/* 186 */     this.killerLevel = killerLevel;
/* 187 */     saveField("killerLevel", Integer.valueOf(killerLevel));
/*     */   }
/*     */   
/*     */   public int getKillerIcon() {
/* 191 */     return this.killerIcon;
/*     */   } public void setKillerIcon(int killerIcon) {
/* 193 */     if (killerIcon == this.killerIcon)
/*     */       return; 
/* 195 */     this.killerIcon = killerIcon;
/*     */   }
/*     */   public void saveKillerIcon(int killerIcon) {
/* 198 */     if (killerIcon == this.killerIcon)
/*     */       return; 
/* 200 */     this.killerIcon = killerIcon;
/* 201 */     saveField("killerIcon", Integer.valueOf(killerIcon));
/*     */   }
/*     */   
/*     */   public int getDeathTime() {
/* 205 */     return this.deathTime;
/*     */   } public void setDeathTime(int deathTime) {
/* 207 */     if (deathTime == this.deathTime)
/*     */       return; 
/* 209 */     this.deathTime = deathTime;
/*     */   }
/*     */   public void saveDeathTime(int deathTime) {
/* 212 */     if (deathTime == this.deathTime)
/*     */       return; 
/* 214 */     this.deathTime = deathTime;
/* 215 */     saveField("deathTime", Integer.valueOf(deathTime));
/*     */   }
/*     */   
/*     */   public long getFightTime() {
/* 219 */     return this.fightTime;
/*     */   } public void setFightTime(long fightTime) {
/* 221 */     if (fightTime == this.fightTime)
/*     */       return; 
/* 223 */     this.fightTime = fightTime;
/*     */   }
/*     */   public void saveFightTime(long fightTime) {
/* 226 */     if (fightTime == this.fightTime)
/*     */       return; 
/* 228 */     this.fightTime = fightTime;
/* 229 */     saveField("fightTime", Long.valueOf(fightTime));
/*     */   }
/*     */   
/*     */   public String getRewardItemId() {
/* 233 */     return this.rewardItemId;
/*     */   } public void setRewardItemId(String rewardItemId) {
/* 235 */     if (rewardItemId.equals(this.rewardItemId))
/*     */       return; 
/* 237 */     this.rewardItemId = rewardItemId;
/*     */   }
/*     */   public void saveRewardItemId(String rewardItemId) {
/* 240 */     if (rewardItemId.equals(this.rewardItemId))
/*     */       return; 
/* 242 */     this.rewardItemId = rewardItemId;
/* 243 */     saveField("rewardItemId", rewardItemId);
/*     */   }
/*     */   
/*     */   public String getRewardItemCount() {
/* 247 */     return this.rewardItemCount;
/*     */   } public void setRewardItemCount(String rewardItemCount) {
/* 249 */     if (rewardItemCount.equals(this.rewardItemCount))
/*     */       return; 
/* 251 */     this.rewardItemCount = rewardItemCount;
/*     */   }
/*     */   public void saveRewardItemCount(String rewardItemCount) {
/* 254 */     if (rewardItemCount.equals(this.rewardItemCount))
/*     */       return; 
/* 256 */     this.rewardItemCount = rewardItemCount;
/* 257 */     saveField("rewardItemCount", rewardItemCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 264 */     StringBuilder sBuilder = new StringBuilder();
/* 265 */     sBuilder.append(" `bossId` = '").append(this.bossId).append("',");
/* 266 */     sBuilder.append(" `bossLevel` = '").append(this.bossLevel).append("',");
/* 267 */     sBuilder.append(" `killerPid` = '").append(this.killerPid).append("',");
/* 268 */     sBuilder.append(" `killerName` = '").append((this.killerName == null) ? null : this.killerName.replace("'", "''")).append("',");
/* 269 */     sBuilder.append(" `killerLevel` = '").append(this.killerLevel).append("',");
/* 270 */     sBuilder.append(" `killerIcon` = '").append(this.killerIcon).append("',");
/* 271 */     sBuilder.append(" `deathTime` = '").append(this.deathTime).append("',");
/* 272 */     sBuilder.append(" `fightTime` = '").append(this.fightTime).append("',");
/* 273 */     sBuilder.append(" `rewardItemId` = '").append((this.rewardItemId == null) ? null : this.rewardItemId.replace("'", "''")).append("',");
/* 274 */     sBuilder.append(" `rewardItemCount` = '").append((this.rewardItemCount == null) ? null : this.rewardItemCount.replace("'", "''")).append("',");
/* 275 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 276 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 280 */     String sql = "CREATE TABLE IF NOT EXISTS `worldBossKillRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`bossId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BossId[对应于RefWorldBoss表ID]',`bossLevel` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss等级',`killerPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '击杀者pid',`killerName` varchar(500) NOT NULL DEFAULT '' COMMENT '击杀者名称',`killerLevel` int(11) NOT NULL DEFAULT '0' COMMENT '击杀者等级',`killerIcon` int(11) NOT NULL DEFAULT '0' COMMENT '击杀者头像',`deathTime` int(11) NOT NULL DEFAULT '0' COMMENT 'Boss死亡时间',`fightTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '战斗时间',`rewardItemId` varchar(500) NOT NULL DEFAULT '' COMMENT '奖励物品信息',`rewardItemCount` varchar(500) NOT NULL DEFAULT '' COMMENT '奖励物品数量',PRIMARY KEY (`id`)) COMMENT='世界boss击杀记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 293 */       ServerConfig.getInitialID() + 1L);
/* 294 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/WorldBossKillRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */