package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DroiyanTreasureBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "treasure_id", comment = "获得密保")
private int treasure_id;
@DataBaseField(type = "int(11)", fieldname = "gain_time", comment = "发生时间")
private int gain_time;
@DataBaseField(type = "int(11)", fieldname = "expire_time", comment = "过期时间")
private int expire_time;

public DroiyanTreasureBO() {
this.id = 0L;
this.pid = 0L;
this.treasure_id = 0;
this.gain_time = 0;
this.expire_time = 0;
}

public DroiyanTreasureBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.treasure_id = rs.getInt(3);
this.gain_time = rs.getInt(4);
this.expire_time = rs.getInt(5);
}

public void getFromResultSet(ResultSet rs, List<DroiyanTreasureBO> list) throws Exception {
list.add(new DroiyanTreasureBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `treasure_id`, `gain_time`, `expire_time`";
}

public String getTableName() {
return "`droiyan_treasure`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.treasure_id).append("', ");
strBuf.append("'").append(this.gain_time).append("', ");
strBuf.append("'").append(this.expire_time).append("', ");
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

public int getTreasureId() {
return this.treasure_id;
} public void setTreasureId(int treasure_id) {
if (treasure_id == this.treasure_id)
return; 
this.treasure_id = treasure_id;
}
public void saveTreasureId(int treasure_id) {
if (treasure_id == this.treasure_id)
return; 
this.treasure_id = treasure_id;
saveField("treasure_id", Integer.valueOf(treasure_id));
}

public int getGainTime() {
return this.gain_time;
} public void setGainTime(int gain_time) {
if (gain_time == this.gain_time)
return; 
this.gain_time = gain_time;
}
public void saveGainTime(int gain_time) {
if (gain_time == this.gain_time)
return; 
this.gain_time = gain_time;
saveField("gain_time", Integer.valueOf(gain_time));
}

public int getExpireTime() {
return this.expire_time;
} public void setExpireTime(int expire_time) {
if (expire_time == this.expire_time)
return; 
this.expire_time = expire_time;
}
public void saveExpireTime(int expire_time) {
if (expire_time == this.expire_time)
return; 
this.expire_time = expire_time;
saveField("expire_time", Integer.valueOf(expire_time));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `treasure_id` = '").append(this.treasure_id).append("',");
sBuilder.append(" `gain_time` = '").append(this.gain_time).append("',");
sBuilder.append(" `expire_time` = '").append(this.expire_time).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `droiyan_treasure` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`treasure_id` int(11) NOT NULL DEFAULT '0' COMMENT '获得密保',`gain_time` int(11) NOT NULL DEFAULT '0' COMMENT '发生时间',`expire_time` int(11) NOT NULL DEFAULT '0' COMMENT '过期时间',PRIMARY KEY (`id`)) COMMENT='决战记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

