package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerMailBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
private long pid;
@DataBaseField(type = "varchar(500)", fieldname = "senderName", comment = "发送者名称")
private String senderName;
@DataBaseField(type = "varchar(200)", fieldname = "title", comment = "邮件标题")
private String title;
@DataBaseField(type = "varchar(1000)", fieldname = "content", comment = "邮件描述")
private String content;
@DataBaseField(type = "int(11)", fieldname = "createTime", comment = "邮件发送时间(单位s)")
private int createTime;
@DataBaseField(type = "int(11)", fieldname = "existTime", comment = "邮件生存时间(s)")
private int existTime;
@DataBaseField(type = "int(11)", fieldname = "pickUpTime", comment = "读取领取时间(单位s)")
private int pickUpTime;
@DataBaseField(type = "int(11)", fieldname = "pickUpExistTime", comment = "读取领取后生存时间(单位s)")
private int pickUpExistTime;
@DataBaseField(type = "varchar(500)", fieldname = "uniformIDList", comment = "物品Id列表")
private String uniformIDList;
@DataBaseField(type = "varchar(500)", fieldname = "uniformCountList", comment = "物品数量列表")
private String uniformCountList;
@DataBaseField(type = "bigint(20)", fieldname = "globalMailID", comment = "全体邮件的ID")
private long globalMailID;

public PlayerMailBO() {
this.id = 0L;
this.pid = 0L;
this.senderName = "";
this.title = "";
this.content = "";
this.createTime = 0;
this.existTime = 0;
this.pickUpTime = 0;
this.pickUpExistTime = 0;
this.uniformIDList = "";
this.uniformCountList = "";
this.globalMailID = 0L;
}

public PlayerMailBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.senderName = rs.getString(3);
this.title = rs.getString(4);
this.content = rs.getString(5);
this.createTime = rs.getInt(6);
this.existTime = rs.getInt(7);
this.pickUpTime = rs.getInt(8);
this.pickUpExistTime = rs.getInt(9);
this.uniformIDList = rs.getString(10);
this.uniformCountList = rs.getString(11);
this.globalMailID = rs.getLong(12);
}

