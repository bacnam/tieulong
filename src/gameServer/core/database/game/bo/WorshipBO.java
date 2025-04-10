package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorshipBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
private long pid;
@DataBaseField(type = "int(11)", size = 20, fieldname = "worshipTimes", comment = "膜拜次数")
private List<Integer> worshipTimes;
@DataBaseField(type = "int(11)", size = 20, fieldname = "beWorshipTimes", comment = "被膜拜次数")
private List<Integer> beWorshipTimes;

public WorshipBO() {
this.id = 0L;
this.pid = 0L;
this.worshipTimes = new ArrayList<>(20); int i;
for (i = 0; i < 20; i++) {
this.worshipTimes.add(Integer.valueOf(0));
}
this.beWorshipTimes = new ArrayList<>(20);
for (i = 0; i < 20; i++) {
this.beWorshipTimes.add(Integer.valueOf(0));
}
}

public WorshipBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.worshipTimes = new ArrayList<>(20); int i;
for (i = 0; i < 20; i++) {
this.worshipTimes.add(Integer.valueOf(rs.getInt(i + 3)));
}
this.beWorshipTimes = new ArrayList<>(20);
for (i = 0; i < 20; i++) {
this.beWorshipTimes.add(Integer.valueOf(rs.getInt(i + 23)));
}
}

public void getFromResultSet(ResultSet rs, List<WorshipBO> list) throws Exception {
list.add(new WorshipBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `worshipTimes_0`, `worshipTimes_1`, `worshipTimes_2`, `worshipTimes_3`, `worshipTimes_4`, `worshipTimes_5`, `worshipTimes_6`, `worshipTimes_7`, `worshipTimes_8`, `worshipTimes_9`, `worshipTimes_10`, `worshipTimes_11`, `worshipTimes_12`, `worshipTimes_13`, `worshipTimes_14`, `worshipTimes_15`, `worshipTimes_16`, `worshipTimes_17`, `worshipTimes_18`, `worshipTimes_19`, `beWorshipTimes_0`, `beWorshipTimes_1`, `beWorshipTimes_2`, `beWorshipTimes_3`, `beWorshipTimes_4`, `beWorshipTimes_5`, `beWorshipTimes_6`, `beWorshipTimes_7`, `beWorshipTimes_8`, `beWorshipTimes_9`, `beWorshipTimes_10`, `beWorshipTimes_11`, `beWorshipTimes_12`, `beWorshipTimes_13`, `beWorshipTimes_14`, `beWorshipTimes_15`, `beWorshipTimes_16`, `beWorshipTimes_17`, `beWorshipTimes_18`, `beWorshipTimes_19`";
}

public String getTableName() {
return "`worship`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', "); int i;
for (i = 0; i < this.worshipTimes.size(); i++) {
strBuf.append("'").append(this.worshipTimes.get(i)).append("', ");
}
for (i = 0; i < this.beWorshipTimes.size(); i++) {
strBuf.append("'").append(this.beWorshipTimes.get(i)).append("', ");
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

public int getWorshipTimesSize() {
return this.worshipTimes.size();
} public List<Integer> getWorshipTimesAll() { return new ArrayList<>(this.worshipTimes); }
public void setWorshipTimesAll(int value) { for (int i = 0; i < this.worshipTimes.size(); ) { this.worshipTimes.set(i, Integer.valueOf(value)); i++; }
} public void saveWorshipTimesAll(int value) { setWorshipTimesAll(value); saveAll(); } public int getWorshipTimes(int index) {
return ((Integer)this.worshipTimes.get(index)).intValue();
} public void setWorshipTimes(int index, int value) {
if (value == ((Integer)this.worshipTimes.get(index)).intValue())
return; 
this.worshipTimes.set(index, Integer.valueOf(value));
}
public void saveWorshipTimes(int index, int value) {
if (value == ((Integer)this.worshipTimes.get(index)).intValue())
return; 
this.worshipTimes.set(index, Integer.valueOf(value));
saveField("worshipTimes_" + index, this.worshipTimes.get(index));
}

public int getBeWorshipTimesSize() {
return this.beWorshipTimes.size();
} public List<Integer> getBeWorshipTimesAll() { return new ArrayList<>(this.beWorshipTimes); }
public void setBeWorshipTimesAll(int value) { for (int i = 0; i < this.beWorshipTimes.size(); ) { this.beWorshipTimes.set(i, Integer.valueOf(value)); i++; }
} public void saveBeWorshipTimesAll(int value) { setBeWorshipTimesAll(value); saveAll(); } public int getBeWorshipTimes(int index) {
return ((Integer)this.beWorshipTimes.get(index)).intValue();
} public void setBeWorshipTimes(int index, int value) {
if (value == ((Integer)this.beWorshipTimes.get(index)).intValue())
return; 
this.beWorshipTimes.set(index, Integer.valueOf(value));
}
public void saveBeWorshipTimes(int index, int value) {
if (value == ((Integer)this.beWorshipTimes.get(index)).intValue())
return; 
this.beWorshipTimes.set(index, Integer.valueOf(value));
saveField("beWorshipTimes_" + index, this.beWorshipTimes.get(index));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',"); int i;
for (i = 0; i < this.worshipTimes.size(); i++) {
sBuilder.append(" `worshipTimes_").append(i).append("` = '").append(this.worshipTimes.get(i)).append("',");
}
for (i = 0; i < this.beWorshipTimes.size(); i++) {
sBuilder.append(" `beWorshipTimes_").append(i).append("` = '").append(this.beWorshipTimes.get(i)).append("',");
}
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `worship` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`worshipTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`beWorshipTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家膜拜信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

