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
/*     */ public class GuildBossChallengeBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "challengeTimes", comment = "挑战次数")
/*     */   private int challengeTimes;
/*     */   @DataBaseField(type = "int(11)", fieldname = "challengeMaxTimes", comment = "挑战最大次数")
/*     */   private int challengeMaxTimes;
/*     */   @DataBaseField(type = "int(11)", fieldname = "challengeBuyTimes", comment = "购买挑战次数")
/*     */   private int challengeBuyTimes;
/*     */   @DataBaseField(type = "bigint(20)", size = 30, fieldname = "totalDamage", comment = "BOSS伤害")
/*     */   private List<Long> totalDamage;
/*     */   @DataBaseField(type = "bigint(20)", size = 30, fieldname = "damageRank", comment = "BOSS伤害")
/*     */   private List<Long> damageRank;
/*     */   @DataBaseField(type = "int(11)", fieldname = "beginFightTime", comment = "玩家开始挑战boss时间,用于计算玩家战斗时间")
/*     */   private int beginFightTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "leaveFightTime", comment = "玩家退出boss时间,用于计算玩家挑战CD时间")
/*     */   private int leaveFightTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "attackTimes", comment = "今日攻击次数")
/*     */   private int attackTimes;
/*     */   
/*     */   public GuildBossChallengeBO() {
/*  36 */     this.id = 0L;
/*  37 */     this.pid = 0L;
/*  38 */     this.challengeTimes = 0;
/*  39 */     this.challengeMaxTimes = 0;
/*  40 */     this.challengeBuyTimes = 0;
/*  41 */     this.totalDamage = new ArrayList<>(30); int i;
/*  42 */     for (i = 0; i < 30; i++) {
/*  43 */       this.totalDamage.add(Long.valueOf(0L));
/*     */     }
/*  45 */     this.damageRank = new ArrayList<>(30);
/*  46 */     for (i = 0; i < 30; i++) {
/*  47 */       this.damageRank.add(Long.valueOf(0L));
/*     */     }
/*  49 */     this.beginFightTime = 0;
/*  50 */     this.leaveFightTime = 0;
/*  51 */     this.attackTimes = 0;
/*     */   }
/*     */   
/*     */   public GuildBossChallengeBO(ResultSet rs) throws Exception {
/*  55 */     this.id = rs.getLong(1);
/*  56 */     this.pid = rs.getLong(2);
/*  57 */     this.challengeTimes = rs.getInt(3);
/*  58 */     this.challengeMaxTimes = rs.getInt(4);
/*  59 */     this.challengeBuyTimes = rs.getInt(5);
/*  60 */     this.totalDamage = new ArrayList<>(30); int i;
/*  61 */     for (i = 0; i < 30; i++) {
/*  62 */       this.totalDamage.add(Long.valueOf(rs.getLong(i + 6)));
/*     */     }
/*  64 */     this.damageRank = new ArrayList<>(30);
/*  65 */     for (i = 0; i < 30; i++) {
/*  66 */       this.damageRank.add(Long.valueOf(rs.getLong(i + 36)));
/*     */     }
/*  68 */     this.beginFightTime = rs.getInt(66);
/*  69 */     this.leaveFightTime = rs.getInt(67);
/*  70 */     this.attackTimes = rs.getInt(68);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildBossChallengeBO> list) throws Exception {
/*  76 */     list.add(new GuildBossChallengeBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  81 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  86 */     return "`id`, `pid`, `challengeTimes`, `challengeMaxTimes`, `challengeBuyTimes`, `totalDamage_0`, `totalDamage_1`, `totalDamage_2`, `totalDamage_3`, `totalDamage_4`, `totalDamage_5`, `totalDamage_6`, `totalDamage_7`, `totalDamage_8`, `totalDamage_9`, `totalDamage_10`, `totalDamage_11`, `totalDamage_12`, `totalDamage_13`, `totalDamage_14`, `totalDamage_15`, `totalDamage_16`, `totalDamage_17`, `totalDamage_18`, `totalDamage_19`, `totalDamage_20`, `totalDamage_21`, `totalDamage_22`, `totalDamage_23`, `totalDamage_24`, `totalDamage_25`, `totalDamage_26`, `totalDamage_27`, `totalDamage_28`, `totalDamage_29`, `damageRank_0`, `damageRank_1`, `damageRank_2`, `damageRank_3`, `damageRank_4`, `damageRank_5`, `damageRank_6`, `damageRank_7`, `damageRank_8`, `damageRank_9`, `damageRank_10`, `damageRank_11`, `damageRank_12`, `damageRank_13`, `damageRank_14`, `damageRank_15`, `damageRank_16`, `damageRank_17`, `damageRank_18`, `damageRank_19`, `damageRank_20`, `damageRank_21`, `damageRank_22`, `damageRank_23`, `damageRank_24`, `damageRank_25`, `damageRank_26`, `damageRank_27`, `damageRank_28`, `damageRank_29`, `beginFightTime`, `leaveFightTime`, `attackTimes`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  91 */     return "`guildBossChallenge`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  96 */     StringBuilder strBuf = new StringBuilder();
/*  97 */     strBuf.append("'").append(this.id).append("', ");
/*  98 */     strBuf.append("'").append(this.pid).append("', ");
/*  99 */     strBuf.append("'").append(this.challengeTimes).append("', ");
/* 100 */     strBuf.append("'").append(this.challengeMaxTimes).append("', ");
/* 101 */     strBuf.append("'").append(this.challengeBuyTimes).append("', "); int i;
/* 102 */     for (i = 0; i < this.totalDamage.size(); i++) {
/* 103 */       strBuf.append("'").append(this.totalDamage.get(i)).append("', ");
/*     */     }
/* 105 */     for (i = 0; i < this.damageRank.size(); i++) {
/* 106 */       strBuf.append("'").append(this.damageRank.get(i)).append("', ");
/*     */     }
/* 108 */     strBuf.append("'").append(this.beginFightTime).append("', ");
/* 109 */     strBuf.append("'").append(this.leaveFightTime).append("', ");
/* 110 */     strBuf.append("'").append(this.attackTimes).append("', ");
/* 111 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 112 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 117 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 118 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 123 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 128 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 132 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 134 */     if (pid == this.pid)
/*     */       return; 
/* 136 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 139 */     if (pid == this.pid)
/*     */       return; 
/* 141 */     this.pid = pid;
/* 142 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getChallengeTimes() {
/* 146 */     return this.challengeTimes;
/*     */   } public void setChallengeTimes(int challengeTimes) {
/* 148 */     if (challengeTimes == this.challengeTimes)
/*     */       return; 
/* 150 */     this.challengeTimes = challengeTimes;
/*     */   }
/*     */   public void saveChallengeTimes(int challengeTimes) {
/* 153 */     if (challengeTimes == this.challengeTimes)
/*     */       return; 
/* 155 */     this.challengeTimes = challengeTimes;
/* 156 */     saveField("challengeTimes", Integer.valueOf(challengeTimes));
/*     */   }
/*     */   
/*     */   public int getChallengeMaxTimes() {
/* 160 */     return this.challengeMaxTimes;
/*     */   } public void setChallengeMaxTimes(int challengeMaxTimes) {
/* 162 */     if (challengeMaxTimes == this.challengeMaxTimes)
/*     */       return; 
/* 164 */     this.challengeMaxTimes = challengeMaxTimes;
/*     */   }
/*     */   public void saveChallengeMaxTimes(int challengeMaxTimes) {
/* 167 */     if (challengeMaxTimes == this.challengeMaxTimes)
/*     */       return; 
/* 169 */     this.challengeMaxTimes = challengeMaxTimes;
/* 170 */     saveField("challengeMaxTimes", Integer.valueOf(challengeMaxTimes));
/*     */   }
/*     */   
/*     */   public int getChallengeBuyTimes() {
/* 174 */     return this.challengeBuyTimes;
/*     */   } public void setChallengeBuyTimes(int challengeBuyTimes) {
/* 176 */     if (challengeBuyTimes == this.challengeBuyTimes)
/*     */       return; 
/* 178 */     this.challengeBuyTimes = challengeBuyTimes;
/*     */   }
/*     */   public void saveChallengeBuyTimes(int challengeBuyTimes) {
/* 181 */     if (challengeBuyTimes == this.challengeBuyTimes)
/*     */       return; 
/* 183 */     this.challengeBuyTimes = challengeBuyTimes;
/* 184 */     saveField("challengeBuyTimes", Integer.valueOf(challengeBuyTimes));
/*     */   }
/*     */   
/*     */   public int getTotalDamageSize() {
/* 188 */     return this.totalDamage.size();
/* 189 */   } public List<Long> getTotalDamageAll() { return new ArrayList<>(this.totalDamage); }
/* 190 */   public void setTotalDamageAll(long value) { for (int i = 0; i < this.totalDamage.size(); ) { this.totalDamage.set(i, Long.valueOf(value)); i++; }
/* 191 */      } public void saveTotalDamageAll(long value) { setTotalDamageAll(value); saveAll(); } public long getTotalDamage(int index) {
/* 192 */     return ((Long)this.totalDamage.get(index)).longValue();
/*     */   } public void setTotalDamage(int index, long value) {
/* 194 */     if (value == ((Long)this.totalDamage.get(index)).longValue())
/*     */       return; 
/* 196 */     this.totalDamage.set(index, Long.valueOf(value));
/*     */   }
/*     */   public void saveTotalDamage(int index, long value) {
/* 199 */     if (value == ((Long)this.totalDamage.get(index)).longValue())
/*     */       return; 
/* 201 */     this.totalDamage.set(index, Long.valueOf(value));
/* 202 */     saveField("totalDamage_" + index, this.totalDamage.get(index));
/*     */   }
/*     */   
/*     */   public int getDamageRankSize() {
/* 206 */     return this.damageRank.size();
/* 207 */   } public List<Long> getDamageRankAll() { return new ArrayList<>(this.damageRank); }
/* 208 */   public void setDamageRankAll(long value) { for (int i = 0; i < this.damageRank.size(); ) { this.damageRank.set(i, Long.valueOf(value)); i++; }
/* 209 */      } public void saveDamageRankAll(long value) { setDamageRankAll(value); saveAll(); } public long getDamageRank(int index) {
/* 210 */     return ((Long)this.damageRank.get(index)).longValue();
/*     */   } public void setDamageRank(int index, long value) {
/* 212 */     if (value == ((Long)this.damageRank.get(index)).longValue())
/*     */       return; 
/* 214 */     this.damageRank.set(index, Long.valueOf(value));
/*     */   }
/*     */   public void saveDamageRank(int index, long value) {
/* 217 */     if (value == ((Long)this.damageRank.get(index)).longValue())
/*     */       return; 
/* 219 */     this.damageRank.set(index, Long.valueOf(value));
/* 220 */     saveField("damageRank_" + index, this.damageRank.get(index));
/*     */   }
/*     */   
/*     */   public int getBeginFightTime() {
/* 224 */     return this.beginFightTime;
/*     */   } public void setBeginFightTime(int beginFightTime) {
/* 226 */     if (beginFightTime == this.beginFightTime)
/*     */       return; 
/* 228 */     this.beginFightTime = beginFightTime;
/*     */   }
/*     */   public void saveBeginFightTime(int beginFightTime) {
/* 231 */     if (beginFightTime == this.beginFightTime)
/*     */       return; 
/* 233 */     this.beginFightTime = beginFightTime;
/* 234 */     saveField("beginFightTime", Integer.valueOf(beginFightTime));
/*     */   }
/*     */   
/*     */   public int getLeaveFightTime() {
/* 238 */     return this.leaveFightTime;
/*     */   } public void setLeaveFightTime(int leaveFightTime) {
/* 240 */     if (leaveFightTime == this.leaveFightTime)
/*     */       return; 
/* 242 */     this.leaveFightTime = leaveFightTime;
/*     */   }
/*     */   public void saveLeaveFightTime(int leaveFightTime) {
/* 245 */     if (leaveFightTime == this.leaveFightTime)
/*     */       return; 
/* 247 */     this.leaveFightTime = leaveFightTime;
/* 248 */     saveField("leaveFightTime", Integer.valueOf(leaveFightTime));
/*     */   }
/*     */   
/*     */   public int getAttackTimes() {
/* 252 */     return this.attackTimes;
/*     */   } public void setAttackTimes(int attackTimes) {
/* 254 */     if (attackTimes == this.attackTimes)
/*     */       return; 
/* 256 */     this.attackTimes = attackTimes;
/*     */   }
/*     */   public void saveAttackTimes(int attackTimes) {
/* 259 */     if (attackTimes == this.attackTimes)
/*     */       return; 
/* 261 */     this.attackTimes = attackTimes;
/* 262 */     saveField("attackTimes", Integer.valueOf(attackTimes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 269 */     StringBuilder sBuilder = new StringBuilder();
/* 270 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 271 */     sBuilder.append(" `challengeTimes` = '").append(this.challengeTimes).append("',");
/* 272 */     sBuilder.append(" `challengeMaxTimes` = '").append(this.challengeMaxTimes).append("',");
/* 273 */     sBuilder.append(" `challengeBuyTimes` = '").append(this.challengeBuyTimes).append("',"); int i;
/* 274 */     for (i = 0; i < this.totalDamage.size(); i++) {
/* 275 */       sBuilder.append(" `totalDamage_").append(i).append("` = '").append(this.totalDamage.get(i)).append("',");
/*     */     }
/* 277 */     for (i = 0; i < this.damageRank.size(); i++) {
/* 278 */       sBuilder.append(" `damageRank_").append(i).append("` = '").append(this.damageRank.get(i)).append("',");
/*     */     }
/* 280 */     sBuilder.append(" `beginFightTime` = '").append(this.beginFightTime).append("',");
/* 281 */     sBuilder.append(" `leaveFightTime` = '").append(this.leaveFightTime).append("',");
/* 282 */     sBuilder.append(" `attackTimes` = '").append(this.attackTimes).append("',");
/* 283 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 284 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 288 */     String sql = "CREATE TABLE IF NOT EXISTS `guildBossChallenge` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`challengeTimes` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengeMaxTimes` int(11) NOT NULL DEFAULT '0' COMMENT '挑战最大次数',`challengeBuyTimes` int(11) NOT NULL DEFAULT '0' COMMENT '购买挑战次数',`totalDamage_0` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_1` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_2` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_3` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_4` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_5` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_6` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_7` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_8` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_9` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_10` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_11` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_12` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_13` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_14` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_15` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_16` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_17` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_18` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_19` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_20` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_21` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_22` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_23` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_24` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_25` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_26` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_27` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_28` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_29` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_0` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_1` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_2` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_3` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_4` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_5` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_6` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_7` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_8` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_9` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_10` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_11` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_12` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_13` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_14` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_15` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_16` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_17` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_18` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_19` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_20` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_21` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_22` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_23` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_24` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_25` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_26` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_27` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_28` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_29` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`beginFightTime` int(11) NOT NULL DEFAULT '0' COMMENT '玩家开始挑战boss时间,用于计算玩家战斗时间',`leaveFightTime` int(11) NOT NULL DEFAULT '0' COMMENT '玩家退出boss时间,用于计算玩家挑战CD时间',`attackTimes` int(11) NOT NULL DEFAULT '0' COMMENT '今日攻击次数',PRIMARY KEY (`id`)) COMMENT='玩家挑战帮派Boss信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 358 */       ServerConfig.getInitialID() + 1L);
/* 359 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildBossChallengeBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */