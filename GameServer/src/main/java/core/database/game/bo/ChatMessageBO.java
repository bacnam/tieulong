package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "int(11)", fieldname = "chatType", comment = "聊天频道[1、世界频道 2、公会频道 3、私聊频道 4、系统频道]")
private int chatType;
@DataBaseField(type = "bigint(20)", fieldname = "receiveCId", comment = "其余表示玩家pid[现在的玩家pid都是大数字]")
private long receiveCId;
@DataBaseField(type = "bigint(20)", fieldname = "senderCId", comment = "发送者CID")
private long senderCId;
@DataBaseField(type = "varchar(500)", fieldname = "content", comment = "聊天内容")
private String content;
@DataBaseField(type = "int(11)", fieldname = "sendTime", comment = "发送时间")
private int sendTime;

public ChatMessageBO() {
this.id = 0L;
this.chatType = 0;
this.receiveCId = 0L;
this.senderCId = 0L;
this.content = "";
this.sendTime = 0;
}

public ChatMessageBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.chatType = rs.getInt(2);
this.receiveCId = rs.getLong(3);
this.senderCId = rs.getLong(4);
this.content = rs.getString(5);
this.sendTime = rs.getInt(6);
}

public void getFromResultSet(ResultSet rs, List<ChatMessageBO> list) throws Exception {
list.add(new ChatMessageBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `chatType`, `receiveCId`, `senderCId`, `content`, `sendTime`";
}

public String getTableName() {
return "`chatMessage`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.chatType).append("', ");
strBuf.append("'").append(this.receiveCId).append("', ");
strBuf.append("'").append(this.senderCId).append("', ");
strBuf.append("'").append((this.content == null) ? null : this.content.replace("'", "''")).append("', ");
strBuf.append("'").append(this.sendTime).append("', ");
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

public int getChatType() {
return this.chatType;
} public void setChatType(int chatType) {
if (chatType == this.chatType)
return; 
this.chatType = chatType;
}
public void saveChatType(int chatType) {
if (chatType == this.chatType)
return; 
this.chatType = chatType;
saveField("chatType", Integer.valueOf(chatType));
}

public long getReceiveCId() {
return this.receiveCId;
} public void setReceiveCId(long receiveCId) {
if (receiveCId == this.receiveCId)
return; 
this.receiveCId = receiveCId;
}
public void saveReceiveCId(long receiveCId) {
if (receiveCId == this.receiveCId)
return; 
this.receiveCId = receiveCId;
saveField("receiveCId", Long.valueOf(receiveCId));
}

public long getSenderCId() {
return this.senderCId;
} public void setSenderCId(long senderCId) {
if (senderCId == this.senderCId)
return; 
this.senderCId = senderCId;
}
public void saveSenderCId(long senderCId) {
if (senderCId == this.senderCId)
return; 
this.senderCId = senderCId;
saveField("senderCId", Long.valueOf(senderCId));
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

public int getSendTime() {
return this.sendTime;
} public void setSendTime(int sendTime) {
if (sendTime == this.sendTime)
return; 
this.sendTime = sendTime;
}
public void saveSendTime(int sendTime) {
if (sendTime == this.sendTime)
return; 
this.sendTime = sendTime;
saveField("sendTime", Integer.valueOf(sendTime));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `chatType` = '").append(this.chatType).append("',");
sBuilder.append(" `receiveCId` = '").append(this.receiveCId).append("',");
sBuilder.append(" `senderCId` = '").append(this.senderCId).append("',");
sBuilder.append(" `content` = '").append((this.content == null) ? null : this.content.replace("'", "''")).append("',");
sBuilder.append(" `sendTime` = '").append(this.sendTime).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `chatMessage` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`chatType` int(11) NOT NULL DEFAULT '0' COMMENT '聊天频道[1、世界频道 2、公会频道 3、私聊频道 4、系统频道]',`receiveCId` bigint(20) NOT NULL DEFAULT '0' COMMENT '其余表示玩家pid[现在的玩家pid都是大数字]',`senderCId` bigint(20) NOT NULL DEFAULT '0' COMMENT '发送者CID',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '聊天内容',`sendTime` int(11) NOT NULL DEFAULT '0' COMMENT '发送时间',PRIMARY KEY (`id`)) COMMENT='聊天信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

