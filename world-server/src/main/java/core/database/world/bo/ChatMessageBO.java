package core.database.world.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "int(11)",
        fieldname = "chatType",
        comment = "聊天频道[1、世界频道 2、公会频道 3、私聊频道 4、系统频道]"
    )
    private int chatType;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "senderPid",
        comment = "]"
    )
    private long senderPid;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "senderName",
        comment = "聊天频道[1、世界频道 2、公会频道 3、私聊频道 4、系统频道]"
    )
    private String senderName;
    @DataBaseField(
        type = "int(11)",
        fieldname = "senderLv",
        comment = "其余表示玩家pid[现在的玩家pid都是大数字]"
    )
    private int senderLv;
    @DataBaseField(
        type = "int(11)",
        fieldname = "senderVipLv",
        comment = "发送者CID"
    )
    private int senderVipLv;
    @DataBaseField(
        type = "int(11)",
        fieldname = "senderIcon",
        comment = "聊天内容"
    )
    private int senderIcon;
    @DataBaseField(
        type = "int(11)",
        fieldname = "senderServerId",
        comment = "发送者服务器Id"
    )
    private int senderServerId;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "message",
        comment = "发送时间"
    )
    private String message;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "content",
        comment = "发送时间"
    )
    private String content;
    @DataBaseField(
        type = "int(11)",
        fieldname = "sendTime",
        comment = "发送时间"
    )
    private int sendTime;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "receivePid",
        comment = "发送时间"
    )
    private long receivePid;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "receiveName",
        comment = "发送时间"
    )
    private String receiveName;
    @DataBaseField(
        type = "tinyint(1)",
        fieldname = "MonthCard",
        comment = "发送时间"
    )
    private boolean MonthCard;
    @DataBaseField(
        type = "tinyint(1)",
        fieldname = "YearCard",
        comment = "发送时间"
    )
    private boolean YearCard;
    @DataBaseField(
        type = "tinyint(1)",
        fieldname = "is_married",
        comment = "发送时间"
    )
    private boolean is_married;

    public ChatMessageBO() {
        this.id = 0L;
        this.chatType = 0;
        this.senderPid = 0L;
        this.senderName = "";
        this.senderLv = 0;
        this.senderVipLv = 0;
        this.senderIcon = 0;
        this.senderServerId = 0;
        this.message = "";
        this.content = "";
        this.sendTime = 0;
        this.receivePid = 0L;
        this.receiveName = "";
        this.MonthCard = false;
        this.YearCard = false;
        this.is_married = false;
    }

    public ChatMessageBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.chatType = rs.getInt(2);
        this.senderPid = rs.getLong(3);
        this.senderName = rs.getString(4);
        this.senderLv = rs.getInt(5);
        this.senderVipLv = rs.getInt(6);
        this.senderIcon = rs.getInt(7);
        this.senderServerId = rs.getInt(8);
        this.message = rs.getString(9);
        this.content = rs.getString(10);
        this.sendTime = rs.getInt(11);
        this.receivePid = rs.getLong(12);
        this.receiveName = rs.getString(13);
        this.MonthCard = rs.getBoolean(14);
        this.YearCard = rs.getBoolean(15);
        this.is_married = rs.getBoolean(16);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new ChatMessageBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return 0L;
    }

    @Override
    public String getItemsName() {
        return "`id`, `chatType`, `senderPid`, `senderName`, `senderLv`, `senderVipLv`, `senderIcon`, `senderServerId`, `message`, `content`, `sendTime`, `receivePid`, `receiveName`, `MonthCard`, `YearCard`, `is_married`";
    }

    @Override
    public String getTableName() {
        return "`chatMessage`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.chatType).append("', ");
        strBuf.append("'").append(this.senderPid).append("', ");
        strBuf.append("'").append(this.senderName == null ? null : this.senderName.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.senderLv).append("', ");
        strBuf.append("'").append(this.senderVipLv).append("', ");
        strBuf.append("'").append(this.senderIcon).append("', ");
        strBuf.append("'").append(this.senderServerId).append("', ");
        strBuf.append("'").append(this.message == null ? null : this.message.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.content == null ? null : this.content.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.sendTime).append("', ");
        strBuf.append("'").append(this.receivePid).append("', ");
        strBuf.append("'").append(this.receiveName == null ? null : this.receiveName.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.MonthCard ? 1 : 0).append("', ");
        strBuf.append("'").append(this.YearCard ? 1 : 0).append("', ");
        strBuf.append("'").append(this.is_married ? 1 : 0).append("', ");
        strBuf.deleteCharAt(strBuf.length() - 2);
        return strBuf.toString();
    }

    @Override
    public ArrayList<byte[]> getInsertValueBytes() {
        return new ArrayList<>();
    }

    @Override
    public void setId(long iID) {
        this.id = iID;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public int getChatType() {
        return this.chatType;
    }

    public void setChatType(int chatType) {
        if (chatType != this.chatType) {
            this.chatType = chatType;
        }
    }

    public void saveChatType(int chatType) {
        if (chatType != this.chatType) {
            this.chatType = chatType;
            this.saveField("chatType", chatType);
        }
    }

    public long getSenderPid() {
        return this.senderPid;
    }

    public void setSenderPid(long senderPid) {
        if (senderPid != this.senderPid) {
            this.senderPid = senderPid;
        }
    }

    public void saveSenderPid(long senderPid) {
        if (senderPid != this.senderPid) {
            this.senderPid = senderPid;
            this.saveField("senderPid", senderPid);
        }
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        if (!senderName.equals(this.senderName)) {
            this.senderName = senderName;
        }
    }

    public void saveSenderName(String senderName) {
        if (!senderName.equals(this.senderName)) {
            this.senderName = senderName;
            this.saveField("senderName", senderName);
        }
    }

    public int getSenderLv() {
        return this.senderLv;
    }

    public void setSenderLv(int senderLv) {
        if (senderLv != this.senderLv) {
            this.senderLv = senderLv;
        }
    }

    public void saveSenderLv(int senderLv) {
        if (senderLv != this.senderLv) {
            this.senderLv = senderLv;
            this.saveField("senderLv", senderLv);
        }
    }

    public int getSenderVipLv() {
        return this.senderVipLv;
    }

    public void setSenderVipLv(int senderVipLv) {
        if (senderVipLv != this.senderVipLv) {
            this.senderVipLv = senderVipLv;
        }
    }

    public void saveSenderVipLv(int senderVipLv) {
        if (senderVipLv != this.senderVipLv) {
            this.senderVipLv = senderVipLv;
            this.saveField("senderVipLv", senderVipLv);
        }
    }

    public int getSenderIcon() {
        return this.senderIcon;
    }

    public void setSenderIcon(int senderIcon) {
        if (senderIcon != this.senderIcon) {
            this.senderIcon = senderIcon;
        }
    }

    public void saveSenderIcon(int senderIcon) {
        if (senderIcon != this.senderIcon) {
            this.senderIcon = senderIcon;
            this.saveField("senderIcon", senderIcon);
        }
    }

    public int getSenderServerId() {
        return this.senderServerId;
    }

    public void setSenderServerId(int senderServerId) {
        if (senderServerId != this.senderServerId) {
            this.senderServerId = senderServerId;
        }
    }

    public void saveSenderServerId(int senderServerId) {
        if (senderServerId != this.senderServerId) {
            this.senderServerId = senderServerId;
            this.saveField("senderServerId", senderServerId);
        }
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        if (!message.equals(this.message)) {
            this.message = message;
        }
    }

    public void saveMessage(String message) {
        if (!message.equals(this.message)) {
            this.message = message;
            this.saveField("message", message);
        }
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        if (!content.equals(this.content)) {
            this.content = content;
        }
    }

    public void saveContent(String content) {
        if (!content.equals(this.content)) {
            this.content = content;
            this.saveField("content", content);
        }
    }

    public int getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(int sendTime) {
        if (sendTime != this.sendTime) {
            this.sendTime = sendTime;
        }
    }

    public void saveSendTime(int sendTime) {
        if (sendTime != this.sendTime) {
            this.sendTime = sendTime;
            this.saveField("sendTime", sendTime);
        }
    }

    public long getReceivePid() {
        return this.receivePid;
    }

    public void setReceivePid(long receivePid) {
        if (receivePid != this.receivePid) {
            this.receivePid = receivePid;
        }
    }

    public void saveReceivePid(long receivePid) {
        if (receivePid != this.receivePid) {
            this.receivePid = receivePid;
            this.saveField("receivePid", receivePid);
        }
    }

    public String getReceiveName() {
        return this.receiveName;
    }

    public void setReceiveName(String receiveName) {
        if (!receiveName.equals(this.receiveName)) {
            this.receiveName = receiveName;
        }
    }

    public void saveReceiveName(String receiveName) {
        if (!receiveName.equals(this.receiveName)) {
            this.receiveName = receiveName;
            this.saveField("receiveName", receiveName);
        }
    }

    public boolean getMonthCard() {
        return this.MonthCard;
    }

    public void setMonthCard(boolean MonthCard) {
        if (MonthCard != this.MonthCard) {
            this.MonthCard = MonthCard;
        }
    }

    public void saveMonthCard(boolean MonthCard) {
        if (MonthCard != this.MonthCard) {
            this.MonthCard = MonthCard;
            this.saveField("MonthCard", MonthCard ? 1 : 0);
        }
    }

    public boolean getYearCard() {
        return this.YearCard;
    }

    public void setYearCard(boolean YearCard) {
        if (YearCard != this.YearCard) {
            this.YearCard = YearCard;
        }
    }

    public void saveYearCard(boolean YearCard) {
        if (YearCard != this.YearCard) {
            this.YearCard = YearCard;
            this.saveField("YearCard", YearCard ? 1 : 0);
        }
    }

    public boolean getIsMarried() {
        return this.is_married;
    }

    public void setIsMarried(boolean is_married) {
        if (is_married != this.is_married) {
            this.is_married = is_married;
        }
    }

    public void saveIsMarried(boolean is_married) {
        if (is_married != this.is_married) {
            this.is_married = is_married;
            this.saveField("is_married", is_married ? 1 : 0);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `chatType` = '").append(this.chatType).append("',");
        sBuilder.append(" `senderPid` = '").append(this.senderPid).append("',");
        sBuilder.append(" `senderName` = '").append(this.senderName == null ? null : this.senderName.replace("'", "''")).append("',");
        sBuilder.append(" `senderLv` = '").append(this.senderLv).append("',");
        sBuilder.append(" `senderVipLv` = '").append(this.senderVipLv).append("',");
        sBuilder.append(" `senderIcon` = '").append(this.senderIcon).append("',");
        sBuilder.append(" `senderServerId` = '").append(this.senderServerId).append("',");
        sBuilder.append(" `message` = '").append(this.message == null ? null : this.message.replace("'", "''")).append("',");
        sBuilder.append(" `content` = '").append(this.content == null ? null : this.content.replace("'", "''")).append("',");
        sBuilder.append(" `sendTime` = '").append(this.sendTime).append("',");
        sBuilder.append(" `receivePid` = '").append(this.receivePid).append("',");
        sBuilder.append(" `receiveName` = '").append(this.receiveName == null ? null : this.receiveName.replace("'", "''")).append("',");
        sBuilder.append(" `MonthCard` = '").append(this.MonthCard ? 1 : 0).append("',");
        sBuilder.append(" `YearCard` = '").append(this.YearCard ? 1 : 0).append("',");
        sBuilder.append(" `is_married` = '").append(this.is_married ? 1 : 0).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `chatMessage` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`chatType` int(11) NOT NULL DEFAULT '0' COMMENT '聊天频道[1、世界频道 2、公会频道 3、私聊频道 4、系统频道]',`senderPid` bigint(20) NOT NULL DEFAULT '0' COMMENT ']',`senderName` varchar(500) NOT NULL DEFAULT '' COMMENT '聊天频道[1、世界频道 2、公会频道 3、私聊频道 4、系统频道]',`senderLv` int(11) NOT NULL DEFAULT '0' COMMENT '其余表示玩家pid[现在的玩家pid都是大数字]',`senderVipLv` int(11) NOT NULL DEFAULT '0' COMMENT '发送者CID',`senderIcon` int(11) NOT NULL DEFAULT '0' COMMENT '聊天内容',`senderServerId` int(11) NOT NULL DEFAULT '0' COMMENT '发送者服务器Id',`message` varchar(500) NOT NULL DEFAULT '' COMMENT '发送时间',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '发送时间',`sendTime` int(11) NOT NULL DEFAULT '0' COMMENT '发送时间',`receivePid` bigint(20) NOT NULL DEFAULT '0' COMMENT '发送时间',`receiveName` varchar(500) NOT NULL DEFAULT '' COMMENT '发送时间',`MonthCard` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发送时间',`YearCard` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发送时间',`is_married` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发送时间',PRIMARY KEY (`id`)) COMMENT='聊天信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
