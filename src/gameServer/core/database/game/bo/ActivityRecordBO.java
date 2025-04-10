package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ActivityRecordBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家pid")
private long pid;
@DataBaseField(type = "bigint(20)", fieldname = "aid", comment = "对应活动主键Id")
private long aid;
@DataBaseField(type = "varchar(500)", fieldname = "activity", comment = "所属活动")
private String activity;
@DataBaseField(type = "int(11)", size = 5, fieldname = "extInt", comment = "int型扩展字段")
private List<Integer> extInt;
@DataBaseField(type = "varchar(500)", size = 4, fieldname = "extStr", comment = "string型扩展字段")
private List<String> extStr;

public ActivityRecordBO() {
this.id = 0L;
this.pid = 0L;
this.aid = 0L;
this.activity = "";
this.extInt = new ArrayList<>(5); int i;
for (i = 0; i < 5; i++) {
this.extInt.add(Integer.valueOf(0));
}
this.extStr = new ArrayList<>(4);
for (i = 0; i < 4; i++) {
this.extStr.add("");
}
}

public ActivityRecordBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.aid = rs.getLong(3);
this.activity = rs.getString(4);
this.extInt = new ArrayList<>(5); int i;
for (i = 0; i < 5; i++) {
this.extInt.add(Integer.valueOf(rs.getInt(i + 5)));
}
this.extStr = new ArrayList<>(4);
for (i = 0; i < 4; i++) {
this.extStr.add(rs.getString(i + 10));
}
}

public void getFromResultSet(ResultSet rs, List<ActivityRecordBO> list) throws Exception {
list.add(new ActivityRecordBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `pid`, `aid`, `activity`, `extInt_0`, `extInt_1`, `extInt_2`, `extInt_3`, `extInt_4`, `extStr_0`, `extStr_1`, `extStr_2`, `extStr_3`";
}

public String getTableName() {
return "`activityRecord`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.aid).append("', ");
strBuf.append("'").append((this.activity == null) ? null : this.activity.replace("'", "''")).append("', "); int i;
for (i = 0; i < this.extInt.size(); i++) {
strBuf.append("'").append(this.extInt.get(i)).append("', ");
}
for (i = 0; i < this.extStr.size(); i++) {
strBuf.append("'").append((this.extStr.get(i) == null) ? null : ((String)this.extStr.get(i)).replace("'", "''")).append("', ");
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

public long getAid() {
return this.aid;
} public void setAid(long aid) {
if (aid == this.aid)
return; 
this.aid = aid;
}
public void saveAid(long aid) {
if (aid == this.aid)
return; 
this.aid = aid;
saveField("aid", Long.valueOf(aid));
}

public String getActivity() {
return this.activity;
} public void setActivity(String activity) {
if (activity.equals(this.activity))
return; 
this.activity = activity;
}
public void saveActivity(String activity) {
if (activity.equals(this.activity))
return; 
this.activity = activity;
saveField("activity", activity);
}

public int getExtIntSize() {
return this.extInt.size();
} public List<Integer> getExtIntAll() { return new ArrayList<>(this.extInt); }
public void setExtIntAll(int value) { for (int i = 0; i < this.extInt.size(); ) { this.extInt.set(i, Integer.valueOf(value)); i++; }
} public void saveExtIntAll(int value) { setExtIntAll(value); saveAll(); } public int getExtInt(int index) {
return ((Integer)this.extInt.get(index)).intValue();
} public void setExtInt(int index, int value) {
if (value == ((Integer)this.extInt.get(index)).intValue())
return; 
this.extInt.set(index, Integer.valueOf(value));
}
public void saveExtInt(int index, int value) {
if (value == ((Integer)this.extInt.get(index)).intValue())
return; 
this.extInt.set(index, Integer.valueOf(value));
saveField("extInt_" + index, this.extInt.get(index));
}

public int getExtStrSize() {
return this.extStr.size();
} public List<String> getExtStrAll() { return new ArrayList<>(this.extStr); }
public void setExtStrAll(String value) { for (int i = 0; i < this.extStr.size(); ) { this.extStr.set(i, value); i++; }
} public void saveExtStrAll(String value) { setExtStrAll(value); saveAll(); } public String getExtStr(int index) {
return this.extStr.get(index);
} public void setExtStr(int index, String value) {
if (value.equals(this.extStr.get(index)))
return; 
this.extStr.set(index, value);
}
public void saveExtStr(int index, String value) {
if (value.equals(this.extStr.get(index)))
return; 
this.extStr.set(index, value);
saveField("extStr_" + index, this.extStr.get(index));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `aid` = '").append(this.aid).append("',");
sBuilder.append(" `activity` = '").append((this.activity == null) ? null : this.activity.replace("'", "''")).append("',"); int i;
for (i = 0; i < this.extInt.size(); i++) {
sBuilder.append(" `extInt_").append(i).append("` = '").append(this.extInt.get(i)).append("',");
}
for (i = 0; i < this.extStr.size(); i++) {
sBuilder.append(" `extStr_").append(i).append("` = '").append((this.extStr == null) ? null : ((String)this.extStr.get(i)).replace("'", "''")).append("',");
}
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `activityRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家pid',`aid` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应活动主键Id',`activity` varchar(500) NOT NULL DEFAULT '' COMMENT '所属活动',`extInt_0` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extInt_1` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extInt_2` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extInt_3` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extInt_4` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extStr_0` varchar(500) NOT NULL DEFAULT '' COMMENT 'string型扩展字段',`extStr_1` varchar(500) NOT NULL DEFAULT '' COMMENT 'string型扩展字段',`extStr_2` varchar(500) NOT NULL DEFAULT '' COMMENT 'string型扩展字段',`extStr_3` varchar(500) NOT NULL DEFAULT '' COMMENT 'string型扩展字段',PRIMARY KEY (`id`)) COMMENT='活动参与记录信息，记录每个玩家参加活动的参与记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

