package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientguideBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "groupId", comment = "组id")
private int groupId;
@DataBaseField(type = "varchar(2000)", fieldname = "finishList", comment = "完成的引导组id，建议采用组id列表的序号按位存放，使用long值存储。该字段由客户端使用与解析")
private String finishList;
@DataBaseField(type = "varchar(2000)", fieldname = "triggerList", comment = "完成的引导组id，建议采用组id列表的序号按位存放，使用long值存储。该字段由客户端使用与解析")
private String triggerList;

public ClientguideBO() {
this.id = 0L;
this.pid = 0L;
this.groupId = 0;
this.finishList = "";
this.triggerList = "";
}

public ClientguideBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.groupId = rs.getInt(3);
this.finishList = rs.getString(4);
this.triggerList = rs.getString(5);
}

public void getFromResultSet(ResultSet rs, List<ClientguideBO> list) throws Exception {
list.add(new ClientguideBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `groupId`, `finishList`, `triggerList`";
}

public String getTableName() {
return "`clientguide`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.groupId).append("', ");
strBuf.append("'").append((this.finishList == null) ? null : this.finishList.replace("'", "''")).append("', ");
strBuf.append("'").append((this.triggerList == null) ? null : this.triggerList.replace("'", "''")).append("', ");
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

public int getGroupId() {
return this.groupId;
} public void setGroupId(int groupId) {
if (groupId == this.groupId)
return; 
this.groupId = groupId;
}
public void saveGroupId(int groupId) {
if (groupId == this.groupId)
return; 
this.groupId = groupId;
saveField("groupId", Integer.valueOf(groupId));
}

public String getFinishList() {
return this.finishList;
} public void setFinishList(String finishList) {
if (finishList.equals(this.finishList))
return; 
this.finishList = finishList;
}
public void saveFinishList(String finishList) {
if (finishList.equals(this.finishList))
return; 
this.finishList = finishList;
saveField("finishList", finishList);
}

public String getTriggerList() {
return this.triggerList;
} public void setTriggerList(String triggerList) {
if (triggerList.equals(this.triggerList))
return; 
this.triggerList = triggerList;
}
public void saveTriggerList(String triggerList) {
if (triggerList.equals(this.triggerList))
return; 
this.triggerList = triggerList;
saveField("triggerList", triggerList);
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `groupId` = '").append(this.groupId).append("',");
sBuilder.append(" `finishList` = '").append((this.finishList == null) ? null : this.finishList.replace("'", "''")).append("',");
sBuilder.append(" `triggerList` = '").append((this.triggerList == null) ? null : this.triggerList.replace("'", "''")).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `clientguide` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`groupId` int(11) NOT NULL DEFAULT '0' COMMENT '组id',`finishList` varchar(2000) NOT NULL DEFAULT '' COMMENT '完成的引导组id，建议采用组id列表的序号按位存放，使用long值存储。该字段由客户端使用与解析',`triggerList` varchar(2000) NOT NULL DEFAULT '' COMMENT '完成的引导组id，建议采用组id列表的序号按位存放，使用long值存储。该字段由客户端使用与解析',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='新手引导数据存储表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

