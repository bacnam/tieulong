package core.logger.flow.impl.db;

import com.zhonglian.server.common.db.annotation.DataBaseField;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.db.DBFlowBase;
import core.server.ServerConfig;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuildDonateChargeLogFlow
        extends DBFlowBase {
    public static String TABLE_NAME = "GuildDonateChargeLog";
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
    private long pid = 0L;
    @DataBaseField(type = "int(11)", fieldname = "vip_level", comment = "VIP等级")
    private int vip_level = 0;
    @DataBaseField(type = "int(11)", fieldname = "level", comment = "队伍等级")
    private int level = 0;
    @DataBaseField(type = "int(11)", fieldname = "reason", comment = "产生原因类型")
    private int reason = 0;
    @DataBaseField(type = "int(11)", fieldname = "num", comment = "数量")
    private int num = 0;
    @DataBaseField(type = "int(11)", fieldname = "cur_remainder", comment = "当前剩余")
    private int cur_remainder = 0;
    @DataBaseField(type = "int(11)", fieldname = "pre_value", comment = "前值")
    private int pre_value = 0;
    @DataBaseField(type = "int(11)", fieldname = "type", comment = "类型(1:获得,2:消耗)")
    private int type = 0;

    public GuildDonateChargeLogFlow() {
    }

    public GuildDonateChargeLogFlow(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        this.pid = pid;
        this.vip_level = vip_level;
        this.level = level;
        this.reason = reason;
        this.num = num;
        this.cur_remainder = cur_remainder;
        this.pre_value = pre_value;
        this.type = type;
    }

    public static String getCreateTableSQL() {
        String sql = "CREATE TABLE IF NOT EXISTS `GuildDonateChargeLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`vip_level` int(11) NOT NULL DEFAULT '0' COMMENT 'VIP等级',`level` int(11) NOT NULL DEFAULT '0' COMMENT '队伍等级',`reason` int(11) NOT NULL DEFAULT '0' COMMENT '产生原因类型',`num` int(11) NOT NULL DEFAULT '0' COMMENT '数量',`cur_remainder` int(11) NOT NULL DEFAULT '0' COMMENT '当前剩余',`pre_value` int(11) NOT NULL DEFAULT '0' COMMENT '前值',`type` int(11) NOT NULL DEFAULT '0' COMMENT '类型(1:获得,2:消耗)',PRIMARY KEY (`id`)) COMMENT='GuildDonate Charge 神装碎片消费日志' DEFAULT CHARSET=utf8";

        return sql;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getInsertSql() {
        return "INSERT INTO GuildDonateChargeLog(`server_id`, `timestamp`, `date_time`, `pid`, `vip_level`, `level`, `reason`, `num`, `cur_remainder`, `pre_value`, `type`)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public void addToBatch(PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(1, ServerConfig.ServerID());
        pstmt.setInt(2, CommTime.nowSecond());
        pstmt.setString(3, CommTime.getNowTimeStringYMD());
        pstmt.setLong(4, this.pid);
        pstmt.setInt(5, this.vip_level);
        pstmt.setInt(6, this.level);
        pstmt.setInt(7, this.reason);
        pstmt.setInt(8, this.num);
        pstmt.setInt(9, this.cur_remainder);
        pstmt.setInt(10, this.pre_value);
        pstmt.setInt(11, this.type);
    }
}

