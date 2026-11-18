package core.logger.flow.impl.db;

import com.zhonglian.server.common.db.annotation.DataBaseField;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.db.DBFlowBase;
import core.server.ServerConfig;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChatLogFlow extends DBFlowBase {
    @DataBaseField(
        type = "int(11)",
        fieldname = "chatType",
        comment = "聊天类型"
    )
    private int chatType = 0;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "receivePId",
        comment = "接受者id"
    )
    private long receivePId = 0L;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "senderPId",
        comment = "发送者id"
    )
    private long senderPId = 0L;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "content",
        comment = "内容"
    )
    private String content = "";
    @DataBaseField(
        type = "int(11)",
        fieldname = "sendTime",
        comment = "发送时间"
    )
    private int sendTime = 0;
    @DataBaseField(
        type = "int(11)",
        fieldname = "serverId",
        comment = "服务器id"
    )
    private int serverId = 0;
    public static String TABLE_NAME = "ChatLog";

    public ChatLogFlow() {
    }

    public ChatLogFlow(int chatType, long receivePId, long senderPId, String content, int sendTime, int serverId) {
        this.chatType = chatType;
        this.receivePId = receivePId;
        this.senderPId = senderPId;
        this.content = content;
        this.sendTime = sendTime;
        this.serverId = serverId;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getInsertSql() {
        return "INSERT INTO ChatLog(`server_id`, `timestamp`, `date_time`, `chatType`, `receivePId`, `senderPId`, `content`, `sendTime`, `serverId`)values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public static String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS `ChatLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`chatType` int(11) NOT NULL DEFAULT '0' COMMENT '聊天类型',`receivePId` bigint(20) NOT NULL DEFAULT '0' COMMENT '接受者id',`senderPId` bigint(20) NOT NULL DEFAULT '0' COMMENT '发送者id',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '内容',`sendTime` int(11) NOT NULL DEFAULT '0' COMMENT '发送时间',`serverId` int(11) NOT NULL DEFAULT '0' COMMENT '服务器id',PRIMARY KEY (`id`)) COMMENT='活动操作日志表' DEFAULT CHARSET=utf8";
    }

    @Override
    public void addToBatch(PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(1, ServerConfig.ServerID());
        pstmt.setInt(2, CommTime.nowSecond());
        pstmt.setString(3, CommTime.getNowTimeStringYMD());
        pstmt.setInt(4, this.chatType);
        pstmt.setLong(5, this.receivePId);
        pstmt.setLong(6, this.senderPId);
        pstmt.setString(7, this.content);
        pstmt.setInt(8, this.sendTime);
        pstmt.setInt(9, this.serverId);
    }
}
