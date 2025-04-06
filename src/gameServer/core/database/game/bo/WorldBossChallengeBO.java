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
/*     */ public class WorldBossChallengeBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "teamLevel", comment = "所属玩家等级")
/*     */   private long teamLevel;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "challengeTimes", comment = "挑战次数")
/*     */   private long challengeTimes;
/*     */   @DataBaseField(type = "bigint(20)", size = 10, fieldname = "inspiringTimes", comment = "鼓舞次数")
/*     */   private List<Long> inspiringTimes;
/*     */   @DataBaseField(type = "bigint(20)", size = 10, fieldname = "totalDamage", comment = "BOSS伤害")
/*     */   private List<Long> totalDamage;
/*     */   @DataBaseField(type = "bigint(20)", size = 10, fieldname = "damageRank", comment = "BOSS伤害")
/*     */   private List<Long> damageRank;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "beginFightTime", comment = "玩家开始挑战boss时间,用于计算玩家战斗时间")
/*     */   private long beginFightTime;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "leaveFightTime", comment = "玩家退出boss时间,用于计算玩家挑战CD时间")
/*     */   private long leaveFightTime;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "attackTimes", comment = "今日攻击次数")
/*     */   private long attackTimes;
/*     */   @DataBaseField(type = "int(11)", fieldname = "resurrection", comment = "复活次数")
/*     */   private int resurrection;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "autoChallenge", comment = "定时自动挑战世界BOSS")
/*     */   private boolean autoChallenge;
/*     */   
/*     */   public WorldBossChallengeBO() {
/*  40 */     this.id = 0L;
/*  41 */     this.pid = 0L;
/*  42 */     this.teamLevel = 0L;
/*  43 */     this.challengeTimes = 0L;
/*  44 */     this.inspiringTimes = new ArrayList<>(10); int i;
/*  45 */     for (i = 0; i < 10; i++) {
/*  46 */       this.inspiringTimes.add(Long.valueOf(0L));
/*     */     }
/*  48 */     this.totalDamage = new ArrayList<>(10);
/*  49 */     for (i = 0; i < 10; i++) {
/*  50 */       this.totalDamage.add(Long.valueOf(0L));
/*     */     }
/*  52 */     this.damageRank = new ArrayList<>(10);
/*  53 */     for (i = 0; i < 10; i++) {
/*  54 */       this.damageRank.add(Long.valueOf(0L));
/*     */     }
/*  56 */     this.beginFightTime = 0L;
/*  57 */     this.leaveFightTime = 0L;
/*  58 */     this.attackTimes = 0L;
/*  59 */     this.resurrection = 0;
/*  60 */     this.autoChallenge = false;
/*     */   }
/*     */   
/*     */   public WorldBossChallengeBO(ResultSet rs) throws Exception {
/*  64 */     this.id = rs.getLong(1);
/*  65 */     this.pid = rs.getLong(2);
/*  66 */     this.teamLevel = rs.getLong(3);
/*  67 */     this.challengeTimes = rs.getLong(4);
/*  68 */     this.inspiringTimes = new ArrayList<>(10); int i;
/*  69 */     for (i = 0; i < 10; i++) {
/*  70 */       this.inspiringTimes.add(Long.valueOf(rs.getLong(i + 5)));
/*     */     }
/*  72 */     this.totalDamage = new ArrayList<>(10);
/*  73 */     for (i = 0; i < 10; i++) {
/*  74 */       this.totalDamage.add(Long.valueOf(rs.getLong(i + 15)));
/*     */     }
/*  76 */     this.damageRank = new ArrayList<>(10);
/*  77 */     for (i = 0; i < 10; i++) {
/*  78 */       this.damageRank.add(Long.valueOf(rs.getLong(i + 25)));
/*     */     }
/*  80 */     this.beginFightTime = rs.getLong(35);
/*  81 */     this.leaveFightTime = rs.getLong(36);
/*  82 */     this.attackTimes = rs.getLong(37);
/*  83 */     this.resurrection = rs.getInt(38);
/*  84 */     this.autoChallenge = rs.getBoolean(39);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<WorldBossChallengeBO> list) throws Exception {
/*  90 */     list.add(new WorldBossChallengeBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  95 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/* 100 */     return "`id`, `pid`, `teamLevel`, `challengeTimes`, `inspiringTimes_0`, `inspiringTimes_1`, `inspiringTimes_2`, `inspiringTimes_3`, `inspiringTimes_4`, `inspiringTimes_5`, `inspiringTimes_6`, `inspiringTimes_7`, `inspiringTimes_8`, `inspiringTimes_9`, `totalDamage_0`, `totalDamage_1`, `totalDamage_2`, `totalDamage_3`, `totalDamage_4`, `totalDamage_5`, `totalDamage_6`, `totalDamage_7`, `totalDamage_8`, `totalDamage_9`, `damageRank_0`, `damageRank_1`, `damageRank_2`, `damageRank_3`, `damageRank_4`, `damageRank_5`, `damageRank_6`, `damageRank_7`, `damageRank_8`, `damageRank_9`, `beginFightTime`, `leaveFightTime`, `attackTimes`, `resurrection`, `autoChallenge`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/* 105 */     return "`worldBossChallenge`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 110 */     StringBuilder strBuf = new StringBuilder();
/* 111 */     strBuf.append("'").append(this.id).append("', ");
/* 112 */     strBuf.append("'").append(this.pid).append("', ");
/* 113 */     strBuf.append("'").append(this.teamLevel).append("', ");
/* 114 */     strBuf.append("'").append(this.challengeTimes).append("', "); int i;
/* 115 */     for (i = 0; i < this.inspiringTimes.size(); i++) {
/* 116 */       strBuf.append("'").append(this.inspiringTimes.get(i)).append("', ");
/*     */     }
/* 118 */     for (i = 0; i < this.totalDamage.size(); i++) {
/* 119 */       strBuf.append("'").append(this.totalDamage.get(i)).append("', ");
/*     */     }
/* 121 */     for (i = 0; i < this.damageRank.size(); i++) {
/* 122 */       strBuf.append("'").append(this.damageRank.get(i)).append("', ");
/*     */     }
/* 124 */     strBuf.append("'").append(this.beginFightTime).append("', ");
/* 125 */     strBuf.append("'").append(this.leaveFightTime).append("', ");
/* 126 */     strBuf.append("'").append(this.attackTimes).append("', ");
/* 127 */     strBuf.append("'").append(this.resurrection).append("', ");
/* 128 */     strBuf.append("'").append(this.autoChallenge ? 1 : 0).append("', ");
/* 129 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 130 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 135 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 136 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 141 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 146 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 150 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 152 */     if (pid == this.pid)
/*     */       return; 
/* 154 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 157 */     if (pid == this.pid)
/*     */       return; 
/* 159 */     this.pid = pid;
/* 160 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getTeamLevel() {
/* 164 */     return this.teamLevel;
/*     */   } public void setTeamLevel(long teamLevel) {
/* 166 */     if (teamLevel == this.teamLevel)
/*     */       return; 
/* 168 */     this.teamLevel = teamLevel;
/*     */   }
/*     */   public void saveTeamLevel(long teamLevel) {
/* 171 */     if (teamLevel == this.teamLevel)
/*     */       return; 
/* 173 */     this.teamLevel = teamLevel;
/* 174 */     saveField("teamLevel", Long.valueOf(teamLevel));
/*     */   }
/*     */   
/*     */   public long getChallengeTimes() {
/* 178 */     return this.challengeTimes;
/*     */   } public void setChallengeTimes(long challengeTimes) {
/* 180 */     if (challengeTimes == this.challengeTimes)
/*     */       return; 
/* 182 */     this.challengeTimes = challengeTimes;
/*     */   }
/*     */   public void saveChallengeTimes(long challengeTimes) {
/* 185 */     if (challengeTimes == this.challengeTimes)
/*     */       return; 
/* 187 */     this.challengeTimes = challengeTimes;
/* 188 */     saveField("challengeTimes", Long.valueOf(challengeTimes));
/*     */   }
/*     */   
/*     */   public int getInspiringTimesSize() {
/* 192 */     return this.inspiringTimes.size();
/* 193 */   } public List<Long> getInspiringTimesAll() { return new ArrayList<>(this.inspiringTimes); }
/* 194 */   public void setInspiringTimesAll(long value) { for (int i = 0; i < this.inspiringTimes.size(); ) { this.inspiringTimes.set(i, Long.valueOf(value)); i++; }
/* 195 */      } public void saveInspiringTimesAll(long value) { setInspiringTimesAll(value); saveAll(); } public long getInspiringTimes(int index) {
/* 196 */     return ((Long)this.inspiringTimes.get(index)).longValue();
/*     */   } public void setInspiringTimes(int index, long value) {
/* 198 */     if (value == ((Long)this.inspiringTimes.get(index)).longValue())
/*     */       return; 
/* 200 */     this.inspiringTimes.set(index, Long.valueOf(value));
/*     */   }
/*     */   public void saveInspiringTimes(int index, long value) {
/* 203 */     if (value == ((Long)this.inspiringTimes.get(index)).longValue())
/*     */       return; 
/* 205 */     this.inspiringTimes.set(index, Long.valueOf(value));
/* 206 */     saveField("inspiringTimes_" + index, this.inspiringTimes.get(index));
/*     */   }
/*     */   
/*     */   public int getTotalDamageSize() {
/* 210 */     return this.totalDamage.size();
/* 211 */   } public List<Long> getTotalDamageAll() { return new ArrayList<>(this.totalDamage); }
/* 212 */   public void setTotalDamageAll(long value) { for (int i = 0; i < this.totalDamage.size(); ) { this.totalDamage.set(i, Long.valueOf(value)); i++; }
/* 213 */      } public void saveTotalDamageAll(long value) { setTotalDamageAll(value); saveAll(); } public long getTotalDamage(int index) {
/* 214 */     return ((Long)this.totalDamage.get(index)).longValue();
/*     */   } public void setTotalDamage(int index, long value) {
/* 216 */     if (value == ((Long)this.totalDamage.get(index)).longValue())
/*     */       return; 
/* 218 */     this.totalDamage.set(index, Long.valueOf(value));
/*     */   }
/*     */   public void saveTotalDamage(int index, long value) {
/* 221 */     if (value == ((Long)this.totalDamage.get(index)).longValue())
/*     */       return; 
/* 223 */     this.totalDamage.set(index, Long.valueOf(value));
/* 224 */     saveField("totalDamage_" + index, this.totalDamage.get(index));
/*     */   }
/*     */   
/*     */   public int getDamageRankSize() {
/* 228 */     return this.damageRank.size();
/* 229 */   } public List<Long> getDamageRankAll() { return new ArrayList<>(this.damageRank); }
/* 230 */   public void setDamageRankAll(long value) { for (int i = 0; i < this.damageRank.size(); ) { this.damageRank.set(i, Long.valueOf(value)); i++; }
/* 231 */      } public void saveDamageRankAll(long value) { setDamageRankAll(value); saveAll(); } public long getDamageRank(int index) {
/* 232 */     return ((Long)this.damageRank.get(index)).longValue();
/*     */   } public void setDamageRank(int index, long value) {
/* 234 */     if (value == ((Long)this.damageRank.get(index)).longValue())
/*     */       return; 
/* 236 */     this.damageRank.set(index, Long.valueOf(value));
/*     */   }
/*     */   public void saveDamageRank(int index, long value) {
/* 239 */     if (value == ((Long)this.damageRank.get(index)).longValue())
/*     */       return; 
/* 241 */     this.damageRank.set(index, Long.valueOf(value));
/* 242 */     saveField("damageRank_" + index, this.damageRank.get(index));
/*     */   }
/*     */   
/*     */   public long getBeginFightTime() {
/* 246 */     return this.beginFightTime;
/*     */   } public void setBeginFightTime(long beginFightTime) {
/* 248 */     if (beginFightTime == this.beginFightTime)
/*     */       return; 
/* 250 */     this.beginFightTime = beginFightTime;
/*     */   }
/*     */   public void saveBeginFightTime(long beginFightTime) {
/* 253 */     if (beginFightTime == this.beginFightTime)
/*     */       return; 
/* 255 */     this.beginFightTime = beginFightTime;
/* 256 */     saveField("beginFightTime", Long.valueOf(beginFightTime));
/*     */   }
/*     */   
/*     */   public long getLeaveFightTime() {
/* 260 */     return this.leaveFightTime;
/*     */   } public void setLeaveFightTime(long leaveFightTime) {
/* 262 */     if (leaveFightTime == this.leaveFightTime)
/*     */       return; 
/* 264 */     this.leaveFightTime = leaveFightTime;
/*     */   }
/*     */   public void saveLeaveFightTime(long leaveFightTime) {
/* 267 */     if (leaveFightTime == this.leaveFightTime)
/*     */       return; 
/* 269 */     this.leaveFightTime = leaveFightTime;
/* 270 */     saveField("leaveFightTime", Long.valueOf(leaveFightTime));
/*     */   }
/*     */   
/*     */   public long getAttackTimes() {
/* 274 */     return this.attackTimes;
/*     */   } public void setAttackTimes(long attackTimes) {
/* 276 */     if (attackTimes == this.attackTimes)
/*     */       return; 
/* 278 */     this.attackTimes = attackTimes;
/*     */   }
/*     */   public void saveAttackTimes(long attackTimes) {
/* 281 */     if (attackTimes == this.attackTimes)
/*     */       return; 
/* 283 */     this.attackTimes = attackTimes;
/* 284 */     saveField("attackTimes", Long.valueOf(attackTimes));
/*     */   }
/*     */   
/*     */   public int getResurrection() {
/* 288 */     return this.resurrection;
/*     */   } public void setResurrection(int resurrection) {
/* 290 */     if (resurrection == this.resurrection)
/*     */       return; 
/* 292 */     this.resurrection = resurrection;
/*     */   }
/*     */   public void saveResurrection(int resurrection) {
/* 295 */     if (resurrection == this.resurrection)
/*     */       return; 
/* 297 */     this.resurrection = resurrection;
/* 298 */     saveField("resurrection", Integer.valueOf(resurrection));
/*     */   }
/*     */   
/*     */   public boolean getAutoChallenge() {
/* 302 */     return this.autoChallenge;
/*     */   } public void setAutoChallenge(boolean autoChallenge) {
/* 304 */     if (autoChallenge == this.autoChallenge)
/*     */       return; 
/* 306 */     this.autoChallenge = autoChallenge;
/*     */   }
/*     */   public void saveAutoChallenge(boolean autoChallenge) {
/* 309 */     if (autoChallenge == this.autoChallenge)
/*     */       return; 
/* 311 */     this.autoChallenge = autoChallenge;
/* 312 */     saveField("autoChallenge", Integer.valueOf(autoChallenge ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 319 */     StringBuilder sBuilder = new StringBuilder();
/* 320 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 321 */     sBuilder.append(" `teamLevel` = '").append(this.teamLevel).append("',");
/* 322 */     sBuilder.append(" `challengeTimes` = '").append(this.challengeTimes).append("',"); int i;
/* 323 */     for (i = 0; i < this.inspiringTimes.size(); i++) {
/* 324 */       sBuilder.append(" `inspiringTimes_").append(i).append("` = '").append(this.inspiringTimes.get(i)).append("',");
/*     */     }
/* 326 */     for (i = 0; i < this.totalDamage.size(); i++) {
/* 327 */       sBuilder.append(" `totalDamage_").append(i).append("` = '").append(this.totalDamage.get(i)).append("',");
/*     */     }
/* 329 */     for (i = 0; i < this.damageRank.size(); i++) {
/* 330 */       sBuilder.append(" `damageRank_").append(i).append("` = '").append(this.damageRank.get(i)).append("',");
/*     */     }
/* 332 */     sBuilder.append(" `beginFightTime` = '").append(this.beginFightTime).append("',");
/* 333 */     sBuilder.append(" `leaveFightTime` = '").append(this.leaveFightTime).append("',");
/* 334 */     sBuilder.append(" `attackTimes` = '").append(this.attackTimes).append("',");
/* 335 */     sBuilder.append(" `resurrection` = '").append(this.resurrection).append("',");
/* 336 */     sBuilder.append(" `autoChallenge` = '").append(this.autoChallenge ? 1 : 0).append("',");
/* 337 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 338 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 342 */     String sql = "CREATE TABLE IF NOT EXISTS `worldBossChallenge` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`teamLevel` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家等级',`challengeTimes` bigint(20) NOT NULL DEFAULT '0' COMMENT '挑战次数',`inspiringTimes_0` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_1` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_2` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_3` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_4` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_5` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_6` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_7` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_8` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_9` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`totalDamage_0` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_1` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_2` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_3` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_4` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_5` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_6` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_7` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_8` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_9` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_0` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_1` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_2` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_3` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_4` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_5` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_6` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_7` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_8` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_9` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`beginFightTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家开始挑战boss时间,用于计算玩家战斗时间',`leaveFightTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家退出boss时间,用于计算玩家挑战CD时间',`attackTimes` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日攻击次数',`resurrection` int(11) NOT NULL DEFAULT '0' COMMENT '复活次数',`autoChallenge` tinyint(1) NOT NULL DEFAULT '0' COMMENT '定时自动挑战世界BOSS',PRIMARY KEY (`id`)) COMMENT='玩家挑战世界Boss信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 383 */       ServerConfig.getInitialID() + 1L);
/* 384 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/WorldBossChallengeBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */