package core.logger.flow.impl.db;

import com.zhonglian.server.common.db.annotation.DataBaseField;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.db.DBFlowBase;
import core.server.ServerConfig;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateRoleLogFlow
        extends DBFlowBase {
    public static String TABLE_NAME = "CreateRoleLog";
    @DataBaseField(type = "varchar(500)", fieldname = "openid", comment = "玩家openid")
    private String openid = "";
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
    private long pid = 0L;
    @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创角时间")
    private int createTime = 0;
    @DataBaseField(type = "varchar(500)", fieldname = "channel", comment = "渠道")
    private String channel = "";
    @DataBaseField(type = "int(11)", fieldname = "serverid", comment = "服务器id")
    private int serverid = 0;

    public CreateRoleLogFlow() {
    }

    public CreateRoleLogFlow(String openid, long pid, int createTime, String channel, int serverid) {
        this.openid = openid;
        this.pid = pid;
        this.createTime = createTime;
        this.channel = channel;
        this.serverid = serverid;
    }

    public static String getCreateTableSQL() {
        String sql = "CREATE TABLE IF NOT EXISTS `CreateRoleLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`openid` varchar(500) NOT NULL DEFAULT '' COMMENT '玩家openid',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创角时间',`channel` varchar(500) NOT NULL DEFAULT '' COMMENT '渠道',`serverid` int(11) NOT NULL DEFAULT '0' COMMENT '服务器id',PRIMARY KEY (`id`)) COMMENT='Create Role 创角日志' DEFAULT CHARSET=utf8";

        return sql;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getInsertSql() {
        return "INSERT INTO CreateRoleLog(`server_id`, `timestamp`, `date_time`, `openid`, `pid`, `createTime`, `channel`, `serverid`)values(?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public void addToBatch(PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(1, ServerConfig.ServerID());
        pstmt.setInt(2, CommTime.nowSecond());
        pstmt.setString(3, CommTime.getNowTimeStringYMD());
        pstmt.setString(4, this.openid);
        pstmt.setLong(5, this.pid);
        pstmt.setInt(6, this.createTime);
        pstmt.setString(7, this.channel);
        pstmt.setInt(8, this.serverid);
    }
}

