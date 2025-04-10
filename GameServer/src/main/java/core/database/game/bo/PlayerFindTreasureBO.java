package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerFindTreasureBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "total", comment = "累计寻宝次数")
private int total;
@DataBaseField(type = "int(11)", fieldname = "times", comment = "每日单抽寻宝次数")
private int times;
@DataBaseField(type = "int(11)", fieldname = "tentimes", comment = "每日十连抽寻宝次数")
private int tentimes;
@DataBaseField(type = "int(11)", fieldname = "warspiritTotal", comment = "战灵累计寻宝次数")
private int warspiritTotal;

public PlayerFindTreasureBO() {
this.id = 0L;
this.pid = 0L;
this.total = 0;
this.times = 0;
this.tentimes = 0;
this.warspiritTotal = 0;
}

public PlayerFindTreasureBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.total = rs.getInt(3);
this.times = rs.getInt(4);
this.tentimes = rs.getInt(5);
this.warspiritTotal = rs.getInt(6);
}

public void getFromResultSet(ResultSet rs, List<PlayerFindTreasureBO> list) throws Exception {
list.add(new PlayerFindTreasureBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `total`, `times`, `tentimes`, `warspiritTotal`";
}

public String getTableName() {
return "`player_find_treasure`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.total).append("', ");
strBuf.append("'").append(this.times).append("', ");
strBuf.append("'").append(this.tentimes).append("', ");
strBuf.append("'").append(this.warspiritTotal).append("', ");
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

public int getTotal() {
return this.total;
} public void setTotal(int total) {
if (total == this.total)
return; 
this.total = total;
}
public void saveTotal(int total) {
if (total == this.total)
return; 
this.total = total;
saveField("total", Integer.valueOf(total));
}

public int getTimes() {
return this.times;
} public void setTimes(int times) {
if (times == this.times)
return; 
this.times = times;
}
public void saveTimes(int times) {
if (times == this.times)
return; 
this.times = times;
saveField("times", Integer.valueOf(times));
}

public int getTentimes() {
return this.tentimes;
} public void setTentimes(int tentimes) {
if (tentimes == this.tentimes)
return; 
this.tentimes = tentimes;
}
public void saveTentimes(int tentimes) {
if (tentimes == this.tentimes)
return; 
this.tentimes = tentimes;
saveField("tentimes", Integer.valueOf(tentimes));
}

public int getWarspiritTotal() {
return this.warspiritTotal;
} public void setWarspiritTotal(int warspiritTotal) {
if (warspiritTotal == this.warspiritTotal)
return; 
this.warspiritTotal = warspiritTotal;
}
public void saveWarspiritTotal(int warspiritTotal) {
if (warspiritTotal == this.warspiritTotal)
return; 
this.warspiritTotal = warspiritTotal;
saveField("warspiritTotal", Integer.valueOf(warspiritTotal));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `total` = '").append(this.total).append("',");
sBuilder.append(" `times` = '").append(this.times).append("',");
sBuilder.append(" `tentimes` = '").append(this.tentimes).append("',");
sBuilder.append(" `warspiritTotal` = '").append(this.warspiritTotal).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `player_find_treasure` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`total` int(11) NOT NULL DEFAULT '0' COMMENT '累计寻宝次数',`times` int(11) NOT NULL DEFAULT '0' COMMENT '每日单抽寻宝次数',`tentimes` int(11) NOT NULL DEFAULT '0' COMMENT '每日十连抽寻宝次数',`warspiritTotal` int(11) NOT NULL DEFAULT '0' COMMENT '战灵累计寻宝次数',PRIMARY KEY (`id`)) COMMENT='玩家寻宝信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