public void getFromResultSet(ResultSet rs, List<PlayerMailBO> list) throws Exception {
list.add(new PlayerMailBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `senderName`, `title`, `content`, `createTime`, `existTime`, `pickUpTime`, `pickUpExistTime`, `uniformIDList`, `uniformCountList`, `globalMailID`";
}

public String getTableName() {
return "`playerMail`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("', ");
strBuf.append("'").append((this.title == null) ? null : this.title.replace("'", "''")).append("', ");
strBuf.append("'").append((this.content == null) ? null : this.content.replace("'", "''")).append("', ");
strBuf.append("'").append(this.createTime).append("', ");
strBuf.append("'").append(this.existTime).append("', ");
strBuf.append("'").append(this.pickUpTime).append("', ");
strBuf.append("'").append(this.pickUpExistTime).append("', ");
strBuf.append("'").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("', ");
strBuf.append("'").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("', ");
strBuf.append("'").append(this.globalMailID).append("', ");
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

public String getSenderName() {
return this.senderName;
} public void setSenderName(String senderName) {
if (senderName.equals(this.senderName))
return; 
this.senderName = senderName;
}
public void saveSenderName(String senderName) {
if (senderName.equals(this.senderName))
return; 
this.senderName = senderName;
saveField("senderName", senderName);
}

public String getTitle() {
return this.title;
} public void setTitle(String title) {
if (title.equals(this.title))
return; 
this.title = title;
}
public void saveTitle(String title) {
if (title.equals(this.title))
return; 
this.title = title;
saveField("title", title);
}

public String getContent() {
return this.content;
} public void setContent(String content) {
if (content.equals(this.content))
return; 
this.content = content;
}
public void saveContent(String content) {
if (content.equals(this.content))
return; 
this.content = content;
saveField("content", content);
}

public int getCreateTime() {
return this.createTime;
} public void setCreateTime(int createTime) {
if (createTime == this.createTime)
return; 
this.createTime = createTime;
}
public void saveCreateTime(int createTime) {
if (createTime == this.createTime)
return; 
this.createTime = createTime;
saveField("createTime", Integer.valueOf(createTime));
}

public int getExistTime() {
return this.existTime;
} public void setExistTime(int existTime) {
if (existTime == this.existTime)
return; 
this.existTime = existTime;
}
public void saveExistTime(int existTime) {
if (existTime == this.existTime)
return; 
this.existTime = existTime;
saveField("existTime", Integer.valueOf(existTime));
}

public int getPickUpTime() {
return this.pickUpTime;
} public void setPickUpTime(int pickUpTime) {
if (pickUpTime == this.pickUpTime)
return; 
this.pickUpTime = pickUpTime;
}
public void savePickUpTime(int pickUpTime) {
if (pickUpTime == this.pickUpTime)
return; 
this.pickUpTime = pickUpTime;
saveField("pickUpTime", Integer.valueOf(pickUpTime));
}

public int getPickUpExistTime() {
return this.pickUpExistTime;
} public void setPickUpExistTime(int pickUpExistTime) {
if (pickUpExistTime == this.pickUpExistTime)
return; 
this.pickUpExistTime = pickUpExistTime;
}
public void savePickUpExistTime(int pickUpExistTime) {
if (pickUpExistTime == this.pickUpExistTime)
return; 
this.pickUpExistTime = pickUpExistTime;
saveField("pickUpExistTime", Integer.valueOf(pickUpExistTime));
}

public String getUniformIDList() {
return this.uniformIDList;
} public void setUniformIDList(String uniformIDList) {
if (uniformIDList.equals(this.uniformIDList))
return; 
this.uniformIDList = uniformIDList;
}
public void saveUniformIDList(String uniformIDList) {
if (uniformIDList.equals(this.uniformIDList))
return; 
this.uniformIDList = uniformIDList;
saveField("uniformIDList", uniformIDList);
}

public String getUniformCountList() {
return this.uniformCountList;
} public void setUniformCountList(String uniformCountList) {
if (uniformCountList.equals(this.uniformCountList))
return; 
this.uniformCountList = uniformCountList;
}
public void saveUniformCountList(String uniformCountList) {
if (uniformCountList.equals(this.uniformCountList))
return; 
this.uniformCountList = uniformCountList;
saveField("uniformCountList", uniformCountList);
}

public long getGlobalMailID() {
return this.globalMailID;
} public void setGlobalMailID(long globalMailID) {
if (globalMailID == this.globalMailID)
return; 
this.globalMailID = globalMailID;
}
public void saveGlobalMailID(long globalMailID) {
if (globalMailID == this.globalMailID)
return; 
this.globalMailID = globalMailID;
saveField("globalMailID", Long.valueOf(globalMailID));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `senderName` = '").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("',");
sBuilder.append(" `title` = '").append((this.title == null) ? null : this.title.replace("'", "''")).append("',");
sBuilder.append(" `content` = '").append((this.content == null) ? null : this.content.replace("'", "''")).append("',");
sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
sBuilder.append(" `existTime` = '").append(this.existTime).append("',");
sBuilder.append(" `pickUpTime` = '").append(this.pickUpTime).append("',");
sBuilder.append(" `pickUpExistTime` = '").append(this.pickUpExistTime).append("',");
sBuilder.append(" `uniformIDList` = '").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("',");
sBuilder.append(" `uniformCountList` = '").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("',");
sBuilder.append(" `globalMailID` = '").append(this.globalMailID).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `playerMail` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`senderName` varchar(500) NOT NULL DEFAULT '' COMMENT '发送者名称',`title` varchar(200) NOT NULL DEFAULT '' COMMENT '邮件标题',`content` varchar(1000) NOT NULL DEFAULT '' COMMENT '邮件描述',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '邮件发送时间(单位s)',`existTime` int(11) NOT NULL DEFAULT '0' COMMENT '邮件生存时间(s)',`pickUpTime` int(11) NOT NULL DEFAULT '0' COMMENT '读取领取时间(单位s)',`pickUpExistTime` int(11) NOT NULL DEFAULT '0' COMMENT '读取领取后生存时间(单位s)',`uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品Id列表',`uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品数量列表',`globalMailID` bigint(20) NOT NULL DEFAULT '0' COMMENT '全体邮件的ID',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='邮件信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

