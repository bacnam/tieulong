package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildBossChallengeBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "challengeTimes", comment = "挑战次数")
private int challengeTimes;
@DataBaseField(type = "int(11)", fieldname = "challengeMaxTimes", comment = "挑战最大次数")
private int challengeMaxTimes;
@DataBaseField(type = "int(11)", fieldname = "challengeBuyTimes", comment = "购买挑战次数")
private int challengeBuyTimes;
@DataBaseField(type = "bigint(20)", size = 30, fieldname = "totalDamage", comment = "BOSS伤害")
private List<Long> totalDamage;
@DataBaseField(type = "bigint(20)", size = 30, fieldname = "damageRank", comment = "BOSS伤害")
private List<Long> damageRank;
@DataBaseField(type = "int(11)", fieldname = "beginFightTime", comment = "玩家开始挑战boss时间,用于计算玩家战斗时间")
private int beginFightTime;
@DataBaseField(type = "int(11)", fieldname = "leaveFightTime", comment = "玩家退出boss时间,用于计算玩家挑战CD时间")
private int leaveFightTime;
@DataBaseField(type = "int(11)", fieldname = "attackTimes", comment = "今日攻击次数")
private int attackTimes;

public GuildBossChallengeBO() {
this.id = 0L;
this.pid = 0L;
this.challengeTimes = 0;
this.challengeMaxTimes = 0;
this.challengeBuyTimes = 0;
this.totalDamage = new ArrayList<>(30); int i;
for (i = 0; i < 30; i++) {
this.totalDamage.add(Long.valueOf(0L));
}
this.damageRank = new ArrayList<>(30);
for (i = 0; i < 30; i++) {
this.damageRank.add(Long.valueOf(0L));
}
this.beginFightTime = 0;
this.leaveFightTime = 0;
this.attackTimes = 0;
}

public GuildBossChallengeBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.challengeTimes = rs.getInt(3);
this.challengeMaxTimes = rs.getInt(4);
this.challengeBuyTimes = rs.getInt(5);
this.totalDamage = new ArrayList<>(30); int i;
for (i = 0; i < 30; i++) {
this.totalDamage.add(Long.valueOf(rs.getLong(i + 6)));
}
this.damageRank = new ArrayList<>(30);
for (i = 0; i < 30; i++) {
this.damageRank.add(Long.valueOf(rs.getLong(i + 36)));
}
this.beginFightTime = rs.getInt(66);
this.leaveFightTime = rs.getInt(67);
this.attackTimes = rs.getInt(68);
}

