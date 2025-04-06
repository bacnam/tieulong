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
/*     */ public class GuildMemberBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "帮会Id")
/*     */   private long guildId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "donate", comment = "贡献值")
/*     */   private int donate;
/*     */   @DataBaseField(type = "int(11)", fieldname = "weekDonate", comment = "周贡献值")
/*     */   private int weekDonate;
/*     */   @DataBaseField(type = "int(11)", fieldname = "historyDonate", comment = "历史贡献值")
/*     */   private int historyDonate;
/*     */   @DataBaseField(type = "int(11)", fieldname = "job", comment = "帮会职务")
/*     */   private int job;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastLeaveTime", comment = "上一次退出时间")
/*     */   private int lastLeaveTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "joinTime", comment = "加入时间")
/*     */   private int joinTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "sacrificeDonate", comment = "当天献祭贡献值")
/*     */   private int sacrificeDonate;
/*     */   @DataBaseField(type = "int(11)", fieldname = "sacrificeStatus", comment = "祭天状态")
/*     */   private int sacrificeStatus;
/*     */   @DataBaseField(type = "int(11)", fieldname = "skillpoints", comment = "帮派技能点")
/*     */   private int skillpoints;
/*     */   @DataBaseField(type = "int(11)", fieldname = "guildwarInspire", comment = "帮派战鼓舞次数")
/*     */   private int guildwarInspire;
/*     */   @DataBaseField(type = "int(11)", fieldname = "longnvPickReward", comment = "守卫龙女领奖次数")
/*     */   private int longnvPickReward;
/*     */   
/*     */   public GuildMemberBO() {
/*  44 */     this.id = 0L;
/*  45 */     this.pid = 0L;
/*  46 */     this.guildId = 0L;
/*  47 */     this.donate = 0;
/*  48 */     this.weekDonate = 0;
/*  49 */     this.historyDonate = 0;
/*  50 */     this.job = 0;
/*  51 */     this.lastLeaveTime = 0;
/*  52 */     this.joinTime = 0;
/*  53 */     this.sacrificeDonate = 0;
/*  54 */     this.sacrificeStatus = 0;
/*  55 */     this.skillpoints = 0;
/*  56 */     this.guildwarInspire = 0;
/*  57 */     this.longnvPickReward = 0;
/*     */   }
/*     */   
/*     */   public GuildMemberBO(ResultSet rs) throws Exception {
/*  61 */     this.id = rs.getLong(1);
/*  62 */     this.pid = rs.getLong(2);
/*  63 */     this.guildId = rs.getLong(3);
/*  64 */     this.donate = rs.getInt(4);
/*  65 */     this.weekDonate = rs.getInt(5);
/*  66 */     this.historyDonate = rs.getInt(6);
/*  67 */     this.job = rs.getInt(7);
/*  68 */     this.lastLeaveTime = rs.getInt(8);
/*  69 */     this.joinTime = rs.getInt(9);
/*  70 */     this.sacrificeDonate = rs.getInt(10);
/*  71 */     this.sacrificeStatus = rs.getInt(11);
/*  72 */     this.skillpoints = rs.getInt(12);
/*  73 */     this.guildwarInspire = rs.getInt(13);
/*  74 */     this.longnvPickReward = rs.getInt(14);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildMemberBO> list) throws Exception {
/*  80 */     list.add(new GuildMemberBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  85 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  90 */     return "`id`, `pid`, `guildId`, `donate`, `weekDonate`, `historyDonate`, `job`, `lastLeaveTime`, `joinTime`, `sacrificeDonate`, `sacrificeStatus`, `skillpoints`, `guildwarInspire`, `longnvPickReward`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  95 */     return "`guildMember`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 100 */     StringBuilder strBuf = new StringBuilder();
/* 101 */     strBuf.append("'").append(this.id).append("', ");
/* 102 */     strBuf.append("'").append(this.pid).append("', ");
/* 103 */     strBuf.append("'").append(this.guildId).append("', ");
/* 104 */     strBuf.append("'").append(this.donate).append("', ");
/* 105 */     strBuf.append("'").append(this.weekDonate).append("', ");
/* 106 */     strBuf.append("'").append(this.historyDonate).append("', ");
/* 107 */     strBuf.append("'").append(this.job).append("', ");
/* 108 */     strBuf.append("'").append(this.lastLeaveTime).append("', ");
/* 109 */     strBuf.append("'").append(this.joinTime).append("', ");
/* 110 */     strBuf.append("'").append(this.sacrificeDonate).append("', ");
/* 111 */     strBuf.append("'").append(this.sacrificeStatus).append("', ");
/* 112 */     strBuf.append("'").append(this.skillpoints).append("', ");
/* 113 */     strBuf.append("'").append(this.guildwarInspire).append("', ");
/* 114 */     strBuf.append("'").append(this.longnvPickReward).append("', ");
/* 115 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 116 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 121 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 122 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 127 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 132 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 136 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 138 */     if (pid == this.pid)
/*     */       return; 
/* 140 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 143 */     if (pid == this.pid)
/*     */       return; 
/* 145 */     this.pid = pid;
/* 146 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getGuildId() {
/* 150 */     return this.guildId;
/*     */   } public void setGuildId(long guildId) {
/* 152 */     if (guildId == this.guildId)
/*     */       return; 
/* 154 */     this.guildId = guildId;
/*     */   }
/*     */   public void saveGuildId(long guildId) {
/* 157 */     if (guildId == this.guildId)
/*     */       return; 
/* 159 */     this.guildId = guildId;
/* 160 */     saveField("guildId", Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public int getDonate() {
/* 164 */     return this.donate;
/*     */   } public void setDonate(int donate) {
/* 166 */     if (donate == this.donate)
/*     */       return; 
/* 168 */     this.donate = donate;
/*     */   }
/*     */   public void saveDonate(int donate) {
/* 171 */     if (donate == this.donate)
/*     */       return; 
/* 173 */     this.donate = donate;
/* 174 */     saveField("donate", Integer.valueOf(donate));
/*     */   }
/*     */   
/*     */   public int getWeekDonate() {
/* 178 */     return this.weekDonate;
/*     */   } public void setWeekDonate(int weekDonate) {
/* 180 */     if (weekDonate == this.weekDonate)
/*     */       return; 
/* 182 */     this.weekDonate = weekDonate;
/*     */   }
/*     */   public void saveWeekDonate(int weekDonate) {
/* 185 */     if (weekDonate == this.weekDonate)
/*     */       return; 
/* 187 */     this.weekDonate = weekDonate;
/* 188 */     saveField("weekDonate", Integer.valueOf(weekDonate));
/*     */   }
/*     */   
/*     */   public int getHistoryDonate() {
/* 192 */     return this.historyDonate;
/*     */   } public void setHistoryDonate(int historyDonate) {
/* 194 */     if (historyDonate == this.historyDonate)
/*     */       return; 
/* 196 */     this.historyDonate = historyDonate;
/*     */   }
/*     */   public void saveHistoryDonate(int historyDonate) {
/* 199 */     if (historyDonate == this.historyDonate)
/*     */       return; 
/* 201 */     this.historyDonate = historyDonate;
/* 202 */     saveField("historyDonate", Integer.valueOf(historyDonate));
/*     */   }
/*     */   
/*     */   public int getJob() {
/* 206 */     return this.job;
/*     */   } public void setJob(int job) {
/* 208 */     if (job == this.job)
/*     */       return; 
/* 210 */     this.job = job;
/*     */   }
/*     */   public void saveJob(int job) {
/* 213 */     if (job == this.job)
/*     */       return; 
/* 215 */     this.job = job;
/* 216 */     saveField("job", Integer.valueOf(job));
/*     */   }
/*     */   
/*     */   public int getLastLeaveTime() {
/* 220 */     return this.lastLeaveTime;
/*     */   } public void setLastLeaveTime(int lastLeaveTime) {
/* 222 */     if (lastLeaveTime == this.lastLeaveTime)
/*     */       return; 
/* 224 */     this.lastLeaveTime = lastLeaveTime;
/*     */   }
/*     */   public void saveLastLeaveTime(int lastLeaveTime) {
/* 227 */     if (lastLeaveTime == this.lastLeaveTime)
/*     */       return; 
/* 229 */     this.lastLeaveTime = lastLeaveTime;
/* 230 */     saveField("lastLeaveTime", Integer.valueOf(lastLeaveTime));
/*     */   }
/*     */   
/*     */   public int getJoinTime() {
/* 234 */     return this.joinTime;
/*     */   } public void setJoinTime(int joinTime) {
/* 236 */     if (joinTime == this.joinTime)
/*     */       return; 
/* 238 */     this.joinTime = joinTime;
/*     */   }
/*     */   public void saveJoinTime(int joinTime) {
/* 241 */     if (joinTime == this.joinTime)
/*     */       return; 
/* 243 */     this.joinTime = joinTime;
/* 244 */     saveField("joinTime", Integer.valueOf(joinTime));
/*     */   }
/*     */   
/*     */   public int getSacrificeDonate() {
/* 248 */     return this.sacrificeDonate;
/*     */   } public void setSacrificeDonate(int sacrificeDonate) {
/* 250 */     if (sacrificeDonate == this.sacrificeDonate)
/*     */       return; 
/* 252 */     this.sacrificeDonate = sacrificeDonate;
/*     */   }
/*     */   public void saveSacrificeDonate(int sacrificeDonate) {
/* 255 */     if (sacrificeDonate == this.sacrificeDonate)
/*     */       return; 
/* 257 */     this.sacrificeDonate = sacrificeDonate;
/* 258 */     saveField("sacrificeDonate", Integer.valueOf(sacrificeDonate));
/*     */   }
/*     */   
/*     */   public int getSacrificeStatus() {
/* 262 */     return this.sacrificeStatus;
/*     */   } public void setSacrificeStatus(int sacrificeStatus) {
/* 264 */     if (sacrificeStatus == this.sacrificeStatus)
/*     */       return; 
/* 266 */     this.sacrificeStatus = sacrificeStatus;
/*     */   }
/*     */   public void saveSacrificeStatus(int sacrificeStatus) {
/* 269 */     if (sacrificeStatus == this.sacrificeStatus)
/*     */       return; 
/* 271 */     this.sacrificeStatus = sacrificeStatus;
/* 272 */     saveField("sacrificeStatus", Integer.valueOf(sacrificeStatus));
/*     */   }
/*     */   
/*     */   public int getSkillpoints() {
/* 276 */     return this.skillpoints;
/*     */   } public void setSkillpoints(int skillpoints) {
/* 278 */     if (skillpoints == this.skillpoints)
/*     */       return; 
/* 280 */     this.skillpoints = skillpoints;
/*     */   }
/*     */   public void saveSkillpoints(int skillpoints) {
/* 283 */     if (skillpoints == this.skillpoints)
/*     */       return; 
/* 285 */     this.skillpoints = skillpoints;
/* 286 */     saveField("skillpoints", Integer.valueOf(skillpoints));
/*     */   }
/*     */   
/*     */   public int getGuildwarInspire() {
/* 290 */     return this.guildwarInspire;
/*     */   } public void setGuildwarInspire(int guildwarInspire) {
/* 292 */     if (guildwarInspire == this.guildwarInspire)
/*     */       return; 
/* 294 */     this.guildwarInspire = guildwarInspire;
/*     */   }
/*     */   public void saveGuildwarInspire(int guildwarInspire) {
/* 297 */     if (guildwarInspire == this.guildwarInspire)
/*     */       return; 
/* 299 */     this.guildwarInspire = guildwarInspire;
/* 300 */     saveField("guildwarInspire", Integer.valueOf(guildwarInspire));
/*     */   }
/*     */   
/*     */   public int getLongnvPickReward() {
/* 304 */     return this.longnvPickReward;
/*     */   } public void setLongnvPickReward(int longnvPickReward) {
/* 306 */     if (longnvPickReward == this.longnvPickReward)
/*     */       return; 
/* 308 */     this.longnvPickReward = longnvPickReward;
/*     */   }
/*     */   public void saveLongnvPickReward(int longnvPickReward) {
/* 311 */     if (longnvPickReward == this.longnvPickReward)
/*     */       return; 
/* 313 */     this.longnvPickReward = longnvPickReward;
/* 314 */     saveField("longnvPickReward", Integer.valueOf(longnvPickReward));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 321 */     StringBuilder sBuilder = new StringBuilder();
/* 322 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 323 */     sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
/* 324 */     sBuilder.append(" `donate` = '").append(this.donate).append("',");
/* 325 */     sBuilder.append(" `weekDonate` = '").append(this.weekDonate).append("',");
/* 326 */     sBuilder.append(" `historyDonate` = '").append(this.historyDonate).append("',");
/* 327 */     sBuilder.append(" `job` = '").append(this.job).append("',");
/* 328 */     sBuilder.append(" `lastLeaveTime` = '").append(this.lastLeaveTime).append("',");
/* 329 */     sBuilder.append(" `joinTime` = '").append(this.joinTime).append("',");
/* 330 */     sBuilder.append(" `sacrificeDonate` = '").append(this.sacrificeDonate).append("',");
/* 331 */     sBuilder.append(" `sacrificeStatus` = '").append(this.sacrificeStatus).append("',");
/* 332 */     sBuilder.append(" `skillpoints` = '").append(this.skillpoints).append("',");
/* 333 */     sBuilder.append(" `guildwarInspire` = '").append(this.guildwarInspire).append("',");
/* 334 */     sBuilder.append(" `longnvPickReward` = '").append(this.longnvPickReward).append("',");
/* 335 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 336 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 340 */     String sql = "CREATE TABLE IF NOT EXISTS `guildMember` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮会Id',`donate` int(11) NOT NULL DEFAULT '0' COMMENT '贡献值',`weekDonate` int(11) NOT NULL DEFAULT '0' COMMENT '周贡献值',`historyDonate` int(11) NOT NULL DEFAULT '0' COMMENT '历史贡献值',`job` int(11) NOT NULL DEFAULT '0' COMMENT '帮会职务',`lastLeaveTime` int(11) NOT NULL DEFAULT '0' COMMENT '上一次退出时间',`joinTime` int(11) NOT NULL DEFAULT '0' COMMENT '加入时间',`sacrificeDonate` int(11) NOT NULL DEFAULT '0' COMMENT '当天献祭贡献值',`sacrificeStatus` int(11) NOT NULL DEFAULT '0' COMMENT '祭天状态',`skillpoints` int(11) NOT NULL DEFAULT '0' COMMENT '帮派技能点',`guildwarInspire` int(11) NOT NULL DEFAULT '0' COMMENT '帮派战鼓舞次数',`longnvPickReward` int(11) NOT NULL DEFAULT '0' COMMENT '守卫龙女领奖次数',PRIMARY KEY (`id`)) COMMENT='帮会成员信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/*     */ 
/*     */ 
/*     */       
/* 356 */       ServerConfig.getInitialID() + 1L);
/* 357 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildMemberBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */