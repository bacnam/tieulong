package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FixedTimeMailBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "mailId", comment = "php邮件ID")
private long mailId;
@DataBaseField(type = "varchar(500)", fieldname = "senderName", comment = "发送者")
private String senderName;
@DataBaseField(type = "varchar(500)", fieldname = "title", comment = "邮件标题")
private String title;
@DataBaseField(type = "varchar(500)", fieldname = "content", comment = "邮件描述")
private String content;
@DataBaseField(type = "varchar(500)", fieldname = "uniformIDList", comment = "物品Id列表")
private String uniformIDList;
@DataBaseField(type = "varchar(500)", fieldname = "uniformCountList", comment = "物品数量列表")
private String uniformCountList;
@DataBaseField(type = "int(11)", fieldname = "beginTime", comment = "开始时间")
private int beginTime;
@DataBaseField(type = "int(11)", fieldname = "hasSendNum", comment = "已发次数")
private int hasSendNum;
@DataBaseField(type = "int(11)", fieldname = "cyclesNum", comment = "循环次数")
private int cyclesNum;

public FixedTimeMailBO() {
this.id = 0L;
this.mailId = 0L;
this.senderName = "";
this.title = "";
this.content = "";
this.uniformIDList = "";
this.uniformCountList = "";
this.beginTime = 0;
this.hasSendNum = 0;
this.cyclesNum = 0;
}

public FixedTimeMailBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.mailId = rs.getLong(2);
this.senderName = rs.getString(3);
this.title = rs.getString(4);
this.content = rs.getString(5);
this.uniformIDList = rs.getString(6);
this.uniformCountList = rs.getString(7);
this.beginTime = rs.getInt(8);
this.hasSendNum = rs.getInt(9);
this.cyclesNum = rs.getInt(10);
}

public void getFromResultSet(ResultSet rs, List<FixedTimeMailBO> list) throws Exception {
list.add(new FixedTimeMailBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `mailId`, `senderName`, `title`, `content`, `uniformIDList`, `uniformCountList`, `beginTime`, `hasSendNum`, `cyclesNum`";
}

public String getTableName() {
return "`fixedTimeMail`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.mailId).append("', ");
strBuf.append("'").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("', ");
strBuf.append("'").append((this.title == null) ? null : this.title.replace("'", "''")).append("', ");
strBuf.append("'").append((this.content == null) ? null : this.content.replace("'", "''")).append("', ");
strBuf.append("'").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("', ");
strBuf.append("'").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("', ");
strBuf.append("'").append(this.beginTime).append("', ");
strBuf.append("'").append(this.hasSendNum).append("', ");
strBuf.append("'").append(this.cyclesNum).append("', ");
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

public long getMailId() {
return this.mailId;
} public void setMailId(long mailId) {
if (mailId == this.mailId)
return; 
this.mailId = mailId;
}
public void saveMailId(long mailId) {
if (mailId == this.mailId)
return; 
this.mailId = mailId;
saveField("mailId", Long.valueOf(mailId));
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

public int getBeginTime() {
return this.beginTime;
} public void setBeginTime(int beginTime) {
if (beginTime == this.beginTime)
return; 
this.beginTime = beginTime;
}
public void saveBeginTime(int beginTime) {
if (beginTime == this.beginTime)
return; 
this.beginTime = beginTime;
saveField("beginTime", Integer.valueOf(beginTime));
}

public int getHasSendNum() {
return this.hasSendNum;
} public void setHasSendNum(int hasSendNum) {
if (hasSendNum == this.hasSendNum)
return; 
this.hasSendNum = hasSendNum;
}
public void saveHasSendNum(int hasSendNum) {
if (hasSendNum == this.hasSendNum)
return; 
this.hasSendNum = hasSendNum;
saveField("hasSendNum", Integer.valueOf(hasSendNum));
}

public int getCyclesNum() {
return this.cyclesNum;
} public void setCyclesNum(int cyclesNum) {
if (cyclesNum == this.cyclesNum)
return; 
this.cyclesNum = cyclesNum;
}
public void saveCyclesNum(int cyclesNum) {
if (cyclesNum == this.cyclesNum)
return; 
this.cyclesNum = cyclesNum;
saveField("cyclesNum", Integer.valueOf(cyclesNum));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `mailId` = '").append(this.mailId).append("',");
sBuilder.append(" `senderName` = '").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("',");
sBuilder.append(" `title` = '").append((this.title == null) ? null : this.title.replace("'", "''")).append("',");
sBuilder.append(" `content` = '").append((this.content == null) ? null : this.content.replace("'", "''")).append("',");
sBuilder.append(" `uniformIDList` = '").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("',");
sBuilder.append(" `uniformCountList` = '").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("',");
sBuilder.append(" `beginTime` = '").append(this.beginTime).append("',");
sBuilder.append(" `hasSendNum` = '").append(this.hasSendNum).append("',");
sBuilder.append(" `cyclesNum` = '").append(this.cyclesNum).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `fixedTimeMail` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`mailId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'php邮件ID',`senderName` varchar(500) NOT NULL DEFAULT '' COMMENT '发送者',`title` varchar(500) NOT NULL DEFAULT '' COMMENT '邮件标题',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '邮件描述',`uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品Id列表',`uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品数量列表',`beginTime` int(11) NOT NULL DEFAULT '0' COMMENT '开始时间',`hasSendNum` int(11) NOT NULL DEFAULT '0' COMMENT '已发次数',`cyclesNum` int(11) NOT NULL DEFAULT '0' COMMENT '循环次数',PRIMARY KEY (`id`)) COMMENT='后台推送定时邮件'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