public void getFromResultSet(ResultSet rs, List<GuildBossChallengeBO> list) throws Exception {
list.add(new GuildBossChallengeBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `challengeTimes`, `challengeMaxTimes`, `challengeBuyTimes`, `totalDamage_0`, `totalDamage_1`, `totalDamage_2`, `totalDamage_3`, `totalDamage_4`, `totalDamage_5`, `totalDamage_6`, `totalDamage_7`, `totalDamage_8`, `totalDamage_9`, `totalDamage_10`, `totalDamage_11`, `totalDamage_12`, `totalDamage_13`, `totalDamage_14`, `totalDamage_15`, `totalDamage_16`, `totalDamage_17`, `totalDamage_18`, `totalDamage_19`, `totalDamage_20`, `totalDamage_21`, `totalDamage_22`, `totalDamage_23`, `totalDamage_24`, `totalDamage_25`, `totalDamage_26`, `totalDamage_27`, `totalDamage_28`, `totalDamage_29`, `damageRank_0`, `damageRank_1`, `damageRank_2`, `damageRank_3`, `damageRank_4`, `damageRank_5`, `damageRank_6`, `damageRank_7`, `damageRank_8`, `damageRank_9`, `damageRank_10`, `damageRank_11`, `damageRank_12`, `damageRank_13`, `damageRank_14`, `damageRank_15`, `damageRank_16`, `damageRank_17`, `damageRank_18`, `damageRank_19`, `damageRank_20`, `damageRank_21`, `damageRank_22`, `damageRank_23`, `damageRank_24`, `damageRank_25`, `damageRank_26`, `damageRank_27`, `damageRank_28`, `damageRank_29`, `beginFightTime`, `leaveFightTime`, `attackTimes`";
}

public String getTableName() {
return "`guildBossChallenge`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.challengeTimes).append("', ");
strBuf.append("'").append(this.challengeMaxTimes).append("', ");
strBuf.append("'").append(this.challengeBuyTimes).append("', "); int i;
for (i = 0; i < this.totalDamage.size(); i++) {
strBuf.append("'").append(this.totalDamage.get(i)).append("', ");
}
for (i = 0; i < this.damageRank.size(); i++) {
strBuf.append("'").append(this.damageRank.get(i)).append("', ");
}
strBuf.append("'").append(this.beginFightTime).append("', ");
strBuf.append("'").append(this.leaveFightTime).append("', ");
strBuf.append("'").append(this.attackTimes).append("', ");
strBuf.deleteCharAt(strBuf.length() - 2);
return strBuf.toString();
}

public ArrayList<byte[]> getInsertValueBytes() {
ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
return ret;
}

public void setId(long iID) {
this.id = iID;
}

public long getId() {
return this.id;
}

public long getPid() {
return this.pid;
} public void setPid(long pid) {
if (pid == this.pid)
return; 
this.pid = pid;
}
public void savePid(long pid) {
if (pid == this.pid)
return; 
this.pid = pid;
saveField("pid", Long.valueOf(pid));
}

public int getChallengeTimes() {
return this.challengeTimes;
} public void setChallengeTimes(int challengeTimes) {
if (challengeTimes == this.challengeTimes)
return; 
this.challengeTimes = challengeTimes;
}
public void saveChallengeTimes(int challengeTimes) {
if (challengeTimes == this.challengeTimes)
return; 
this.challengeTimes = challengeTimes;
saveField("challengeTimes", Integer.valueOf(challengeTimes));
}

public int getChallengeMaxTimes() {
return this.challengeMaxTimes;
} public void setChallengeMaxTimes(int challengeMaxTimes) {
if (challengeMaxTimes == this.challengeMaxTimes)
return; 
this.challengeMaxTimes = challengeMaxTimes;
}
public void saveChallengeMaxTimes(int challengeMaxTimes) {
if (challengeMaxTimes == this.challengeMaxTimes)
return; 
this.challengeMaxTimes = challengeMaxTimes;
saveField("challengeMaxTimes", Integer.valueOf(challengeMaxTimes));
}

public int getChallengeBuyTimes() {
return this.challengeBuyTimes;
} public void setChallengeBuyTimes(int challengeBuyTimes) {
if (challengeBuyTimes == this.challengeBuyTimes)
return; 
this.challengeBuyTimes = challengeBuyTimes;
}
public void saveChallengeBuyTimes(int challengeBuyTimes) {
if (challengeBuyTimes == this.challengeBuyTimes)
return; 
this.challengeBuyTimes = challengeBuyTimes;
saveField("challengeBuyTimes", Integer.valueOf(challengeBuyTimes));
}

public int getTotalDamageSize() {
return this.totalDamage.size();
} public List<Long> getTotalDamageAll() { return new ArrayList<>(this.totalDamage); }
public void setTotalDamageAll(long value) { for (int i = 0; i < this.totalDamage.size(); ) { this.totalDamage.set(i, Long.valueOf(value)); i++; }
} public void saveTotalDamageAll(long value) { setTotalDamageAll(value); saveAll(); } public long getTotalDamage(int index) {
return ((Long)this.totalDamage.get(index)).longValue();
} public void setTotalDamage(int index, long value) {
if (value == ((Long)this.totalDamage.get(index)).longValue())
return; 
this.totalDamage.set(index, Long.valueOf(value));
}
public void saveTotalDamage(int index, long value) {
if (value == ((Long)this.totalDamage.get(index)).longValue())
return; 
this.totalDamage.set(index, Long.valueOf(value));
saveField("totalDamage_" + index, this.totalDamage.get(index));
}

public int getDamageRankSize() {
return this.damageRank.size();
} public List<Long> getDamageRankAll() { return new ArrayList<>(this.damageRank); }
public void setDamageRankAll(long value) { for (int i = 0; i < this.damageRank.size(); ) { this.damageRank.set(i, Long.valueOf(value)); i++; }
} public void saveDamageRankAll(long value) { setDamageRankAll(value); saveAll(); } public long getDamageRank(int index) {
return ((Long)this.damageRank.get(index)).longValue();
} public void setDamageRank(int index, long value) {
if (value == ((Long)this.damageRank.get(index)).longValue())
return; 
this.damageRank.set(index, Long.valueOf(value));
}
public void saveDamageRank(int index, long value) {
if (value == ((Long)this.damageRank.get(index)).longValue())
return; 
this.damageRank.set(index, Long.valueOf(value));
saveField("damageRank_" + index, this.damageRank.get(index));
}

public int getBeginFightTime() {
return this.beginFightTime;
} public void setBeginFightTime(int beginFightTime) {
if (beginFightTime == this.beginFightTime)
return; 
this.beginFightTime = beginFightTime;
}
public void saveBeginFightTime(int beginFightTime) {
if (beginFightTime == this.beginFightTime)
return; 
this.beginFightTime = beginFightTime;
saveField("beginFightTime", Integer.valueOf(beginFightTime));
}

public int getLeaveFightTime() {
return this.leaveFightTime;
} public void setLeaveFightTime(int leaveFightTime) {
if (leaveFightTime == this.leaveFightTime)
return; 
this.leaveFightTime = leaveFightTime;
}
public void saveLeaveFightTime(int leaveFightTime) {
if (leaveFightTime == this.leaveFightTime)
return; 
this.leaveFightTime = leaveFightTime;
saveField("leaveFightTime", Integer.valueOf(leaveFightTime));
}

public int getAttackTimes() {
return this.attackTimes;
} public void setAttackTimes(int attackTimes) {
if (attackTimes == this.attackTimes)
return; 
this.attackTimes = attackTimes;
}
public void saveAttackTimes(int attackTimes) {
if (attackTimes == this.attackTimes)
return; 
this.attackTimes = attackTimes;
saveField("attackTimes", Integer.valueOf(attackTimes));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `challengeTimes` = '").append(this.challengeTimes).append("',");
sBuilder.append(" `challengeMaxTimes` = '").append(this.challengeMaxTimes).append("',");
sBuilder.append(" `challengeBuyTimes` = '").append(this.challengeBuyTimes).append("',"); int i;
for (i = 0; i < this.totalDamage.size(); i++) {
sBuilder.append(" `totalDamage_").append(i).append("` = '").append(this.totalDamage.get(i)).append("',");
}
for (i = 0; i < this.damageRank.size(); i++) {
sBuilder.append(" `damageRank_").append(i).append("` = '").append(this.damageRank.get(i)).append("',");
}
sBuilder.append(" `beginFightTime` = '").append(this.beginFightTime).append("',");
sBuilder.append(" `leaveFightTime` = '").append(this.leaveFightTime).append("',");
sBuilder.append(" `attackTimes` = '").append(this.attackTimes).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `guildBossChallenge` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`challengeTimes` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengeMaxTimes` int(11) NOT NULL DEFAULT '0' COMMENT '挑战最大次数',`challengeBuyTimes` int(11) NOT NULL DEFAULT '0' COMMENT '购买挑战次数',`totalDamage_0` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_1` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_2` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_3` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_4` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_5` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_6` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_7` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_8` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_9` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_10` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_11` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_12` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_13` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_14` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_15` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_16` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_17` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_18` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_19` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_20` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_21` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_22` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_23` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_24` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_25` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_26` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_27` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_28` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_29` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_0` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_1` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_2` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_3` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_4` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_5` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_6` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_7` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_8` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_9` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_10` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_11` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_12` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_13` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_14` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_15` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_16` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_17` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_18` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_19` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_20` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_21` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_22` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_23` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_24` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_25` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_26` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_27` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_28` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_29` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`beginFightTime` int(11) NOT NULL DEFAULT '0' COMMENT '玩家开始挑战boss时间,用于计算玩家战斗时间',`leaveFightTime` int(11) NOT NULL DEFAULT '0' COMMENT '玩家退出boss时间,用于计算玩家挑战CD时间',`attackTimes` int(11) NOT NULL DEFAULT '0' COMMENT '今日攻击次数',PRIMARY KEY (`id`)) COMMENT='玩家挑战帮派Boss信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

