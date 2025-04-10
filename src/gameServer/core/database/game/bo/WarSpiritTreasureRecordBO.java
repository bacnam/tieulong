package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WarSpiritTreasureRecordBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "spirit_id", comment = "战灵id")
private int spirit_id;
@DataBaseField(type = "int(11)", fieldname = "time", comment = "获得时间")
private int time;

public WarSpiritTreasureRecordBO() {
this.id = 0L;
this.pid = 0L;
this.spirit_id = 0;
this.time = 0;
}

public WarSpiritTreasureRecordBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.spirit_id = rs.getInt(3);
this.time = rs.getInt(4);
}

public void getFromResultSet(ResultSet rs, List<WarSpiritTreasureRecordBO> list) throws Exception {
list.add(new WarSpiritTreasureRecordBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `spirit_id`, `time`";
}

public String getTableName() {
return "`warSpiritTreasureRecord`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.spirit_id).append("', ");
strBuf.append("'").append(this.time).append("', ");
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

public int getSpiritId() {
return this.spirit_id;
} public void setSpiritId(int spirit_id) {
if (spirit_id == this.spirit_id)
return; 
this.spirit_id = spirit_id;
}
public void saveSpiritId(int spirit_id) {
if (spirit_id == this.spirit_id)
return; 
this.spirit_id = spirit_id;
saveField("spirit_id", Integer.valueOf(spirit_id));
}

public int getTime() {
return this.time;
} public void setTime(int time) {
if (time == this.time)
return; 
this.time = time;
}
public void saveTime(int time) {
if (time == this.time)
return; 
this.time = time;
saveField("time", Integer.valueOf(time));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `spirit_id` = '").append(this.spirit_id).append("',");
sBuilder.append(" `time` = '").append(this.time).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `warSpiritTreasureRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`spirit_id` int(11) NOT NULL DEFAULT '0' COMMENT '战灵id',`time` int(11) NOT NULL DEFAULT '0' COMMENT '获得时间',PRIMARY KEY (`id`)) COMMENT='战灵抽奖表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

