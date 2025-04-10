package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RedPacketBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "left_money", comment = "剩余金额")
private int left_money;
@DataBaseField(type = "int(11)", fieldname = "max_money", comment = "总金额")
private int max_money;
@DataBaseField(type = "int(11)", fieldname = "already_pick", comment = "已领取人数")
private int already_pick;
@DataBaseField(type = "int(11)", fieldname = "max_pick", comment = "最大领取人数")
private int max_pick;
@DataBaseField(type = "int(11)", fieldname = "time", comment = "红包生成时间")
private int time;
@DataBaseField(type = "int(11)", fieldname = "packet_type_id", comment = "红包种类id")
private int packet_type_id;
@DataBaseField(type = "int(11)", fieldname = "status", comment = "领取状态")
private int status;

public RedPacketBO() {
this.id = 0L;
this.pid = 0L;
this.left_money = 0;
this.max_money = 0;
this.already_pick = 0;
this.max_pick = 0;
this.time = 0;
this.packet_type_id = 0;
this.status = 0;
}

public RedPacketBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.left_money = rs.getInt(3);
this.max_money = rs.getInt(4);
this.already_pick = rs.getInt(5);
this.max_pick = rs.getInt(6);
this.time = rs.getInt(7);
this.packet_type_id = rs.getInt(8);
this.status = rs.getInt(9);
}

public void getFromResultSet(ResultSet rs, List<RedPacketBO> list) throws Exception {
list.add(new RedPacketBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `left_money`, `max_money`, `already_pick`, `max_pick`, `time`, `packet_type_id`, `status`";
}

public String getTableName() {
return "`red_packet`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.left_money).append("', ");
strBuf.append("'").append(this.max_money).append("', ");
strBuf.append("'").append(this.already_pick).append("', ");
strBuf.append("'").append(this.max_pick).append("', ");
strBuf.append("'").append(this.time).append("', ");
strBuf.append("'").append(this.packet_type_id).append("', ");
strBuf.append("'").append(this.status).append("', ");
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

public int getLeftMoney() {
return this.left_money;
} public void setLeftMoney(int left_money) {
if (left_money == this.left_money)
return; 
this.left_money = left_money;
}
public void saveLeftMoney(int left_money) {
if (left_money == this.left_money)
return; 
this.left_money = left_money;
saveField("left_money", Integer.valueOf(left_money));
}

public int getMaxMoney() {
return this.max_money;
} public void setMaxMoney(int max_money) {
if (max_money == this.max_money)
return; 
this.max_money = max_money;
}
public void saveMaxMoney(int max_money) {
if (max_money == this.max_money)
return; 
this.max_money = max_money;
saveField("max_money", Integer.valueOf(max_money));
}

public int getAlreadyPick() {
return this.already_pick;
} public void setAlreadyPick(int already_pick) {
if (already_pick == this.already_pick)
return; 
this.already_pick = already_pick;
}
public void saveAlreadyPick(int already_pick) {
if (already_pick == this.already_pick)
return; 
this.already_pick = already_pick;
saveField("already_pick", Integer.valueOf(already_pick));
}

public int getMaxPick() {
return this.max_pick;
} public void setMaxPick(int max_pick) {
if (max_pick == this.max_pick)
return; 
this.max_pick = max_pick;
}
public void saveMaxPick(int max_pick) {
if (max_pick == this.max_pick)
return; 
this.max_pick = max_pick;
saveField("max_pick", Integer.valueOf(max_pick));
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

public int getPacketTypeId() {
return this.packet_type_id;
} public void setPacketTypeId(int packet_type_id) {
if (packet_type_id == this.packet_type_id)
return; 
this.packet_type_id = packet_type_id;
}
public void savePacketTypeId(int packet_type_id) {
if (packet_type_id == this.packet_type_id)
return; 
this.packet_type_id = packet_type_id;
saveField("packet_type_id", Integer.valueOf(packet_type_id));
}

public int getStatus() {
return this.status;
} public void setStatus(int status) {
if (status == this.status)
return; 
this.status = status;
}
public void saveStatus(int status) {
if (status == this.status)
return; 
this.status = status;
saveField("status", Integer.valueOf(status));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `left_money` = '").append(this.left_money).append("',");
sBuilder.append(" `max_money` = '").append(this.max_money).append("',");
sBuilder.append(" `already_pick` = '").append(this.already_pick).append("',");
sBuilder.append(" `max_pick` = '").append(this.max_pick).append("',");
sBuilder.append(" `time` = '").append(this.time).append("',");
sBuilder.append(" `packet_type_id` = '").append(this.packet_type_id).append("',");
sBuilder.append(" `status` = '").append(this.status).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `red_packet` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`left_money` int(11) NOT NULL DEFAULT '0' COMMENT '剩余金额',`max_money` int(11) NOT NULL DEFAULT '0' COMMENT '总金额',`already_pick` int(11) NOT NULL DEFAULT '0' COMMENT '已领取人数',`max_pick` int(11) NOT NULL DEFAULT '0' COMMENT '最大领取人数',`time` int(11) NOT NULL DEFAULT '0' COMMENT '红包生成时间',`packet_type_id` int(11) NOT NULL DEFAULT '0' COMMENT '红包种类id',`status` int(11) NOT NULL DEFAULT '0' COMMENT '领取状态',PRIMARY KEY (`id`)) COMMENT='红包表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

