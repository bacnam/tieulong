package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TitleBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "title_id", comment = "称号id")
private int title_id;
@DataBaseField(type = "bigint(20)", fieldname = "value", comment = "当前数值")
private long value;
@DataBaseField(type = "tinyint(1)", fieldname = "is_using", comment = "是否正在使用")
private boolean is_using;
@DataBaseField(type = "tinyint(1)", fieldname = "is_active", comment = "是否激活")
private boolean is_active;
@DataBaseField(type = "int(11)", fieldname = "active_time", comment = "激活时间")
private int active_time;
@DataBaseField(type = "tinyint(1)", fieldname = "is_reward", comment = "是否获得奖励")
private boolean is_reward;

public TitleBO() {
this.id = 0L;
this.pid = 0L;
this.title_id = 0;
this.value = 0L;
this.is_using = false;
this.is_active = false;
this.active_time = 0;
this.is_reward = false;
}

public TitleBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.title_id = rs.getInt(3);
this.value = rs.getLong(4);
this.is_using = rs.getBoolean(5);
this.is_active = rs.getBoolean(6);
this.active_time = rs.getInt(7);
this.is_reward = rs.getBoolean(8);
}

public void getFromResultSet(ResultSet rs, List<TitleBO> list) throws Exception {
list.add(new TitleBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `title_id`, `value`, `is_using`, `is_active`, `active_time`, `is_reward`";
}

public String getTableName() {
return "`title`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.title_id).append("', ");
strBuf.append("'").append(this.value).append("', ");
strBuf.append("'").append(this.is_using ? 1 : 0).append("', ");
strBuf.append("'").append(this.is_active ? 1 : 0).append("', ");
strBuf.append("'").append(this.active_time).append("', ");
strBuf.append("'").append(this.is_reward ? 1 : 0).append("', ");
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

public int getTitleId() {
return this.title_id;
} public void setTitleId(int title_id) {
if (title_id == this.title_id)
return; 
this.title_id = title_id;
}
public void saveTitleId(int title_id) {
if (title_id == this.title_id)
return; 
this.title_id = title_id;
saveField("title_id", Integer.valueOf(title_id));
}

public long getValue() {
return this.value;
} public void setValue(long value) {
if (value == this.value)
return; 
this.value = value;
}
public void saveValue(long value) {
if (value == this.value)
return; 
this.value = value;
saveField("value", Long.valueOf(value));
}

public boolean getIsUsing() {
return this.is_using;
} public void setIsUsing(boolean is_using) {
if (is_using == this.is_using)
return; 
this.is_using = is_using;
}
public void saveIsUsing(boolean is_using) {
if (is_using == this.is_using)
return; 
this.is_using = is_using;
saveField("is_using", Integer.valueOf(is_using ? 1 : 0));
}

public boolean getIsActive() {
return this.is_active;
} public void setIsActive(boolean is_active) {
if (is_active == this.is_active)
return; 
this.is_active = is_active;
}
public void saveIsActive(boolean is_active) {
if (is_active == this.is_active)
return; 
this.is_active = is_active;
saveField("is_active", Integer.valueOf(is_active ? 1 : 0));
}

public int getActiveTime() {
return this.active_time;
} public void setActiveTime(int active_time) {
if (active_time == this.active_time)
return; 
this.active_time = active_time;
}
public void saveActiveTime(int active_time) {
if (active_time == this.active_time)
return; 
this.active_time = active_time;
saveField("active_time", Integer.valueOf(active_time));
}

public boolean getIsReward() {
return this.is_reward;
} public void setIsReward(boolean is_reward) {
if (is_reward == this.is_reward)
return; 
this.is_reward = is_reward;
}
public void saveIsReward(boolean is_reward) {
if (is_reward == this.is_reward)
return; 
this.is_reward = is_reward;
saveField("is_reward", Integer.valueOf(is_reward ? 1 : 0));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `title_id` = '").append(this.title_id).append("',");
sBuilder.append(" `value` = '").append(this.value).append("',");
sBuilder.append(" `is_using` = '").append(this.is_using ? 1 : 0).append("',");
sBuilder.append(" `is_active` = '").append(this.is_active ? 1 : 0).append("',");
sBuilder.append(" `active_time` = '").append(this.active_time).append("',");
sBuilder.append(" `is_reward` = '").append(this.is_reward ? 1 : 0).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `title` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`title_id` int(11) NOT NULL DEFAULT '0' COMMENT '称号id',`value` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前数值',`is_using` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否正在使用',`is_active` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否激活',`active_time` int(11) NOT NULL DEFAULT '0' COMMENT '激活时间',`is_reward` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否获得奖励',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

