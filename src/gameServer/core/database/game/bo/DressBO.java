package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DressBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "dress_id", comment = "时装id")
private int dress_id;
@DataBaseField(type = "int(11)", fieldname = "type", comment = "时装类型")
private int type;
@DataBaseField(type = "int(11)", fieldname = "char_id", comment = "装备的角色")
private int char_id;
@DataBaseField(type = "int(11)", fieldname = "active_time", comment = "激活时间")
private int active_time;
@DataBaseField(type = "int(11)", fieldname = "equip_time", comment = "穿戴时间")
private int equip_time;

public DressBO() {
this.id = 0L;
this.pid = 0L;
this.dress_id = 0;
this.type = 0;
this.char_id = 0;
this.active_time = 0;
this.equip_time = 0;
}

public DressBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.dress_id = rs.getInt(3);
this.type = rs.getInt(4);
this.char_id = rs.getInt(5);
this.active_time = rs.getInt(6);
this.equip_time = rs.getInt(7);
}

public void getFromResultSet(ResultSet rs, List<DressBO> list) throws Exception {
list.add(new DressBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `dress_id`, `type`, `char_id`, `active_time`, `equip_time`";
}

public String getTableName() {
return "`dress`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.dress_id).append("', ");
strBuf.append("'").append(this.type).append("', ");
strBuf.append("'").append(this.char_id).append("', ");
strBuf.append("'").append(this.active_time).append("', ");
strBuf.append("'").append(this.equip_time).append("', ");
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

public int getDressId() {
return this.dress_id;
} public void setDressId(int dress_id) {
if (dress_id == this.dress_id)
return; 
this.dress_id = dress_id;
}
public void saveDressId(int dress_id) {
if (dress_id == this.dress_id)
return; 
this.dress_id = dress_id;
saveField("dress_id", Integer.valueOf(dress_id));
}

public int getType() {
return this.type;
} public void setType(int type) {
if (type == this.type)
return; 
this.type = type;
}
public void saveType(int type) {
if (type == this.type)
return; 
this.type = type;
saveField("type", Integer.valueOf(type));
}

public int getCharId() {
return this.char_id;
} public void setCharId(int char_id) {
if (char_id == this.char_id)
return; 
this.char_id = char_id;
}
public void saveCharId(int char_id) {
if (char_id == this.char_id)
return; 
this.char_id = char_id;
saveField("char_id", Integer.valueOf(char_id));
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

public int getEquipTime() {
return this.equip_time;
} public void setEquipTime(int equip_time) {
if (equip_time == this.equip_time)
return; 
this.equip_time = equip_time;
}
public void saveEquipTime(int equip_time) {
if (equip_time == this.equip_time)
return; 
this.equip_time = equip_time;
saveField("equip_time", Integer.valueOf(equip_time));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `dress_id` = '").append(this.dress_id).append("',");
sBuilder.append(" `type` = '").append(this.type).append("',");
sBuilder.append(" `char_id` = '").append(this.char_id).append("',");
sBuilder.append(" `active_time` = '").append(this.active_time).append("',");
sBuilder.append(" `equip_time` = '").append(this.equip_time).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `dress` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`dress_id` int(11) NOT NULL DEFAULT '0' COMMENT '时装id',`type` int(11) NOT NULL DEFAULT '0' COMMENT '时装类型',`char_id` int(11) NOT NULL DEFAULT '0' COMMENT '装备的角色',`active_time` int(11) NOT NULL DEFAULT '0' COMMENT '激活时间',`equip_time` int(11) NOT NULL DEFAULT '0' COMMENT '穿戴时间',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

