package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorldBossBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "bossId", comment = "Boss关卡ID(WorldBoss静态表ID)")
private long bossId;
@DataBaseField(type = "bigint(20)", fieldname = "bossHp", comment = "Boss当前生命(复活时刷新)")
private long bossHp;
@DataBaseField(type = "bigint(20)", fieldname = "bossMaxHp", comment = "Boss最大生命(复活时刷新)")
private long bossMaxHp;
@DataBaseField(type = "bigint(20)", fieldname = "bossLevel", comment = "Boss当前等级")
private long bossLevel;
@DataBaseField(type = "tinyint(1)", fieldname = "isDead", comment = "Boss是否已死亡")
private boolean isDead;
@DataBaseField(type = "bigint(20)", fieldname = "deadTime", comment = "Boss死亡时间")
private long deadTime;
@DataBaseField(type = "bigint(20)", fieldname = "reviveTime", comment = "Boss复活时间")
private long reviveTime;
@DataBaseField(type = "bigint(20)", fieldname = "lastKillCid", comment = "上次击杀者pid")
private long lastKillCid;

public WorldBossBO() {
this.id = 0L;
this.bossId = 0L;
this.bossHp = 0L;
this.bossMaxHp = 0L;
this.bossLevel = 0L;
this.isDead = false;
this.deadTime = 0L;
this.reviveTime = 0L;
this.lastKillCid = 0L;
}

public WorldBossBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.bossId = rs.getLong(2);
this.bossHp = rs.getLong(3);
this.bossMaxHp = rs.getLong(4);
this.bossLevel = rs.getLong(5);
this.isDead = rs.getBoolean(6);
this.deadTime = rs.getLong(7);
this.reviveTime = rs.getLong(8);
this.lastKillCid = rs.getLong(9);
}

public void getFromResultSet(ResultSet rs, List<WorldBossBO> list) throws Exception {
list.add(new WorldBossBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `bossId`, `bossHp`, `bossMaxHp`, `bossLevel`, `isDead`, `deadTime`, `reviveTime`, `lastKillCid`";
}

public String getTableName() {
return "`worldBoss`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.bossId).append("', ");
strBuf.append("'").append(this.bossHp).append("', ");
strBuf.append("'").append(this.bossMaxHp).append("', ");
strBuf.append("'").append(this.bossLevel).append("', ");
strBuf.append("'").append(this.isDead ? 1 : 0).append("', ");
strBuf.append("'").append(this.deadTime).append("', ");
strBuf.append("'").append(this.reviveTime).append("', ");
strBuf.append("'").append(this.lastKillCid).append("', ");
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

public long getBossId() {
return this.bossId;
} public void setBossId(long bossId) {
if (bossId == this.bossId)
return; 
this.bossId = bossId;
}
public void saveBossId(long bossId) {
if (bossId == this.bossId)
return; 
this.bossId = bossId;
saveField("bossId", Long.valueOf(bossId));
}

public long getBossHp() {
return this.bossHp;
} public void setBossHp(long bossHp) {
if (bossHp == this.bossHp)
return; 
this.bossHp = bossHp;
}
public void saveBossHp(long bossHp) {
if (bossHp == this.bossHp)
return; 
this.bossHp = bossHp;
saveField("bossHp", Long.valueOf(bossHp));
}

public long getBossMaxHp() {
return this.bossMaxHp;
} public void setBossMaxHp(long bossMaxHp) {
if (bossMaxHp == this.bossMaxHp)
return; 
this.bossMaxHp = bossMaxHp;
}
public void saveBossMaxHp(long bossMaxHp) {
if (bossMaxHp == this.bossMaxHp)
return; 
this.bossMaxHp = bossMaxHp;
saveField("bossMaxHp", Long.valueOf(bossMaxHp));
}

public long getBossLevel() {
return this.bossLevel;
} public void setBossLevel(long bossLevel) {
if (bossLevel == this.bossLevel)
return; 
this.bossLevel = bossLevel;
}
public void saveBossLevel(long bossLevel) {
if (bossLevel == this.bossLevel)
return; 
this.bossLevel = bossLevel;
saveField("bossLevel", Long.valueOf(bossLevel));
}

public boolean getIsDead() {
return this.isDead;
} public void setIsDead(boolean isDead) {
if (isDead == this.isDead)
return; 
this.isDead = isDead;
}
public void saveIsDead(boolean isDead) {
if (isDead == this.isDead)
return; 
this.isDead = isDead;
saveField("isDead", Integer.valueOf(isDead ? 1 : 0));
}

public long getDeadTime() {
return this.deadTime;
} public void setDeadTime(long deadTime) {
if (deadTime == this.deadTime)
return; 
this.deadTime = deadTime;
}
public void saveDeadTime(long deadTime) {
if (deadTime == this.deadTime)
return; 
this.deadTime = deadTime;
saveField("deadTime", Long.valueOf(deadTime));
}

public long getReviveTime() {
return this.reviveTime;
} public void setReviveTime(long reviveTime) {
if (reviveTime == this.reviveTime)
return; 
this.reviveTime = reviveTime;
}
public void saveReviveTime(long reviveTime) {
if (reviveTime == this.reviveTime)
return; 
this.reviveTime = reviveTime;
saveField("reviveTime", Long.valueOf(reviveTime));
}

public long getLastKillCid() {
return this.lastKillCid;
} public void setLastKillCid(long lastKillCid) {
if (lastKillCid == this.lastKillCid)
return; 
this.lastKillCid = lastKillCid;
}
public void saveLastKillCid(long lastKillCid) {
if (lastKillCid == this.lastKillCid)
return; 
this.lastKillCid = lastKillCid;
saveField("lastKillCid", Long.valueOf(lastKillCid));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `bossId` = '").append(this.bossId).append("',");
sBuilder.append(" `bossHp` = '").append(this.bossHp).append("',");
sBuilder.append(" `bossMaxHp` = '").append(this.bossMaxHp).append("',");
sBuilder.append(" `bossLevel` = '").append(this.bossLevel).append("',");
sBuilder.append(" `isDead` = '").append(this.isDead ? 1 : 0).append("',");
sBuilder.append(" `deadTime` = '").append(this.deadTime).append("',");
sBuilder.append(" `reviveTime` = '").append(this.reviveTime).append("',");
sBuilder.append(" `lastKillCid` = '").append(this.lastKillCid).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `worldBoss` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`bossId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss关卡ID(WorldBoss静态表ID)',`bossHp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss当前生命(复活时刷新)',`bossMaxHp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss最大生命(复活时刷新)',`bossLevel` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss当前等级',`isDead` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Boss是否已死亡',`deadTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss死亡时间',`reviveTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss复活时间',`lastKillCid` bigint(20) NOT NULL DEFAULT '0' COMMENT '上次击杀者pid',PRIMARY KEY (`id`)) COMMENT='世界boss信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

