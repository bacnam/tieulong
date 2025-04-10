package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StealGoldBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "times", comment = "偷取次数")
private int times;
@DataBaseField(type = "bigint(20)", size = 4, fieldname = "fighters", comment = "路人")
private List<Long> fighters;

public StealGoldBO() {
this.id = 0L;
this.pid = 0L;
this.times = 0;
this.fighters = new ArrayList<>(4);
for (int i = 0; i < 4; i++) {
this.fighters.add(Long.valueOf(0L));
}
}

public StealGoldBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.times = rs.getInt(3);
this.fighters = new ArrayList<>(4);
for (int i = 0; i < 4; i++) {
this.fighters.add(Long.valueOf(rs.getLong(i + 4)));
}
}

public void getFromResultSet(ResultSet rs, List<StealGoldBO> list) throws Exception {
list.add(new StealGoldBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `times`, `fighters_0`, `fighters_1`, `fighters_2`, `fighters_3`";
}

public String getTableName() {
return "`steal_gold`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.times).append("', ");
for (int i = 0; i < this.fighters.size(); i++) {
strBuf.append("'").append(this.fighters.get(i)).append("', ");
}
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

public int getFightersSize() {
return this.fighters.size();
} public List<Long> getFightersAll() { return new ArrayList<>(this.fighters); }
public void setFightersAll(long value) { for (int i = 0; i < this.fighters.size(); ) { this.fighters.set(i, Long.valueOf(value)); i++; }
} public void saveFightersAll(long value) { setFightersAll(value); saveAll(); } public long getFighters(int index) {
return ((Long)this.fighters.get(index)).longValue();
} public void setFighters(int index, long value) {
if (value == ((Long)this.fighters.get(index)).longValue())
return; 
this.fighters.set(index, Long.valueOf(value));
}
public void saveFighters(int index, long value) {
if (value == ((Long)this.fighters.get(index)).longValue())
return; 
this.fighters.set(index, Long.valueOf(value));
saveField("fighters_" + index, this.fighters.get(index));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `times` = '").append(this.times).append("',");
for (int i = 0; i < this.fighters.size(); i++) {
sBuilder.append(" `fighters_").append(i).append("` = '").append(this.fighters.get(i)).append("',");
}
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `steal_gold` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`times` int(11) NOT NULL DEFAULT '0' COMMENT '偷取次数',`fighters_0` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_1` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_2` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_3` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',PRIMARY KEY (`id`)) COMMENT='探金手系统'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

