package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildMemberSkillBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "skillId", comment = "帮会技能静态Id")
private int skillId;
@DataBaseField(type = "int(11)", fieldname = "level", comment = "帮会技能等级")
private int level;
@DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
private int createTime;

public GuildMemberSkillBO() {
this.id = 0L;
this.pid = 0L;
this.skillId = 0;
this.level = 0;
this.createTime = 0;
}

public GuildMemberSkillBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.skillId = rs.getInt(3);
this.level = rs.getInt(4);
this.createTime = rs.getInt(5);
}

public void getFromResultSet(ResultSet rs, List<GuildMemberSkillBO> list) throws Exception {
list.add(new GuildMemberSkillBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `skillId`, `level`, `createTime`";
}

public String getTableName() {
return "`guildMemberSkill`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.skillId).append("', ");
strBuf.append("'").append(this.level).append("', ");
strBuf.append("'").append(this.createTime).append("', ");
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

public int getSkillId() {
return this.skillId;
} public void setSkillId(int skillId) {
if (skillId == this.skillId)
return; 
this.skillId = skillId;
}
public void saveSkillId(int skillId) {
if (skillId == this.skillId)
return; 
this.skillId = skillId;
saveField("skillId", Integer.valueOf(skillId));
}

public int getLevel() {
return this.level;
} public void setLevel(int level) {
if (level == this.level)
return; 
this.level = level;
}
public void saveLevel(int level) {
if (level == this.level)
return; 
this.level = level;
saveField("level", Integer.valueOf(level));
}

public int getCreateTime() {
return this.createTime;
} public void setCreateTime(int createTime) {
if (createTime == this.createTime)
return; 
this.createTime = createTime;
}
public void saveCreateTime(int createTime) {
if (createTime == this.createTime)
return; 
this.createTime = createTime;
saveField("createTime", Integer.valueOf(createTime));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `skillId` = '").append(this.skillId).append("',");
sBuilder.append(" `level` = '").append(this.level).append("',");
sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `guildMemberSkill` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`skillId` int(11) NOT NULL DEFAULT '0' COMMENT '帮会技能静态Id',`level` int(11) NOT NULL DEFAULT '0' COMMENT '帮会技能等级',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',PRIMARY KEY (`id`)) COMMENT='帮会成员技能信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

