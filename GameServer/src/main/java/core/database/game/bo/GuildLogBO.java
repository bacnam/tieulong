package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildLogBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "帮会id")
private long guildId;
@DataBaseField(type = "int(11)", fieldname = "type", comment = "日志类型")
private int type;
@DataBaseField(type = "varchar(50)", size = 5, fieldname = "param", comment = "参数")
private List<String> param;
@DataBaseField(type = "int(11)", fieldname = "time", comment = "日志时间")
private int time;

public GuildLogBO() {
this.id = 0L;
this.guildId = 0L;
this.type = 0;
this.param = new ArrayList<>(5);
for (int i = 0; i < 5; i++) {
this.param.add("");
}
this.time = 0;
}

public GuildLogBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.guildId = rs.getLong(2);
this.type = rs.getInt(3);
this.param = new ArrayList<>(5);
for (int i = 0; i < 5; i++) {
this.param.add(rs.getString(i + 4));
}
this.time = rs.getInt(9);
}

public void getFromResultSet(ResultSet rs, List<GuildLogBO> list) throws Exception {
list.add(new GuildLogBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `guildId`, `type`, `param_0`, `param_1`, `param_2`, `param_3`, `param_4`, `time`";
}

public String getTableName() {
return "`guildLog`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.guildId).append("', ");
strBuf.append("'").append(this.type).append("', ");
for (int i = 0; i < this.param.size(); i++) {
strBuf.append("'").append((this.param.get(i) == null) ? null : ((String)this.param.get(i)).replace("'", "''")).append("', ");
}
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

public long getGuildId() {
return this.guildId;
} public void setGuildId(long guildId) {
if (guildId == this.guildId)
return; 
this.guildId = guildId;
}
public void saveGuildId(long guildId) {
if (guildId == this.guildId)
return; 
this.guildId = guildId;
saveField("guildId", Long.valueOf(guildId));
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

public int getParamSize() {
return this.param.size();
} public List<String> getParamAll() { return new ArrayList<>(this.param); }
public void setParamAll(String value) { for (int i = 0; i < this.param.size(); ) { this.param.set(i, value); i++; }
} public void saveParamAll(String value) { setParamAll(value); saveAll(); } public String getParam(int index) {
return this.param.get(index);
} public void setParam(int index, String value) {
if (value.equals(this.param.get(index)))
return; 
this.param.set(index, value);
}
public void saveParam(int index, String value) {
if (value.equals(this.param.get(index)))
return; 
this.param.set(index, value);
saveField("param_" + index, this.param.get(index));
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
sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
sBuilder.append(" `type` = '").append(this.type).append("',");
for (int i = 0; i < this.param.size(); i++) {
sBuilder.append(" `param_").append(i).append("` = '").append((this.param == null) ? null : ((String)this.param.get(i)).replace("'", "''")).append("',");
}
sBuilder.append(" `time` = '").append(this.time).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `guildLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮会id',`type` int(11) NOT NULL DEFAULT '0' COMMENT '日志类型',`param_0` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`param_1` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`param_2` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`param_3` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`param_4` varchar(50) NOT NULL DEFAULT '' COMMENT '参数',`time` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间',PRIMARY KEY (`id`)) COMMENT='竞技场信息表,记录每个玩家竞技场相关信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

