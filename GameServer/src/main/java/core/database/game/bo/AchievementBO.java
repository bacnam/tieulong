package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AchievementBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*玩家ID")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "achieveId", comment = "成就ID")
private int achieveId;
@DataBaseField(type = "int(11)", fieldname = "completeCount", comment = "完成次数")
private int completeCount;
@DataBaseField(type = "int(11)", fieldname = "achieveCount", comment = "达成成就数量(系列成就)")
private int achieveCount;
@DataBaseField(type = "varchar(500)", fieldname = "gainPrizeList", comment = "领取列表")
private String gainPrizeList;
@DataBaseField(type = "bigint(20)", fieldname = "argument", comment = "附加参数")
private long argument;
@DataBaseField(type = "bigint(20)", fieldname = "argument2", comment = "附加参数2")
private long argument2;
@DataBaseField(type = "bigint(20)", fieldname = "argument3", comment = "附加参数3")
private long argument3;

public AchievementBO() {
this.id = 0L;
this.pid = 0L;
this.achieveId = 0;
this.completeCount = 0;
this.achieveCount = 0;
this.gainPrizeList = "";
this.argument = 0L;
this.argument2 = 0L;
this.argument3 = 0L;
}

public AchievementBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.achieveId = rs.getInt(3);
this.completeCount = rs.getInt(4);
this.achieveCount = rs.getInt(5);
this.gainPrizeList = rs.getString(6);
this.argument = rs.getLong(7);
this.argument2 = rs.getLong(8);
this.argument3 = rs.getLong(9);
}

public void getFromResultSet(ResultSet rs, List<AchievementBO> list) throws Exception {
list.add(new AchievementBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `achieveId`, `completeCount`, `achieveCount`, `gainPrizeList`, `argument`, `argument2`, `argument3`";
}

public String getTableName() {
return "`achievement`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.achieveId).append("', ");
strBuf.append("'").append(this.completeCount).append("', ");
strBuf.append("'").append(this.achieveCount).append("', ");
strBuf.append("'").append((this.gainPrizeList == null) ? null : this.gainPrizeList.replace("'", "''")).append("', ");
strBuf.append("'").append(this.argument).append("', ");
strBuf.append("'").append(this.argument2).append("', ");
strBuf.append("'").append(this.argument3).append("', ");
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

public int getAchieveId() {
return this.achieveId;
} public void setAchieveId(int achieveId) {
if (achieveId == this.achieveId)
return; 
this.achieveId = achieveId;
}
public void saveAchieveId(int achieveId) {
if (achieveId == this.achieveId)
return; 
this.achieveId = achieveId;
saveField("achieveId", Integer.valueOf(achieveId));
}

public int getCompleteCount() {
return this.completeCount;
} public void setCompleteCount(int completeCount) {
if (completeCount == this.completeCount)
return; 
this.completeCount = completeCount;
}
public void saveCompleteCount(int completeCount) {
if (completeCount == this.completeCount)
return; 
this.completeCount = completeCount;
saveField("completeCount", Integer.valueOf(completeCount));
}

public int getAchieveCount() {
return this.achieveCount;
} public void setAchieveCount(int achieveCount) {
if (achieveCount == this.achieveCount)
return; 
this.achieveCount = achieveCount;
}
public void saveAchieveCount(int achieveCount) {
if (achieveCount == this.achieveCount)
return; 
this.achieveCount = achieveCount;
saveField("achieveCount", Integer.valueOf(achieveCount));
}

public String getGainPrizeList() {
return this.gainPrizeList;
} public void setGainPrizeList(String gainPrizeList) {
if (gainPrizeList.equals(this.gainPrizeList))
return; 
this.gainPrizeList = gainPrizeList;
}
public void saveGainPrizeList(String gainPrizeList) {
if (gainPrizeList.equals(this.gainPrizeList))
return; 
this.gainPrizeList = gainPrizeList;
saveField("gainPrizeList", gainPrizeList);
}

public long getArgument() {
return this.argument;
} public void setArgument(long argument) {
if (argument == this.argument)
return; 
this.argument = argument;
}
public void saveArgument(long argument) {
if (argument == this.argument)
return; 
this.argument = argument;
saveField("argument", Long.valueOf(argument));
}

public long getArgument2() {
return this.argument2;
} public void setArgument2(long argument2) {
if (argument2 == this.argument2)
return; 
this.argument2 = argument2;
}
public void saveArgument2(long argument2) {
if (argument2 == this.argument2)
return; 
this.argument2 = argument2;
saveField("argument2", Long.valueOf(argument2));
}

public long getArgument3() {
return this.argument3;
} public void setArgument3(long argument3) {
if (argument3 == this.argument3)
return; 
this.argument3 = argument3;
}
public void saveArgument3(long argument3) {
if (argument3 == this.argument3)
return; 
this.argument3 = argument3;
saveField("argument3", Long.valueOf(argument3));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `achieveId` = '").append(this.achieveId).append("',");
sBuilder.append(" `completeCount` = '").append(this.completeCount).append("',");
sBuilder.append(" `achieveCount` = '").append(this.achieveCount).append("',");
sBuilder.append(" `gainPrizeList` = '").append((this.gainPrizeList == null) ? null : this.gainPrizeList.replace("'", "''")).append("',");
sBuilder.append(" `argument` = '").append(this.argument).append("',");
sBuilder.append(" `argument2` = '").append(this.argument2).append("',");
sBuilder.append(" `argument3` = '").append(this.argument3).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `achievement` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*玩家ID',`achieveId` int(11) NOT NULL DEFAULT '0' COMMENT '成就ID',`completeCount` int(11) NOT NULL DEFAULT '0' COMMENT '完成次数',`achieveCount` int(11) NOT NULL DEFAULT '0' COMMENT '达成成就数量(系列成就)',`gainPrizeList` varchar(500) NOT NULL DEFAULT '' COMMENT '领取列表',`argument` bigint(20) NOT NULL DEFAULT '0' COMMENT '附加参数',`argument2` bigint(20) NOT NULL DEFAULT '0' COMMENT '附加参数2',`argument3` bigint(20) NOT NULL DEFAULT '0' COMMENT '附加参数3',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='成就信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

